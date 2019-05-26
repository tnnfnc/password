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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import it.tnnfnc.apps.application.AbstractApplication;
import it.tnnfnc.apps.application.properties.AbstractEditingPanel;
import it.tnnfnc.apps.application.ui.GridBagLayoutUtility;

//import net.catode.panels.Utility;

public class SettingsPanel extends AbstractEditingPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected AbstractApplication<?> application;
	// private Properties properties;
	// private JSpinner displayTime = new JSpinner(new SpinnerNumberModel(10, 5,
	// 60, 5));
	//
	// private JSpinner passphraseTimeout = new JSpinner(new
	// SpinnerNumberModel(5,
	// 1, 90, 1));
	// private JSpinner historyLenght = new JSpinner(new SpinnerNumberModel(5,
	// 0,
	// 10, 1));
	// private JSpinner passwordValidity = new JSpinner(new
	// SpinnerNumberModel(1,
	// 1, 365, 1));
	// private JSpinner expiryWarning = new JSpinner(new SpinnerNumberModel(15,
	// 1,
	// 30, 1));

	private JComboBox<String> displayTime_combo = new JComboBox<String>(
			new String[] { "10", "15", "20", "30", "60" });
	private JComboBox<String> passphraseTimeout_combo = new JComboBox<String>(
			new String[] { "5", "15", "30", "60", "90", "120", "180" });
	private JComboBox<String> historyLenght_combo = new JComboBox<String>(
			new String[] { "0", "1", "5", "10" });
	private JComboBox<String> passwordValidity_combo = new JComboBox<String>(
			new String[] { "7", "15", "30", "90", "180", "360" });
	private JComboBox<String> expiryWarning_combo = new JComboBox<String>(
			new String[] { "-1", "7", "10", "15" });

	private JComboBox<Object> autosaving = new JComboBox<Object>();
	private JComboBox<Object> passwordOutput = new JComboBox<Object>();
	private String label;

	public SettingsPanel(AbstractApplication<?> application) {
		this.application = application;
		autosaving.addItem(application.getLocalization().getString(
				PasswordConstants.AUTOSAVE_NEVER));
		autosaving.addItem(application.getLocalization().getString(
				PasswordConstants.AUTOSAVE_ALWAYS));
		autosaving.addItem(application.getLocalization().getString(
				PasswordConstants.AUTOSAVE_SCHEDULED));

		passwordOutput.addItem(application.getLocalization().getString(
				PasswordConstants.OPT_DISPLAYCOPY));
		passwordOutput.addItem(application.getLocalization().getString(
				PasswordConstants.OPT_COPY));
		passwordOutput.addItem(application.getLocalization().getString(
				PasswordConstants.OPT_DISPLAY));

		// displayTime_combo.addItem("");
		// passphraseTimeout_combo.addItem("");
		// historyLenght_combo.addItem("");
		// passwordValidity_combo.addItem("");
		// expiryWarning_combo.addItem("");

		label = application.getLocalization().getString("Settings");

		createGUI();

		initGUI();

	}

	private void createGUI() {
		GridBagConstraints gbc = new GridBagConstraints();
		setLayout(new GridBagLayout());
		GridBagLayoutUtility.initConstraints(gbc);

		GridBagLayoutUtility.add(new JLabel(application.getLocalization()
				.getString(PasswordConstants.PREF_DISPLAY_TIME)),
				displayTime_combo, new JLabel(application.getLocalization()
						.getString("sec")), this, gbc);
		GridBagLayoutUtility.newLine(gbc);
		GridBagLayoutUtility.add(new JLabel(application.getLocalization()
				.getString(PasswordConstants.PREF_TIMEOUT)),
				passphraseTimeout_combo, new JLabel(application
						.getLocalization().getString("min")), this, gbc);
		GridBagLayoutUtility.newLine(gbc);
		GridBagLayoutUtility.add(new JLabel(application.getLocalization()
				.getString(PasswordConstants.PREF_HISTORY)),
				historyLenght_combo, new JLabel(application.getLocalization()
						.getString("records")), this, gbc);

		GridBagLayoutUtility.newLine(gbc);

		GridBagLayoutUtility.add(new JLabel(application.getLocalization()
				.getString(PasswordConstants.PREF_OUTPUT)), passwordOutput,
				this, gbc);

		GridBagLayoutUtility.newLine(gbc);
		GridBagLayoutUtility.add(new JLabel(application.getLocalization()
				.getString(PasswordConstants.PREF_VALIDITY)),
				passwordValidity_combo, new JLabel(application
						.getLocalization().getString("days")), this, gbc);

		GridBagLayoutUtility.newLine(gbc);
		GridBagLayoutUtility.add(new JLabel(application.getLocalization()
				.getString(PasswordConstants.PREF_EXPIRY_WARNING)),
				expiryWarning_combo, new JLabel(application.getLocalization()
						.getString("days")), this, gbc);

		GridBagLayoutUtility.newLine(gbc);
		GridBagLayoutUtility.add(new JLabel(application.getLocalization()
				.getString(PasswordConstants.PREF_AUTOSAVE)), autosaving,
				new JLabel(application.getLocalization().getString("min")),
				this, gbc);

		// GridBagConstraints gbc = new GridBagConstraints();
		// setLayout(new GridBagLayout());
		// GridBagLayoutUtility.initConstraints(gbc);
		//
		// GridBagLayoutUtility.add(new JLabel(application.getLocalization()
		// .getString(PasswordProperties.PREF_DISPLAY_TIME)), displayTime,
		// new JLabel(application.getLocalization().getString("sec")),
		// this, gbc);
		//
		// GridBagLayoutUtility.newLine(gbc);
		// GridBagLayoutUtility.add(new JLabel(application.getLocalization()
		// .getString(PasswordProperties.PREF_TIMEOUT)),
		// passphraseTimeout, new JLabel(application.getLocalization()
		// .getString("min")), this, gbc);
		// GridBagLayoutUtility.newLine(gbc);
		// GridBagLayoutUtility.add(new JLabel(application.getLocalization()
		// .getString(PasswordProperties.PREF_HISTORY)), historyLenght,
		// new JLabel(application.getLocalization().getString("records")),
		// this, gbc);
		//
		// GridBagLayoutUtility.newLine(gbc);
		//
		// GridBagLayoutUtility.add(new JLabel(application.getLocalization()
		// .getString(PasswordProperties.PREF_OUTPUT)), passwordOutput,
		// this, gbc);
		//
		// GridBagLayoutUtility.newLine(gbc);
		// GridBagLayoutUtility.add(new JLabel(application.getLocalization()
		// .getString(PasswordProperties.PREF_VALIDITY)),
		// passwordValidity, new JLabel(application.getLocalization()
		// .getString("days")), this, gbc);
		//
		// GridBagLayoutUtility.newLine(gbc);
		// GridBagLayoutUtility.add(new JLabel(application.getLocalization()
		// .getString(PasswordProperties.PREF_EXPIRY_WARNING)),
		// expiryWarning, new JLabel(application.getLocalization()
		// .getString("days")), this, gbc);
		//
		// GridBagLayoutUtility.newLine(gbc);
		// GridBagLayoutUtility.add(new JLabel(application.getLocalization()
		// .getString(PasswordProperties.PREF_AUTOSAVE)), autosaving,
		// new JLabel(application.getLocalization().getString("min")),
		// this, gbc);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.passwordManager.I_PropertiesPanel#update()
	 */
	@Override
	public void initGUI() {
		String key = PasswordConstants.PREF_DISPLAY_TIME;
		String value = application.getProperties().getProperty(
				PasswordConstants.PREF_DISPLAY_TIME, "15");
		displayTime_combo.setSelectedItem(value);

		key = PasswordConstants.PREF_TIMEOUT;
		value = application.getProperties().getProperty(key, "90");
		passphraseTimeout_combo.setSelectedItem(value);

		key = PasswordConstants.PREF_HISTORY;
		value = application.getProperties().getProperty(key, "10");
		historyLenght_combo.setSelectedItem(value);

		key = PasswordConstants.PREF_VALIDITY;
		value = application.getProperties().getProperty(key, "90");
		passwordValidity_combo.setSelectedItem(value);

		key = PasswordConstants.PREF_EXPIRY_WARNING;
		value = application.getProperties().getProperty(key, "10");
		expiryWarning_combo.setSelectedItem(value);

		key = PasswordConstants.PREF_OUTPUT;
		value = application.getProperties().getProperty(key,
				PasswordConstants.OPT_DISPLAYCOPY);
		passwordOutput.setSelectedIndex(getPasswordOutputOption(value));

		key = PasswordConstants.PREF_AUTOSAVE;
		value = application.getProperties().getProperty(key, "");
		autosaving.setSelectedItem(getAutosaveOption(value));

		// String key = PasswordProperties.PREF_DISPLAY_TIME;
		// String value = application.getProperties().getProperty(
		// PasswordProperties.PREF_DISPLAY_TIME, "15");
		// displayTime.setValue(Utility.toInteger(value));
		//
		// key = PasswordProperties.PREF_TIMEOUT;
		// value = application.getProperties().getProperty(key, "90");
		// passphraseTimeout.setValue(Utility.toInteger(value));
		//
		// key = PasswordProperties.PREF_HISTORY;
		// value = application.getProperties().getProperty(key, "10");
		// historyLenght.setValue(Utility.toInteger(value));
		//
		// key = PasswordProperties.PREF_OUTPUT;
		// value = application.getProperties().getProperty(key,
		// PasswordProperties.OPT_DISPLAYCOPY);
		// passwordOutput.setSelectedIndex(getPasswordOutputOption(value));
		//
		// key = PasswordProperties.PREF_VALIDITY;
		// value = application.getProperties().getProperty(key, "90");
		// passwordValidity.setValue(Utility.toInteger(value));
		//
		// key = PasswordProperties.PREF_EXPIRY_WARNING;
		// value = application.getProperties().getProperty(key, "10");
		// expiryWarning.setValue(Utility.toInteger(value));
		//
		// key = PasswordProperties.PREF_AUTOSAVE;
		// value = application.getProperties().getProperty(key, "");
		// autosaving.setSelectedItem(getAutosaveOption(value));

		// key = SuperPasswordDefault.ENCRYPT;
		// value = application.getProperties().getProperty(key, "");
		// encrypt.setSelected(Boolean.parseBoolean(value));

	}

	private int getAutosaveOption(String value) {
		if (value.equals(PasswordConstants.AUTOSAVE_ALWAYS))
			return 1;
		else if (value.equals(PasswordConstants.AUTOSAVE_SCHEDULED))
			return 2;
		else
			// (prefOutput.equals(PasswordDefault.OPT_DISPLAYCOPY))
			return 0;
	}

	private int getPasswordOutputOption(String prefOutput) {
		if (prefOutput.equals(PasswordConstants.OPT_COPY))
			return 1;
		else if (prefOutput.equals(PasswordConstants.OPT_DISPLAY))
			return 2;
		else
			return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.passwordManager.I_PropertiesPanel#change()
	 */
	@Override
	public void update() {
		// String key = PasswordProperties.PREF_DISPLAY_TIME;
		// application.getProperties().setProperty(key,
		// displayTime.getValue() + "");
		//
		// key = PasswordProperties.PREF_TIMEOUT;
		// application.getProperties().setProperty(key,
		// passphraseTimeout.getValue() + "");
		//
		// key = PasswordProperties.PREF_HISTORY;
		// application.getProperties().setProperty(key,
		// historyLenght.getValue() + "");
		//
		//
		// key = PasswordProperties.PREF_VALIDITY;
		// application.getProperties().setProperty(key,
		// passwordValidity.getValue() + "");
		//
		// key = PasswordProperties.PREF_EXPIRY_WARNING;
		// application.getProperties().setProperty(key,
		// expiryWarning.getValue() + "");

		String key = PasswordConstants.PREF_DISPLAY_TIME;
		application.getProperties().setProperty(key,
				displayTime_combo.getSelectedItem() + "");

		key = PasswordConstants.PREF_TIMEOUT;
		application.getProperties().setProperty(key,
				passphraseTimeout_combo.getSelectedItem() + "");

		key = PasswordConstants.PREF_HISTORY;
		application.getProperties().setProperty(key,
				historyLenght_combo.getSelectedItem() + "");

		key = PasswordConstants.PREF_VALIDITY;
		application.getProperties().setProperty(key,
				passwordValidity_combo.getSelectedItem() + "");

		key = PasswordConstants.PREF_EXPIRY_WARNING;
		application.getProperties().setProperty(key,
				expiryWarning_combo.getSelectedItem() + "");

		key = PasswordConstants.PREF_AUTOSAVE;
		application.getProperties().setProperty(key,
				setAutosaveOption(autosaving.getSelectedIndex()));

		key = PasswordConstants.PREF_OUTPUT;
		application.getProperties().setProperty(key,
				setPasswordOutputOption(passwordOutput.getSelectedIndex()));

	}

	private String setAutosaveOption(int selectedIndex) {
		switch (selectedIndex) {
		case 1:
			return PasswordConstants.AUTOSAVE_ALWAYS;
		case 2:
			return PasswordConstants.AUTOSAVE_SCHEDULED;
		default:
			return PasswordConstants.AUTOSAVE_NEVER;
		}
	}

	private String setPasswordOutputOption(int selectedIndex) {
		switch (selectedIndex) {
		case 1:
			return PasswordConstants.OPT_COPY;
		case 2:
			return PasswordConstants.OPT_DISPLAY;
		default:
			return PasswordConstants.OPT_DISPLAYCOPY;
		}
	}

	@Override
	public String getLabel() {
		return label;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		// displayTime.setEnabled(enabled);
		// passphraseTimeout.setEnabled(enabled);
		// historyLenght.setEnabled(enabled);
		// passwordValidity.setEnabled(enabled);
		// expiryWarning.setEnabled(enabled);

		displayTime_combo.setEnabled(enabled);
		passphraseTimeout_combo.setEnabled(enabled);
		historyLenght_combo.setEnabled(enabled);
		passwordValidity_combo.setEnabled(enabled);
		expiryWarning_combo.setEnabled(enabled);

		autosaving.setEnabled(enabled);
		passwordOutput.setEnabled(enabled);

		// encrypt.setEnabled(enabled);
		super.setEnabled(enabled);
	}

	public static void main(String[] args) {
		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Create and set up the window.
				JFrame frame = new JFrame("Properties panel demo");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				// Create and set up the content pane.
				SettingsPanel demo = new SettingsPanel(new Password());
				frame.getContentPane().add(demo);

				// Display the window.
				frame.pack();
				frame.setVisible(true);
			}
		});
	}

}
