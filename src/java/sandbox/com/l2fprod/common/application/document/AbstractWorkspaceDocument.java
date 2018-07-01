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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;

import javax.swing.event.EventListenerList;

/**
 * AbstractWorkspaceDocument. <br>
 */
public class AbstractWorkspaceDocument implements WorkspaceDocument {

  private static final int OPENED = 0;
  private static final int ACTIVATED = 1;
  private static final int DEACTIVATED = 2;
  private static final int CLOSING = 3;
  private static final int CLOSED = 4;
  
  private boolean closed = true;
  private boolean selected = false;
  private String title;
  
  private Workspace workspace;

  protected EventListenerList listeners;
  protected PropertyChangeSupport support;
  protected VetoableChangeSupport vetoSupport;
  
  public AbstractWorkspaceDocument() {
    listeners = new EventListenerList();
    support = new PropertyChangeSupport(this);
    vetoSupport = new VetoableChangeSupport(this);
  }

  public void addDocumentListener(WorkspaceDocumentListener listener) {
    listeners.add(WorkspaceDocumentListener.class, listener);
  }

  public void removeDocumentListener(WorkspaceDocumentListener listener) {
    listeners.remove(WorkspaceDocumentListener.class, listener);
  }

  public Workspace getWorkspace() {
    return workspace;
  }

  public void setWorkspace(Workspace workspace) {
    if (this.workspace != null) {
      throw new IllegalArgumentException("document is already in a workspace");
    }
    this.workspace = workspace;
  }

  public void setClosed(boolean closed) throws PropertyVetoException {
    // do nothing if no change required
    if (this.closed == closed) {
      return;
    }
    
    boolean old = this.closed;
    if (closed) {
      vetoSupport.fireVetoableChange("closed", old, closed);
      fireEvent(CLOSING);
      this.closed = true;
      fireEvent(CLOSED);
      workspace = null;
    } else {
      this.closed = false;
      fireEvent(OPENED);
    }
    support.firePropertyChange("closed", old, closed);
  }
  
  public boolean isClosed() {
    return closed;
  }
  
  public void setSelected(boolean selected) throws PropertyVetoException {
    if (this.selected == selected) {
      return;
    }
    
    boolean old = this.selected;
    if (selected) {
      this.selected = true;
      fireEvent(ACTIVATED);
    } else {
      vetoSupport.fireVetoableChange("selected", old, selected);
      this.selected = false;
      fireEvent(DEACTIVATED);
    }
    support.firePropertyChange("selected", old, selected);
  }
  
  public boolean isSelected() {
    return selected;
  }
  
  public void save() {
  }
  
  public String getTitle() {
    return title;
  }
  
  public void setTitle(String title) {
    String old = this.title;
    this.title = title;
    support.firePropertyChange("title", old, title);
  }
  
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
  }
  
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    support.removePropertyChangeListener(listener);
  }
  
  public void addVetoableChangeListener(VetoableChangeListener listener) {
    vetoSupport.addVetoableChangeListener(listener);
  }
  
  public void removeVetoableChangeListener(VetoableChangeListener listener) {
    vetoSupport.removeVetoableChangeListener(listener);
  }
  
  private void fireEvent(int event) {
    WorkspaceDocumentListener[] wdl = (WorkspaceDocumentListener[])listeners
        .getListeners(WorkspaceDocumentListener.class);
    for (int i = 0, c = wdl.length; i < c; i++) {
      switch (event) {
        case OPENED :
          wdl[i].documentOpened(this);
          break;
        case ACTIVATED :
          wdl[i].documentActivated(this);
          break;
        case DEACTIVATED :
          wdl[i].documentDeactivated(this);
          break;
        case CLOSING :
          wdl[i].documentClosing(this);
          break;
        case CLOSED :
          wdl[i].documentClosed(this);
          break;
      }
    }
  }
  
  public String toString() {
    return super.toString() + "[" + paramString() + "]";
  }
  
  protected String paramString() {
    return "title=" + getTitle() +
    ",closed=" + isClosed();
  }
  
}
