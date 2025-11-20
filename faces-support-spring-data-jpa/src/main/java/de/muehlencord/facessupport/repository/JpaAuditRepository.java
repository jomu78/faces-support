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

package de.muehlencord.facessupport.repository;

import de.muehlencord.facessupport.Auditable;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Joern Muehlencord, 2020-11-01
 * @since 0.1.0
 */
@NoRepositoryBean
public interface JpaAuditRepository<T extends Auditable, I> extends JpaRepositoryImplementation<T, I> {

  Logger logger = LoggerFactory.getLogger(JpaAuditRepository.class);

  default List<T> findAllActive() {
    return findAllActive(LocalDateTime.now(), Sort.unsorted());
  }

  default List<T> findAllActive(LocalDateTime d) {
    return findAllActive(d, Sort.unsorted());
  }

  abstract List<T> findAllActive(LocalDateTime d, Sort sort);

  @Transactional
  default T endDate(T t, String userName) {
    return endDateInternal(t, userName, LocalDateTime.now());
  }

  @Transactional
  default T endDate(T t, String userName, LocalDateTime dateTime) {
    return endDateInternal(t, userName, dateTime);
  }

  default T endDateInternal(T t, String userName, LocalDateTime dateTime) {
    logger.debug("{} endDated at {} by {}", t, dateTime, userName);
    if (t.getAudit().getValidTo() == null) {
      t.getAudit()
        .withEndDate(userName, dateTime)
        .withLastUpdatedBy(userName)
        .withLastUpdatedOn(LocalDateTime.now());
      return save(t);
    } else {
      return t;
    }
  }

  default List<T> fullyLoadActive(LocalDateTime d) {
    var active = findAllActive(d);
    List<T> rv = new ArrayList<>();
    for (T t : active) {
      rv.add((T) Hibernate.unproxy(t));
    }

    return rv;
  }

}
