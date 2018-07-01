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
package com.l2fprod.common.util;

import junit.framework.TestCase;

/**
 * JVMTest.<br>
 *
 */
public class JVMTest extends TestCase {

  public JVMTest(String p_Name) {
    super(p_Name);
  }
  
  public void testAll() {
    JVM jvm = new JVM("1.5.0");
    assertFalse(jvm.isOneDotOne());
    assertTrue(jvm.isOneDotFive());
    
    jvm = new JVM("1.4.2_02");
    assertTrue(jvm.isOneDotFour());
    assertTrue(jvm.isOrLater(JVM.JDK1_3));    
  }
}
