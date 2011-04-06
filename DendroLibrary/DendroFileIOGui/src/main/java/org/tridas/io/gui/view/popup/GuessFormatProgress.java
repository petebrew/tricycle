/*******************************************************************************
 * Copyright 2011 Peter Brewer and Daniel Murphy
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
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.tridas.io.gui.I18n;
import org.tridas.io.gui.control.CancelGuessFormatEvent;
import org.tridas.io.gui.model.popup.GuessFormatDialogModel;


public class GuessFormatProgress extends JDialog {

	private static final long serialVersionUID = 1L;
	private JProgressBar progress;
	private JButton cancelButton;
	private final GuessFormatDialogModel model;
	
	public GuessFormatProgress(JFrame parent, GuessFormatDialogModel argModel) {
		super(parent);
		model = argModel;
		initializeComponents();
		linkModel();
		addListeners();
		pack();
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}
	
	private void initializeComponents() {
		progress = new JProgressBar();
		cancelButton = new JButton();
		cancelButton.setText(I18n.getText("general.cancel"));
		setLayout(new GridLayout(0, 1));
		
		progress.setMinimum(0);
		progress.setMaximum(100);
		progress.setStringPainted(true);
		
		JPanel bottom = new JPanel();
		bottom.setLayout(new BorderLayout());
		bottom.add(progress, "Center");
		bottom.add(cancelButton, "East");
		add(bottom);
		setPreferredSize(new Dimension(350, 75));
	}
	
	private void addListeners() {
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CancelGuessFormatEvent event = new CancelGuessFormatEvent(model);
				event.dispatch();
			}
		});
	}
	
	private void linkModel() {
		
		progress.setValue(model.getProgressPercent());
		
		model.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				String prop = evt.getPropertyName();
				
				if (prop.equals("progressPercent")) {
					int percent = (Integer) evt.getNewValue();
					progress.setValue(percent);
				}
			}
		});
	}
	
}
