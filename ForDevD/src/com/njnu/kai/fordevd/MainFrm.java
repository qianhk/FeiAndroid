package com.njnu.kai.fordevd;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

public class MainFrm {

	private JFrame frmFordevd;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrm window = new MainFrm();
					window.frmFordevd.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainFrm() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmFordevd = new JFrame();
		frmFordevd.setResizable(false);
		frmFordevd.setTitle("ForDevD");
		frmFordevd.setBounds(100, 100, 800, 600);
		frmFordevd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLabel lblStatusbar = new JLabel("  Kai Java Gui Application");
		frmFordevd.getContentPane().add(lblStatusbar, BorderLayout.SOUTH);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmFordevd.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		tabbedPane.addTab("Friend", null, panel, null);
		panel.setLayout(null);

		JButton btnFriendStart = new JButton("Start");
		btnFriendStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnFriendStart.setBounds(656, 13, 117, 29);
		panel.add(btnFriendStart);

		JTextArea txtFriendResult = new JTextArea();
		txtFriendResult.setColumns(64);
		txtFriendResult.setRows(10);
		txtFriendResult.setBounds(6, 54, 767, 434);
		panel.add(txtFriendResult);
		tabbedPane.addTab("Tab 2", new JLabel("Tab 2 Content"));
		tabbedPane.addTab("Tab 3", new JLabel("Tab 3 Content"));

		JMenuBar menuBar = new JMenuBar();
		frmFordevd.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmItem = new JMenuItem("Item1");
		mnFile.add(mntmItem);
	}
}
