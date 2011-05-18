/*******************************************************************************
 * Copyright 2011 Daniel Murphy and Peter Brewer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.tridas.io.gui.view.popup;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.tridas.io.gui.I18n;
import org.tridas.io.gui.model.popup.OverwriteModel;
import org.tridas.io.gui.model.popup.OverwriteModel.Response;

/**
 * @author daniel
 *
 */
public class OverwritePopup extends JDialog {
	private static final long serialVersionUID = 1L;

	private final OverwriteModel model;
	
	private JLabel message;
	private JLabel checkBoxText;
	private JButton overwrite;
	private JButton ignore;
	private JButton rename;
	private JCheckBox applyToAll;
	
	public OverwritePopup(OverwriteModel argModel, JFrame argParent){
		super(argParent, true);
		model = argModel;
		initComponents();
		linkModel();
		addListeners();
		populateLocale();
		pack();
		setLocationRelativeTo(argParent);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	}

	/**
	 * 
	 */
	private void initComponents() {
		overwrite = new JButton();
		ignore = new JButton();
		rename = new JButton();
		applyToAll = new JCheckBox();
		message = new JLabel();
		checkBoxText = new JLabel();
		
		setLayout(new BorderLayout());
		add(message, "Center");
		
		Box box = Box.createHorizontalBox();
		box.add(overwrite);
		box.add(ignore);
		box.add(rename);
		box.add(Box.createHorizontalGlue());
		box.add(checkBoxText);
		box.add(applyToAll);
		add(box, "South");
	}

	private void linkModel() {
		message.setText(model.getMessage());
		applyToAll.setSelected(model.isAll());
		
		model.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent argEvt) {
				String n = argEvt.getPropertyName();
				if(n.equals("all")){
					applyToAll.setSelected((Boolean)argEvt.getNewValue());
				}else if(n.equals("message")){
					message.setText(argEvt.getNewValue().toString());
				}
			}
		});
	}

	private void addListeners() {
		applyToAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent argE) {
				// BAD! shouldn't be doing this, but it's fine here
				// should be using a controller for this
				model.setAll(applyToAll.isSelected());
			}
		});
		
		overwrite.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent argE) {
				// BAD! shouldn't be doing this, but it's fine here
				// should be using a controller for this
				model.setResponse(Response.OVERWRITE);
				setVisible(false);
			}
		});
		
		ignore.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent argE) {
				// BAD! shouldn't be doing this, but it's fine here
				// should be using a controller for this
				model.setResponse(Response.IGNORE);
				setVisible(false);
			}
		});
		
		rename.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent argE) {
				// BAD! shouldn't be doing this, but it's fine here
				// should be using a controller for this
				model.setResponse(Response.RENAME);
				setVisible(false);
			}
		});
	}
	
	private void populateLocale(){
		checkBoxText.setText(I18n.getText("view.popup.overwrite.apply"));
		ignore.setText(I18n.getText("view.popup.overwrite.ignore"));
		overwrite.setText(I18n.getText("view.popup.overwrite.overwrite"));
		rename.setText(I18n.getText("view.popup.overwrite.rename"));
		setTitle(I18n.getText("view.popup.overwrite.overwrite"));
	}
}
