/**
 * Copyright 2010 Daniel Murphy
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
/**
 * Created on Jun 7, 2010, 2:51:29 PM
 */
package org.tridas.io.gui.view.popup;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.tridas.io.gui.I18n;
import org.tridas.io.gui.control.convert.ConvertController;
import org.tridas.io.gui.model.TricycleModelLocator;
import org.tridas.io.gui.model.popup.ConvertingDialogModel;

import com.dmurph.mvc.MVCEvent;

/**
 * @author Daniel Murphy
 */
@SuppressWarnings("serial")
public class ConvertProgress extends JDialog {
	private JLabel convertingLabel;
	private JProgressBar progress;
	private JButton cancelButton;
	
	private final ConvertingDialogModel model;
	
	public ConvertProgress(JFrame parent, ConvertingDialogModel argModel) {
		super(parent, true);
		model = argModel;
		initializeComponents();
		populateLocale();
		linkModel();
		addListeners();
		pack();
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	}
	
	private void initializeComponents() {
		convertingLabel = new JLabel();
		progress = new JProgressBar();
		cancelButton = new JButton();
		
		setLayout(new GridLayout(0, 1));
		
		progress.setMinimum(0);
		progress.setMaximum(100);
		progress.setStringPainted(true);
		
		add(convertingLabel);
		JPanel bottom = new JPanel();
		bottom.setLayout(new BorderLayout());
		bottom.add(progress, "Center");
		bottom.add(cancelButton, "East");
		add(bottom);
		setPreferredSize(new Dimension(350, 75));
	}
	
	private void addListeners() {
//		addWindowListener(new WindowAdapter() {
//			public void windowClosing(WindowEvent e) {
//				setVisible(false);
//			}
//		});
		
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MVCEvent event = new MVCEvent(ConvertController.CANCEL_CONVERT);
				event.dispatch();
			}
		});
	}
	
	private void populateLocale() {
		setIconImage(TricycleModelLocator.getInstance().getWindowIcon().getImage());
		setTitle(I18n.getText("view.popup.convert.title"));
		setConvertingFilename("");
		cancelButton.setText(I18n.getText("general.cancel"));
	}
	
	private void setConvertingFilename(String argFilename) {
		if (argFilename == null) {
			argFilename = "";
		}
		convertingLabel.setText(I18n.getText("view.popup.convert.label", argFilename));
	}
	
	private void linkModel() {
		if (model.getConvertingFilename() != null) {
			setConvertingFilename(model.getConvertingFilename());
		}
		
		progress.setValue(model.getConvertingPercent());
		
		model.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				String prop = evt.getPropertyName();
				
				if (prop.equals("convertingPercent")) {
					int percent = (Integer) evt.getNewValue();
					progress.setValue(percent);
				}
				else if (prop.equals("convertingFilename")) {
					setConvertingFilename(evt.getNewValue().toString());
				}
			}
		});
	}
}
