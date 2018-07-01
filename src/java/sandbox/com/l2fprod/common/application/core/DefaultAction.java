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
package com.l2fprod.common.application.core;

import com.l2fprod.common.util.OS;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.KeyStroke;

/**
 * DefaultAction. <br>
 */
class DefaultAction extends AbstractAction implements UpdateableAction {

  private String enablesFor;

  private Class objectType;

  private String commandClass;

  private ActionUpdater updater;

  public DefaultAction() {
  }
  
  private Command newCommand() {
    try {
      Command command = (Command)Class.forName(commandClass).newInstance();
      return command;
    } catch (InstantiationException e) {
      throw new RuntimeException("Can't construct command " + commandClass, e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException("Can't construct commmand " + commandClass, e);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("Can't find command " + commandClass, e);
    }
  }

  public final void actionPerformed(ActionEvent e) {
    CommandContext context = (CommandContext)getValue("commandContext");
    newCommand().execute(context);
  }

  public final void update(CommandContext context) {
    getUpdater().update(this, context);
  }

  public void setId(String value) {
    putValue("id", value);
  }

  public String getId() {
    return (String)getValue("id");
  }

  public void setText(String text) {
    putValue(NAME, text);
  }

  public String getText() {
    return (String)getValue(NAME);
  }

  public void setSmallIcon(Icon icon) {
    putValue(SMALL_ICON, icon);
  }

  public Icon getSmallIcon() {
    return (Icon)getValue(SMALL_ICON);
  }

  public void setMnemonic(char mnemonic) {
    putValue("mnemonic", new Character(mnemonic));
  }

  public char getMnemonic() {
    Character mnemonic = (Character)getValue("mnemonic");
    if (mnemonic == null) {
      return 0;
    } else {
      return mnemonic.charValue();
    }
  }

  public void setCommand(String commandClass) {
    this.commandClass = commandClass;
  }

  public String getCommand() {
    return commandClass;
  }

  public void setObjectType(String objectClass) {
    try {
      this.objectType = Class.forName(objectClass);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("Can't find object type", e);
    }
  }

  public Class getObjectType() {
    return objectType;
  }

  public void setEnablesFor(String enablesFor) {
    this.enablesFor = enablesFor;
  }

  public void setUpdater(String updaterClass) {
    try {
      this.updater = (ActionUpdater)Class.forName(updaterClass).newInstance();
    } catch (InstantiationException e) {
      throw new RuntimeException("Can't construct updater " + updaterClass, e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException("Can't construct updater " + updaterClass, e);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("Can't find updater " + updaterClass, e);
    }
  }

  /**
   * Sets the accelerator for this action. The accelerator string
   * <code>accel</code> is used to build a
   * {@link javax.swing.KeyStroke}. To support different operating
   * system behavior a special modifier named <code>menukey</code>
   * can be used to identify the operating system dependent modifier
   * to use in menus (such as "ctrl" on Windows platforms or "Command"
   * key on Mac OS X).
   * 
   * @param accel
   */
  public void setAccelerator(String accel) {
    String acceleratorString;
    if (OS.isMacOSX()) {
      acceleratorString = accel.replaceAll("menukey", "meta");
    } else {
      acceleratorString = accel.replaceAll("menukey", "ctrl");
    }
    KeyStroke accelerator = KeyStroke.getKeyStroke(acceleratorString);
    putValue(ACCELERATOR_KEY, accelerator);
  }

  public KeyStroke getAccelerator() {
    return (KeyStroke)getValue(ACCELERATOR_KEY);
  }

  private ActionUpdater getUpdater() {
    if (updater == null) {
      // no updater was provider at init
      // build a default updater
      if (objectType == null && enablesFor != null) {
        updater = new DefaultActionUpdater(enablesFor);
      } else if (objectType != null) {
        updater = new DefaultActionUpdater(objectType, enablesFor);
      } else {
        updater = ActionUpdater.SET_ENABLED_TRUE;
      }
    }
    return updater;
  }

  public String toString() {
    return super.toString() + "[" + paramString() + "]";
  }

  protected String paramString() {
    return "id=" + getId() + ",text=" + getText() + ",mnemonic="
        + getMnemonic() + ",command=" + getCommand() + ",enablesFor="
        + enablesFor + ",acccel=" + getAccelerator();
  }

}