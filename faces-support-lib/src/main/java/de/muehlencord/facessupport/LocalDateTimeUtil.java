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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;

/**
 * util to handle LocalDateTime conversion.
 *
 * @author Joern Muehlencord, 2025-08-17
 * @since 0.1.0
 */
public class LocalDateTimeUtil  {

  private LocalDateTimeUtil() {

  }

  /**
   * Convert Date and LocalDate to LocalDateTime if needed.
   *
   * @param javaType      type of the entity
   * @param valueObjectIn object which probably must be converted
   * @param <T> the type to convert
   * @return original array of objects or array of LocalDateTime objects
   */
  public static <T> Object[] convertToLocalDateTimeIfNeeded(Class<T> javaType, Object[] valueObjectIn) {
    boolean voNotEmptyAndDate = valueObjectIn != null && valueObjectIn.length > 0 && valueObjectIn[0] instanceof Date;
    boolean voNotEmptyAndLocalDate =
      valueObjectIn != null && valueObjectIn.length > 0 && valueObjectIn[0] instanceof LocalDate;
    if (voNotEmptyAndDate && LocalDateTime.class.isAssignableFrom(javaType)) {
      return Arrays.stream(valueObjectIn).map(a -> convertDateToLocalDateTime((Date) a)).toArray();
    } else if (voNotEmptyAndLocalDate && LocalDateTime.class.isAssignableFrom(javaType)) {
      return Arrays.stream(valueObjectIn).map(a -> convertLocalDateToLocalDateTime((LocalDate) a)).toArray();
    }
    return valueObjectIn;
  }

  private static Object convertDateToLocalDateTime(Date date) {
    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
  }

  private static Object convertLocalDateToLocalDateTime(LocalDate date) {
    return date.atStartOfDay();
  }

  /**
   * Convert LocalDate to LocalDateTime
   *
   * @param o        object which probably must be converted
   * @param javaType type of the entity
   * @return original array of objects or array of LocalDateTime objects
   */
  public static Object convertLocalDateToLocalDateTime(Object o, Class<?> javaType) {
    if (LocalDateTime.class.isAssignableFrom(javaType) && o instanceof LocalDate localDate) {
      return localDate.atStartOfDay();
    }
    return o;
  }


  /**
   * calculate the last working day of the month. Saturdays are not handled as work days.
   *
   * @return the last working days of the month.
   */
  public static LocalDate getLastWorkingDayOfLastMonth() {
    return getLastWorkingDayOfLastMonth (LocalDate.now(), false);
  }

  /**
   * calculate the last working day of the month. Saturdays can be handled as working days or not.
   *
   * @param date                 the date to calculate the last working day of the month for.
   * @param saturdayIsWorkingDay if true, saturdays are handled as working days. If false, saturdays are
   *                             not handled as working days.
   * @return the last working day of the month, the given date belongs to.
   */
  public static LocalDate getLastWorkingDayOfLastMonth(LocalDate date, boolean saturdayIsWorkingDay) {
    int dayOfMonth = date.getDayOfMonth();

    LocalDate rv = LocalDate.from(date);
    rv = rv.minusDays (dayOfMonth);

    if (rv.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
      rv = rv.minusDays(1);
    }

    if (!saturdayIsWorkingDay && rv.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
      rv = rv.minusDays(1);
    }

    return rv;
  }

  /**
   * add an amount of working days to a given date. The amount of days must be positive, holidays are not
   * taken into account.
   *
   * @param date                 the date to start from
   * @param workdays             the amount of workdays to add. must be a positive value.
   * @param saturdayIsWorkingDay if true, saturdays are handled as work days, if set to false, not.
   * @return the calculated date, when adding workdays to the given date,
   */
  public static LocalDate addWorkingDays(LocalDate date, int workdays, boolean saturdayIsWorkingDay) {
    if (workdays < 1) {
      return date;
    }

    LocalDate result = date;
    int addedDays = 0;
    while (addedDays < workdays) {
      result = result.plusDays(1);
      if (result.getDayOfWeek() == DayOfWeek.SUNDAY) {
        // sundays are not counted
      } else if (result.getDayOfWeek() == DayOfWeek.SATURDAY && !saturdayIsWorkingDay) {
        // if configured, saturday is also not counted
      } else {
        addedDays++;
      }
    }

    return result;
  }
}
