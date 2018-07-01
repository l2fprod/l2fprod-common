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
package com.l2fprod.common.springrcp;

import com.l2fprod.common.swing.JDirectoryChooser;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JComponent;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.support.CustomBinding;

public class JDirectoryChooserBinding extends CustomBinding {

  private final JDirectoryChooser component;

  public JDirectoryChooserBinding(FormModel model, String property,
    JDirectoryChooser component) {
    super(model, property, File.class);
    this.component = component;
  }

  protected JComponent doBindControl() {
    component.setSelectedFile((File)getValue());
    component.addPropertyChangeListener(new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();
        if (JDirectoryChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(prop)) {
          File file = (File)evt.getNewValue();
          controlValueChanged(file);
        }
      }
    });
    return component;
  }

  protected void readOnlyChanged() {
    component.setEnabled(isEnabled() && !isReadOnly());
  }

  protected void enabledChanged() {
    component.setEnabled(isEnabled() && !isReadOnly());
  }

  protected void valueModelChanged(Object newValue) {
    component.setSelectedFile((File)newValue);
  }
  
}
