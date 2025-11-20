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

package de.muehlencord.facessupport.view;

import de.muehlencord.facessupport.FacesUtil;
import de.muehlencord.facessupport.SessionView;
import jakarta.inject.Inject;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * base view class
 *
 * @author Joern Muehlencord, 2022-08-31
 * @since 0.3.0
 */
public abstract class AbstractView implements Serializable, FacesUtil {

  private static final Logger logger = LoggerFactory.getLogger(AbstractView.class);

  public static final String ERROR = "error";
  public static final String INFO = "information";

  protected final SessionView sessionView;

  @Inject
  protected AbstractView(SessionView sessionView) {
    this.sessionView = sessionView;
  }

  protected void addI18nErrorMessage(String messageId, String i18ElementName, String value) {
    String summary = sessionView.getLocalizedMessage(ERROR);
    String i18nElement = sessionView.getLocalizedMessage(i18ElementName);
    String message = sessionView.getLocalizedMessage(messageId, i18nElement, value);
    addGlobalErrorMessage(summary, message, true);
  }

  protected void addI18nErrorMessage(String messageKey, Throwable ex, boolean validationFailed) {
    var summary = sessionView.getLocalizedMessage(messageKey);
    var message = ExceptionUtils.getRootCauseMessage(ex);
    if (logger.isDebugEnabled()) {
      logger.debug(ex.getMessage(), ex);
    }
    addGlobalErrorMessage(summary, message, validationFailed);
  }

  protected void addNotSelectedMessage(String i18ElementName) {
    String summary = sessionView.getLocalizedMessage(ERROR);
    String i18nElement = sessionView.getLocalizedMessage(i18ElementName);
    String message = sessionView.getLocalizedMessage("message_not_selected", i18nElement);
    addGlobalErrorMessage(summary, message, true);
  }

  protected StreamedContent getStreamedContent(
    String i18nString, String messageId, Path path, String localFileName, String contentType, String contentEncoding
  ) {
    String fileName = path.toString();
    long contentLength;

    try {
      contentLength = Files.size(path);
    } catch (IOException ex) {
      String summaryText = sessionView.getLocalizedMessage(ERROR);
      String i18nElement = sessionView.getLocalizedMessage(i18nString);
      String msg = sessionView.getLocalizedMessage(messageId, i18nElement, ExceptionUtils.getRootCauseMessage(ex));
      addGlobalErrorMessage(summaryText, msg);
      throw new UncheckedIOException(ex);
    }

    return DefaultStreamedContent
      .builder()
      .name(localFileName)
      .contentType(contentType)
      .contentLength(contentLength)
      .contentEncoding(contentEncoding)
      .stream(() -> {
        try {
          return new FileInputStream(fileName);
        } catch (FileNotFoundException ex) {
          String summaryText = sessionView.getLocalizedMessage(ERROR);
          String i18nElement = sessionView.getLocalizedMessage(i18nString);
          String msg = sessionView.getLocalizedMessage(messageId, i18nElement, ExceptionUtils.getRootCauseMessage(ex));
          addGlobalErrorMessage(summaryText, msg);
          throw new UncheckedIOException(ex);
        }
      })
      .build();
  }

}
