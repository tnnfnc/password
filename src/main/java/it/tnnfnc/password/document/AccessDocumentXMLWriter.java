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

import it.tnnfnc.apps.application.ui.style.I_StyleObject;
import it.tnnfnc.apps.application.undo.I_Status;
import it.tnnfnc.apps.application.undo.ObjectStatusModel;
import it.tnnfnc.password.domain.Access;
import it.tnnfnc.password.domain.AccessFactory;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * The class is responsible of writing a password document using SAX events.
 * 
 * @author Franco Toninato
 * 
 */
public class AccessDocumentXMLWriter {

	private ContentHandler handler;
	private String style;
	private String value;
	private int progr;
	private AccessFactory factory;

	/**
	 * Creates a new xml writer.
	 * 
	 * @param h
	 *            the content handler.
	 * @param f
	 *            the access factory.
	 */
	public AccessDocumentXMLWriter(ContentHandler h, AccessFactory f) {
		this.handler = h;
		this.factory = f;
	}

	/**
	 * Shot events for writing an access;
	 * 
	 * @param anAccess
	 *            the access.
	 * @param m
	 * @throws SAXException
	 */
	public void write(Access anAccess, ObjectStatusModel<?> m) throws SAXException {
		AttributesImpl atts;
		atts = new AttributesImpl();
		// <access> - name
		atts.addAttribute("", "", AccessDocumentXMLHandler.ATTR_NAME, "CDATA", progr++ + "");
		handler.startElement("", "", AccessDocumentXMLHandler.ROW, atts);

		// Write the access fields
		for (int i = 0; i < anAccess.size(); i++) {
			Object name = factory.getKey(i);
			try {
				value = anAccess.getFactory().serialize(factory.getType(i).getName(), anAccess.getValue(name));
			} catch (IllegalArgumentException e) {
				throw new SAXException(e.getMessage());
			}
			if (value == null)
				System.out.println("AccessXMLWriter - key=" + name + " value = " + value);
			value = value == null ? "" : value;

			style = anAccess.get(i).getStyle() + "";
			atts.clear();
			atts.addAttribute("", "", AccessDocumentXMLHandler.ATTR_NAME, "CDATA", //
					name + "");
			if (style != null && style != "") {
				atts.addAttribute("", "", AccessDocumentXMLHandler.ATTR_STYLE, "CDATA", //
						style);
			}
			handler.startElement("", "", AccessDocumentXMLHandler.FIELD, atts);
			// <value>
			atts.clear();
			handler.startElement("", "", AccessDocumentXMLHandler.VALUE, atts);
			handler.characters(value.toCharArray(), 0, value.length());
			// </value>
			handler.endElement("", "", AccessDocumentXMLHandler.VALUE);
			atts.clear();
			// for (I_ObjectTrace<?> o : m.getHistory(field)) {
			for (I_Status<?> o : m.getHistory(anAccess.getTraceID(i))) {
				String timestamp = o.getTimeStamp().getTime() + "";
				atts.addAttribute("", "", AccessDocumentXMLHandler.ATTR_TIME, "CDATA", timestamp);
				/* *************** <backup> - timestamp *************** */
				handler.startElement("", "", AccessDocumentXMLHandler.BACKUP, atts);
				String backup;
				try {
					backup = factory.serialize(factory.getType(i).getName(), ((I_StyleObject) o.getStatus()).getValue())
							.toString();
					handler.characters(backup.toCharArray(), 0, backup.length());
				} catch (IllegalArgumentException e) {

				}
				// </backup>
				handler.endElement("", "", AccessDocumentXMLHandler.BACKUP);
				atts.clear();
				/* *************** <backup> - timestamp *************** */
			}
			// </field>
			handler.endElement("", "", AccessDocumentXMLHandler.FIELD);
		}
		handler.endElement("", "", AccessDocumentXMLHandler.ROW);
		atts.clear();
	}

	/**
	 * Shot events for writing an access;
	 * 
	 * @param a
	 *            the access.
	 * @throws SAXException
	 */
	public void writeDescriptor() throws SAXException {
		AttributesImpl atts;
		atts = new AttributesImpl();
		handler.startElement("", "", AccessDocumentXMLHandler.DESCRIPTOR, atts);
		String buffer = null;
		String key;
		for (int i = 0; i < factory.getSize(); i++) {
			atts.clear();
			key = factory.getKey(i).toString();
			atts.addAttribute("", "", AccessDocumentXMLHandler.ATTR_NAME, "CDATA", key);
			atts.addAttribute("", "", AccessDocumentXMLHandler.ATTR_SYSTEM, "CDATA", factory.isInternal(i) + "");
			handler.startElement("", "", AccessDocumentXMLHandler.F, atts);
			atts.clear();
			handler.startElement("", "", AccessDocumentXMLHandler.TYPE, atts);
			buffer = factory.getType(i).getName();
			handler.characters(buffer.toCharArray(), 0, buffer.length());
			handler.endElement("", "", AccessDocumentXMLHandler.TYPE);
			atts.clear();
			handler.startElement("", "", AccessDocumentXMLHandler.HEADER, atts);
			buffer = factory.getLabel(key);
			handler.characters(buffer.toCharArray(), 0, buffer.length());
			handler.endElement("", "", AccessDocumentXMLHandler.HEADER);

			handler.startElement("", "", AccessDocumentXMLHandler.DEFAULT, atts);
			if (factory.getDefault(key) != null) {
				buffer = factory.getDefault(key);
				handler.characters(buffer.toCharArray(), 0, buffer.length());
			}
			handler.endElement("", "", AccessDocumentXMLHandler.DEFAULT);

			handler.endElement("", "", AccessDocumentXMLHandler.F);
		}
		handler.endElement("", "", AccessDocumentXMLHandler.DESCRIPTOR);

	}

	// /**
	// * @param object
	// * the input object
	// * @return the value object
	// */
	// private String parseField(Object object) {
	// Object v = object;
	// if (object instanceof I_StyleObject) {
	// v = ((I_StyleObject) object).getValue();
	// style = ((I_StyleObject) object).getStyle();
	// } else {
	// style = null;
	// }
	// // value = AccessFactory.toString(v).toString();
	// return factory.objectToStringt(v, null).toString();
	// }

	/**
	 * @throws SAXException
	 */
	public void startDocument() throws SAXException {
		handler.startDocument();
		AttributesImpl atts;
		atts = new AttributesImpl();
		handler.startElement("", "", AccessDocumentXMLHandler.ARRAY, atts);
	}

	/**
	 * @throws SAXException
	 */
	public void endDocument() throws SAXException {
		handler.endElement("", "", AccessDocumentXMLHandler.ARRAY);
		handler.endDocument();
	}
}
