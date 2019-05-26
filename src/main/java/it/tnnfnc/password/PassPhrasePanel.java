/*
 * Copyright (c) 2015, Franco Toninato. All rights reserved.
 * 
 * This program is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your option) 
 * any later version.
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * THERE IS NO WARRANTY FOR THE PROGRAM, TO THE EXTENT PERMITTED BY APPLICABLE LAW. 
 * EXCEPT WHEN OTHERWISE STATED IN WRITING THE COPYRIGHT HOLDERS AND/OR OTHER 
 * PARTIES PROVIDE THE PROGRAM �AS IS� WITHOUT WARRANTY OF ANY KIND, EITHER 
 * EXPRESSED OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE ENTIRE RISK AS TO THE 
 * QUALITY AND PERFORMANCE OF THE PROGRAM IS WITH YOU. SHOULD THE PROGRAM PROVE 
 * DEFECTIVE, YOU ASSUME THE COST OF ALL NECESSARY SERVICING, REPAIR OR CORRECTION.
 */
package it.tnnfnc.password;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.InvalidKeyException;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import it.tnnfnc.apps.application.SwingPeriodicTask;
import it.tnnfnc.apps.application.ui.KeyboardComponentEditor;
import it.tnnfnc.apps.application.ui.PasswordStrengthField;
import it.tnnfnc.apps.application.ui.PopMessage;
import it.tnnfnc.apps.application.ui.Utility;

/**
 * @author Franco Toninato
 * 
 */
