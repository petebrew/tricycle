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
 * Created at Jun 20, 2010, 5:00:46 PM
 */
package org.tridas.io.gui.view.popup;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tridas.io.TridasIO;
import org.tridas.io.enums.Charsets;
import org.tridas.io.enums.NamingConvention;
import org.tridas.io.gui.I18n;
import org.tridas.io.gui.components.LocaleComboRenderer;
import org.tridas.io.gui.components.LocaleComboRenderer.TricycleLocale;
import org.tridas.io.gui.control.config.ConfigController;
import org.tridas.io.gui.control.config.ConfigEvent;
import org.tridas.io.gui.model.ConfigModel;
import org.tridas.io.gui.model.ConvertModel;
import org.tridas.io.gui.model.FileListModel;
import org.tridas.io.gui.model.TricycleModel;
import org.tridas.io.gui.model.TricycleModelLocator;
import org.tridas.io.util.AutoCompleteJComboBoxer;
import org.tridas.spatial.CoordinateReferenceSystem;

import com.dmurph.mvc.MVCEvent;

/**
 * @author daniel
 *
 */
@SuppressWarnings("rawtypes")
public class OptionsWindow extends JDialog {
	private final static Logger log = LoggerFactory.getLogger(OptionsWindow.class);

	private JComboBox readingCharset;
	private JButton readingDefaults;
	
	private JComboBox namingConvention;
	private JComboBox writingCharset;
	private JButton writingDefaults;
	private JButton cancelButton;
	private JButton okButton;
		
	private final ConfigModel model;
	private final TricycleModelLocator loc = TricycleModelLocator.getInstance();
	private JCheckBox cbxEnableAnonomous;
	private JCheckBox cbxAutoUpdate;
	private JPanel privacyPanel;
	private JComboBox cboReaderCRS;
	private JLabel lblReaderCRS;
	private JComboBox cboWriterCRS;
	private JLabel lblWriterCRS;
	private JPanel readingPanel;
	private JLabel lblInputCharacterSet;
	private JLabel lblWritingCharacterSet;
	private JLabel lblNamingConvention;
	private JComboBox cboLocale;
	private JLabel lblLocale;
	private JLabel lblAutomaticallyCheckFor;
	private JLabel lblEnableAnonymousUsage;
	
	public OptionsWindow(JFrame argOwner, ConfigModel argModel) {
		super(argOwner, true);
		model = argModel;
		initializeComponents();
		populateLocale();
		linkModel();
		addListeners();
		pack();
		setLocationRelativeTo(argOwner);
	}
	
	/**
	 * 
	 */
	private void initializeComponents() {
		getContentPane().setLayout(new MigLayout("", "[451.00px,grow]", "[][][][19.00,grow,fill][]"));
		readingDefaults = new JButton();
		
		readingPanel = new JPanel();
		getContentPane().add(readingPanel, "cell 0 0,growx");
		readingPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), I18n.getText("view.options.readingPanel"), TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		readingPanel.setLayout(new MigLayout("", "[320px:320.00px][grow,left]", "[25px][25px][25px]"));
		lblInputCharacterSet = new JLabel("Input character set:");
		readingPanel.add(lblInputCharacterSet, "cell 0 0,alignx right");
		readingCharset = new JComboBox();
		readingPanel.add(readingCharset, "cell 1 0");
		
		
		readingCharset.setEditable(false);
		
		lblReaderCRS = new JLabel("Coordinate reference system:");
		readingPanel.add(lblReaderCRS, "cell 0 1,alignx right,growy");
		
		cboReaderCRS = new JComboBox();
		cboReaderCRS.setModel(new DefaultComboBoxModel(new String[] {"Default for format"}));
		lblReaderCRS.setLabelFor(cboReaderCRS);
		readingPanel.add(cboReaderCRS, "cell 1 1,alignx left,growy");
		readingPanel.add(readingDefaults, "cell 0 2 2 1,alignx right,growy");
		writingDefaults = new JButton();
		
