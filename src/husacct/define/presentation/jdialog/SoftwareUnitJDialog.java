package husacct.define.presentation.jdialog;

import husacct.ServiceProvider;
import husacct.common.Resource;
import husacct.control.ControlServiceImpl;
import husacct.define.presentation.moduletree.AnalyzedModuleTree;
import husacct.define.presentation.utils.DefaultMessages;
import husacct.define.task.PopUpController;
import husacct.define.task.SoftwareUnitController;
import husacct.define.task.components.AnalyzedModuleComponent;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class SoftwareUnitJDialog extends JDialog implements ActionListener, KeyListener {

	private static final long serialVersionUID = 3093579720278942807L;
	
	private JPanel UIMappingPanel;
	private JPanel regExMappingPanel;
	
	public JButton saveButton;
	public JButton cancelButton;
	
	public JRadioButton UIMapping;
	public JRadioButton regExMapping;
	
	public AnalyzedModuleTree softwareDefinitionTree;
	private SoftwareUnitController softwareUnitController;
	
	public SoftwareUnitJDialog(long moduleId) {
		super(((ControlServiceImpl) ServiceProvider.getInstance().getControlService()).getMainController().getMainGui(), true);
		this.softwareUnitController = new SoftwareUnitController(moduleId);
		this.softwareUnitController.setAction(PopUpController.ACTION_NEW);
		initUI();
	}

	/**
	 * Creating Gui
	 */
	private void initUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			setTitle(ServiceProvider.getInstance().getLocaleService().getTranslatedString("SoftwareUnitTitle"));
			setIconImage(new ImageIcon(Resource.get(Resource.HUSACCT_LOGO)).getImage());
			
			this.getContentPane().add(this.createTypeSelectionPanel(), BorderLayout.NORTH);
			this.getContentPane().add(this.createUIMappingPanel(), BorderLayout.CENTER);
			this.getContentPane().add(this.createButtonPanel(), BorderLayout.SOUTH);
			
			this.setResizable(false);
			this.setSize(650, 300);
			this.pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private JPanel createTypeSelectionPanel() {
		JPanel typeSelectionPanel = new JPanel();
		typeSelectionPanel.setLayout(new GridLayout(2,2));
		typeSelectionPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		
		JLabel typeSelectionLabel = new JLabel(ServiceProvider.getInstance().getLocaleService().getTranslatedString("SelectSoftwareDefinitionType"));
		typeSelectionPanel.add(typeSelectionLabel);
		typeSelectionPanel.add(new JLabel(""));
		
		UIMapping = new JRadioButton(ServiceProvider.getInstance().getLocaleService().getTranslatedString("UIMapping"));
		UIMapping.setSelected(true);
		UIMapping.addActionListener(this);
		
		regExMapping = new JRadioButton(ServiceProvider.getInstance().getLocaleService().getTranslatedString("RegExMapping"));
		regExMapping.addActionListener(this);
		
		ButtonGroup mappingRadioButtonsGroup = new ButtonGroup();
		mappingRadioButtonsGroup.add(UIMapping);
		mappingRadioButtonsGroup.add(regExMapping);
		
		typeSelectionPanel.add(UIMapping);
		typeSelectionPanel.add(regExMapping);
		
		return typeSelectionPanel;
	}
	
	private JPanel createUIMappingPanel() {
		if(regExMappingPanel != null)
			this.getContentPane().remove(regExMappingPanel);
		
		UIMappingPanel = new JPanel();
		UIMappingPanel.setLayout(this.createUIMappingPanelLayout());
		UIMappingPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		
		JLabel softwareUnitsLabel = new JLabel(ServiceProvider.getInstance().getLocaleService().getTranslatedString("SelectSoftwareDefinition"));
		UIMappingPanel.add(softwareUnitsLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		UIMappingPanel.add(this.getUIMappingScrollPane(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 220));
		return UIMappingPanel;
	}
	
	private GridBagLayout createUIMappingPanelLayout() {
		GridBagLayout UIMappingPanelLayout = new GridBagLayout();
		UIMappingPanelLayout.rowWeights = new double[] { 0.0, 0.0, 0.1 };
		UIMappingPanelLayout.rowHeights = new int[] { 25, 25, 220 };
		UIMappingPanelLayout.columnWeights = new double[] { 0.0 };
		UIMappingPanelLayout.columnWidths = new int[] { 500 };
		return UIMappingPanelLayout;
	}
	
	private JScrollPane getUIMappingScrollPane() {
		JScrollPane softwareUnitScrollPane = new JScrollPane();
		softwareUnitScrollPane.setSize(400, 220);
		softwareUnitScrollPane.setPreferredSize(new java.awt.Dimension(500, 220));
		AnalyzedModuleComponent rootComponent = this.softwareUnitController.getSoftwareUnitTreeComponents();
		this.softwareDefinitionTree = new AnalyzedModuleTree(rootComponent);
		softwareUnitScrollPane.setViewportView(this.softwareDefinitionTree);
		return softwareUnitScrollPane;
	}
	
	private JPanel createRegExMappingPanel() {
		this.getContentPane().remove(UIMappingPanel);

		regExMappingPanel = new JPanel();
		regExMappingPanel.setLayout(new GridLayout(2,2));
		regExMappingPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		
		JLabel regExLabel = new JLabel();
		regExMappingPanel.add(regExLabel);
		regExLabel.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("RegularExpression"));
		
		JTextField regExTextfield = new JTextField();
		regExTextfield.setToolTipText(DefaultMessages.TIP_MODULE);
		regExMappingPanel.add(regExTextfield);
		
		return regExMappingPanel;
	}
	
	private GridBagLayout createRegExMappingPanelLayout() {
		GridBagLayout regExPanelLayout = new GridBagLayout();
		regExPanelLayout.rowWeights = new double[] { 0.0, 0.0, 0.1 };
		regExPanelLayout.rowHeights = new int[] { 25, 25, 220 };
		regExPanelLayout.columnWeights = new double[] { 0.0 };
		regExPanelLayout.columnWidths = new int[] { 500 };
		return regExPanelLayout;
	}
	
	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel();
		
		saveButton = new JButton(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Add"));
		buttonPanel.add(saveButton);
		saveButton.addActionListener(this);
		
		cancelButton = new JButton(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Cancel"));
		buttonPanel.add(cancelButton);
		cancelButton.addActionListener(this);
		
		return buttonPanel;
	}

	@Override
	public void actionPerformed(ActionEvent action) {
		if (action.getSource() == this.saveButton) {
			this.save();
		} else if (action.getSource() == this.cancelButton) {
			this.cancel();
		}
		else if (action.getSource() == this.UIMapping) {
			System.out.println(this.getContentPane().getComponentCount());
			this.getContentPane().add(this.createUIMappingPanel(), BorderLayout.CENTER);
			this.pack();
			this.repaint();
		}
		else if (action.getSource() == this.regExMapping) {
			System.out.println(this.getContentPane().getComponentCount());
			this.getContentPane().add(this.createRegExMappingPanel(), BorderLayout.CENTER);
			this.pack();
			this.repaint();
		}
	}
	
	/**
	 * Do nothing
	 */
	@Override
	public void keyPressed(KeyEvent event) {
		
	}
	
	@Override
	public void keyReleased(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
			this.dispose();
		} else if (event.getKeyCode() == KeyEvent.VK_ENTER) {
			this.save();
		}
	}
	
	/**
	 * Do nothing
	 */
	@Override
	public void keyTyped(KeyEvent event) {
		
	}

	private void save() {
		TreeSelectionModel paths = this.softwareDefinitionTree.getSelectionModel();
		for (TreePath path : paths.getSelectionPaths()){
			AnalyzedModuleComponent selectedComponent = (AnalyzedModuleComponent) path.getLastPathComponent();
			this.softwareUnitController.save(selectedComponent.getUniqueName(), selectedComponent.getType());			
		}
		this.dispose();
		
		
//		TreePath path = this.softwareDefinitionTree.getSelectionPath();
	}
	
	private void cancel() {
		this.dispose();
	}
}
