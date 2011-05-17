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
 * Created on Jun 7, 2010, 12:12:08 AM
 */
package org.tridas.io.gui.view.popup;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import net.miginfocom.swing.MigLayout;

import org.tridas.io.gui.App;
import org.tridas.io.gui.I18n;
import org.tridas.io.gui.model.TricycleModelLocator;
import org.tridas.io.util.IOUtils;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.JButton;

/**
 * @author Daniel Murphy
 */
@SuppressWarnings("serial")
public class AboutWindow extends JDialog {
	
	private JLabel txtVersion;
	private JLabel txtTimestamp;
	private JLabel txtRevision;
	
	public AboutWindow(JFrame argParent) {
		super(argParent, true);
		initComponents();
		populateLocale();
		addListeners();
		pack();
		setResizable(false);
		setLocationRelativeTo(argParent);
	}

	/**
	 * 
	 */
	private void initComponents() {
		getContentPane().setLayout(new MigLayout("", "[497px,grow]", "[][296px,grow][]"));
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.setBackground(Color.WHITE);
		getContentPane().add(panel_1, "cell 0 0,growx,aligny center");
		panel_1.setLayout(new MigLayout("", "[383px,grow][]", "[19px]"));
		
		JLabel lblTitle = new JLabel(I18n.getText("view.popup.about.header"));
		panel_1.add(lblTitle, "cell 0 0,alignx left,aligny center");
		lblTitle.setFont(new Font("Dialog", Font.BOLD, 16));
		
		JLabel llogo = new JLabel(new ImageIcon(IOUtils.getFileInJarURL("icons/64x64/application.png")));
		panel_1.add(llogo, "cell 1 0");
		
		//Box box = Box.createHorizontalBox();
		//box.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, "cell 0 1,grow");
		
		JPanel panelAbout = new JPanel();
		tabbedPane.addTab(I18n.getText("view.popup.about"), null, panelAbout, null);
		panelAbout.setLayout(new MigLayout("", "[171.00px,grow][grow]", "[][][][][][][][109.00px,grow,fill]"));
		
		JLabel lblVersion = new JLabel(I18n.getText("view.popup.about.version")+":");
		panelAbout.add(lblVersion, "cell 0 0,alignx right");
		
		txtVersion = new JLabel("x.x.x");
		txtVersion.setFont(new Font("Dialog", Font.PLAIN, 12));
		panelAbout.add(txtVersion, "cell 1 0");
		
		JLabel lblBuildRevision = new JLabel(I18n.getText("view.popup.about.revision")+":");
		panelAbout.add(lblBuildRevision, "cell 0 1,alignx right");
		
		txtRevision = new JLabel("xxx");
		txtRevision.setFont(new Font("Dialog", Font.PLAIN, 12));
		panelAbout.add(txtRevision, "cell 1 1");
		
		JLabel lblBuildTimestamp = new JLabel(I18n.getText("view.popup.about.timestamp")+":");
		panelAbout.add(lblBuildTimestamp, "cell 0 2,alignx right");
		
		txtTimestamp = new JLabel("xx Xxx 20XX, xx:xx");
		txtTimestamp.setFont(new Font("Dialog", Font.PLAIN, 12));
		panelAbout.add(txtTimestamp, "cell 1 2");
		
		JLabel lblDendroiolibraryBy = new JLabel(I18n.getText("view.popup.about.libby")+":");
		panelAbout.add(lblDendroiolibraryBy, "cell 0 4,alignx right");
		
		JLabel txtLibAuthors = new JLabel(I18n.getText("view.popup.about.libauthors"));
		txtLibAuthors.setFont(new Font("Dialog", Font.PLAIN, 12));
		panelAbout.add(txtLibAuthors, "cell 1 4");
		
		JLabel lblGraphicalInterfaceBy = new JLabel(I18n.getText("view.popup.about.guiby")+":");
		panelAbout.add(lblGraphicalInterfaceBy, "cell 0 5,alignx right");
		
		JLabel txtGUIAuthors = new JLabel(I18n.getText("view.popup.about.guiauthors"));
		txtGUIAuthors.setFont(new Font("Dialog", Font.PLAIN, 12));
		panelAbout.add(txtGUIAuthors, "cell 1 5");
		
		JPanel panel = new JPanel();
		panelAbout.add(panel, "cell 0 7 2 1,grow");
		panel.setLayout(new BorderLayout(0, 0));
		
		JTextArea txtAcknowledgements = new JTextArea();
		txtAcknowledgements.setForeground(Color.BLACK);
		txtAcknowledgements.setEnabled(false);
		txtAcknowledgements.setEditable(false);
		txtAcknowledgements.setLineWrap(true);
		txtAcknowledgements.setWrapStyleWord(true);
		txtAcknowledgements.setText(I18n.getText("view.popup.about.acknowledgementstext"));
		
		panel.add(txtAcknowledgements);
		
		JLabel lblAcknowledgements = new JLabel(I18n.getText("view.popup.about.acknowledgements")+":");
		panel.add(lblAcknowledgements, BorderLayout.NORTH);
			
		
		JPanel panelLicense = new JPanel();
		tabbedPane.addTab(I18n.getText("view.popup.about.license"), null, panelLicense, null);
		panelLicense.setLayout(new BorderLayout(0, 0));
		
		JTextArea txtLicense = new JTextArea();
		txtLicense.setText(I18n.getText("view.popup.about.licensetext"));
		txtLicense.setFont(new Font("Monospaced", Font.PLAIN, 12));
		txtLicense.setEditable(false);
		panelLicense.add(txtLicense, BorderLayout.CENTER);
		
		JButton btnOk = new JButton("OK");
		getContentPane().add(btnOk, "cell 0 2,alignx right");
		btnOk.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();				
			}		
		});

	}
	
	
	/**
	 * 
	 */
	private void addListeners() {
		addWindowListener(new WindowAdapter() {
			/**
			 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
			 */
			@Override
			public void windowClosing(WindowEvent e) {
				closeWindow();
			}
		});
	}
	
	private void closeWindow(){
		setVisible(false);
	}
	
	private void populateLocale() {
		setIconImage(TricycleModelLocator.getInstance().getWindowIcon().getImage());
		
		// Extract build timestamp from Manifest
		txtVersion.setText(App.getBuildVersion());
		txtRevision.setText(App.getBuildRevision());
		txtTimestamp.setText(App.getBuildTimestamp());
		

		setTitle(I18n.getText("view.popup.about.title"));
		
	}
}
