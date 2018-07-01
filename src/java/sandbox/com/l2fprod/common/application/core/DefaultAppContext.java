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
package com.l2fprod.common.application.core;

import com.l2fprod.common.application.selection.EmptySelection;
import com.l2fprod.common.application.selection.Selection;
import com.l2fprod.common.application.selection.SelectionChangedEvent;
import com.l2fprod.common.application.selection.SelectionListener;
import com.l2fprod.common.application.selection.SelectionProvider;

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.Action;
import javax.swing.event.EventListenerList;

/**
 * DefaultAppContext.<br>
 * 
 */
public class DefaultAppContext implements AppContext, SelectionManager,
  ActionManager, CommandContext, SelectionListener {

  private EventListenerList selectionListeners;

  private OnFocusChangeUpdater focusUpdater;

  private Component focusOwner;

  private SelectionProvider selectionProvider;

  private Selection selection;

  private List actions;

  private Map services;

  public DefaultAppContext() {
    actions = new ArrayList();
    services = new Hashtable();

    selectionProvider = SelectionProvider.NULL_PROVIDER;
    setSelection(null);
    selectionListeners = new EventListenerList();

    // CurrentTasksPanel taskPanel = new CurrentTasksPanel(taskManager);
    // taskPanel.getTasksFrame().setVisible(true);

    focusUpdater = new OnFocusChangeUpdater();
    KeyboardFocusManager.getCurrentKeyboardFocusManager()
      .addPropertyChangeListener("focusOwner", focusUpdater);
  }

  public ActionManager getActions() {
    return this;
  }

  public SelectionManager getSelectionManager() {
    return this;
  }

  public Object getService(Object key) {
    return services.get(key);
  }

  public void registerService(Object key, Object value) {
    services.put(key, value);
  }

  public Selection getSelection() {
    return selection;
  }

  private void setSelection(Selection selection) {
    if (selection == null) {
      this.selection = new EmptySelection();
    } else {
      this.selection = selection;
    }
  }

  public Component getFocusOwner() {
    return focusOwner;
  }

  public void add(Action action) {
    synchronized (actions) {
      actions.add(action);
    }
    action.putValue("commandContext", this);
  }

  public void remove(Action action) {
    synchronized (actions) {
      actions.remove(action);
    }
    action.putValue("commandContext", null);
  }

  public Action get(String actionId) {
    for (Iterator iter = actions.iterator(); iter.hasNext();) {
      Action a = (Action)iter.next();
      if (actionId.equals(a.getValue("id"))) { return a; }
    }
    return null;
  }

  public void updateActions() {
    Object[] currentActions;
    synchronized (actions) {
      currentActions = actions.toArray();
    }

    for (int i = 0, c = currentActions.length; i < c; i++) {
      Action action = (Action)currentActions[i];
      if (action instanceof UpdateableAction) {
        ((UpdateableAction)action).update(this);
      }
    }
  }

  class OnFocusChangeUpdater implements PropertyChangeListener {

    public void propertyChange(PropertyChangeEvent evt) {
      Component newFocusOwner = (Component)evt.getNewValue();

      // find a selection provider for this focus owner
      SelectionProvider newSelectionProvider = SelectionProvider.Helper
        .findSelectionProvider(newFocusOwner);

      // if we have no selection provider for this component,
      // we do not handle it as our selection provider and keep our old
      // selection.
      if (newSelectionProvider != null) {
        // unregister from the previous provider
        selectionProvider.removeSelectionListener(DefaultAppContext.this);

        // register to the new provider
        newSelectionProvider.addSelectionListener(DefaultAppContext.this);

        selectionProvider = newSelectionProvider;
      }

      // update context
      focusOwner = newFocusOwner;
      setSelection(selectionProvider.getSelection());

      // update the actions
      updateActions();
    }

  }

  /**
   * Update actions whenever the selection changes
   */
  public void selectionChanged(SelectionChangedEvent event) {
    setSelection(selectionProvider.getSelection());
    updateActions();
    fireSelectionChanged(event);
  }

  public void addSelectionListener(SelectionListener listener) {
    selectionListeners.add(SelectionListener.class, listener);
  }

  public void removeSelectionListener(SelectionListener listener) {
    selectionListeners.remove(SelectionListener.class, listener);
  }

  private void fireSelectionChanged(SelectionChangedEvent event) {
    SelectionListener[] l = (SelectionListener[])selectionListeners
      .getListeners(SelectionListener.class);
    for (int i = 0, c = l.length; i < c; i++) {
      l[i].selectionChanged(event);
    }
  }

}
