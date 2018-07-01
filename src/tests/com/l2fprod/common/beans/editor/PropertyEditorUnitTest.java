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
package com.l2fprod.common.beans.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.beans.PropertyEditor;
import java.io.File;
import java.util.Date;

import junit.framework.TestCase;

/**
 * PropertyEditorUnitTest. <br>
 */
public class PropertyEditorUnitTest extends TestCase {

  public PropertyEditorUnitTest(String name) {
    super(name);
  }

  public void testBasic() throws Exception {
    Object[] editorsAndValues = new Object[]{
        BooleanAsCheckBoxPropertyEditor.class, Boolean.TRUE,
        BooleanPropertyEditor.class, Boolean.TRUE,
        ColorPropertyEditor.class, Color.black,
        FilePropertyEditor.class, new File(".").getCanonicalFile(),
        DimensionPropertyEditor.class, new Dimension(453, 120),
        DoublePropertyEditor.class, new Double(34455555),
        DoublePropertyEditor.class, new Double(344555553223.221445554),
        DoublePropertyEditor.class, new Double(-1257344555553223.221445554987987),
        FloatPropertyEditor.class, new Float(232.3343455),
        FloatPropertyEditor.class, new Float(-0.0000012),
        FontPropertyEditor.class, new Font("SansSerif", Font.BOLD, 35),
        InsetsPropertyEditor.class, new Insets(7, 8, 3, 1),
        IntegerPropertyEditor.class, new Integer(12345678),
        LongPropertyEditor.class, new Long(122922829339L),
        LongPropertyEditor.class, new Long(-122922829339L),
        RectanglePropertyEditor.class, new Rectangle(-234, 455, 55, -23),
        ShortPropertyEditor.class, new Short("12"),
        StringPropertyEditor.class, "a String",       
        JCalendarDatePropertyEditor.class, new Date(),
        //PENDING(fred): This test does not pass as JCalendar does
        // not accept "null" date
        // JCalendarDatePropertyEditor.class, null,
        NachoCalendarDatePropertyEditor.class, new Date(),
        //PENDING(fred): This test does not pass as NachoCalendar does
        // not accept "null" date
        // NachoCalendarDatePropertyEditor.class, null,
    };

    for (int i = 0, c = editorsAndValues.length; i < c; i = i + 2) {
      PropertyEditor editor = (PropertyEditor)((Class)editorsAndValues[i])
          .newInstance();
      editor.setValue(editorsAndValues[i + 1]);
      Object value = editor.getValue();
      assertTrue(editor.getClass().getName() + ", expected " +
          editorsAndValues[i + 1] + 
          " but was " + value,
          value == editorsAndValues[i + 1] ||
          (editorsAndValues[i + 1] != null && editorsAndValues[i + 1].equals(value)));
    }
  }
}