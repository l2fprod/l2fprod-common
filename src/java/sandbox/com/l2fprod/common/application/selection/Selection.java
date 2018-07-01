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
package com.l2fprod.common.application.selection;

import com.l2fprod.common.util.filtering.Filter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Selection.<br>
 *
 */
public interface Selection {

  /**
   * Returns an array of the selected objects
   * @return the selected objects
   */
  Object[] getSelection();
  
  boolean isEmpty();
 
 	class Helper {
 		
 		public static Object[] filter(Selection selection, final Class clazz) {
 			return filter(selection, new Filter() {
 				public boolean accept(Object o) {
 					return clazz.isInstance(o);
 				}
 			}, clazz);
 		}
 		
		public static Object[] filter(Selection selection, Filter filter, Class clazz) {
 			List array = new ArrayList();
 			if (!selection.isEmpty()) {
 				Object[] selected = selection.getSelection();
 				for (int i = 0, c = selected.length; i < c; i++) {
 					if (filter.accept(selected[i])) {
 						array.add(selected[i]);
 					}
 				}
 			}
 			Object[] returned = (Object[])Array.newInstance(clazz, array.size());
 			return array.toArray(returned); 
 		}
 		
 	}

}
