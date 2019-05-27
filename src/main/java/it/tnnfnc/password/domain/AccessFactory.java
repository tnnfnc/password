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
package it.tnnfnc.password.domain;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;

import it.tnnfnc.apps.application.ui.style.StyleObject;
import it.tnnfnc.encoders.Base64;
import it.tnnfnc.password.PasswordConstants;
import it.tnnfnc.table.cell.ButtonValue;
import it.tnnfnc.table.cell.RangedValue;
import it.tnnfnc.table.cell.RankStarValue;
import it.tnnfnc.table.row.I_RowFactory;

//

/**
 * This class is responsible of creating access objects. Only one object
 * instance from this class can be created.
 * 
 * @author Franco Toninato
 * 
 */
public class AccessFactory implements I_RowFactory<Access>, Serializable {

	/**
	 * Category - String.
	 */
	public final static String CATEGORY = "category"; //
	/**
	 * Access name - String.
	 */
	public final static String ACCESS = "access"; //
	/**
	 * URL - String.
	 */
	public final static String URL = "URL"; //
	/**
	 * User name - String.
	 */
	public final static String USER = "user"; //
	/**
	 * E-mail - String.
	 */
	public final static String EMAIL = "email"; //
	/**
	 * Password - String.
	 */
	public final static String PASSWORD = "password"; //
	/**
	 * Button show password - JButton.
	 */
	public final static String PASSWORD_DISPLAY = "passwordButton"; //
	/**
	 * Button show password - JButton.
	 */
	public final static String CHANGE_BUTTON = "changeButton"; //
	/**
	 * Status - String.
	 */
	public final static String STATUS = "status"; //
	/**
	 * Created on - Date.
	 */
	public final static String CREATED_ON = "fromDate"; //
	/**
	 * Expire on - Date.
	 */
	public final static String EXPIRE_ON = "toDate"; //
	/**
	 * Changed on - Date.
	 */
	public final static String CHANGED_ON = "lastChange";
	/**
	 * Validity - int.
	 */
	public final static String VALIDITY = "validity"; //
	/**
	 * Length - int.
	 */
	public final static String PWD_LENGTH = "length"; //
	/**
	 * Description - String.
	 */
	public final static String TEXT = "text"; //
	/**
	 * Rating - int.
	 */
	public final static String RATING = "rating";
	/**
	 * Expire flag - boolean.
	 */
	public final static String EXPIRE = "expire"; //
	/**
	 * Generation - boolean.
	 */
	public final static String GENERATE = "autogeneration"; //
	/**
	 * Password type: generated from a rule, user defined with a rule.
	 */
	public final static String PASSWORD_TYPE = "passwordType"; //
	/**
	 * Generation - boolean.
	 */
	public final static String CHARS = "chars"; //
	/**
	 * Include chars - boolean.
	 */
	public final static String NUMBERS = "numbers"; //
	/**
	 * Include numbers - boolean.
	 */
	public final static String SYMBOLS = "symbols"; //
	/**
	 * Include symbols - boolean.
	 */
	public final static String SALT = "salt"; //
	/**
	 * Salt - String.
	 */
	public final static String STATISTICS = "Statistic";

	// Access status
	/**
	 * Access status: undefined.
	 */
	public final static String STATUS_UNDEFINED = "undefined";
	/**
	 * Access status: active.
	 */
	public final static String STATUS_ACTIVE = "active"; // everything ok
	/**
	 * Access status: expired.
	 */
	public final static String STATUS_EXPIRED = "expired"; // expired
	/**
	 * Access status: changed.
	 */
	public final static String STATUS_CHANGED = "changed"; // recently changed,
	/**
	 * Access status: warning.
	 */
	public static final String STATUS_WARNING = "warning"; // is expiring soon

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Index values greater than internal are custom fields.
	 */
	private final int internal;

	/**
	 * Name, type pairs.
	 * 
	 * @author Franco Toninato
	 * 
	 */
	private static class Key {
		public Key(String name, Class<?> type, String l, String deflt) {
			this.key = name;
			this.type = type;
			this.label = l;
			this.deflt = deflt;
		}

