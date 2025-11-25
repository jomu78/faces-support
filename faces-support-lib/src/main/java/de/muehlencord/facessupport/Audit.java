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

  /**
   * set the validFrom value
   *
   * @param validFrom the value to set
   */
  void setValidFrom(LocalDateTime validFrom);

  /**
   * returns the validFrom date
   * @return the validFrom date
   */
  LocalDateTime getValidFrom();

  /**
   * set the valueFrom value and return the updated Audit object
   *
   * @param validFrom the value to set
   * @return the updated Audit object
   */
  Audit withValidFrom(LocalDateTime validFrom);

  /**
   * set the valueToVaue
   *
   * @param validTo the value to set
   */
  void setValidTo(LocalDateTime validTo);

  /**
   * returns the validTo value
   *
   * @return the current validTo value
   */
  LocalDateTime getValidTo();

  /**
   * update thw validTo date to the given date and return the updated Audit object.
   *
   * @param validTo the value to set
   * @return the updated Audit object
   */
  Audit withValidTo(LocalDateTime validTo);

  /**
   * set the validTo date to NOW
   *
   * @return the updated Audit object
   */
  Audit withEndDate();

  /**
   * set the validTo date to NOW and the updatedBy
   *
   * @param userName the value to set
   * @return the updated Audit object
   */
  Audit withEndDate(String userName);

  /**
   * set the validTo date to the given endDate and the updatedBy userName
   * @param userName the value to set updatedBy to
   * @param endDate the value to set validTo to
   * @return the updated Audit object
   */
  Audit withEndDate(String userName, LocalDateTime endDate);

  /**
   * set the createdOn value
   * @param createdOn the value to set
   */
  void setCreatedOn(LocalDateTime createdOn);

  /**
   * returns the createdOn value
   * @return the createdOn value
   */
  LocalDateTime getCreatedOn();

  /**
   * update the createdOn value.
   * @param createdOn the value to set
   * @return the updated Audit object
   */
  Audit withCreatedOn(LocalDateTime createdOn);

  /**
   * set the createdBy value to the given name
   * @param name the value to set createdBy to
   */
  void setCreatedBy(String name);

  /**
   * returns the createdBy value
   * @return the createdBy value
   */
  String getCreatedBy();

  /**
   * set the createdBy value to the given name.
   * @param name the value to set
   * @return the updated Audit object
   */
  Audit withCreatedBy(String name);

  /**
   * set the lastUpdatedOn value
   * @param lastUpdatedOn the value to set
   */
  void setLastUpdatedOn(LocalDateTime lastUpdatedOn);

  /**
   * returns the lastUpdatedOn value
   * @return the lastUpdatedOn value
   */
  LocalDateTime getLastUpdatedOn();

  /**
   * update the lastUpdatedOn value
   * @param lastUpdatedOn the value to set
   * @return the updated Audit object
   */
  Audit withLastUpdatedOn(LocalDateTime lastUpdatedOn);

  /**
   * update the lastUpdatedBy value
   * @param name the value to set
   */
  void setLastUpdatedBy(String name);

  /**
   * returns the lastUpdatedBy value
   * @return the lastUpdatedBy value
   */
  String getLastUpdatedBy();

  /**
   * update the lastUpdatedBy value to the given name.
   * @param name the value to set
   * @return the updated Audit object
   */
  Audit withLastUpdatedBy(String name);

}
