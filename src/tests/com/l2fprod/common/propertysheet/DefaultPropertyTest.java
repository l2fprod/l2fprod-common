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

import junit.framework.TestCase;

/**
 * Unit test for class {@link DefaultProperty}.
 * 
 * @author <a href="mailto:juergen@heidak.de">Juergen Heidak</a>
 */
public class DefaultPropertyTest extends TestCase {

  /**
   * Constructor for DefaultPropertyTest.
   * 
   * @param name
   */
  public DefaultPropertyTest(String name) {
    super(name);
  }

  /**
   * Run this test using the textual ui.
   * 
   * @param args Ignored.
   */
  public static void main(String[] args) {
    junit.textui.TestRunner.run(DefaultPropertyTest.class);
  }

  /**
   * Test a cloned <code>DefaultProperty</code> has its
   * <code>value</code> property copied.
   */
  public void testCloneCopiesValue() {
    DefaultProperty dp1 = new DefaultProperty();
    dp1.setName("name");
    dp1.setDisplayName("displayName");
    dp1.setCategory("Category");
    dp1.setValue("dummy value");

    DefaultProperty clone = (DefaultProperty)dp1.clone();
    assertEquals(dp1.getName(), clone.getName());
    assertEquals(dp1.getDisplayName(), clone.getDisplayName());
    assertEquals(dp1.getCategory(), clone.getCategory());
    assertSame("Test value reference is copied", dp1.getValue(), clone
      .getValue());
  }

  /**
   * Test method {@link DefaultProperty#equals(Object)}.
   */
  public void testEquals() {
    DefaultProperty dp1 = new DefaultProperty();
    dp1.setName("name");
    dp1.setEditable(false);
    dp1.setValue("dummy value");

    DefaultProperty clone = (DefaultProperty)dp1.clone();

    assertEquals("Test 1", dp1, clone);
    assertEquals("Test 2", dp1, dp1);
    assertEquals("Test 3", clone, clone);

    // value is not considered in equals
    /*
    clone.setValue("changed value");
    assertTrue("Test 10", !dp1.equals(clone));

    dp1.setValue("changed value");
    assertEquals("Test 11", dp1, clone);
    */
    
    dp1.setEditable(!dp1.isEditable());
    assertTrue("Test 12", !dp1.equals(clone));
  }

  /**
   * Test method {@link DefaultProperty#hashCode()}.
   */
  public void testHashCode() {
    DefaultProperty dp1 = new DefaultProperty();
    dp1.setName("name");
    dp1.setEditable(false);
    dp1.setValue("dummy value");

    DefaultProperty clone = (DefaultProperty)dp1.clone();

    assertEquals("Test 1", dp1.hashCode(), clone.hashCode());

    clone.setName("second name");
    assertTrue("Test 5", dp1.hashCode() != clone.hashCode());

    dp1.setName("second name");
    assertEquals("Test 10", dp1.hashCode(), clone.hashCode());
  }
  
}
