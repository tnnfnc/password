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
package it.tnnfnc.password.document;

import java.io.IOException;
import java.io.Serializable;

import it.tnnfnc.apps.application.document.Document;
import it.tnnfnc.apps.application.ui.style.I_StyleObject;
import it.tnnfnc.apps.application.undo.ObjectStatusModel;

/**
 * A document is an array of accesses.
 * 
 * @author Franco Toninato
 * 
 */
public class VoidDocument extends Document implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4618015871856884772L;

	/**
	 * Create a new document.
	 */
	public VoidDocument() {
		initialize();
	}

	/**
	 * Initializes the fields and properties of this class with default values.
	 * This is called by the constructors.
	 */
	protected void initialize() {
	}

	/**
	 * Get the trace model.
	 * 
	 * @return the trace model.
	 */
	@Override
	public ObjectStatusModel<I_StyleObject> getTraceModel() {
		return null;
	}

	@Override
	public VoidDocument copy() throws CloneNotSupportedException {
		VoidDocument aDocument = new VoidDocument();
		return aDocument;
	}

	/**
	 * Open an XML document using AccessXMLHandler.
	 * 
	 * @see it.tnnfnc.apps.application.document.Document#open(java.io.InputStream)
	 */
	@Override
	public synchronized void open() throws IOException {

		System.out.println("opening now " + getResource().getURL());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.apps.document.Document#close()
	 */
	@Override
	public synchronized void close() throws IOException {
		System.out.println("closing now " + getResource().getURL());
	}

	/**
	 * Save into ax XML format using AccessXMLWriter.
	 * 
	 * @see it.tnnfnc.apps.application.document.Document#save()
	 */
	@Override
	public synchronized void save() throws IOException {
		System.out.println("saving " + getResource().getURL());
	}

}
