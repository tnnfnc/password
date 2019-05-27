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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.DigestException;
import java.security.InvalidKeyException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileFilter;

import it.tnnfnc.apps.application.AbstractApplication;
import it.tnnfnc.apps.application.PeriodicTask;
import it.tnnfnc.apps.application.SwingPeriodicTask;
import it.tnnfnc.apps.application.document.DocumentChangeEvent;
import it.tnnfnc.apps.application.document.I_DocumentChangeListener;
import it.tnnfnc.apps.application.properties.PropertiesPanel;
import it.tnnfnc.apps.application.ui.AbstractDialog;
import it.tnnfnc.apps.application.ui.ApplicationClipboard;
import it.tnnfnc.apps.application.ui.LicensePanel;
import it.tnnfnc.apps.application.ui.LoginDialog;
import it.tnnfnc.apps.application.ui.LoginPanel;
import it.tnnfnc.apps.application.ui.LogsPanel;
import it.tnnfnc.apps.application.ui.PopMessage;
import it.tnnfnc.apps.application.ui.TabPanelEditor;
import it.tnnfnc.apps.application.ui.TabbedPane;
import it.tnnfnc.apps.application.ui.TabbedPaneProperties;
import it.tnnfnc.apps.application.ui.Utility;
import it.tnnfnc.apps.application.ui.style.I_StyleObject;
import it.tnnfnc.apps.application.ui.style.StyleObject;
import it.tnnfnc.apps.application.undo.I_Status;
import it.tnnfnc.apps.application.undo.ObjectStatus;
import it.tnnfnc.apps.resource.I_Resource;
import it.tnnfnc.datamodel.search.AbstractRegexSearch;
import it.tnnfnc.datamodel.search.RegexHtmlStyler;
import it.tnnfnc.datamodel.search.RegexMatcher;
import it.tnnfnc.datamodel.search.SearchPanel;
import it.tnnfnc.encoders.Base64;
import it.tnnfnc.encoders.BigIntegerEncoder;
import it.tnnfnc.encoders.EncodingAlgorithmFactory;
import it.tnnfnc.encoders.IdentityEncoder;
import it.tnnfnc.encoders.LongIntegerEncoder;
import it.tnnfnc.password.command.AccessVisitor;
import it.tnnfnc.password.command.VersionAccessVisitor;
import it.tnnfnc.password.command.ChangeAccessVisitor;
import it.tnnfnc.password.command.ChangeCategoryVisitor;
import it.tnnfnc.password.command.ChangeSecurityVisitor;
import it.tnnfnc.password.command.ExportToSpreadsheetVisitor;
import it.tnnfnc.password.command.InitAccessVisitor;
import it.tnnfnc.password.document.AccessDocument;
import it.tnnfnc.password.domain.Access;
import it.tnnfnc.password.domain.AccessFactory;
import it.tnnfnc.password.domain.AccessTableModel;
import it.tnnfnc.security.EncryptionPreferences;
import it.tnnfnc.security.crypto.I_BlockCipherStream;
import it.tnnfnc.table.HyperLinkTableListener;
import it.tnnfnc.table.StyleTable;
import it.tnnfnc.table.TableProperties;
import it.tnnfnc.table.cell.CellComboBoxEditor;
import it.tnnfnc.table.cell.RangedValue;
import it.tnnfnc.table.cell.StyleCellEditor;
import it.tnnfnc.table.cell.StyleCellRenderer;
import it.tnnfnc.table.command.CommandCellHistory;
import it.tnnfnc.table.command.CommandCellStyle;
import it.tnnfnc.table.command.CommandColumnChooser;
import it.tnnfnc.table.command.CommandDeleteRows;
import it.tnnfnc.table.command.CommandEditRow;
import it.tnnfnc.table.command.CommandInsertRows;
import it.tnnfnc.table.command.CommandMoveRowsDown;
import it.tnnfnc.table.command.CommandMoveRowsUp;
import it.tnnfnc.table.command.TableCommand;
import it.tnnfnc.table.header.TableHeader;

//import net.catode.password.AccessSecurityProvider;

/**
 * @author franco toninato
 * 
 */
public class Password extends AbstractApplication<AccessDocument> implements I_DocumentChangeListener {

	// The table view
	private StyleTable table;

	// Panels
	private PropertiesPanel generalSettingsPanel; // Properties
	private PropertiesPanel securitySettingsPanel; // Properties
	private ResourceSelectionPanel resourceSelectionPanel; //
	private PassPhrasePanel passphrasePanel; // Model
	private SearchPanel searchPanel; // Model
	private AccessEditPanel accessPanel;
	private JScrollPane tableScrollPane;
	private LogsPanel logsPanel;
	// Accesses categories
	private TabbedPane categoryTabs;
	private DefaultComboBoxModel<String> categoryModel;
	// Actions
	private Hashtable<String, Action> userCommands = new Hashtable<String, Action>();
	private JToggleButton editCommand;
	// Controls
	private AccessVisitor initAccessVisitor;
	private AccessVisitor changeAccessVisitor;

	// Security settings
	private AccessSecurityObject securityObject;
	// private ByteToCharEncodersFactory passwordGeneratorFactory;
	private PasswordGenerator passwordGenerator;

	// Context menu
	private JPopupMenu tablePopupMenu;
	// Ready when password is defined
	private boolean ready;
	// Manage greater than 3 for the same file
	private int error_attempt_counter = 0;

	private TitledBorder BORDER_ACTIVE;

	private TitledBorder BORDER_NOTACTIVE;

	/**
	 * Constructor.
	 */
	public Password() {
		initialise();
		createGUI();
		showGUI();
		// changeLookAndFeel("Nimbus");
		// changeLookAndFeel("Metal");
	}

	/**
	 * Launch the application.
	 * 
	 * @param args no arguments needed.
	 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				new Password();
			}
		});
	}

	/**
	 * Initialize local variables. Called by constructors.
	 */
	protected void initialise() {
		setVersion("2.2.2.1"); // version = "2.2.2";// + "- test";
		// Resource bundle
		setLocalization((ListResourceBundle) ResourceBundle.getBundle(this.bundleName + "Bundle", Locale.getDefault()));

		// Default properties
		properties = PasswordConstants.getDefaultProperties();
		// Program version
		properties.setProperty(AbstractApplication.VERSION_KEY, getVersion());
		// Create and set up the window.
		frame = new JFrame(getLocalization().getString("application") + " " + getVersion());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//
		table = new StyleTable();
		table.addMouseListener(new TableMouseLister());
		table.setToolTipText(getLocalization().getString("tt-access table"));
		initAccessVisitor = new InitAccessVisitor(this);
		changeAccessVisitor = new ChangeAccessVisitor(this);
		resourceSelectionPanel = new ResourceSelectionPanel(this);
		searchPanel = new SearchPanel();
		categoryModel = new DefaultComboBoxModel<String>();
		logsPanel = new LogsPanel();

		EncodingAlgorithmFactory eaf = new EncodingAlgorithmFactory(BigIntegerEncoder.class.getName(),
				LongIntegerEncoder.class.getName(), IdentityEncoder.class.getName());
		passwordGenerator = new PasswordGenerator(eaf);

		checkSecurity();

	}

	private void checkSecurity() {
		{
			logsPanel.appendLog("No security manager was installed!", PasswordConstants.getRedFormat());
			return;
		}
		// SecurityManager securityManager = System.getSecurityManager();
		// System.out.println("SecurityManager step 1 " + securityManager);
		// if (securityManager != null) {
		// String file = "pippo";
		// try {
		// securityManager.checkRead(file);
		// } catch (SecurityException err) {
		// }
		// try {
		// securityManager.checkWrite(file);
		// } catch (SecurityException err) {
		// }
		// } else {
		// securityManager = new SecurityManager();
		// System.setSecurityManager(securityManager);
		// System.out.println("SecurityManager step 2 " + securityManager);
		// }

		/*
		 * File, Socket, Net, Security, Runtime, Property, AWT, Reflect, and
		 * Serializable; java.io.FilePermission, java.net.SocketPermission,
		 * java.net.NetPermission, java.security.SecurityPermission,
		 * java.lang.RuntimePermission, java.util.PropertyPermission,
		 * java.awt.AWTPermission, java.lang.reflect.ReflectPermission, and
		 * java.io.SerializablePermission.
		 */
	}

