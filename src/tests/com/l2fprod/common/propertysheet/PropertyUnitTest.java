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
package com.l2fprod.common.propertysheet;

import com.l2fprod.common.beans.BaseBeanInfo;

import java.beans.BeanInfo;
import java.beans.Introspector;

import javax.swing.JLabel;
import javax.swing.JPanel;

import junit.framework.TestCase;

/**
 * PropertyUnitTest.<br>
 *
 */
public class PropertyUnitTest extends TestCase {

  public PropertyUnitTest(String arg0) {
    super(arg0);
  }

  public void testBasic() {
    DefaultProperty prop = new DefaultProperty();
    prop.setName("name");
    assertEquals("name", prop.getName());
  }
  
  public void testJPanel() throws Exception {
    JPanel panel = new JPanel();
    BeanInfo beanInfo = Introspector.getBeanInfo(JPanel.class);
    PropertySheetPanel sheet = new PropertySheetPanel();
    sheet.setBeanInfo(beanInfo);
    sheet.readFromObject(panel);
  }

  public void testCustomJLabel() throws Exception {
    ExtJLabel label = new ExtJLabel();
    ExtJLabelBeanInfo beanInfo = new ExtJLabelBeanInfo();
    PropertySheetPanel sheet = new PropertySheetPanel();
    sheet.setBeanInfo(beanInfo);
    sheet.readFromObject(label);
  }

  static class ExtJLabel extends JLabel {
  }

  static class ExtJLabelBeanInfo extends BaseBeanInfo {
    public ExtJLabelBeanInfo() {
      super(ExtJLabel.class);
      addProperty("text");
    }
  }
}
