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

import com.l2fprod.common.swing.plaf.ButtonBarButtonUI;
import com.l2fprod.common.swing.plaf.blue.BlueishButtonBarUI;
import com.l2fprod.common.swing.plaf.blue.BlueishButtonUI;
import com.l2fprod.common.swing.plaf.misc.IconPackagerButtonBarUI;

import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.metal.MetalButtonUI;

import junit.framework.TestCase;

/**
 * Test for JButtonBar. <br>
 *  
 */
public class JButtonBarUnitTest extends TestCase {

  public JButtonBarUnitTest(String arg0) {
    super(arg0);
  }

  /**
   * @todo replace the Class.forName with
   *       LookAndFeelAddons.initDefaultAddon() when added
   * @throws Exception
   */
  public void testUIUpdate() throws Exception {
    // init the default UIs
    Class.forName(JButtonBar.class.getName());
    
    // default ui will be IconPackagerButtonBarUI
    UIManager.put("ButtonBarUI", IconPackagerButtonBarUI.class.getName());

    JButtonBar bar = new JButtonBar();
    JButton button1 = new JButton();
    int initialChangeListenerCount =
      button1.getPropertyChangeListeners("UI").length;
    assertTrue(bar.getUI() instanceof IconPackagerButtonBarUI);

    bar.add(button1);
    // as soon as a button gets added to a JButtonBar, its UI will have to be
    // updated to a ButtonBarButtonUI
    assertTrue(button1.getUI() instanceof ButtonBarButtonUI);
    // in the meantime, we added a listener on the UI
    assertEquals(
      initialChangeListenerCount + 1,
      button1.getPropertyChangeListeners("UI").length);

    // whenever the look and feel changes, the UI of the button is restored to
    // a ButtonBarButtonUI
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    SwingUtilities.updateComponentTreeUI(bar);
    assertTrue(button1.getUI() instanceof ButtonBarButtonUI);

    // now changes the look of the ButtonBarUI
    bar.setUI(new BlueishButtonBarUI());

    // its buttons must have changed too
    assertTrue(button1.getUI() instanceof BlueishButtonUI);

    // try to change the UI of the button, it should revert to a ButtonBarUI
    button1.setUI(new MetalButtonUI());
    // the listener will revert the ui
    assertTrue(button1.getUI() instanceof BlueishButtonUI);

    // remove the button from the bar
    bar.remove(button1);
    // our UI listener should have been removed
    assertEquals(
        initialChangeListenerCount,
        button1.getPropertyChangeListeners("UI").length);
    
    // try to change the UI of the button, it must work as it is no longer
    // tracked.
    ButtonUI ui = new MetalButtonUI();
    button1.setUI(ui);
    assertEquals(ui, button1.getUI());
  }
}
