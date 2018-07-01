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
package com.l2fprod.common.application;

import com.l2fprod.common.application.core.AppContext;
import com.l2fprod.common.application.core.XmlUIBuilder;
import com.l2fprod.common.application.selection.Selection;
import com.l2fprod.common.application.selection.SelectionChangedEvent;
import com.l2fprod.common.application.selection.SelectionListener;
import com.l2fprod.common.swing.StatusBar;
import com.l2fprod.common.util.OS;

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.io.IOException;
import java.net.URL;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * Application. <br>
 */
public class Application extends JFrame {

  private AppContext context;

  public Application() throws HeadlessException {
    super();
  }

  public Application(GraphicsConfiguration gc) {
    super(gc);
  }

  public Application(String title) throws HeadlessException {
    super(title);
  }

  public Application(String title, GraphicsConfiguration gc) {
    super(title, gc);
  }

  public AppContext getContext() {
    return context;
  }

  /**
   * Initialize an application with the given context.
   * 
   * @param context
   */
  public void initialize(AppContext context) {
    this.context = context;
  }

  /**
   * It will:
   * <ul>
   * <li>load the actions.xml (if found in the application package)
   * to init menubar, toolbar and actions</li>
   * </ul>
   */
  protected void defaultUIInit() {
    URL ui = getClass().getResource("ui.xml");
    if (ui != null) {      
      try {
        XmlUIBuilder builder;
        builder = new XmlUIBuilder(context, ui);
        JMenuBar menubar = builder.getMenubar("default");
        setJMenuBar(menubar);

        JToolBar toolbar = builder.getToolBar("default");
        if (toolbar != null) {
          getContentPane().add("North", toolbar);
        }

        final StatusBar statusbar = builder.getStatusBar("default");
        if (statusbar != null) {
          getContentPane().add("South", statusbar);

          SelectionListener statusBarUpdater = new SelectionListener() {

            public void selectionChanged(SelectionChangedEvent event) {
              Selection selection = context.getSelectionManager()
                  .getSelection();
              JLabel message = (JLabel)statusbar.getZone("message");
              message.setText(selection.isEmpty()?"No Selection":String
                  .valueOf(selection.getSelection().length));
            }
          };
          context.getSelectionManager().addSelectionListener(statusBarUpdater);
        }
      } catch (ParserConfigurationException e) {
        e.printStackTrace();
      } catch (SAXException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }

    }

    initMacOSX();
    
    addWindowListener(new WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent e) {
        Action exit = getContext().getActions().get("exit");
        if (exit != null) {
          exit.actionPerformed(null);
        }
      }
    });
  }

  protected void initMacOSX() {
    if (OS.isMacOSX()) {
      try {
        final Action about = getContext().getActions().get("about");
        final Action prefs = getContext().getActions().get("preferences");
        final Action exit = getContext().getActions().get("exit");
        class ApplicationOSXAdapter extends OSXAdapter {
          public ApplicationOSXAdapter() throws Exception {
            super();
          }
          protected void handleAbout() {
            about.actionPerformed(null);
          }
          
          protected void handlePreferences() {
            prefs.actionPerformed(null);
          }
          
          protected void handleQuit() {
            exit.actionPerformed(null);
          }
        };
        OSXAdapter adapter = new ApplicationOSXAdapter();
        adapter.setEnabledAboutMenu(true);
        adapter.setEnabledPreferencesMenu(true);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}