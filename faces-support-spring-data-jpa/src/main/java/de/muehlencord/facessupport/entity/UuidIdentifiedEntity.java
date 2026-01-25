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

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

/**
 * entity with id field of type UUID.
 *
 * @author Joern Muehlencord, 2025-08-17
 * @since 0.1.0
 */
@Setter
@Getter
@MappedSuperclass
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public abstract class UuidIdentifiedEntity extends IdentifiableEntity<UUID> {

  /**
   * the id of the entity
   */
  @Id
  @Basic(optional = false)
  @NotNull
  @Column(name = "id")
  protected UUID id;

  /**
   * generate the id based on {@link UUID#randomUUID()}.
   */
  @Override
  public void generateId() {
    if (id == null) {
      id = UUID.randomUUID();
    }
  }

  @Override
  public boolean equals(Object o) {
    var hibernateAwareEquals = super.hibernateAwareEquals(o);
    if (!hibernateAwareEquals) {
      return false;
    }

    if (o instanceof UuidIdentifiedEntity that) {
      return Objects.equals(id, that.id);
    } else {
      return false;
    }
  }

}
