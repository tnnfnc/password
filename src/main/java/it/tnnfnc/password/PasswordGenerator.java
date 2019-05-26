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

import it.tnnfnc.encoders.Base64;
import it.tnnfnc.encoders.EncodingAlgorithmFactory;
import it.tnnfnc.encoders.IdentityEncoder;
import it.tnnfnc.password.domain.Access;
import it.tnnfnc.password.domain.AccessFactory;
import it.tnnfnc.security.SecurityObject;

/**
 * @author FT
 *
 */
public class PasswordGenerator {
	EncodingAlgorithmFactory encodingAlgorithmFactory;

	public PasswordGenerator(EncodingAlgorithmFactory eaf) {
		this.encodingAlgorithmFactory = eaf;
	}

	/**
	 * Get the list of available algorithms. Pass through to
	 * {@link EncodingAlgorithmFactory#getList()}.
	 * 
	 * @return the list of available algorithms.
	 */
	public String[] getList() {
		return encodingAlgorithmFactory.getList();
	}

	/**
	 * Get the password.
	 * 
	 * @param access
	 *            the access.
	 * @param security
	 *            the security object.
	 * @return the password.
	 * @throws IllegalArgumentException
	 */
	public char[] getPassword(Access access, SecurityObject security) throws IllegalArgumentException {
		char[] password = null;

		String algorithm = (access.getValue(AccessFactory.PASSWORD_TYPE) + "").split("\\.")[0];
		try {
			if ((algorithm).equalsIgnoreCase(IdentityEncoder.algorithm)) {
				// Init the decipher
				byte input[];
				input = Base64.decode(access.getUserPassword().getBytes());
				password = new String(security.getCipher(access.getSalt(), false).doFinal(input, 0, input.length))
						.toCharArray();
			} else {
				byte[] in = security.generateKey(access.getSalt(), access.getPasswordLength());
				password = encodingAlgorithmFactory.getAlgorithmInstance(algorithm).encode(in,
						PasswordDictionary.getDictionary(access), access.getPasswordLength());
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getCause());
		}
		return password;
	}
}
