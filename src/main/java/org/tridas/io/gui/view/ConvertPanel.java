/**
 * Copyright 2010 Daniel Murphy and Peter Brewer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tridas.io.gui.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import net.miginfocom.swing.MigLayout;

import org.codehaus.plexus.util.FileUtils;
import org.tridas.io.exceptions.ConversionWarning;
import org.tridas.io.gui.I18n;
import org.tridas.io.gui.command.ConvertCommand.DendroWrapper;
import org.tridas.io.gui.command.ConvertCommand.StructWrapper;
import org.tridas.io.gui.components.CustomTreeCellRenderer;
import org.tridas.io.gui.control.convert.ConvertController;
import org.tridas.io.gui.control.convert.SaveEvent;
import org.tridas.io.gui.model.ConvertModel;
import org.tridas.io.gui.model.TricycleModel;
import org.tridas.io.gui.model.TricycleModelLocator;
import org.tridas.io.util.IOUtils;

import com.dmurph.mvc.ObjectEvent;

/**
 * @author Daniel
 */
@SuppressWarnings("serial")
public class ConvertPanel extends JPanel {

	private static final String iconSize = "16x16";
	private JScrollPane scrollPane;
	private JTree convertedTree;
	private JButton btnSaveAll;
	private JButton expandAll;
	private JButton collapseAll;
	private JLabel results;
	private JButton btnPreview;
	private StyledDocument doc;

	private DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Conversion Data");
	
	private final ConvertModel model;
	private JPanel panel;
	private JPanel panel_1;
	private JSplitPane splitPane;
	private JScrollPane scrollPaneWarnings;
	private JTextPane txtWarnings;
	private JLabel lblConversionWarnings;
	
	public TricycleAction actionSaveAll;
	public TricycleAction actionPreviewFile;
	
	public ConvertPanel(ConvertModel argModel) {
		initActions();
		model = argModel;
		initializeComponents();
		populateLocale();
		linkModel();
		addListeners();
		
		
	}
	
	private void initActions()
	{
		ImageIcon icnSave = null;
		ImageIcon icnPreview = null;
		
		try{
			icnSave = new ImageIcon(IOUtils.getFileInJarURL("icons/22x22/filesave.png"));
			icnPreview = new ImageIcon(IOUtils.getFileInJarURL("icons/22x22/preview.png"));
		}  catch (NullPointerException e)
		{
			e.printStackTrace();
		}
	
			
		actionSaveAll = new TricycleAction("Save all", icnSave){

				@Override
				public void actionPerformed(ActionEvent event) {
					doSave();
				}
		};

		actionPreviewFile = new TricycleAction("Preview", icnPreview){

			@Override
			public void actionPerformed(ActionEvent evt) {
				if(model.getSelectedNode() == null){
					return;
				}
				ObjectEvent<DefaultMutableTreeNode> event = new ObjectEvent<DefaultMutableTreeNode>(
						ConvertController.PREVIEW, model.getSelectedNode());
				event.dispatch();
			}
		};
	}
	
	private void initializeComponents() {
		
		
		setLayout(new MigLayout("", "[450px,grow]", "[35px][grow][]"));
		
		
		panel = new JPanel();
		add(panel, "cell 0 0,grow");
		panel.setLayout(new MigLayout("", "[222.00px][][119px,grow][][89px]", "[25px]"));
		btnPreview = new JButton();
		btnPreview.setAction(actionPreviewFile);
		panel.add(btnPreview, "flowx,cell 0 0,alignx left,aligny top");
		btnSaveAll = new JButton();
		btnSaveAll.setAction(actionSaveAll);
		panel.add(btnSaveAll, "cell 4 0,alignx left,aligny top");
		
	
		ImageIcon ficon     = new ImageIcon(IOUtils.getFileInJarURL("icons/"+iconSize+"/fail.png"));
		ImageIcon wicon     = new ImageIcon(IOUtils.getFileInJarURL("icons/"+iconSize+"/warning.png"));
		ImageIcon sicon     = new ImageIcon(IOUtils.getFileInJarURL("icons/"+iconSize+"/success.png"));
		ImageIcon infoicon  = new ImageIcon(IOUtils.getFileInJarURL("icons/"+iconSize+"/info.png"));
		ImageIcon filewicon = new ImageIcon(IOUtils.getFileInJarURL("icons/"+iconSize+"/filewarning.png"));
		ImageIcon filesicon = new ImageIcon(IOUtils.getFileInJarURL("icons/"+iconSize+"/filesuccess.png"));
		
		DefaultTreeModel model = new DefaultTreeModel(rootNode, false);
		
		splitPane = new JSplitPane();
		splitPane.setOneTouchExpandable(true);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		
		add(splitPane, "cell 0 1,grow");
		scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);
		convertedTree = new JTree();
		
