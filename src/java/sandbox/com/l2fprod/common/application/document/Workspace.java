/**
 * @PROJECT.FULLNAME@ @VERSION@ License.
 *
 * Copyright @YEAR@ L2FProd.com
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
 */
package com.l2fprod.common.application.document;

import java.beans.PropertyVetoException;

/**
 * A workspace defined a container for documents. Documents can be
 * added (opened) / removed (closed) to /from a workspace. At a given
 * time, there is only one "active" document (selected) in the
 * workspace. <br>
 */
public interface Workspace {

  /**
   * Selects the given document in the workspace. The document must
   * belong to this workspace otherwise an IllegalArgumentException
   * will be thrown.
   * 
   * @param document
   * @throws IllegalArgumentException if the document is not in this
   *           workspace
   * @throws PropertyVetoException if the previously selected document
   *           prevents another document from being selected
   */
  public void setSelected(WorkspaceDocument document)
      throws IllegalArgumentException, PropertyVetoException;

  /**
   * Gets the currently selected document.
   * 
   * @return the currently selected document
   */
  public WorkspaceDocument getSelected();

  /**
   * Gets all documents hosted in this workspace.
   * 
   * @return all documents hosted in this workspace
   */
  public WorkspaceDocument[] getDocuments();

  /**
   * Opens the given document in this workspace.
   * 
   * @param document
   */
  public void open(WorkspaceDocument document);

  /**
   * Closes the given document. Registered listeners on the document
   * may prevent it from closing.
   * 
   * @param document
   * @throws PropertyVetoException if one of the listeners prevented
   *           the document to be closed
   */
  public void close(WorkspaceDocument document) throws PropertyVetoException;

  /**
   * Saves the given document.
   * 
   * @param document
   */
  public void save(WorkspaceDocument document);

}