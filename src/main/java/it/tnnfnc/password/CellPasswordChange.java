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

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import it.tnnfnc.apps.application.ui.PopMessage;
import it.tnnfnc.password.domain.Access;
import it.tnnfnc.password.domain.AccessFactory;
import it.tnnfnc.password.domain.AccessTableModel;
import it.tnnfnc.table.cell.CellEditor;

/**
 * Implements editor and renderer for a button in a table cell. The toggle
 * button is supported.
 * 
 * @author Franco Toninato
 * 
 */
public class CellPasswordChange extends CellEditor implements TableCellEditor,
		TableCellRenderer, ActionListener
// extends CellButton
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ChangeAccessPasswordPanel changePanel;
	private Password application;
	private JButton button;

	public CellPasswordChange(JButton button, Password app) {
		this.button = button;
		this.cell = new Point(0, 0);
		button.setBorderPainted(false);
		button.addActionListener(this);
		button.setOpaque(true);
		setClickCountToStart(1);
		button.setActionCommand(PasswordConstants.CHANGE_PASSWORD);
		changePanel = new ChangeAccessPasswordPanel(app);
		this.application = app;
	}

	private Access getAccess(int row) {
		Access a = ((AccessTableModel) table.getModel()).getEntry(row);
		return a;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (application.isEditing() == false) {
			PopMessage.displayInfo(application.getFrame(), application
					.getLocalization().getString("Enable edit mode"));
			cancelCellEditing();
			return;
		}
		if (application.isReady() == false) {
			PopMessage.displayInfo(application.getFrame(), application
					.getLocalization().getString("Passphrase is missing"));
			cancelCellEditing();
			return;
		}
		try {
			changePanel.showDialog(getAccess(cell.x));
			((AccessTableModel) table.getModel()).fireTableCellUpdated(cell.x,
					cell.y);
			// Need for exiting the button
			cancelCellEditing();
		} catch (Exception e1) {
			cancelCellEditing();
			PopMessage.displayInfo(application.getFrame(), application
					.getLocalization().getString("Error") + e1.getMessage());
		}
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		super.table = table;
		if (hasFocus) {
			button.requestFocus();
		}
		button.setText((getAccess(row).getValue(AccessFactory.STATUS) + "")
				.equals(AccessFactory.STATUS_UNDEFINED) ? application
				.getLocalization().getString("New") : application
				.getLocalization().getString("Change"));
		return button;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		super.table = table;
		this.value = value;
		this.table = table;
		cell.setLocation(row, column);
		button.setText((getAccess(row).getValue(AccessFactory.STATUS) + "")
				.equals(AccessFactory.STATUS_UNDEFINED) ? application
				.getLocalization().getString("New") : application
				.getLocalization().getString("Change"));
		return button;
	}

	@Override
	public boolean isCellEditable(EventObject anEvent) {
		return true;
	}

	@Override
	public boolean shouldSelectCell(EventObject anEvent) {
		return true;
	}

	// @Override
	// public Object getCellEditorValue() {
	// return null;
	// }

}
