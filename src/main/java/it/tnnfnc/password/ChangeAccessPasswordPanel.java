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
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.security.DigestException;
import java.security.InvalidKeyException;
import java.util.ListResourceBundle;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import it.tnnfnc.apps.application.ui.ChangePasswordPanel;
import it.tnnfnc.apps.application.ui.FieldLayout;
import it.tnnfnc.apps.application.ui.GridBagLayoutUtility;
import it.tnnfnc.apps.application.ui.PasswordGauge;
import it.tnnfnc.apps.application.ui.PopMessage;
import it.tnnfnc.apps.application.ui.style.StyleObject;
import it.tnnfnc.encoders.IdentityEncoder;
import it.tnnfnc.password.domain.Access;
import it.tnnfnc.password.domain.AccessFactory;

/**
 * @author Franco Toninato
 * 
 */
public class ChangeAccessPasswordPanel extends JPanel implements ActionListener, ChangeListener {
	private static final String CONFIRM = "ok";
	private static final String CANCEL = "ko";
	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	private Password application;
	private ListResourceBundle language;

	private Access access;
	private Access access_new;
	private boolean passwordChanged = false;

	// Output
	private JTextField accessName;
	private JTextField user;
	private JTextField status;
	private JProgressBar currentPasswordBar;
	private JProgressBar generatedPasswordBar;

	// Input
	private JButton buttonShowPassword;
	private JButton buttonGeneratePassword;
	private JCheckBox chars;
	private JCheckBox numbers;
	private JCheckBox symbols;
	private JSpinner pwd_length;
	private JComboBox<?> passwordType;
	private JComboBox<?>[] password_rules = new JComboBox<?>[1];

	private JTabbedPane tabbsPanel;
	private JCheckBox expire;
	private JSpinner validity;

	private ChangePasswordPanel passwordPanel;
	private JPanel passwordRulesPanel;
	private JDialog dialog;

	/**
	 * Constructor.
	 * 
	 * @param application
	 *            the program application.
	 */
	public ChangeAccessPasswordPanel(Password application) {
		this.application = application;
		initialise();
		// createGUI();
		// initPanel();
	}

	private void initialise() {
		language = application.getLocalization();
		passwordPanel = new ChangePasswordPanel();

		currentPasswordBar = new JProgressBar();
		generatedPasswordBar = new JProgressBar();

		buttonShowPassword = new JButton();
		buttonGeneratePassword = new JButton();

		buttonShowPassword.addActionListener(this);
		buttonGeneratePassword.addActionListener(this);

		currentPasswordBar.setStringPainted(true);

		currentPasswordBar.setString(application.getLocalization().getString(PasswordConstants.OPT_DISPLAY));
		generatedPasswordBar.setStringPainted(true);

		generatedPasswordBar.setString(application.getLocalization().getString(PasswordConstants.OPT_DISPLAY));

		currentPasswordBar.setIndeterminate(false);
		generatedPasswordBar.setIndeterminate(false);

		accessName = new JTextField("");
		accessName.setEditable(false);
		user = new JTextField("");
		user.setEditable(false);

		status = new JTextField("");
		status.setEditable(false);

		chars = new JCheckBox();
		numbers = new JCheckBox();
		symbols = new JCheckBox();
		expire = new JCheckBox();
		// JSpinner
		validity = new JSpinner();
		pwd_length = new JSpinner();

		// String[] algos = new String[application.getPasswordAlgorithm().length
		// - 1];
		String[] algos = new String[application.getPasswordAlgorithms().length - 1];
		int i = 0;
		for (String a : application.getPasswordAlgorithms()) {
			if (!a.equals(IdentityEncoder.algorithm)) {
				algos[i++] = a;
			}
		}

		passwordType = new JComboBox<String>(algos);

		String[] rules = PasswordDictionary.getDictionaries();
		for (int j = 0; j < password_rules.length; j++) {
			password_rules[j] = new JComboBox<String>(rules);
		}

		tabbsPanel = new JTabbedPane();
		tabbsPanel.addChangeListener(this);
		passwordRulesPanel = new JPanel();
	}

