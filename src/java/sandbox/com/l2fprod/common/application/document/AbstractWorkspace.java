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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.List;

/**
 * Base implementation for a Workspace. <br>
 */
public class AbstractWorkspace implements Workspace {

  private List documents;
  private WorkspaceDocument selected;
  private WorkspaceDocumentListener documentListener;
  private PropertyChangeListener changeListener;

  public AbstractWorkspace() {
    documents = new ArrayList();
    documentListener = new DocumentListener();
    changeListener = new ChangeListener();
  }

  public void setSelected(WorkspaceDocument document)
      throws PropertyVetoException {
    if (document == null) {
      return;
    }
    if (document.getWorkspace() != this) {
      throw new IllegalArgumentException(
          "document not opened in this workspace");
    }
    WorkspaceDocument oldSelected = this.selected;
    if (oldSelected != null) {
      oldSelected.setSelected(false);
      this.selected = null;
    }
    document.setSelected(true);
    this.selected = document;
  }

  public final WorkspaceDocument getSelected() {
    return selected;
  }

  public final WorkspaceDocument[] getDocuments() {
    return (WorkspaceDocument[])documents.toArray(new WorkspaceDocument[0]);
  }

  public final void open(WorkspaceDocument document) {
    document.setWorkspace(this);
    document.addDocumentListener(documentListener);
    document.addPropertyChangeListener(changeListener);
    try {
      document.setClosed(false);
    } catch (PropertyVetoException e) {
      e.printStackTrace();
    }
  }

  /**
   * Called once the document has been opened in this workspace.
   * Subclasses will react by making the document visible to the user.
   * 
   * @param document
   */
  protected void documentOpened(WorkspaceDocument document) {
  }

  /**
   * Called whenever a property is changed in the Document
   */
  protected void documentChanged(WorkspaceDocument document,
      PropertyChangeEvent event) {
  }

  /**
   * Called once the document has been closed. Subclasses will react
   * by hiding the document.
   * 
   * @param document
   */
  protected void documentClosed(WorkspaceDocument document) {
  }

  public void close(WorkspaceDocument document) throws PropertyVetoException {
    if (document == null) {
      return;
    }
    if (document.getWorkspace() != this) {
      throw new IllegalArgumentException(
          "document not opened in this workspace");
    }
    document.setClosed(true);
  }

  public void save(WorkspaceDocument document) {
    if (document == null) {
      return;
    }
    if (document.getWorkspace() != this) {
      throw new IllegalArgumentException(
          "document not opened in this workspace");
    }
    document.save();
  }

  class DocumentListener extends WorkspaceDocumentAdapter {

    public void documentOpened(WorkspaceDocument document) {
      documents.add(document);
      AbstractWorkspace.this.documentOpened(document);
    }

    public void documentClosed(WorkspaceDocument document) {
      document.removeDocumentListener(this);
      documents.remove(document);
      AbstractWorkspace.this.documentClosed(document);
    }
  }

  class ChangeListener implements PropertyChangeListener {

    public void propertyChange(PropertyChangeEvent evt) {
      // make sure to remove ourselves from the listener list of the
      // document
      if ("closed".equals(evt.getPropertyName())
          && Boolean.TRUE.equals(evt.getNewValue())) {
        ((WorkspaceDocument)evt.getSource()).removePropertyChangeListener(this);
      } else {
        if ("selected".equals(evt.getPropertyName())
            && Boolean.TRUE.equals(evt.getNewValue())) {
          try {
            AbstractWorkspace.this.setSelected((WorkspaceDocument)evt
                .getSource());
          } catch (PropertyVetoException e) {
            e.printStackTrace();
          }
        }
        AbstractWorkspace.this.documentChanged((WorkspaceDocument)evt
            .getSource(), evt);
      }
    }
  }
}