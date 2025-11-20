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

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.joinfaces.primefaces.SpringDataJpaLazyDataModel;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.MatchMode;

import java.util.List;

/**
 * extended filter meta to support IN predicates.
 *
 * @author Joern Muehlencord, 2025-09-11
 * @since 1.5.0
 */
public class ExtendedFilterMetaSpecification<T> extends SpringDataJpaLazyDataModel.FilterMetaSpecification<T> {

  private FilterMeta internalFilterMeta;

  public ExtendedFilterMetaSpecification(FilterMeta filterMeta) {
    super(filterMeta);
    this.internalFilterMeta = filterMeta;
  }

  @Override
  public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
    if (MatchMode.IN.equals(internalFilterMeta.getMatchMode())) {
      List<Object> filterValue = (List<Object>) internalFilterMeta.getFilterValue();
      Path<?> path = getPath(root, internalFilterMeta.getField());
      return path.in(filterValue);
    } else {
      return super.toPredicate(root, query, criteriaBuilder);
    }
  }
}
