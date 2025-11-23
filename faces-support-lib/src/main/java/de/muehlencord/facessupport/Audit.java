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

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * audit for a given object.
 *
 * @author Joern Muehlencord, 2025-08-19
 * @since 0.1.0
 */
public interface Audit extends Serializable {

  void setValidFrom(LocalDateTime validFrom);

  LocalDateTime getValidFrom();

  Audit withValidFrom(LocalDateTime validFrom);

  void setValidTo(LocalDateTime validTo);

  LocalDateTime getValidTo();

  Audit withValidTo(LocalDateTime validTo);

  Audit withEndDate();

  Audit withEndDate(String userName);

  Audit withEndDate(String userName, LocalDateTime endDate);

  void setCreatedOn(LocalDateTime createdOn);

  LocalDateTime getCreatedOn();

  Audit withCreatedOn(LocalDateTime createdOn);

  void setCreatedBy(String name);

  String getCreatedBy();

  Audit withCreatedBy(String name);

  void setLastUpdatedOn(LocalDateTime lastUpdatedOn);

  LocalDateTime getLastUpdatedOn();

  Audit withLastUpdatedOn(LocalDateTime lastUpdatedOn);

  void setLastUpdatedBy(String name);

  String getLastUpdatedBy();

  Audit withLastUpdatedBy(String name);

}
