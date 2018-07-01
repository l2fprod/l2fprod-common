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
package com.l2fprod.common.swing.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.table.AbstractTableModel;

public abstract class AbstractMutableListTableModel/*<T>*/ extends AbstractTableModel
  implements com.l2fprod.common.swing.list.MutableListModel/*<T>*/ {

  protected List/*<T>*/ _data = new ArrayList/*<T>*/();

  public synchronized void add(Object/*T*/ object) {
    _data.add(object);
    fireTableRowsInserted(_data.size() - 1, _data.size() - 1);
  }

  public synchronized void add(Object/*T*/[] objects) {
    if (objects == null || objects.length == 0) return;
    _data.addAll(Arrays.asList(objects));
    fireTableRowsInserted(_data.size() - objects.length, _data.size() - 1);
  }

  public synchronized void add(int index, Object/*T*/ element) {
    if (index < 0) {
      add(element);
    } else {
      _data.add(index, element);
      fireTableRowsInserted(index, index);
    }
  }

  /**
   * Shortcut for fireTableDataChanged()
   */
  public void update() {
    fireTableDataChanged();
  }

  /**
   * Notify listeners that the given row has been updated.
   * 
   * @param row
   *          an <code>int</code> value
   * @see #getIndex(Object)
   */
  public void update(int row) {
    fireTableRowsUpdated(row, row);
  }

  /**
   * Notify listeners that the given object has been updated. This method will
   * traverse the model to find all occurences of the given object.
   * 
   * @param o
   *          an <code>Object</code> value
   * @return the number of rows updated
   * @see #getIndex(Object)
   */
  public int update(Object/*T*/ o) {
    int fromIndex = 0;
    int rowCount = 0;
    int index;
    while ((index = getIndex(o, fromIndex)) != -1) {
      update(index);
      rowCount++;
      fromIndex = index + 1;
    }
    return rowCount;
  }

  public synchronized void replace(int index, Object/*T*/ object) {
    _data.set(index, object);
    fireTableRowsUpdated(index, index);
    fireTableDataChanged();
  }

  public synchronized void remove(Object/*T*/ o) {
    remove(getIndex(o));
  }

  public synchronized Object/*T*/ remove(int index) {
    if ((index >= 0) && (index < _data.size())) {
      Object/*T*/ o = _data.remove(index);
      fireTableRowsDeleted(index, index);
      fireTableDataChanged();
      return o;
    }
    return null;
  }

  public synchronized void removeAll() {
    _data = new ArrayList/*<T>*/();
    fireTableDataChanged();
  }

  /**
   * Move the object at <code>index</code> to the top of the list.
   * 
   * @param index
   *          an <code>int</code> value
   */
  public void moveToTop(int index) {
    moveComponent(index, 0);
  }

  /**
   * Move the object at <code>index</code> one line up
   * 
   * @param index
   *          an <code>int</code> value
   */
  public void moveUp(int index) {
    moveComponent(index, index - 1);
  }

  /**
   * Move the object at <code>index</code> one line down
   * 
   * @param index
   *          an <code>int</code> value
   */
  public void moveDown(int index) {
    moveComponent(index, index + 1);
  }

  /**
   * Move the object at <code>index</code> to the bottom of the list.
   * 
   * @param index
   *          an <code>int</code> value
   */
  public void moveToBottom(int index) {
    if (index != -1) {
      if (index == getSize() - 1) { return; }
      moveComponent(index, getSize() - 1);
    }
  }

  /**
   * Move the objects between <code>oldIndex0</code> and
   * <code>oldIndex1</code> to the new position <code>newIndex0</code>.
   * 
   * @param oldIndex0 ,
   *          oldIndex1 the limits of the interval to move
   * @param newIndex0
   *          the new position of the element that was at oldIndex0
   */
  public void moveTo(int oldIndex0, int oldIndex1, int newIndex0) {
    throw new Error("Not yet implemented");
  }

  private void moveComponent(int p_OldIndex, int p_NewIndex) {

    if (p_OldIndex < 0 || p_OldIndex > getSize()) { return; }

    int addedIndex = -1;

    Object/*T*/ o = remove(p_OldIndex);

    if (p_NewIndex < 0) {
      add(0, o);
      addedIndex = 0;
    } else if (p_NewIndex >= getSize()) {
      add(o);
      addedIndex = getSize() - 1;
    } else {
      addedIndex = p_NewIndex;
      add(p_NewIndex, o);
    }

    fireContentsChanged(this, p_OldIndex, addedIndex);

  }

  /**
   * Returns the index within this model of the first occurence of the specified
   * Object.
   * 
   * @param o
   *          an <code>Object</code> value
   * @return the first occurence or -1 if o is not found in the model.
   */
  public int getIndex(Object o) {
    return getIndex(o, 0);
  }

  /**
   * Return the index within this model of the first occurence of the specified
   * Object, starting at the specified index (included).
   * 
   * @param o
   *          an <code>Object</code> value
   * @param fromIndex
   *          an <code>int</code> value
   * @return an <code>int</code> value
   */
  public int getIndex(Object o, int fromIndex) {
    int result = -1;
    Object current = null;
    for (int i = fromIndex, c = getRowCount(); i < c; i++) {
      current = getObject(i);
      if (current == o) {
        result = i;
        break;
      }
    }
    return result;
  }

  public Object/*T*/ getObject(int index) {
    if (index < 0 || index >= _data.size())
      return null;
    else {
      return _data.get(index);
    }
  }

  public int getRowCount() {
    return _data.size();
  }

  public void fireTableRowsInserted(int firstRow, int lastRow) {
    super.fireTableRowsInserted(firstRow, lastRow);
    fireIntervalAdded(this, firstRow, lastRow);
  }

  public void fireTableDataChanged() {
    super.fireTableDataChanged();
    fireContentsChanged(this, 0, getSize());
  }

  public void fireTableRowsUpdated(int firstRow, int lastRow) {
    super.fireTableRowsUpdated(firstRow, lastRow);
    fireContentsChanged(this, firstRow, lastRow);
  }

  public void fireTableRowsDeleted(int firstRow, int lastRow) {
    super.fireTableRowsDeleted(firstRow, lastRow);
    fireIntervalRemoved(this, firstRow, lastRow);
  }

  // LIST MODEL IMPL
  protected EventListenerList listenerList = new EventListenerList();

  /**
   * Add a listener to the list that's notified each time a change to the data
   * model occurs.
   * 
   * @param l
   *          the ListDataListener
   */
  public void addListDataListener(ListDataListener l) {
    listenerList.add(ListDataListener.class, l);
  }

  /**
   * Remove a listener from the list that's notified each time a change to the
   * data model occurs.
   * 
   * @param l
   *          the ListDataListener
   */
  public void removeListDataListener(ListDataListener l) {
    listenerList.remove(ListDataListener.class, l);
  }

  /**
   * AbstractListModel subclasses must call this method <b>after </b> one or
   * more elements of the list change. The changed elements are specified by a
   * closed interval index0, index1, i.e. the range that includes both index0
   * and index1. Note that index0 need not be less than or equal to index1.
   * 
   * @param source
   *          The ListModel that changed, typically "this".
   * @param index0
   *          One end of the new interval.
   * @param index1
   *          The other end of the new interval.
   * @see EventListenerList
   * @see DefaultListModel
   */
  protected void fireContentsChanged(Object source, int index0, int index1) {
    Object[] listeners = listenerList.getListenerList();
    ListDataEvent e = null;

    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == ListDataListener.class) {
        if (e == null) {
          e = new ListDataEvent(source, ListDataEvent.CONTENTS_CHANGED, index0,
            index1);
        }
        ((ListDataListener)listeners[i + 1]).contentsChanged(e);
      }
    }
  }

  /**
   * AbstractListModel subclasses must call this method <b>after </b> one or
   * more elements are added to the model. The new elements are specified by a
   * closed interval index0, index1, i.e. the range that includes both index0
   * and index1. Note that index0 need not be less than or equal to index1.
   * 
   * @param source
   *          The ListModel that changed, typically "this".
   * @param index0
   *          One end of the new interval.
   * @param index1
   *          The other end of the new interval.
   * @see EventListenerList
   * @see DefaultListModel
   */
  protected void fireIntervalAdded(Object source, int index0, int index1) {
    Object[] listeners = listenerList.getListenerList();
    ListDataEvent e = null;

    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == ListDataListener.class) {
        if (e == null) {
          e = new ListDataEvent(source, ListDataEvent.INTERVAL_ADDED, index0,
            index1);
        }
        ((ListDataListener)listeners[i + 1]).intervalAdded(e);
      }
    }
  }

  /**
   * AbstractListModel subclasses must call this method <b>after </b> one or
   * more elements are removed from the model. The new elements are specified by
   * a closed interval index0, index1, i.e. the range that includes both index0
   * and index1. Note that index0 need not be less than or equal to index1.
   * 
   * @param source
   *          The ListModel that changed, typically "this".
   * @param index0
   *          One end of the new interval.
   * @param index1
   *          The other end of the new interval.
   * @see EventListenerList
   * @see DefaultListModel
   */
  protected void fireIntervalRemoved(Object source, int index0, int index1) {
    Object[] listeners = listenerList.getListenerList();
    ListDataEvent e = null;

    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == ListDataListener.class) {
        if (e == null) {
          e = new ListDataEvent(source, ListDataEvent.INTERVAL_REMOVED, index0,
            index1);
        }
        ((ListDataListener)listeners[i + 1]).intervalRemoved(e);
      }
    }
  }

  public Object/*T*/ getElementAt(int index) {
    return getObject(index);
  }

  public int getSize() {
    return getRowCount();
  }

}