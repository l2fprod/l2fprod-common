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
package com.l2fprod.common.application.selection;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

/**
 * DefaultSelectionProvider. <br>
 */
public abstract class DefaultSelectionProvider
    implements
      SelectionProvider,
      ListSelectionListener,
      TreeSelectionListener,
      FocusListener {

  private EventListenerList listeners = new EventListenerList();

  public DefaultSelectionProvider() {
  }

  public DefaultSelectionProvider(JList list) {
    this(list.getSelectionModel());
    list.addFocusListener(this);
  }

  public DefaultSelectionProvider(JTable table) {
    this(table.getSelectionModel());
    table.addFocusListener(this);
  }

  public DefaultSelectionProvider(ListSelectionModel model) {
    model.addListSelectionListener(this);
  }

  public DefaultSelectionProvider(JTree tree) {
    tree.getSelectionModel().addTreeSelectionListener(this);
    tree.addFocusListener(this);
  }

  /**
   * Notify SelectionListener whenever the selection changes
   */
  public void valueChanged(ListSelectionEvent e) {
    if (!e.getValueIsAdjusting()) {
      fireSelectionChanged();
    }
  }

  public void valueChanged(TreeSelectionEvent e) {
    fireSelectionChanged();
  }

  public void focusGained(FocusEvent e) {
    if (!e.isTemporary()) {
      fireSelectionChanged();
    }
  }

  public void focusLost(FocusEvent e) {
    // nothing to do on selection lost
  }

  public void addSelectionListener(SelectionListener listener) {
    listeners.add(SelectionListener.class, listener);
  }

  public void removeSelectionListener(SelectionListener listener) {
    listeners.remove(SelectionListener.class, listener);
  }

  protected void fireSelectionChanged() {
    SelectionListener[] l = (SelectionListener[])listeners
        .getListeners(SelectionListener.class);
    SelectionChangedEvent event = new SelectionChangedEvent(this);
    for (int i = 0, c = l.length; i < c; i++) {
      l[i].selectionChanged(event);
    }
  }

}