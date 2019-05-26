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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.ListResourceBundle;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import it.tnnfnc.apps.application.document.I_IOBasicOperations;
import it.tnnfnc.apps.application.ui.AbstractDialog;
import it.tnnfnc.apps.application.ui.LocaleBundle;
import it.tnnfnc.apps.application.ui.Utility;
import it.tnnfnc.apps.resource.FileResource;
import it.tnnfnc.apps.resource.I_Resource;
import it.tnnfnc.security.SecurityObject;
import it.tnnfnc.security.xml.XMLCipherFileFilter;
import it.tnnfnc.security.xml.XMLCipherFileResource;
import it.tnnfnc.security.xml.XMLEncryptionInputStream;

/**
 * This class allows the user to choose a resource to input/output operations.
 * 
 * @author franco toninato
 * 
 */
@SuppressWarnings("serial")
public class ResourceSelectionPanel extends JPanel {
	private static final int MAX_FILE = 10;
	private static final String FILE = "file";
	// private static final String DIR = "local.dir";
	/**
	 * New resource action.
	 */
	private static String NEW_ACTION = "New";
	/**
	 * Open resource action.
	 */
	private static String OPEN_ACTION = "Open";
	/**
	 * Save resource action.
	 */
	private static String SAVE_ACTION = "Save";
	/**
	 * Save as resource action.
	 */
	private static String SAVEAS_ACTION = "SaveAs";
	/**
	 * Close resource action.
	 */
	private static String CLOSE_ACTION = "Close";
	/**
	 * exit resource action.
	 */
	private static String EXIT_ACTION = "Exit";
	/**
	 * Clear the resource history.
	 */
	private static String CLEAR_ACTION = "Clear";
	// Language
	private ListResourceBundle language = (ListResourceBundle) ResourceBundle.getBundle(LocaleBundle.class.getName());
	// Path
	private JTextField resourcePath;
	// External Application
	private I_IOBasicOperations application;
	// private FileFilter fileFilter;
	private File propertiesFile;
	// private file history;
	// private ArrayList<FileHistoryItem> fileHistory;
	private ArrayList<URL> fileHistory;
	// file history menu
	private JMenu menu;
	// Old resource
	private I_Resource resource;
	// Current resource
	private I_Resource tmp_resource;
	// File chooser
	private JFileChooser fileChooser = new JFileChooser();
	private int counter;

	/**
	 * Object constructor.
	 */
	public ResourceSelectionPanel(I_IOBasicOperations a) {
		this.application = a;
		createGUI();
	}

