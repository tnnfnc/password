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

import java.io.File;
import java.util.ListResourceBundle;

import javax.swing.filechooser.FileFilter;

/**
 * File filter.
 * 
 * @author Franco Toninato
 * 
 */
public class PasswordFileFilter extends FileFilter {
	ListResourceBundle localization;
	/**
	 * Access file extension.
	 */
	public final static String FILE_EXTENSION = "spl";

	public PasswordFileFilter(ListResourceBundle resource) {
		localization = resource;
	}

	@Override
	public boolean accept(File f) {
		String ext = getExtension(f);
		return (ext == null || ext.equalsIgnoreCase(FILE_EXTENSION));
	}

	@Override
	public String getDescription() {
		return "." + FILE_EXTENSION + " "
				+ localization.getString("Password file");
	}

	/*
	 * Get the extension of a file.
	 */
	private static String getExtension(File f) {
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');

		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}

}
