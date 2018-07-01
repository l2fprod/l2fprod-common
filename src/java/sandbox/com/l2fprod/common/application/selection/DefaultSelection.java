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

import java.awt.Component;

import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * DefaultSelection. <br>
 *  
 */
public class DefaultSelection implements Selection {

  private Object[] objects;

  public DefaultSelection(JList list) {
    this.objects = list.getSelectedValues();
  }

  public DefaultSelection(JTree tree) {
    this(tree, false);
  }

  public DefaultSelection(JTree tree, boolean onlyLastPathComponent) {
    this(tree.getSelectionModel(), onlyLastPathComponent);
  }

  public DefaultSelection(TreeSelectionModel tree, boolean onlyLastPathComponent) {
    TreePath[] paths = tree.getSelectionPaths();
    if (paths == null) {
      objects = null;
    } else {
      objects = new Object[paths.length];
      if (onlyLastPathComponent) {
        for (int i = 0, c = objects.length; i < c; i++) {
          objects[i] = paths[i].getLastPathComponent();
        }
      }
    }
  }

  public DefaultSelection(Object[] objects) {
    this.objects = objects;
  }

  public Object[] getSelection() {
    return objects;
  }

  public boolean isEmpty() {
    return objects == null || objects.length == 0;
  }

  public String toString() {
    return super.toString()
      + "[size="
      + (isEmpty() ? "0" : String.valueOf(objects.length))
      + "]";
  }
  
  /**
   * Find the selection out of a Component. Return an empty selection if there
   * are not selected objects.
   * 
   * @param component
   * @return the selection for the given component
   */
  public static Selection findSelection(Component component) {
    if (component instanceof JList) {
      return new DefaultSelection((JList)component);
    } else if (component instanceof JTree) {
      return new DefaultSelection((JTree)component, true);
    } else if (component instanceof SelectionProvider) {
      return ((SelectionProvider)component).getSelection();
    } else {
      return new EmptySelection();
    }
  }

}
