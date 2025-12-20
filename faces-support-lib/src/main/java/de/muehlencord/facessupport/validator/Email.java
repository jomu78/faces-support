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
package de.muehlencord.facessupport.validator;

import jakarta.validation.Constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * annotation to mark a field as email with automatic email validation constraint.
 *
 * @author Joern Muehlencord (joern@muehlencord.de)
 * @since 0.1.0
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = EmailConstraintValidator.class)
// FIXME - broken ClientValidationConstraint from Primefaces 13 still uses javax.xx
//@ClientConstraint(resolvedBy = EmailClientValidationConstraint.class)
@Documented
public @interface Email {

  /**
   * returns the message to return if the validation fails.
   *
   * @return the message if validation fails.
   */
  String message() default "{de.muehlencord.shared.jeeutil.validator.Email}";

}
