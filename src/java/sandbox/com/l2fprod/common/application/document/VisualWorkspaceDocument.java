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
 * Extends the WorkspaceDocument to provide a graphical representation
 * of the document. Workspaces will be a container for this
 * representation.
 */
public interface VisualWorkspaceDocument extends WorkspaceDocument {

  /**
   * @return the graphical representation of the document.
   */
  public Component getVisualComponent();

  /**
   * @return the icon associated with this document
   */
  public Icon getIcon();

  /**
   * Sets the icon of this document
   * 
   * @param icon
   */
  public void setIcon(Icon icon);

}