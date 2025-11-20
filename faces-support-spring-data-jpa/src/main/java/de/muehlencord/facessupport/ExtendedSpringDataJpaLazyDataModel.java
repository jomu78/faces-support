/*
 * Copyright 2025, Joern Muehlencord
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package de.muehlencord.facessupport;

import de.muehlencord.facessupport.entity.IdentifiableEntity;
import org.joinfaces.primefaces.SpringDataJpaLazyDataModel;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * primefaces lazy dataModel bound to a spring data jpa repository.
 *
 * @author Joern Muehlencord, 2025-09-07
 * @since 1.5.0
 */
public class ExtendedSpringDataJpaLazyDataModel<
  T extends IdentifiableEntity<I>,
  I extends Serializable,
  R extends JpaRepository<T, I> & JpaSpecificationExecutor<T>>
  extends SpringDataJpaLazyDataModel<T, I, R> {

  private final Map<String, FilterMeta> defaultFilters = new HashMap<>();
  private final Map<String, Function<FilterMeta, Specification<T>>> customFilters = new HashMap<>();
  private final Map<String, List<Sort.Order>> customSort = new HashMap<>();

  private final transient R internalRepository;

  public ExtendedSpringDataJpaLazyDataModel(R repository) {
    super(repository);
    this.internalRepository = repository;
  }

  @Override
  public List<T> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
    // include defaultFilter into provided filters
    Map<String, FilterMeta> filters = new HashMap<>();
    if (!defaultFilters.isEmpty()) {
      filters.putAll(defaultFilters);
    }
    if (filterBy != null && !filterBy.isEmpty()) {
      filters.putAll(filterBy);
    }
    return super.load(first, pageSize, sortBy, filters);
  }

  @Nullable
  @Override
  protected Specification<T> getSpecification(Map<String, FilterMeta> filterBy) {
    if (CollectionUtils.isEmpty(filterBy)) {
      return null;
    }

    return filterBy.values().stream()
      .map(this::getSpecification)
      .reduce((a, b) -> Specification.where(a).and(b))
      .orElse(null);
  }

  @Override
  protected Specification<T> getSpecification(FilterMeta filterMeta) {
    if (customFilters.containsKey(filterMeta.getField())) {
      return customFilters.get(filterMeta.getField()).apply(filterMeta);
    }
    return new ExtendedFilterMetaSpecification<>(filterMeta);
  }

  @Override
  public int count(Map<String, FilterMeta> filterBy) {
    var finalFilterBy = new HashMap<String, FilterMeta>();
    if (filterBy != null) {
      finalFilterBy.putAll(filterBy);
    }
    finalFilterBy.putAll(defaultFilters);

    return super.count(finalFilterBy);
  }


  @Override
  protected Sort getSort(Map<String, SortMeta> sortBy) {
    if (CollectionUtils.isEmpty(sortBy)) {
      return Sort.unsorted();
    }

    List<Sort.Order> orders = sortBy.values().stream()
      .sorted(Comparator.comparing(SortMeta::getOrder))
      .map(sortMeta -> {
        Sort.Direction direction = getDirection(sortMeta);

        if (customSort.containsKey(sortMeta.getField())) {
          return customSort.get(sortMeta.getField());
        }

        Sort.Order order = Sort.Order.by(sortMeta.getField());

        if (direction != null) {
          order = order.with(direction);
        }

        if (!sortMeta.isCaseSensitiveSort()) {
          order = order.ignoreCase();
        }

        return List.of(order);
      }).flatMap(Collection::stream).toList();

    return Sort.by(orders);
  }

  static Sort.Direction getDirection(SortMeta sortMeta) {
    return getDirection(sortMeta.getOrder());
  }

  private static Sort.Direction getDirection(SortOrder order) {
    return switch (order) {
      case ASCENDING -> Sort.Direction.ASC;
      case DESCENDING -> Sort.Direction.DESC;
      default -> null;
    };
  }

  /* **** additional features **** */
  public void addCustomFilter(String key, Function<FilterMeta, Specification<T>> specFactory) {
    this.customFilters.put(key, specFactory);
  }

  public void addDefaultFilter(String key, FilterMeta filterMeta) {
    defaultFilters.put(key, filterMeta);
  }

  public void addCustomSort(String key, List<Sort.Order> orders) {
    customSort.put(key, orders);
  }


  /* **** custom repository methods **** */


  public T load(I id) {
    return internalRepository.findById(id).orElse(null);
  }

  public Optional<T> findById(I id) {
    return internalRepository.findById(id);
  }

  public T save(T element) {
    return internalRepository.save(element);
  }

  public void delete(T element) {
    internalRepository.delete(element);
  }

  @Deprecated
  public List<T> getAllElements() {
    return internalRepository.findAll();
  }
}
