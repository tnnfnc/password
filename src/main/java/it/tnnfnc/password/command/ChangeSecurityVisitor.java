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

import it.tnnfnc.encoders.Base64;
import it.tnnfnc.password.AccessSecurityObject;
import it.tnnfnc.password.Password;
import it.tnnfnc.password.domain.Access;
import it.tnnfnc.password.domain.AccessFactory;
import it.tnnfnc.security.crypto.I_BlockCipherStream;

/**
 * Change a security setting. For every access the generated password is
 * converted into a user defined password, and a status message is displayed for
 * every one.<br/>
 * Flow:
 * <ol>
 * <li>Press the change security button</li>
 * <li>Must be entered the old pass phrase checked against the current one</li>
 * <li>A new pass phrase must be entered</li>
 * <li>Security settings can be changed</li>
 * <li>Confirm the changes</li>
 * <li>The application saves every password as a user password and cipher with
 * the new security settings</li>
 * </ol>
 * 
 * @author Franco Toninato
 * 
 */
public class ChangeSecurityVisitor extends AccessVisitor {

	private Password application;
	private AccessSecurityObject security;
	private String log[];

	/**
	 * @param application
	 * @param security
	 */
	public ChangeSecurityVisitor(Password application, AccessSecurityObject security) {
		this.application = application;
		this.security = security;
		log = new String[0];
	}

	/**
	 * Encrypt the current password with the new security parameters. All
	 * generated passwords are replaced by user password.
	 * 
	 * @see it.tnnfnc.password.command.AccessVisitor#visitAccess(it.tnnfnc.password.domain.Access)
	 */
	@Override
	public void visitAccess(Access access) {
		if (access.isPasswordDefined()) {
			super.visitAccess(access);
			try {
				// Get the current password
				String pwd = new String(application.getPassword(access));
				byte[] salt = security.getRandomSeed();
				I_BlockCipherStream cipher = security.getCipher(salt, true);
				// Set the current password as newly encrypted user password.
				access.setUserPassword(
						new String(Base64.encode(cipher.doFinal(pwd.getBytes(), 0, pwd.getBytes().length))));
				access.setSalt(salt);
				// TODO the changed date

			} catch (Exception e) {
				// int i = 0;
				String temp[] = new String[log.length + 1];
				System.arraycopy(log, 0, temp, 0, log.length);
				temp[log.length] = access.getValue(AccessFactory.ACCESS) + ": Error occurred: " + e.getMessage();
				log = temp;
				application.setLog(log[log.length - 1]);
				// System.out.println("Error at: " + (log.length - 1) + " - " +
				// e.printStackTrace();
			}
		}
	}

	/**
	 * Get the transformation result.
	 * 
	 * @return the errors occurred.
	 */
	public String[] getResult() {
		return log;
	}
}
