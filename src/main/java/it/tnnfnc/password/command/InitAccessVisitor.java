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

import it.tnnfnc.password.Password;
import it.tnnfnc.password.PasswordConstants;
import it.tnnfnc.password.document.AccessDocument;
import it.tnnfnc.password.domain.Access;
import it.tnnfnc.password.domain.AccessFactory;
import it.tnnfnc.password.domain.AccessTableModel;

/**
 * Initialize the access with defaults.
 * 
 * @author franco toninato
 * 
 */
public class InitAccessVisitor extends AccessVisitor {

	private Password application;

	/**
	 * @param a
	 */
	public InitAccessVisitor(Password a) {
		this.application = a;
		this.properties = a.getProperties();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.catode.password.AccessVisitor#visitAccess(net.catode.password.Access)
	 */
	@Override
	public void visitAccess(Access access) {
		// Default validity
		access.setValue(AccessFactory.ACCESS, application.getLocalization()
				.getString("..."));

		access.setValue(
				AccessFactory.VALIDITY,
				+Integer.parseInt(properties
						.getProperty(PasswordConstants.PREF_VALIDITY)) + "");

		access.setValue(AccessFactory.CATEGORY,
				application.getSelectedTabName());

		access.get(AccessFactory.STATUS).setStyle(
				PasswordConstants.getGrayFormat());
		access.setValue(AccessFactory.STATUS, application.getLocalization()
				.getString(AccessFactory.STATUS_UNDEFINED));

		super.visitAccess(access);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenet.catode.password.AccessVisitor#visitDocument(net.catode.password.
	 * AccessDocument)
	 */
	@Override
	public void visitDocument(AccessDocument document) {
		super.visitDocument(document);
		// document.setChanged(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenet.catode.password.AccessVisitor#visitModel(net.catode.password.
	 * AccessTableModel)
	 */
	@Override
	public void visitModel(AccessTableModel model) {
		super.visitModel(model);
	}

}
