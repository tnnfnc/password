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

import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import it.tnnfnc.apps.application.ui.style.I_StyleObject;
import it.tnnfnc.apps.application.ui.style.StyleObject;
import it.tnnfnc.apps.application.undo.ObjectStatus;
import it.tnnfnc.apps.application.undo.ObjectStatusModel;
import it.tnnfnc.password.PasswordConstants;
import it.tnnfnc.password.domain.Access;
import it.tnnfnc.password.domain.AccessFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Handler for parsing a password document into an XML object representing the
 * document.
 * 
 * @author Franco Toninato
 * 
 */
public class AccessDocumentXMLHandler extends DefaultHandler {
	// public static final String ATTR_VERSION = "accessVersion";
	// public static final String HEADER = "header";
	// public static final String BODY = "body";
	// public static final String ATTR_SALT = "salt";

	public static final String ARRAY = "array";
	public static final String ROW = "row";
	public static final String FIELD = "field";
	public static final String VALUE = "value";
	public static final String BACKUP = "backup";
	public static final String ATTR_TIME = "time";

	public static final String ATTR_NAME = "name";
	public static final String ATTR_STYLE = "style";

	public static final String DESCRIPTOR = "descriptor";
	public static final String F = "f";
	public static final String TYPE = "type";
	public static final String HEADER = "header";
	public static final String DEFAULT = "default";
	public static final String ATTR_SYSTEM = "system";

	private ArrayList<Access> accessList;
	private Access access;
	private StringBuffer buffer;
	// private Attributes attributes;

	private AccessFactory factory;
	private ObjectStatusModel<I_StyleObject> trace;

	private ValueBuilder valueBuilder = new ValueBuilder();
	private DescriptorBuilder descriptorBuilder = new DescriptorBuilder();
	private PropertiesBuilder propertiesBuilder = new PropertiesBuilder();
	private Properties properties;

