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

import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import it.tnnfnc.password.domain.AccessFactory;
import it.tnnfnc.security.EncryptionPreferences;
import it.tnnfnc.security.SecurityProvider;
import it.tnnfnc.table.TableProperties;

public class PasswordConstants {

	static final String[][] classnames = {
			// {"foolname", "className"},
			{ "RangedValue", "it.tnnfnc.table.cell.RangedValue" }, //
			{ "ButtonValue", "it.tnnfnc.table.cell.ButtonValue" }, //
			{ "RankStarValue", "it.tnnfnc.table.cell.RankStarValue" },//

	};

	// Application Settings
	/**
	 * Passprase timeout.
	 */
	public static final String PREF_TIMEOUT = "passphrase.timeout";
	/**
	 * Auto saving time interval.
	 */
	public static final String PREF_AUTOSAVE = "autosaving";
	/**
	 * Changes saved in the history.
	 */
	public static final String PREF_HISTORY = "history.lenght";
	/**
	 * Password output time.
	 */
	public static final String PREF_DISPLAY_TIME = "password.displayTime";
	/**
	 * Password output type: display, copy, display and copy.
	 */
	public static final String PREF_OUTPUT = "password.output";
	/**
	 * Password validity.
	 */
	public static final String PREF_VALIDITY = "password.validity";
	/**
	 * Warning period before password expires.
	 */
	public static final String PREF_EXPIRY_WARNING = "password.expiryWarning";

	/**
	 * Session security check.
	 */
	public static final String PREF_CHECK = "check"; //
	/**
	 * Unit of time.
	 */
	public static final int FRAMERATE = 100;

	/**
	 * Display password option: display and copy to clip board.
	 */
	public static final String OPT_DISPLAYCOPY = "DC";
	/**
	 * Display password option: copy to clip board only.
	 */
	public static final String OPT_COPY = "C";
	/**
	 * Display password option: display only.
	 */
	public static final String OPT_DISPLAY = "D";
	/**
	 * Program license.
	 */
	public static final String LICENSE = "LICENSE";

	/**
	 * Automatic save option: never.
	 */
	public static final String AUTOSAVE_NEVER = "never";
	/**
	 * Automatic save option: always at every change.
	 */
	public static final String AUTOSAVE_ALWAYS = "always";
	/**
	 * Automatic save option: scheduled every two minutes.
	 */
	public static final String AUTOSAVE_SCHEDULED = "scheduled";
	/**
	 * Scheduled auto-saving value in millisecs.
	 */
	public static final long SAVE_SCHEDULED_TIME = 120000L;

	// Commands
	/**
	 * Command change password.
	 */
	public static final String CHANGE_PASSWORD = "CHANGE_PASSWORD";

	// Constants
	/**
	 * Thread name for password timing.
	 */
	public static final String PASSWORD_THREAD = "PASSWORD";
	/**
	 * Thread name for auto-saving timing.
	 */
	public static final String AUTOSAVE_THREAD = "AUTOSAVE";
	/**
	 * Thread name for pass phrase timing.
	 */
	public static final String PASSPHRASE_THREAD = "PASSPHRASE";

	private static ListResourceBundle language;

