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

import javax.swing.JComponent;

/**
 * SelectionProvider. <br>
 *  
 */
public interface SelectionProvider {

  SelectionProvider NULL_PROVIDER =
    new SelectionProvider() {

    public Selection getSelection() {
      return new EmptySelection();
    }

    public void addSelectionListener(SelectionListener listener) {
    }

    public void removeSelectionListener(SelectionListener listener) {
    }
  };

  Selection getSelection();

  void addSelectionListener(SelectionListener listener);

  void removeSelectionListener(SelectionListener listener);

  class Helper {

    /**
     * Get a SelectionProvider for component or null if there is no selection
     * provider for such component
     * 
     * @param component
     * @return a SelectionProvider for the given component
     */
    public static SelectionProvider findSelectionProvider(final Component component) {
      if (component instanceof SelectionProvider) {
        return (SelectionProvider)component;
      } else if (component instanceof JComponent) {
        return (SelectionProvider) ((JComponent)component).getClientProperty(
          "SelectionProvider");
      } else {
        return null;
      }
    }

    public static void setSelectionProvider(
      JComponent component,
      SelectionProvider provider) {
      component.putClientProperty("SelectionProvider", provider);
    }
  }
}
