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

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * FacesValidator for email addresses .
 * @author Joern Muehlencord (joern@muehlencord.de)
 * @since 0.1.0
 */
@FacesValidator("de.muehlencord.shared.jeeutil.validator.EmailValidator")
public class EmailValidator implements Validator<String> {

  private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\."
    + "[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*"
    + "(\\.[A-Za-z]{2,})$";

  private final Pattern pattern;

  /**
   * create a new instance of  the emailValidator using the default email pattern.
   */
  public EmailValidator() {
    pattern = Pattern.compile(EMAIL_PATTERN);
  }


  @Override
  public void validate(FacesContext context, UIComponent component, String value) throws ValidatorException {
    Matcher matcher = pattern.matcher(value);
    if (!matcher.matches()) {
      FacesMessage msg = new FacesMessage("E-mail validation failed.", "Invalid E-mail format.");

      msg.setSeverity(FacesMessage.SEVERITY_ERROR);
      throw new ValidatorException(msg);
    }
  }

  /**
   * returns true, if the given email address is valid.
   * @param emailAddress the address to validate.
   * @return true, if the given email address is valid, false otherwise.
   */
  public boolean isValid(String emailAddress) {
    Matcher matcher = pattern.matcher(emailAddress);
    return matcher.matches();
  }

}
