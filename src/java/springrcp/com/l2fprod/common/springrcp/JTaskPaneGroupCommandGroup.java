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

import com.l2fprod.common.swing.JTaskPaneGroup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.command.CommandRegistry;
import org.springframework.richclient.command.SwingActionAdapter;

public class JTaskPaneGroupCommandGroup extends CommandGroup {

  private List actions = new ArrayList();

  private boolean expanded;
  private boolean special;
  private boolean scrollOnExpand;
  private boolean animated;

  public JTaskPaneGroupCommandGroup(String groupId, CommandRegistry registry) {
    super(groupId, registry);
  }

  public void setExpanded(boolean expanded) {
    if (hasChanged(isExpanded(), expanded)) {
      this.expanded = expanded;
      firePropertyChange(JTaskPaneGroup.EXPANDED_CHANGED_KEY, !expanded,
        expanded);
    }
  }

  public boolean isExpanded() {
    return expanded;
  }

  public boolean isAnimated() {
    return animated;
  }

  public void setAnimated(boolean animated) {
    if (hasChanged(isAnimated(), animated)) {
      this.animated = animated;
      firePropertyChange(JTaskPaneGroup.ANIMATED_CHANGED_KEY, !animated,
        animated);
    }
  }

  public boolean isScrollOnExpand() {
    return scrollOnExpand;
  }

  public void setScrollOnExpand(boolean scrollOnExpand) {
    if (hasChanged(isScrollOnExpand(), scrollOnExpand)) {
      this.scrollOnExpand = scrollOnExpand;
      firePropertyChange(JTaskPaneGroup.SCROLL_ON_EXPAND_CHANGED_KEY,
        !scrollOnExpand, scrollOnExpand);
    }
  }

  public boolean isSpecial() {
    return special;
  }

  public void setSpecial(boolean special) {
    if (hasChanged(isSpecial(), special)) {
      this.special = special;
      firePropertyChange(JTaskPaneGroup.SPECIAL_CHANGED_KEY, !special, special);
    }
  }

  public JTaskPaneGroup createTaskPaneGroup() {
    JTaskPaneGroup taskpane = new JTaskPaneGroup();
    taskpane.setExpanded(isExpanded());
    taskpane.setTitle(getText());
    taskpane.setAnimated(isAnimated());
    taskpane.setScrollOnExpand(isScrollOnExpand());
    taskpane.setSpecial(isSpecial());
    if (isFaceConfigured()) {
      taskpane.setToolTipText(getFaceDescriptor().getCaption());
    }

    for (Iterator members = actions.iterator(); members.hasNext();) {
      ActionCommand member = (ActionCommand)members.next();

      SwingActionAdapter adapter = new SwingActionAdapter(member);
      taskpane.add(adapter);
    }
    return taskpane;
  }

  public void addActionCommand(ActionCommand command) {
    super.add(command);
    actions.add(command);
  }

}