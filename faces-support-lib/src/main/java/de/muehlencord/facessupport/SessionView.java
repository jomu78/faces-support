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

  String getUserName();

  String getUserLocale();

  Locale getLocale();

  String getUserLanguage();

  String getUserDateFormat();

  String getLocalizedBoolean (boolean value);

  String getLocalizedMessage(String id);

  String getLocalizedMessage(String id, Object... params);

  String getCurrencyValue(BigDecimal price, String currencyCode, boolean currencySymbolRightSide);

  boolean canEdit(String requiredChangeRole);
}
