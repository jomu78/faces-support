package de.muehlencord.facessupport.demo.entity;

import de.muehlencord.facessupport.entity.LongIdentifiedEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * demo repository
 *
 * @author Joern Muehlencord, 2025-11-21
 * @since 0.1.0
 */
@Entity
@Getter
@Setter
public class Person extends LongIdentifiedEntity implements Serializable {


  /**
   * person firstName
   */
  private String firstName;

  /**
   * person lastName
   */
  private String lastName;
}
