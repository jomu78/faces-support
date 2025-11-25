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

  /**
   * error string
   */
  public static final String ERROR = "error";
  /**
   * information string
   */
  public static final String INFO = "information";

  /**
   * the sessionView to use
   */
  protected final SessionView sessionView;

  /**
   * creates a new instance of AbstractView
   *
   * @param sessionView the sessionView to use for i18 messages.
   */
  @Inject
  protected AbstractView(SessionView sessionView) {
    this.sessionView = sessionView;
  }

  /**
   * add an i18n error message to the face context.
   *
   * @param messageId      the id of the message to read from the resource bundle
   * @param i18ElementName the id of the element name in the i18n resource bundle
   * @param value          the parameter to be parsed into the message.
   */
  protected void addI18nErrorMessage(String messageId, String i18ElementName, String value) {
    String summary = sessionView.getLocalizedMessage(ERROR);
    String i18nElement = sessionView.getLocalizedMessage(i18ElementName);
    String message = sessionView.getLocalizedMessage(messageId, i18nElement, value);
    addGlobalErrorMessage(summary, message, true);
  }

  /**
   * add an i18n error message to the face context.
   *
   * @param messageId        the id of the message to read from the resource bundle
   * @param ex               the exception causing the error message
   * @param validationFailed if true, the validation is marked as failed in the faces context.
   */
  protected void addI18nErrorMessage(String messageId, Throwable ex, boolean validationFailed) {
    var summary = sessionView.getLocalizedMessage(messageId);
    var message = ExceptionUtils.getRootCauseMessage(ex);
    if (logger.isDebugEnabled()) {
      logger.debug(ex.getMessage(), ex);
    }
    addGlobalErrorMessage(summary, message, validationFailed);
  }

  /**
   * adds an error i18n message "xxx is not selected". The i18n resource bundle needs to have a message_not_selected
   *
   * @param i18ElementName the i18n element of the type to put into the error message.
   */
  protected void addNotSelectedMessage(String i18ElementName) {
    String summary = sessionView.getLocalizedMessage(ERROR);
    String i18nElement = sessionView.getLocalizedMessage(i18ElementName);
    String message = sessionView.getLocalizedMessage("message_not_selected", i18nElement);
    addGlobalErrorMessage(summary, message, true);
  }

  /**
   * return the content of the given file as StreamedContent. If the export fails, an i18n message based
   * on messageId und i18nString is generated to serve a proper error message.
   *
   * @param i18nString      the id of the type in the i18n in the message resource bundle.
   * @param messageId       the id of the error message to use
   * @param path            the file to export as StreamedContent
   * @param localFileName   the fileName to present in the browser when downloading the file
   * @param contentType     the contentType of the file
   * @param contentEncoding the contentEncoding of the file.
   * @return the content of the given file as StreamedContent
   */
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
