package com.l2fprod.common.swing;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class GroupOfTaskPaneGroupTest {

  /**
   * @param args
   */
  public static void main(String[] args) {
    JFrame frame = new JFrame();
    
    JTaskPane tp = new JTaskPane();
    JTaskPaneGroup tpg1 = makeGroup("tp1");
    JTaskPaneGroup tpg2 = makeGroup("tp2");
    JTaskPaneGroup tpg3 = makeGroup("tp3");
    tp.add(tpg1);
    tp.add(tpg2);
    tp.add(tpg3);
    
    GroupOfTaskPaneGroup group = new GroupOfTaskPaneGroup();
    group.add(tpg1);
    group.add(tpg2);
    group.add(tpg3);
    
    frame.getContentPane().add("Center", tp);
    frame.pack();
    frame.show();
  }

  static JTaskPaneGroup makeGroup(String title) {
    JTaskPaneGroup tpg = new JTaskPaneGroup();
    tpg.setTitle(title);
    tpg.add(new JLabel("Hello"));
    tpg.add(new JButton("World!"));
    return tpg;
  }
}
