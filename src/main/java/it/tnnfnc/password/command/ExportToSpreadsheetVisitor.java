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

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import it.tnnfnc.apps.application.ui.Utility;
import it.tnnfnc.password.Password;
import it.tnnfnc.password.document.AccessDocument;
import it.tnnfnc.password.domain.Access;
import it.tnnfnc.password.domain.AccessFactory;

/**
 * Export the list of accesses to a spreadsheet, passwords are visible.
 * 
 * @author Franco Toninato
 * 
 */
public class ExportToSpreadsheetVisitor extends AccessVisitor {

	private Password application;
	private String log[];
	private FileWriter writer;
	private JTable table;
	private int[] visibleComumns;

	/**
	 * Get a printable access dump.
	 * 
	 * @return a readable access dump.
	 */
	private String write(Access a) {
		StringBuffer s = new StringBuffer();
		AccessFactory f = a.getFactory();
		for (int i = 0; i < visibleComumns.length; i++) {
			if (f.getType(visibleComumns[i]) == Date.class) {
				s.append(Utility.dateToString(new Date(Long.parseLong(
						f.serialize(f.getType(visibleComumns[i]).getName(), a.get(visibleComumns[i]).getValue()))), "/")
						+ "\t");
			} else {
				if (f.getKey(visibleComumns[i]).equals(AccessFactory.PASSWORD_DISPLAY)) {
					int j = f.getIndex(AccessFactory.PASSWORD);
					s.append(f.serialize(f.getType(j).getName(), a.get(j).getValue()) + "\t");
				} else {
					s.append(f.serialize(f.getType(visibleComumns[i]).getName(), a.get(visibleComumns[i]).getValue())
							+ "\t");
				}
			}
		}

		s.append("\r\n");

		return s.toString();
	}

	/**
	 * @param application
	 *            the program.
	 * @param writer
	 *            the file writer
	 * @param table
	 *            the table to get the visible columns
	 */
	public ExportToSpreadsheetVisitor(Password application, FileWriter writer, JTable table) {
		this.application = application;
		this.writer = writer;
		log = new String[0];
		this.table = table;
	}

	public String[] getLog() {
		return log;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.catode.password.AccessVisitor#visitAccess(net.catode.password.Access)
	 */
	@Override
	public void visitAccess(Access access) {
		super.visitAccess(access);
		if (access.isPasswordDefined()) {
			try {
				char pw[] = application.getPassword(access);
				Access ia = access.copy();
				ia.setValue(AccessFactory.PASSWORD, new String(pw));
				// ia.setValue(AccessFactory.PASSWORD_DISPLAY, new String(pw));
				writer.write(write(ia));
			} catch (Exception e) {
				String temp[] = new String[log.length + 1];
				System.arraycopy(log, 0, temp, 0, log.length);
				temp[log.length] = access.getValue(AccessFactory.ACCESS) + ": Error occurred: " + e.getMessage();
				log = temp;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.password.AccessVisitor#visitDocument(net.catode.password.
	 * AccessDocument)
	 */
	@Override
	public void visitDocument(AccessDocument document) {
		super.visitDocument(document);
		AccessFactory f = document.getAccessFactory();
		StringBuffer s = new StringBuffer();
		TableColumnModel m = table.getColumnModel();
		visibleComumns = new int[m.getColumnCount()];
		for (int i = 0; i < m.getColumnCount(); i++) {
			visibleComumns[i] = m.getColumn(i).getModelIndex();
			s.append(f.getLabel(f.getKey(visibleComumns[i])) + "\t");
		}
		s.append("\r\n");

		// Write the spreadsheet header with column names.
		try {
			writer.write(s.toString());
		} catch (IOException e) {
		}
	}

}