		// boolean edit;
		// boolean visible;
		String key;
		String label;
		Class<?> type;
		String deflt;
	};

	/**
	 * Field list.
	 */
	private Key fieldList[] = new Key[0];
	private final HashMap<String, Integer> keyMap = new HashMap<String, Integer>();

	/**
	 * This class cannot be instantiated.
	 */
	private AccessFactory() {
		addField(CATEGORY, String.class, getLabel(CATEGORY), "");
		addField(ACCESS, String.class, getLabel(ACCESS), "");
		addField(URL, String.class, getLabel(URL), "");
		addField(USER, String.class, getLabel(USER), "");
		addField(EMAIL, String.class, getLabel(EMAIL), "");
		addField(PASSWORD_DISPLAY, RangedValue.class, getLabel(PASSWORD_DISPLAY), "");//
		addField(CHANGE_BUTTON, ButtonValue.class, getLabel(CHANGE_BUTTON), "false");//
		addField(EXPIRE_ON, Date.class, getLabel(EXPIRE_ON), "");
		addField(RATING, RankStarValue.class, getLabel(RATING), "");//
		addField(TEXT, StringBuffer.class, getLabel(TEXT), "");
		addField(STATUS, String.class, getLabel(STATUS), "");
		addField(PASSWORD, String.class, getLabel(PASSWORD), "");
		addField(PWD_LENGTH, Integer.class, getLabel(PWD_LENGTH), "8");
		addField(CREATED_ON, Date.class, getLabel(CREATED_ON), "");
		addField(CHANGED_ON, Date.class, getLabel(CHANGED_ON), "");
		addField(VALIDITY, Integer.class, getLabel(VALIDITY), "180");
		addField(STATISTICS, Integer.class, getLabel(STATISTICS), "");
		addField(EXPIRE, Boolean.class, getLabel(EXPIRE), "true");
		addField(GENERATE, Boolean.class, getLabel(GENERATE), "true");
		addField(PASSWORD_TYPE, String.class, getLabel(PASSWORD_TYPE), "");
		addField(CHARS, Boolean.class, getLabel(CHARS), "false");
		addField(NUMBERS, Boolean.class, getLabel(NUMBERS), "false");
		addField(SYMBOLS, Boolean.class, getLabel(SYMBOLS), "false");
		addField(SALT, String.class, getLabel(SALT), "");
		internal = getSize();
	}

	/**
	 * Create a new instance of this object. Only one instance can exist at one
	 * time.
	 * 
	 * @return a new instance of this class.
	 */
	public static AccessFactory getInstance() {
		return new AccessFactory();
	}

	/**
	 * Add a new field. Existing fields are overwritten.
	 * 
	 * @param key
	 *            the field key name.
	 * @param type
	 *            the field type.
	 * @param label
	 *            the field label.
	 * @param deflt
	 *            the field default value.
	 */
	public int addField(Object key, Class<?> type, String label, String deflt) {
		// look for the key: exists
		int f_id = getIndex(key);
		// look for the key: does not exist
		if (f_id == -1) {
			f_id = fieldList.length;
			Key keys[] = new Key[getSize() + 1];
			System.arraycopy(fieldList, 0, keys, 0, getSize());
			fieldList = keys;
		}
		fieldList[f_id] = new Key(key.toString(), type, label, deflt);
		keyMap.put(key.toString(), f_id);
		return f_id;
	}

	/**
	 * Get the field label name. It returns the label set by the user, otherwise
	 * it looks for a resources entry. If nothing was found the same field key
	 * is returned as its label.
	 * 
	 * @param key
	 *            the field key.
	 * @return the string with the label name.
	 */
	public String getLabel(Object key) {
		String l = null;
		if (getIndex(key) > -1) {
			l = fieldList[getIndex(key)].label;
		}
		try {
			l = l == null ? PasswordConstants.getLanguage().getString(key.toString()) : l;
		} catch (Exception e) {
			l = key.toString();
		}
		return l;
	}

	/**
	 * Get the field key from its position.
	 * 
	 * @param i
	 *            the field position.
	 * @return the field key.
	 * 
	 */
	public Object getKey(int i) {
		return i < getSize() ? fieldList[i].key : null;
	}