	public static Properties getDefaultProperties() {
		Properties properties = new Properties();

		// Application Settings
		properties.setProperty(PREF_TIMEOUT, "10"); // min.
		properties.setProperty(PREF_HISTORY, "10"); // items
		properties.setProperty(PREF_DISPLAY_TIME, "15"); // sec
		properties.setProperty(PREF_OUTPUT, OPT_DISPLAYCOPY);
		properties.setProperty(PREF_VALIDITY, "180"); // days
		properties.setProperty(PREF_EXPIRY_WARNING, "15");// days
		properties.setProperty(PREF_AUTOSAVE, AUTOSAVE_NEVER); // option
		// Security Settings
		properties.setProperty(EncryptionPreferences.PREF_SECURITYPROVIDER, SecurityProvider.class.getName()); // SecurityProvider
		properties.setProperty(EncryptionPreferences.PREF_HMAC, "HMAC/SHA-256"); //
		properties.setProperty(EncryptionPreferences.PREF_ITERATIONS, "1000"); //
		properties.setProperty(EncryptionPreferences.PREF_SALT, ""); //
		properties.setProperty(EncryptionPreferences.PREF_PBKDF, "PBKDF2"); //
		properties.setProperty(EncryptionPreferences.PREF_KEYSIZE, "256"); //
		properties.setProperty(EncryptionPreferences.PREF_CIPHER_ALGORITHM, "AES/CBC/PKCS5Padding"); //

		// Visible columns
		int p = 0;
		properties.setProperty(TableProperties.getColumnKey(getLanguage().getString(AccessFactory.CATEGORY)),
				p++ + ";20;true");// bit
		properties.setProperty(TableProperties.getColumnKey(getLanguage().getString(AccessFactory.RATING)),
				p++ + ";15;true");// bit
		properties.setProperty(TableProperties.getColumnKey(getLanguage().getString(AccessFactory.ACCESS)),
				p++ + ";20;true");// bit
		properties.setProperty(TableProperties.getColumnKey(getLanguage().getString(AccessFactory.URL)),
				p++ + ";20;true");// bit
		properties.setProperty(TableProperties.getColumnKey(getLanguage().getString(AccessFactory.USER)),
				p++ + ";20;true");// bit
		properties.setProperty(TableProperties.getColumnKey(getLanguage().getString(AccessFactory.EMAIL)),
				p++ + ";20;true");// bit
		properties.setProperty(TableProperties.getColumnKey(getLanguage().getString(AccessFactory.STATUS)),
				p++ + ";10;true");// bit
		properties.setProperty(TableProperties.getColumnKey(getLanguage().getString(AccessFactory.PASSWORD_DISPLAY)),
				p++ + ";30;true");// bit
		properties.setProperty(TableProperties.getColumnKey(getLanguage().getString(AccessFactory.CHANGE_BUTTON)),
				p++ + ";15;true");// bit
		properties.setProperty(TableProperties.getColumnKey(getLanguage().getString(AccessFactory.EXPIRE_ON)),
				p++ + ";15;true");// bit
		properties.setProperty(TableProperties.getColumnKey(getLanguage().getString(AccessFactory.GENERATE)),
				p++ + ";5;true");// bit

		return properties;
	}

	/**
	 * Get the language resources.
	 * 
	 * @return the language resources.
	 */
	public static ListResourceBundle getLanguage() {
		// Resource
		if (language == null)
			language = (ListResourceBundle) ResourceBundle.getBundle(PasswordBundle.class.getName());
		return language;
	}

	/**
	 * Set the language.
	 * 
	 * @param lang
	 *            the language resources.
	 */
	public static void setLocale(String lang) {
		Locale locale = new Locale(lang);
		if (language == null || language.getLocale() != locale)
			language = (ListResourceBundle) ResourceBundle.getBundle(PasswordBundle.class.getName(), locale);
	}

	/**
	 * Get the error format.
	 * 
	 * @return the error format.
	 */
	public static String getBlankFormat() {
		return "";
	}

	/**
	 * Get the warning format.
	 * 
	 * @return the error format.
	 */
	public static String getYellowFormat() {
		return "color:0,0,0;background-color:255,255,150;";
	}

	/**
	 * Get the warning format.
	 * 
	 * @return the error format.
	 */
	public static String getRedFormat() {
		return "color:0,0,0;background-color:255,150,120;";
	}

	/**
	 * Get the warning format.
	 * 
	 * @return the error format.
	 */
	public static String getBluFormat() {
		return "color:0,0,0;background-color:170,190,220;";
	}

	/**
	 * Get the warning format.
	 * 
	 * @return the error format.
	 */
	public static String getGreenFormat() {
		return "color:0,0,0;background-color:199,233,192;";
	}

	/**
	 * Get the warning format.
	 * 
	 * @return the error format.
	 */
	public static String getGrayFormat() {
		return // "font-family:Dialog;font-style:plain;font-size:12;" +
		"color:0,0,0;background-color:217,217,217;";
	}

	/**
	 * Return the class name for the custom type.
	 * 
	 * @param foolName
	 *            the name without packages.
	 * @return the class name.
	 */
	public static String getClassName(String foolName) {
		for (String[] strings : classnames) {
			if (strings[0].equals(foolName)) {
				return strings[1];
			}
		}
		return null;
	}

}
