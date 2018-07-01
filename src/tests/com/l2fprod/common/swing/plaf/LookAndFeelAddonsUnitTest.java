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
package com.l2fprod.common.swing.plaf;

import com.l2fprod.common.swing.plaf.metal.MetalLookAndFeelAddons;
import com.l2fprod.common.swing.plaf.windows.WindowsLookAndFeelAddons;
import com.l2fprod.common.util.OS;

import javax.swing.UIManager;

import junit.framework.TestCase;

public class LookAndFeelAddonsUnitTest extends TestCase {

  public LookAndFeelAddonsUnitTest(String arg0) {
    super(arg0);
  }

  public void testTrackingChanges() throws Exception {
    // track look and feel changes
    LookAndFeelAddons.setTrackingLookAndFeelChanges(true);
    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    assertTrue(LookAndFeelAddons.getAddon() instanceof MetalLookAndFeelAddons);

    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    if (OS.isWindows()) {
      assertTrue(LookAndFeelAddons.getAddon() instanceof WindowsLookAndFeelAddons);
    } else {
      //
    }
    
    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    assertTrue(LookAndFeelAddons.getAddon() instanceof MetalLookAndFeelAddons);

    // stop tracking
    LookAndFeelAddons.setTrackingLookAndFeelChanges(false);
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    assertTrue("Addon is " + LookAndFeelAddons.getAddon(), LookAndFeelAddons
      .getAddon() instanceof MetalLookAndFeelAddons);    
  }
}
