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

import java.math.BigDecimal;
import java.util.Locale;

/**
 * interface for SessionView context.
 *
 * @author Joern Muehlencord, 2021-12-26
 * @since 0.1.0
 */
public interface SessionView {

  /**
   * returns the userName of the loggedIn user.
   *
   * @return the userName of the loggedIn user
   */
  String getUserName();

  /**
   * returns the users locale as String
   *
   * @return the users locale as String
   */
  String getUserLocale();

  /**
   * returns the users Locale
   *
   * @return the users Locale
   */
  Locale getLocale();

  /**
   * returns the language part of the users Locale.
   *
   * @return the language part of the users Locale
   */
  String getUserLanguage();

  /**
   * returns the users date format to use.
   *
   * @return the users date format to use
   */
  String getUserDateFormat();

  /**
   * returns the localized boolean value - e.g. yes/no or ja/nein (German).
   *
   * @param value the boolean value to translate.
   * @return the localized boolean value.
   */
  String getLocalizedBoolean (boolean value);

  /**
   * returns the localized message, read from the i18n message bundle by the given id.
   *
   * @param id the id to translate
   * @return the localized message of the i18n id.
   */
  String getLocalizedMessage(String id);

  /**
   * returns the localized message, read from the i18n message bundle by the given id.
   *
   * @param id     the id to translate
   * @param params the objects to parse into the messages.
   * @return the localized message of the i18n id.
   */
  String getLocalizedMessage(String id, Object... params);

  /**
   * return a currency string of a given price
   *
   * @param price                   the value to format
   * @param currencyCode            the currency code to use (e.g. €)
   * @param currencySymbolRightSide if set to true, the code is placed on the right side (e. 10 €).
   *                                Otherwise, it s placed on the left side (e.g. $ 10)
   * @return the value as formated currency string.
   */
  String getCurrencyValue(BigDecimal price, String currencyCode, boolean currencySymbolRightSide);

  /**
   * returns true, if the user has the required change role.
   *
   * @param requiredChangeRole the role required to be able to edit the current page.
   * @return true, if the user has the required change role and can edit the page, false otherwise.
   */
  boolean canEdit(String requiredChangeRole);
}
