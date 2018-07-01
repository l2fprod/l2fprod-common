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

import com.l2fprod.common.swing.StatusBar;
import com.l2fprod.common.util.ResourceManager;

import java.awt.Component;
import java.awt.Container;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Stack;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Build actions, toolbars, menubars, popup menus from an xml file.
 */
public class XmlUIBuilder {

  public final static String ID = "id";

  public final static String ACTION = "action";
  public final static String ACTION_REF = "action-ref";

  public final static String TOOLBAR = "toolbar";

  public final static String MENUBAR = "menubar";
  public final static String MENU = "menu";
  public final static String MENU_REF = "menu-ref";

  public final static String STATUSBAR = "statusbar";
  public final static String STATUSBARITEM = "statusbaritem";

  public final static String SEPARATOR = "separator";

  private final static Logger logger = Logger.getLogger("XmlUIBuilder");

  private Map actions;

  private Map menus;

  private Map menubars;

  private Map toolbars;

  private Map statusbars;

  private AppContext context;

  public XmlUIBuilder(AppContext context, URL xml)
      throws ParserConfigurationException, SAXException, IOException {

    this.context = context;

    actions = new HashMap();
    menus = new HashMap();
    menubars = new HashMap();
    toolbars = new HashMap();
    statusbars = new HashMap();

    SAXParserFactory factory = SAXParserFactory.newInstance();
    SAXParser parser = factory.newSAXParser();
    DefaultHandler handler = new Handler();
    parser.parse(xml.openStream(), handler);
  }

  public JMenuBar getMenubar(String id) {
    JMenuBar menubar = (JMenuBar)menubars.get(id);
    //    assert menubar != null;
    return menubar;
  }

  public JMenu getMenu(String id) {
    JMenu menu = (JMenu)menus.get(id);
    //    assert menu != null;
    return menu;
  }

  public JToolBar getToolBar(String id) {
    JToolBar toolbar = (JToolBar)toolbars.get(id);
    //    assert toolbar != null;
    return toolbar;
  }

