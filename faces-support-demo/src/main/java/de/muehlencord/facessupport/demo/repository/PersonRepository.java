package de.muehlencord.facessupport.demo.repository;

import de.muehlencord.facessupport.demo.entity.Person;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

/**
 * demo repository
 *
 * @author Joern Muehlencord, 2025-11-21
 * @since 0.1.0
 */
public interface PersonRepository extends JpaRepositoryImplementation<Person, Long> {
}