	private void createGUI() {
		// Common info panel
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		GridBagLayoutUtility.initConstraints(gbc);

		GridBagLayoutUtility.add(new JLabel(application.getLocalization().getString(AccessFactory.ACCESS)), accessName,
				topPanel, gbc);
		GridBagLayoutUtility.newLine(gbc);
		GridBagLayoutUtility.add(new JLabel(application.getLocalization().getString(AccessFactory.USER)), user,
				topPanel, gbc);
		GridBagLayoutUtility.newLine(gbc);
		GridBagLayoutUtility.add(new JLabel(application.getLocalization().getString(AccessFactory.STATUS)), status,
				topPanel, gbc);
		GridBagLayoutUtility.newLine(gbc);
		GridBagLayoutUtility.add(buttonShowPassword, currentPasswordBar, topPanel, gbc);

		JPanel commandsPanel = new JPanel();
		commandsPanel.add(createCommandPanel(), BorderLayout.SOUTH);

		// Generation
		tabbsPanel.addTab(application.getLocalization().getString(AccessFactory.GENERATE), createGenerationPanel());
		// User
		tabbsPanel.addTab(application.getLocalization().getString("User defined"), createUserPanel());
		// Bottom
		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.add(commandsPanel, BorderLayout.SOUTH);
		bottomPanel.add(displayPasswordPanel(), BorderLayout.CENTER);

		// Layout
		this.setLayout(new BorderLayout());
		this.add(topPanel, BorderLayout.NORTH);
		this.add(tabbsPanel, BorderLayout.CENTER);
		this.add(bottomPanel, BorderLayout.SOUTH);
	}

	private Component displayPasswordPanel() {
		JPanel p = new JPanel(new BorderLayout());
		FieldLayout fl = new FieldLayout(p);
		fl.append(0, 1, buttonGeneratePassword, generatedPasswordBar);
		return p;
	}

	private Component createUserPanel() {
		return passwordPanel;
	}

	private Component createGenerationPanel() {
		JPanel p = new JPanel();
		p.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		GridBagLayoutUtility.initConstraints(gbc);

		// Length from to
		GridBagLayoutUtility.add(new JLabel(application.getLocalization().getString(AccessFactory.PWD_LENGTH)),
				pwd_length, new JLabel(application.getLocalization().getString("digits")), p, gbc);
		// Algorithm type - unmanaged
		GridBagLayoutUtility.right(gbc);
		GridBagLayoutUtility.add(new JLabel(application.getLocalization().getString(AccessFactory.PASSWORD_TYPE)),
				passwordType, p, gbc);

		// Validity
		GridBagLayoutUtility.newLine(gbc);
		GridBagLayoutUtility.add(new JLabel(application.getLocalization().getString(AccessFactory.VALIDITY)), validity,
				p, gbc);
		// Warning
		GridBagLayoutUtility.right(gbc);
		GridBagLayoutUtility.add(new JLabel(application.getLocalization().getString(AccessFactory.EXPIRE)), expire, p,
				gbc);

		// Rules
		JPanel q = new JPanel();
		q.setBorder(new TitledBorder(application.getLocalization().getString("Rule")));
		q.setLayout(new BorderLayout());
		JPanel q1 = new JPanel(new BorderLayout());
		JPanel q2 = new JPanel(new FlowLayout());
		q2.add(new JButton(new AddRuleCommand(application.getLocalization().getString("Add rule"))));
		q2.add(new JButton(new RemoveRuleCommand(application.getLocalization().getString("Remove rule"))));
		q1.add(q2, BorderLayout.PAGE_END);
		q.add(q1, BorderLayout.LINE_END);

		q.add(passwordRulesPanel, BorderLayout.LINE_START);
		GridBagLayoutUtility.newLine(gbc);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		p.add(q, gbc);

		return p;
	}

	private JPanel createCommandPanel() {
		// ActionListener listener = new InternalListener();
		JButton ok = new JButton(application.getLocalization().getString("Confirm"));
		JButton cancel = new JButton(application.getLocalization().getString("Cancel"));
		ok.setActionCommand(CONFIRM);
		cancel.setActionCommand(CANCEL);
		ok.addActionListener(this);
		cancel.addActionListener(this);
		JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		actionPanel.add(ok);
		actionPanel.add(cancel);
		return actionPanel;
	}

