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

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

/**
 * Helper class for java faces application.
 *
 * @author Joern Muehlencord (joern@muehlencord.de)
 */
public interface FacesUtil {
  

  /**
   * returns true, if the current request is in the request phase. False otherwise.
   *
   * @return true, if the current request is in the request phase. False otherwise.
   */
  default boolean isRenderRequest() {
    return !FacesContext.getCurrentInstance().getRenderResponse();
  }

  /**
   * returns true, if the current request is in the response phase. True otherwise.
   *
   * @return true, if the current request is in the response phase. True otherwise.
   */
  default boolean isRenderResponse() {
    return FacesContext.getCurrentInstance().getRenderResponse();
  }

  /**
   * Adds the given message to the object with the id specified by clientId.
   *
   * @param clientId the id of the object to bind the message to.
   * @param message the message to display
   */
  default void addMessage(String clientId, FacesMessage message) {
    FacesContext.getCurrentInstance().addMessage(clientId, message);
  }

  /**
   * Adds the given message to the object with the id specified by clientId.
   *
   * @param clientId the id of the object to bind the message to.
   * @param message the message to display
   * @param validationFailed if true, the validation is marked as failed in the FacesContext.
   */
  default void addMessage(String clientId, FacesMessage message, boolean validationFailed) {
    FacesContext.getCurrentInstance().addMessage(clientId, message);
    if (validationFailed) {
      FacesContext.getCurrentInstance().validationFailed();
    }
  }

  /**
   * Adds the given message as global message.
   *
   * @param message the message to display
   */
  default void addGlobalMessage(FacesMessage message) {
    FacesContext.getCurrentInstance().addMessage(null, message);
  }

  /**
   * Adds the given message as global message with severity "Information".
   *
   * @param summary the message to add
   */
  default void addGlobalInfoMessage(String summary) {
    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
    FacesContext.getCurrentInstance().addMessage(null, message);
  }

  /**
   * Adds the given message as global message with severity "Information".
   *
   * @param summary the summary message to display
   * @param detail the detailed message to display
   */
  default void addGlobalInfoMessage(String summary, String detail) {
    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
    FacesContext.getCurrentInstance().addMessage(null, message);
  }

  /**
   * Adds the given message as global message with severity "Warning".
   *
   * @param summary the summary message to display
   */
  default void addGlobalWarningMessage(String summary) {
    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, summary, null);
    FacesContext.getCurrentInstance().addMessage(null, message);
  }

  /**
   * Adds the given message as global message with severity "Warning".
   *
   * @param summary the summary message to display
   * @param detail the detailed message to display
   */
  default void addGlobalWarningMessage(String summary, String detail) {
    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, summary, detail);
    FacesContext.getCurrentInstance().addMessage(null, message);
  }

  /**
   * Adds the given message as global message with severity "Error".
   *
   * @param summary the message to display
   */
  default void addGlobalErrorMessage(String summary) {
    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
    FacesContext.getCurrentInstance().addMessage(null, message);
  }

  /**
   * Adds the given message as global message with severity "Error".
   *
   * @param summary the summary message to display
   * @param detail the detailed message to display
   */
  default void addGlobalErrorMessage(String summary, String detail) {
    addGlobalErrorMessage(summary, detail, false);
  }



  /**
   * Adds the given message as global message with severity "Error".
   *
   * @param summary the summary message to display
   * @param detail the detailed message to display
   * @param valiationFailed if set to true, the method calls currentInstance().validationFailed() to invalidate the current request. 
   */
  default void addGlobalErrorMessage(String summary, String detail, boolean valiationFailed) {
    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail);
    FacesContext.getCurrentInstance().addMessage(null, message);
    if (valiationFailed) {
      FacesContext.getCurrentInstance().validationFailed();
    }
  }

  /**
   * Adds the given message as global message with severity "Fatal".
   *
   * @param summary the message to display
   */
  default void addGlobalFatalMessage(String summary) {
    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, summary, null);
    FacesContext.getCurrentInstance().addMessage(null, message);
  }

  /**
   * Adds the given message as global message with severity "Fatal".
   *
   * @param summary the summary message to display
   * @param detail the detailed message to display
   */
  default void addGlobalFatalMessage(String summary, String detail) {
    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, summary, detail);
    FacesContext.getCurrentInstance().addMessage(null, message);
  }

  /**
   * Adds the given message with severity "Info" to the object with the id specified by clientId.
   *
   * @param clientId the id of the object to bind the message to.
   * @param summary the summary message to display
   * @param detail the detailed message to display
   */
  default void addInfoMessage(String clientId, String summary, String detail) {
    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
    FacesContext.getCurrentInstance().addMessage(clientId, message);
  }

  /**
   * Adds the given message with severity "Error" to the object with the id specified by clientId.
   *
   * @param clientId the id of the object to bind the message to.
   * @param summary the summary message to display
   * @param detail the detailed message to display
   */
  default void addErrorMessage(String clientId, String summary, String detail) {
    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail);
    FacesContext.getCurrentInstance().addMessage(clientId, message);
  }

  /**
   * adds the given message as an error message, clientId null.
   *
   * @param clientId  the id of the client to insert the message into. Typically, a p:message id=clientId.
   * @param summary          the summary of the message
   * @param detail           the detail of the message
   * @param validationFailed controls, whether the message should cause a validation failed exception or not
   */
  default void addErrorMessage(String clientId, String summary, String detail, boolean validationFailed) {
    addErrorMessage(clientId, summary, detail);
    if (validationFailed) {
      FacesContext.getCurrentInstance().validationFailed();
    }
  }

  /**
   * Adds the given message with severity "Warning" to the object with the id specified by clientId.
   *
   * @param clientId the id of the object to bind the message to.
   * @param summary the summary message to display
   * @param detail the detailed message to display
   */
  default void addWarningMessage(String clientId, String summary, String detail) {
    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, summary, detail);
    FacesContext.getCurrentInstance().addMessage(clientId, message);
  }

  /**
   * Adds the given message with severity "Fatal" to the object with the id specified by clientId.
   *
   * @param clientId the id of the object to bind the message to.
   * @param summary the summary message to display
   * @param detail the detailed message to display
   */
  default void addFatalMessage(String clientId, String summary, String detail) {
    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, summary, detail);
    FacesContext.getCurrentInstance().addMessage(clientId, message);
  }

}
