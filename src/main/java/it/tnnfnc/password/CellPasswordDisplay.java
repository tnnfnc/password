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
package it.tnnfnc.password; //Package

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import it.tnnfnc.table.cell.CellEditor;

/**
 * This class implements a cell editor as a button to display the password.
 * 
 * @author Franco Toninato
 * 
 */
public class CellPasswordDisplay extends CellEditor implements TableCellEditor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton button;
	private Password application;

	/**
	 * @param p
	 *            the application.
	 */
	public CellPasswordDisplay(Password p) {
		setClickCountToStart(1);
		button = new JButton();
		button.addActionListener(new ThisButtonListener());
		this.application = p;
	}

	/**
	 * Get the cell point in the table.
	 * 
	 * @return the cell point.
	 */
	public Point getPoint() {
		return this.cell;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		this.table = table;
		this.value = value;
		this.cell.setLocation(row, column);

		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		return button;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.table.cell.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		return value;
	}

	/**
	 * Button listener, when pressed calls the dialog.
	 * 
	 */
	private class ThisButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			try {
				int modifiers = event.getModifiers();
				application.displayPassword(cell.x, modifiers);
			} catch (Exception e) {
				e.printStackTrace();
			}
			cancelCellEditing();
		}
	}
}