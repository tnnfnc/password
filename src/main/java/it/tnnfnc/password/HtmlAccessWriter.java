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

import it.tnnfnc.password.domain.Access;
import it.tnnfnc.password.domain.AccessFactory;

public class HtmlAccessWriter {

	/**
	 * Get a printable access dump.
	 * 
	 * @return a readable access dump.
	 */
	public static String write(Access a) {
		StringBuffer text = new StringBuffer();

		text.append("<H3>");
		text.append(a.getFactory().getLabel(AccessFactory.ACCESS));
		text.append(": ");
		text.append(a.getFactory().serialize(
				a.getFactory()
						.getType(a.getFactory().getIndex(AccessFactory.ACCESS))
						.getName(), a.getValue(AccessFactory.ACCESS)));
		text.append("</H3>");

		text.append(a.getFactory().getLabel(AccessFactory.CATEGORY));
		text.append(": ");
		text.append(a.getFactory()
				.serialize(
						a.getFactory()
								.getType(
										a.getFactory().getIndex(
												AccessFactory.CATEGORY))
								.getName(), a.getValue(AccessFactory.CATEGORY)));
		text.append(", ");

		text.append(a.getFactory().getLabel(AccessFactory.URL));
		text.append(": ");
		text.append(a.getFactory().serialize(
				a.getFactory()
						.getType(a.getFactory().getIndex(AccessFactory.URL))
						.getName(), a.getValue(AccessFactory.URL)));
		text.append(", ");

		text.append(a.getFactory().getLabel(AccessFactory.USER));
		text.append(": ");
		text.append(a.getFactory().serialize(
				a.getFactory()
						.getType(a.getFactory().getIndex(AccessFactory.USER))
						.getName(), a.getValue(AccessFactory.USER)));
		text.append(", ");

		text.append(a.getFactory().getLabel(AccessFactory.TEXT));
		text.append(": ");
		text.append(a.getFactory().serialize(
				a.getFactory()
						.getType(a.getFactory().getIndex(AccessFactory.TEXT))
						.getName(), a.getValue(AccessFactory.TEXT)));

		return text.toString();
	}
}
