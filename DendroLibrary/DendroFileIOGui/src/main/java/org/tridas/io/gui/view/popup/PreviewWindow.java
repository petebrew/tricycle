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
 * Created on Jun 7, 2010, 6:33:38 PM
 */
package org.tridas.io.gui.view.popup;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.tridas.io.gui.I18n;
import org.tridas.io.gui.model.TricycleModelLocator;
import org.tridas.io.gui.model.popup.PreviewModel;

/**
 * @author Daniel Murphy
 */
@SuppressWarnings("serial")
public class PreviewWindow extends JFrame {
	
	private final PreviewModel model;
	private JTextArea text;
	private JButton copy;
	
	public PreviewWindow(JFrame argParent, int argWidth, int argHeight, PreviewModel argModel) {
		model = argModel;
		initComponents();
		populateLocale();
		linkModel();
		addListeners();
		
		setSize(argWidth, argHeight);
		setLocationRelativeTo(argParent);
	}
	
	private void initComponents() {
		text = new JTextArea();
		copy = new JButton();
		text.setEditable(false);
		Font font = new Font("Monospaced", Font.PLAIN, 11);
		
		text.setFont(font);
		add(new JScrollPane(text), "Center");
		JPanel p = new JPanel();
		p.add(copy, "East");
		add(p, "South");
	}
	
	private void populateLocale() {
		copy.setText("Copy");
		setIconImage(TricycleModelLocator.getInstance().getWindowIcon().getImage());
	}
	
	private void setTitleName(String argFilename) {
		if (argFilename == null) {
			setTitle(I18n.getText("view.popup.preview.title", "?"));
		}
		setTitle(I18n.getText("view.popup.preview.title", argFilename));
	}
	
	private void linkModel() {
		text.setText(model.getFileString());
		setTitleName(model.getFilename());
		
		model.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				String p = evt.getPropertyName();
				
				if (p.equals("fileString")) {
					text.setText(evt.getNewValue().toString());
				}
				else if (p.equals("filename")) {
					setTitleName(evt.getNewValue().toString());
				}
			}
		});
	}
	
	private void addListeners() {
		copy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent argE) {
				StringSelection ss = new StringSelection(model.getFileString());
			    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
			}
		});
	}
}