	/**
	 * 
	 */
	private void init(Access a) {
		try {
			this.access = a;
			accessName.setText(access.getValue(AccessFactory.ACCESS) + "");
			user.setText(access.getValue(AccessFactory.USER) + "");
			status.setText(access.getValue(AccessFactory.STATUS) + "");
			pwd_length.setValue(access.getValue(AccessFactory.PWD_LENGTH));

			passwordType.setSelectedItem(AccessFactory.getPasswordType(access));

			initPasswordRules(AccessFactory.getPasswordRules(access));

			validity.setValue(access.getValue(AccessFactory.VALIDITY));

			chars.setSelected((Boolean) access.getValue(AccessFactory.CHARS));
			numbers.setSelected((Boolean) access.getValue(AccessFactory.NUMBERS));
			symbols.setSelected((Boolean) access.getValue(AccessFactory.SYMBOLS));

			accessName.setToolTipText(access.getValue(AccessFactory.TEXT) + "");
			access_new = access.copy();
			access_new.clearPassword();
			buttonShowPassword.setEnabled(access.isPasswordDefined());
		} catch (CloneNotSupportedException e) {
			PopMessage.displayError(ChangeAccessPasswordPanel.this, language.getString(e.getMessage()),
					language.getString(e.getClass().getName()));
		}
	}

	private void initPasswordRules(String[] rules) {

		passwordRulesPanel.removeAll();

		JPanel p = new JPanel();
		p.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		GridBagLayoutUtility.initConstraints(gbc);
		String[] dictionaries = PasswordDictionary.getDictionaries();

		password_rules = new JComboBox<?>[rules.length];
		for (int i = 0; i < rules.length; i++) {
			password_rules[i] = new JComboBox<String>(dictionaries);
			GridBagLayoutUtility.newLine(gbc);
			GridBagLayoutUtility.add(new JLabel(application.getLocalization().getString("Rule")), password_rules[i], p,
					gbc);
			try {
				password_rules[i].setSelectedItem(rules[i]);
			} catch (Exception e) {
				password_rules[i].setSelectedIndex(0);
			}
		}

		passwordRulesPanel.add(p, BorderLayout.LINE_START);
	}

	/**
	 * 
	 */
	private void initPanel() {
		currentPasswordBar.setValue(0);
		generatedPasswordBar.setValue(0);
		currentPasswordBar.setString(language.getString("Display"));
		generatedPasswordBar.setString(language.getString("Display"));
		passwordPanel.reset();
		passwordChanged = false;
		application.clearClipboard();
		String label = application.getLocalization()
				.getString(application.getProperties().getProperty(PasswordConstants.PREF_OUTPUT));
		buttonShowPassword.setText(label);
		buttonGeneratePassword.setText(label);
		tabbsPanel.setSelectedIndex(access != null && !access.isPasswordGenerated() ? 1 : 0);
	}

	/**
	 * Cancel and exit .
	 * 
	 */
	private void cancel() {
		initPanel();
		dialog.dispose();
	}

