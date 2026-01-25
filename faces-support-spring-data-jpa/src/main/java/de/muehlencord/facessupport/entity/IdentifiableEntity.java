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

import de.muehlencord.facessupport.IdentifiableObject;
import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.domain.Persistable;

/**
 * base class entity, identified by field id which is of type T.
 * @param <T>  the domain type the ID.
 *
 * @author Joern Muehlencord, 2025-08-17
 * @since 0.1.0
 */
@MappedSuperclass
public abstract class IdentifiableEntity <T extends Serializable>
  implements Serializable, IdentifiableObject<T>, Persistable<T> {

  /**
   * new instance of an identifiable entity.
   */
  protected IdentifiableEntity() {
    // construct a new instance
  }

  /**
   * returns true, if the id is null and therefor edit has been called on a new entity.
   *
   * @return true if is a new entity, false otherwise.
   */
  @Override
  public boolean isNew() {
    return getId() == null;
  }

  /* *** equals / hashCode / toString *** */

  /**
   * string representation of the entity
   *
   * @return string representation of the entity
   */
  @Override
  public String toString() {
    return String.format("%s[id=%s]", getClass().getSimpleName(), getIdString());
  }


  public boolean hibernateAwareEquals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    Class<?> oEffectiveClass = o instanceof HibernateProxy hibernateProxy
      ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass()
      : o.getClass();

    Class<?> thisEffectiveClass = this instanceof HibernateProxy hibernateProxy
      ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass()
      : this.getClass();

    return thisEffectiveClass == oEffectiveClass;
  }

  @Override
  public int hashCode() {
    return this instanceof HibernateProxy hibernateProxy
      ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass().hashCode()
      : getClass().hashCode();
  }
}
