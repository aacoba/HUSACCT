package husacct.define.presentation.jpanel;

import husacct.ServiceProvider;
import husacct.common.help.presentation.HelpableJPanel;
import husacct.common.services.IServiceListener;
import husacct.define.presentation.jdialog.SoftwareUnitJDialog;
import husacct.define.presentation.tables.JTableSoftwareUnits;
import husacct.define.presentation.tables.JTableTableModel;
import husacct.define.presentation.utils.UiDialogs;
import husacct.define.task.DefinitionController;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;

public class SoftwareUnitsJPanel extends HelpableJPanel implements ActionListener,
		Observer, IServiceListener {

	private static final long serialVersionUID = 8086576683923713276L;
	private final Logger logger = Logger.getLogger(SoftwareUnitsJPanel.class);

	private JButton addSoftwareUnitButton;
	private JMenuItem addSoftwareUnitItem = new JMenuItem();
	private JPopupMenu popupMenu = new JPopupMenu();
	private JButton removeSoftwareUnitButton;
	SoftwareUnitJDialog softwareUnitFrame = null;

	private JMenuItem removeSoftwareUnitItem = new JMenuItem();
	private JScrollPane softwareUnitsPane;
	private JTableSoftwareUnits softwareUnitsTable;

	public SoftwareUnitsJPanel() {
		super();
	}

	/**
	 * Handling ActionPerformed
	 */
	@Override
	public void actionPerformed(ActionEvent action) {
		if (!DefinitionController.getInstance().isAnalysed()) {
			JOptionPane.showMessageDialog(this, ServiceProvider.getInstance().getLocaleService().getTranslatedString("NotAnalysedYet"),
					ServiceProvider.getInstance().getLocaleService().getTranslatedString("NotAnalysedYetTitle"), JOptionPane.ERROR_MESSAGE);
		}
		else {
			if (action.getSource() == addSoftwareUnitButton	|| action.getSource() == addSoftwareUnitItem) {
				addSoftwareUnit(); 
			} else if (action.getSource() == removeSoftwareUnitButton || action.getSource() == removeSoftwareUnitItem) {
				removeSoftwareUnits();
			}
		}
	}

	protected JPanel addButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(createButtonPanelLayout());
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 2));
		buttonPanel.setPreferredSize(new java.awt.Dimension(90, 156));

		addSoftwareUnitButton = new JButton();
		buttonPanel.add(addSoftwareUnitButton, new GridBagConstraints(0, 0, 1,
				1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		addSoftwareUnitButton.addActionListener(this);

		removeSoftwareUnitButton = new JButton();
		buttonPanel.add(removeSoftwareUnitButton, new GridBagConstraints(0, 1,
				1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		removeSoftwareUnitButton.addActionListener(this);

		setButtonTexts();
		return buttonPanel;
	}

	private void addSoftwareUnit() {
		long moduleId = DefinitionController.getInstance().getSelectedModuleId();
		if (moduleId != -1) {
			if (softwareUnitFrame == null) {
				softwareUnitFrame = new SoftwareUnitJDialog(moduleId);
				ServiceProvider.getInstance().getControlService().centerDialog(softwareUnitFrame);
				softwareUnitFrame.setVisible(true);
			} else if (!softwareUnitFrame.isVisible()) {
				ServiceProvider.getInstance().getControlService().centerDialog(softwareUnitFrame);
				softwareUnitFrame.setVisible(true);
			}
		}
	}

	private JScrollPane addSoftwareUnitsTable() {
		softwareUnitsPane = new JScrollPane();
		softwareUnitsTable = new JTableSoftwareUnits();
		softwareUnitsTable.setDragEnabled(true);
		softwareUnitsPane.setViewportView(softwareUnitsTable);
		softwareUnitsTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				createPopup(event);
				setButtonEnableState();
			}

			@Override
			public void mouseEntered(MouseEvent event) {
				createPopup(event);
				setButtonEnableState();
			}

			@Override
			public void mousePressed(MouseEvent event) {
				createPopup(event);
				setButtonEnableState();
			}
		});
		return softwareUnitsPane;
	}

	private GridBagLayout createButtonPanelLayout() {
		GridBagLayout buttonPanelLayout = new GridBagLayout();
		buttonPanelLayout.rowWeights = new double[] { 0.0, 0.0, 0.1 };
		buttonPanelLayout.rowHeights = new int[] { 0, 11, 7 };
		buttonPanelLayout.columnWeights = new double[] { 0.1 };
		buttonPanelLayout.columnWidths = new int[] { 7 };
		return buttonPanelLayout;
	}

	private void createPopup(MouseEvent event) {
		if (SwingUtilities.isRightMouseButton(event)) {
			int row = softwareUnitsTable.rowAtPoint(event.getPoint());
			int column = softwareUnitsTable.columnAtPoint(event.getPoint());
			if (!softwareUnitsTable.isRowSelected(row)) {
				softwareUnitsTable.changeSelection(row, column, false, false);
			}
			popupMenu.show(event.getComponent(), event.getX(), event.getY());
		}
	}

	private void createPopupMenu() {
		addSoftwareUnitItem = new JMenuItem(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Add"));
		addSoftwareUnitItem.addActionListener(this);
		removeSoftwareUnitItem = new JMenuItem(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Remove"));
		removeSoftwareUnitItem.addActionListener(this);

		popupMenu.add(addSoftwareUnitItem);
		popupMenu.add(removeSoftwareUnitItem);
	}

	private void disableButtons() {
		addSoftwareUnitButton.setEnabled(false);
		addSoftwareUnitItem.setEnabled(false);

		removeSoftwareUnitButton.setEnabled(false);
		removeSoftwareUnitItem.setEnabled(false);
	}

	private void enableAddDisableEditRemoveButtons() {
		addSoftwareUnitButton.setEnabled(true);
		addSoftwareUnitItem.setEnabled(true);

		removeSoftwareUnitButton.setEnabled(false);
		removeSoftwareUnitItem.setEnabled(false);
	}

	private void enableButtons() {
		addSoftwareUnitButton.setEnabled(true);
		addSoftwareUnitItem.setEnabled(true);

		removeSoftwareUnitButton.setEnabled(true);
		removeSoftwareUnitItem.setEnabled(true);
	}

	public TableModel getModel() {
		return softwareUnitsTable.getModel();
	}

	public int getSelectedRow() {
		return softwareUnitsTable.getSelectedRow();
	}

	/**
	 * Creating Gui
	 */
	public void initGui() {
		try {
			DefinitionController.getInstance().addObserver(this);
			BorderLayout softwareUnitsPanelLayout = new BorderLayout();
			setLayout(softwareUnitsPanelLayout);
			setBorder(BorderFactory.createTitledBorder(ServiceProvider.getInstance().getLocaleService().getTranslatedString("AssignedSoftwareUnitsTitle")));
			this.add(addSoftwareUnitsTable(), BorderLayout.CENTER);
			this.add(addButtonPanel(), BorderLayout.EAST);
			ServiceProvider.getInstance().getLocaleService().addServiceListener(this);
			createPopupMenu();
			setButtonEnableState();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void removeSoftwareUnits() {
		if (getSelectedRow() != -1) {
			List<String> selectedModules = new ArrayList<String>();
			List<String> types = new ArrayList<String>();
			for (int selectedRow : softwareUnitsTable.getSelectedRows()) {
				String softwareUnitName = (String) softwareUnitsTable.getValueAt(selectedRow, 0);
				String type = (String) softwareUnitsTable.getValueAt(selectedRow, 1);
				selectedModules.add(softwareUnitName);
				types.add(type);
			}
			DefinitionController.getInstance().removeSoftwareUnits(selectedModules, types);

		} else {
			JOptionPane.showMessageDialog(this,ServiceProvider.getInstance().getLocaleService().getTranslatedString("SoftwareunitSelectionError"),
					ServiceProvider.getInstance().getLocaleService().getTranslatedString("WrongSelectionTitle"),
					JOptionPane.ERROR_MESSAGE);

		}
	}

	private void setButtonEnableState() {
		if (DefinitionController.getInstance().getSelectedModuleId() == -1) {
			disableButtons();
		} else if (softwareUnitsTable.getRowCount() == 0 || getSelectedRow() == -1) {
			enableAddDisableEditRemoveButtons();
		} else {
			enableButtons();
		}
	}

	private void setButtonTexts() {
		addSoftwareUnitButton.setText(ServiceProvider.getInstance()
				.getLocaleService().getTranslatedString("Add"));
		removeSoftwareUnitButton.setText(ServiceProvider.getInstance()
				.getLocaleService().getTranslatedString("Remove"));
	}

	@Override
	public void update() {
		setButtonTexts();
		setBorder(BorderFactory.createTitledBorder(ServiceProvider.getInstance().getLocaleService().getTranslatedString("AssignedSoftwareUnitsTitle")));
		softwareUnitsTable.changeColumnHeaders();
	}

	// Observer
	@Override
	public void update(Observable o, Object arg) {
		updateSoftwareUnitTable();
		setButtonEnableState();
	}

	public void updateSoftwareUnitTable() {
		try {
			JTableTableModel suTableModel = (JTableTableModel) softwareUnitsTable.getModel();
			suTableModel.getDataVector().removeAllElements();
			long selectedModuleId = DefinitionController.getInstance().getSelectedModuleId();

			if (selectedModuleId != -1) {
				ArrayList<String> softwareUnitNames = DefinitionController.getInstance().getSoftwareUnitNamesBySelectedModule(selectedModuleId);
				if (softwareUnitNames != null) {
					for (String softwareUnitName : softwareUnitNames) {
						String softwareUnitType = DefinitionController.getInstance().getSoftwareUnitTypeBySoftwareUnitName(softwareUnitName);
						Object rowdata[] = { softwareUnitName, softwareUnitType };
						suTableModel.addRow(rowdata);
					}
				}
			}
			suTableModel.fireTableDataChanged();

		} catch (Exception e) {
			//e.printStackTrace();
			logger.error(e.getMessage());
			UiDialogs.errorDialog(this, e.getMessage());
		}
	}
}