	/**
	 * Create the GUI
	 */
	private void createGUI() {
		Container container = frame.getContentPane();
		container.setLayout(new BorderLayout());

		// Resource
		resourceSelectionPanel.setBorder(createTitledBorder(getLocalization().getString("Resource")));
		// Pass-phrase
		passphrasePanel = new PassPhrasePanel(this, 20);
		JPanel firstPanel = new JPanel(new BorderLayout());
		firstPanel.add(passphrasePanel, BorderLayout.PAGE_START);
		// firstPanel.add(logsPanel, BorderLayout.PAGE_END);

		// Main commands
		JPanel commandsPanel = new JPanel(new BorderLayout());
		commandsPanel.add(resourceSelectionPanel, BorderLayout.PAGE_START);
		commandsPanel.add(firstPanel, BorderLayout.PAGE_END);

		// Program Properties
		JPanel propertiesPanel = createPropertiesPanel();

		// Security Properties
		JPanel securityPanel = createSecurityPanel();

		// Access Table panel
		JPanel tablePanel = new JPanel(new BorderLayout());
		tablePanel.add(createToolBar(), BorderLayout.PAGE_START);
		tablePanel.add(createTabsPanel(), BorderLayout.CENTER);
		// tablePanel.add(firstPanel, BorderLayout.PAGE_END);

		// Jtabbedpane
		JTabbedPane tabbedPanel = new JTabbedPane();
		// tabbedPanel.addTab(getLocalization().getString("Step 1. Set a pass
		// phrase"), firstPanel);
		tabbedPanel.addTab(getLocalization().getString("Manage the accesses"), tablePanel);
		tabbedPanel.addTab(getLocalization().getString("Log records"), logsPanel);
		tabbedPanel.addTab("Properties", propertiesPanel);
		tabbedPanel.addTab("Security", securityPanel);
		tabbedPanel.addTab(getLocalization().getString(PasswordConstants.LICENSE),
				new LicensePanel(LicensePanel.GNU_GENERAL_HEADER));

		container.add(commandsPanel, BorderLayout.PAGE_START);
		container.add(tabbedPanel, BorderLayout.CENTER);

		// Create status bar
		BORDER_NOTACTIVE = new TitledBorder(new MatteBorder(6, 0, 0, 0, Color.red), getLocalization().getString("off"),
				TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, null, Color.red);

		// PopupMenu
		createPopUpMenu();

		// Initialize gui
		passphrasePanel.setEnabled(false);
		generalSettingsPanel.setEnabled(false);
		securitySettingsPanel.setEnabled(false);
		searchPanel.setEnabled(false);
		editCommand.setEnabled(false);
		setReady(false);

		uninstallCommands();
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be invoked
	 * from the event dispatch thread.
	 */
	private void showGUI() {
		frame.pack();
		Dimension minsize = frame.getSize();
		minsize.height = frame.getMinimumSize().height;
		frame.setMinimumSize(minsize);
		frame.setVisible(true);
	}

	// private JPanel createEditPanel() {
	// JPanel p = new JPanel();
	// FieldLayout layout = new FieldLayout(p);
	// Action editAction = new CommandEdit(getLocalization().getString(
	// "Edit off"));
	// editCommand = new JToggleButton(editAction);
	// editCommand.setToolTipText(getLocalization().getString("tt-edit mode"));
	// // userCommands.put(EditCommand.ACTION_COMMAND, editAction);
	// layout.append(0, 1, null, editCommand);
	// return p;
	// }

	/**
	 * Create the properties panel. Security settings and general settings.
	 * 
	 * @return the properties panel.
	 */
	private JPanel createPropertiesPanel() {
		generalSettingsPanel = new PropertiesPanel();
		generalSettingsPanel.addPanel(new SettingsPanel(this));
		Action changeAction = new CommandChangeProperties(
				getLocalization().getString(CommandChangeProperties.ACTION_COMMAND));
		Action cancelAction = new CommandCancelProperties(
				getLocalization().getString(CommandCancelProperties.ACTION_COMMAND));
		generalSettingsPanel.addButton(changeAction);
		generalSettingsPanel.addButton(cancelAction);
		userCommands.put(CommandChangeProperties.ACTION_COMMAND, changeAction);
		userCommands.put(CommandCancelProperties.ACTION_COMMAND, cancelAction);
		return generalSettingsPanel;
	}

	/**
	 * Create the properties panel. Security settings and general settings.
	 * 
	 * @return the properties panel.
	 */
	private JPanel createSecurityPanel() {
		securitySettingsPanel = new PropertiesPanel();
		securitySettingsPanel.addPanel(new PasswordBaseKeyPanel(properties));
		securitySettingsPanel.addPanel(new CipherStreamPanel(properties));
		Action changeAction = new CommandChangeSecurityProperties(
				getLocalization().getString(CommandChangeProperties.ACTION_COMMAND));
		Action cancelAction = new CommandCancelSecurityProperties(
				getLocalization().getString(CommandCancelProperties.ACTION_COMMAND));
		securitySettingsPanel.addButton(changeAction);
		securitySettingsPanel.addButton(cancelAction);
		userCommands.put(CommandChangeSecurityProperties.ACTION_COMMAND, changeAction);
		userCommands.put(CommandCancelSecurityProperties.ACTION_COMMAND, cancelAction);
		return securitySettingsPanel;
	}

	/**
	 * Creates the button panel.
	 * 
	 * @return the button panel component.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private JComponent createToolBar() {
		// JToolBar tools = new JToolBar(SwingConstants.VERTICAL);
		JToolBar tools = new JToolBar(SwingConstants.HORIZONTAL);
		accessPanel = new AccessEditPanel(this);

		Action editAction = new CommandEdit(getLocalization().getString("Edit off"));
		editCommand = new JToggleButton(editAction);
		editCommand.setToolTipText(getLocalization().getString("tt-edit mode"));
		tools.add(editCommand);

		tools.addSeparator();

		addButton(tools, new CommandEditRow(getLocalization().getString("Edit row"), table, accessPanel),
				CommandEditRow.ACTION_COMMAND, getLocalization().getString("tt-Edit row"));

		addButton(tools, new CommandMoveRowsUp(getLocalization().getString("up"), table),
				CommandMoveRowsUp.ACTION_COMMAND, getLocalization().getString("tt-row up")); // OK

		addButton(tools, new CommandMoveRowsDown(getLocalization().getString("down"), table),
				CommandMoveRowsDown.ACTION_COMMAND, getLocalization().getString("tt-row down")); // OK

		addButton(tools, new CommandCellHistory(getLocalization().getString("history"), table, null),
				CommandCellHistory.ACTION_COMMAND, getLocalization().getString("tt-history"));

		tools.addSeparator();

		addButton(tools, new CommandSearchAccess(getLocalization().getString("search")),
				CommandSearchAccess.ACTION_COMMAND, getLocalization().getString("tt-search"));
		addButton(tools, new CommandColumnChooser(getLocalization().getString("Columns"), table),
				CommandColumnChooser.ACTION_COMMAND, getLocalization().getString("tt-arrange columns"));

		tools.addSeparator();

		addButton(tools, new CommandInsertRows<Access>(getLocalization().getString("New"), table, null),
				CommandInsertRows.ACTION_COMMAND, getLocalization().getString("tt-New row"));

		addButton(tools, new CommandDeleteRows(getLocalization().getString("delete"), table),
				CommandDeleteRows.ACTION_COMMAND, getLocalization().getString("tt-delete row")); // OK

		addButton(tools, new CommandCellStyle(getLocalization().getString("style"), table),
				CommandCellStyle.ACTION_COMMAND, getLocalization().getString("tt-change style")); // OK

		tools.addSeparator();
		//
		addButton(tools, new CommandExportToSheet(getLocalization().getString("Export")),
				CommandExportToSheet.ACTION_COMMAND, getLocalization().getString("tt-export to sheet"));

		// addButton(tools, new
		// CommandImportDictionary(getLocalization().getString("ImportDictionary")),
		// CommandImportDictionary.ACTION_COMMAND,
		// getLocalization().getString("tt-Import Dictionary"));

		// addButton(tools, new CommandChangeLAF("ChangeLAF"),
		// CommandChangeLAF.ACTION_COMMAND, "ChangeLAF");
		// addButton(tools,
		// new CommandRefreshTabs(getLocalization().getString("Refresh")),
		// "REFRESH");
		return tools;
	}

	private void createPopUpMenu() {
		// Create the popup menu.
		tablePopupMenu = new JPopupMenu();
		// tablePopupMenu.add(commands.get(CommandMoveRowsDown.ACTION_COMMAND));
		// tablePopupMenu.add(commands.get(CommandMoveRowsUp.ACTION_COMMAND));
		tablePopupMenu.add(userCommands.get(CommandEditRow.ACTION_COMMAND));
		tablePopupMenu.add(userCommands.get(CommandCellHistory.ACTION_COMMAND));
		tablePopupMenu.add(userCommands.get(CommandCellStyle.ACTION_COMMAND)); // OK
	}

	/**
	 * Create the access management panel. This is the mail panel.
	 * 
	 * @return the access management panel.
	 */
	private JComponent createTabsPanel() {
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		table.setCellSelectionEnabled(true);

		categoryTabs = new TabbedPane(new TabPanelEditor());
		categoryTabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		categoryTabs.setTabPlacement(JTabbedPane.LEFT);
		// listener to the category model and tab model
		TabActionListener tal = new TabActionListener();
		categoryModel.addListDataListener(tal);
		categoryTabs.addActionListener(tal);
		categoryTabs.addChangeListener(tal);
		categoryTabs.setVisible(false);
		return categoryTabs;
	}

	/**
	 * Install commands every time the document changes. Buttons acting on the table
	 * rows are activated.
	 */
	@SuppressWarnings("unchecked")
	private void installCommands() {
		// Search
		RegexMatcher ts = new RegexMatcher();
		ts.setStyler(new RegexHtmlStyler());
		AbstractRegexSearch ams = new AccessTableRegexSearch(ts, document.getModel());

		searchPanel.addSearch(new HyperLinkTableListener(table), ams, "Access");
		// Buttons
		Action b;
		b = userCommands.get(CommandColumnChooser.ACTION_COMMAND);
		CommandColumnChooser ccc = (CommandColumnChooser) b;
		ccc.initialize();

		b = userCommands.get(CommandCellHistory.ACTION_COMMAND);
		CommandCellHistory csh = (CommandCellHistory) b;
		csh.setTraceModel(document.getTraceModel());

		// Add only current selected category!
		b = userCommands.get(CommandInsertRows.ACTION_COMMAND);
		CommandInsertRows<Access> cir = (CommandInsertRows<Access>) b;
		cir.setRowFactory(document.getAccessFactory());

		edit(true);
	}

	/**
	 * 
	 */
	private void edit(boolean active) {
		for (Iterator<Action> iterator = userCommands.values().iterator(); iterator.hasNext();) {
			Action type = iterator.next();
			type.setEnabled(editCommand.isSelected());
		}

		// Active always
		userCommands.get(CommandMoveRowsUp.ACTION_COMMAND).setEnabled(active);
		userCommands.get(CommandMoveRowsDown.ACTION_COMMAND).setEnabled(active);
		userCommands.get(CommandCellHistory.ACTION_COMMAND).setEnabled(active);
		// userCommands.get(CommandFitRowHeigth.ACTION_COMMAND).setEnabled(active);
		userCommands.get(CommandSearchAccess.ACTION_COMMAND).setEnabled(active);
		userCommands.get(CommandColumnChooser.ACTION_COMMAND).setEnabled(active);
		passphrasePanel.setEnabled(active);

		// Table buttons
		userCommands.get(CommandInsertRows.ACTION_COMMAND).setEnabled(editCommand.isSelected() && active);
		userCommands.get(CommandDeleteRows.ACTION_COMMAND).setEnabled(editCommand.isSelected() && active);
		userCommands.get(CommandEditRow.ACTION_COMMAND).setEnabled(active);

		userCommands.get(CommandCellStyle.ACTION_COMMAND).setEnabled(editCommand.isSelected() && active);
		// Properties
		generalSettingsPanel.setEnabled(editCommand.isSelected() && active);
		securitySettingsPanel.setEnabled(editCommand.isSelected() && active);
		userCommands.get(CommandChangeProperties.ACTION_COMMAND).setEnabled(editCommand.isSelected() && active);
		userCommands.get(CommandCancelProperties.ACTION_COMMAND).setEnabled(editCommand.isSelected() && active);
		userCommands.get(CommandChangeSecurityProperties.ACTION_COMMAND).setEnabled(editCommand.isSelected() && active);
		userCommands.get(CommandCancelSecurityProperties.ACTION_COMMAND).setEnabled(editCommand.isSelected() && active);
		userCommands.get(CommandExportToSheet.ACTION_COMMAND).setEnabled(editCommand.isSelected() && active);

		// Log management
		logsPanel.setEnabled(editCommand.isSelected() && active);
		// Resources
		// resourceSelectionPanel.setEnabled(editCommand.isSelected() &&
		// active);
		// Pass phrase
		// passphrasePanel.setEnabled(editCommand.isSelected() && active);
	}

	/**
	 * Install categories from the document model.
	 */
	private void installCategories() {
		categoryModel.removeAllElements();
		categoryTabs.removeAll();
		// addTab shots a table event!
		categoryTabs.addTab(getLocalization().getString("..."), tableScrollPane);
		for (int i = 0; i < document.getModel().getFullSize(); i++) {
			String o = document.getModel().getEntry(i).get(AccessFactory.CATEGORY).getValue().toString();
			if (categoryModel.getIndexOf(o) == -1) {
				categoryModel.addElement(o);
				if (categoryTabs.indexOfTab(o) == -1) {
					categoryTabs.addTab(o, tableScrollPane);
				}
			}
		}
		categoryTabs.setSelectedIndex(0);
	}

	/**
	 * 
	 */
	private void installDocument() {
		AccessFactory fieldlist = AccessFactory.getInstance();
		table.setModel(document.getModel());
		document.accept(new VersionAccessVisitor(this));
		// Setting cell editor and renderer
		// Category management
		CellComboBoxEditor<String> comboCellEditor = new CellComboBoxEditor<String>(categoryModel);
		comboCellEditor.setClickCountToStart(1);
		table.getColumnModel().getColumn(fieldlist.getIndex(AccessFactory.CATEGORY))
				.setCellEditor(new StyleCellEditor(comboCellEditor));
		// Display access password
		table.getColumnModel().getColumn(fieldlist.getIndex(AccessFactory.PASSWORD_DISPLAY))
				.setCellEditor(new StyleCellEditor(new CellPasswordDisplay(this)));
		table.getColumnModel().getColumn(fieldlist.getIndex(AccessFactory.PASSWORD_DISPLAY))
				.setCellRenderer(new StyleCellRenderer(new CellPasswordRenderer()));
		// Change access password
		JButton changeButton = new JButton(getLocalization().getString("Change"));
		table.getColumnModel().getColumn(fieldlist.getIndex(AccessFactory.CHANGE_BUTTON))
				.setCellEditor(new StyleCellEditor(new CellPasswordChange(changeButton, this)));
		table.getColumnModel().getColumn(fieldlist.getIndex(AccessFactory.CHANGE_BUTTON))
				.setCellRenderer(new StyleCellRenderer(
						new CellPasswordChange(new JButton(getLocalization().getString("Change")), this)));
		// Table header
		TableHeader header = new TableHeader(table.getColumnModel());
		table.setTableHeader(header);
		table.getModel().addTableModelListener(new ModelChangeListener());
		tableScrollPane = new JScrollPane(table);
		tableScrollPane.setColumnHeaderView(header);
		categoryTabs.setVisible(true);
	}

	/**
	 * Add a button to the button panel. Button fill the available horizontal space.
	 * 
	 * @param p             panel.
	 * @param a             action.
	 * @param actionCommand the action command.
	 * @param tooltip       tooltip
	 * @param g             constraint.
	 */
	private void addButton(JToolBar p, Action a, String actionCommand, String tooltip) {
		JButton b = new JButton(a);
		b.setToolTipText(tooltip);
		p.add(b);
		userCommands.put(actionCommand, a);
	}

	/**
	 * Uninstall commands when the document is closed. Buttons acting on the table
	 * rows are made unactive.
	 */
	private void uninstallCommands() {
		searchPanel.removeSearch("");
		searchPanel.clear();
		edit(false);
	}

	/**
	 * Uninstall categories from the tabs pane.
	 */
	private void uninstallCategories() {
		categoryTabs.removeAll();
	}

	/**
	 * Set default application properties at closing the document.
	 */
	private void uninstallProperties() {
		properties.clear();
		properties.putAll(PasswordConstants.getDefaultProperties());
		generalSettingsPanel.initGUI();
	}

	/**
	 * Get the model.
	 * 
	 * @return the model
	 */
	private AccessTableModel getModel() {
		AccessDocument document = this.document;
		return document != null ? document.getModel() : null;
	}

	/**
	 * @return
	 */
	private boolean displayingPasswordEnabled() {

		String value = properties.getProperty(PasswordConstants.PREF_OUTPUT);

		if (!value.equals(PasswordConstants.OPT_COPY)) {
			return true;
		} else
			return false;
	}

	/**
	 * @return true when writing to clipboard is enabled.
	 */
	private boolean writingClipboardEnabled() {
		String value = properties.getProperty(PasswordConstants.PREF_OUTPUT);

		if (!value.equals(PasswordConstants.OPT_DISPLAY)) {
			return true;
		} else
			return false;

	}

	private void autosave() {
		// System.out.println(this.getClass().getName() + " autosave() "
		taskScheduler.stop(PasswordConstants.AUTOSAVE_THREAD);

		// Start the service again

		try {
			if (properties.getProperty(PasswordConstants.PREF_AUTOSAVE).equals(PasswordConstants.AUTOSAVE_SCHEDULED)) {
				long time = PasswordConstants.SAVE_SCHEDULED_TIME;
				if (taskScheduler.isRunningCheck(PasswordConstants.AUTOSAVE_THREAD)) {
					taskScheduler.stop(PasswordConstants.AUTOSAVE_THREAD);
				}

				taskScheduler.addTask(new AutosavingTask(this), time, PasswordConstants.AUTOSAVE_THREAD);

				// timer.stop(PasswordDefault.AUTOSAVE_THREAD);
			}
		} catch (Exception err) {
			logsPanel.appendLog(getLocalization().getString("Error") + "autosave - " + err.getMessage(),
					PasswordConstants.getRedFormat());
		}

	}

	/**
	 * Change the security for the current document.
	 * 
	 * @param security the new security for the document.
	 * @param backup   back up properties.
	 */
	private void changeSecurity(AccessSecurityObject security, Properties backup) {
		// Export back up to the device memory
		ChangeSecurityVisitor visitor = new ChangeSecurityVisitor(this, security);
		document.accept(visitor);
		String log[] = visitor.getResult();
		// Update the new security settings
		if (log.length == 0) {
			this.securityObject = security;
		} else {
			// Restore properties
			properties.clear();
			properties.putAll(backup);
			// Initialize the properties panels
			generalSettingsPanel.initGUI();
			securitySettingsPanel.initGUI();
			revertSecurity();
		}
	}

	private void revertSecurity() {
		// Manage the undo of the security changes
	}

	/**
	 * When ready the password is enabled.
	 * 
	 * @param b true when ready.
	 */
	private void setReady(boolean b) {
		this.ready = b;
		if (!b && isEditing()) {
			editCommand.doClick();
		}
		// table.setEnabled(b);
		if (b) {
			categoryTabs.setBorder(BORDER_ACTIVE);
		} else {
			categoryTabs.setBorder(BORDER_NOTACTIVE);
		}
	}

	/**
	 * Create a titled border. The style can be changed.
	 * 
	 * @param t the title.
	 * @return the border.
	 */
	static Border createTitledBorder(String t) {
		return new TitledBorder(t);
	}

	/**
	 * Create an {@link AccessDocument} instance.
	 */
	@Override
	protected AccessDocument newDocument() {
		AccessDocument d = new AccessDocument();
		d.setVersion(getVersion());
		return d;
	}

	@Override
	protected void setActiveDocument() {

		installDocument();
		installCategories();
		// Add the listener for the document changes
		document.addDocumentChangeListener(this);
		// New document
		document.setChanged(false);
		//
		logsPanel.appendLog(
				getLocalization().getString("Current document") + ": " + getActiveDocument().getResource().getURL()//
						+ "  " + getLocalization().getString("Document version") + ": " + document.getVersion(), //
				PasswordConstants.getBlankFormat());
		// Edit
		editCommand.setEnabled(true);
		editCommand.setSelected(false); // Edit off
		installCommands();
		// Check and update the document status
		document.accept(changeAccessVisitor);
		//
		setFrameTitle();
	}

	@Override
	protected void closeActiveDocument() {
		taskScheduler.stop();
		if (editCommand.isSelected())
			editCommand.doClick(); // Edit off
		editCommand.setEnabled(false); // Edit off
		uninstallCommands();
		uninstallCategories();
		uninstallProperties();

	}

	@Override
	protected void exitApplication() throws IllegalStateException {
		taskScheduler.stop();
		System.exit(0);
	}

	@Override
	protected void loadDefaultProperties() {
		properties.clear();
		properties.putAll(PasswordConstants.getDefaultProperties());
		document.setProperties(properties);
		generalSettingsPanel.initGUI();
		securitySettingsPanel.initGUI();
		TableProperties.applyTableColumnsProperties(table, properties);
		TabbedPaneProperties.applyTabsPaneProperties(categoryTabs, properties);
		autosave();
	}

	@Override
	protected void loadProperties() throws IOException {
		properties.clear();
		properties.putAll(document.getProperties());
		document.setProperties(properties);
		generalSettingsPanel.initGUI();
		securitySettingsPanel.initGUI();
		TableProperties.applyTableColumnsProperties(table, properties);
		TabbedPaneProperties.applyTabsPaneProperties(categoryTabs, properties);
		autosave();
	}

	@Override
	protected void saveProperties() throws IOException {
		properties.setProperty(VERSION_KEY, getVersion());
		TableProperties.saveTableColumnsProperties(table, properties);
		TabbedPaneProperties.saveTabsPaneProperties(categoryTabs, properties);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.apps.AbstractApplication#close()
	 */
	@Override
	public void close(I_Resource r) throws IOException {
		if (document != null) {
			logsPanel.appendLog(
					getLocalization().getString("Close document") + ": " + getActiveDocument().getResource().getURL(),
					PasswordConstants.getBlankFormat());
			document.removeDocumentChangeListener(this);
		}
		super.close(r);
		setFrameTitle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.apps.AbstractApplication#exit()
	 */
	@Override
	public void exit() throws IOException {
		if (document != null)
			logsPanel.appendLog(getLocalization().getString("Exit") + ": " + getActiveDocument().getResource().getURL(),
					PasswordConstants.getBlankFormat());
		super.exit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.apps.AbstractApplication#save()
	 */
	@Override
	public void save(I_Resource r) throws IOException {
		try {
			super.save(r);
		} catch (IOException e) {
			logsPanel.appendLog(getLocalization().getString("Save") + ": " + getLocalization().getString("Error") + ": "
					+ e.getMessage(), PasswordConstants.getRedFormat());
		}
		if (document != null)
			logsPanel.appendLog(getLocalization().getString("Save") + ": " + getActiveDocument().getResource().getURL(),
					PasswordConstants.getBlankFormat());
		// setFrameTitle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.catode.apps.AbstractApplication#saveAs(net.catode.resource.I_Resource )
	 */
	@Override
	public void saveAs(I_Resource r) throws IOException {
		super.saveAs(r);
		if (document != null)
			logsPanel.appendLog(
					getLocalization().getString("Save as") + ": " + getActiveDocument().getResource().getURL(),
					PasswordConstants.getBlankFormat());
		// setFrameTitle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.apps.AbstractApplication#closeAll()
	 */
	@Override
	public void closeAll() throws IOException {
		if (document != null)
			logsPanel.appendLog(getLocalization().getString("Close all"), PasswordConstants.getBlankFormat());
		super.closeAll();
		setFrameTitle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.apps.AbstractApplication#getFileFilter()
	 */
	@Override
	public FileFilter getFileFilter() {
		return new PasswordFileFilter(getLocalization());
	}

	@Override
	public String getFileExtension() {
		return PasswordFileFilter.FILE_EXTENSION;
	}

	@Override
	public String getFileName() {
		String uname = System.getProperty("user.name");
		return ("AcessList" + uname);
		// return (Utility.dateToString(new Date(), null) + uname +
		// "AcessList");
	}

	@Override
	public void performChange(DocumentChangeEvent event) {
		setFrameTitle();
	}

	/**
	 * Display a log.
	 * 
	 * @param logText
	 */
	public void setLog(String logText) {
		logsPanel.appendLog(logText, "");
	}

	/**
	 * Fire a table event every time an item of the document model is selected.
	 */
	public void notifyChange() {
		if (document == null)
			return;
		int row = table.getSelectedRow();
		if (row > -1) {
			getModel().fireTableRowsUpdated(row, row);
			logsPanel.appendLog(
					getLocalization().getString("Change access") + ": "
							+ getModel().getEntry(row).getValue(AccessFactory.ACCESS),
					PasswordConstants.getYellowFormat());
		}
	}

	/**
	 * Get the available password algorithms.
	 * 
	 * @return the available password algorithms.
	 */
	public String[] getPasswordAlgorithms() {
		return passwordGenerator.getList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.password.PasswordController#clearClipboard()
	 */
	public void clearClipboard() {
		if (writingClipboardEnabled()) {
			ApplicationClipboard.clearClipboard();
			// Clipboard clipboard = Toolkit.getDefaultToolkit()
			// .getSystemClipboard();
			// clipboard.setContents(new StringSelection(""), new
			// StringSelection(
			// ""));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.catode.password.PasswordController#writeToClipboard(java.lang.String)
	 */
	public void copyToClipboard(Object o) {
		if (o != null && writingClipboardEnabled()) {
			// Clipboard clipboard = Toolkit.getDefaultToolkit()
			// .getSystemClipboard();
			// StringSelection ss = new StringSelection(o.toString());
			// clipboard.setContents(ss, ss);
			ApplicationClipboard.copyToClipboard(o);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.password.PasswordController#displayPassword(java.awt.Point)
	 */
	public void displayPassword(int row, int modifiers) {
		if (isReady() == false) {
			PopMessage.displayInfo(getFrame(), getLocalization().getString("Passphrase is missing"));
			return;
		}
		Access access = getModel().getEntry(row);
		try {
			if (getTaskScheduler().isRunningCheck(PasswordConstants.PASSWORD_THREAD)) {
				getTaskScheduler().stop(PasswordConstants.PASSWORD_THREAD);
			} else {
				getTaskScheduler().stop(PasswordConstants.PASSWORD_THREAD);
				int period = PasswordConstants.FRAMERATE;
				int duration = 1000
						* Utility.toInteger(document.getProperties().getProperty(PasswordConstants.PREF_DISPLAY_TIME));

				char[] p = getPassword(access);

				if (displayingPasswordEnabled()) {
					access.displayPassword(period, duration, p);
				} else {
					access.displayPassword(period, duration, null);
				}

				getTaskScheduler().addTask(new DisplayPasswordTask(access, getModel().indexOf(row)), period, duration,
						PasswordConstants.PASSWORD_THREAD);

				if (p != null) {
					String buffer = (modifiers
							& java.awt.event.ActionEvent.SHIFT_MASK) == java.awt.event.ActionEvent.SHIFT_MASK
									? access.getValue(AccessFactory.USER) + "\t" + new String(p)
									: new String(p);
					copyToClipboard(buffer);
				}
				access.updateStatistics();
				logsPanel.appendLog(
						getLocalization().getString("Show password") + ": " + access.getValue(AccessFactory.ACCESS),
						PasswordConstants.getBlankFormat());
			}

		} catch (IllegalStateException e) {
			// generator internal status is in error.
			PopMessage.displayError(frame, getLocalization().getString("Security Exception"),
					getLocalization().getString("Invalid key parameters"));
		} catch (IllegalArgumentException e) {
			// when the generator internal status is in error.
			PopMessage.displayError(frame, getLocalization().getString("Security Exception"),
					getLocalization().getString("Wrong security parameters"));
		}
	}

	/**
	 * Encrypt the user password.
	 * 
	 * @param access the access.
	 * @throws IllegalStateException     when the generator internal status is in
	 *                                   error.
	 * @throws IllegalBlockSizeException when an error occurs attempting to decipher
	 *                                   a user password.
	 * @throws BadPaddingException       when an error occurs attempting to decipher
	 *                                   a user password.
	 * @throws IllegalArgumentException  when an error occurs attempting to decipher
	 *                                   a user password.
	 * @throws HashException
	 */
	public void encryptPassword(Access access) throws IllegalStateException, IllegalArgumentException,
			BadPaddingException, IllegalBlockSizeException, DigestException {
		if (isReady() == false) {
			throw new IllegalStateException("Passphrase is missing");
		}
		access.setSalt(securityObject.getRandomSeed());
		I_BlockCipherStream c = securityObject.getCipher(access.getSalt(), true);
		byte input[] = access.getUserPassword().getBytes();
		access.setUserPassword(new String(Base64.encode(c.doFinal(input, 0, input.length))));

		// securityObject.cipherPassword(access);
	}

	/**
	 * Get the password.
	 * 
	 * @param access the access.
	 * @return the password.
	 * @throws IllegalStateException    when the generator internal status is in
	 *                                  error.
	 * @throws IllegalArgumentException when an error occurs attempting to decipher
	 *                                  a user password.
	 */
	public char[] getPassword(Access access) throws IllegalStateException, IllegalArgumentException {
		if (securityObject == null) {
			throw new IllegalStateException("Security is undefined!");
		} else {
			char[] p = passwordGenerator.getPassword(access, securityObject);
			return p;
		}
	}

	/**
	 * Get a new password for the access. As a side effect the access salt is
	 * updated from a random seed.
	 * 
	 * @param access the access.
	 * @return the password.
	 * 
	 * @throws IllegalStateException    when the generator internal status is in
	 *                                  error.
	 * @throws InvalidKeyException      when the initialization step fails because
	 *                                  of wrong parameters.
	 * @throws IllegalArgumentException when an error occurs attempting to decipher
	 *                                  a user password.
	 */
	public char[] newPassword(Access access) throws IllegalStateException, IllegalArgumentException {
		if (isReady() == false) {
			throw new IllegalStateException("Passphrase is missing");
		}
		if (securityObject == null) {
			throw new IllegalStateException("Security is undefined!");
		} else {
			access.setSalt(securityObject.getRandomSeed());
			return passwordGenerator.getPassword(access, securityObject);
		}
	}

	/**
	 * Start the pass phrase.
	 * 
	 * @param progressBar the count down progress bar.
	 * @throws Exception when an error occurs during the secret key generation.
	 */
	public void setPassphrase(byte[] b) throws Exception {
		try {
			securityObject = new AccessSecurityObject(properties, b);
			setReady(true);
			// addTimeProgressBar(time);
			logsPanel.appendLog(getLocalization().getString("Set pass phrase"), PasswordConstants.getGreenFormat());
		} catch (Exception e) {
			setError_attempt_counter(getError_attempt_counter() + 1);
			logsPanel.appendLog(getLocalization().getString("Invalid passphrase!") + " Message: " + e.getMessage(),
					PasswordConstants.getRedFormat());
			throw e;
		}
	}

	/**
	 * Stop the pass phrase.
	 * 
	 */
	public void stopPassphrase() throws Exception {
		if (securityObject != null)
			securityObject.reset();
		setReady(false);
		logsPanel.appendLog(getLocalization().getString("Stop pass prase"), PasswordConstants.getBlankFormat());
	}

	/**
	 * Return true when the application is in editing mode.
	 * 
	 * @return true when ready.
	 */
	public boolean isEditing() {
		return editCommand.isSelected();
	}

	/**
	 * Return true when the pass phrase is running.
	 * 
	 * @return true when ready.
	 */
	public boolean isReady() {
		return this.ready;
	}

	/**
	 * Get the current selected tab name.
	 * 
	 * @return the current selected tab name.
	 */
	public String getSelectedTabName() {
		return categoryTabs.getTitleAt(categoryTabs.getSelectedIndex());
	}

	/**
	 * Set the old changed internal object status. It is not allow more than one
	 * status with the same time stamp. The trace is ordered from the first to the
	 * last time stamp.
	 * 
	 * @param tracedObjectID the object changed.
	 * @param oldValue       the old internal object status.
	 */
	public void saveStatus(Object tracedObjectID, Object oldValue) {
		I_Status<I_StyleObject> o = new ObjectStatus<I_StyleObject>(new StyleObject(oldValue, ""), "Changed: ");

		((AccessDocument) getActiveDocument()).getTraceModel().setTrace(tracedObjectID, o);
	}

	/**
	 * @return the error_attempt_counter
	 */
	public int getError_attempt_counter() {
		return error_attempt_counter;
	}

	/**
	 * 
	 */
	private void setFrameTitle() {
		if (document != null) {
			int i = document.getDocumentName().lastIndexOf("/");
			String docname = i < 0 ? document.getDocumentName()
					: document.getDocumentName().substring(i).replace("/", " - ");
			String d = document.isChanged() ? " *" : "";

			getFrame().setTitle(
					getLocalization().getString("application") + " " + getVersion() + " " + docname + " " + d);
		} else {
			getFrame().setTitle(getLocalization().getString("application") + " " + getVersion());
		}
	}

	/**
	 * @param error_attempt_counter the error_attempt_counter to set
	 */
	private void setError_attempt_counter(int error_attempt_counter) {
		this.error_attempt_counter = error_attempt_counter;
	}

	/**
	 * Update tabs from category model.
	 * 
	 */
	@SuppressWarnings("serial")
	private class CommandExportToSheet extends AbstractAction {
		public static final String ACTION_COMMAND = "EXPORT_SPREADSHEET";

		public CommandExportToSheet(String name) {
			super(name);
		}

		@Override
		public void actionPerformed(ActionEvent event) {

			// System.out.println(ACTION_COMMAND);
			// Check the passphrase is running
			if (isReady() && isEditing()) {
				// Choose the path
				JFileChooser fileChooser = new JFileChooser();
				File file = new File(fileChooser.getFileSystemView().getDefaultDirectory().getPath()
						+ System.getProperty("file.separator") + document.getDocumentName() + "_DUMP.xls");
				fileChooser.setSelectedFile(file);
				int userOption = fileChooser.showSaveDialog(getFrame());
				File f = fileChooser.getSelectedFile();
				switch (userOption) {
				case JFileChooser.APPROVE_OPTION:
					try {
						FileWriter writer = new FileWriter(f);
						ExportToSpreadsheetVisitor v = new ExportToSpreadsheetVisitor(Password.this, writer, table);
						document.accept(v);
						writer.close();

						logsPanel.appendLog(getLocalization().getString("tt-export to sheet") + " " + f.getName(),
								PasswordConstants.getRedFormat());
						if (v.getLog().length > 0) {
							StringBuffer b = new StringBuffer();
							for (String s : v.getLog()) {
								b.append(s);
								b.append(" \n ");
							}
							logsPanel.appendLog(getLocalization().getString("tt-export to sheet") + " " + b.toString(),
									PasswordConstants.getRedFormat());
						}
					} catch (IOException e) {
						// logs.appendLog(getLocalization().getString("Export")
						// + " " + f.getName(),
						// PasswordDefault.getRedFormat());
						PopMessage.displayError(getFrame(),
								getLocalization().getString("Export") + " \n" + f.getName() + " \n" + e.getMessage());
						// e.printStackTrace();
					}
					break;
				case JFileChooser.CANCEL_OPTION:
					break;
				case JFileChooser.ERROR_OPTION:
					break;
				default:
					break;
				}
			} else {
				PopMessage.displayInfo(getFrame(), getLocalization().getString("Passphrase is missing"));
			}
		}
	}

	/**
	 * Search the accesses for text tokens.
	 * 
	 */
	@SuppressWarnings("serial")
	private class CommandSearchAccess extends AbstractAction {
		public static final String ACTION_COMMAND = "Search";

		public CommandSearchAccess(String name) {
			super(name);
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			searchPanel.showSearchDialog();
		}
	}

	/**
	 * Change the program properties action.
	 * 
	 */
	@SuppressWarnings("serial")
	private class CommandChangeProperties extends AbstractAction {
		public static final String ACTION_COMMAND = "Change";

		public CommandChangeProperties(String name) {
			super(name);
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			generalSettingsPanel.update();
			// System.out.println("change from " + this.getClass().getName());
			document.setChanged(true);
			autosave();
			logsPanel.appendLog(getLocalization().getString("Change properties"), PasswordConstants.getYellowFormat());
		}
	}

	/**
	 * Cancel the program properties change.
	 * 
	 */
	@SuppressWarnings("serial")
	private class CommandCancelProperties extends AbstractAction {
		public static final String ACTION_COMMAND = "Cancel";

		public CommandCancelProperties(String name) {
			super(name);
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			generalSettingsPanel.initGUI();
		}
	}

	/**
	 * Change the program properties action. Ask before changing the security If OK,
	 * go ahead and show the password panel; if CANCEL go ahead with the new
	 * security settings; if CHANGE go ahead with the new pass-phrase and security
	 * settings
	 */
	@SuppressWarnings("serial")
	private class CommandChangeSecurityProperties extends AbstractAction {
		public static final String ACTION_COMMAND = "ChangeSecurity";

		public CommandChangeSecurityProperties(String name) {
			super(name);
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			if (isReady() == false) {
				PopMessage.displayInfo(getFrame(), getLocalization().getString("Passphrase is missing"));
				return;
			}

			// Warning!
			Object[] options = { getLocalization().getString("Change"), getLocalization().getString("Cancel") };
			int selOption = JOptionPane.showOptionDialog(null, getLocalization().getString("Click CHANGE to continue"),
					getLocalization().getString("Change security settings"), JOptionPane.DEFAULT_OPTION,
					JOptionPane.WARNING_MESSAGE, null, options, options[1]);

			// OK go ahead with changes
			if (selOption == 0) {
				try {
					// 1) Change the pass phrase - may be the same
					LoginDialog changeDialog = new LoginDialog(LoginPanel.PASSWORD_CHANGE);
					changeDialog.setTitle(getLocalization().getString("Change security"));
					AbstractDialog.Options opt = changeDialog.showDialog(getFrame());
					if (opt.equals(AbstractDialog.Options.OK_OPTION)) {
						// Check authorized change
						if (securityObject.check(new String(changeDialog.getPassword()).getBytes())) {

							// Back-up old properties
							Properties backupProperties = new Properties();
							for (Iterator<Object> iterator = properties.keySet().iterator(); iterator.hasNext();) {
								String key = iterator.next() + "";
								backupProperties.setProperty(key, properties.getProperty(key));
							}
							// Changes are effective - initialize salt and check
							properties.setProperty(EncryptionPreferences.PREF_SALT, new String());
							properties.setProperty(PasswordConstants.PREF_CHECK, new String());

							securitySettingsPanel.update();
							// New security object
							changeSecurity(new AccessSecurityObject(properties,
									new String(changeDialog.getNewPassword()).getBytes()), backupProperties);
							document.setChanged(true);
							logsPanel.appendLog(getLocalization().getString("Change security"),
									PasswordConstants.getRedFormat());
						} else {
							securitySettingsPanel.initGUI();
							setError_attempt_counter(getError_attempt_counter() + 1);
							logsPanel.appendLog(getLocalization().getString("Invalid passphrase!"),
									PasswordConstants.getRedFormat());
							PopMessage.displayError(getFrame(), getLocalization().getString("Security Exception"),
									getLocalization().getString("Invalid passphrase!"));
						}
					} else if (opt.equals(AbstractDialog.Options.CLOSE_OPTION)) {
						securitySettingsPanel.initGUI();
						PopMessage.displayInfo(getFrame(), getLocalization().getString("No changes"));
					} else {
						securitySettingsPanel.initGUI();
					}
					changeDialog.reset();
					changeDialog.dispose();
					//
				} catch (Exception e) {
					securitySettingsPanel.initGUI();
					logsPanel.appendLog(getLocalization().getString("Security Exception") + ": " + e.getMessage(),
							PasswordConstants.getRedFormat());
					PopMessage.displayError(getFrame(), getLocalization().getString("Security Exception"),
							e.getMessage());
					// e.printStackTrace();
				}
			} else if (selOption == 1) { // Cancel
				securitySettingsPanel.initGUI();
				PopMessage.displayInfo(getFrame(), getLocalization().getString("No changes"));
			} else { //
				securitySettingsPanel.initGUI();
				PopMessage.displayInfo(getFrame(), getLocalization().getString("No changes"));
			}
		}
	}

	/**
	 * Cancel the program properties change.
	 * 
	 */
	@SuppressWarnings("serial")
	private class CommandCancelSecurityProperties extends AbstractAction {
		public static final String ACTION_COMMAND = "CancelSecurity";

		public CommandCancelSecurityProperties(String name) {
			super(name);
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			securitySettingsPanel.initGUI();
		}
	}

	/**
	 * Enable edit mode. If enabled user can change and save, when disable user
	 * can't do anything.
	 * 
	 */
	private class CommandEdit extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public CommandEdit(String name) {
			super(name);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JToggleButton b = (JToggleButton) e.getSource();

			if (b.isSelected() && properties.getProperty(PasswordConstants.PREF_CHECK) != null
					&& !properties.getProperty(PasswordConstants.PREF_CHECK).isEmpty()) {
				// Check security
				LoginDialog changeDialog = new LoginDialog(LoginPanel.PASSWORD);
				changeDialog.setTitle(getLocalization().getString("Unlock"));
				AbstractDialog.Options opt = changeDialog.showDialog(getFrame());

				if (opt.equals(AbstractDialog.Options.OK_OPTION)) {
					try {
						new AccessSecurityObject(properties, new String(changeDialog.getPassword()).getBytes());
					} catch (Exception err) {
						PopMessage.displayError(getFrame(), getLocalization().getString("Security Exception"),
								getLocalization().getString("Invalid passphrase!"));
						setError_attempt_counter(getError_attempt_counter() + 1);
						logsPanel.appendLog(getLocalization().getString("Invalid passphrase!"),
								PasswordConstants.getRedFormat());
						b.setSelected(false);
					}
				} else if (opt.equals(AbstractDialog.Options.NO_OPTION)) {
					b.setSelected(false);
				} else {
					b.setSelected(false);
				}
			}
			if (b.isSelected()) {
				b.setText(getLocalization().getString("Edit on"));
				logsPanel.appendLog(
						getLocalization().getString("Edit on") + ": " + getActiveDocument().getResource().getURL(),
						PasswordConstants.getYellowFormat());
			} else {
				b.setText(getLocalization().getString("Edit off"));
				logsPanel.appendLog(
						getLocalization().getString("Edit off") + ": " + getActiveDocument().getResource().getURL(),
						PasswordConstants.getBlankFormat());
			}
			try {
				document.getModel().setEditable(b.isSelected());
			} catch (NullPointerException npe) {
				// document closed
			}
			edit(true);
		}
	}

	/**
	 * Import the table of contents from a text file.
	 * 
	 */
	@SuppressWarnings("unused")
	private class CommandImportDictionary extends AbstractAction {
		public static final String ACTION_COMMAND = "ImportDictionary";
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public CommandImportDictionary(String name) {
			super(name);
		}

		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			int returnVal = fileChooser.showOpenDialog(frame);
			switch (returnVal) {
			case JFileChooser.APPROVE_OPTION:
				importContents(fileChooser.getSelectedFile());
				break;
			case JFileChooser.CANCEL_OPTION:
				break;
			case JFileChooser.ERROR_OPTION:
				break;
			default:
				break;
			}
		}

		private void importContents(File f) {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF8"));
				String l;
				Map<String, String> syllables = new HashMap<String, String>();
				while ((l = reader.readLine()) != null) {
					// Read a line,
					System.out.println(this.getClass().getName() + " original string " + l);

					System.out
							.println(this.getClass().getName() + " blank spaces removed " + l.replaceAll("\\s+", " "));
					System.out.println(this.getClass().getName() + " blank spaces removed and - separated"
							+ l.replaceAll("\\s+", " ").replaceAll(" ", "-"));

					// replace spaces with "-"

					for (String s : l.replaceAll("\\s+", " ").replaceAll(" ", "-").split("-")) {
						syllables.put(s.replaceAll("[^a-zA-Z]", ""), s);
					}

					// split at "-"
					// put syllables a key map
				}
				// get the key map of the table with distinct keys
				// System.out.println(this.getClass().getName() + "" +
				// syllables);
				String[] dictionary = new String[0];
				dictionary = syllables.keySet().toArray(dictionary);
				Arrays.sort(dictionary);
				System.out.println(this.getClass().getName() + " ");
				for (String t : dictionary) {
					System.out.print("\"" + t + "\"" + ",");
				}

				for (String t : PasswordDictionary.getDictionary(AccessFactory.getInstance().newRow())) {
					System.out.print("\"" + t + "\"" + ",");
				}

			} catch (FileNotFoundException e) {
				PopMessage.displayError(getFrame(), e.getMessage());
			} catch (IOException e) {
				PopMessage.displayError(getFrame(), e.getMessage());
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
					}
				}

			}
		}

	}

	/**
	 * Listener for the table mouse. It shows pop up menu, and pressing SHIFT +
	 * click copies the selected cell content into the clipboard.
	 */
	private class TableMouseLister extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent event) {
			Point p = getPoint(event);
			Object a = null;
			if (p.x > -1 && p.y > -1) {
				a = ((I_StyleObject) table.getValueAt(p.x, p.y)).getValue();
			}
			// int onmask = MouseEvent.ALT_DOWN_MASK
			// | MouseEvent.BUTTON1_DOWN_MASK;
			// int offmask = MouseEvent.CTRL_DOWN_MASK;
			// if ((event.getModifiersEx() & (onmask | offmask)) == onmask) {
			// System.out.println("click + shift" + event.getModifiersEx());
			// }
			int onmask = InputEvent.ALT_DOWN_MASK;
			if ((event.getModifiersEx() & (onmask)) == onmask) {
				copyToClipboard(a + "");
			}
			// System.out.println("click + ?" + event.getModifiersEx());

		}

		@Override
		public void mousePressed(MouseEvent e) {
			showPopup(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			showPopup(e);
		}

		private Point getPoint(MouseEvent event) {
			return new Point(table.rowAtPoint(event.getPoint()), table.columnAtPoint(event.getPoint()));
		}

		private void showPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {
				tablePopupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}

	}

	/**
	 * Timer task updating the progress bar. When finished it cancels the password.
	 * 
	 */
	private class AutosavingTask extends PeriodicTask {

		private Password app;

		public AutosavingTask(Password app) {
			this.app = app;
		}

		/**
		 * 
		 */
		@Override
		public void performLast() {

		}

		@Override
		public void performTask() {
			// logs.appendLog(
			// getLocalization().getString("Autosave document")
			// + ": is changed? " + app.getDocument().isChanged()
			// + " name= " + getDocument().getURL()
			// + " is ready? " + app.isReady(),
			// SuperPasswordDefault.getBluFormat());
			// System.out.println(this.getClass().getName() + " prepare
			// autosaving");
			if (app.getActiveDocument().isChanged()) {
				try {
					// System.out.println(this.getClass().getName() + "
					// autosaving: document is changed ");
					app.getActiveDocument().save();
					// System.out.println(this.getClass().getName() + "
					// autosaving: document vas saved ");
					app.getActiveDocument().setChanged(false);
					logsPanel.appendLog(getLocalization().getString("Autosave document") + ": "
							+ getActiveDocument().getResource().getURL(), PasswordConstants.getBlankFormat());
				} catch (IOException e) {
					logsPanel.appendLog(getLocalization().getString("Autosaving failure!") + ": "
							+ getActiveDocument().getResource().getURL(), PasswordConstants.getRedFormat());
				}
			}
		}
	}

	/**
	 * Listen to the tab changes and propagates to the category model. Listen to
	 * category model changes and propagates to the tab model.
	 * 
	 */
	private class TabActionListener implements ActionListener, ListDataListener, ChangeListener {

		private int n; // progressive category name

		/*
		 * Listen to the current tab selection for filtering .
		 */
		@Override
		public void stateChanged(ChangeEvent event) {
			int tabIndex = categoryTabs.getSelectedIndex();
			int col = table.convertColumnIndexToView(0);
			// Remove column 0 filters
			TableHeader header = (TableHeader) table.getTableHeader();
			if (tabIndex == 0) { // Remove filters
				header.removeFilter(col);
			} else if (tabIndex > 0) { // Filter
				I_StyleObject match = new StyleObject(categoryTabs.getTitleAt(tabIndex));
				Access a = document.getAccessFactory().newRow();
				a.set(0, match);
				header.doFilter(col, a);
			}
		}

		/*
		 * Listen tab editor events and add new tab button.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			int index = categoryTabs.getSelectedIndex();
			String oldCategory = categoryTabs.getTitleAt(index);
			String newCategory = categoryTabs.getEditComponent().getTitle();
			// Cancel
			if (e.getActionCommand().equalsIgnoreCase(TabbedPane.CANCEL)) {
			}
			// Change an existing category name
			else if (e.getActionCommand().equalsIgnoreCase(TabbedPane.CHANGE)) {
				if (categoryModel.getIndexOf(oldCategory) > -1) {
					categoryModel.insertElementAt(newCategory, categoryModel.getIndexOf(oldCategory));
					categoryModel.removeElement(oldCategory);
				} else {
					categoryModel.addElement(newCategory);
				}
				document.accept(new ChangeCategoryVisitor(oldCategory, newCategory));
			}
			// Remove an existing category
			else if (e.getActionCommand().equalsIgnoreCase(TabbedPane.REMOVE)) {
				// set the category to blank!
				oldCategory = categoryTabs.getTitleAt(categoryTabs.getSelectedIndex());
				// document.accept(new CommandRemoveCategory());
				document.accept(new ChangeCategoryVisitor(oldCategory, getLocalization().getString("...")));
				// Remove category from the model
				categoryModel.removeElement(oldCategory);
			}
			// Add a new category
			else if (e.getActionCommand().equalsIgnoreCase(TabbedPane.ADD)) {
				String s = getLocalization().getString("New category") + (n == 0 ? "" : n);
				n++;
				if (categoryTabs.indexOfTab(s) == -1) {
					categoryTabs.addTab(s, null, tableScrollPane, s);
					// categoryTabs.addTab(new StyleObject(s,
					// PredefinedStyles.blueWhite1), null,
					// tableScrollPane, s);
					// categoryTabs.addTab(new StyleObject(s, ""), null,
					// tableScrollPane, s);

					categoryModel.addElement(s);
				}
			} else {
			}
		}

		// Listen to the category model.
		@Override
		public void contentsChanged(ListDataEvent e) {
		}

		// Listen to tab changes.
		@Override
		public void intervalAdded(ListDataEvent e) {
		}

		@Override
		public void intervalRemoved(ListDataEvent e) {
		}
	}

	/**
	 * Listen to the table model changes.
	 * 
	 */
	private class ModelChangeListener implements TableModelListener {

		@Override
		public void tableChanged(TableModelEvent e) {
			if (document == null)
				return;

			int row = e.getFirstRow();
			Access access = getModel().getEntry(row);
			// System.out.println(this.getClass().getName() + "change from " + "
			// event type= " + e.getType() + " access= "
			// + (row > -1 ? access.getValue(AccessFactory.ACCESS) : "no
			// access"));

			if (row > -1 && access != null) {
				String s = getLocalization().getString(AccessFactory.ACCESS) + ": ";
				if (e.getType() == TableModelEvent.UPDATE
						&& e.getColumn() != access.getFactory().getIndex(AccessFactory.PASSWORD)) {
					logThis(s, CommandMoveRowsUp.ACTION_COMMAND, PasswordConstants.getBlankFormat());
					logThis(s, CommandMoveRowsDown.ACTION_COMMAND, PasswordConstants.getBlankFormat());
					access.accept(changeAccessVisitor);
					getActiveDocument().setChanged(true);
				} else if (e.getType() == TableModelEvent.INSERT) {
					logThis(s, CommandInsertRows.ACTION_COMMAND, PasswordConstants.getBlankFormat());
					access.accept(initAccessVisitor);
					// System.out.println("change from " +
					// this.getClass().getName() + " event " + e);
					getActiveDocument().setChanged(true);
				} else if (e.getType() == TableModelEvent.DELETE) {
					logThis(s, CommandDeleteRows.ACTION_COMMAND, PasswordConstants.getYellowFormat());
					// System.out.println("change from " +
					// this.getClass().getName() + " event " + e);
					getActiveDocument().setChanged(true);
				}
			}

			try {
				if (properties.getProperty(PasswordConstants.PREF_AUTOSAVE).equals(PasswordConstants.AUTOSAVE_ALWAYS)
						&& getActiveDocument().isChanged()) {
					getActiveDocument().save();
					getActiveDocument().setChanged(false);
					logsPanel.appendLog(getLocalization().getString("Autosave document") + ": "
							+ getActiveDocument().getResource().getURL(), PasswordConstants.getBlankFormat());
				}
			} catch (IOException e1) {
				// e1.printStackTrace();
				getActiveDocument().setChanged(true);
				logsPanel.appendLog(getLocalization().getString("Autosaving failure!") + ": " + e1.getMessage(),
						PasswordConstants.getRedFormat());
			}
		}

		/**
		 * Append a log if not empty.
		 */
		private void logThis(String s, String command, String format) {
			String l = ((TableCommand) userCommands.get(command)).getLog();
			if (l.length() > 0) {
				logsPanel.appendLog(s + l, format);
			}
		}

	}

	/**
	 * Timer task updating the progress bar. When finished it cancels the password.
	 * 
	 */
	private class DisplayPasswordTask extends SwingPeriodicTask {
		private RangedValue rangedValue;
		private int index;
		private int column = document.getAccessFactory().getIndex(AccessFactory.PASSWORD);

		public DisplayPasswordTask(Access access, int index) {
			this.rangedValue = (RangedValue) access.getValue(AccessFactory.PASSWORD_DISPLAY);
			this.index = index;
		}

		@Override
		public void performLast() {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					rangedValue.setValue("");
					rangedValue.setGauge(0);
					clearClipboard();
					int row = getModel().getIndex(index);
					getModel().fireTableChanged(new TableModelEvent(getModel(), row, row, column));
				}
			});
		}

		@Override
		public void performTask() {
			rangedValue.setGauge((int) (this.duration + now - System.currentTimeMillis()));
			int y = getModel().getIndex(index);
			getModel().fireTableChanged(new TableModelEvent(getModel(), y, y, column));
		}
	}

}