	/**
	 * Confirm the change password.
	 * 
	 * @return true when the password was changed.
	 * 
	 * @throws IllegalArgumentException
	 *             when an error occurs attempting to decipher a user password.
	 * @throws IllegalStateException
	 *             when the generator internal status is in error.
	 * @throws BadPaddingException
	 *             when an error occurs attempting to decipher a user password.
	 * @throws IllegalBlockSizeException
	 *             when an error occurs attempting to decipher a user password.
	 */
	private void update()
			throws IllegalArgumentException, IllegalStateException, BadPaddingException, IllegalBlockSizeException {
		if (access_new.isPasswordGenerated()) {
			// Case: generated password
			try {
				if (access.isPasswordDefined())
					// Log the old password
					application.saveStatus(access.getTraceID(access.getFactory().getIndex(AccessFactory.PASSWORD)),
							new String(application.getPassword(access)));
				setValue(access, AccessFactory.PASSWORD_TYPE, updatePasswordRules(passwordType.getSelectedItem() + ""));
				setValue(access, AccessFactory.GENERATE, access_new.isPasswordGenerated() + "");
				setValue(access, AccessFactory.PWD_LENGTH, pwd_length.getValue() + "");
				setValue(access, AccessFactory.CHARS, chars.isSelected() + "");
				setValue(access, AccessFactory.NUMBERS, numbers.isSelected() + "");
				setValue(access, AccessFactory.SYMBOLS, symbols.isSelected() + "");
				// Clean the previous user password
				access.setValue(AccessFactory.PASSWORD, "");
				access.setSalt(access_new.getSalt());
				access.updateExpirationDate();
				application.notifyChange();
			} catch (IllegalArgumentException e) {
				throw new IllegalStateException(e.getCause());
			}
		}
		// Case: user defined password
		else if (access_new.isPasswordDefined()) {
			if (passwordPanel.passwordCheck()) {
				// Leave unchanged the previous settings
				try {
					if (access.isPasswordDefined()) // Log the old password
						application.saveStatus(access.getTraceID(access.getFactory().getIndex(AccessFactory.PASSWORD)),
								new String(application.getPassword(access)));

					setValue(access, AccessFactory.GENERATE, access_new.isPasswordGenerated() + "");
					// setValue(access, AccessFactory.PASSWORD_TYPE,
					// IdentityEncoder.algorithm);
					setValue(access, AccessFactory.PASSWORD_TYPE, updatePasswordRules(IdentityEncoder.algorithm));

					access.setUserPassword(new String(access_new.getUserPassword()));
					application.encryptPassword(access);
					access.updateExpirationDate();
					application.notifyChange();
				} catch (IllegalArgumentException e) {
					throw new IllegalStateException(e.getCause());
				} catch (DigestException e) {
					throw new IllegalStateException(e.getCause());
				}
			} else {
				passwordPanel.reset();
			}
		}
	}

	/**
	 * Skip the blank rules.
	 * 
	 * @param algorithm
	 *            algorithm.
	 * @return the value.
	 */
	private String updatePasswordRules(String algorithm) {
		String r = algorithm + "";
		if (algorithm.equals(IdentityEncoder.algorithm))
			return IdentityEncoder.algorithm;
		for (int i = 0; i < password_rules.length; i++) {
			// Skip the blank rules
			if (!password_rules[i].getSelectedItem().equals(PasswordDictionary.Empty.name)) {
				r = r + "." + password_rules[i].getSelectedItem();
			}
		}
		return r;
	}

	/**
	 * @param pw
	 */
	private void displayPassword(JProgressBar field, String pwd) {
		String opt = application.getProperties().getProperty(PasswordConstants.PREF_OUTPUT);

		if (opt.equals(PasswordConstants.OPT_DISPLAY) || opt.equals(PasswordConstants.OPT_DISPLAYCOPY)) {
			field.setString(pwd);
			field.setValue(PasswordGauge.getStrength(pwd.toCharArray()));

		}
		if (opt.equals(PasswordConstants.OPT_COPY) || opt.equals(PasswordConstants.OPT_DISPLAYCOPY)) {
			application.clearClipboard();
			application.copyToClipboard(pwd);
		}
	}

	/**
	 * Show the current access password.
	 */
	private void showCurrentPassword() throws InvalidKeyException, IllegalStateException, IllegalArgumentException,
			IllegalArgumentException, BadPaddingException, IllegalBlockSizeException {
		displayPassword(currentPasswordBar, new String(application.getPassword(access)));
	}

	/**
	 * Show the new user defined password.
	 */
	private void showNewUserPassword() throws InvalidKeyException, IllegalStateException, IllegalArgumentException {
		// Update new password preferences
		access_new.clearPassword();
		access_new.get(AccessFactory.PWD_LENGTH).setObject(pwd_length.getValue());
		access_new.get(AccessFactory.CHARS).setObject(chars.isSelected());
		access_new.get(AccessFactory.NUMBERS).setObject(numbers.isSelected());
		access_new.get(AccessFactory.SYMBOLS).setObject(symbols.isSelected());
		access_new.setUserPassword(new String(passwordPanel.getPassword()));

		String pw = access_new.getUserPassword();

		displayPassword(generatedPasswordBar, pw);
	}

