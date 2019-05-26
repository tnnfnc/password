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

import java.security.DigestException;
import java.security.InvalidKeyException;
import java.util.Properties;

import javax.swing.border.TitledBorder;

import it.tnnfnc.apps.application.ui.AbstractDialog;
import it.tnnfnc.apps.application.ui.FieldLayout;
import it.tnnfnc.apps.application.ui.LoginPanel;
import it.tnnfnc.apps.application.ui.PopMessage;
import it.tnnfnc.security.SecurityObject;

public class BlockCipherChooser extends AbstractDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Properties p;
	private LoginPanel loginPanel;
	private CipherStreamPanel cipherPanel;
	private PasswordBaseKeyPanel keyDerivatorPanel;
	private SecurityObject securityObject;
	/**
	 * Display option: new security.
	 */
	public static int NEW_SECURITY = 0;
	/**
	 * Display option: open existing security.
	 */
	public static int OPEN_SECURITY = 1;

	/**
	 * @param p
	 *            properties.
	 */
	public BlockCipherChooser(Properties p) {
		this(p, LoginPanel.PASSWORD_CONFIRM);
	}

	/**
	 * /**
	 * 
	 * @param p
	 *            properties.
	 * @param options
	 *            options for password display.
	 */
	public BlockCipherChooser(Properties p, int options) {
		this.p = p;
		if (options == OPEN_SECURITY) {
			loginPanel = new LoginPanel(LoginPanel.PASSWORD);
		} else {
			loginPanel = new LoginPanel(LoginPanel.PASSWORD_CONFIRM);
		}
		// loginPanel.setConfirmRequired(true);
		// securityProviderPanel = new SecurityProviderPanel(p);
		cipherPanel = new CipherStreamPanel(p);
		keyDerivatorPanel = new PasswordBaseKeyPanel(p);
		createGUI();
	}

	/**
	 * Get the security object.
	 * 
	 * @return the securityObject
	 */
	public SecurityObject getSecurityObject() {
		return securityObject;
	}

	private void message(String e) {
		PopMessage.displayError(this, language.getString("Security Exception"),
				e);

	}

	private void createGUI() {
		// Save the properties
		enableOption(AbstractDialog.Options.OK_OPTION, language.getString("OK"));
		// Close dialog without saving
		enableOption(AbstractDialog.Options.CLOSE_OPTION,
				language.getString("Close"));

		//
		keyDerivatorPanel.setBorder(new TitledBorder(keyDerivatorPanel
				.getLabel()));
		cipherPanel.setBorder(new TitledBorder(cipherPanel.getLabel()));

		FieldLayout l = new FieldLayout(inputPanel);

		l.append(0, 2, null, loginPanel);
		// l.append(0, 2, null, securityProviderPanel);
		l.append(0, 1, null, keyDerivatorPanel);
		l.pair(0, 1, null, cipherPanel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.apps.AbstractDialog#performOK()
	 */
	@Override
	protected boolean performOK() {
		if (loginPanel.passwordCheck() == false) {
			message(language.getString("Invalid password"));
			return false;
		} else {
			try {
				keyDerivatorPanel.update();
				cipherPanel.update();
				// securityProviderPanel.update();
				char[] password = new char[0];
				if (loginPanel.getType() == LoginPanel.PASSWORD_CHANGE
						|| loginPanel.getType() == LoginPanel.PASSWORD_CONFIRM) {
					password = loginPanel.getNewPassword();
				} else {
					password = loginPanel.getPassword();
				}
				if (password != null) {
					securityObject = new SecurityObject(p,
							LoginPanel.toBytes(password));
					return true;
				} else {
					message(language.getString("Invalid password"));
				}
			} catch (InvalidKeyException e) {
				message(e.getClass().getName() + ":\n " + e.getMessage());
				e.printStackTrace();
			} catch (IllegalStateException e) {
				message(e.getClass().getName() + ":\n " + e.getMessage());
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				message(e.getClass().getName() + ":\n " + e.getMessage());
				e.printStackTrace();
			} catch (DigestException e) {
				message(e.getClass().getName() + ":\n " + e.getMessage());
				e.printStackTrace();
			} catch (NullPointerException e) {
				message(e.getClass().getName() + ":\n " + e.getMessage());
				e.printStackTrace();
			}
			return false;
		}
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		Properties q = new Properties();

		final BlockCipherChooser t = new BlockCipherChooser(q);
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				t.setTitle(this.getClass().getName());
				t.setName("name");
				t.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				t.pack();
				t.setVisible(true);
			}
		});
	}
}