				CustomTreeCellRenderer renderer = new CustomTreeCellRenderer(sicon, wicon, ficon, filesicon, filewicon, infoicon);
				ToolTipManager.sharedInstance().registerComponent(convertedTree);
				convertedTree.setCellRenderer(renderer);
				convertedTree.setModel(model);
				convertedTree.setRootVisible(false);
				convertedTree.setShowsRootHandles(true);
				convertedTree.expandRow(0);
		scrollPane.setViewportView(convertedTree);
		
		JPanel panelWarning = new JPanel();
		panelWarning.setLayout(new MigLayout("", "[9px,grow,fill]", "[][9px,grow,fill]"));
		
		lblConversionWarnings = new JLabel("Conversion warnings:");
		panelWarning.add(lblConversionWarnings, "cell 0 0");
		
		scrollPaneWarnings = new JScrollPane();
		panelWarning.add(scrollPaneWarnings, "cell 0 1,alignx left,aligny top");
		splitPane.setRightComponent(panelWarning);
		
		txtWarnings = new JTextPane();
		txtWarnings.setEditable(false);
		txtWarnings.setContentType("text/html");
		
		scrollPaneWarnings.setViewportView(txtWarnings);
		
		panel_1 = new JPanel();
		add(panel_1, "cell 0 2,grow");
		panel_1.setLayout(new MigLayout("", "[][grow][][]", "[]"));
		results = new JLabel();
		panel_1.add(results, "cell 0 0");
		collapseAll = new JButton();
		panel_1.add(collapseAll, "cell 2 0");
		expandAll = new JButton();
		panel_1.add(expandAll, "cell 3 0");
		expandAll.putClientProperty( "JButton.buttonType", "segmentedTextured" );
		expandAll.putClientProperty( "JButton.segmentPosition", "last" );
		
		

		collapseAll.putClientProperty( "JButton.buttonType", "segmentedTextured" );
		collapseAll.putClientProperty( "JButton.segmentPosition", "first" );
		
		splitPane.setDividerLocation(200);
		splitPane.setResizeWeight(1);
		
		
		doc = txtWarnings.getStyledDocument();
		
        Style def = StyleContext.getDefaultStyleContext().
                getStyle(StyleContext.DEFAULT_STYLE);

		Style regular = doc.addStyle("regular", def);
		StyleConstants.setFontFamily(def, "SansSerif");
		
		Style s = doc.addStyle("italic", regular);
		StyleConstants.setItalic(s, true);
		
		s = doc.addStyle("bold", regular);
		StyleConstants.setBold(s, true);
		
		s = doc.addStyle("small", regular);
		StyleConstants.setFontSize(s, 8);
		
		s = doc.addStyle("large", regular);
		StyleConstants.setBold(s, true);
		StyleConstants.setFontSize(s, 16);
		
		s = doc.addStyle("largered", regular);
		StyleConstants.setForeground(s, Color.RED);
		StyleConstants.setBold(s, true);
		StyleConstants.setFontSize(s, 16);
		
		s = doc.addStyle("warningicon", regular);
        StyleConstants.setAlignment(s, StyleConstants.ALIGN_LEFT);
        if (wicon != null) {
            StyleConstants.setIcon(s, wicon);
        }
		
