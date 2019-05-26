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

import java.util.Properties;

import it.tnnfnc.datamodel.I_Visitor;
import it.tnnfnc.password.document.AccessDocument;
import it.tnnfnc.password.domain.Access;
import it.tnnfnc.password.domain.AccessTableModel;

/**
 * 
 *
 */
public class AccessVisitor implements I_Visitor {
	protected Properties properties;
	protected AccessDocument document;
	protected AccessTableModel model;

	/**
	 * Inspect the access.
	 * 
	 * @param access
	 *            the access.
	 */
	public void visitAccess(Access access) {
	}

	/**
	 * Inspect the document and store the reference.
	 * 
	 * @param document
	 *            the document.
	 */
	public void visitDocument(AccessDocument document) {
		this.document = document;
	}

	/**
	 * Inspect the model and store the reference.
	 * 
	 * @param model
	 *            the model.
	 */
	public void visitModel(AccessTableModel model) {
		this.model = model;
	}

	@Override
	public void visit(Object o) {

	}

}