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

import it.tnnfnc.password.domain.AccessFactory;
import it.tnnfnc.security.EncryptionPreferences;
import it.tnnfnc.security.SecurityProvider;

public class PasswordBundle extends ListResourceBundle {
	// public static final String FIXED = "1.";

	@Override
	public Object[][] getContents() {
		return contents;
	}

	static final Object[][] contents = { { "", "" }, //
			// Access attribute names
			{ AccessFactory.ACCESS, "Access" }, //
			{ AccessFactory.CATEGORY, "Category" }, //
			{ AccessFactory.VALIDITY, "Validity" }, //
			{ AccessFactory.EXPIRE, "Expiration warning" }, //
			{ AccessFactory.CREATED_ON, "Created on" }, //
			{ AccessFactory.CHANGED_ON, "Last changed" }, //
			{ AccessFactory.PWD_LENGTH, "Length" }, //
			{ AccessFactory.PASSWORD, "User password" }, //
			{ AccessFactory.RATING, "Rating" }, //
			{ AccessFactory.STATISTICS, "Hits" }, //
			{ AccessFactory.SALT, "Salt" }, //
			{ AccessFactory.TEXT, "Description" }, //
			{ AccessFactory.EXPIRE_ON, "Expires on" }, //
			{ AccessFactory.URL, "url" }, //
			{ AccessFactory.USER, "User Name" }, //
			{ AccessFactory.EMAIL, "Contact e-mail" }, //
			{ AccessFactory.GENERATE, "Generated" }, //
			{ AccessFactory.PASSWORD_TYPE, "Password type" }, //
			{ AccessFactory.CHARS, "Characters" }, //
			{ AccessFactory.NUMBERS, "Numbers" }, //
			{ AccessFactory.SYMBOLS, "Symbols" }, //
			{ AccessFactory.PASSWORD_DISPLAY, "Password" }, //
			{ AccessFactory.CHANGE_BUTTON, "Change" }, //
			{ AccessFactory.STATUS, "Status" }, //
			{ AccessFactory.STATUS_UNDEFINED, "undefined" }, //
			{ AccessFactory.STATUS_ACTIVE, "active" }, //
			{ AccessFactory.STATUS_CHANGED, "changed" }, //
			{ AccessFactory.STATUS_EXPIRED, "expired" }, //
			{ AccessFactory.STATUS_WARNING, "expiring" }, //
			// Security settings
			{ SecurityProvider.TYPE_BLOCKCIPHER, "Encryption Algorithm" }, //
			{ SecurityProvider.TYPE_BLOCKCIPHER_MODE, "Chain Encryption Mode" }, //
			{ SecurityProvider.TYPE_BLOCK_PADDING, "Padding schema" }, //
			{ EncryptionPreferences.PREF_ITERATIONS, "Encryption Key Iterations" }, //
			{ EncryptionPreferences.PREF_HASH, "Hash Algorithm" }, //
			{ EncryptionPreferences.PREF_SALT, "Encryption Key Salt" }, //
			{ EncryptionPreferences.PREF_KEYSIZE, "Encryption Key Strength" }, //
			{ EncryptionPreferences.PREF_ITERATIONS, "Password Iterations" }, //
			{ EncryptionPreferences.PREF_PBKDF, "Password Derivation Function" }, //
			{ EncryptionPreferences.PREF_SALT, "Password Salt" }, //
			{ EncryptionPreferences.PREF_KEYSIZE, "Password Strength" }, //
			{ EncryptionPreferences.PREF_SECURITYPROVIDER, "Services Provider" }, //
			// General settings
			{ PasswordConstants.PREF_AUTOSAVE, "Auto-save" }, //
			{ PasswordConstants.PREF_TIMEOUT, "Timeout" }, //
			{ PasswordConstants.PREF_DISPLAY_TIME, "Password display time" }, //
			{ PasswordConstants.PREF_OUTPUT, "Password output" }, //
			{ PasswordConstants.PREF_EXPIRY_WARNING, "Password expiry warning" }, //
			{ PasswordConstants.PREF_VALIDITY, "Password default validity" }, //
			{ PasswordConstants.PREF_HISTORY, "History lenght" },
			{ PasswordConstants.OPT_DISPLAYCOPY + "", "Display & Copy" }, //
			{ PasswordConstants.OPT_COPY + "", "Copy" }, //
			{ PasswordConstants.OPT_DISPLAY + "", "Display" }, //
			{ PasswordConstants.AUTOSAVE_NEVER, "Never" }, //
			{ PasswordConstants.AUTOSAVE_ALWAYS, "At every change" }, //
			{ PasswordConstants.AUTOSAVE_SCHEDULED, "Every two minutes" }, //
			{ PasswordConstants.LICENSE, "License" }, //

			//
			{ "Display & Copy", "Display & Copy" }, //
			{ "Copy", "Copy" }, //
			{ "Display", "Display" }, //
			{ "application", "Password" }, //
			{ "label name", "Label" }, //
			{ "history", "History" }, //
			{ "Table", "Table" }, //
			{ "on", "Pass phrase ON" }, //
			{ "off", "Pass phrase OFF" }, //
			{ "days", "days" }, //
			{ "min", "minutes" }, //
			{ "sec", "seconds" }, //
			{ "months", "months" }, //
			{ "bits", "bits" }, //
			{ "bytes", "bytes" }, //
			{ "cycles", "cycles" }, //
			{ "items", "Items" }, //
			{ "internal", "internal" }, //
			{ "random", "Generate" }, //
			{ "records", "records" }, //
			{ "digits", "digits" }, //
			{ "clicks", "clicks" }, //
			{ "New category", "New category" }, //
			{ "Copy", "Copy" }, //
			{ "up", "Up" }, //
			{ "down", "Down" }, //
			{ "fit", "Fit" }, //
			{ "style", "Edit Style" }, //
			{ "edit", "Edit" }, //
			{ "delete", "Delete" }, //
			{ "Refresh", "Refresh" }, //
			{ "OK", "OK" }, //
			{ "Cancel", "Cancel" }, //
			{ "Stop", "Stop" }, //
			{ "Change", "Change" }, //
			{ "Password Strength", "Password Strength" }, //
			{ "Close", "Close" }, //
			{ "Confirm", "Confirm" }, //
			{ "search", "Search" }, //
			{ "New", "New" }, //
			{ "Edit category", "Edit category" }, //
			{ "Edit row", "Edit row" }, //
			{ "Edit on", "Edit on" }, //
			{ "Edit off", "Edit off" }, //
			{ "Save as", "Save as" }, //
			{ "Save", "Save" }, //
			{ "Unlock", "Unlock" }, //
			{ "Lock", "Locked" }, //
			{ "Exit", "Exit" }, //
			{ "Passphrase", "Passphrase" }, //
			{ "Settings", "Settings" }, //
			{ "...", "..." }, //
			{ "Columns", "Columns" }, //
			{ "Security", "Security" }, //
			{ "Password settings", "Password settings" }, //
			{ "Cipher settings", "Cipher settings" }, //
			{ "Change access password", "Change access password" }, //
			{ "Change access", "Change access" }, //
			{ "User defined", "User defined" }, //
			{ "Old password", "Password" }, //
			{ "New password", "New password" }, //
			{ "Resource", "Resource" }, //
			{ "backup local path", "Browse a Backup Local Path" }, //
			{ "Password file", "Access password file" }, //
			{ "Manage the accesses", "Manage your accesses" }, //
			{ "Export", "Export to spreadsheet" }, // Export to sheet
			{ "Display", "Display" }, //
			{ "ImportDictionary", "Import Dictionary" }, //
			{ "Rule", "Password Rule" }, //
			{ "Add rule", "Add +..." }, //
			{ "Remove rule", "Remove -..." }, //
			{ "Document version", "Document version" }, //
			{ "Log records", "Log records" }, //
			{ "Time left", "Time left" }, //
			/*
			 * MESSAGES
			 */
			{ "Error", "Error: " }, //
			{ "Cipher exception", "Cipher exception" }, //
			{ "Wrong data length", "Wrong data length" }, //
			{ "Bad padding error", "Bad padding error" }, //
			{ "Wrong cipher block size", "Wrong cipher block size" }, //

			{ "Security Exception", "Security Exception" }, //
			{ "Wrong security parameters", "Wrong security parameters" }, //
			{ "Invalid key parameters", "Invalid key parameters" }, //
			{ "Invalid security state", "Invalid security state!" }, //
			{ "Please, get a new random salt!", "Please, get a new random salt!" }, //
			{ "Invalid passphrase!", "Invalid pass phrase attempt!" }, //
			{ "Invalid password", "Invalid password" }, //
			{ "Passphrase unsettled!", "Pass phrase unsettled!" }, //
			{ "Autosaving failure!", "Autosaving failure!" }, //
			{ "No password", "No password" }, //
			{ "No changes", "No changes" }, //
			{ "Click CHANGE to continue", "Click CHANGE to continue" }, //
			{ "Change security settings", "Change security settings" }, //
			{ "Click OK will change all passwords! Continue?", "Click OK will change all passwords! Continue?" }, // "Click
																													// OK
																													// will
																													// change
																													// all
																													// passwords!
																													// Continue?"
			{ "Passphrase is missing", "Pass phrase is missing" }, //
			{ "Enable edit mode", "Enable edit mode" }, //
			{ "Show password", "Show password" }, //
			{ "Change properties", "Change program properties" }, //
			{ "Change security", "Change security parameters" }, //
			{ "Creating access document", "Creating access document" }, //
			{ "Current document", "Current document" }, //
			{ "Close document", "Close document" }, //
			{ "Autosave document", "Autosave document" }, //
			{ "Set pass phrase", "Set pass phrase" }, //
			{ "Stop pass prase", "Stop pass prase" }, //
			{ "Close all", "Close all documents" }, //
			{ "Old version", "The document is an old version and it will be updated" }, //
			/*
			 * Tooltips
			 */
			{ "tt-New row", //
					"Add a new row at the selected position in the table or at the end" }, //
			{ "tt-Edit row", //
					"Edit the selected row" }, //
			{ "tt-delete row", //
					"Delete the selected row, is not possible to recover lost data" }, //
			{ "tt-row up", //
					"Move up the selected rows of one position" }, //
			{ "tt-row down", //
					"Move down the selected rows of one position" }, //
			{ "tt-history", //
					"Display the changes history of the selected table cell" }, //
			{ "tt-change style", //
					"Change the display format of the selected table cells" }, //
			{ "tt-search", //
					"Full text search in the table" }, //
			{ "tt-arrange columns", //
					"Display a dialog with all columns" }, //
			{ "tt-edit mode", //
					"Switch on or off the editing mode for the program" }, //
			{ "tt-start stop passphrase", //
					"Start and stop the active pass phrase" }, //
			{ "tt-access table", //
					"Press AltGr + left mouse click to copy a cell into clipboard" }, //
			{ "tt-export to sheet", //
					"Export the list to Spreadsheet" }, //
			{ "tt-Import Dictionary", //
					"Import a new Dictionary" }, //

			// END OF MATERIAL TO LOCALIZE
	};
}
