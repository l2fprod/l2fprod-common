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

import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.command.CommandGroupFactoryBean;
import org.springframework.richclient.command.CommandRegistry;

public class JTaskPaneFactoryBean extends CommandGroupFactoryBean {

  private String groupId;
  private Object[] encodedMembers;
  private CommandRegistry registry;

  public void setBeanName(String beanName) {
    this.groupId = beanName;
  }

  public void setMembers(Object[] members) {
    this.encodedMembers = members;
  }

  public void setCommandRegistry(CommandRegistry registry) {
    this.registry = registry;
  }

  protected CommandGroup createCommandGroup() {
    JTaskPaneCommandGroup group = new JTaskPaneCommandGroup(groupId, registry);

    if (encodedMembers != null) {
      for (int i = 0; i < encodedMembers.length; i++) {
        JTaskPaneGroupCommandGroup childGroup = (JTaskPaneGroupCommandGroup)encodedMembers[i];
        group.addTaskPaneGroup(childGroup);
      }
    }
    
    return group;
  }
}
