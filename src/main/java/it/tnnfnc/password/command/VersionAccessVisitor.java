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
package it.tnnfnc.password.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import it.tnnfnc.apps.application.ui.PopMessage;
import it.tnnfnc.encoders.IdentityEncoder;
import it.tnnfnc.encoders.LongIntegerEncoder;
import it.tnnfnc.password.Password;
import it.tnnfnc.password.PasswordDictionary;
import it.tnnfnc.password.document.AccessDocument;
import it.tnnfnc.password.domain.Access;
import it.tnnfnc.password.domain.AccessFactory;
import it.tnnfnc.password.domain.AccessTableModel;

/**
 * 
 *
 */
public class VersionAccessVisitor extends AccessVisitor {

	private Password program;

	/**
	 * Inspect the access.
	 * 
	 * @param access
	 *            the access.
	 */
	public void visitAccess(Access access) {
		super.visitAccess(access);
	}

	/**
	 * 
	 */
	public VersionAccessVisitor(Password program) {
		super();
		this.program = program;
	}

	/**
	 * Inspect the document and store the reference.
	 * 
	 * @param document
	 *            the document.
	 */
	public void visitDocument(AccessDocument document) {
		super.visitDocument(document);
		Properties p = document.getProperties();
		if (program.checkVersion(document.getVersion()) < 0) {
			String key = null;
			ArrayList<String> oldKeys = new ArrayList<String>();
			for (Iterator<Object> iterator = p.keySet().iterator(); iterator.hasNext();) {
				key = (String) iterator.next();
				if (key.contains("net.catode")) {
					oldKeys.add(key);
				}
			}

			for (String old : oldKeys) {
				p.remove(old);
			}
		}
	}

	/**
	 * Inspect the model and store the reference.
	 * 
	 * @param model
	 *            the model.
	 */
	public void visitModel(AccessTableModel model) {
		super.visitModel(model);

		if (document.getVersion().equals("0")) {
			PopMessage.displayWarning(program.getFrame(), program.getLocalization().getString("Old version"));
			fixVersion_0(model);
		}
	}

	@Override
	public void visit(Object o) {

	}

	private void fixVersion_0(AccessTableModel model) {
		// Inspect the model elements - local
		for (int i = 0; i < model.getRowCount(); i++) {
			Access a = model.getEntry(i);
			if ((a.getValue(AccessFactory.PASSWORD_TYPE) + "").length() == 0 && a.isPasswordDefined()) {
				if (a.isPasswordGenerated()) {
					a.get(AccessFactory.PASSWORD_TYPE).setObject(LongIntegerEncoder.algorithm);
				} else {
					a.get(AccessFactory.PASSWORD_TYPE).setObject(IdentityEncoder.algorithm);
				}
			}
			// Replace char sym num with new code
			String rule = a.getValue(AccessFactory.PASSWORD_TYPE) + "";
			if ((Boolean) a.getValue(AccessFactory.CHARS) == true) {
				rule = rule + "." + PasswordDictionary.Letters.name;
			}
			if ((Boolean) a.getValue(AccessFactory.SYMBOLS) == true) {
				rule = rule + "." + PasswordDictionary.Symbols.name;
			}
			if ((Boolean) a.getValue(AccessFactory.NUMBERS) == true) {
				rule = rule + "." + PasswordDictionary.Numbers.name;
				rule = rule + "." + PasswordDictionary.Numbers.name;
			}
			a.setValue(AccessFactory.PASSWORD_TYPE, rule);
			a.setValue(AccessFactory.CHARS, "false");
			a.setValue(AccessFactory.SYMBOLS, "false");
			a.setValue(AccessFactory.NUMBERS, "false");
			program.getActiveDocument().setChanged(true);
		}
	}
}