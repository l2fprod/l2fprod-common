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
package com.l2fprod.common.demo.tasks;

import com.l2fprod.common.swing.JTaskPane;
import com.l2fprod.common.swing.JTaskPaneGroup;

import java.awt.BorderLayout;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

/**
 * Nested taskpanes and actions to create new.<br>
 */
public class TaskPaneDynamic extends JPanel {

  public TaskPaneDynamic() {
    setLayout(new BorderLayout());

    JTaskPane taskPane = new JTaskPane();
    add("Center", new JScrollPane(taskPane));

    JTaskPaneGroup group = new JTaskPaneGroup();
    group.setTitle("First Group");
    taskPane.add(group);
    
    addNestedGroupAction(group);
    addSiblingGroupAction(group);
  }

  void addNestedGroupAction(final JTaskPaneGroup parent) {
    Action addNestedGroup = new AbstractAction("Add nested JTaskPaneGroup") {
      public void actionPerformed(java.awt.event.ActionEvent e) {
        JTaskPaneGroup nested = new JTaskPaneGroup();
        nested.setTitle("Nested");
        addNestedGroupAction(nested);
        addSiblingGroupAction(nested);
        addRemoveThisGroupAction(nested);
        
        parent.add(nested);
        parent.revalidate();
        parent.repaint();
      }
    };
    parent.add(addNestedGroup);        
  }

  void addSiblingGroupAction(final JTaskPaneGroup sibling) {
    Action addNestedGroup = new AbstractAction("Add sibling JTaskPaneGroup") {
      public void actionPerformed(java.awt.event.ActionEvent e) {
        JTaskPaneGroup newSibling = new JTaskPaneGroup();
        newSibling.setTitle("Sibling");
        addNestedGroupAction(newSibling);
        addSiblingGroupAction(newSibling);
        addRemoveThisGroupAction(newSibling);
        
        sibling.getParent().add(newSibling);
        ((JComponent)sibling.getParent()).revalidate();
        ((JComponent)sibling.getParent()).repaint();
      }
    };
    sibling.add(addNestedGroup);            
  }
  
  void addRemoveThisGroupAction(final JTaskPaneGroup group) {
    Action removeThisGroup = new AbstractAction("Remove this group") {
      public void actionPerformed(java.awt.event.ActionEvent e) {
        // get the parent before
        JComponent parent = (JComponent)group.getParent();
        
        parent.remove(group);
        parent.revalidate();
        parent.repaint();
      }
    };
    group.add(removeThisGroup);
  }
  
  /**
   * @param args
   */
  public static void main(String[] args) throws Exception {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

    JFrame frame = new JFrame("TaskPaneDynamic");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    frame.getContentPane().add("Center", new TaskPaneDynamic());
    frame.setSize(300, 500);
    frame.setVisible(true);
  }

}