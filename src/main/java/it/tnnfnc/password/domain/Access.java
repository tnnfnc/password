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

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import it.tnnfnc.apps.application.ui.style.I_StyleObject;
import it.tnnfnc.apps.application.undo.I_TracedStatus;
import it.tnnfnc.datamodel.I_Visitable;
import it.tnnfnc.encoders.Base64;
import it.tnnfnc.encoders.IdentityEncoder;
import it.tnnfnc.password.command.AccessVisitor;
import it.tnnfnc.table.cell.RangedValue;
import it.tnnfnc.table.row.I_TableRow;

//import net.catode.table.style.I_StyleObject;

/**
 * Access.
 * 
 * @author Franco Toninato
 * 
 */
public class Access implements I_TableRow<I_StyleObject>, I_TracedStatus, I_Visitable<AccessVisitor>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private I_StyleObject array[] = new I_StyleObject[0];
	private Object id;
	private final AccessFactory factory;
	private int trace_id = this.hashCode();

	/**
	 * The constructor is restricted to the its factory class.
	 */
	public Access(AccessFactory f) {
		factory = f;
	}

	/**
	 * Deep clones this access.
	 * 
	 * @return the cloned object or null when the cloning fails.
	 * 
	 * @throws CloneNotSupportedException
	 *             when the cloning fails.
	 * @throws IllegalArgumentException
	 */
	public Access copy() throws CloneNotSupportedException, IllegalArgumentException {
		Access a = factory.newRow();
		for (int i = 0; i < size(); i++) {
			Object key = factory.getKey(i);
			a.setValue(key, factory.serialize(factory.getType(i).getName(), this.getValue(key)));
			a.get(i).setStyle(get(i).getStyle());

			// a.set(i, new StyleObject(//
			// factory.deSerialize(factory.getType(i).getName(),
			// factory.serialize(factory.getType(i).getName(),
			// get(i).getValue())), //
			// get(i).getStyle()));
		}
		a.setIdentifier(getIdentifier());
		a.trace_id = trace_id;

		return a;
	}

	/**
	 * Get this access factory.
	 * 
	 * @return the access factory.
	 */
	public AccessFactory getFactory() {
		return factory;
	}

	/**
	 * Get the password length.
	 * 
	 * @return the password length.
	 */
	public int getPasswordLength() {
		return Integer.parseInt(getValue(AccessFactory.PWD_LENGTH) + "");
	}

	/**
	 * Get the user defined password.
	 * 
	 * @return the user defined password.
	 */
	public String getUserPassword() {
		return getValue(AccessFactory.PASSWORD) + "";
	}

	/**
	 * Set the user defined password.
	 * 
	 * @param p
	 *            the user defined password.
	 */
	public void setUserPassword(String p) {
		setValue(AccessFactory.PASSWORD, p);
		setValue(AccessFactory.GENERATE, false + "");
		// set Password Type
		setValue(AccessFactory.PASSWORD_TYPE, IdentityEncoder.algorithm);
		// set last Changed
		get(AccessFactory.CHANGED_ON).setObject(new Date());
	}

	/**
	 * Get the salt.
	 * 
	 * @return the access salt or an empty array.
	 */
	public byte[] getSalt() throws IllegalArgumentException {
		return Base64.decode(getValue(AccessFactory.SALT).toString().getBytes());
	}

	/**
	 * Set the temporary password.
	 * 
	 * @param period
	 * @param duration
	 * @param p
	 *            the password.
	 */
	public void displayPassword(int period, int duration, char[] p) {
		RangedValue rv = (RangedValue) getValue(AccessFactory.PASSWORD_DISPLAY);
		rv.setMax(duration);
		rv.setMin(0);
		rv.setStep(duration / period);

		if (p != null) {
			rv.setValue(new String(p));
		} else {
			rv.setValue(new String(""));
		}
	}

	/**
	 * Set the salt for password generation.
	 * 
	 * @param s
	 *            the salt.
	 * @throws IllegalArgumentException
	 */
	public void setSalt(byte[] s) throws IllegalArgumentException {
		setValue(AccessFactory.SALT, new String(Base64.encode(s)));
	}

	/**
	 * Change the access expiration date at password changing.
	 */
	public void updateExpirationDate() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, ((Integer) get(AccessFactory.VALIDITY).getValue()));
		get(AccessFactory.EXPIRE_ON).setObject(c.getTime());
	}

	/**
	 * Update the statistic field every time a password is used.
	 */
	public void updateStatistics() {
		get(AccessFactory.STATISTICS)
				.setObject(new Integer(((Integer) get(AccessFactory.STATISTICS).getValue()).intValue() + 1));
	}

	/**
	 * Return true if the password is defined.
	 * 
	 * @return true if the password is defined.
	 * @throws IllegalArgumentException
	 */
	public boolean isPasswordDefined() throws IllegalArgumentException {
		return (((Boolean) getValue(AccessFactory.GENERATE)).booleanValue() && getSalt().length > 0
				|| getUserPassword().length() > 0);
	}

	/**
	 * Return true if the password is generated.
	 * 
	 * @return true if the password is generated.
	 */
	public boolean isPasswordGenerated() {
		return (((Boolean) getValue(AccessFactory.GENERATE)).booleanValue());
	}

	/**
	 * Clear the password.
	 * 
	 */
	public void clearPassword() {
		setValue(AccessFactory.SALT, new String(""));
		setValue(AccessFactory.PASSWORD, new String(""));
	}

	/**
	 * Get the inner value at the field identifier.
	 * 
	 * @param key
	 *            the field identifier.
	 * @return the value at the field identifier.
	 */
	public Object getValue(Object key) {
		return get(key).getValue();
	}

	/**
	 * Set the inner value at the field identifier converting it from a string.
	 * 
	 * @param key
	 *            the field identifier.
	 * @param value
	 *            the new value as a string.
	 * @return the value at the field identifier.
	 */
	public Object setValue(Object key, String value) {
		int index = getFactory().getIndex(key);
		Object e = getFactory().deSerialize(getFactory().getType(getFactory().getIndex(key)).getName(), value);
		return get(index).setObject(e);
	}

	@Override
	public Object getTraceID(Object key) {
		return (trace_id + "" + key);
	}

	@Override
	public int columnCount() {
		return factory.getSize();
	}

	@Override
	public I_StyleObject get(int col) {
		return array[col];
	}

	@Override
	public Object getIdentifier() {
		return this.id;
	}

	@Override
	public I_StyleObject set(int col, I_StyleObject value) {
		I_StyleObject oldValue = null;
		if (col > -1 && col < size()) {
			oldValue = array[col];
			array[col] = value;
		} else {
			I_StyleObject o[] = new I_StyleObject[col + 1];
			System.arraycopy(array, 0, o, 0, array.length);
			o[col] = value;
			array = o;
		}
		return oldValue;
	}

	@Override
	public void setIdentifier(Object id) {
		this.id = id;
	}

	@Override
	public I_StyleObject[] toArray() {
		return array;
	}

	@Override
	public I_StyleObject get(Object key) {
		return array[factory.getIndex(key)];
	}

	@Override
	public I_StyleObject set(Object key, I_StyleObject value) {
		// search for the key if existing, or creates a new one
		int f_id = factory.getIndex(key);
		if (f_id == -1) {
			f_id = factory.addField(key, value.getClass(), null, null);
		}
		return set(f_id, value);
	}

	@Override
	public void accept(AccessVisitor visitor) {
		visitor.visitAccess(this);
	}

	@Override
	public int size() {
		return array.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer s = new StringBuffer(this.getClass().getName());
		s.append(" {");
		for (int i = 0; i < array.length; i++) {
			s.append(factory.getKey(i) + ": " + get(i) + "; ");
		}
		s.append("}");
		return s.toString();
	}
}