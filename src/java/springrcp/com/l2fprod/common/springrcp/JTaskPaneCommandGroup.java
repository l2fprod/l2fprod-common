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
package com.l2fprod.common.springrcp;

import com.l2fprod.common.swing.JTaskPane;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.command.CommandRegistry;

public class JTaskPaneCommandGroup extends CommandGroup {

  private List groups = new ArrayList();

  public JTaskPaneCommandGroup(String groupId, CommandRegistry registry) {
    super(groupId, registry);
  }

  public JTaskPane createTaskPane() {
    JTaskPane taskPane = new JTaskPane();
    for (Iterator it = groups.iterator(); it.hasNext();) {
      JTaskPaneGroupCommandGroup member = (JTaskPaneGroupCommandGroup)it.next();
      taskPane.add(member.createTaskPaneGroup());
    }
    return taskPane;
  }

  public void addTaskPaneGroup(JTaskPaneGroupCommandGroup childGroup) {
    super.add(childGroup);
    groups.add(childGroup);
  }

}
