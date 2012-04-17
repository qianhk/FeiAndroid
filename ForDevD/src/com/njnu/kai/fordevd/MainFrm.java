package com.njnu.kai.fordevd;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.JLabel;

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
		frmFordevd.setTitle("ForDevD");
		frmFordevd.setBounds(100, 100, 450, 300);
		frmFordevd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLabel lblNewLabel = new JLabel("New label");
		frmFordevd.getContentPane().add(lblNewLabel, BorderLayout.SOUTH);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmFordevd.getContentPane().add(tabbedPane, BorderLayout.CENTER);
	}

}
