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
package com.l2fprod.common.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.border.AbstractBorder;

/**
 * Paints an highlight if the component or one of its descendants have
 * the focus.
 * 
 * @see #repaintOnFocusChange(Component)
 */
public class FocusedComponentBorder extends AbstractBorder {

  private static List componentToRepaint = Collections
      .synchronizedList(new ArrayList(3));
  static {
    PropertyChangeListener listener = new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent evt) {
        repaintComponents();
      }
    };
    KeyboardFocusManager.getCurrentKeyboardFocusManager()
        .addPropertyChangeListener(listener);
  }

  public Insets getBorderInsets(Component component) {
    return new Insets(1, 1, 1, 1);
  }

  public void paintBorder(Component component, Graphics g, int x, int y, int w,
      int h) {
    Component focusOwner = KeyboardFocusManager
        .getCurrentKeyboardFocusManager().getFocusOwner();
    if (focusOwner == component
        || (component instanceof Container && ((Container)component)
            .isAncestorOf(focusOwner))) {
      Color oldColor = g.getColor();
      g.setColor(Color.red);
      g.drawRect(x, y, w - 1, h - 1);
      g.setColor(oldColor);
    }
  }

  /**
   * Register the given component to be repainted whenever the focused
   * component changes. This must be called for each component using a
   * FocusedComponentBorder.
   * 
   * @param component
   */
  public static void repaintOnFocusChange(Component component) {
    componentToRepaint.add(new WeakReference(component));
  }

  private static void repaintComponents() {
    WeakReference[] refs = (WeakReference[])componentToRepaint
        .toArray(new WeakReference[0]);
    for (int i = 0; i < refs.length; i++) {
      Component component = (Component)refs[i].get();
      if (component == null) {
        componentToRepaint.remove(refs[i]);        
      } else {
        component.repaint();
      }
    }
  }
  
}