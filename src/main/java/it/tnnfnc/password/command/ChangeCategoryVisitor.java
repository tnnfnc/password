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

import it.tnnfnc.password.document.AccessDocument;
import it.tnnfnc.password.domain.Access;
import it.tnnfnc.password.domain.AccessFactory;

/**
 * Command change category.
 * 
 */
public class ChangeCategoryVisitor extends AccessVisitor {

	private String oldCategory;
	private String newCategory;

	/**
	 * Change the access category.
	 * 
	 * @param oldCategory
	 *            the old value.
	 * @param newCategory
	 *            the new value.
	 */
	public ChangeCategoryVisitor(String oldCategory, String newCategory) {
		this.oldCategory = oldCategory;
		this.newCategory = newCategory;
	}

	/**
	 * Change the access category.
	 */
	@Override
	public void visitAccess(Access access) {
		super.visitAccess(access);
		if (access.getValue(AccessFactory.CATEGORY).toString().equals(oldCategory)) {
			access.setValue(AccessFactory.CATEGORY, newCategory);
		}
	}

	/**
	 * 
	 */
	@Override
	public void visitDocument(AccessDocument document) {
		super.visitDocument(document);
		// System.out.println("change from " + this.getClass().getName() );
		document.setChanged(true);
	}

}
