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

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * object identified by Long.
 *
 * @author Joern Muehlencord, 2025-08-17
 * @since 0.1.0
 */
@Getter
@Setter
public abstract class LongIdentifiedObject implements IdentifiableObject<Long>, Serializable {

  /**
   * the id of the element.
   */
  protected Long id;

  /**
   * returns the id element of the object.
   *
   * @return the id element of the object
   */

  @Override
  public Long getId() {
    return id;
  }
}
