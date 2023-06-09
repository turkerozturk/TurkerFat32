/***********************************************************************
 * Copyright 2023 Turker Ozturk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***********************************************************************/


package com.turkerozturk.library.fat32.gui;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * http://www.java2s.com/Tutorial/Java/0260__Swing-Event/HowtoWriteaTreeSelectionListener.htm
 */
public class SelectionListener implements TreeSelectionListener {

  @Override
  public void valueChanged(TreeSelectionEvent se) {
    JTree tree = (JTree) se.getSource();
    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree
        .getLastSelectedPathComponent();
    String selectedNodeName = selectedNode.toString();
   // if (selectedNode.isLeaf()) {

      System.out.println(selectedNodeName);

   // }
  }
}