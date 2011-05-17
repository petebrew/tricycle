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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
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
		
		Box box = Box.createHorizontalBox();
		box.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		box.add(tabbedPane);
		
		JPanel panelAbout = new JPanel();
		tabbedPane.addTab(I18n.getText("view.popup.about"), null, panelAbout, null);
		panelAbout.setLayout(new MigLayout("", "[171.00px,grow][grow][]", "[][][][][][][][][][109.00px,grow,fill]"));
		
		JLabel lblTitle = new JLabel(I18n.getText("view.popup.about.header"));
		lblTitle.setFont(new Font("Dialog", Font.BOLD, 16));
		panelAbout.add(lblTitle, "cell 0 0 3 1,alignx center");
		
		JLabel llogo = new JLabel(new ImageIcon(IOUtils.getFileInJarURL("icons/64x64/application.png")));
		panelAbout.add(llogo, "cell 2 1 1 5,alignx center,aligny top");
		
		JLabel lblVersion = new JLabel(I18n.getText("view.popup.about.version")+":");
		panelAbout.add(lblVersion, "cell 0 2,alignx right");
		
		txtVersion = new JLabel("x.x.x");
		txtVersion.setFont(new Font("Dialog", Font.PLAIN, 12));
		panelAbout.add(txtVersion, "cell 1 2");
		
		JLabel lblBuildRevision = new JLabel(I18n.getText("view.popup.about.revision")+":");
		panelAbout.add(lblBuildRevision, "cell 0 3,alignx right");
		
		txtRevision = new JLabel("xxx");
		txtRevision.setFont(new Font("Dialog", Font.PLAIN, 12));
		panelAbout.add(txtRevision, "cell 1 3");
		
		JLabel lblBuildTimestamp = new JLabel(I18n.getText("view.popup.about.timestamp")+":");
		panelAbout.add(lblBuildTimestamp, "cell 0 4,alignx right");
		
		txtTimestamp = new JLabel("xx Xxx 20XX, xx:xx");
		txtTimestamp.setFont(new Font("Dialog", Font.PLAIN, 12));
		panelAbout.add(txtTimestamp, "cell 1 4");
		
		JLabel lblDendroiolibraryBy = new JLabel(I18n.getText("view.popup.about.libby")+":");
		panelAbout.add(lblDendroiolibraryBy, "cell 0 6,alignx right");
		
		JLabel txtLibAuthors = new JLabel(I18n.getText("view.popup.about.libauthors"));
		txtLibAuthors.setFont(new Font("Dialog", Font.PLAIN, 12));
		panelAbout.add(txtLibAuthors, "cell 1 6 2 1");
		
		JLabel lblGraphicalInterfaceBy = new JLabel(I18n.getText("view.popup.about.guiby")+":");
		panelAbout.add(lblGraphicalInterfaceBy, "cell 0 7,alignx right");
		
		JLabel txtGUIAuthors = new JLabel(I18n.getText("view.popup.about.guiauthors"));
		txtGUIAuthors.setFont(new Font("Dialog", Font.PLAIN, 12));
		panelAbout.add(txtGUIAuthors, "cell 1 7 2 1");
		
		JPanel panel = new JPanel();
		panelAbout.add(panel, "cell 0 9 3 1,grow");
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
		getContentPane().add(box, "Center");
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