	/**
	 * Test the class.
	 * 
	 * @param args
	 *            no args needed.
	 */
	public static void main(String[] args) {
		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				// Create and set up the window.
				JFrame frame = new JFrame("Resource selection panel");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				// Create and set up the content pane.
				ResourceSelectionPanel demo = new ResourceSelectionPanel(new I_IOBasicOperations() {

					@Override
					public void create(I_Resource r) throws IOException {

					}

					@Override
					public void open(I_Resource r) throws IOException {

					}

					@Override
					public void save(I_Resource r) throws IOException {

					}

					@Override
					public void saveAs(I_Resource r) throws IOException {

					}

					@Override
					public void close(I_Resource r) throws IOException {

					}

					@Override
					public void exit() throws IOException {

					}
				});
				frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.LINE_AXIS));
				frame.getContentPane().add(demo);
				// Display the window.
				frame.pack();
				frame.setVisible(true);
			}
		});
	}

	/**
	 * 
	 */
	private void exitApplication() {
		if (closeResource()) {
			updateResourceName(resource);
			saveFileHistory();
			exit(resource);
		}
	}

	/**
	 * Ask to save before closing the resource.
	 */
	private boolean closeResource() {
		boolean result = false;
		if (isOpen(resource)) {
			// ask to save? (Y) save and close; (N) close; (-) Stop;
			int option = askToSave(resource);
			switch (option) {
			case JOptionPane.NO_OPTION:
				resource = close(resource);
				tmp_resource = null;
				resource = null;
				updateResourceName(resource);
				result = true;
				break;
			case JOptionPane.YES_OPTION:
				if (saveResource()) {
					resource = close(resource);
					result = true;
					tmp_resource = null;
					resource = null;
					updateResourceName(resource);
				} else {
					result = false;
				}
				break;
			case JOptionPane.CANCEL_OPTION:
				result = false;
				break;
			}
		} else {
			result = true;
		}
		return result;
	}

	/**
	 * 
	 */
	private void saveAsResource() {
		if (isOpen(resource)) {
			// If you stop before ending the resource stays open
			tmp_resource = chooseSaveDialog();
			if (tmp_resource != null && askToOverwrite(tmp_resource)) {
				tmp_resource = saveAs(tmp_resource);
				if (tmp_resource != null) {
					resource = tmp_resource;
					tmp_resource = null;
				}
				updateResourceName(resource);
				updateFileHistory(resource);
			} else {
				updateResourceName(resource);
				updateFileHistory(resource);
			}
		}
	}

	/**
	 * Save a resource: if it's new ask to overwrite. Return true when saved.
	 */
	private boolean saveResource() {
		boolean result = false;
		if (isOpen(tmp_resource)) {
			// If you stop before ending the resource stays open
			tmp_resource = chooseSaveDialog();
			if (tmp_resource != null && askToOverwrite(tmp_resource)) {
				tmp_resource = saveAs(tmp_resource);
				if (tmp_resource != null) {
					resource = tmp_resource;
					tmp_resource = null;
					updateFileHistory(resource);
					result = true;
				}
				updateResourceName(resource);
			} else {
				result = false;
				updateFileHistory(resource);
				updateResourceName(resource);
			}
		} else if (isOpen(resource)) {
			resource = save(resource);
			updateFileHistory(resource);
			result = true;
		}
		return result;
	}

	/**
	 * 
	 */
	private void openExistingResource() {
		if (isOpen(resource)) {
			// ask to save? (Y) save and close; (N) close; (-) Stop;
			int option = askToSave(resource);
			switch (option) {
			case JOptionPane.NO_OPTION:
				resource = close(resource);
				if (resource != null) {
					resource = chooseOpenDialog(resource);
					resource = open(resource);
					updateResourceName(resource);
					updateFileHistory(resource);
				}
				break;
			case JOptionPane.YES_OPTION:
				resource = save(resource);
				resource = close(resource);
				resource = chooseOpenDialog(resource);
				resource = open(resource);
				updateResourceName(resource);
				updateFileHistory(resource);
				break;
			case JOptionPane.CANCEL_OPTION:
				break;
			}
		} else {
			resource = chooseOpenDialog(resource);
			if (resource != null) {
				resource = open(resource);
				updateResourceName(resource);
				updateFileHistory(resource);
			}
		}
	}

	/**
	 * 
	 */
	private void createNewResource() {
		if (closeResource()) {
			resource = create(resource);
			tmp_resource = resource;//
			updateResourceName(resource);
		}
	}

	/**
	 * Initialize the local variables, it is called by the constructor.
	 */
	private void createGUI() {

		fileChooser.addChoosableFileFilter(new XMLCipherFileFilter());

		resourcePath = new JTextField(20);// "Resource Location");
		resourcePath.setEditable(false);

		JButton newButton = new JButton(language.getString(NEW_ACTION));
		newButton.setToolTipText(language.getString("tt-new"));
		JButton openButton = new JButton(language.getString(OPEN_ACTION));
		openButton.setToolTipText(language.getString("tt-open"));
		JButton saveButton = new JButton(language.getString(SAVE_ACTION));
		saveButton.setToolTipText(language.getString("tt-save"));
		JButton saveAsButton = new JButton(language.getString(SAVEAS_ACTION));
		saveAsButton.setToolTipText(language.getString("tt-saveas"));
		JButton closeButton = new JButton(language.getString(CLOSE_ACTION));
		closeButton.setToolTipText(language.getString("tt-close"));
		JButton exitButton = new JButton(language.getString(EXIT_ACTION));
		exitButton.setToolTipText(language.getString("tt-exit"));
		// Action commands
		newButton.setActionCommand(NEW_ACTION);
		openButton.setActionCommand(OPEN_ACTION);
		saveButton.setActionCommand(SAVE_ACTION);
		saveAsButton.setActionCommand(SAVEAS_ACTION);
		closeButton.setActionCommand(CLOSE_ACTION);
		exitButton.setActionCommand(EXIT_ACTION);

		// Buttons event listeners
		ButtonListener internalListener = new ButtonListener();
		newButton.addActionListener(internalListener);
		openButton.addActionListener(internalListener);
		saveButton.addActionListener(internalListener);
		saveAsButton.addActionListener(internalListener);
		closeButton.addActionListener(internalListener);
		exitButton.addActionListener(internalListener);

		String filePath = System.getProperty("user.home") + "/"
				+ (application == null ? "_" : application.getClass().getName());
		propertiesFile = new File(filePath);

		// File history
		fileHistory = new ArrayList<URL>();

		// Menu recent file history
		menu = new JMenu(language.getString("Recent resources"));
		loadFileHistory();
		JMenuBar mb = new JMenuBar();
		mb.add(menu);
		// Buttons grid row one
		// this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		add(mb);
		add(Box.createHorizontalGlue());
		add(openButton);
		// openButton.setAlignmentY(BOTTOM_ALIGNMENT);
		add(newButton);
		add(saveButton);
		add(saveAsButton);
		add(closeButton);
		add(exitButton);
		add(resourcePath);

	}

	/**
	 * Get the selected resource.
	 * 
	 * @return the selected resource.
	 */
	public void addFileFilter(FileFilter filter) {
		fileChooser.addChoosableFileFilter(filter);
	}

	protected I_Resource create(I_Resource r) {
		String fileName = null;
		try {
			File file = new File(fileName = fileChooser.getFileSystemView().getDefaultDirectory().getPath()
					+ System.getProperty("file.separator") + defaultFileName());
			fileChooser.setSelectedFile(file);
			r = new FileResource(file);
			application.create(r);
			return r;
		} catch (Exception e) {
			JOptionPane.showConfirmDialog(this, language.getString("Unable create a new document") + e.getMessage(),
					fileName, JOptionPane.CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
			// e.printStackTrace();
			return null;
		}
	}

	protected I_Resource open(I_Resource r) {
		// System.out.println("open " + r.getURL());
		try {
			application.open(r);
			return r;
		} catch (IOException e) {
			String m = new String();
			m = r == null ? m : r.getURL().getPath();
			JOptionPane.showConfirmDialog(this, language.getString("Unable to open") + e.getMessage(), m,
					JOptionPane.CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
			// e.printStackTrace();
			return null;
		}
	}

	protected I_Resource save(I_Resource r) {
		// System.out.println("save " + r.getURL());
		try {
			application.save(r);
			return r;
		} catch (IOException e) {
			JOptionPane.showConfirmDialog(this, language.getString("Unable to save") + e.getMessage(),
					r.getURL().getPath(), JOptionPane.CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}

	protected I_Resource saveAs(I_Resource r) {
		// System.out.println("save as " + r.getURL());
		try {
			application.saveAs(r);
			return r;
		} catch (IOException e) {
			JOptionPane.showConfirmDialog(this, language.getString("Unable to save") + e.getMessage(),
					r.getURL().getPath(), JOptionPane.CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}

	protected I_Resource close(I_Resource r) {
		// r = null; Ineffective!
		// System.out.println("close " + r.getURL());
		try {
			application.close(r);
			return r;
		} catch (IOException e) {
			JOptionPane.showConfirmDialog(this, language.getString("Application exception") + e.getMessage(),
					r.getURL().getPath(), JOptionPane.CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}

	protected void exit(I_Resource r) {
		// System.out.println("exit ");
		try {
			application.exit();
		} catch (IOException e) {
			// e.printStackTrace();
		}
	}

	/*
	 * Get the extension of a file.
	 */
	private static String getFileExtension(File f) {
		String ext = "";
		String s = f.getName();
		int i = s.lastIndexOf('.');

		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}

	/**
	 * @throws MalformedURLException
	 * 
	 */
	private void updateResourceName(I_Resource r) {
		resourcePath.setText(r == null ? "" : r.getURL().toString());
	}

	private String defaultFileName() {
		return FILE + "_" + counter++;
	}

	private I_Resource openHistoryResource(URL url) {
		if (url.getProtocol().equalsIgnoreCase("FILE")) {
			try {
				return resourceOpen(new File(url.toURI().getPath()));
			} catch (URISyntaxException e) {
				// e.printStackTrace();
				return null;
			}
		}
		return null;
	}

	private I_Resource chooseSaveDialog() {
		I_Resource r = null;
		int returnVal = fileChooser.showSaveDialog(this);
		switch (returnVal) {
		case JFileChooser.APPROVE_OPTION:
			File f = fileAutoExtension(fileChooser);
			fileChooser.setSelectedFile(f);
			if (getFileExtension(f).equalsIgnoreCase(XMLCipherFileFilter.FILE_EXT)) {
				Properties p = new Properties();
				BlockCipherChooser cipherChooser = new BlockCipherChooser(p, BlockCipherChooser.NEW_SECURITY);
				AbstractDialog.Options opt = cipherChooser.showDialog(this);
				if (opt.equals(AbstractDialog.Options.OK_OPTION)) {
					System.out.println(this.getClass().getName() + " ok, properties = " + p);
					SecurityObject securityObject = cipherChooser.getSecurityObject();
					r = new XMLCipherFileResource(f, securityObject, p);
				} else {
					return chooseSaveDialog();
				}
			} else {
				r = new FileResource(f);
			}
			break;
		case JFileChooser.CANCEL_OPTION:
			r = null;
			break;
		case JFileChooser.ERROR_OPTION:
			r = null;
			break;
		default:
			r = null;
			break;
		}
		return r;
	}

	private File fileAutoExtension(JFileChooser fc) {
		File f = fc.getSelectedFile();
		if (fc.getFileFilter().getClass().equals(XMLCipherFileFilter.class)) {
			return setFileExtension(f, XMLCipherFileFilter.FILE_EXT);
		}
		return f;

	}

	private File setFileExtension(File f, String fileExt) {
		if (getFileExtension(f).equalsIgnoreCase(fileExt)) {
			return f;
		} else {
			String path = f.getPath();
			int i = path.lastIndexOf('.');

			if (i > 0 && i < path.length() - 1) {
				path = path.substring(0, i) + "." + fileExt;
			} else if (i < 0) {
				path = path + "." + fileExt;
			}
			return new File(path);
		}
	}

	private boolean isOpen(I_Resource r) {
		return r != null;
	}

	private I_Resource chooseOpenDialog(I_Resource r) {
		int returnVal = fileChooser.showOpenDialog(this);
		switch (returnVal) {
		case JFileChooser.APPROVE_OPTION:
			return resourceOpen(fileChooser.getSelectedFile());
		case JFileChooser.CANCEL_OPTION:
			break;
		case JFileChooser.ERROR_OPTION:
			break;
		default:
			break;
		}
		return null;
	}

	// private I_Resource ifEncryptCheck(File f) {
	// if (getFileExtension(f).equalsIgnoreCase(
	// XMLEncryptedFileFilter.FILE_EXT)) {
	// Properties p = new Properties();
	// BlockCipherChooser cipherChooser = new BlockCipherChooser(p,
	// BlockCipherChooser.NEW_SECURITY);
	// AbstractDialog.Options opt = cipherChooser.showDialog(this);
	// if (opt.equals(AbstractDialog.Options.OK_OPTION)) {
	// SecurityObject securityObject = cipherChooser
	// .getSecurityObject();
	// return new XMLCipherFileResource(f, securityObject, p);
	// } else {
	// cipherChooser.dispose();
	// return null;
	// }
	// }
	// return new FileResource(f);
	// }

	private I_Resource resourceOpen(File f) {
		if (getFileExtension(f).equalsIgnoreCase(XMLCipherFileFilter.FILE_EXT)) {
			try {
				// Get the parameters
				XMLEncryptionInputStream xmlEncriptedStream;
				xmlEncriptedStream = new XMLEncryptionInputStream(new FileInputStream(f));
				Properties p = xmlEncriptedStream.getProperties();
				xmlEncriptedStream.close();

				BlockCipherChooser cipherChooser = new BlockCipherChooser(p, BlockCipherChooser.OPEN_SECURITY);
				AbstractDialog.Options opt = cipherChooser.showDialog(this);
				if (opt.equals(AbstractDialog.Options.OK_OPTION)) {
					SecurityObject securityObject = cipherChooser.getSecurityObject();
					return new XMLCipherFileResource(f, securityObject, p);
					// PopMessage.displayError(
					// ResourceSelectionPanel2.this,
					// language.getString("Security Exception"),
					// "\n" + e.getClass().getName() + ": "
					// + e.getMessage());
					// cipherChooser.dispose();
				} else {
					return null;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new FileResource(f);
	}

	// private boolean exists(I_Resource r) {
	// // return r != null && r.exist();
	// return r != null;
	// }

	/**
	 * @return -1 for cancel, 0 for go ahead without saving, 1 for save and go
	 *         ahead
	 */
	private int askToSave(I_Resource r) {
		return r.isChanged() ? JOptionPane.showConfirmDialog(this, language.getString("Do you want to save?"),
				r.getURL().getPath(), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE)
				: JOptionPane.NO_OPTION;
	}

	private boolean askToOverwrite(I_Resource r) {
		if (r != null && r.exists()) {
			return JOptionPane.showConfirmDialog(this, language.getString("Do you want to overwrite?"),
					r.getURL().getPath(), JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
		}
		return true;
	}

	/**
	 * Load local properties and add to the recent resources.
	 */
	private void loadFileHistory() {
		try {
			Properties lp = new Properties();
			lp.loadFromXML(new FileInputStream(propertiesFile));
			for (int i = 0; i < lp.size(); i++) {
				fileHistory.add(new URL(lp.getProperty(FILE + i)));
			}
		} catch (InvalidPropertiesFormatException e) {
			// e.printStackTrace();
		} catch (FileNotFoundException e) {
			// e.printStackTrace();
		} catch (IOException e) {
			// e.printStackTrace();
		}
		updateHistoryMenu();
	}

	private void saveFileHistory() {
		try {
			FileOutputStream os;
			os = new FileOutputStream(propertiesFile);
			Properties lp = new Properties();
			for (int i = 0; i < fileHistory.size(); i++) {
				// System.out.println("item " + FILE + i + " "
				// + fileHistory.get(i).toString());
				lp.setProperty(FILE + i, fileHistory.get(i).toString());
			}
			lp.storeToXML(os, Utility.timeStamp());
			os.close();
			// System.out.println("save history to " + propertiesFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void updateFileHistory(I_Resource r) {
		if (r != null && r.getURL() != null) {
			URL item;
			item = r.getURL();
			int i = fileHistory.indexOf(item);
			if (i < 0) {
				fileHistory.add(0, item);
				if (fileHistory.size() > MAX_FILE) {
					fileHistory.remove(MAX_FILE - 1);
				}
			} else {
				// move to the first position
				fileHistory.remove(i);
				fileHistory.add(0, item);
			}
			// Update the menu
			updateHistoryMenu();
		}
	}

	private void updateHistoryMenu() {
		int c = menu.getMenuComponentCount();
		for (int i = 0; i < c; i++) {
			menu.remove(0);
		}
		ActionListener l = new MenuListener();
		JMenuItem clear = new JMenuItem(language.getString("Clear"));
		clear.addActionListener(l);
		clear.setActionCommand(CLEAR_ACTION);
		menu.add(clear);
		menu.addSeparator();
		for (URL i : fileHistory) {
			JMenuItem menu_item = new JMenuItem(i.toString());
			menu_item.addActionListener(l);
			menu.add(menu_item);
		}
	}

	private void clearFileHistory() {
		fileHistory.clear();
		updateHistoryMenu();
	}

	/**
	 * Listen to the GUI buttons.
	 * 
	 */
	protected class MenuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals(CLEAR_ACTION)) {
				clearFileHistory();
			}
			// Open history
			else {
				JMenuItem item = (JMenuItem) e.getSource();
				URL url = null;
				for (int i = 0; i < fileHistory.size(); i++) {
					if (fileHistory.get(i).toString().equals(item.getActionCommand())) {
						url = fileHistory.get(i);
						break;
					}
				}
				if (url != null) {
					if (closeResource()) {
						tmp_resource = openHistoryResource(url);
						resource = open(tmp_resource);
						tmp_resource = null;
						updateResourceName(resource);
						updateFileHistory(resource);
						// System.out.println(resource.getURL());
						// System.out.println(tmp_resource.getURL());
					}
				}
			}
		}
	}

	/**
	 * Listen to the GUI buttons.
	 * 
	 */
	protected class ButtonListener implements ActionListener {
		/*
		 * Input: current / null Output: new / current
		 * 
		 * Input
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals(NEW_ACTION)) {
				createNewResource();
			} else if (e.getActionCommand().equals(OPEN_ACTION)) {
				openExistingResource();
			} else if (e.getActionCommand().equals(SAVE_ACTION)) {
				saveResource();
			} else if (e.getActionCommand().equals(SAVEAS_ACTION)) {
				saveAsResource();
			} else if (e.getActionCommand().equals(CLOSE_ACTION)) {
				closeResource();
			} else if (e.getActionCommand().equals(EXIT_ACTION)) {
				exitApplication();
			} else if (e.getActionCommand().equals(CLEAR_ACTION)) {
				clearFileHistory();
			}
		}
	}
}
