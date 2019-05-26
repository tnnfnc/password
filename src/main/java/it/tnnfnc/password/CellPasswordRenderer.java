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

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

import it.tnnfnc.table.cell.RangedValue;

/**
 * Renders a table cell as a text string in a progress bar. The string is
 * displayed as long as the progress bar reaches its upper limit. A timer must
 * be provided.
 * 
 * @author Franco Toninato
 */
public class CellPasswordRenderer extends JProgressBar implements TableCellRenderer {
	private static final long serialVersionUID = 1L;
	private static final Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

	// private static final Color background = UIManager
	// .getColor("ProgressBar.foreground");

	/**
	 * Returns a progress bar renderer.
	 * 
	 * @return (methods only)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		if (table == null) {
			return this;
		}

		// if (this.isOpaque() == false)
		// setOpaque(true);

		setFont(table.getFont());
		setBorderPainted(false);
		if (!isStringPainted())
			setStringPainted(true);

		if (hasFocus) {
			Border b = null;
			if (isSelected)
				b = UIManager.getBorder("Table.focusSelectedCellHighlightBorder");
			if (b == null)
				b = UIManager.getBorder("Table.focusCellHighlightBorder");

		} else {
			setBorder(noFocusBorder);
		}

		// Color back = getBackground();
		// setOpaque(back != null && back.equals(table.getBackground()));

		setValue(value);

		return this;
	}

	/**
	 * Set the value from the table into the rendering component.
	 * 
	 * @param cell
	 *            the value.
	 */
	protected void setValue(Object cell) {
		if (cell instanceof RangedValue) {
			RangedValue rv = (RangedValue) cell;

			if (rv.getMax() != rv.getMin() && (rv.getMax() != getMaximum() || rv.getMin() != getMinimum())) {
				setMaximum(rv.getMax());
				setMinimum(rv.getMin());
			}
			setValue(rv.getGauge());
			setString(rv.getValue() == null ? "" : rv.getValue() + "");
		}
	}

	// /*
	// * (non-Javadoc)
	// *
	// * @see java.awt.Container#invalidate()
	// */
	// @Override
	// public void invalidate() {
	// }
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see javax.swing.JComponent#isOpaque()
	// */
	// @Override
	// public boolean isOpaque() {
	// return true;
	// }
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see java.awt.Component#repaint()
	// */
	// @Override
	// public void repaint() {
	// }
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see javax.swing.JComponent#repaint(long, int, int, int, int)
	// */
	// @Override
	// public void repaint(long tm, int x, int y, int width, int height) {
	// }
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see javax.swing.JComponent#repaint(java.awt.Rectangle)
	// */
	// @Override
	// public void repaint(Rectangle r) {
	// }
	//
	// @Override
	// public void revalidate() {
	// }
	//
	// @Override
	// public void validate() {
	// }
}
