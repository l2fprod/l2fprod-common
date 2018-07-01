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

import com.l2fprod.common.swing.icons.EmptyIcon;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import junit.framework.TestCase;

/**
 * WorkspaceUnitTest. <br>
 */
public class WorkspaceUnitTest extends TestCase {

  public WorkspaceUnitTest(String name) {
    super(name);
  }

  public void testWKS() throws Exception {
    // create a new empty workspace
    Workspace wks = new AbstractWorkspace();

    // create a new document
    WorkspaceDocument doc = new AbstractWorkspaceDocument();
    // after creation, a document has no workspace and is closed
    assertNull(doc.getWorkspace());
    assertTrue(doc.isClosed());

    // open this document
    wks.open(doc);
    // ensure it is in this workspace
    assertEquals(wks, doc.getWorkspace());
    // and it is now opened
    assertFalse(doc.isClosed());

    // it can not be opened twice
    try {
      wks.open(doc);
      assertTrue("a doc can not be added twice", false);
    } catch (IllegalArgumentException e) {
    }

    // listener can be added to prevent the document from being closed
    VetoableChangeListener preventClosing = new VetoableChangeListener() {

      public void vetoableChange(PropertyChangeEvent evt)
          throws PropertyVetoException {
        throw new PropertyVetoException("document will not close", evt);
      }
    };
    doc.addVetoableChangeListener(preventClosing);

    try {
      doc.setClosed(true);
      assertTrue("not reached", false);
    } catch (PropertyVetoException e) {
      // expected
    }
    // without the listener, it will close without problems.
    doc.removeVetoableChangeListener(preventClosing);
    doc.setClosed(true);

    // add again the listener and close again the document, closing a
    // closed document does nothing
    doc.addVetoableChangeListener(preventClosing);
    try {
      doc.setClosed(true);
    } catch (PropertyVetoException e) {
      assertTrue("not reached", false);
    }

    assertTrue(doc.isClosed());
    assertNull(doc.getWorkspace());
    assertEquals(0, wks.getDocuments().length);
  }
  
  public void testWKSSelection() throws Exception {
    WorkspaceDocument doc1 = new AbstractWorkspaceDocument();
    WorkspaceDocument doc2 = new AbstractWorkspaceDocument();
    Workspace wks = new AbstractWorkspace();
    wks.open(doc1);
    wks.open(doc2);
    doc1.setSelected(true);
    assertEquals(doc1, wks.getSelected());
    assertTrue(doc1.isSelected());
    assertFalse(doc2.isSelected());
    doc2.setSelected(true);
    assertTrue(doc2.isSelected());
    assertFalse(doc1.isSelected());
  }

  public void testTabbedWKS() throws Exception {
    TabbedWorkspace wks = new TabbedWorkspace();

    VisualWorkspaceDocument doc = new AbstractVisualWorkspaceDocument(
        new JPanel());
    wks.open(doc);
    assertNull(((JTabbedPane)wks.getVisualComponent()).getIconAt(0));

    Icon empty = new EmptyIcon();
    doc.setIcon(empty);
    doc.setTitle("title");

    assertEquals(1, ((JTabbedPane)wks.getVisualComponent()).getTabCount());
    assertEquals(doc.getIcon(), ((JTabbedPane)wks.getVisualComponent())
        .getIconAt(0));
    assertEquals(doc.getTitle(), ((JTabbedPane)wks.getVisualComponent())
        .getTitleAt(0));
    wks.close(doc);
    assertEquals(0, ((JTabbedPane)wks.getVisualComponent()).getTabCount());
  }
}