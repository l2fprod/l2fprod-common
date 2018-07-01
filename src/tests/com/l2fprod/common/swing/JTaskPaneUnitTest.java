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

import javax.swing.UIManager;

import junit.framework.TestCase;

public class JTaskPaneUnitTest extends TestCase {

  public JTaskPaneUnitTest(String arg0) {
    super(arg0);
  }

  public void testJTaskPaneGroup() {
    JTaskPaneGroup group = new JTaskPaneGroup();
    group.setAnimated(true);
    assertTrue(group.isAnimated());
    group.setAnimated(false);
    assertFalse(group.isAnimated());

    UIManager.put("TaskPaneGroup.animate", Boolean.FALSE);
    group = new JTaskPaneGroup();
    assertFalse(group.isAnimated());

    UIManager.put("TaskPaneGroup.animate", null);
    group = new JTaskPaneGroup();
    assertTrue(group.isAnimated());
  }

  public void testGroupOfJTaskPaneGroups() {
    JTaskPaneGroup tp1 = new JTaskPaneGroup();
    tp1.setTitle("tp1");
    JTaskPaneGroup tp2 = new JTaskPaneGroup();
    tp2.setTitle("tp2");
    
    // collapse both taskpanes at the beginning
    tp1.setExpanded(false);
    tp2.setExpanded(false);

    GroupOfTaskPaneGroup group = new GroupOfTaskPaneGroup();
    group.add(tp1);
    group.add(tp2);
    // both taskpanes remain collapsed
    assertFalse(tp1.isExpanded());
    assertFalse(tp2.isExpanded());

    // the first task pane gets expanded
    tp1.setExpanded(true);
    assertTrue(tp1.isExpanded());
    assertFalse(tp2.isExpanded());

    // the second task pane gets expanded
    tp2.setExpanded(true);
    assertTrue(tp2.isExpanded());
    // the first gets collapsed
    assertFalse(tp1.isExpanded());

    // remove the second taskpane from the group
    group.remove(tp2);
    // and expand the first taskpane
    tp1.setExpanded(true);
    // both are now expanded (as the second got removed from the group, it is no
    // longer automatically collapsed)á
    assertTrue(tp1.isExpanded());
    assertTrue(tp2.isExpanded());
  }

}