		s = doc.addStyle("erroricon", regular);
        StyleConstants.setAlignment(s, StyleConstants.ALIGN_LEFT);
        if (ficon != null) {
            StyleConstants.setIcon(s, ficon);
        }
        
        
        
	}
	
	public void doSave()
	{
		SaveEvent event = new SaveEvent();
		event.dispatch();
	}
	
	private void addListeners() {
		
		
		expandAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// no need to go to controller
				expandAll();
			}
		});
		
		collapseAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// no need to go to controller
				collapseAll();
			}
		});
		
		convertedTree.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int selRow = convertedTree.getRowForLocation(e.getX(), e.getY());
				TreePath selPath = convertedTree.getPathForLocation(e.getX(), e.getY());
				if (selRow > 0) {
					if (e.getClickCount() == 2) {
						DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath.getLastPathComponent();
						if(node.getUserObject() instanceof DendroWrapper){ // means it's a file node
							ObjectEvent<DefaultMutableTreeNode> event = new ObjectEvent<DefaultMutableTreeNode>(
									ConvertController.PREVIEW, node);
							event.dispatch();
						}
					}
				}
			}
		});
		
		convertedTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent argE) {
				
				// BAD PRACTICE this should be an event to the controller
				if(argE != null && argE.getNewLeadSelectionPath() != null && argE.getNewLeadSelectionPath().getLastPathComponent() != null){
					model.setSelectedNode((DefaultMutableTreeNode) argE.getNewLeadSelectionPath().getLastPathComponent());
				}else{
					model.setSelectedNode(null);
				}
			}
		});
		
	}
	
	private void populateLocale() {
		
		collapseAll.setText(I18n.getText("view.convert.collapse"));
		expandAll.setText(I18n.getText("view.convert.expand"));
		btnPreview.setText("Preview selected file");
		
	}
	
	private void linkModel() {
		setStatus(model.getProcessed(), model.getFailed(), model.getConvWithWarnings());
		DefaultTreeModel treeModel = (DefaultTreeModel) convertedTree.getModel();
		for (DefaultMutableTreeNode node : model.getNodes()) {
			treeModel.insertNodeInto(node, rootNode, rootNode.getChildCount());
		}
		expandToFiles();
		
		
		if(model.getConvertedList().isEmpty()){
			actionSaveAll.setEnabled(false); // for issue 233
		}
		
		if(model.getSelectedNode() == null){
			actionPreviewFile.setEnabled(false);
		}
		
		model.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				String prop = evt.getPropertyName();
				
				if (prop.equals("nodes")) {
					DefaultTreeModel treeModel = (DefaultTreeModel) convertedTree.getModel();
					rootNode.removeAllChildren();
					for (DefaultMutableTreeNode node : model.getNodes()) {
						rootNode.add(node);
					}
					treeModel.setRoot(rootNode);
					expandToFiles();
							
				}
				else if (prop.equals("processed")) {
					setStatus(model.getProcessed(), model.getFailed(), model.getConvWithWarnings());
				}
				else if (prop.equals("failed")) {
					setStatus(model.getProcessed(), model.getFailed(), model.getConvWithWarnings());
				}
				else if (prop.equals("convWithWarnings")) {
					setStatus(model.getProcessed(), model.getFailed(), model.getConvWithWarnings());
				}/*else if (prop.equals("outputFormat")) {
					outputFormat.setSelectedItem(evt.getNewValue());
				}*/
				else if(prop.equals("selectedNode")){
					DefaultMutableTreeNode node = model.getSelectedNode();
					txtWarnings.setText("");
					
					if(node == null){
						actionPreviewFile.setEnabled(false);	
					}
					else if(node.getUserObject() instanceof DendroWrapper){ // means it's a file
						actionPreviewFile.setEnabled(true);
					}else{
						actionPreviewFile.setEnabled(false);
					}
					
					setMessagesForNode(node);		
					
					
				}
			}
		});
		
		TricycleModel mwm = TricycleModelLocator.getInstance().getTricycleModel();
		mwm.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				
				if (evt.getPropertyName().equals("lock")) {
					boolean lock = (Boolean) evt.getNewValue();
					if (lock) {
						//convertButton.setEnabled(false);
						actionSaveAll.setEnabled(false);
					}
					else {
						//convertButton.setEnabled(true);
						if(!model.getConvertedList().isEmpty()){
							actionSaveAll.setEnabled(true); // for issue 233
						}else{
							actionSaveAll.setEnabled(false);
						}
					}
				}
			}
		});
	}
	
	
	private void appendConversionMsg(String s, String styleName) {
		   try {
		      
		      doc.insertString(doc.getLength(), s, doc.getStyle(styleName));
		   } catch(BadLocationException exc) {
		      exc.printStackTrace();
		   }
	}
	
	
	private void setMessagesForNode(DefaultMutableTreeNode node)
	{
		txtWarnings.setText("");

		if(node==null || node.getUserObject()==null) return;
			
		
		if(node.getUserObject() instanceof StructWrapper)
		{
			boolean hasMessages = false;
			
			StructWrapper ob = (StructWrapper) node.getUserObject();
			try{
				if(!ob.struct.errorMessage.isEmpty())
				{
					appendConversionMsg("   ", "erroricon");
					appendConversionMsg("   Fatal error converting file\n", "largered");
					appendConversionMsg("\n", "small");
					appendConversionMsg(ob.struct.errorMessage, "regular");
					toggleSplitPane(JSplitPane.BOTTOM, true);
					return;
				}
			} catch (NullPointerException e){}
			
			try{
				if(ob.struct.reader.getWarnings()!=null && ob.struct.reader.getWarnings().length>0)
				{

					appendConversionMsg("   ", "warningicon");
					appendConversionMsg("   There were some issues reading the input file '"+FileUtils.basename(ob.struct.reader.getOriginalFilename(), "ggg")+"'\n", "large");
					appendConversionMsg("\n", "small");
					hasMessages = true;
					
					HashSet<String> warnings = new HashSet<String>();
					for(ConversionWarning warning : ob.struct.reader.getWarnings())
					{
						warnings.add(warning.getMessage());					
					}
					
					Iterator<String> it = warnings.iterator();
					while(it.hasNext())
					{
						appendConversionMsg("   - "+it.next()+"\n", "regular");
					}
					
					appendConversionMsg("\n", "large");
					
					
				}
			}catch (NullPointerException e){}
			
			try{
				if(ob.struct.writer.getWarnings()!=null && ob.struct.writer.getWarnings().length>0)
				{
					appendConversionMsg("   ", "warningicon");
					appendConversionMsg("   There were some issues writing to "+ob.struct.writer.getShortName()+" format\n", "large");
					appendConversionMsg("\n", "small");
					hasMessages = true;
					
					HashSet<String> warnings = new HashSet<String>();
					for(ConversionWarning warning : ob.struct.writer.getWarnings())
					{
						warnings.add(warning.getMessage());					
					}
					
					Iterator<String> it = warnings.iterator();
					while(it.hasNext())
					{
						appendConversionMsg("   - "+it.next()+"\n", "regular");
					}
				}
			}catch (NullPointerException e){}
			
			if(hasMessages==false)
			{
				toggleSplitPane(JSplitPane.BOTTOM, false);
				appendConversionMsg("File converted successfully with no warnings", "large");

			}
			else
			{
			
				toggleSplitPane(JSplitPane.BOTTOM, true);
			}
		}
		else if(node.getUserObject() instanceof DendroWrapper){
			DendroWrapper wrapper = (DendroWrapper) node.getUserObject();
			if(wrapper.file.getDefaults().getWarnings().size() != 0){
				
				appendConversionMsg("   ", "warningicon");
				appendConversionMsg("   There were some issues writing this file\n", "large");
				appendConversionMsg("\n", "small");
				
				HashSet<String> warnings = new HashSet<String>();
				for(ConversionWarning warning : wrapper.file.getDefaults().getWarnings())
				{
					warnings.add(warning.getMessage());					
				}
				
				Iterator<String> it = warnings.iterator();
				while(it.hasNext())
				{
					appendConversionMsg("   - "+it.next()+"\n", "regular");
				}
				
				toggleSplitPane(JSplitPane.BOTTOM, true);
			}
			else
			{
				appendConversionMsg("File converted successfully with no warnings", "large");
				toggleSplitPane(JSplitPane.BOTTOM, false);
			}
			
		}
		else
		{
			toggleSplitPane(JSplitPane.BOTTOM, false);
		}
	}
	
	private void expandToFiles() {
		int row = 0;
		while (row < convertedTree.getRowCount()) {
			if (convertedTree.getPathForRow(row).getPathCount() < 3) {
				convertedTree.expandRow(row);
			}
			row++;
		}
	}
	
	private void setStatus(int argProcessed, int argFailed, int argConvWithWarnings) {
		results.setText(I18n.getText("view.convert.status", argProcessed+"", argFailed+"", argConvWithWarnings+""));
	}
	
	private void expandAll() {
		for (int i = 0; i < convertedTree.getRowCount(); i++) {
			convertedTree.expandRow(i);
		}
	}
	
	private void collapseAll() {
		for (int i = convertedTree.getRowCount() - 1; i >= 0; i--) {
			convertedTree.collapseRow(i);
		}
	}
	
	/**
	 * toggle JSplitPane
	 * @param sp - splitpane to toggle
	 * @param upLeft - is it left or top component to collapse? or button or right
	 * @param setVisible - 
	 */
	public void toggleSplitPane(String whichpanel, boolean setVisible) {
	    
		JSplitPane sp = this.splitPane; 
		boolean upLeft = whichpanel.equals(JSplitPane.TOP) || whichpanel.equals(JSplitPane.LEFT);
		
		
		try {
	        Field buttonField;

	        //get field button from divider
	        if (upLeft) {
	            if (setVisible != (sp.getDividerLocation() < sp.getMinimumDividerLocation())) {
	                return;
	            }
	            buttonField = BasicSplitPaneDivider.class.getDeclaredField(setVisible ? "rightButton" : "leftButton");
	        } else {
	            if (setVisible != (sp.getDividerLocation() > sp.getMaximumDividerLocation())) {
	                return;
	            }

	            buttonField = BasicSplitPaneDivider.class.getDeclaredField(setVisible ? "leftButton" : "rightButton");
	        }
	        //allow access
	        buttonField.setAccessible(true);
	        //get instance of button to click at
	        JButton button = (JButton) buttonField.get(((BasicSplitPaneUI) sp.getUI()).getDivider());
	        //click it
	        button.doClick();
	        //if you manage more dividers at same time before returning from event,
	        //you should update layout and ui, otherwise nothing happens on some dividers:
	        sp.updateUI();
	        sp.doLayout();


	    } catch (Exception e) {
	       
	    }
	}
}
