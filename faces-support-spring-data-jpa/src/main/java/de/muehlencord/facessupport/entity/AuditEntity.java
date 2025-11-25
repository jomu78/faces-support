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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import de.muehlencord.facessupport.Audit;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Entity implementation of {@link  de.muehlencord.facessupport.Audit  Audit}
 *
 * @author Joern Muehlencord (joern@muehlencord.de)
 * @since 0.1.0
 */
@Embeddable
@Getter
@Setter
public class AuditEntity implements Audit, Serializable {

  @Serial
  private static final long serialVersionUID = -955765069412891842L;

  /**
   * the start date the entity is valid from
   */
  @Basic(optional = false)
  @NotNull
  @Column(name = "valid_from")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime validFrom;

  /**
   * the end date the entity is valid to
   */
  @Column(name = "valid_to")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime validTo;

  /**
   * the timestamp the entity was created on
   */
  @Basic(optional = false)
  @NotNull
  @Column(name = "created_on")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @CreationTimestamp
  @CreatedDate
  private LocalDateTime createdOn;

  /**
   * the user the entity was created by
   */
  @Basic(optional = false)
  @NotNull
  @Column(name = "created_by")
  @CreatedBy
  private String createdBy;

  /**
   * the timestamp the entity was updated at last on
   */
  @Basic(optional = false)
  @NotNull
  @Column(name = "last_updated_on")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @LastModifiedDate
  private LocalDateTime lastUpdatedOn;

  /**
   * the user the entity was updated by
   */
  @Basic(optional = false)
  @NotNull
  @Column(name = "last_updated_by")
  @LastModifiedBy
  private String lastUpdatedBy;

  /**
   * create a new instance valid from now.
   */
  public AuditEntity() {
    this.validFrom = LocalDateTime.now(ZoneOffset.UTC);
    this.lastUpdatedOn = LocalDateTime.now(ZoneOffset.UTC);
    this.createdOn = LocalDateTime.now(ZoneOffset.UTC);
  }

  /**
   * creates a new (depp clone) instance based on the given audit
   *
   * @param audit the audit to clone from.
   */
  public AuditEntity(Audit audit) {
    validFrom = audit.getValidFrom();
    validTo = audit.getValidTo();
    createdOn = audit.getCreatedOn();
    createdBy = audit.getCreatedBy();
    lastUpdatedOn = audit.getLastUpdatedOn();
    lastUpdatedBy = audit.getLastUpdatedBy();
  }

  /**
   * returns true, if the element is still valid at the current system time (NOW).
   *
   * @return true, if the element is still valid at the current system time (NOW), false otherwise.
   */
  @Transient
  @JsonIgnore
  public boolean isValid() {
    return isValidAt(LocalDateTime.now());
  }

  /**
   * returns true, if the element is valid at the start of the given day.
   *
   * @param referenceDate the date the validity is evaluated for.
   * @return true, if the element is still valid at the given date (start of day), false otherwise.
   */
  @Transient
  public boolean isValidAt(LocalDate referenceDate) {
    LocalDateTime startReferenceDate = referenceDate.atStartOfDay();
    return isValidAt(startReferenceDate);
  }

  /**
   * returns true, if the element is valid at the given dateTime.
   *
   * @param referenceDate the date the validity is evaluated for.
   * @return true, if the element is still valid at the given dateTime, false otherwise.
   */
  @Transient
  public boolean isValidAt(LocalDateTime referenceDate) {
    if (validFrom.isAfter(referenceDate)) {
      return false;
    }

    if (validTo == null) {
      return true;
    } else {
      return validTo.isAfter(referenceDate);
    }
  }

  /* **** fluent api **** */

  /**
   * fluent setter to create a new entity with NOW as validFrom date
   *
   * @param userName the use who created the user
   * @return the updated AuditEntity
   */
  public AuditEntity withNewAudit(String userName) {
    LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
    this.setCreatedBy(userName);
    this.setCreatedOn(now);
    this.setLastUpdatedBy(userName);
    this.setLastUpdatedOn(now);
    this.setValidFrom(now);
    return this;
  }