  public StatusBar getStatusBar(String id) {
    StatusBar status = (StatusBar)statusbars.get(id);
    //    assert status != null;
    return status;
  }

  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("actions={");
    for (Iterator iter = actions.values().iterator(); iter.hasNext();) {
      DefaultAction action = (DefaultAction)iter.next();
      buffer.append(action.toString());
      if (iter.hasNext()) {
        buffer.append(',');
      }
    }
    buffer.append("},");
    buffer.append("menus={");
    for (Iterator iter = menus.values().iterator(); iter.hasNext();) {
      JMenu menu = (JMenu)iter.next();
      buffer.append(menu.toString());
      if (iter.hasNext()) {
        buffer.append(',');
      }
    }
    buffer.append("}");
    return buffer.toString();
  }

  private String getResource(String text) {
    try {
      return ResourceManager.resolve(text);
    } catch (MissingResourceException e) {
      e.printStackTrace();
      return text;
    }
  }

  private void applyAttributes(JMenu menu, Attributes attributes) {
    menu.setText(getResource(attributes.getValue("text")));
    String mnemonic = getResource(attributes.getValue("mnemonic"));
    if (mnemonic != null && mnemonic.length() > 0) {
      menu.setMnemonic(mnemonic.charAt(0));
    }
  }

  private void applyAttributes(DefaultAction action, Attributes attributes) {
    action.setId(attributes.getValue(ID));
    action.setText(getResource(attributes.getValue("text")));
    String mnemonic = getResource(attributes.getValue("mnemonic"));
    if (mnemonic != null && mnemonic.length() > 0) {
      action.setMnemonic(mnemonic.charAt(0));
    }
    action.setCommand(attributes.getValue("command"));
    String objectClass = attributes.getValue("objectType");
    if (objectClass != null && objectClass.length() > 0) {
      action.setObjectType(objectClass);
    }
    action.setEnablesFor(attributes.getValue("enablesFor"));
    String icon = attributes.getValue("icon");
    if (icon != null && icon.length() > 0) {
      URL url = getClass().getResource(icon);
      if (url == null) {
        System.out.println("icon " + icon + " not found");
      } else {
        action.setSmallIcon(new ImageIcon(url));
      }
    }
    String accel = attributes.getValue("accelerator");
    if (accel != null && accel.length() > 0) {
      action.setAccelerator(accel);
    }
  }

  private void addMenuToContainer(String menuId, Container parent) {
    logger.info("adding " + menuId + " to " + parent);
    JMenu menu = (JMenu)menus.get(menuId);
    parent.add(menu);
  }

  private void addActionToContainer(String actionId, Container parent) {
    DefaultAction action = (DefaultAction)actions.get(actionId);
    if (parent instanceof JMenu) {
      JMenuItem item = ((JMenu)parent).add(action);
      item.setMnemonic(action.getMnemonic());
    } else if (parent instanceof JToolBar) {
      ((JToolBar)parent).add(action);
    }
  }

  private void addComponentToContainer(Component component, Container parent) {
    parent.add(component);
  }

  class Handler extends DefaultHandler {

    Stack containerStack;

    List doItLaters;

    public void startDocument() {
      containerStack = new Stack();
      doItLaters = new ArrayList();
    }

    public void startElement(String uri, String localName, String qName,
        Attributes attributes) throws SAXException {
      logger.entering(this.getClass().getName(), "startElement");

      try {
        if (MENUBAR.equals(qName)) {
          logger.info("menubar(" + attributes.getValue(ID) + ")");
          JMenuBar menubar = new JMenuBar();
          menubars.put(attributes.getValue(ID), menubar);
          containerStack.push(menubar);
        } else if (TOOLBAR.equals(qName)) {
          logger.info("toolbar(" + attributes.getValue(ID) + ")");
          JToolBar toolbar = new JToolBar();
          toolbar.setFloatable("true".equals(attributes.getValue("floatable")));
          toolbars.put(attributes.getValue(ID), toolbar);
          containerStack.push(toolbar);
        } else if (STATUSBAR.equals(qName)) {
          logger.info("statusbar(" + attributes.getValue(ID) + ")");
          StatusBar statusbar = new StatusBar();
          statusbars.put(attributes.getValue(ID), statusbar);
          containerStack.push(statusbar);
        } else if (STATUSBARITEM.equals(qName)) {
          logger.info("statusbaritem(" + attributes.getValue(ID) + ")");
          String componentClass = attributes.getValue("classname");
          String constraint = attributes.getValue("constraint");
          Component zone = (Component)Class.forName(componentClass)
              .newInstance();
          StatusBar container = (StatusBar)containerStack.peek();
          container.addZone(attributes.getValue(ID), zone, constraint);
        } else if (MENU.equals(qName)) {
          logger.info("menu(" + attributes.getValue(ID) + ")");
          JMenu menu = new JMenu();
          applyAttributes(menu, attributes);
          menus.put(attributes.getValue(ID), menu);
          containerStack.push(menu);
        } else if (MENU_REF.equals(qName)) {
          logger.info("menu-ref(" + attributes.getValue("idref") + ")");
          final Container container = (Container)containerStack.peek();
          final String idref = attributes.getValue("idref");
          Runnable doItLater = new Runnable() {

            public void run() {
              addMenuToContainer(idref, container);
            }
          };
          doItLaters.add(doItLater);
        } else if (ACTION.equals(qName)) {
          // start an action
          DefaultAction action = new DefaultAction();
          applyAttributes(action, attributes);
          actions.put(action.getId(), action);
          context.getActions().add(action);
        } else if (ACTION_REF.equals(qName)) {
          final Container container = (Container)containerStack.peek();
          final String idref = attributes.getValue("idref");
          Runnable doItLater = new Runnable() {

            public void run() {
              addActionToContainer(idref, container);
            }
          };
          doItLaters.add(doItLater);
        } else if (SEPARATOR.equals(qName)) {
          final Container container = (Container)containerStack.peek();
          Runnable doItLater = new Runnable() {

            public void run() {
              addComponentToContainer(new JSeparator(), container);
            }
          };
          doItLaters.add(doItLater);
        }
      } catch (Exception e) {
        e.printStackTrace();
        throw new SAXException(e);
      }
    }

    public void endElement(String uri, String localName, String qName)
        throws SAXException {
      super.endElement(uri, localName, qName);
      if (MENUBAR.equals(qName) || MENU.equals(qName) || TOOLBAR.equals(qName)
          || STATUSBAR.equals(qName)) {
        containerStack.pop();
      }
    }

    public void endDocument() throws SAXException {
      try {
        super.endDocument();

        //        assert containerStack.size() == 0;

        for (Iterator iter = doItLaters.iterator(); iter.hasNext();) {
          Runnable runnable = (Runnable)iter.next();
          runnable.run();
        }
      } catch (Exception e) {
        e.printStackTrace();
        throw new SAXException(e);
      }
    }

    public void error(SAXParseException e) {
      report(e);
    }

    public void fatalError(SAXParseException e) {
      report(e);
    }

    public void warning(SAXParseException e) {
      report(e);
    }

    private void report(SAXException e) {
      e.getCause().printStackTrace();
    }
  }

}