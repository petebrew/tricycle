/**
 * Created at Jun 15, 2010, 8:39:53 PM
 */
package org.tridas.io.gui.view.popup;

import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EventObject;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.tridas.io.defaults.AbstractDefaultValue;
import org.tridas.io.defaults.values.StringDefaultValue;
import org.tridas.io.gui.components.DefaultFieldEditor;
import org.tridas.io.gui.components.editors.StringValueEditor;
import org.tridas.io.gui.model.popup.MetadataEditorModel;

/**
 * @author daniel
 *
 */
@SuppressWarnings("serial")
public class MetadataEditor extends JDialog {
	
	private JTable table;
	private MetadataEditorModel model;
	private TableModelListener listener;
	
	public MetadataEditor(JFrame argParent, MetadataEditorModel argModel){
		super(argParent, true);
		model = argModel;
		
		initComponents();
		populateLocale();
		linkModel();
		addListeners();
		pack();
		setLocationRelativeTo(argParent);
	}

	/**
	 * 
	 */
	private void initComponents() {
		table = new JTable();
		
		table.setDefaultEditor(AbstractDefaultValue.class, new DefaultFieldEditor());
		table.setDefaultRenderer(AbstractDefaultValue.class, new FieldRenderer());
		add(new JScrollPane(table), "Center");
	}

	/**
	 * 
	 */
	private void populateLocale() {
		
	}
	
	/**
	 * 
	 */
	private void linkModel() {
		table.setModel(model.getTableModel());
		setTitle(model.getFilename());
		
		model.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				String prop = evt.getPropertyName();
				if(prop.equals("filename")){
					setTitle(evt.getNewValue().toString());
				}else if(prop.equals("tableModel")){
					TableModel tmodel = (TableModel) evt.getNewValue();
					table.getModel().removeTableModelListener(listener);
					table.setModel(tmodel);
					tmodel.addTableModelListener(listener);
				}
			}
		});
	}
	
	private void addListeners() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});
	}
}

@SuppressWarnings("serial")
class FieldRenderer extends DefaultTableCellRenderer implements TableCellRenderer{
	/**
	 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable argTable, Object argValue, boolean argIsSelected,
			boolean argHasFocus, int argRow, int argColumn) {
		String value = ((AbstractDefaultValue<?>)argValue).getStringValue();
		return super.getTableCellRendererComponent(argTable, value, argIsSelected, argHasFocus, argRow, argColumn);
	}
}