	/**
	 * Get the field position from its key.
	 * 
	 * @param key
	 *            field key.
	 * 
	 * @return the field position.
	 */
	public int getIndex(Object key) {
		try {
			return keyMap.get(key.toString());
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * Get the field default value.
	 * 
	 * @param key
	 *            the field key name.
	 * @return the field default value.
	 */
	public String getDefault(Object key) {
		return getIndex(key) == -1 ? null : fieldList[getIndex(key)].deflt;
	}

	/**
	 * Get the field expected object type from its position.
	 * 
	 * @param i
	 *            the field position.
	 * @return the expected object type.
	 * 
	 */
	public Class<?> getType(int i) {
		return i < getSize() ? fieldList[i].type : null;
	}

	/**
	 * Get the number of fields.
	 * 
	 * @return the number of fields.
	 */
	public int getSize() {
		try {
			return fieldList.length;
		} catch (NullPointerException e) {
			return 0;
		}
	}

	/**
	 * Get if the field was user defined.
	 * 
	 * @param i
	 *            the field position.
	 * @return true if the field is a system field, false otherwise.
	 * 
	 */
	public boolean isInternal(int i) {
		return i < internal;
	}

	/**
	 * Converts a string into an object.
	 * 
	 * @param type
	 *            the object class.
	 * @param value
	 *            the value.
	 * @return the object.
	 */
	public Object deSerialize(String type, String value) {
		Object o = new Object();
		if (type.equals(String.class.getName())) {
			value = value == null ? "" : value;
			o = new String(value);
		} else if (type.equals(Integer.class.getName())) {
			int i = 0;
			try {
				i = Integer.parseInt(value);
			} catch (NumberFormatException e) {
				i = 0;
			}
			o = Integer.valueOf(i);
		} else if (type.equals(Boolean.class.getName())) {
			o = Boolean.valueOf(value.trim());
			// o = new Boolean(value);
		} else if (type.equals(URL.class.getName())) {
			value = (value == null || value == "") ? "http://" : value;
			try {
				o = new URL(value);
			} catch (MalformedURLException e) {
				try {
					o = new URL("http://");
				} catch (MalformedURLException e1) {
				}
			}
		} else if (type.equals(StringBuffer.class.getName())) {
			value = value == null ? "" : value;
			o = new StringBuffer(value);
		} else if (type.equals(char[].class.getName())) {
			value = value == null ? "" : value;
			o = new String(value).toCharArray();
		} else if (type.equals(Date.class.getName())) {
			Date d = new Date();
			try {
				long l = Long.parseLong(value);
				d.setTime(l);
				o = d;
			} catch (NumberFormatException e) {
				o = d;
			}
		}
		// /
		//
		// else if (type.equals(RangedValue.class.getName())) {
		else if (type.contains(RangedValue.class.getSimpleName())) {
			// System.out.println(RangedValue.class.getSimpleName());
			value = value == null ? "0" : value;
			int i = 0;
			try {
				i = Integer.parseInt(value);
			} catch (NumberFormatException e) {
				i = 0;
			}
			RangedValue rv = new RangedValue();
			rv.setMax(10);
			rv.setMin(0);
			rv.setStep(1);
			rv.setGauge(i);
			rv.setValue(null);
			o = rv;
		}
		//
		else if (type.contains(RankStarValue.class.getSimpleName())) {
			// else if (type.equals(RankStarValue.class.getName())) {
			value = value == null ? "" : value;
			RankStarValue fsv = new RankStarValue();
			try {
				int l = Integer.parseInt(value);
				if (l <= RankStarValue.SCALE) {
					fsv.setLevel(l);
				}
			} catch (NumberFormatException e) {
				fsv.setLevel(0);
			}
			o = fsv;
		}
		// /
		else if (type.contains(ButtonValue.class.getSimpleName())) {
			boolean b = (value != null && value == "true") ? true : false;
			ButtonValue bv = new ButtonValue();
			bv.setSelected(b);
			o = bv;
		} else {
			throw new IllegalArgumentException("Unexpected format for: " + value);
		}
		// /
		//

		return o;
	}

	/**
	 * Converts an object into a string where the value is the string
	 * representation of the object.
	 * 
	 * @param type
	 *            the object class.
	 * @param obj
	 *            the object.
	 * 
	 * @return a string representation of the object.
	 */
	public String serialize(String type, Object obj) throws IllegalArgumentException {
		if (type == null || type.length() == 0) {
			return null;
		} else if (obj == null) {
			return null;
		} else if (type.equals(Date.class.getName())) {
			Date d = (Date) obj;
			return Long.toString(d.getTime());
		} else if (type.equals(Boolean.class.getName())) {
			// System.out.println(this.getClass().getName() + " toString() " +
			// obj );
			// System.out.println(this.getClass().getName() + " ((Boolean)
			// obj).toString() " + ((Boolean) obj).toString() );
			return ((Boolean) obj).toString();
		} else if (type.equals(ButtonValue.class.getName())) {
			return Boolean.toString(((ButtonValue) obj).isSelected());
		} else if (type.equals(URL.class.getName())) {
			return ((URL) obj).toExternalForm();
		} else if (type.equals(RangedValue.class.getName())) {
			RangedValue rv = ((RangedValue) obj);
			String gauge = rv.getGauge() + "";
			return gauge;
		} else if (type.equals(RankStarValue.class.getName())) {
			RankStarValue fs = (RankStarValue) obj;
			return fs.getLevel() + "";
		}
		// Byte, base 64 conversion
		else if (type.equals(byte[].class.getName())) {
			byte[] b = (byte[]) obj;
			Base64.decode(b);
			return new String(b);
		} else if (type.equals(Integer.class.getName())) {
			return obj.toString();
		}
		// Other types
		else {
			return obj.toString();
		}
	}

	/**
	 * Set the label for an existing field.
	 * 
	 * @param key
	 *            the field key name.
	 * @param l
	 *            the field label.
	 */
	public void setLabel(Object key, String l) {
		fieldList[getIndex(key)].label = l;
	}

	/**
	 * Set the field default value.
	 * 
	 * @param key
	 *            the field key name.
	 * @param deflt
	 *            the field default value.
	 */
	public void setDefault(Object key, String deflt) {
		if (getIndex(key) > -1) {
			fieldList[getIndex(key)].deflt = deflt;
		}
	}

	/**
	 * Get the password type.
	 * 
	 * @param a
	 *            the access.
	 * @return the algorithm.
	 */
	public static String getPasswordType(Access a) {
		if (a != null) {
			String pt = a.getValue(AccessFactory.PASSWORD_TYPE) + "";
			String[] rules_array = pt.split("\\.");
			return rules_array[0];
		}
		return null;
	}

	/**
	 * Get the password rules.
	 * 
	 * @param a
	 *            the access.
	 * @return an array where elements are the applied rules.
	 */
	public static String[] getPasswordRules(Access a) {
		if (a != null) {
			String[] r = (a.getValue(AccessFactory.PASSWORD_TYPE) + "").split("\\.");

			String[] rules = new String[r.length > 1 ? r.length - 1 : 0];

			for (int i = 1; i < r.length; i++) {
				rules[i - 1] = r[i];
			}

			return rules;
		}
		return new String[0];
	}

	/**
	 * Create a new access with defaults.
	 * 
	 * @return a new access object.
	 */
	@Override
	public Access newRow() {
		Access a = new Access(this);
		for (int j = 0; j < getSize(); j++) {
			a.set(j, new StyleObject(deSerialize(getType(j).getName(), fieldList[j].deflt), ""));
		}
		return a;
	}

	@Override
	public String toString() {
		StringBuffer b = new StringBuffer();
		for (int i = 0; i < fieldList.length; i++) {
			b.append(fieldList[i].key);
			b.append(" type=");
			b.append(fieldList[i].type.getName());
			b.append(" Label=");
			b.append(fieldList[i].label);
			// System.out.println(fieldList[i].key + " type="
			// + fieldList[i].type.getName() + " Label="
			// + fieldList[i].label);
		}
		return null;
	}

}