public class PassPhrasePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String RUN_CMD = "RUN";
	private static final String STOP_CMD = "STOP";

	private JPanel container;
	private JProgressBar progressBar;
	private JPasswordField passwordField;
	private Password application;
	private JButton start;
	private ButtonsListener buttonsListener;
	private KeyboardComponentEditor keyboardComponentEditor;
	private int passLen = 8;

	/**
	 * Object constructor.
	 * 
	 * @param e
	 *            the application program.
	 * 
	 */
	public PassPhrasePanel(Password a) {
		this(a, 8);
	}

	/**
	 * Object constructor.
	 * 
	 * @param e
	 *            the application program.
	 * 
	 * @param len
	 *            display password field length.
	 */
	public PassPhrasePanel(Password a, int len) {
		this.passLen = len > 0 ? len : 8;
		this.application = a;
		initialize();
		createGUI();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean b) {
		super.setEnabled(b);
		try {
			keyboardComponentEditor.setEnabled(b);
			passwordField.setEnabled(b);
			start.setEnabled(b);
		} catch (Exception e) {
		}
	}

	/**
	 * Reset the pass phrase.
	 */
	public void reset() {
		passwordField.requestFocusInWindow();
		passwordField.setText(new String(""));
		application.getTaskScheduler().stop(PasswordConstants.PASSPHRASE_THREAD);
	}

	/**
	 * Get the pass phrase and reinitialize the input.
	 * 
	 * @return the pass phrase and reset the input password field.
	 */
	public byte[] getPassPhrase() {
		char b[] = passwordField.getPassword();
		byte p[] = new String(b).getBytes();
		Arrays.fill(b, '0');
		reset();
		return p;
	}

	/**
	 * Creates the key generator and the secret key. Switch from the the
	 * password field to the progress bar.
	 * 
	 */
	public void displayPassphrase() throws InvalidKeyException {
		((CardLayout) (container.getLayout())).show(container, RUN_CMD);
		long period = 1000L;
		int duration = Utility.toInteger(application.getProperties().getProperty(PasswordConstants.PREF_TIMEOUT)) * 60
				* 1000;
		progressBar.setMaximum(duration);
		progressBar.setMinimum(0);
		application.getTaskScheduler().addTask(new DisplayPassPhraseTask(progressBar), period, duration,
				PasswordConstants.PASSPHRASE_THREAD);
	}

	/**
	 * Reset the pass phrase, stop the progress bar and show the password field
	 * ready for input.
	 * 
	 */
	public void stopPassphrase() {
		reset();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				((CardLayout) (container.getLayout())).show(container, STOP_CMD);
			}
		});
	}

	/**
	 * Initialize the local variables, it is called by the constructor.
	 */
	private void initialize() {
		buttonsListener = new ButtonsListener();
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		passwordField = new JPasswordField(passLen);
		keyboardComponentEditor = new KeyboardComponentEditor(passwordField);
		start = new JButton(application.getLocalization().getString("OK"));
		start.setActionCommand(RUN_CMD);
		start.addActionListener(new ButtonsListener());
		start.setToolTipText(application.getLocalization().getString("tt-start stop passphrase"));
	}

	/**
	 * Create the GUI.
	 */
	private void createGUI() {
		container = new JPanel(new CardLayout());
		container.add(createPassPhrasePanel(), STOP_CMD);
		container.add(createProgressBarPanel(), RUN_CMD);
		this.setLayout(new BorderLayout());
		this.add(container, BorderLayout.CENTER);
		// this.add(container);

	}

	private JPanel createProgressBarPanel() {
		JButton stop = new JButton(application.getLocalization().getString("Stop"));
		stop.setActionCommand(STOP_CMD);
		stop.addActionListener(buttonsListener);

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		// panel.add(new JLabel(application.getLocalization().getString("Time
		// left")));
		panel.add(Box.createHorizontalStrut(4));
		panel.add(stop);
		panel.add(Box.createHorizontalStrut(4));
		panel.add(progressBar);
		panel.add(Box.createHorizontalStrut(4));
		// panel.add(Box.createHorizontalStrut(10));
		// panel.add(Box.createGlue());

		return panel;

		// New
		// JButton stop = new
		// JButton(application.getLocalization().getString("Stop"));
		// stop.setActionCommand(STOP_CMD);
		// stop.addActionListener(buttonsListener);
		//
		// JPanel panel = new JPanel(new BorderLayout());
		// panel.add(new JLabel(application.getLocalization().getString("Time
		// left")), BorderLayout.LINE_START);
		// panel.add(progressBar, BorderLayout.CENTER);
		// panel.add(stop, BorderLayout.LINE_END);
		//
		// return panel;
	}

	private JPanel createPassPhrasePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		// panel.add(Box.createGlue());
		panel.add(Box.createHorizontalStrut(4));
		panel.add(new JLabel(application.getLocalization().getString("Set pass phrase")));
		panel.add(Box.createHorizontalStrut(4));
		panel.add(keyboardComponentEditor);
		// panel.add(Box.createHorizontalGlue());
		panel.add(Box.createHorizontalStrut(4));
		panel.add(start);
		panel.add(Box.createHorizontalStrut(4));
		// panel.add(Box.createGlue());
		panel.add(new JLabel(application.getLocalization().getString("Password Strength")));
		panel.add(Box.createHorizontalStrut(4));
		panel.add(new PasswordStrengthField(passwordField));
		panel.add(Box.createHorizontalStrut(4));
		// panel.add(Box.createGlue());
		return panel;
	}

	/**
	 * Listen to the card selection.
	 * 
	 */
	private class ButtonsListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			String card = ((JButton) event.getSource()).getActionCommand();
			if (card.equalsIgnoreCase(RUN_CMD)) {
				try {
					application.setPassphrase(getPassPhrase());
					displayPassphrase();
				} catch (Exception e) {
					stopPassphrase();
					PopMessage.displayError(PassPhrasePanel.this,
							application.getLocalization().getString("Security Exception") + " Pass Phrase panel "
									+ e.getMessage());
				}
			} else if (card.equalsIgnoreCase(STOP_CMD)) {
				try {
					application.stopPassphrase();
					stopPassphrase();
				} catch (Exception e) {
					PopMessage.displayError(PassPhrasePanel.this,
							application.getLocalization().getString("Security Exception") //
									+ ": " + application.getLocalization().getString("off"));
				}
			} else {
			}
		}
	}

	/**
	 * Timer task updating the progress bar. When finished it cancels the
	 * password.
	 * 
	 */
	private class DisplayPassPhraseTask extends SwingPeriodicTask {
		private JProgressBar progressbar;

		public DisplayPassPhraseTask(JProgressBar p) {
			this.progressbar = p;
		}

		@Override
		public void performLast() {
			try {
				stopPassphrase();
				if (application.isReady())
					application.stopPassphrase();
			} catch (Exception e) {
				PopMessage.displayError(PassPhrasePanel.this,
						application.getLocalization().getString("Security Exception") //
								+ ": " + application.getLocalization().getString("off"));
			}
		}

		@Override
		public void performTask() {
			progressbar.setString(Utility.timeStamp(left));
			progressbar.setValue((int) (left));
		}
	}

	public static void main(String[] args) {
		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Create and set up the window.
				JFrame frame = new JFrame("Passphrase panel demo");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				// Create and set up the content pane.
				PassPhrasePanel demo = new PassPhrasePanel(new Password());
				frame.getContentPane().add(demo);
				demo.setEnabled(false);
				// Display the window.
				frame.pack();
				frame.setVisible(true);
			}
		});
	}

}
