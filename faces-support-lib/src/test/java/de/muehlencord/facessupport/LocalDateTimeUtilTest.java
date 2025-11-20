package de.muehlencord.facessupport;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

/**
 * the LocalDateTimeUtil.
 *
 * @author Joern Muehlencord, 2025-08-17
 * @since 0.1.0
 */
class LocalDateTimeUtilTest {

  @Test
  void getLastWorkingDayOfLastMonth() {

    LocalDate referenceDate = LocalDate.of (2021, 04, 18);
    LocalDate testDate = LocalDateTimeUtil.getLastWorkingDayOfLastMonth(referenceDate, false);
    Assertions.assertThat(testDate).isEqualTo(LocalDate.of(2021, 03, 31));

    referenceDate = LocalDate.of (2021, 03, 01);
    testDate = LocalDateTimeUtil.getLastWorkingDayOfLastMonth(referenceDate, false);
    Assertions.assertThat(testDate).isEqualTo(LocalDate.of(2021, 02, 26));

    referenceDate = LocalDate.of (2021, 03, 01);
    testDate = LocalDateTimeUtil.getLastWorkingDayOfLastMonth(referenceDate, true);
    Assertions.assertThat(testDate).isEqualTo(LocalDate.of(2021, 02, 27));
  }

}
