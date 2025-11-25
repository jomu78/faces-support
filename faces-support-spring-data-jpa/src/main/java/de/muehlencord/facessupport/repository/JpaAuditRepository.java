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
 * base class for a Jpa Repository for entity supporting {@link  de.muehlencord.facessupport.Auditable  Auditable}
 * @param <T>  the object to access with the repository, needs to implement
 *           {@link  de.muehlencord.facessupport.Auditable  Auditable}
 * @param <I> the type of the ID of the object
 *
 * @author Joern Muehlencord, 2020-11-01
 * @since 0.1.0
 */
@NoRepositoryBean
public interface JpaAuditRepository<T extends Auditable, I> extends JpaRepositoryImplementation<T, I> {

  /**
   * the logging object for this interface.
   */
  Logger logger = LoggerFactory.getLogger(JpaAuditRepository.class);

  /**
   * return all elements, which are valid NOW
   *
   * @return a list of all elements, which are valid NOW
   */
  default List<T> findAllActive() {
    return findAllActive(LocalDateTime.now(), Sort.unsorted());
  }

  /**
   * return all elements, wich are valid at the given timeStamp
   *
   * @param d the timestamp the entity needs to be valid at
   * @return the list of all elements.
   */
  default List<T> findAllActive(LocalDateTime d) {
    return findAllActive(d, Sort.unsorted());
  }

  /**
   * return a list of elements, which needs to be valid at the given timestamp
   *
   * @param d    the timestamp the element needs to be valid at
   * @param sort the Sort object to be applied to the list
   * @return a list of a elements, which are active at the given timestamp.
   */
  abstract List<T> findAllActive(LocalDateTime d, Sort sort);

  /**
   * set the validTo timestamp and the updatedBy value
   *
   * @param t        the element to updated
   * @param userName the user who executed the update
   * @return the updated entity with updated validTo, updatedOn and updatedBy values
   */
  @Transactional
  default T endDate(T t, String userName) {
    return endDateInternal(t, userName, LocalDateTime.now());
  }

  /**
   * set the validTo timestamp and the updatedBy value.
   *
   * @param t        the element to updated
   * @param userName the user who executed the update
   * @param dateTime the timestamp to update the validTo date to
   * @return the updated entity with updated validTo, updatedOn and updatedBy values
   */
  @Transactional
  default T endDate(T t, String userName, LocalDateTime dateTime) {
    return endDateInternal(t, userName, dateTime);
  }

  /**
   * set the validTo timestamp and the updatedBy value to the given values.
   *
   * @param t        the element to update
   * @param userName the userName to set the updatedBy value to
   * @param dateTime the timestamp to set the validTo date to
   * @return the updated entity with updated validTo, updatedOn and updatedBy values
   */
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

  /**
   * returns an unproxied value of all active elements
   *
   * @param d the timeStamp the element needs to valid at
   * @return an unproxied value of all active elements
   */
  default List<T> fullyLoadActive(LocalDateTime d) {
    var active = findAllActive(d);
    List<T> rv = new ArrayList<>();
    for (T t : active) {
      rv.add((T) Hibernate.unproxy(t));
    }

    return rv;
  }

}