		JPanel writingPanel = new JPanel();
		getContentPane().add(writingPanel, "cell 0 1,growx");
		writingPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), I18n.getText("view.options.writerPanel"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		writingPanel.setLayout(new MigLayout("", "[320px:320.00px][grow,fill]", "[25px][][25px][25px]"));
		lblNamingConvention = new JLabel("Naming convention:");
		writingPanel.add(lblNamingConvention, "cell 0 0,alignx right");
		namingConvention = new JComboBox();
		writingPanel.add(namingConvention, "cell 1 0,alignx left");
		
		namingConvention.setEditable(false);
		lblWritingCharacterSet = new JLabel("Writing character set:");
		writingPanel.add(lblWritingCharacterSet, "cell 0 1,alignx right");
		writingCharset = new JComboBox();
		writingPanel.add(writingCharset, "cell 1 1,alignx left");
		
		writingCharset.setEditable(false);
		
		lblWriterCRS = new JLabel("Coordinate reference system:");
		writingPanel.add(lblWriterCRS, "cell 0 2,alignx trailing");
		
		cboWriterCRS = new JComboBox();
		cboWriterCRS.setModel(new DefaultComboBoxModel(new String[] {"WGS84 [EPSG:4326]"}));
		writingPanel.add(cboWriterCRS, "cell 1 2,alignx left");
		writingPanel.add(writingDefaults, "cell 0 3 2 1,alignx right,growy");
		
		privacyPanel = new JPanel();
		privacyPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), I18n.getText("view.options.misc"), TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		getContentPane().add(privacyPanel, "cell 0 2,growx");
		privacyPanel.setLayout(new MigLayout("", "[][320px:320px][670px]", "[23px][23px][]"));
		
		lblLocale = new JLabel("Locale (restart required):");
		privacyPanel.add(lblLocale, "cell 1 0,alignx right");
		
		cboLocale = new JComboBox();
		cboLocale.setModel(new DefaultComboBoxModel(LocaleComboRenderer.TricycleLocale.values()));
		cboLocale.setRenderer(new LocaleComboRenderer());
		
		boolean localechosen = false;
		for(int i=0; i<cboLocale.getItemCount(); i++)
		{
			// Try setting from preferred language and country code
			TricycleLocale l = (TricycleLocale) cboLocale.getItemAt(i);
			if(l.getLanguageCode().equals(I18n.localeprefs.get("languagecode", "xxx")) && l.getCountryCode().equals(I18n.localeprefs.get("countrycode", "xxx"))) 
			{
				cboLocale.setSelectedIndex(i);
				localechosen = true;
				break;
			}
		}

		for(int i=0; i<cboLocale.getItemCount(); i++)
		{
			// Failed so try setting just from preferred language code
			TricycleLocale l = (TricycleLocale) cboLocale.getItemAt(i);
			if(l.getLanguageCode().equals(I18n.localeprefs.get("languagecode", "xxx"))) 
			{
				cboLocale.setSelectedIndex(i);
				localechosen = true;
				break;
			}
		}
		
		if(localechosen==false)
		{
			// Still failed so try setting from default for system
			for(int i=0; i<cboLocale.getItemCount(); i++)
			{
				TricycleLocale l = (TricycleLocale) cboLocale.getItemAt(i);
				if(l.getLocale().equals(Locale.getDefault())) 
				{
					cboLocale.setSelectedIndex(i);
					localechosen = true;
					break;
				}
			}			
		}
		
		if(localechosen==false)
		{
			// *STILL* failed so default to English
			for(int i=0; i<cboLocale.getItemCount(); i++)
			{
				TricycleLocale l = (TricycleLocale) cboLocale.getItemAt(i);
				
				if(l.getLocale().equals(Locale.ENGLISH)) 
				{
					cboLocale.setSelectedIndex(i);
					localechosen = true;
					break;
				}
			}	
		}
		
		if(localechosen==false)
		{
			// Oh I give up!
			log.error("Locale not set correctly");
		}
		
		privacyPanel.add(cboLocale, "flowx,cell 2 0,alignx left");
		
		lblAutomaticallyCheckFor = new JLabel("Automatically check for updates");
		privacyPanel.add(lblAutomaticallyCheckFor, "cell 1 1,alignx right");
		
		cbxAutoUpdate = new JCheckBox("");
		privacyPanel.add(cbxAutoUpdate, "flowy,cell 2 1,grow");
		cbxAutoUpdate.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		lblEnableAnonymousUsage = new JLabel("Enable anonymous usage data submission:");
		privacyPanel.add(lblEnableAnonymousUsage, "cell 1 2,alignx right");
		
		cbxEnableAnonomous = new JCheckBox("");
		privacyPanel.add(cbxEnableAnonomous, "cell 2 2,grow");
		cbxEnableAnonomous.setAlignmentX(Component.CENTER_ALIGNMENT);
		cancelButton = new JButton();
		okButton = new JButton();
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPanel, "cell 0 4,alignx right");
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		
		// Wire up autocomplete on SRS name combos
		new AutoCompleteJComboBoxer(cboWriterCRS);
		new AutoCompleteJComboBoxer(cboReaderCRS);
		
		// Disable CRS combos until we've wired them up
		cboWriterCRS.setEnabled(false);
		cboReaderCRS.setEnabled(false);

	}
	
	/**
	 * 
	 */
	private void addListeners() {
		namingConvention.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				getRootPane().putClientProperty("Window.documentModified", Boolean.TRUE);
				String naming = namingConvention.getSelectedItem().toString();
				ConfigEvent event = new ConfigEvent(ConfigController.SET_NAMING_CONVENTION, naming);
				event.dispatch();
			}
		});
		
		
		writingCharset.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				getRootPane().putClientProperty("Window.documentModified", Boolean.TRUE);
				String charset = writingCharset.getSelectedItem().toString();
				ConfigEvent event = new ConfigEvent(ConfigController.SET_WRITING_CHARSET, charset);
				event.dispatch();
			}
		});
		
		readingCharset.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				getRootPane().putClientProperty("Window.documentModified", Boolean.TRUE);
				String charset = readingCharset.getSelectedItem().toString();
				ConfigEvent event = new ConfigEvent(ConfigController.SET_READING_CHARSET, charset);
				event.dispatch();
			}
		});
		
		readingDefaults.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent argE) {
				getRootPane().putClientProperty("Window.documentModified", Boolean.TRUE);
				MVCEvent event = new MVCEvent(ConfigController.INPUT_DEFAULTS_PRESSED);
				event.dispatch();
			}
		});
		
		writingDefaults.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent argE) {
				getRootPane().putClientProperty("Window.documentModified", Boolean.TRUE);
				MVCEvent event = new MVCEvent(ConfigController.OUTPUT_DEFAULTS_PRESSED);
				event.dispatch();
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.revertChanges();
				getRootPane().putClientProperty("Window.documentModified", Boolean.FALSE);
				setVisible(false);
			}
		});
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent argE) {
				model.saveChanges();
				getRootPane().putClientProperty("Window.documentModified", Boolean.FALSE);
				setVisible(false);
			}
		});
		
		cbxEnableAnonomous.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent argE) {
				boolean enabled = cbxEnableAnonomous.isSelected();
				TricycleModelLocator.getInstance().getTricycleModel().setTracking(enabled);
				if(enabled){
					TricycleModelLocator.getInstance().setDontAskTracking(true);
				}
			}
		});
		
		cbxAutoUpdate.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent argE) {
				boolean enabled = cbxAutoUpdate.isSelected();
				TricycleModelLocator.getInstance().getTricycleModel().setAutoUpdate(enabled);
			}
		});
		
		cboLocale.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if(cboLocale.getSelectedIndex()==0)
				{
					log.debug("Resetting locale to system default");
					I18n.localeprefs.remove("countrycode");
					I18n.localeprefs.remove("languagecode");
				}
				else
				{			
					TricycleLocale locale = (TricycleLocale) cboLocale.getSelectedItem();
					log.debug("Overriding locale to "+locale.getCountryCode()+", "+locale.getLanguageCode());
					I18n.localeprefs.put("countrycode", locale.getCountryCode());
					I18n.localeprefs.put("languagecode", locale.getLanguageCode());
				}
			}
		});
	}
	
	/**
	 * 
	 */
	@SuppressWarnings({ "unchecked"})
	private void populateLocale() {
		FileListModel fmodel = loc.getFileListModel();
		ConvertModel cmodel = loc.getConvertModel();
		setTitle(I18n.getText("view.options.title"));
				
		
		lblInputCharacterSet.setText(I18n.getText("view.options.input.charset"));
		lblWritingCharacterSet.setText(I18n.getText("view.options.output.charset"));
		lblReaderCRS.setText(I18n.getText("view.options.crs")+":");
		lblWriterCRS.setText(I18n.getText("view.options.crs")+":");
		readingDefaults.setText(I18n.getText("view.options.input.defaults", fmodel.getInputFormat()));
		writingDefaults.setText(I18n.getText("view.options.output.defaults", cmodel.getOutputFormat()));
		okButton.setText(I18n.getText("view.options.ok"));
		cancelButton.setText(I18n.getText("general.cancel"));
		lblAutomaticallyCheckFor.setText(I18n.getText("view.options.autocheckupdate")+":");
		lblLocale.setText(I18n.getText("view.options.locale")+":");
		lblEnableAnonymousUsage.setText(I18n.getText("view.options.enableanonstats")+":");
		
		for (String conv : NamingConvention.getNamingConventions()) {
			namingConvention.addItem(conv);
		}

		// put default one on top, so the user will see
		// "automatic" right underneath
		readingCharset.addItem(Charset.defaultCharset().displayName());
		for (String s : Charsets.getReadingCharsets()) {
			if (s.equals(Charset.defaultCharset().displayName())) {
				continue;
			}
			readingCharset.addItem(s);
		}
		
		for (String s : Charsets.getWritingCharsets()) {
			writingCharset.addItem(s);
		}
		
	    Iterator it = TridasIO.crsMap.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        CoordinateReferenceSystem crs = (CoordinateReferenceSystem) pairs.getValue();
	        cboReaderCRS.addItem(crs.toString());
	    }
		
		
	}
	
	/**
	 * 
	 */
	private void linkModel() {
		
		// Try to remember last used format
		if(TricycleModelLocator.getInstance().getLastUsedOutputFormat()!=null)
		{
			namingConvention.setSelectedItem(TricycleModelLocator.getInstance().getLastNamingConvention());
			model.setNamingConvention(TricycleModelLocator.getInstance().getLastNamingConvention());
		}
		else
		{
			namingConvention.setSelectedItem(model.getNamingConvention());
		}
				
		readingCharset.setSelectedItem(model.getReadingCharset());
		writingCharset.setSelectedItem(model.getWritingCharset());
		cbxEnableAnonomous.setSelected(TricycleModelLocator.getInstance().getTricycleModel().isTracking());
		cbxAutoUpdate.setSelected(TricycleModelLocator.getInstance().getTricycleModel().isAutoUpdate());

		model.saveChanges();
		
		model.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent argEvt) {
				String prop = argEvt.getPropertyName();
				
				if (prop.equals("namingConvention")) {
					namingConvention.setSelectedItem(argEvt.getNewValue());
				}
				else if (prop.equals("writingCharset")) {
					writingCharset.setSelectedItem(argEvt.getNewValue());
				}
				else if (prop.equals("readingCharset")) {
					readingCharset.setSelectedItem(argEvt.getNewValue());
				}
			}
		});
		
		FileListModel fmodel = loc.getFileListModel();
		ConvertModel cmodel = loc.getConvertModel();
		fmodel.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent argEvt) {
				if(argEvt.getPropertyName().equals("inputFormat")){
					readingDefaults.setText(I18n.getText("view.options.input.defaults", argEvt.getNewValue().toString()));
				}
			}
		});
		cmodel.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent argEvt) {
				if(argEvt.getPropertyName().equals("outputFormat")){
					writingDefaults.setText(I18n.getText("view.options.output.defaults", argEvt.getNewValue().toString()));
				}
			}
		});
		
	
		
		final TricycleModel mwm = loc.getTricycleModel();
		mwm.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				
				if (evt.getPropertyName().equals("lock")) {
					boolean lock = (Boolean) evt.getNewValue();
					if (lock) {
						namingConvention.setEnabled(false);
						readingCharset.setEnabled(false);
						writingCharset.setEnabled(false);
						readingDefaults.setEnabled(false);
						writingDefaults.setEnabled(false);
						cbxEnableAnonomous.setEnabled(false);
						cbxAutoUpdate.setEnabled(false);
					}
					else {
						namingConvention.setEnabled(true);
						readingCharset.setEnabled(true);
						writingCharset.setEnabled(true);
						readingDefaults.setEnabled(true);
						writingDefaults.setEnabled(true);
						cbxEnableAnonomous.setEnabled(true);
						cbxAutoUpdate.setEnabled(true);
					}
				}
			}
		});
		
		mwm.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent argEvt) {
				if(argEvt.getPropertyName().equals("tracking")){
					cbxEnableAnonomous.setSelected(mwm.isTracking());
				}
				if(argEvt.getPropertyName().equals("autoupdate")){
					cbxAutoUpdate.setSelected(mwm.isAutoUpdate());
				}
			}
		});
	}
}
