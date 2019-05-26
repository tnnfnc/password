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

import java.util.Calendar;
import java.util.Date;

import it.tnnfnc.password.Password;
import it.tnnfnc.password.PasswordConstants;
import it.tnnfnc.password.document.AccessDocument;
import it.tnnfnc.password.domain.Access;
import it.tnnfnc.password.domain.AccessFactory;
import it.tnnfnc.password.domain.AccessTableModel;

/**
 * Check the access internal state and update the standard message.
 * 
 * @author franco toninato
 * 
 */
public class ChangeAccessVisitor extends AccessVisitor {
	private Password application;

	public ChangeAccessVisitor(Password a) {
		this.properties = a.getProperties();
		this.application = a;
	}

	/**
	 * 
	 * @see it.tnnfnc.password.command.AccessVisitor#visitAccess(it.tnnfnc.password.domain.Access)
	 * 
	 */
	@Override
	public void visitAccess(Access access) {
		super.visitAccess(access);
		statusCheck(access);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.passwordManager.AccessVisitor#visitDocument(net.catode.
	 * passwordManager.AccessDocument)
	 */
	@Override
	public void visitDocument(AccessDocument document) {
		super.visitDocument(document);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.passwordManager.AccessVisitor#visitDocument(net.catode.
	 * passwordManager.AccessDocument)
	 */
	@Override
	public void visitModel(AccessTableModel model) {
		super.visitModel(model);
	}

	/**
	 * Change the access status.
	 * 
	 * @param access
	 *            the access
	 */
	private void statusCheck(Access access) throws NumberFormatException {
		if (access.isPasswordDefined()) {
			// Set the warning date
			long now = Calendar.getInstance().getTime().getTime();
			long expireDate = ((Date) access.getValue(AccessFactory.EXPIRE_ON)).getTime();
			long warningPeriod = 0L;
			try {
				warningPeriod = Long
						.parseLong(application.getProperties().getProperty(PasswordConstants.PREF_EXPIRY_WARNING) + "")
						* 24 * 3600000;
			} catch (NumberFormatException e) {
			}

			if (now > expireDate) {
				access.get(AccessFactory.STATUS).setStyle(PasswordConstants.getRedFormat());
				access.setValue(AccessFactory.STATUS,
						application.getLocalization().getString(AccessFactory.STATUS_EXPIRED));
			} else if (now >= expireDate - warningPeriod) {
				access.get(AccessFactory.STATUS).setStyle(PasswordConstants.getYellowFormat());
				access.setValue(AccessFactory.STATUS,
						application.getLocalization().getString(AccessFactory.STATUS_WARNING));
			} else {
				access.get(AccessFactory.STATUS).setStyle(PasswordConstants.getBlankFormat());
				access.setValue(AccessFactory.STATUS,
						application.getLocalization().getString(AccessFactory.STATUS_ACTIVE));
			}
		} else {
			access.get(AccessFactory.STATUS).setStyle(PasswordConstants.getGrayFormat());
			access.setValue(AccessFactory.STATUS,
					application.getLocalization().getString(AccessFactory.STATUS_UNDEFINED));
		}

	}

}