	/**
	 * Show the new generated password.
	 */
	private void showNewGeneratedPassword()
			throws InvalidKeyException, IllegalStateException, IllegalArgumentException {
		// Update new password preferences
		access_new.clearPassword();
		access_new.get(AccessFactory.PWD_LENGTH).setObject(pwd_length.getValue());
		// access_new.get(AccessFactory.PASSWORD_TYPE).setObject(passwordType.getSelectedItem());
		access_new.get(AccessFactory.PASSWORD_TYPE).setObject(updatePasswordRules(passwordType.getSelectedItem() + ""));
		access_new.get(AccessFactory.CHARS).setObject(chars.isSelected());
		access_new.get(AccessFactory.NUMBERS).setObject(numbers.isSelected());
		access_new.get(AccessFactory.SYMBOLS).setObject(symbols.isSelected());
		char p[] = application.newPassword(access_new);
		String pw = new String(p);
		displayPassword(generatedPasswordBar, pw);
	}

	/**
	 * Set a value and log the change.
	 * 
	 * @param anAccess
	 *            the access.
	 * @param key
	 *            the key field changed.
	 * @param value
	 *            the new field value.
	 * @param changelog
	 *            save the log of old value.
	 */
	private void setValue(Access anAccess, String key, String value) {
		// Set the value
		String safevalue = value + "";
		int col = anAccess.getFactory().getIndex(key);
		Object oldValue = anAccess.setValue(key, safevalue);
		if (oldValue != null && !anAccess.getFactory().serialize(anAccess.getFactory().getType(col).getName(), oldValue)
				.equals(safevalue)) {
			application.saveStatus(anAccess.getTraceID(col), oldValue);
		}
	}

