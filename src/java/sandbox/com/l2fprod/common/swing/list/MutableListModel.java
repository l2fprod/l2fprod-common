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
package com.l2fprod.common.swing.list;

import com.l2fprod.common.util.ResourceManager;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public interface MutableListModel/*<T>*/ extends ListModel {

  /**
   * Appends the specified element to the end of this list (optional operation).
   * 
   * @param element
   *          an <code>Object</code> value
   */
  void add(Object/*T*/ element);

  /**
   * Inserts the specified element at the specified position in this list
   * (optional operation).
   * 
   * @param index
   *          an <code>int</code> value
   * @param element
   *          an <code>Object</code> value
   */
  void add(int index, Object/*T*/ element);

  /**
   * Remove All Elements from this list model.
   */
  void removeAll();

  /**
   * Remove the element at <code>index</code>
   * 
   * @param index
   *          an <code>int</code> value
   * @return the element previously at the specified position.
   */
  Object/*T*/ remove(int index);

  /**
   * Move the object at <code>index</code> to the top of the list.
   * 
   * @param index
   *          an <code>int</code> value
   */
  void moveToTop(int index);

  /**
   * Move the object at <code>index</code> one line up
   * 
   * @param index
   *          an <code>int</code> value
   */
  void moveUp(int index);

  /**
   * Move the object at <code>index</code> one line down
   * 
   * @param index
   *          an <code>int</code> value
   */
  void moveDown(int index);

  /**
   * Move the object at <code>index</code> to the bottom of the list.
   * 
   * @param index
   *          an <code>int</code> value
   */
  void moveToBottom(int index);

  /**
   * Move the objects between <code>oldIndex0</code> and
   * <code>oldIndex1</code> to the new position <code>newIndex0</code>.
   * 
   * @param oldIndex0
   *          oldIndex0 the limits of the interval to move
   * @param oldIndex1
   *          oldIndex1 the limits of the interval to move
   * @param newIndex0
   *          the new position of the element that was at oldIndex0
   */
  void moveTo(int oldIndex0, int oldIndex1, int newIndex0);

  public interface ActionList {

    public void actionOnListPerformed(ActionEvent event, JList list);
  }

  /**
   * Standard list of Actions that could be plugged to a list.
   */
  public static class Actions {

    private static interface Updater {

      void update(Action action, Component target);
    }

    private static void setupAction(final Action action,
      final Component target, final Updater updater) {
      if (!(target instanceof JList || target instanceof JTable)) { throw new IllegalArgumentException(
        "Only JList or JTable are supported by MutableListModel.Actions"); }

      final ListDataListener modelListener = new ListDataListener() {

        public void contentsChanged(ListDataEvent e) {
          updater.update(action, target);
        }

        public void intervalAdded(ListDataEvent e) {
          updater.update(action, target);
        }

        public void intervalRemoved(ListDataEvent e) {
          updater.update(action, target);
        }
      };
      target.addPropertyChangeListener("model", new PropertyChangeListener() {

        public void propertyChange(PropertyChangeEvent event) {
          ListModel oldModel = (ListModel)event.getOldValue();
          ListModel newModel = (ListModel)event.getNewValue();

          if (oldModel != null) {
            oldModel.removeListDataListener(modelListener);
          }
          newModel.addListDataListener(modelListener);

          updater.update(action, target);
        }
      });

      ListSelectionModel selectionModel = null;
      if (target instanceof JList) {
        selectionModel = ((JList)target).getSelectionModel();
      } else if (target instanceof JTable) {
        selectionModel = ((JTable)target).getSelectionModel();
      }

      selectionModel.addListSelectionListener(new ListSelectionListener() {

        public void valueChanged(ListSelectionEvent event) {
          updater.update(action, target);
        }
      });
      if (target instanceof JList && ((JList)target).getModel() != null) {
        ((JList)target).getModel().addListDataListener(modelListener);
      } else if (target instanceof JTable
        && ((JTable)target).getModel() != null) {
        ((ListModel)((JTable)target).getModel())
          .addListDataListener(modelListener);
      }
      updater.update(action, target);
    }

    public static Action createMoveToTopAction(final JList list) {
      final Action moveToTop = new AbstractAction(ResourceManager.ui()
        .getString("list.moveTop")) {

        public void actionPerformed(ActionEvent event) {
          ((MutableListModel)list.getModel())
            .moveToTop(list.getSelectedIndex());
          list.setSelectedIndex(0);
        }
      };
      setupAction(moveToTop, list, new Updater() {

        public void update(Action action, Component p_Target) {
          action.setEnabled(((JList)p_Target).getModel().getSize() > 0
            && ((JList)p_Target).getSelectedIndex() > 0);
        }
      });
      moveToTop.putValue(Action.SHORT_DESCRIPTION, ResourceManager.ui()
        .getString("list.moveTop"));
      return moveToTop;
    }

    public static Action createMoveUpAction(final JList list) {
      final Action moveUp = new AbstractAction(ResourceManager.ui().getString(
        "list.moveUp")) {

        public void actionPerformed(ActionEvent event) {
          int selected = list.getSelectedIndex();
          ((MutableListModel)list.getModel()).moveUp(selected);
          list.setSelectedIndex(selected - 1);
        }
      };
      setupAction(moveUp, list, new Updater() {

        public void update(Action action, Component p_Target) {
          action.setEnabled(((JList)p_Target).getModel().getSize() > 0
            && ((JList)p_Target).getSelectedIndex() > 0);
        }
      });
      moveUp.putValue(Action.SHORT_DESCRIPTION, ResourceManager.ui().getString(
        "list.moveUp"));
      return moveUp;
    }

    public static Action createMoveUpAction(final JTable table) {
      final Action moveUp = new AbstractAction(ResourceManager.ui().getString(
        "list.moveUp")) {

        public void actionPerformed(ActionEvent event) {
          int selected = table.getSelectedRow();
          ((MutableListModel)table.getModel()).moveUp(selected);
          table.getSelectionModel().setSelectionInterval(selected - 1,
            selected - 1);
        }
      };
      setupAction(moveUp, table, new Updater() {

        public void update(Action action, Component p_Target) {
          action.setEnabled(((JTable)p_Target).getModel().getRowCount() > 0
            && ((JTable)p_Target).getSelectedRow() > 0);
        }
      });
      moveUp.putValue(Action.SHORT_DESCRIPTION, ResourceManager.ui().getString(
        "list.moveUp"));
      return moveUp;
    }

    public static Action createMoveDownAction(final JList list) {
      final Action moveDown = new AbstractAction(ResourceManager.ui()
        .getString("list.moveDown")) {

        public void actionPerformed(ActionEvent event) {
          int selected = list.getSelectedIndex();
          ((MutableListModel)list.getModel()).moveDown(selected);
          list.setSelectedIndex(selected + 1);
        }
      };
      setupAction(moveDown, list, new Updater() {

        public void update(Action action, Component p_Target) {
          int selected = ((JList)p_Target).getSelectedIndex();
          action.setEnabled(((JList)p_Target).getModel().getSize() > 0
            && selected != -1
            && selected < (((JList)p_Target).getModel().getSize() - 1));
        }
      });
      moveDown.putValue(Action.SHORT_DESCRIPTION, ResourceManager.ui()
        .getString("list.moveDown"));
      return moveDown;
    }

    public static Action createMoveDownAction(final JTable table) {
      final Action moveDown = new AbstractAction(ResourceManager.ui()
        .getString("list.moveDown")) {

        public void actionPerformed(ActionEvent event) {
          int selected = table.getSelectedRow();
          ((MutableListModel)table.getModel()).moveDown(selected);
          table.getSelectionModel().setSelectionInterval(selected + 1,
            selected + 1);
        }
      };
      setupAction(moveDown, table, new Updater() {

        public void update(Action action, Component p_Target) {
          int selected = ((JTable)p_Target).getSelectedRow();
          action.setEnabled(((JTable)p_Target).getModel().getRowCount() > 0
            && selected != -1
            && selected < (((JTable)p_Target).getModel().getRowCount() - 1));
        }
      });
      moveDown.putValue(Action.SHORT_DESCRIPTION, ResourceManager.ui()
        .getString("list.moveDown"));
      return moveDown;
    }

    public static Action createMoveToBottomAction(final JList list) {
      final Action moveToBottom = new AbstractAction(ResourceManager.ui()
        .getString("list.moveBottom")) {

        public void actionPerformed(ActionEvent event) {
          ((MutableListModel)list.getModel()).moveToBottom(list
            .getSelectedIndex());
          list.setSelectedIndex(list.getModel().getSize() - 1);
        }
      };
      setupAction(moveToBottom, list, new Updater() {

        public void update(Action action, Component p_Target) {
          int selected = ((JList)p_Target).getSelectedIndex();
          action.setEnabled(((JList)p_Target).getModel().getSize() > 0
            && selected != -1
            && selected < (((JList)p_Target).getModel().getSize() - 1));
        }
      });
      moveToBottom.putValue(Action.SHORT_DESCRIPTION, ResourceManager.ui()
        .getString("list.moveBottom"));
      return moveToBottom;
    }

    public static Action createRemoveAction(final JList list) {
      final Action remove = new AbstractAction(ResourceManager.ui().getString(
        "list.remove")) {

        public void actionPerformed(ActionEvent event) {
          int selected = list.getSelectedIndex();
          if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(list,
            ResourceManager.ui().getString("list.removeWarning"),
            ResourceManager.ui().getString("list.removeWarningTitle"),
            JOptionPane.YES_NO_OPTION)) {
            ((MutableListModel)list.getModel()).remove(selected);
            selected = Math.min(list.getModel().getSize() - 1, selected);
            list.setSelectedIndex(selected);
          }
        }
      };
      setupAction(remove, list, new Updater() {

        public void update(Action action, Component p_Target) {
          action.setEnabled(((JList)p_Target).getModel().getSize() > 0
            && ((JList)p_Target).getSelectedIndex() != -1);
        }
      });
      remove.putValue(Action.SHORT_DESCRIPTION, ResourceManager.ui().getString(
        "list.remove"));
      return remove;
    }

    public static Action createRemoveAction(final JTable table) {
      final Action remove = new AbstractAction(ResourceManager.ui().getString(
        "list.remove")) {

        public void actionPerformed(ActionEvent event) {
          int selected = table.getSelectedRow();
          ((MutableListModel)table.getModel()).remove(selected);
          selected = Math.min(table.getModel().getRowCount() - 1, selected);
          if (selected >= 0) {
            table.setRowSelectionInterval(selected, selected);
          }
        }
      };
      setupAction(remove, table, new Updater() {

        public void update(Action action, Component p_Target) {
          action.setEnabled(((JTable)p_Target).getModel().getRowCount() > 0
            && ((JTable)p_Target).getSelectedRow() != -1);
        }
      });
      remove.putValue(Action.SHORT_DESCRIPTION, ResourceManager.ui().getString(
        "list.remove"));
      return remove;
    }

    public static Action createRemoveAllAction(final JList list) {
      final Action removeAll = new AbstractAction(ResourceManager.ui()
        .getString("list.removeAll")) {

        public void actionPerformed(ActionEvent event) {
          if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(list,
            ResourceManager.ui().getString("list.removeAllWarning"),
            ResourceManager.ui().getString("list.removeAllWarningTitle"),
            JOptionPane.YES_NO_OPTION)) {
            ((MutableListModel)list.getModel()).removeAll();
          }
        }
      };
      setupAction(removeAll, list, new Updater() {

        public void update(Action action, Component p_Target) {
          action.setEnabled(((JList)p_Target).getModel().getSize() > 0);
        }
      });
      removeAll.putValue(Action.SHORT_DESCRIPTION, ResourceManager.ui()
        .getString("list.removeAll"));
      return removeAll;
    }

    public static Action createRemoveAllAction(final JTable table) {
      final Action removeAll = new AbstractAction(ResourceManager.ui()
        .getString("list.removeAll")) {

        public void actionPerformed(ActionEvent event) {
          if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(table,
            ResourceManager.ui().getString("list.removeAllWarning"),
            ResourceManager.ui().getString("list.removeAllWarningTitle"),
            JOptionPane.YES_NO_OPTION)) {
            ((MutableListModel)table.getModel()).removeAll();
          }
        }
      };
      setupAction(removeAll, table, new Updater() {

        public void update(Action action, Component p_Target) {
          action.setEnabled(((JTable)p_Target).getModel().getRowCount() > 0);
        }
      });
      removeAll.putValue(Action.SHORT_DESCRIPTION, ResourceManager.ui()
        .getString("list.removeAll"));
      return removeAll;
    }

    public static Action createActionListAction(final JList list, String text,
      final ActionList _action) {
      final Action result = new AbstractAction(text) {

        public void actionPerformed(ActionEvent event) {
          _action.actionOnListPerformed(event, list);
        }
      };
      setupAction(result, list, new Updater() {

        public void update(Action action, Component p_Target) {
          action.setEnabled(((JList)p_Target).getModel().getSize() > 0
            && ((JList)p_Target).getSelectedIndex() >= 0);
        }
      });
      result.putValue(Action.SHORT_DESCRIPTION, text);
      return result;
    }

  } // Actions

}
