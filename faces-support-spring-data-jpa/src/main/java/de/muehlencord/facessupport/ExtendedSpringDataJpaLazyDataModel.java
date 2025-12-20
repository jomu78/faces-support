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
import org.jspecify.annotations.NonNull;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
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
 * @param <T>  the entity to read with the dataModel
 * @param <I>  the type of the ID of the entity T to read
 * @param <R> the repository to access the database with
 *
 * @author Joern Muehlencord, 2025-09-07
 * @since 1.5.0
 */
public class ExtendedSpringDataJpaLazyDataModel<
  T extends IdentifiableEntity<I>,
  I extends Serializable,
  R extends JpaRepository<T, I> & JpaSpecificationExecutor<T>>
  extends SpringDataJpaLazyDataModel<T, I, R> {

  /**
   * default filters applied to any request
   */
  private final Map<String, FilterMeta> defaultFilters = new HashMap<>();
  /**
   * custom filter implementations
   */
  private final Map<String, Function<FilterMeta, Specification<T>>> customFilters = new HashMap<>();
  /**
   * custom sort implementations
   */
  private final Map<String, List<Sort.Order>> customSort = new HashMap<>();

  /**
   * the repository to use. Since repository in SpringDataJpaLazyDataModel is not protected, we need
   * to keep our own reference.
   */
  private final transient R internalRepository;

  /**
   * creates a new instance using the given repository,
   *
   * @param repository the repository to use.
   */
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

  @NonNull
  @Override
  protected Specification<T> getSpecification(Map<String, FilterMeta> filterBy) {
    if (CollectionUtils.isEmpty(filterBy)) {
      return Specification.unrestricted();
    }

    return filterBy.values().stream()
      .map(this::getSpecification)
      .reduce(Specification::and)
      .orElse( Specification.unrestricted());
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

  /**
   * apply a custom filter for the given key,
   * @param key the key to apply the custom filter on
   * @param specFactory the specification factory to apply.
   */
  public void addCustomFilter(String key, Function<FilterMeta, Specification<T>> specFactory) {
    this.customFilters.put(key, specFactory);
  }

  /**
   * apply a default filter for the given key.
   *
   * @param key        the key to apply the filter to
   * @param filterMeta the filterMeta to apply
   */
  public void addDefaultFilter(String key, FilterMeta filterMeta) {
    defaultFilters.put(key, filterMeta);
  }

  /**
   * add a custom sort for the given key.
   *
   * @param key    the key to apply the custom sort on
   * @param orders the sort to apply.
   */
  public void addCustomSort(String key, List<Sort.Order> orders) {
    customSort.put(key, orders);
  }


  /* **** custom repository methods **** */

  /**
   * find the element by the given id
   *
   * @param id the id of the element to search for
   * @return the given element if found, null otherwise
   */
  public T load(I id) {
    return internalRepository.findById(id).orElse(null);
  }

  /**
   * find the element by the given id
   * @param id the id of the element to search for
   * @return the entity with the given id or {@literal Optional#empty()} if none found.
   */
  public Optional<T> findById(I id) {
    return internalRepository.findById(id);
  }

  /**
   * save the given element
   * @param element the element to save
   * @return the updated element
   */
  public T save(T element) {
    return internalRepository.save(element);
  }

  /**
   * delete the given element
   * @param element the element to delete
   */
  public void delete(T element) {
    internalRepository.delete(element);
  }

  /**
   * returns a list of all elements (without pagination, no lazy loading)
   *
   * @return a list of all elements.
   * @deprecated use {@link de.muehlencord.facessupport.ExtendedSpringDataJpaLazyDataModel#load(int, int, Map, Map)}
   * instead.
   */
  @Deprecated(forRemoval = true)
  public List<T> getAllElements() {
    return internalRepository.findAll();
  }
}
