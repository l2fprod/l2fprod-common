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

import java.io.File;
import java.util.Map;

import javax.swing.JComponent;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.support.AbstractBinder;

import com.l2fprod.common.swing.JDirectoryChooser;

public class JDirectoryChooserBinder extends AbstractBinder {

  protected JDirectoryChooserBinder() {
    super(File.class);
  }

  protected JComponent createControl(Map context) {
    return new JDirectoryChooser();
  }

  protected Binding doBind(JComponent control, FormModel formModel,
    String formPropertyPath, Map context) {
    final JDirectoryChooser directoryChooser = (JDirectoryChooser)control;
    return new JDirectoryChooserBinding(formModel, formPropertyPath,
      directoryChooser);
  }

}
