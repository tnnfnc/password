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
package it.tnnfnc.password.domain;

import java.util.ArrayList;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import it.tnnfnc.apps.application.ui.style.I_StyleObject;
import it.tnnfnc.apps.application.ui.style.StyleObject;
import it.tnnfnc.apps.application.undo.I_Status;
import it.tnnfnc.apps.application.undo.I_Undoable;
import it.tnnfnc.apps.application.undo.ObjectStatus;
import it.tnnfnc.datamodel.AbstractIndexModel;
import it.tnnfnc.datamodel.DataChangeEvent;
import it.tnnfnc.datamodel.I_DataChangeListener;
import it.tnnfnc.datamodel.I_Visitable;
import it.tnnfnc.datamodel.Index;
import it.tnnfnc.password.command.AccessVisitor;
import it.tnnfnc.password.document.AccessDocument;

/**
 * This class implements a table model for an access document.
 * 
 * @author Franco Toninato
 * 
 */
public class AccessTableModel extends AbstractIndexModel<Access>
		implements TableModel, I_DataChangeListener, I_Visitable<AccessVisitor>, I_Undoable {

	/**
	 * The internal data model.
	 */
	protected ArrayList<Access> accessArray;
	/**
	 * The document.
	 */
	protected AccessDocument document;
	/**
	 * Table model listeners.
	 */
	private boolean isEditable;

	/*
	 * Table model overall editability.
	 */

	/**
	 * Class builder.
	 * 
	 * @param d
	 *            the document.
	 */
	public AccessTableModel(AccessDocument d) {
		this.document = d;
		accessArray = new ArrayList<Access>();
		addDataChangeListener(this);
	}

	/**
	 * Sends a {@link TableModelEvent} to all registered listeners to inform
	 * them that the table data have changed.
	 */
	private void fireTableDataChanged() {
		fireTableChanged(new TableModelEvent(this, 0, Integer.MAX_VALUE));
	}

	/**
	 * Sends a {@link TableModelEvent} to all registered listeners to inform
	 * them that some rows have been inserted into the model.
	 * 
	 * @param firstRow
	 *            the index of the first row.
	 * @param lastRow
	 *            the index of the last row.
	 */
	private void fireTableRowsInserted(int firstRow, int lastRow) {
		// System.out.println(this.getClass().getName()+".fireTableRowsInserted
		// "
		// + firstRow + "" +lastRow);
		fireTableChanged(
				new TableModelEvent(this, firstRow, lastRow, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
	}

	/**
	 * Sends a {@link TableModelEvent} to all registered listeners to inform
	 * them that some rows have been deleted from the model.
	 * 
	 * @param firstRow
	 *            the index of the first row.
	 * @param lastRow
	 *            the index of the last row.
	 */
	private void fireTableRowsDeleted(int firstRow, int lastRow) {
		fireTableChanged(
				new TableModelEvent(this, firstRow, lastRow, TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE));
	}

	/**
	 * Sends a {@link TableModelEvent} to all registered listeners to inform
	 * them that some rows have been updated.
	 * 
	 * @param firstRow
	 *            the index of the first row.
	 * @param lastRow
	 *            the index of the last row.
	 */
	public void fireTableRowsUpdated(int firstRow, int lastRow) {
		fireTableChanged(
				new TableModelEvent(this, firstRow, lastRow, TableModelEvent.ALL_COLUMNS, TableModelEvent.UPDATE));
	}

	/**
	 * Sends the specified event to all registered listeners.
	 * 
	 * @param event
	 *            the event to send.
	 */
	public void fireTableChanged(TableModelEvent event) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		if (event == null)
			return;
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == TableModelListener.class) {
				((TableModelListener) listeners[i + 1]).tableChanged(event);
			}
		}
	}

	/**
	 * Sends a {@link TableModelEvent} to all registered listeners to inform
	 * them that a single cell has been updated.
	 * 
	 * @param row
	 *            the row index.
	 * @param column
	 *            the column index.
	 */
	public void fireTableCellUpdated(int row, int column) {
		fireTableChanged(new TableModelEvent(this, row, row, column));
	}

	/**
	 * Set when the table is editable. The editable columns are: category,
	 * access, user, url, display password.
	 * 
	 * @param b
	 *            true is editable.
	 */
	public void setEditable(boolean b) {
		this.isEditable = b;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.table.TableModel#addTableModelListener(javax.swing.event.
	 * TableModelListener)
	 */
	@Override
	public void addTableModelListener(TableModelListener listener) {
		listenerList.add(TableModelListener.class, listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		return getActiveSize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// return model.get(indexOf(rowIndex)).get(columnIndex);
		return getEntry(rowIndex).get(columnIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
	 */
	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		Access access = getEntry(rowIndex);
		try {
			document.getTraceModel().setTrace(access.getTraceID(columnIndex), (I_StyleObject) value,
					(I_StyleObject) getValueAt(rowIndex, columnIndex), "");

			// The original model stores the data.
			access.set(columnIndex, (I_StyleObject) value);
		} catch (ClassCastException e) {
			access.set(columnIndex, new StyleObject(value, ""));
		}
		this.setEntry(rowIndex, access);
		// System.out.println("AccessTableModel " + value);
		fireTableCellUpdated(rowIndex, columnIndex);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.table.TableModel#removeTableModelListener(javax.swing.event
	 * .TableModelListener)
	 */
	@Override
	public void removeTableModelListener(TableModelListener listener) {
		listenerList.remove(TableModelListener.class, listener);
	}

	// From IndexModel
	@Override
	public void performChange(DataChangeEvent e) {
		/*
		 * Listen to the itself and forward events to the table.
		 */
		switch (e.getEventType()) {
		case DELETE:
			fireTableRowsDeleted(e.getFrom(), e.getTo());
			break;
		case INSERT: // Add
			fireTableRowsInserted(e.getFrom(), e.getTo());
			break;
		case UPDATE:
			fireTableRowsUpdated(e.getFrom(), e.getTo());
			break;
		case REINDEX: // Sort & Filter
			fireTableDataChanged();
			break;
		default:
			// fireTableDataChanged();
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.model.I_Undoable#getStatus()
	 */
	@Override
	public I_Status<Index> getStatus() {
		return new ObjectStatus<Index>(this.dataIndex.clone(), "Access table model");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.model.I_Undoable#setStatus(net.catode.model.I_ObjectTrace
	 * )
	 */
	@Override
	public void setStatus(I_Status<?> s) {
		this.dataIndex = (Index) s.getStatus();
		// addDataChangeListener(this);
		fireTableDataChanged();
	}

	@Override
	public void clearAll() {
		accessArray.clear();
		clear();
	}

	@Override
	public AccessTableModel copy() {
		AccessTableModel acopy = new AccessTableModel(null);
		acopy.accessArray = accessArray;
		acopy.dataIndex = dataIndex.clone();
		acopy.removeDataChangeListener(acopy);
		return acopy;
	}

	@Override
	public Access setEntry(int index, Access item) {
		// return model.setAccess(indexOf(index), item);
		return accessArray.set(indexOf(index), item);
	}

	@Override
	protected Access innerGet(int index) {
		Access a = null;
		try {
			a = accessArray.get(index);
		} catch (IndexOutOfBoundsException e) {
		}
		return a;
	}

	@Override
	protected void innerAdd(Access rowData) {
		accessArray.add(rowData);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return document.getAccessFactory().getType(columnIndex);
	}

	@Override
	public int getColumnCount() {
		return document.getAccessFactory().getSize();
	}

	@Override
	public String getColumnName(int columnIndex) {
		// return model.getFieldLabel(columnIndex);
		AccessFactory factory = document.getAccessFactory();
		return factory.getLabel(document.getAccessFactory().getKey(columnIndex));
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		AccessFactory factory = document.getAccessFactory();
		return (//
		factory.getKey(columnIndex).equals(AccessFactory.CHANGE_BUTTON) //
				|| factory.getKey(columnIndex).equals(AccessFactory.PASSWORD_DISPLAY) //
				|| (factory.getKey(columnIndex).equals(AccessFactory.CATEGORY) && this.isEditable) //
				|| (factory.getKey(columnIndex).equals(AccessFactory.RATING) && this.isEditable) //
				|| (factory.getKey(columnIndex).equals(AccessFactory.EMAIL) && this.isEditable) //
				|| (factory.getKey(columnIndex).equals(AccessFactory.ACCESS) && this.isEditable) //
				|| (factory.getKey(columnIndex).equals(AccessFactory.USER) && this.isEditable) //
				|| (factory.getKey(columnIndex).equals(AccessFactory.EXPIRE_ON) && this.isEditable) //
				|| (factory.getKey(columnIndex).equals(AccessFactory.URL) && this.isEditable) //
		);
	}

	@Override
	public void accept(AccessVisitor visitor) {
		// Inspect the model - Global operations
		// System.out.println(this.getClass().getName() + " visiting model ");
		visitor.visitModel(this);
		// Inspect the model elements - local
		// System.out.println(this.getClass().getName() + " visiting access ");
		for (int i = 0; i < getRowCount(); i++) {
			visitor.visitAccess(getEntry(i));
		}
	}

}