	public AccessDocumentXMLHandler(AccessFactory f) {
		factory = f;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		buffer.append(new String(ch, start, length));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#startDocument()
	 */
	@Override
	public void startDocument() throws SAXException {
		// Does specific actions at the beginning of a document
		super.startDocument();
		trace = new ObjectStatusModel<I_StyleObject>();
		accessList = new ArrayList<Access>();
		buffer = new StringBuffer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
	 * java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		// this.attributes = attributes;
		openElement(qName);

		// Properties descriptor
		if (localName.equals("properties")) {
			properties = new Properties();
		} else if (localName.equals("comment")) {
		} else if (localName.equals("entry")) {
			propertiesBuilder.setAttributes(attributes);
		}

		// Access descriptor
		else if (localName.equals(DESCRIPTOR)) {
		} else if (localName.equals("f")) {
			descriptorBuilder.setAttributes(attributes);
		} else if (localName.equals(TYPE)) {
		} else if (localName.equals(HEADER)) {
		} else if (localName.equals(DEFAULT)) {
		}

		// Access
		else if (localName.equals(ROW)) {
			// access = factory.createAccess();
			accessList.add(access = factory.newRow());
		} else if (localName.equals(FIELD)) {
			valueBuilder.setAttributes(attributes);
		} else if (localName.equals(BACKUP)) {
			// valueBuilder.setAttributes(attributes);
		} else {
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		// Properties descriptor
		if (localName.equals("properties")) {
		} else if (localName.equals("comment")) {
		} else if (localName.equals("entry")) {
			propertiesBuilder.setProperty(properties);
		}

		// Access descriptor
		else if (localName.equals(DESCRIPTOR)) {
		} else if (localName.equals(F)) {
			descriptorBuilder.build();
		} else if (localName.equals(TYPE)) {
			descriptorBuilder.setType();
		} else if (localName.equals(HEADER)) {
			descriptorBuilder.setHeader();
		} else if (localName.equals(DEFAULT)) {
			descriptorBuilder.setDefault();
		} else if (localName.equals(ARRAY)) {
		}
		// Access
		else if (localName.equals(ROW)) {
		} else if (localName.equals(FIELD)) {
		}
		// main field
		else if (localName.equals(VALUE)) {
			valueBuilder.build(access);
		}
		// field backup
		else if (localName.equals(BACKUP)) {
			valueBuilder.backup(access);
		} else {
		}
		closeElement(qName);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#endDocument()
	 */
	@Override
	public void endDocument() throws SAXException {
		// Auto-generated method stub
	}

	/**
	 * Get the result array.
	 * 
	 * @return the parsed result array.
	 */
	public Access[] getResult() {
		return accessList.toArray(new Access[0]);
	}

	/**
	 * Get the old value entries.
	 * 
	 * @return the old values.
	 */
	public ObjectStatusModel<?> getTraceModel() {
		return trace;
	}

	/**
	 * Get the application properties.
	 * 
	 * @return the application properties.
	 */
	public Properties getProperties() {
		return properties;
	}

	private void openElement(String string) {
		// System.out.println("<" + string + ">");
		buffer.delete(0, buffer.length());
	}

	private void closeElement(String string) {
		// System.out.println(buffer.toString().trim() + "\n</" + string + ">");
		buffer.delete(0, buffer.length());
	}

	/**
	 * Parser for custom fields value and backup.
	 * 
	 */
	class ValueBuilder {
		// I_StyleObject value;

		private Attributes atts;
		private String name;
		private String style;

		public void setAttributes(Attributes atts) {
			this.atts = atts;
			name = atts.getValue(ATTR_NAME);
			style = atts.getValue(ATTR_STYLE);
		}

		public void build(Access a) {
			if (name != null && name != "") {
				a.setValue(name, buffer.toString().trim());
				a.get(name).setStyle(style);
			}
		}

		/**
		 * @throws SAXException
		 */
		public void backup(Access a) throws SAXException {
			try {
				int pos = factory.getIndex(name);
				I_StyleObject bkvalue = new StyleObject(
						factory.deSerialize(factory.getType(pos).getName(), buffer.toString().trim()));
				Date timestamp = new Date();
				timestamp.setTime(Long.parseLong(atts.getValue(ATTR_TIME)));
				trace.setTrace(a.getTraceID(pos), new ObjectStatus<I_StyleObject>(bkvalue, "", timestamp));

			} catch (NumberFormatException e) {

			}
		}
	}

	class DescriptorBuilder {
		// private Attributes atts;
		private String type;
		private String deflt;
		private String header;
		private String name;

		/**
		 * Set the attributes.
		 * 
		 * @param atts
		 *            the attributes.
		 */
		public void setAttributes(Attributes atts) {
			// this.atts = atts;
			name = atts.getValue("name");
		}

		public void build() throws SAXException {
			try {
				Class<?> clazz = Class.forName(type);
				factory.addField(name, clazz, header, deflt);
			} catch (ClassNotFoundException e) {
				try {
					Class<?> clazz = Class.forName(
							PasswordConstants.getClassName(type.substring(type.lastIndexOf(".") + 1, type.length())));
					factory.addField(name, clazz, header, deflt);
				} catch (ClassNotFoundException e1) {
					throw new SAXException(e.getMessage());
				}
			}
		}

		public void setType() {
			type = buffer.toString().trim();
		}

		public void setHeader() {
			header = buffer.toString().trim();
		}

		public void setDefault() {
			deflt = buffer.toString().trim();
		}

	}

	class PropertiesBuilder {
		// private Attributes atts;
		private String key;

		/**
		 * Set the attributes.
		 * 
		 * @param atts
		 *            the attributes.
		 */
		public void setAttributes(Attributes atts) {
			// this.atts = atts;
			this.key = atts.getValue("key");
		}

		public void setProperty(Properties p) {
			if (key != null)
				p.setProperty(key, buffer.toString().trim());
		}

	}
}
