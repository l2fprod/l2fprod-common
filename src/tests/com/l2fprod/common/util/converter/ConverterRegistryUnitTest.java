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
package com.l2fprod.common.util.converter;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;

import junit.framework.TestCase;

/**
 * ConverterRegistryUnitTest. <br>
 *  
 */
public class ConverterRegistryUnitTest extends TestCase {

  public ConverterRegistryUnitTest(String name) {
    super(name);
  }

  public void testNumbers() {
    ConverterRegistry reg = ConverterRegistry.instance();
    
    assertEquals(new Double(15), reg.convert(Double.class, "15"));
    assertEquals(new Long(15), reg.convert(Long.class, "15"));
    assertEquals(new Integer(15), reg.convert(Integer.class, "15"));
    assertEquals(new Float(15), reg.convert(Float.class, "15"));
    assertEquals(new Short((short)15), reg.convert(Short.class, "15"));
  }
  
  public void testAWT() {
    ConverterRegistry reg = ConverterRegistry.instance();

    Dimension dim = new Dimension();
    Rectangle rect = new Rectangle();
    Insets insets = new Insets(0, 0, 0, 0);
    Point point = new Point();
    Font font = new Font("SansSerif", 0, 12);

    reg.convert(String.class, dim);
    reg.convert(String.class, rect);
    reg.convert(String.class, insets);
    reg.convert(String.class, point);
    reg.convert(String.class, font);

    reg.convert(Dimension.class, (String) null);
    assertEquals(
      new Dimension(100, 100),
      reg.convert(Dimension.class, "100 x100"));
    assertEquals(
      new Rectangle(5, 7, 100, 50),
      reg.convert(Rectangle.class, "5 7 100 50.0"));
    assertEquals(
      new Insets(154, 878, 911, 0),
      reg.convert(Insets.class, "154 878 911 0"));
    assertEquals(new Point(8, 888), reg.convert(Point.class, "8 888"));

    assertConversionError(reg, Dimension.class, "100 100");
    assertConversionError(reg, Dimension.class, "asd100a30");
    assertConversionError(reg, Point.class, "123");
    assertConversionError(reg, Point.class, "123x100");
    assertConversionError(reg, Point.class, "1 2 3");
    assertConversionError(reg, Rectangle.class, "1 2 3 4 5");
    assertConversionError(reg, Rectangle.class, "12345");
    assertConversionError(reg, Rectangle.class, "1 2 5");
    assertConversionError(reg, Rectangle.class, "1x 2 3x 4 x 5");
    assertConversionError(reg, Insets.class, "1 2 3 4 5");
    assertConversionError(reg, Insets.class, "12345");
    assertConversionError(reg, Insets.class, "1 2 5");
    assertConversionError(reg, Insets.class, "1x 2 3x 4 x 5");
    
    assertConversionError(reg, Font.class, new Rectangle());
  }

  private void assertConversionError(
    Converter converter,
    Class type,
    Object value) {
    try {
      converter.convert(type, value);
      assertFalse("Exception was expected for " + type + " and " + value, true);
    } catch (IllegalArgumentException e) {
      // expected
    }
  }
}
