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

package de.muehlencord.facessupport.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * entity with id field of type integer.
 *
 * @author Joern Muehlencord, 2025-08-17
 * @since 0.1.0
 */
@MappedSuperclass
@Getter
@Setter
public class IntegerIdentifiedEntity extends IdentifiableEntity<Integer> implements Serializable {

  /**
   * the id of the entity
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  protected Integer id;

  @Override
  public boolean equals(Object o) {
    var hibernateAwareEquals = super.hibernateAwareEquals(o);
    if (!hibernateAwareEquals) {
      return false;
    }

    if (o instanceof IntegerIdentifiedEntity that) {
      return Objects.equals(id, that.id);
    } else {
      return false;
    }
  }

}
