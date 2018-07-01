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

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;

/**
 * A workspace which shows VisualWorkspaceDocuments in tabs.
 */
public class TabbedWorkspace extends AbstractWorkspace {

  private JTabbedPane pane;

  public TabbedWorkspace() {
    pane = new JTabbedPane();
    pane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    pane.getModel().addChangeListener(new javax.swing.event.ChangeListener() {

      public void stateChanged(ChangeEvent e) {
        try {
          TabbedWorkspace.super.setSelected(getDocument(pane
              .getSelectedComponent()));
        } catch (PropertyVetoException e1) {
        }
      }
    });
  }

  public Component getVisualComponent() {
    return pane;
  }

  public void setSelected(WorkspaceDocument document) throws PropertyVetoException {
    super.setSelected(document);
    if (document instanceof VisualWorkspaceDocument) {
      int index = getIndex((VisualWorkspaceDocument)document);
      pane.setSelectedIndex(index);
    }
  }

  protected void documentOpened(WorkspaceDocument document) {
    if (document instanceof VisualWorkspaceDocument) {
      VisualWorkspaceDocument ui = (VisualWorkspaceDocument)document;
      pane.addTab(ui.getTitle(), ui.getIcon(), ui.getVisualComponent());
    }
  }

  protected void documentClosed(WorkspaceDocument document) {
    if (document instanceof VisualWorkspaceDocument) {
      Component ui = ((VisualWorkspaceDocument)document).getVisualComponent();
      pane.remove(ui);
    }
  }

  protected void documentChanged(WorkspaceDocument document,
      PropertyChangeEvent event) {
    if (event.getSource() instanceof VisualWorkspaceDocument) {
      VisualWorkspaceDocument ui = (VisualWorkspaceDocument)event.getSource();
      int index = getIndex(ui);
      if ("title".equals(event.getPropertyName())) {
        pane.setTitleAt(index, document.getTitle());
      } else if ("icon".equals(event.getPropertyName())) {
        pane.setIconAt(index, ui.getIcon());
      }
    }
  }

  private VisualWorkspaceDocument getDocument(Component component) {
    WorkspaceDocument[] documents = getDocuments();
    VisualWorkspaceDocument result = null;
    for (int i = 0, c = documents.length; i < c; i++) {
      if (documents[i] instanceof VisualWorkspaceDocument
          && ((VisualWorkspaceDocument)documents[i]).getVisualComponent() == component) {
        result = (VisualWorkspaceDocument)documents[i];
      }
    }
    return result;
  }

  private int getIndex(VisualWorkspaceDocument document) {
    return pane.indexOfComponent(document.getVisualComponent());
  }
  
}