  /**
   * fluent setter to create a new entity with the given validFrom timestamp
   *
   * @param userName  the use who created the user
   * @param validFrom the timestamp, the audit is valid from
   * @return the updated AuditEntity
   */
  public AuditEntity withNewAudit(String userName, LocalDateTime validFrom) {
    LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
    this.setCreatedBy(userName);
    this.setCreatedOn(now);
    this.setLastUpdatedBy(userName);
    this.setLastUpdatedOn(now);
    this.setValidFrom(validFrom);
    return this;
  }

  /**
   * end date the entity by setting the validTo and updatedOn to NOW,
   *
   * @return the updated AuditEntity.
   */
  public AuditEntity withEndDate() {
    return withEndDate(LocalDateTime.now(ZoneOffset.UTC));
  }

  /**
   * end date the entity by setting the validTo and updatedOn to NOW,
   *
   * @param userName the value to set updatedBy to
   * @return the updated AuditEntity.
   */
  public AuditEntity withEndDate(String userName) {
    this.validTo = LocalDateTime.now(ZoneOffset.UTC);
    this.lastUpdatedOn = LocalDateTime.now(ZoneOffset.UTC);
    this.lastUpdatedBy = userName;
    return this;
  }

  /**
   * end date the entity by setting the validTo date to the given endDate
   *
   * @param userName the value to set updatedBy to
   * @param endDate  the date to set the validTo timestamp to
   * @return the updated AuditEntity.
   */
  public AuditEntity withEndDate(String userName, LocalDateTime endDate) {
    this.validTo = endDate;
    this.lastUpdatedOn = LocalDateTime.now(ZoneOffset.UTC);
    this.lastUpdatedBy = userName;
    return this;
  }

  /**
   * end date the entity by setting the validTo date to the given endDate
   *
   * @param endDate the date to set the validTo timestamp to
   * @return the updated AuditEntity.
   */
  private AuditEntity withEndDate(LocalDateTime endDate) {
    this.setValidTo(endDate);
    return this;
  }

  /**
   * update the validFrom timestamp to the given timeStamp
   *
   * @param validFrom the timestamp to set the validFrom timestamp to
   * @return the updated AuditEntity.
   */
  public AuditEntity withValidFrom(final LocalDateTime validFrom) {
    this.validFrom = validFrom;
    return this;
  }

  /**
   * update the validTo timestamp to the given timeStamp
   *
   * @param validTo the date to set the validTo timestamp to
   * @return the updated AuditEntity.
   */
  public AuditEntity withValidTo(final LocalDateTime validTo) {
    this.validTo = validTo;
    return this;
  }

  /**
   * update the createOn timeStamp to the given timestamp
   *
   * @param createdOn the timestamp to set createOn to
   * @return the updated AuditEntity.
   */
  public AuditEntity withCreatedOn(final LocalDateTime createdOn) {
    this.createdOn = createdOn;
    return this;
  }

  /**
   * update the createBy value to the given value
   *
   * @param createdBy the value to set createdBy to
   * @return the updated AuditEntity.
   */
  public AuditEntity withCreatedBy(final String createdBy) {
    this.createdBy = createdBy;
    return this;
  }

  /**
   * update the lastUpdatedOn timeStamp to the given timestamp
   *
   * @param lastUpdatedOn the timestamp to set lastUpdatedOn to
   * @return the updated AuditEntity.
   */

  public AuditEntity withLastUpdatedOn(final LocalDateTime lastUpdatedOn) {
    this.lastUpdatedOn = lastUpdatedOn;
    return this;
  }

  /**
   * update the lastUpdatedBy value to the given value
   *
   * @param lastUpdatedBy the value to set lastUpdatedBy to
   * @return the updated AuditEntity.
   */
  public AuditEntity withLastUpdatedBy(final String lastUpdatedBy) {
    this.lastUpdatedBy = lastUpdatedBy;
    return this;
  }

}
