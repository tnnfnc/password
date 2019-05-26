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

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ListResourceBundle;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import it.tnnfnc.apps.application.properties.AbstractEditingPanel;
import it.tnnfnc.apps.application.ui.GridBagLayoutUtility;
import it.tnnfnc.apps.application.ui.SecurityResources;
import it.tnnfnc.security.EncryptionPreferences;
import it.tnnfnc.security.SecurityProvider;
import it.tnnfnc.security.SecurityProvider.Service;

/**
 * This class implements a view for selecting a block cipher stream blockCipher.
 * 
 * @author Franco Toninato
 * 
 */
public class PasswordBaseKeyPanel extends AbstractEditingPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4202613345456128988L;

	private JComboBox<Service> hashAlgorithm;
	private JComboBox<Service> mac;
	private JComboBox<Service> randomAlgorithm;
	private JComboBox<String> iterations;
	private ListResourceBundle resource;
	protected Properties properties;
	protected SecurityProvider securityProvider;

	// private JComboBox keySize;
	// private ChangePasswordPanel loginPane;

	/**
	 * Creates a panel for choosing the parameters for password based key
	 * generation.
	 * 
	 * @param p
	 *            defaults.
	 */
	public PasswordBaseKeyPanel(Properties p) {
		super();
		this.properties = p;
		resource = (ListResourceBundle) ResourceBundle
				.getBundle(SecurityResources.class.getName());
		securityProvider = new SecurityProvider();
		createGUI();
		initGUI();
	}

	private void createGUI() {
		JTextField hmac = new JTextField("HMAC/");
		hmac.setEditable(false);
		hashAlgorithm = new JComboBox<Service>(new DefaultComboBoxModel<Service>());
		randomAlgorithm = new JComboBox<Service>(new DefaultComboBoxModel<Service>());
		mac = new JComboBox<Service>(new DefaultComboBoxModel<Service>());
		iterations = new JComboBox<String>(new String[] { "1", "10", "100", "1000",
				"10000", "100000", "1000000", "10000000" });

		fillComboModel(EncryptionPreferences.PREF_HASH, hashAlgorithm);
		fillComboModel(EncryptionPreferences.PREF_PBKDF, randomAlgorithm);
		fillComboModel(EncryptionPreferences.PREF_HMAC, mac);

		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		GridBagLayoutUtility.initConstraints(gbc);
		gbc.insets.set(2, 2, 2, 2);

		GridBagLayoutUtility.add(
				new JLabel(resource.getString("random function")),
				randomAlgorithm, this, gbc);

		GridBagLayoutUtility.newLine(gbc);
		JPanel p = new JPanel(new FlowLayout());
		p.add(mac);
		p.add(new JLabel("/"));
		p.add(hashAlgorithm);
		GridBagLayoutUtility.add(new JLabel(resource.getString("mac")), p,
				this, gbc);
		GridBagLayoutUtility.newLine(gbc);
		GridBagLayoutUtility.add(new JLabel(resource.getString("iterations")),
				iterations, this, gbc);

	}

	private void fillComboModel(String type, JComboBox<Service> cb) {
		SecurityProvider.Service s[] = securityProvider
				.getAvailableServices(type);
		for (int i = 0; i < s.length; i++) {
			cb.addItem(s[i]);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		mac.setEnabled(enabled);
		hashAlgorithm.setEnabled(enabled);
		randomAlgorithm.setEnabled(enabled);
		iterations.setEnabled(enabled);
		super.setEnabled(enabled);
	}

	/**
	 * Update dialog fields from the input parameters.
	 */
	@Override
	public void initGUI() {
		//

		if (properties.getProperty(EncryptionPreferences.PREF_HMAC) != null) {
			String hmacAlgorithm[] = new String[2];
			hmacAlgorithm = SecurityProvider.parseRequest(properties
					.getProperty(EncryptionPreferences.PREF_HMAC));

			// HMAC
			for (int i = 0; i < mac.getItemCount(); i++) {
				if (mac.getItemAt(i).getId()
						.equalsIgnoreCase(hmacAlgorithm[0] + "")) {
					mac.setSelectedIndex(i);
					break;
				}
			}
			// Hash
			for (int i = 0; i < hashAlgorithm.getItemCount(); i++) {
				if (hashAlgorithm.getItemAt(i)
						.getId().equalsIgnoreCase(hmacAlgorithm[1] + "")) {
					hashAlgorithm.setSelectedIndex(i);
					break;
				}
			}

		} else
		// Default
		{
			hashAlgorithm
					.setSelectedIndex(hashAlgorithm.getModel().getSize() - 1);

		}

		//
		for (int i = 0; i < randomAlgorithm.getItemCount(); i++) {
			if (randomAlgorithm.getItemAt(i)
					.getId()
					.equalsIgnoreCase(
							properties
									.getProperty(EncryptionPreferences.PREF_PBKDF))) {
				randomAlgorithm.setSelectedIndex(i);
				break;
			}

		}
		//
		for (int i = 0; i < iterations.getItemCount(); i++) {
			if (((String) iterations.getItemAt(i)).equalsIgnoreCase(properties
					.getProperty(EncryptionPreferences.PREF_ITERATIONS))) {
				iterations.setSelectedIndex(i);
				break;
			} else {
				iterations.setSelectedIndex(3);
			}
		}

	}

	@Override
	public void update() {
		properties.setProperty(
				EncryptionPreferences.PREF_HMAC,
				((SecurityProvider.Service) mac.getSelectedItem()).getId()
						+ "/"
						+ ((SecurityProvider.Service) hashAlgorithm
								.getSelectedItem()).getId());
		properties.setProperty(EncryptionPreferences.PREF_PBKDF,
				((SecurityProvider.Service) randomAlgorithm.getSelectedItem())
						.getId());
		properties.setProperty(EncryptionPreferences.PREF_ITERATIONS,
				iterations.getSelectedItem() + "");
	}

	@Override
	public String getLabel() {
		return resource.getString("Key parameters");
	}

	public static void main(String[] args) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame f = new JFrame("Key from password");
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				Properties p = new Properties();
				PasswordBaseKeyPanel csc = new PasswordBaseKeyPanel(p);
				System.out.println(p);
				f.getContentPane().add(csc);
				f.pack();
				f.setVisible(true);
			}
		});
	}

}
