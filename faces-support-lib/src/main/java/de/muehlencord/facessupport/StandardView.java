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

import jakarta.faces.event.ActionEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import java.io.Serializable;
import java.util.List;

/**
 * interface for manged bean to for crud operations on IdentifiableObject
 * @param <T> the base object the view supports.
 * @param <I> the Id object of the base object.
 * @author Joern Muehlencord, 2025-08-17
 * @since 0.1.0
 */
public interface StandardView <T extends IdentifiableObject<I>, I extends Serializable> extends Serializable {

  /**
   * returns the lazy data model implementation - typically used in the xhtml file, primefaces datatable value tag.
   *
   * @return the lazy data model implementation.
   */
  LazyDataModel<T> getDataModel();

  /**
   * returns the list of SortMeta for sorting the datatable model in use.
   *
   * @return the list of SortMeta
   */
  List<SortMeta> getSortBy();

  /**
   * returns the full list of all elements. Used alternatively when not using the lazy data model. Be careful when this returns a big list.
   *
   * @return all elements found in the table.
   */
  List<T> getAllElements();

  /**
   * returns the selected element - typically connected to a primefaces datatable selectedElement tag
   *
   * @return the selected element
   */
  T getSelectedElement();

  /**
   * sets the selected element - typically connected to ta primefaces datatable selectedElement tag
   *
   * @param element the element to set as selected element
   */
  void setSelectedElement(T element);

  /**
   * the edit element is used when updating an element or creating a new element. The startEdit element clones the selected element into the editElement to be
   * able to rollback in case cancel is pressed. Only used in edit dialogs etc.
   *
   * @return the edit element.
   */
  T getEditElement();

  /**
   * sets the edit element - typically not directly used but e.g. startAdd or startEdit will set the value
   *
   * @param element the element to edit
   */
  void setEditElement(T element);

  /**
   * stops editing the edit element and rolls back to the previous data. Typically, connected to an actionListener event of a commandButton in an edit dialog.
   *
   * @param event the event causing this method to be called.
   */
  void cancelEdit(ActionEvent event);

  /**
   * stops editing the edit element and rolls back to the previous data. Typically, connected to an actionListener event of a commandButton in an edit dialog.
   */
  void cancelEdit();

  /**
   * deletes the selected entity. Typically, connected to an actionListener of a commandButton in the overview page.
   */
  void delete();

  /**
   * save the made changes or create a new element. Typically, connected to an actionListener of a commandButton in an edit dialog.
   *
   * @param event the event causing this method to be called.
   */
  void saveEdit(ActionEvent event);

  /**
   * save the made changes or create a new element. Typically, connected to an actionListener of a commandButton in an edit dialog.
   */
  void saveEdit();

  /**
   * create a new element. Typically, connected to an actionListener of a commandButton in the overview page.
   */
  void startAdd();

  /**
   * edit the selected element. Typically, connected to an actionListener of a commandButton in the overview page.
   */
  void startEdit();

  /**
   * listener when a row in a datatable is selected. The default implementation is empty.
   *
   * @param event the select event.
   */
  void onRowSelect(SelectEvent<?> event);

  /**
   * listener when a row in a datatable is unselected. The default implementation is empty.
   *
   * @param event the unselect event.
   */
  void onRowUnselect(UnselectEvent<?> event);

  /**
   * returns true, if the edit element was caused by startAdd, false otherwise.
   *
   * @return true, if the edit element was caused by startAdd, false otherwise.
   */
  boolean isNewEdit();

  /**
   * returns the name of the required role to be able to edit elements. if a user has this role, getCanEdit returns true, false otherwise. If this method
   * returns null, no special edit rights are required and getCanEdit will return true.
   *
   * @return the name of the role a user must have, if the user can edit the element.
   */
  String getRequiredChangeRole();

  /**
   * returns true, if the user as edit rights.
   * @return true, if the uas edit rights, false otherwise.
   */
  boolean getCanEdit();
}