	/**
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent event) {

		if (event.getSource().equals(buttonShowPassword))
		// Old Password
		{
			try {
				showCurrentPassword();
			} catch (IllegalStateException e) {
				// generator internal status is in error.
				PopMessage.displayError(ChangeAccessPasswordPanel.this, language.getString("Security Exception"),
						language.getString("Passphrase is missing"));
			} catch (InvalidKeyException e) {
				// when the initialization step fails because of wrong
				// parameters.
				PopMessage.displayError(ChangeAccessPasswordPanel.this, language.getString("Security Exception"),
						language.getString("Invalid key parameters"));
			} catch (IllegalArgumentException e) {
				// when the generator internal status is in error.
				PopMessage.displayError(ChangeAccessPasswordPanel.this, language.getString("Security Exception"),
						language.getString("Wrong security parameters"));
			} catch (BadPaddingException e) {
				// when an error occurs attempting to decipher a user password.
				PopMessage.displayError(ChangeAccessPasswordPanel.this, language.getString("Cipher exception"),
						language.getString("Bad padding error"));
			} catch (IllegalBlockSizeException e) {
				// when an error occurs attempting to decipher a user password.
				PopMessage.displayError(ChangeAccessPasswordPanel.this, language.getString("Cipher exception"),
						language.getString("Wrong cipher block size"));
			}
		}
		// New generated password
		else if (event.getSource().equals(buttonGeneratePassword) && tabbsPanel.getSelectedIndex() == 0) {
			try {
				showNewGeneratedPassword();
				passwordChanged = true;
			} catch (InvalidKeyException e1) {
				passwordChanged = false;
				PopMessage.displayError(ChangeAccessPasswordPanel.this, language.getString("Security Exception"),
						language.getString("Invalid key parameters"));
			} catch (IllegalStateException e1) {
				passwordChanged = false;
				PopMessage.displayError(ChangeAccessPasswordPanel.this, language.getString("Security Exception"),
						language.getString("Invalid security state"));
			} catch (IllegalArgumentException e1) {
				passwordChanged = false;
				PopMessage.displayError(ChangeAccessPasswordPanel.this, language.getString("Security Exception"),
						language.getString("Wrong data length"));
			}
		}
		// New user password
		else if (event.getSource().equals(buttonGeneratePassword) && tabbsPanel.getSelectedIndex() == 1) {
			try {
				// Check the password
				if (passwordPanel.passwordCheck()) {
					showNewUserPassword();
					passwordChanged = true;
				} else {
					passwordChanged = false;
				}
			} catch (InvalidKeyException e1) {
				passwordChanged = false;
				PopMessage.displayError(ChangeAccessPasswordPanel.this, language.getString("Security Exception"),
						language.getString("Invalid key parameters"));
			} catch (IllegalStateException e1) {
				passwordChanged = false;
				PopMessage.displayError(ChangeAccessPasswordPanel.this, language.getString("Security Exception"),
						language.getString("Invalid security state"));
			} catch (IllegalArgumentException e1) {
				passwordChanged = false;
				PopMessage.displayError(ChangeAccessPasswordPanel.this, language.getString("Security Exception"),
						language.getString("Wrong data length"));
			}
		}
		// Cancel
		else if (event.getActionCommand().equals(CANCEL)) {
			cancel();
		}
		// Confirm
		else if (event.getActionCommand().equals(CONFIRM) && passwordChanged) {
			try {
				update();
				initPanel();
				dialog.dispose();
			} catch (IllegalStateException e) {
				passwordChanged = false;
				PopMessage.displayError(ChangeAccessPasswordPanel.this, language.getString("Security Exception"),
						language.getString("Invalid security state"));
			} catch (IllegalArgumentException e) {
				passwordChanged = false;
				PopMessage.displayError(ChangeAccessPasswordPanel.this, language.getString("Security Exception"),
						language.getString("Wrong data length"));
			} catch (BadPaddingException e) {
				passwordChanged = false;
				PopMessage.displayError(ChangeAccessPasswordPanel.this, language.getString("Cipher exception"),
						language.getString("Bad padding error"));
			} catch (IllegalBlockSizeException e) {
				passwordChanged = false;
				PopMessage.displayError(ChangeAccessPasswordPanel.this, language.getString("Cipher exception"),
						language.getString("Wrong cipher block size"));
			}
		} else {
			PopMessage.displayError(ChangeAccessPasswordPanel.this, language.getString("Security Exception"),
					language.getString("No password"));
		}
		if (application.getTaskScheduler().isRunningCheck(ChangeAccessPasswordPanel.this.hashCode() + "")) {
			application.getTaskScheduler().stop(ChangeAccessPasswordPanel.this.hashCode() + "");
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (access_new == null)
			return;
		access_new.setValue(AccessFactory.GENERATE, "" + (tabbsPanel.getSelectedIndex() == 0));
		access_new.clearPassword();

	}

	private class AddRuleCommand extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public AddRuleCommand(String name) {
			super(name);
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			String[] rules = new String[password_rules.length + 1];
			for (int i = 0; i < password_rules.length; i++) {
				rules[i] = password_rules[i].getSelectedItem() + "";
			}
			rules[password_rules.length] = PasswordDictionary.Empty.name;

			initPasswordRules(rules);

			dialog.pack();
		}

	}

	private class RemoveRuleCommand extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public RemoveRuleCommand(String name) {
			super(name);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int j = 0;
			String[] rules = new String[password_rules.length];
			for (int i = 0; i < password_rules.length; i++) {
				if (!password_rules[i].getSelectedItem().equals(PasswordDictionary.Empty.name)) {
					rules[j] = password_rules[i].getSelectedItem() + "";
					j++;
				}
			}
			String[] tmp_rules = new String[j];
			System.arraycopy(rules, 0, tmp_rules, 0, j);
			initPasswordRules(tmp_rules);

			dialog.pack();
		}

	}

	/**
	 * Show an edit tab dialog.
	 * 
	 * @param a
	 *            the access.
	 */
	public void showDialog(Access a) {
		class InternalWindowsListener extends WindowAdapter {
			@Override
			public void windowClosing(WindowEvent event) {
			}
		}
		if (dialog == null) {
			dialog = new JDialog();
			dialog.setTitle(language.getString("Change access password"));
			dialog.setModal(true);
			dialog.addWindowListener(new InternalWindowsListener());
			dialog.getContentPane().setLayout(new BorderLayout());
			dialog.getContentPane().add(this, BorderLayout.CENTER);
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			createGUI();
		}
		init(a);
		initPanel();
		dialog.setLocationRelativeTo(application.getFrame());
		dialog.pack();
		dialog.setVisible(true);
	}

	public static void main(String[] args) {
		// Schedule a job for the event dispatch thread
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				ChangeAccessPasswordPanel demo = new ChangeAccessPasswordPanel(new Password());
				Access a = AccessFactory.getInstance().newRow();
				a.set(AccessFactory.ACCESS, new StyleObject("access", null));
				System.out.println(a);
				demo.showDialog(a);
			}
		});
	}
}
