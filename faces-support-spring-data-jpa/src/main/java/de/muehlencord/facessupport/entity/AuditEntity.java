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

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author Joern Muehlencord (joern@muehlencord.de)
 */
@Embeddable
@Getter
@Setter
public class AuditEntity implements Audit, Serializable {

  private static final long serialVersionUID = -955765069412891842L;

  @Basic(optional = false)
  @NotNull
  @Column(name = "valid_from")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime validFrom;

  @Column(name = "valid_to")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime validTo;

  @Basic(optional = false)
  @NotNull
  @Column(name = "created_on")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @CreationTimestamp
  @CreatedDate
  private LocalDateTime createdOn;

  @Basic(optional = false)
  @NotNull
  @Column(name = "created_by")
  @CreatedBy
  private String createdBy;

  @Basic(optional = false)
  @NotNull
  @Column(name = "last_updated_on")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @LastModifiedDate
  private LocalDateTime lastUpdatedOn;

  @Basic(optional = false)
  @NotNull
  @Column(name = "last_updated_by")
  @LastModifiedBy
  private String lastUpdatedBy;

  public AuditEntity() {
    this.validFrom = LocalDateTime.now(ZoneOffset.UTC);
    this.lastUpdatedOn = LocalDateTime.now(ZoneOffset.UTC);
    this.createdOn = LocalDateTime.now(ZoneOffset.UTC);
  }

  public AuditEntity(Audit audit) {
    validFrom = audit.getValidFrom();
    validTo = audit.getValidTo();
    createdOn = audit.getCreatedOn();
    createdBy = audit.getCreatedBy();
    lastUpdatedOn = audit.getLastUpdatedOn();
    lastUpdatedBy = audit.getLastUpdatedBy();
  }

  @Transient
  @JsonIgnore
  public boolean isValid() {
    return isValidAt(LocalDateTime.now());
  }

  @Transient
  public boolean isValidAt(LocalDate referenceDate) {
    LocalDateTime startReferenceDate = referenceDate.atStartOfDay();
    return isValidAt(startReferenceDate);
  }

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

  public AuditEntity withNewAudit(String userName) {
    LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
    this.setCreatedBy(userName);
    this.setCreatedOn(now);
    this.setLastUpdatedBy(userName);
    this.setLastUpdatedOn(now);
    this.setValidFrom(now);
    return this;
  }

  public AuditEntity withNewAudit(String userName, LocalDateTime validFrom) {
    LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
    this.setCreatedBy(userName);
    this.setCreatedOn(now);
    this.setLastUpdatedBy(userName);
    this.setLastUpdatedOn(now);
    this.setValidFrom(validFrom);
    return this;
  }

  public AuditEntity withEndDate() {
    return withEndDate(LocalDateTime.now(ZoneOffset.UTC));
  }

  public AuditEntity withEndDate(String userName) {
    this.validTo = LocalDateTime.now(ZoneOffset.UTC);
    this.lastUpdatedOn = LocalDateTime.now(ZoneOffset.UTC);
    this.lastUpdatedBy = userName;
    return this;
  }

  public AuditEntity withEndDate(String userName, LocalDateTime endDate) {
    this.validTo = endDate;
    this.lastUpdatedOn = LocalDateTime.now(ZoneOffset.UTC);
    this.lastUpdatedBy = userName;
    return this;
  }

  private AuditEntity withEndDate(LocalDateTime endDate) {
    this.setValidTo(endDate);
    return this;
  }

  public AuditEntity withValidFrom(final LocalDateTime validFrom) {
    this.validFrom = validFrom;
    return this;
  }

  public AuditEntity withValidTo(final LocalDateTime validTo) {
    this.validTo = validTo;
    return this;
  }

  public AuditEntity withCreatedOn(final LocalDateTime createdOn) {
    this.createdOn = createdOn;
    return this;
  }

  public AuditEntity withCreatedBy(final String createdBy) {
    this.createdBy = createdBy;
    return this;
  }

  public AuditEntity withLastUpdatedOn(final LocalDateTime lastUpdatedOn) {
    this.lastUpdatedOn = lastUpdatedOn;
    return this;
  }

  public AuditEntity withLastUpdatedBy(final String lastUpdatedBy) {
    this.lastUpdatedBy = lastUpdatedBy;
    return this;
  }

}
