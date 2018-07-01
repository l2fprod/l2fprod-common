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

import java.awt.Component;

import javax.swing.Icon;

/**
 * AbstractVisualWorkspaceDocument.<br>
 * 
 */
public class AbstractVisualWorkspaceDocument extends AbstractWorkspaceDocument
    implements
      VisualWorkspaceDocument {

  private Component ui;
  private Icon icon;
  
  public AbstractVisualWorkspaceDocument(Component ui) {
    this.ui = ui;
  }
  
  public Component getVisualComponent() {
    return ui;
  }
  
  
  public void setIcon(Icon icon) {
    Icon old = this.icon;
    this.icon = icon;
    support.firePropertyChange("icon", old, icon);
  }
  
  public Icon getIcon() {
    return icon;
  }
  
  
  protected String paramString() {
    return super.paramString() +
    ",component=" + getVisualComponent() +
    ",icon=" + getIcon();
  }
  
}
