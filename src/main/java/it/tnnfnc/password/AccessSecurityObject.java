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

import java.security.DigestException;
import java.security.InvalidKeyException;
import java.util.Properties;

import it.tnnfnc.encoders.Base64;
import it.tnnfnc.security.SecurityObject;

/**
 * Manage the cryptography needed for the password management.
 * 
 * @author Franco Toninato
 * 
 */
public class AccessSecurityObject extends SecurityObject {

	/**
	 * Create a security instance. A new random salt is generated if an initial
	 * value is passed.
	 * 
	 * @param properties
	 *            the properties.
	 * @param password
	 *            the password for key derivation.
	 * @throws InvalidKeyException
	 * @throws IllegalStateException
	 * @throws IllegalArgumentException
	 * @throws HashException
	 * @throws IllegalArgumentException
	 */
	public AccessSecurityObject(Properties properties, byte[] key) throws InvalidKeyException, IllegalStateException,
			IllegalArgumentException, DigestException, IllegalArgumentException {
		super(properties, key);

		// Auto check;
		if (properties.getProperty(PasswordConstants.PREF_CHECK) != null
				&& properties.getProperty(PasswordConstants.PREF_CHECK).length() > 0) {
			securityID = Base64.decode(properties.getProperty(PasswordConstants.PREF_CHECK).getBytes());
			if (check(key) == false) {
				throw new InvalidKeyException("Wrong passphrase");
			}
		}
		// set check;
		else {
			properties.setProperty(PasswordConstants.PREF_CHECK, new String(Base64.encode(securityID)));
		}

	}

}
