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

import jakarta.validation.metadata.ConstraintDescriptor;

import java.util.HashMap;
import java.util.Map;

/**
 * FIXME - broken ClientValidationConstraint from Primefaces 13 still uses javax.xx
 *
 * @author Joern Muehlencord (joern@muehlencord.de)
 */
public class EmailClientValidationConstraint { // {implements ClientValidationConstraint {

  public static final String MESSAGE_METADATA = "data-p-email-msg";

  //  @Override
  public Map<String, Object> getMetadata(ConstraintDescriptor<?> constraintDescriptor) {
    Map<String, Object> metadata = new HashMap<>();
    Map<?, ?> attrs = constraintDescriptor.getAttributes();
    Object message = attrs.get("message");
    if (message != null) {
      metadata.put(MESSAGE_METADATA, message);
    }

    return metadata;
  }

  //    @Override
  public String getValidatorId() {
    return Email.class.getSimpleName();
  }

}
