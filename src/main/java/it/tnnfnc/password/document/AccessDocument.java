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
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import it.tnnfnc.apps.application.AbstractApplication;
import it.tnnfnc.apps.application.document.Document;
import it.tnnfnc.apps.application.properties.PropertiesXMLWriter;
import it.tnnfnc.apps.application.ui.style.I_StyleObject;
import it.tnnfnc.apps.application.undo.ObjectStatusModel;
import it.tnnfnc.apps.resource.I_Resource;
import it.tnnfnc.datamodel.I_Visitable;
import it.tnnfnc.password.PasswordConstants;
import it.tnnfnc.password.command.AccessVisitor;
import it.tnnfnc.password.domain.Access;
import it.tnnfnc.password.domain.AccessFactory;
import it.tnnfnc.password.domain.AccessTableModel;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * A document is an array of accesses.
 * 
 * @author Franco Toninato
 * 
 */
public class AccessDocument extends Document implements I_Visitable<AccessVisitor>, Serializable {

	private AccessFactory factory;
	private Properties properties;
	private AccessTableModel dataModel;

	// private static final String VERSION = "2.0";
	// private ListSelectionModel selections;

	/**
	 * 
	 */
	private static final long serialVersionUID = -4618015871856884772L;

	/**
	 * Create a new document.
	 */
	public AccessDocument() {
		initialize();
	}

	/**
	 * Initializes the fields and properties of this class with default values.
	 * This is called by the constructors.
	 */
	protected void initialize() {
		factory = AccessFactory.getInstance();
		traceModel = new ObjectStatusModel<I_StyleObject>();
		dataModel = new AccessTableModel(this);
		properties = new Properties();
		setVersion(properties.getProperty(AbstractApplication.VERSION_KEY, ""));
	}

	/**
	 * Get the data model.
	 * 
	 * @return the data model.
	 */
	public AccessTableModel getModel() {
		return dataModel;
	}

	/**
	 * Get the trace model.
	 * 
	 * @return the trace model.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ObjectStatusModel<I_StyleObject> getTraceModel() {
		return (ObjectStatusModel<I_StyleObject>) traceModel;
	}

	/**
	 * Get the document properties.
	 * 
	 * @return the properties.
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * Set the document properties.
	 * 
	 * @param p
	 *            the properties.
	 */
	public void setProperties(Properties p) {
		properties = p;
	}

	/**
	 * Get the access factory.
	 * 
	 * @return the access factory.
	 */
	public AccessFactory getAccessFactory() {
		return factory;
	}

	@Override
	public AccessDocument copy() throws CloneNotSupportedException {
		AccessDocument aDocument = new AccessDocument();
		return aDocument;
	}

	/**
	 * Performs analysis over accessArray contained in this document. This
	 * method is part of a visitor design pattern.
	 * 
	 * @param visitor
	 *            the analyzer.
	 */
	@Override
	public void accept(AccessVisitor visitor) {
		visitor.visitDocument(this);
		dataModel.accept(visitor);
	}

	/**
	 * Open an XML document using AccessXMLHandler.
	 * 
	 * @see it.tnnfnc.apps.application.document.Document#open(java.io.InputStream)
	 */
	@Override
	public synchronized void open() throws IOException {
		initialize();

		Thread t = new Thread() {
			public void run() {

				try {
					XMLReader xr;
					InputStream in = null;
					try {
						xr = XMLReaderFactory.createXMLReader();
						AccessDocumentXMLHandler handler = new AccessDocumentXMLHandler(factory);
						xr.setContentHandler(handler);
						xr.setErrorHandler(handler);
						// Open the resource
						I_Resource r = getResource();
						r.setLock(true);
						in = r.getInputStream();
						// try {
						xr.parse(new InputSource(in));
						// Build
						Access[] list = handler.getResult();
						traceModel = handler.getTraceModel();
						properties = handler.getProperties();
						setVersion(properties.getProperty(AbstractApplication.VERSION_KEY, "0"));
						for (Access a : list) {
							dataModel.addEntry(a);
						}
						in.close();
						// wait(10000);
					} //
					catch (IOException e1) {
						if (in != null)
							in.close();
						e1.printStackTrace();
					} //
					catch (SAXException e1) {
						if (in != null)
							in.close();
						e1.printStackTrace();
					} //
						// catch (InterruptedException e) {
						// e.printStackTrace();
						// }
				} catch (IOException e1) {

				}
			}
		};

		t.start();

		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.apps.document.Document#close()
	 */
	@Override
	public synchronized void close() throws IOException {
		traceModel = null;
		properties = null;
		factory = null;
		dataModel = null;
	}

	/**
	 * Save into ax XML format using AccessXMLWriter.
	 * 
	 * @see it.tnnfnc.apps.application.document.Document#save()
	 */
	@Override
	public synchronized void save() throws IOException {
		I_Resource r = getResource();
		if (r == null || r.getOutputStream() == null) {
			throw new IOException("No resource available");
		}
		// ->
		StreamResult streamResult = new StreamResult();
		streamResult.setOutputStream(r.getOutputStream());
		r.setLock(true);
		// !! Cast to obtain a SAXTransformerFactory
		SAXTransformerFactory tf = (SAXTransformerFactory) TransformerFactory.newInstance();

		try {
			TransformerHandler hd = tf.newTransformerHandler();
			hd.setResult(streamResult);

			// !! Set the parameters!
			Transformer transformer = hd.getTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "access.dtd");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			// -> Log
			traceModel.trimToSize(Integer.parseInt(properties.getProperty(PasswordConstants.PREF_HISTORY) + ""));

			// -> Serialize here
			AccessDocumentXMLWriter w = new AccessDocumentXMLWriter(hd, factory);
			PropertiesXMLWriter pw = new PropertiesXMLWriter(hd);

			w.startDocument();

			// Update the new version
			// setVersion(VERSION);
			// properties.setProperty(VERSION_KEY, VERSION);
			pw.write(properties);
			w.writeDescriptor();
			for (int i = 0; i < dataModel.getFullSize(); i++) {
				w.write(dataModel.getEntry(i), traceModel);
			}
			w.endDocument();
			// <-
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
			IOException ex = new IOException(e.getCause());
			throw ex;
		} catch (SAXException e) {
			e.printStackTrace();
			IOException ex = new IOException(e.getCause());
			throw ex;
		} finally {

			streamResult.getOutputStream().close();
			// impossible to write any more!
		}
	}

}
