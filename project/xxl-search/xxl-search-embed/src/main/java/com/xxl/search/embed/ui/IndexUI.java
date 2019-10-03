package com.xxl.search.embed.ui;



import javax.swing.*;

/**
 * index
 * @author xuxueli 16/8/18.
 */
public class IndexUI extends JFrame{


	private static IndexUI instance;
	public static IndexUI getInstance() {

		if (instance == null) {
			instance = new IndexUI();

			instance.add(WebSitePanel.getInstance());

			instance.setTitle("搜索小程序");
			instance.setSize(450,200);
			instance.setLocationRelativeTo(null);	// center
			instance.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			instance.setResizable(false);
			instance.setVisible(true);


		}
		return instance;

	}

}