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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * A wrapper around Apple eawt classes to handle About, Preferences
 * and Quit commands. It registers a listener on the Application.
 */
public class OSXAdapter {

  private Class applicationClass;
  private Object application;
  private Method setHandled;
  private boolean preferencesEnabled = false;
  private boolean aboutEnabled = false;

  public OSXAdapter() throws Exception {
    applicationClass = Class.forName("com.apple.eawt.Application");

    Class appListenerClass = Class
        .forName("com.apple.eawt.ApplicationListener");

    Object appListener = Proxy.newProxyInstance(Thread.currentThread()
        .getContextClassLoader(), new Class[]{appListenerClass}, new Invoker());

    Method getApplication = applicationClass.getMethod("getApplication", null);
    application = getApplication.invoke(null, null);

    Method addListener = applicationClass.getMethod("addApplicationListener",
        new Class[]{appListenerClass});
    addListener.invoke(application, new Object[]{appListener});

    Class eventClass = Class.forName("com.apple.eawt.ApplicationEvent");
    setHandled = eventClass.getMethod("setHandled", new Class[]{boolean.class});
  }

  public void setEnabledPreferencesMenu(boolean enable) throws Exception {
    setEnabled("PreferencesMenu", enable);
    preferencesEnabled = enable;
  }

  public void setEnabledAboutMenu(boolean enable) throws Exception {
    setEnabled("AboutMenu", enable);
    aboutEnabled = enable;
  }

  private void setEnabled(String name, boolean enable) throws Exception {
    Method setEnabled = applicationClass.getMethod("setEnabled" + name,
        new Class[]{boolean.class});
    setEnabled.invoke(application, new Object[]{Boolean.valueOf(enable)});
  }

  protected void handleAbout() {
  }

  protected void handlePreferences() {

  }

  protected void handleQuit() {
  }

  class Invoker implements InvocationHandler {

    public Object invoke(Object proxy, Method method, Object[] args)
        throws Throwable {
      if ("handlePreferences".equals(method.getName()) && preferencesEnabled) {
        handlePreferences();
        setHandled.invoke(args[0], new Object[]{Boolean.TRUE});
      } else if ("handleAbout".equals(method.getName()) && aboutEnabled) {
        handleAbout();
        setHandled.invoke(args[0], new Object[]{Boolean.TRUE});
      } else if ("handleQuit".equals(method.getName())) {
        handleQuit();
        setHandled.invoke(args[0], new Object[]{Boolean.TRUE});
      }
      return null;
    }
  }

}