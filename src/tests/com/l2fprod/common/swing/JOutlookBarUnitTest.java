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

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import junit.framework.TestCase;

public class JOutlookBarUnitTest extends TestCase {

  public JOutlookBarUnitTest(String arg0) {
    super(arg0);
  }

  public void testInsertBug() {
    JOutlookBar bar = new JOutlookBar();

    JPanel tab1 = new JPanel() {
      public String toString() {
        return "tab1";
      }
    };
    bar.addTab("Tab 1", tab1);
    assertEquals(bar.getTabCount(), bar.extendedPages.size());

    JPanel tab0 = new JPanel() {
      public String toString() {
        return "tab0";
      }
    };
    bar.insertTab("Tab 0", null, tab0, "tip", 0);
    assertEquals(bar.getTabCount(), bar.extendedPages.size());

    JPanel tab2 = new JPanel() {
      public String toString() {
        return "tab2";
      }
    };
    bar.addTab("Tab 2", tab2);
    assertEquals(bar.getTabCount(), bar.extendedPages.size());

    // bar.getComponent(0) is a tab button
    assertEquals(tab0, bar.getComponent(1));
    // bar.getComponent(2) is a tab button;
    assertEquals(tab1, bar.getComponent(3));
    // bar.getComponent(4) is a tab button;
    assertEquals(tab2, bar.getComponent(5));

    assertEquals(0, bar.indexOfComponent(tab0));
    assertEquals(1, bar.indexOfComponent(tab1));
    
    bar.remove(tab0);
    bar.remove(tab1);
    bar.remove(tab2);
    
    assertEquals(0, bar.getComponentCount());
    assertEquals(0, bar.extendedPages.size());
  }

  public void testBar() {
    JOutlookBar bar = new JOutlookBar();
    try {
      bar.setAlignmentAt(0, SwingConstants.LEFT);
      assertTrue(false);
    } catch (IndexOutOfBoundsException e) {
      // expected
    }

    bar.add(new JPanel());
    bar.add(new JPanel());
    assertEquals(2, bar.extendedPages.size());

    bar.setAlignmentAt(0, SwingConstants.CENTER);
    assertEquals(SwingConstants.CENTER, bar.getAlignmentAt(0));
    bar.setAlignmentAt(0, SwingConstants.LEFT);
    assertEquals(SwingConstants.LEFT, bar.getAlignmentAt(0));
    bar.setAlignmentAt(0, SwingConstants.RIGHT);
    assertEquals(SwingConstants.RIGHT, bar.getAlignmentAt(0));

    bar.setAllTabsAlignment(SwingConstants.LEFT);
    assertEquals(SwingConstants.LEFT, bar.getAlignmentAt(0));
    assertEquals(SwingConstants.LEFT, bar.getAlignmentAt(1));

    bar.removeAll();
    assertEquals(0, bar.extendedPages.size());
  }

}
