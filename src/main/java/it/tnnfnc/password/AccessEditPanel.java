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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import it.tnnfnc.apps.application.ui.FiveStarsCombo; 
import it.tnnfnc.apps.application.ui.GridBagLayoutUtility;
import it.tnnfnc.password.domain.Access;
import it.tnnfnc.password.domain.AccessFactory;
import it.tnnfnc.table.cell.RankStarValue;
import it.tnnfnc.table.row.AbstractTableRowDetail;

/**
 * A panel for access management.
 * 
 * @author Franco Toninato
 * 
 */
public class AccessEditPanel extends AbstractTableRowDetail<Access> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Password application;
	private ChangeInputListener inputListener;
	private Access internalAccess;
	private HashMap<String, JComponent> inputFieldsMap;
	private boolean changed = false; // Data changed

	private static int TEXTLONG = 20;
	// private static int TEXTMID = 10;
	private static int TEXTSHORT = 5;

	// Input fields
	private JTextField category;
	private JTextField access;
	private FiveStarsCombo important;
	private JTextField user;
	private JTextArea text;
	private JTextField url;
	private JTextField statistics;
	private JTextField created_on;
	private JTextField expire_on;
	private JTextField changed_on;
	private JTextField status;
	private JTextField validity;
	private JTextField password_type;
	private JCheckBox generate;
	private JCheckBox expire;
	private JCheckBox chars;
	private JCheckBox numbers;
	private JCheckBox symbols;
	private JTextField pwd_length;
	private JTextField salt;
	private JPasswordField password;

	private JPanel fieldsContainer;

	// private boolean appendMore = true;

	public AccessEditPanel(Password application) {
		this.application = application;
		initialise();
		createGuiOld();
	}

	/**
	 * Set a change.
	 * 
	 * @param changed
	 *            the changed to set
	 */
	private void setChanged(boolean changed) {
		this.changed = changed;
	}

	private void initialise() {
		inputListener = new ChangeInputListener();
		category = new JTextField("", TEXTLONG);
		category.setEditable(false);
		//
		access = new JTextField("", TEXTLONG);
		access.getDocument().addDocumentListener(inputListener);
		//
		important = new FiveStarsCombo();
		important.addItemListener(inputListener);
		//
		url = new JTextField("", TEXTLONG);
		url.getDocument().addDocumentListener(inputListener);
		//
		user = new JTextField("", TEXTLONG);
		user.getDocument().addDocumentListener(inputListener);
		//
		statistics = new JTextField("0", TEXTSHORT);
		statistics.setEditable(false);
		// DateFormat.getDateInstance(DateFormat.MEDIUM).format(1L);
		String now = formatDate(new Date());
		created_on = new JTextField(now);
		created_on.setEditable(false);
		//
		changed_on = new JTextField(now);
		changed_on.setEditable(false);
		//
		status = new JTextField("", TEXTLONG);
		status.setEditable(false);
		//
		text = new JTextArea("", 3, TEXTLONG);
		text.getDocument().addDocumentListener(inputListener);
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		//
		expire_on = new JTextField(now);
		expire_on.setEditable(false);
		//
		validity = new JTextField("", TEXTSHORT);
		validity.setEditable(false);
		//
		expire = new JCheckBox();
		expire.addActionListener(inputListener);
		//
		generate = new JCheckBox();
		generate.setEnabled(false);
		//
		pwd_length = new JTextField("", TEXTSHORT);
		pwd_length.setEditable(false);
		//
		chars = new JCheckBox();
		chars.setEnabled(false);
		// //
		numbers = new JCheckBox();
		numbers.setEnabled(false);
		// //
		symbols = new JCheckBox();
		symbols.setEnabled(false);
		//
		password = new JPasswordField();
		password.setEditable(false);
		//
		salt = new JTextField();
		salt.setEditable(false);
		//
		password_type = new JTextField();
		password_type.setEditable(false);
		//

		//
		inputFieldsMap = new HashMap<String, JComponent>();
		inputFieldsMap.put(AccessFactory.ACCESS, access);
		inputFieldsMap.put(AccessFactory.RATING, important);
		inputFieldsMap.put(AccessFactory.URL, url);
		inputFieldsMap.put(AccessFactory.USER, user);
		inputFieldsMap.put(AccessFactory.TEXT, text);
		inputFieldsMap.put(AccessFactory.STATUS, status);
		inputFieldsMap.put(AccessFactory.STATISTICS, statistics);
		inputFieldsMap.put(AccessFactory.CREATED_ON, created_on);
		inputFieldsMap.put(AccessFactory.EXPIRE_ON, expire_on);
		inputFieldsMap.put(AccessFactory.EXPIRE, expire);
		inputFieldsMap.put(AccessFactory.CHANGED_ON, changed_on);
		inputFieldsMap.put(AccessFactory.VALIDITY, validity);
		inputFieldsMap.put(AccessFactory.GENERATE, generate);
		inputFieldsMap.put(AccessFactory.PWD_LENGTH, pwd_length);
		inputFieldsMap.put(AccessFactory.CHARS, chars);
		inputFieldsMap.put(AccessFactory.NUMBERS, numbers);
		inputFieldsMap.put(AccessFactory.SYMBOLS, symbols);
		//
		inputFieldsMap.put(AccessFactory.PASSWORD_TYPE, null);
		inputFieldsMap.put(AccessFactory.SALT, null);
		inputFieldsMap.put(AccessFactory.PASSWORD, null);
	}

	private void createGuiOld() {
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		JPanel panel = new JPanel(gbl);
		this.setLayout(new BorderLayout());
		this.add(panel, BorderLayout.CENTER);

		// begin of Access
		GridBagLayoutUtility.initConstraints(gbc);
		GridBagLayoutUtility.add(new JLabel(application.getLocalization()
				.getString(AccessFactory.CATEGORY)), category, panel, gbc);
		GridBagLayoutUtility.right(gbc);
		GridBagLayoutUtility.add(new JLabel(application.getLocalization()
				.getString(AccessFactory.ACCESS)), access, panel, gbc);

		GridBagLayoutUtility.newLine(gbc);
		GridBagLayoutUtility.add(new JLabel(application.getLocalization()
				.getString(AccessFactory.URL)), url, panel, gbc);
		GridBagLayoutUtility.right(gbc);
		GridBagLayoutUtility.add(new JLabel(application.getLocalization()
				.getString(AccessFactory.USER)), user, panel, gbc);

		// start of Description box
		GridBagLayoutUtility.newLine(gbc);
		// GridBagLayoutUtility.spanBlock(gbc, 4, 1);
		JScrollPane scrollingText = new JScrollPane(text,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		GridBagLayoutUtility.add(new JLabel(application.getLocalization()
				.getString(AccessFactory.TEXT)), scrollingText, panel, gbc);
		// end of Description box
		GridBagLayoutUtility.right(gbc);
		GridBagLayoutUtility.add(new JLabel(application.getLocalization()
				.getString(AccessFactory.STATUS)), status, panel, gbc);

		GridBagLayoutUtility.newLine(gbc);
		GridBagLayoutUtility.add(new JLabel(application.getLocalization()
				.getString(AccessFactory.RATING)), important, panel, gbc);
		GridBagLayoutUtility.right(gbc);
		GridBagLayoutUtility.add(new JLabel(application.getLocalization()
				.getString(AccessFactory.STATISTICS)), statistics, new JLabel(
				application.getLocalization().getString("clicks")), panel, gbc);

		GridBagLayoutUtility.newLine(gbc);
		GridBagLayoutUtility.add(new JLabel(application.getLocalization()
				.getString(AccessFactory.CREATED_ON)), created_on, panel, gbc);
		GridBagLayoutUtility.right(gbc);
		GridBagLayoutUtility.add(new JLabel(application.getLocalization()
				.getString(AccessFactory.CHANGED_ON)), changed_on, panel, gbc);

		GridBagLayoutUtility.newLine(gbc);
		GridBagLayoutUtility.add(new JLabel(application.getLocalization()
				.getString(AccessFactory.VALIDITY)), validity, new JLabel(
				application.getLocalization().getString("days")), panel, gbc);
		GridBagLayoutUtility.right(gbc);
		GridBagLayoutUtility.add(new JLabel(application.getLocalization()
				.getString(AccessFactory.EXPIRE_ON)), expire_on, panel, gbc);

		GridBagLayoutUtility.newLine(gbc);
		GridBagLayoutUtility.add(new JLabel(application.getLocalization()
				.getString(AccessFactory.EXPIRE)), expire, panel, gbc);
		// end of Access

		// begin of password settings f Len, char, num, sym
		GridBagLayoutUtility.newLine(gbc);
		GridBagLayoutUtility.add(new JLabel(application.getLocalization()
				.getString(AccessFactory.GENERATE)), generate, panel, gbc);
		// Others
		GridBagLayoutUtility.right(gbc);
		GridBagLayoutUtility.add(new JLabel(application.getLocalization()
				.getString(AccessFactory.PASSWORD_TYPE)), password_type, panel,
				gbc);
		// Length from to
		GridBagLayoutUtility.newLine(gbc);
		GridBagLayoutUtility.add(new JLabel(application.getLocalization()
				.getString(AccessFactory.PWD_LENGTH)), pwd_length, new JLabel(
				application.getLocalization().getString("digits")), panel, gbc);

		// Chars yes/no at least
//		GridBagLayoutUtility.newLine(gbc);
//		GridBagLayoutUtility.add(new JLabel(application.getLocalization()
//				.getString(AccessFactory.CHARS)), chars, panel, gbc);
//
//		// Numbers yes/no at least
//		GridBagLayoutUtility.newLine(gbc);
//		GridBagLayoutUtility.add(new JLabel(application.getLocalization()
//				.getString(AccessFactory.NUMBERS)), numbers, panel, gbc);
//
//		// Symbols yes/no at least
//		GridBagLayoutUtility.newLine(gbc);
//		GridBagLayoutUtility.add(new JLabel(application.getLocalization()
//				.getString(AccessFactory.SYMBOLS)), symbols, panel, gbc);
		// end of password settings f Len, char, num, sym

		
		
		// Others
		GridBagLayoutUtility.newLine(gbc);
		GridBagLayoutUtility.add(new JLabel(application.getLocalization()
				.getString(AccessFactory.SALT)), salt, panel, gbc);
		// Others
		GridBagLayoutUtility.newLine(gbc);
		GridBagLayoutUtility.add(new JLabel(application.getLocalization()
				.getString(AccessFactory.PASSWORD)), password, panel, gbc);
		// Symbols yes/no at least
		fieldsContainer = new JPanel();
		JScrollPane sp = new JScrollPane(fieldsContainer);
		sp.setPreferredSize(new Dimension(0, 0));
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.add(sp, BorderLayout.PAGE_END);

		clear();
	}

	private String formatDate(Object d) {
		return DateFormat.getDateTimeInstance(DateFormat.DEFAULT,
				DateFormat.LONG).format((Date) d);
	}

	private void setValue(Access anAccess, String key, String value) throws IllegalArgumentException {
		// Set the value
		String safevalue = value + "";
		int col = anAccess.getFactory().getIndex(key);
		Object oldValue = anAccess.setValue(key, safevalue);
		if (oldValue != null
				&& !anAccess
						.getFactory()
						.serialize(
								anAccess.getFactory().getType(col).getName(),
								oldValue).equals(safevalue)) {
			application.saveStatus(anAccess.getTraceID(col), oldValue);

		}
	}

	// private void createGui() {
	// JPanel panel = new JPanel();
	//
	// FieldLayout fieldLayoutUtility = new FieldLayout(panel);
	// this.setLayout(new BorderLayout());
	// this.add(panel, BorderLayout.CENTER);
	//
	// // begin of Access
	// fieldLayoutUtility
	// .append(0, 1, new JLabel(application.getLocalization()
	// .getString(AccessFactory.CATEGORY)), category);
	// fieldLayoutUtility.append(0, 1, new JLabel(application
	// .getLocalization().getString(AccessFactory.ACCESS)), access,
	// null);
	// fieldLayoutUtility.pair(0, 1, new JLabel(application.getLocalization()
	// .getString(AccessFactory.STATUS)), status, null);
	// // start of Description box
	// JScrollPane scrollingText = new JScrollPane(text,
	// ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
	// ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	// fieldLayoutUtility.append(0, 2, new JLabel(application
	// .getLocalization().getString(AccessFactory.TEXT)),
	// scrollingText, null);
	// // end of Description box
	// fieldLayoutUtility.append(0, 1, new JLabel(application
	// .getLocalization().getString(AccessFactory.URL)), url, null);
	// fieldLayoutUtility.pair(0, 1, new JLabel(application.getLocalization()
	// .getString(AccessFactory.USER)), user, null);
	// fieldLayoutUtility.append(0, 1, new JLabel(application
	// .getLocalization().getString(AccessFactory.RATING)), important,
	// null);
	// fieldLayoutUtility.pair(0, 1, new JLabel(application.getLocalization()
	// .getString(AccessFactory.STATISTICS)), statistics, new JLabel(
	// application.getLocalization().getString("clicks")));
	//
	// fieldLayoutUtility.append(0, 1, new JLabel(application
	// .getLocalization().getString(AccessFactory.CREATED_ON)),
	// created_on, null);
	// fieldLayoutUtility.pair(0, 1, new JLabel(application.getLocalization()
	// .getString(AccessFactory.CHANGED_ON)), changed_on, null);
	// fieldLayoutUtility.append(0, 1, new JLabel(application
	// .getLocalization().getString(AccessFactory.VALIDITY)),
	// validity,
	// new JLabel(application.getLocalization().getString("days")));
	// fieldLayoutUtility.pair(0, 1, new JLabel(application.getLocalization()
	// .getString(AccessFactory.EXPIRE_ON)), expire_on, null);
	// fieldLayoutUtility.append(0, 1, new JLabel(application
	// .getLocalization().getString(AccessFactory.EXPIRE)), expire,
	// null);
	// // end of Access
	//
	// // begin of password settings f Len, char, num, sym
	// fieldLayoutUtility.append(0, 1, new JLabel(application
	// .getLocalization().getString(AccessFactory.GENERATE)),
	// generate, null);
	//
	// // Length from to
	// fieldLayoutUtility.append(0, 1, new JLabel(application
	// .getLocalization().getString(AccessFactory.PWD_LENGTH)),
	// pwd_length,
	// new JLabel(application.getLocalization().getString("digits")));
	//
	// fieldLayoutUtility
	// .append(0, 1, new JLabel(application.getLocalization()
	// .getString(AccessFactory.CHARS)), chars, null);
	//
	// // Numbers yes/no at least
	// fieldLayoutUtility.append(0, 1, new JLabel(application
	// .getLocalization().getString(AccessFactory.NUMBERS)), numbers,
	// null);
	//
	// // Symbols yes/no at least
	// fieldLayoutUtility.append(0, 1, new JLabel(application
	// .getLocalization().getString(AccessFactory.SYMBOLS)), symbols,
	// null);
	//
	// clear();
	// }

	// private void displayMoreFields(Access a) {
	// GridBagLayout gbl = new GridBagLayout();
	// GridBagConstraints gbc = new GridBagConstraints();
	// fieldsContainer.setLayout(gbl);
	//
	// int size = a.getFactory().getSize();
	//
	// for (int i = 0; i < size; i++) {
	// // add a field
	// // get the editor from the class
	// if (a.getFactory().getType(i).equals(String.class)) {
	// GridBagLayoutUtility.newLine(gbc);
	// GridBagLayoutUtility.add(
	// new JLabel(application.getLocalization().getString(
	// a.getFactory().getKey(i) + "")),
	// new JTextField(a.getValue(a.getFactory().getKey(i))
	// + ""), fieldsContainer, gbc);
	// }
	// if (a.getFactory().getType(i).equals(Boolean.class)) {
	// GridBagLayoutUtility.newLine(gbc);
	// GridBagLayoutUtility.add(
	// new JLabel(application.getLocalization().getString(
	// a.getFactory().getKey(i) + "")), new JCheckBox(
	// a.getValue(a.getFactory().getKey(i)) + ""),
	// fieldsContainer, gbc);
	// }
	// if (a.getFactory().getType(i).equals(Integer.class)) {
	// GridBagLayoutUtility.newLine(gbc);
	// GridBagLayoutUtility.add(
	// new JLabel(application.getLocalization().getString(
	// a.getFactory().getKey(i) + "")),
	// new JSpinner(), fieldsContainer, gbc);
	// }
	// if (a.getFactory().getType(i).equals(Date.class)) {
	// GridBagLayoutUtility.newLine(gbc);
	// GridBagLayoutUtility.add(
	// new JLabel(application.getLocalization().getString(
	// a.getFactory().getKey(i) + "")),
	// new JTextField(a.getValue(a.getFactory().getKey(i))
	// + ""), fieldsContainer, gbc);
	// }
	// if (a.getFactory().getType(i).equals(StringBuffer.class)) {
	// GridBagLayoutUtility.newLine(gbc);
	// GridBagLayoutUtility.add(
	// new JLabel(application.getLocalization().getString(
	// a.getFactory().getKey(i) + "")),
	// new JTextField(a.getValue(a.getFactory().getKey(i))
	// + ""), fieldsContainer, gbc);
	// }
	//
	// }
	// // appendMore = false;
	// }

	// private void initMoreFields(Access a) {
	// for (int i = 0; i < a.getFactory().getSize(); i++) {
	// // add a field
	// if (isEditingEnabled(a.getFactory().getKey(i))) {
	//
	// }
	//
	// }
	// }

	// private boolean isEditingEnabled(Object key) {
	// boolean result = (//
	// key.equals(AccessFactory.PASSWORD_TYPE)//
	// || key.equals(AccessFactory.SALT)//
	// || key.equals(AccessFactory.PASSWORD)//
	// || key.equals(AccessFactory.VALIDITY)//
	// || key.equals(AccessFactory.PWD_LENGTH)//
	// // || key.equals(AccessFactory.ACCESS)//
	// // || key.equals(AccessFactory.ACCESS)//
	// // || key.equals(AccessFactory.ACCESS)//
	// // || key.equals(AccessFactory.ACCESS)//
	// // || key.equals(AccessFactory.ACCESS)//
	// // || key.equals(AccessFactory.ACCESS)//
	// // || key.equals(AccessFactory.ACCESS)//
	// // || key.equals(AccessFactory.ACCESS)//
	// // || key.equals(AccessFactory.ACCESS)//
	// );
	// return result;
	// }

	/**
	 * Return true when changed.
	 * 
	 * @return the changed.
	 */
	public boolean isChanged() {
		return changed;
	}

	// public void set(ArrayList<Object> a) {}

	/**
	 * Clear fields.
	 */
	public void clear() {
		for (String key : inputFieldsMap.keySet()) {
			JComponent c = inputFieldsMap.get(key);
			if (c instanceof JTextComponent) {
				((JTextComponent) c).setText("");
			} else if (c instanceof JSpinner) {
				((JSpinner) c).setValue(0);
			} else if (c instanceof AbstractButton) {
				((AbstractButton) c).setSelected(false);
			} else if (c instanceof FiveStarsCombo) {
				((FiveStarsCombo) c).setSelectedItem(0);
			} else {
			}
		}
		setChanged(changed);
		// for (JComponent c : inputFields) {
		// if (c instanceof JTextComponent) {
		// ((JTextComponent) c).setText("");
		// } else if (c instanceof JSpinner) {
		// // ((JSpinner) c).setValue(0);
		// } else if (c instanceof AbstractButton) {
		// ((AbstractButton) c).setSelected(false);
		// } else if (c instanceof FiveStarsCombo) {
		// ((FiveStarsCombo) c).setSelectedItem(0);
		// }
		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean b) {
		for (String key : inputFieldsMap.keySet()) {
			JComponent c = inputFieldsMap.get(key);
			if (c instanceof JTextComponent) {
				((JTextComponent) c).setEnabled(b);
			} else if (c instanceof JSpinner) {
				((JSpinner) c).setEnabled(b);
			} else if (c instanceof AbstractButton) {
				((AbstractButton) c).setEnabled(b);
			} else if (c == null) {
				// nothing
			} else {
				c.setEnabled(b);
			}
		}
		// for (JComponent c : inputFields) {
		// if (c instanceof JTextComponent) {
		// ((JTextComponent) c).setEnabled(b);
		// } else if (c instanceof JSpinner) {
		// ((JSpinner) c).setEnabled(b);
		// } else if (c instanceof AbstractButton) {
		// ((AbstractButton) c).setEnabled(b);
		// } else {
		// c.setEnabled(b);
		// }
		// }
	}

	@Override
	public void init(Access a) {
		/*
		 * Set the current access. Panel fields are updated from the access. If
		 * the input object is null a new standard access is provided.
		 */
		if (a == null) {
			this.internalAccess = a;
			clear();
		} else {
			this.internalAccess = a;
			category.setText(a.getValue(AccessFactory.CATEGORY) + "");
			access.setText(a.getValue(AccessFactory.ACCESS) + "");
			important.setSelectedItem(((RankStarValue) a
					.getValue(AccessFactory.RATING)).getLevel());
			url.setText(a.getValue(AccessFactory.URL) + "");
			statistics.setText(a.getValue(AccessFactory.STATISTICS) + "");
			user.setText(a.getValue(AccessFactory.USER) + "");

			created_on
					.setText(formatDate(a.getValue(AccessFactory.CREATED_ON)));

			changed_on
					.setText(formatDate(a.getValue(AccessFactory.CHANGED_ON)));

			expire_on.setText(formatDate(a.getValue(AccessFactory.EXPIRE_ON)));

			text.setText(a.getValue(AccessFactory.TEXT) + "");
			expire.setSelected((Boolean) (a.getValue(AccessFactory.EXPIRE)));
			generate.setSelected((Boolean) (a.getValue(AccessFactory.GENERATE)));
			pwd_length.setText(a.getValue(AccessFactory.PWD_LENGTH) + "");
			chars.setSelected((Boolean) (a.getValue(AccessFactory.CHARS)));
			numbers.setSelected((Boolean) (a.getValue(AccessFactory.NUMBERS)));
			symbols.setSelected((Boolean) (a.getValue(AccessFactory.SYMBOLS)));
			status.setText(a.getValue(AccessFactory.STATUS) + "");
			validity.setText(a.getValue(AccessFactory.VALIDITY) + "");
			password_type.setText(a.getValue(AccessFactory.PASSWORD_TYPE) + "");
			salt.setText(a.getValue(AccessFactory.SALT) + "");
			password.setText(a.getValue(AccessFactory.PASSWORD) + "");
		}
		setChanged(false);
		//
		setEnabled(application.isEditing());
		// ------------- //
		// if (appendMore) {
		// displayMoreFields(a);
		// }
		// initMoreFields(a);
	}

	@Override
	public void update(Access row) throws IllegalArgumentException {
		row = internalAccess;
		setValue(internalAccess, AccessFactory.ACCESS, access.getText());
		((RankStarValue) internalAccess.get(AccessFactory.RATING).getValue())
				.setLevel(important.getSelectedIndex());
		setValue(internalAccess, AccessFactory.URL, url.getText());
		setValue(internalAccess, AccessFactory.USER, user.getText());
		setValue(internalAccess, AccessFactory.TEXT, text.getText());
		setValue(internalAccess, AccessFactory.EXPIRE, expire.isSelected() + "");
		setValue(internalAccess, AccessFactory.CHARS, chars.isSelected() + "");
		setValue(internalAccess, AccessFactory.NUMBERS, numbers.isSelected()
				+ "");
		setValue(internalAccess, AccessFactory.SYMBOLS, symbols.isSelected()
				+ "");
		// setValue(internalAccess, AccessFactory.CHANGED_ON, new
		// Date().getTime()
		// + "");
		internalAccess.get(AccessFactory.CHANGED_ON).setObject(new Date());
		changed_on.setText(internalAccess.get(AccessFactory.CHANGED_ON)
				.getValue() + "");

		application.notifyChange();

		setChanged(true);
	}

	@Override
	public String getLabel() {
		return null;
	}

	/**
	 * Listen to the fields changes.
	 * 
	 */
	public class ChangeInputListener implements DocumentListener,
			ActionListener, ChangeListener, ItemListener {

		@Override
		public void changedUpdate(DocumentEvent arg0) {
			// System.out.println("Input from text change");
			setChanged(true);
		}

		@Override
		public void insertUpdate(DocumentEvent arg0) {
			// System.out.println("Input from text update");
			setChanged(true);
		}

		@Override
		public void removeUpdate(DocumentEvent arg0) {
			// System.out.println("Input from text remove");
			setChanged(true);
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// System.out.println("Input from spinner");
			setChanged(true);
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			// System.out.println("Input from check box at event: " + e);
			setChanged(true);
		}

		@Override
		public void itemStateChanged(ItemEvent arg0) {
			setChanged(true);
		}

	}

	public static void main(String[] args) {
		// Schedule a job for the event dispatch thread
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				new AccessEditPanel(new Password());

				// Access a = AccessFactory.getInstance().newRow();
				// a.set(AccessFactory.ACCESS, new StyleObject("access", null));
				// System.out.println(a);
				// // demo.
			}
		});
	}
}
