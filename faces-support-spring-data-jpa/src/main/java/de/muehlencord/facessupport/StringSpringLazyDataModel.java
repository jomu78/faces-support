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
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * lazy data model bound to spring data jpa for entities with String based ids.
 * @param <T> the entity to support. It must use a String as ID field.
 * @param <R> the repository to use.
 *
 * @author Joern Muehlencord, 2025-08-17
 * @since 1.5.0
 */
public class StringSpringLazyDataModel<
  T extends IdentifiableEntity<String>,
  R extends JpaRepository<T, String> & JpaSpecificationExecutor<T>>
  extends ExtendedSpringDataJpaLazyDataModel<T, String, R> {

  /**
   * creates a new instance of data model, which supports entities with String as IDs.
   *
   * @param repository the repository to use,
   */
  public StringSpringLazyDataModel(R repository) {
    super(repository);
  }


}
