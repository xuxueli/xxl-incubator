package com.xxl.cip.ui;

import com.xxl.cip.ui.ssh.SshPanel;
import com.xxl.cip.ui.website.WebSitePanel;

import javax.swing.*;

/**
 * index
 * @author xuxueli
 */
public class IndexUI extends JFrame{


	private static IndexUI instance;
	public static IndexUI getInstance() {

		if (instance == null) {
			instance = new IndexUI();

			JTabbedPane tabbedpane=new JTabbedPane();
			tabbedpane.add("Quick Link", WebSitePanel.getInstance());
			tabbedpane.add("Easy SSH", SshPanel.getInstance());
			instance.add(tabbedpane);

			instance.setTitle("China Internet Plus");
			instance.setSize(800,450);
			instance.setLocationRelativeTo(null);	// center
			instance.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			instance.setResizable(false);
			instance.setVisible(true);

			//overwrite windowClosing
			/*this.addWindowListener(
				new java.awt.event.WindowAdapter(){
					public void windowClosing(java.awt.event.WindowEvent evt) {
					}
			});*/

		}
		return instance;

	}

}