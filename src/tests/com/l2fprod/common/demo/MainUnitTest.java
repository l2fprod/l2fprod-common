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
package com.l2fprod.common.demo;

import com.l2fprod.common.Version;
import com.l2fprod.common.swing.JButtonBar;
import com.l2fprod.common.swing.LookAndFeelTweaks;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;

import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.UIManager;

import junit.framework.TestCase;

import org.netbeans.jemmy.ComponentChooser;
import org.netbeans.jemmy.JemmyProperties;
import org.netbeans.jemmy.operators.AbstractButtonOperator;
import org.netbeans.jemmy.operators.ComponentOperator;
import org.netbeans.jemmy.util.PNGEncoder;

/**
 * MainUnitTest. <br>
 *  
 */
public class MainUnitTest extends TestCase {

  private JFrame demo;

  protected void setUp() throws Exception {
    super.setUp();

    JemmyProperties.getProperties().setDispatchingModel(
      JemmyProperties.ROBOT_MODEL_MASK);

    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
    }

    LookAndFeelTweaks.tweak();

    demo = new JFrame("L2FProd.com Common Components "
      + Version.getVersion() + " (build " + Version.getBuildTimestamp() + ")");
    demo.getContentPane().setLayout(new BorderLayout());
    demo.getContentPane().add("Center", new Main());
    demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    demo.setSize(400, 500);
    demo.setLocation(100, 100);
    demo.setVisible(true);
  }

  public void testJButtonBar() throws Exception {
    // iterate through JButtons of JButtonBar
    JButtonBar buttonbar =
      (
        JButtonBar)ComponentOperator
          .waitComponent(demo, new ComponentChooser() {
      public boolean checkComponent(Component arg0) {
        return arg0 instanceof JButtonBar;
      }
      public String getDescription() {
        return null;
      }
    });

    Component[] components = buttonbar.getComponents();
    for (int i = 0, c = components.length; i < c; i++) {
      if (components[i] instanceof AbstractButton) {
        AbstractButton button = (AbstractButton)components[i];
        new AbstractButtonOperator(button).push();
        // after each click on the button, perform a screenshot
        File screenshot = new File("screenshot-" + i + ".png");
        System.out.println("Screenshot in " + screenshot.getAbsolutePath());
        PNGEncoder.captureScreen(
          demo,
          screenshot.getAbsolutePath(),
          PNGEncoder.COLOR_MODE);
      }
    }
  }

  protected void tearDown() throws Exception {
    demo = null;
    super.tearDown();
  }

  public MainUnitTest(String arg0) {
    super(arg0);
  }

}
