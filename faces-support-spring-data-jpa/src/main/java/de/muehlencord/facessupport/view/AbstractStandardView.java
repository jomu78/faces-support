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

import de.muehlencord.facessupport.Auditable;
import de.muehlencord.facessupport.ExtendedSpringDataJpaLazyDataModel;
import de.muehlencord.facessupport.FacesUtil;
import de.muehlencord.facessupport.SessionView;
import de.muehlencord.facessupport.StandardView;
import de.muehlencord.facessupport.entity.AuditEntity;
import de.muehlencord.facessupport.entity.IdentifiableEntity;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.event.ActionEvent;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * abstract class as base for a backing bean supporting CRUD operations.
 * @param <T> the entity type this view supports
 * @param <I> the type of the primary key of the entity shown in this view
 * @param <R> the repository to access the database with
 *
 * @author Joern Muehlencord 2020-05-24
 */
public abstract class AbstractStandardView<
  T extends IdentifiableEntity<I>,
  I extends Serializable,
  R extends JpaRepository<T, I> & JpaSpecificationExecutor<T>>
    extends AbstractView
    implements StandardView<T,I>, Serializable, FacesUtil {

  /**
   * the logger
   */
  private static final Logger logger = LoggerFactory.getLogger(AbstractStandardView.class);

  /**
   * the dataModel used
   */
  protected ExtendedSpringDataJpaLazyDataModel<T, I, R> dataModel;
  /**
   * the list of sortBy arguments
   */
  private final List<SortMeta> sortBy = new ArrayList<>();

  /**
   * all elements without lazy loading
   */
  protected transient List<T> allElements = null;
  /**
   * the element currently on edit
   */
  protected T editElement = null;
  /**
   * the element currently selected in the dataTable
   */
  protected T selectedElement = null;

  /**
   * the id to access the i18n message resource bundle. used to create i18n message and translate the type
   */
  private final String i18nId;
  /**
   * the java representation of the element type.
   */
  private final Class<T> clazz;

  /**
   * creates a new instance of the AbstractStandardView
   *
   * @param i18nId        the id of the type in the i18n resource bundle. Used in the session view to create i18n messages
   * @param sessionView   the sessionView object used to translate messages
   * @param lazyDataModel the dataModel used to access the data
   * @param clazz         the class of the type to handle
   */
  protected AbstractStandardView(
    String i18nId,
    SessionView sessionView, ExtendedSpringDataJpaLazyDataModel<T, I, R> lazyDataModel,
    Class<T> clazz
  ) {
    super(sessionView);
    this.i18nId = i18nId;
    this.dataModel = lazyDataModel;
    this.clazz = clazz;
  }

  /**
   * creates a new instance of the AbstractStandardView
   *
   * @param i18nId        the id of the type in the i18n resource bundle. Used in the session view to create i18n messages
   * @param sessionView   the sessionView object used to translate messages
   * @param lazyDataModel the dataModel used to access the data
   * @param clazz         the class of the type to handle
   * @param sortFields    the names of the fields to sort by. (all ascending)
   */
  protected AbstractStandardView(
    String i18nId,
    SessionView sessionView, ExtendedSpringDataJpaLazyDataModel<T, I, R> lazyDataModel,
    Class<T> clazz,
    String... sortFields
  ) {
    super(sessionView);
    this.i18nId = i18nId;
    this.dataModel = lazyDataModel;
    this.clazz = clazz;

    for (String sortField : sortFields) {
      sortBy.add(SortMeta.builder()
        .field(sortField)
        .order(SortOrder.ASCENDING)
        .build());
    }
  }

  /**
   * creates a new instance of the AbstractStandardView
   *
   * @param i18nId        the id of the type in the i18n resource bundle. Used in the session view to create i18n messages
   * @param sessionView   the sessionView object used to translate messages
   * @param lazyDataModel the dataModel used to access the data
   * @param clazz         the class of the type to handle
   * @param sortBy        the list of sort elements.
   */
  protected AbstractStandardView(
    String i18nId,
    SessionView sessionView, ExtendedSpringDataJpaLazyDataModel<T, I, R> lazyDataModel,
    Class<T> clazz,
    List<SortMeta> sortBy
  ) {
    super(sessionView);
    this.i18nId = i18nId;
    this.dataModel = lazyDataModel;
    this.clazz = clazz;
    this.sortBy.addAll(sortBy);
  }

  /**
   * returns the required role a use needs to have, to edit the page. By default, the method returns null which means
   * the user can edit the page. This function needs to be overwritten, if you want to make use of this feature.
   * See also {@link  AbstractStandardView#getCanEdit() getCanEdit}
   *
   * @return the role the use needs to have to be able to edit the page. null, if a use can edit the page
   * without any special role required.
   */
  @Override
  public String getRequiredChangeRole() {
    return null;
  }

  /**
   * start create a new element (in memory) and put this new element in edit mode.
   */
  @Override
  public void startAdd() {
    try {
      editElement = clazz.getConstructor().newInstance();

      if (editElement instanceof Auditable auditable) {
        auditable.setAudit(new AuditEntity().withNewAudit(sessionView.getUserName()));
      }
    } catch (Exception ex) {
      addGlobalErrorMessage("Error creating new instance", ex.getMessage());
    }
  }

  /**
   * open the selected element in edit mode.
   */
  @Override
  public void startEdit() {
    if (selectedElement == null) {
      addGlobalErrorMessage("Please select an element first");
    } else {
      editElement = dataModel.load(selectedElement.getId());
    }
  }

  /**
   * save the element currently in edit mode.
   *
   * @param ae the event causing this method to be called.
   */
  @Override
  public void saveEdit(ActionEvent ae) {
    saveEdit();
  }

  /**
   * save the element currently in edit mode.
   */
  @Override
  public void saveEdit() {
    try {
      if (editElement != null) {
        if (editElement.getId() == null) {
          editElement.generateId();
        }
        if (editElement instanceof Auditable auditable) {
          if (auditable.getAudit() == null) {
            // create new audit if no audit exists
            auditable.setAudit(new AuditEntity().withNewAudit(sessionView.getUserName()));
          }
        }

        editElement = dataModel.save(editElement);

        // force reload
        allElements = null;

        selectedElement = dataModel.load(editElement.getId());
        editElement = null;
      }
    } catch (RuntimeException ex) {
      String summary = sessionView.getLocalizedMessage(AbstractView.ERROR);
      String i18nElement = sessionView.getLocalizedMessage(i18nId);
      String msg = sessionView.getLocalizedMessage("message_failed_to_save", i18nElement, ExceptionUtils.getRootCauseMessage(ex));
      FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, msg);
      addMessage("editDialogMessages", facesMessage, true);
    }
  }

  /**
   * cancel the edit mode for the current element and revert it back into the status it was loaded from (startEdit) or
   * discard it completely if it was created as new (startAdd).
   *
   * @param ae the event causing this method to be called.
   */
  public void cancelEdit(ActionEvent ae) {
    cancelEdit();
  }

  /**
   * cancel the edit mode for the current element and revert it back into the status it was loaded from (startEdit) or
   * discard it completely if it was created as new (startAdd).
   */
  @Override
  public void cancelEdit() {
    editElement = null;
  }

  /**
   * delete the currently selected element. If the element implements {@link de.muehlencord.facessupport.Auditable}
   * the element not deleted but an endDate is set
   */
  @Override
  public void delete() {
    if (selectedElement == null) {
      String summary = sessionView.getLocalizedMessage(AbstractView.ERROR);
      String i18nElement = sessionView.getLocalizedMessage(i18nId);
      String message = sessionView.getLocalizedMessage("message_not_selected", i18nElement);
      addGlobalErrorMessage(summary, message);
    } else {
      try {
        if (selectedElement instanceof Auditable auditable) {
          auditable.getAudit().withEndDate();
          dataModel.save(selectedElement);
        } else {
          dataModel.delete(selectedElement);
        }

        selectedElement = null;
        editElement = null;
        // force reload
        allElements = null;
      } catch (RuntimeException ex) {
        String summary = sessionView.getLocalizedMessage(AbstractView.ERROR);
        String message = sessionView.getLocalizedMessage("message_failed_to_delete", i18nId, ExceptionUtils.getRootCause(ex).getMessage());
        addGlobalErrorMessage(summary, message);
      }
    }
  }

  /**
   * method called, when a row in the dataTable is selected.
   *
   * @param event the select event.
   */
  @Override
  public void onRowSelect(SelectEvent<?> event) {
    // nothing to do, just required to have UI updated
  }

  /**
   * method called, when a row in the dataTable is unselected.
   *
   * @param event the select event.
   */
  @Override
  public void onRowUnselect(UnselectEvent<?> event) {
    // nothing to do, just required to have UI updated   
  }

  /**
   * returns true, if the element on edit is a new instance (no id available yet) or not.
   *
   * @return true, if getId() == null, false otherwise,
   */
  @Override
  public boolean isNewEdit() {
    return editElement == null || editElement.getId() == null;
  }

  /**
   * returns true, if the user as edit rights
   */
  @Override
  public boolean getCanEdit() {
    String requiredChangeRole = getRequiredChangeRole();
    if (requiredChangeRole == null) {
      return true;
    }

    return sessionView.canEdit (requiredChangeRole);
  }

  /* *** getter / setter *** */

  @Override
  public LazyDataModel<T> getDataModel() {
    return dataModel;
  }

  @Override
  public List<SortMeta> getSortBy() {
    return sortBy;
  }

  /**
   * add a default filter wich is applied to any dataModel request.
   *
   * @param key        for the column to apply the filter on.
   * @param filterMeta the filterMeta specification describing the filter.
   */
  public void addDefaultFilter(String key, FilterMeta filterMeta) {
    dataModel.addDefaultFilter(key, filterMeta);
  }

  @Override
  @Deprecated(forRemoval = true, since = "0.1.0")
  public List<T> getAllElements() {
    if (allElements == null) {
      try {
        allElements = dataModel.getAllElements();
      } catch (Exception ex) {
        if (logger.isDebugEnabled()) {
          logger.debug(ex.getMessage(), ex);
        }
        String summary = sessionView.getLocalizedMessage(AbstractView.ERROR);
        String message = sessionView.getLocalizedMessage("message_failed_to_load", i18nId, ExceptionUtils.getRootCause(ex).getMessage());
        addGlobalErrorMessage(summary, message);
        // init all elements to avoid exception is logged and message is added twice in render and response phase
        allElements = Collections.emptyList();
      }
    }
    return allElements;
  }

  @Override
  public T getEditElement() {
    return editElement;
  }

  @Override
  public void setEditElement(T editElement) {
    this.editElement = editElement;
  }

  @Override
  public T getSelectedElement() {
    return selectedElement;
  }

  @Override
  public void setSelectedElement(T selectedElement) {
    this.selectedElement = selectedElement;
  }

}
