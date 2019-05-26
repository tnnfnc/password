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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ListResourceBundle;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

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
public class CipherStreamPanel extends AbstractEditingPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4202613345456128988L;

	private JComboBox<SecurityProvider.Service> blockCipher;
	private JComboBox<SecurityProvider.Service> cipherMode;
	private JComboBox<SecurityProvider.Service> padSchema;
	private JComboBox<String> keyLen;
	private ListResourceBundle resource;
	protected Properties properties;
	protected SecurityProvider securityProvider;

	/**
	 * Creates a block cipher stream chooser.
	 * 
	 * @param p
	 *            the defaults.
	 */
	public CipherStreamPanel(Properties p) {
		super();
		this.properties = p;
		securityProvider = new SecurityProvider();
		createGUI();
		initGUI();
	}

	private void createGUI() {
		resource = (ListResourceBundle) ResourceBundle
				.getBundle(SecurityResources.class.getName());
		blockCipher = new JComboBox<SecurityProvider.Service>(
				new DefaultComboBoxModel<SecurityProvider.Service>());
		cipherMode = new JComboBox<SecurityProvider.Service>(
				new DefaultComboBoxModel<SecurityProvider.Service>());
		padSchema = new JComboBox<SecurityProvider.Service>(
				new DefaultComboBoxModel<SecurityProvider.Service>());
		keyLen = new JComboBox<String>(new DefaultComboBoxModel<String>());

		fillComboModel(SecurityProvider.TYPE_BLOCKCIPHER, blockCipher);
		fillComboModel(SecurityProvider.TYPE_BLOCKCIPHER_MODE, cipherMode);
		fillComboModel(SecurityProvider.TYPE_BLOCK_PADDING, padSchema);

		ActionListener comboBoxListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource().equals(blockCipher)) {
					// Key treat
					int selIndex = keyLen.getSelectedIndex();
					SecurityProvider.Service s = (SecurityProvider.Service) blockCipher
							.getSelectedItem();
					keyLen.removeAllItems();
					for (String k : s.getKeyLenght()) {
						keyLen.addItem(k);
					}
					if (selIndex > -1 && selIndex < keyLen.getModel().getSize()) {
						keyLen.setSelectedIndex(selIndex);
					} else {
						keyLen.setSelectedIndex(0);
					}
				}
			}
		};

		blockCipher.addActionListener(comboBoxListener);
		cipherMode.addActionListener(comboBoxListener);
		keyLen.addActionListener(comboBoxListener);
		padSchema.addActionListener(comboBoxListener);

		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		GridBagLayoutUtility.initConstraints(gbc);
		gbc.insets.set(2, 2, 2, 2);

		GridBagLayoutUtility.add(new JLabel(resource.getString("cipher")),
				blockCipher, this, gbc);
		GridBagLayoutUtility.newLine(gbc);
		GridBagLayoutUtility.add(new JLabel(resource.getString("mode")),
				cipherMode, this, gbc);
		GridBagLayoutUtility.newLine(gbc);
		GridBagLayoutUtility.add(new JLabel(resource.getString("pad")),
				padSchema, this, gbc);
		GridBagLayoutUtility.newLine(gbc);
		GridBagLayoutUtility.add(new JLabel(resource.getString("keylen")),
				keyLen, new JLabel(resource.getString("bits")), this, gbc);

	}

	private void fillComboModel(String type, JComboBox<Service> cb) {
		SecurityProvider.Service s[] = securityProvider
				.getAvailableServices(type);
		for (int i = 0; i < s.length; i++) {
			cb.addItem(s[i]);
		}
	}

	/**
	 * Update dialog fields from the input parameters.
	 */
	@Override
	public void initGUI() {
		String[] cipherAlgorithm = new String[3];
		if (properties.getProperty(EncryptionPreferences.PREF_CIPHER_ALGORITHM) != null) {
			// properties
			cipherAlgorithm = SecurityProvider.parseRequest(properties
					.getProperty(EncryptionPreferences.PREF_CIPHER_ALGORITHM));
		}

		for (int i = 0; i < blockCipher.getItemCount(); i++) {
			if (blockCipher.getItemAt(i).getId()
					.equalsIgnoreCase(cipherAlgorithm[0] + "")) {
				blockCipher.setSelectedIndex(i);
				break;
			} else {
				blockCipher.setSelectedIndex(0);
			}
		}

		// encryption_mode: CBC
		for (int i = 0; i < cipherMode.getItemCount(); i++) {
			if (cipherMode.getItemAt(i).getId()
					.equalsIgnoreCase(cipherAlgorithm[1] + "")) {
				cipherMode.setSelectedIndex(i);
				break;
			} else {
				cipherMode.setSelectedIndex(0);
			}
		}

		// padding
		for (int i = 0; i < padSchema.getItemCount(); i++) {
			if (padSchema.getItemAt(i).getId()
					.equalsIgnoreCase(cipherAlgorithm[2] + "")) {
				padSchema.setSelectedIndex(i);
				break;
			} else {
				padSchema.setSelectedIndex(0);
			}
		}

		// encryption_strength
		for (int i = 0; i < keyLen.getItemCount(); i++) {
			if (keyLen.getItemAt(i).equalsIgnoreCase(
					properties.getProperty(EncryptionPreferences.PREF_KEYSIZE))) {
				keyLen.setSelectedIndex(i);
				break;
			} else {
				keyLen.setSelectedIndex(keyLen.getItemCount() - 1);
			}
		}
	}

	@Override
	public void update() {
		// properties.setProperty(EncryptionConstants.BLOCKCIPHER,
		// ((Service) blockCipher.getSelectedItem()).getId());
		// properties.setProperty(EncryptionConstants.CIPHERMODE,
		// ((Service) cipherMode.getSelectedItem()).getId());
		// properties.setProperty(EncryptionConstants.PADDING,
		// ((Service) padSchema.getSelectedItem()).getId());

		properties.setProperty(EncryptionPreferences.PREF_KEYSIZE,
				keyLen.getSelectedItem() + "");

		StringBuffer b = new StringBuffer();
		b.append(((SecurityProvider.Service) blockCipher.getSelectedItem())
				.getId());
		b.append("/");
		b.append(((SecurityProvider.Service) cipherMode.getSelectedItem())
				.getId());
		b.append("/");
		b.append(((SecurityProvider.Service) padSchema.getSelectedItem())
				.getId());

		properties.setProperty(EncryptionPreferences.PREF_CIPHER_ALGORITHM,
				b.toString());

	}

	@Override
	public String getLabel() {
		return resource.getString("Block cipher parameters");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		blockCipher.setEnabled(enabled);
		cipherMode.setEnabled(enabled);
		keyLen.setEnabled(enabled);
		padSchema.setEnabled(enabled);
		super.setEnabled(enabled);
	}

	// private void setAlgorithm(SecurityProvider.Service algorithmName) {
	// DefaultComboBoxModel<Service> m = (DefaultComboBoxModel<Service>)
	// blockCipher.getModel();
	// if (m.getSize() == 0) {
	// m.addElement(algorithmName);
	// }
	// m.setSelectedItem(algorithmName);
	// }

	public static void main(String[] args) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame f = new JFrame("Cipher Panel Light test");
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				Properties p = new Properties();
				p.setProperty(EncryptionPreferences.PREF_CIPHER_ALGORITHM,
						"SERPENT/CBC/PAD8");

				// p.setProperty(EncryptionConstants.CIPHERMODE, "ECB");

				// p.setProperty(EncryptionConstants.PADDING, "PAD8");

				p.setProperty(EncryptionPreferences.PREF_KEYSIZE, "1024");

				CipherStreamPanel csc = new CipherStreamPanel(p);
				f.getContentPane().add(csc);
				f.pack();
				f.setVisible(true);
				csc.update();

			}
		});
	}
}
