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
package org.tridas.io.gui.command;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tridas.io.AbstractDendroCollectionWriter;
import org.tridas.io.AbstractDendroFileReader;
import org.tridas.io.IDendroFile;
import org.tridas.io.TridasIO;
import org.tridas.io.defaults.IMetadataFieldSet;
import org.tridas.io.exceptions.ConversionWarning;
import org.tridas.io.exceptions.ConversionWarningException;
import org.tridas.io.exceptions.ImpossibleConversionException;
import org.tridas.io.exceptions.IncorrectDefaultFieldsException;
import org.tridas.io.exceptions.InvalidDendroFileException;
import org.tridas.io.gui.I18n;
import org.tridas.io.gui.control.convert.ConvertEvent;
import org.tridas.io.enums.InputFormat;
import org.tridas.io.gui.model.ConvertModel;
import org.tridas.io.gui.model.ConvertModel.TreatFilesAsOption;
import org.tridas.io.gui.model.FileListModel;
import org.tridas.io.gui.model.TricycleModel;
import org.tridas.io.gui.model.TricycleModelLocator;
import org.tridas.io.gui.model.popup.ConvertingDialogModel;
import org.tridas.io.gui.view.popup.ConvertProgress;
import org.tridas.io.naming.HierarchicalNamingConvention;
import org.tridas.io.naming.INamingConvention;
import org.tridas.io.naming.KeycodeNamingConvention;
import org.tridas.io.naming.NumericalNamingConvention;
import org.tridas.io.naming.SeriesCode8CharNamingConvention;
import org.tridas.io.naming.SeriesCodeNamingConvention;
import org.tridas.io.naming.UUIDNamingConvention;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasProject;
import org.tridas.schema.TridasTridas;

import com.dmurph.mvc.IllegalThreadException;
import com.dmurph.mvc.IncorrectThreadException;
import com.dmurph.mvc.MVC;
import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

/**
 * @author Daniel
 *
 */
public class ConvertCommand implements ICommand {
	private final static Logger log = LoggerFactory.getLogger(ConvertCommand.class);
	
	public void execute(MVCEvent argEvent) {
		
		//MVC.getTracker().trackEvent("Convert", "execute");

		ConvertEvent event = (ConvertEvent) argEvent;
		String outputFormat = event.getOutputFormat();
		INamingConvention naming;
		
		try {
			MVC.splitOff();
		} catch (IllegalThreadException e1) {
			e1.printStackTrace();
		} catch (IncorrectThreadException e1) {
			e1.printStackTrace();
		}
		
		boolean outputFormatFound = false;
		for (String format : TridasIO.getSupportedWritingFormats()) {
			if (format.equalsIgnoreCase(outputFormat)) {
				outputFormat = format;
				outputFormatFound = true;
				TricycleModelLocator.getInstance().setLastUsedOutputFormat(outputFormat);
			}
		}
		if (!outputFormatFound) {
			JOptionPane.showMessageDialog(null, I18n.getText("control.convert.noOutput", outputFormat),
					I18n.getText("general.error"), JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		FileListModel fileList = TricycleModelLocator.getInstance().getFileListModel();
		
		boolean inputFormatFound = false;
		for (String format : TridasIO.getSupportedReadingFormats()) {
			if (format.equalsIgnoreCase(event.getInputFormat())) {
				fileList.setInputFormat(format);
				inputFormatFound = true;
				TricycleModelLocator.getInstance().setLastUsedInputFormat(format);
			}
		}
		if (!inputFormatFound && !event.getInputFormat().equals(InputFormat.AUTO)) {
			JOptionPane.showMessageDialog(null, I18n.getText("control.convert.noInput", outputFormat),
					I18n.getText("general.error"), JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (event.getNamingConvention().equals(org.tridas.io.I18n.getText("general.default"))) {
			 naming = null;
		}
		else if (event.getNamingConvention().equals(org.tridas.io.I18n.getText("namingconvention.uuid"))) {
			naming = new UUIDNamingConvention();
		}
		else if (event.getNamingConvention().equals(org.tridas.io.I18n.getText("namingconvention.hierarchical"))) {
			naming = new HierarchicalNamingConvention();
		}
		else if (event.getNamingConvention().equals(org.tridas.io.I18n.getText("namingconvention.numerical"))) {
			naming = new NumericalNamingConvention();
		}
		else if (event.getNamingConvention().equals(org.tridas.io.I18n.getText("namingconvention.seriescode")))
		{
			naming = new SeriesCodeNamingConvention();
		}
		else if (event.getNamingConvention().equals(org.tridas.io.I18n.getText("namingconvention.seriescode8char")))
		{
			naming = new SeriesCode8CharNamingConvention();
		}
		else if (event.getNamingConvention().equals(org.tridas.io.I18n.getText("namingconvention.keycode")))
		{
			naming = new KeycodeNamingConvention();
		}
		else {
			JOptionPane.showMessageDialog(null, I18n.getText("control.convert.noNaming", event.getNamingConvention()),
					I18n.getText("general.error"), JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		
		TricycleModelLocator.getInstance().setLastNamingConvention(event.getNamingConvention());

		

		if(event.getTreatFilesAs().equals(TreatFilesAsOption.ONE_PROJECT))
		{
			
			// Treat files as one project
			convertFilesIntoSingleProject(fileList.getInputFiles().toArray(new String[0]), fileList.getInputFormat(),
					event.getReaderDefaults(), outputFormat, event.getWriterDefaults(), naming);
		}
		else if(event.getTreatFilesAs().equals(TreatFilesAsOption.ONE_OBJECT))
		{
			
			// Treat files as one project
			convertFilesIntoSingleObject(fileList.getInputFiles().toArray(new String[0]), fileList.getInputFormat(),
					event.getReaderDefaults(), outputFormat, event.getWriterDefaults(), naming);
		}
		else
		{
			// Treat files separately
			convertFiles(fileList.getInputFiles().toArray(new String[0]), fileList.getInputFormat(),
					event.getReaderDefaults(), outputFormat, event.getWriterDefaults(), naming);
		}
	}
	
	
	private void convertFilesIntoSingleProject(String[] argFiles, String argInputFormat, IMetadataFieldSet argInputDefaults,
			String argOutputFormat, IMetadataFieldSet argOutputDefaults, INamingConvention argNaming) 
	{
		
		log.debug("Converting files to one project");
		
		TricycleModel mwm = TricycleModelLocator.getInstance().getTricycleModel();
		ConvertProgress storedConvertProgress = null;
		try {
			
			mwm.setLock(true);
			ArrayList<ConvertModel.ReaderWriterObject> list = new ArrayList<ConvertModel.ReaderWriterObject>();
			
			ConvertingDialogModel model = new ConvertingDialogModel();
			model.setConvertingFilename("");
			model.setConvertingPercent(0);
			
			final ConvertProgress convertProgress = new ConvertProgress(TricycleModelLocator.getInstance()
					.getMainWindow(), model);
			storedConvertProgress = convertProgress;
			
			// i have to do this in a different thread
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					convertProgress.setVisible(true);
				}
			});
			
			while(!convertProgress.isVisible()){
				Thread.sleep(100);
			}
			
			
			ConvertModel cmodel = TricycleModelLocator.getInstance().getConvertModel();
			cmodel.setConvertRunning(true);
			
			ConvertModel.ReaderWriterObject struct = new ConvertModel.ReaderWriterObject();
			list.add(struct);
			
			if(argFiles.length>1)
			{
				// Need to create a substitute for file name as we have many
				struct.file = argFiles.length + " files combined";
			}
			else
			{
				struct.file = argFiles[0];
			}
			
			TridasTridas combinedContainer = new TridasTridas();
			AbstractDendroFileReader reader = null;
			for (int i = 0; i < argFiles.length; i++) 
			{
				if (!cmodel.isConvertRunning()) 
				{
					break;
				}
				String file = argFiles[i];
				
				model.setConvertingFilename("Several files");
				
				
				if (argInputFormat.equals(InputFormat.AUTO)) {
					String extension = file.substring(file.lastIndexOf(".") + 1);
					log.debug("extention: " + extension);
					reader = TridasIO.getFileReaderFromExtension(extension);
				}
				else {
					reader = TridasIO.getFileReader(argInputFormat);
				}
				

				
				if (reader == null) {
					struct.errorMessage = I18n.getText("control.convert.readerNull");
					continue;
				}
				
				try {
					if (argInputDefaults == null) {
						reader.loadFile(file);
					}
					else {
						reader.loadFile(file, (IMetadataFieldSet) argInputDefaults.clone());
					}
				} catch (IOException e) {
					struct.errorMessage = I18n.getText("control.convert.ioException", e.toString());
					continue;
				} catch (InvalidDendroFileException e) {
					struct.errorMessage = e.getLocalizedMessage();
					continue;
				} catch (IncorrectDefaultFieldsException e) {
					struct.errorMessage = e.toString() + " Please report bug.";
				}
				
				TridasTridas container = reader.getTridasContainer();
				
				combinedContainer.getProjects().addAll(container.getProjects());
				model.setConvertingPercent(i * 100 / argFiles.length);
				
			}
			
			if (struct.errorMessage != null) {
				return;
			}

			combinedContainer = mergeProjectsIntoOne(combinedContainer);
			
			
			AbstractDendroCollectionWriter writer = TridasIO.getFileWriter(argOutputFormat);
			
			// If no naming convention was passed, set to default from writer.
			if (argNaming == null)
			{
				argNaming = writer.getNamingConvention();
			}
			
			if (argNaming instanceof NumericalNamingConvention) {
				if (argFiles[0].contains(".")) {
					String justFile = argFiles[0].substring(argFiles[0].lastIndexOf(File.separatorChar) + 1,
							argFiles[0].lastIndexOf('.'));
					((NumericalNamingConvention) argNaming).setBaseFilename(justFile);
				}
				else {
					((NumericalNamingConvention) argNaming).setBaseFilename(argFiles[0]);
				}
			}
			
			// Set naming
			writer.setNamingConvention(argNaming);
			
			// Get naming again in case writer has overriden request
			argNaming = writer.getNamingConvention();
			

			
			try {
				if (argOutputDefaults == null) {
					writer.load(combinedContainer);
				}
				else {
					writer.load(combinedContainer, (IMetadataFieldSet) argOutputDefaults.clone());
				}
			} catch (ImpossibleConversionException e) {
				struct.errorMessage = e.getMessage();
			} catch (ConversionWarningException e) {
				struct.errorMessage = e.getLocalizedMessage();
			} catch (IncorrectDefaultFieldsException e) {
				struct.errorMessage = e.getLocalizedMessage();
			}

			if (struct.errorMessage==null && writer.getFiles().length == 0) {
				struct.errorMessage = I18n.getText("control.convert.noFilesWritten");
			}
			
			
			
			struct.reader = reader;
			struct.writer = writer;
			constructNodes(list, argNaming);
			
		} catch (Exception e) {
			log.error("Exception thrown while converting.", e);
			throw new RuntimeException(e);
		} finally {
			if (storedConvertProgress != null) {
				storedConvertProgress.setVisible(false);
			}
			
			mwm.setLock(false);
		}
	}
	
	private void convertFilesIntoSingleObject(String[] argFiles, String argInputFormat, IMetadataFieldSet argInputDefaults,
			String argOutputFormat, IMetadataFieldSet argOutputDefaults, INamingConvention argNaming) 
	{
		
		log.debug("Converting files to one object");
		
		TricycleModel mwm = TricycleModelLocator.getInstance().getTricycleModel();
		ConvertProgress storedConvertProgress = null;
		try {
			
			mwm.setLock(true);
			ArrayList<ConvertModel.ReaderWriterObject> list = new ArrayList<ConvertModel.ReaderWriterObject>();
			
			ConvertingDialogModel model = new ConvertingDialogModel();
			model.setConvertingFilename("");
			model.setConvertingPercent(0);
			
			final ConvertProgress convertProgress = new ConvertProgress(TricycleModelLocator.getInstance()
					.getMainWindow(), model);
			storedConvertProgress = convertProgress;
			
			// i have to do this in a different thread
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					convertProgress.setVisible(true);
				}
			});
			
			while(!convertProgress.isVisible()){
				Thread.sleep(100);
			}
			
			
			ConvertModel cmodel = TricycleModelLocator.getInstance().getConvertModel();
			cmodel.setConvertRunning(true);
			
			ConvertModel.ReaderWriterObject struct = new ConvertModel.ReaderWriterObject();
			list.add(struct);
			
			if(argFiles.length>1)
			{
				// Need to create a substitute for file name as we have many
				struct.file = argFiles.length + " files combined";
			}
			else
			{
				struct.file = argFiles[0];
			}
			
			TridasTridas combinedContainer = new TridasTridas();
			AbstractDendroFileReader reader = null;
			for (int i = 0; i < argFiles.length; i++) 
			{
				if (!cmodel.isConvertRunning()) 
				{
					break;
				}
				String file = argFiles[i];
				
				model.setConvertingFilename("Several files");
				
				
				if (argInputFormat.equals(InputFormat.AUTO)) {
					String extension = file.substring(file.lastIndexOf(".") + 1);
					log.debug("extention: " + extension);
					reader = TridasIO.getFileReaderFromExtension(extension);
				}
				else {
					reader = TridasIO.getFileReader(argInputFormat);
				}
				

				
				if (reader == null) {
					struct.errorMessage = I18n.getText("control.convert.readerNull");
					continue;
				}
				
				try {
					if (argInputDefaults == null) {
						reader.loadFile(file);
					}
					else {
						reader.loadFile(file, (IMetadataFieldSet) argInputDefaults.clone());
					}
				} catch (IOException e) {
					struct.errorMessage = I18n.getText("control.convert.ioException", e.toString());
					continue;
				} catch (InvalidDendroFileException e) {
					struct.errorMessage = e.getLocalizedMessage();
					continue;
				} catch (IncorrectDefaultFieldsException e) {
					struct.errorMessage = e.toString() + " Please report bug.";
				}
				
				TridasTridas container = reader.getTridasContainer();
				
				combinedContainer.getProjects().addAll(container.getProjects());
				model.setConvertingPercent(i * 100 / argFiles.length);
				
			}
			
			if (struct.errorMessage != null) {
				return;
			}

			combinedContainer = mergeObjectsIntoOne(combinedContainer);
			
			
			AbstractDendroCollectionWriter writer = TridasIO.getFileWriter(argOutputFormat);
			
			// If no naming convention was passed, set to default from writer.
			if (argNaming == null)
			{
				argNaming = writer.getNamingConvention();
			}
			
			if (argNaming instanceof NumericalNamingConvention) {
				if (argFiles[0].contains(".")) {
					String justFile = argFiles[0].substring(argFiles[0].lastIndexOf(File.separatorChar) + 1,
							argFiles[0].lastIndexOf('.'));
					((NumericalNamingConvention) argNaming).setBaseFilename(justFile);
				}
				else {
					((NumericalNamingConvention) argNaming).setBaseFilename(argFiles[0]);
				}
			}
			
			// Set naming
			writer.setNamingConvention(argNaming);
			
			// Get naming again in case writer has overriden request
			argNaming = writer.getNamingConvention();
			

			
			try {
				if (argOutputDefaults == null) {
					writer.load(combinedContainer);
				}
				else {
					writer.load(combinedContainer, (IMetadataFieldSet) argOutputDefaults.clone());
				}
			} catch (ImpossibleConversionException e) {
				struct.errorMessage = e.getMessage();
			} catch (ConversionWarningException e) {
				struct.errorMessage = e.getLocalizedMessage();
			} catch (IncorrectDefaultFieldsException e) {
				struct.errorMessage = e.getLocalizedMessage();
			}

			if (struct.errorMessage==null && writer.getFiles().length == 0) {
				struct.errorMessage = I18n.getText("control.convert.noFilesWritten");
			}
			
			
			
			struct.reader = reader;
			struct.writer = writer;
			constructNodes(list, argNaming);
			
		} catch (Exception e) {
			log.error("Exception thrown while converting.", e);
			throw new RuntimeException(e);
		} finally {
			if (storedConvertProgress != null) {
				storedConvertProgress.setVisible(false);
			}
			
			mwm.setLock(false);
		}
	}
	
	private void convertFiles(String[] argFiles, String argInputFormat, IMetadataFieldSet argInputDefaults,
			String argOutputFormat, IMetadataFieldSet argOutputDefaults, INamingConvention argNaming) 
	{
		
		log.debug("Converting files to multiple projects");
		
		TricycleModel mwm = TricycleModelLocator.getInstance().getTricycleModel();
		ConvertProgress storedConvertProgress = null;
		try {
			
			mwm.setLock(true);
			ArrayList<ConvertModel.ReaderWriterObject> list = new ArrayList<ConvertModel.ReaderWriterObject>();
			
			ConvertingDialogModel model = new ConvertingDialogModel();
			model.setConvertingFilename("");
			model.setConvertingPercent(0);
			
			final ConvertProgress convertProgress = new ConvertProgress(TricycleModelLocator.getInstance()
					.getMainWindow(), model);
			storedConvertProgress = convertProgress;
			
			// i have to do this in a different thread
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					convertProgress.setVisible(true);
				}
			});
			
			while(!convertProgress.isVisible()){
				Thread.sleep(100);
			}
			
			
			ConvertModel cmodel = TricycleModelLocator.getInstance().getConvertModel();
			cmodel.setConvertRunning(true);
			
			for (int i = 0; i < argFiles.length; i++) {
				if (!cmodel.isConvertRunning()) {
					break;
				}
				String file = argFiles[i];
				
				model.setConvertingFilename(file);
				
				AbstractDendroFileReader reader;
				if (argInputFormat.equals(InputFormat.AUTO)) {
					String extension = file.substring(file.lastIndexOf(".") + 1);
					log.debug("extention: " + extension);
					reader = TridasIO.getFileReaderFromExtension(extension);
				}
				else {
					reader = TridasIO.getFileReader(argInputFormat);
				}
				
				ConvertModel.ReaderWriterObject struct = new ConvertModel.ReaderWriterObject();
				list.add(struct);
				struct.file = file;
				
				if (reader == null) {
					struct.errorMessage = I18n.getText("control.convert.readerNull");
					continue;
				}
				
				try {
					if (argInputDefaults == null) {
						reader.loadFile(file);
					}
					else {
						reader.loadFile(file, (IMetadataFieldSet) argInputDefaults.clone());
					}
				} catch (IOException e) {
					struct.errorMessage = I18n.getText("control.convert.ioException", e.toString());
					continue;
				} catch (InvalidDendroFileException e) {
					struct.errorMessage = e.getLocalizedMessage();
					continue;
				} catch (IncorrectDefaultFieldsException e) {
					struct.errorMessage = e.toString() + " Please report bug.";
				}
				
				TridasTridas container = reader.getTridasContainer();
				
				AbstractDendroCollectionWriter writer = TridasIO.getFileWriter(argOutputFormat);
				
				// If no naming convention was passed, set to default from writer.
				if (argNaming == null)
				{
					argNaming = writer.getNamingConvention();
				}
				
				if (argNaming instanceof NumericalNamingConvention) {
					if (file.contains(".")) {
						String justFile = file.substring(file.lastIndexOf(File.separatorChar) + 1,
								file.lastIndexOf('.'));
						((NumericalNamingConvention) argNaming).setBaseFilename(justFile);
					}
					else {
						((NumericalNamingConvention) argNaming).setBaseFilename(file);
					}
				}
				
				// Set naming
				writer.setNamingConvention(argNaming);
				
				// Get naming again in case writer has overriden request
				argNaming = writer.getNamingConvention();
				
				if (struct.errorMessage != null) {
					continue;
				}
				
				try {
					if (argOutputDefaults == null) {
						writer.load(container);
					}
					else {
						writer.load(container, (IMetadataFieldSet) argOutputDefaults.clone());
					}
				} catch (ImpossibleConversionException e) {
					struct.errorMessage = e.getMessage();
				} catch (ConversionWarningException e) {
					struct.errorMessage = e.getLocalizedMessage();
				} catch (IncorrectDefaultFieldsException e) {
					struct.errorMessage = e.getLocalizedMessage();
				}
				
				struct.reader = reader;
				struct.writer = writer;
				
				if (struct.errorMessage==null && writer.getFiles().length == 0) {
					struct.errorMessage = I18n.getText("control.convert.noFilesWritten");
				}
				
				model.setConvertingPercent(i * 100 / argFiles.length);
			}
			constructNodes(list, argNaming);
			
		} catch (Exception e) {
			log.error("Exception thrown while converting.", e);
			throw new RuntimeException(e);
		} finally {
			if (storedConvertProgress != null) {
				storedConvertProgress.setVisible(false);
			}
			
			mwm.setLock(false);
		}
	}
	
	private void constructNodes(ArrayList<ConvertModel.ReaderWriterObject> list, INamingConvention argNaming) {
		ArrayList<DefaultMutableTreeNode> nodes = new ArrayList<DefaultMutableTreeNode>();
		
		ConvertModel model = TricycleModelLocator.getInstance().getConvertModel();
		model.getConvertedList().clear();
		model.getConvertedList().addAll(list);
		
		// we need to set processed/failed/convertedWithWarnings
		int processed = 0;
		int failed = 0;
		int convWWarnings = 0;
		
		log.debug("Struct list has "+list.size()+" items");
		for (ConvertModel.ReaderWriterObject s : list) 
		{
		
			
			DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(new StructWrapper(s));
			nodes.add(leaf);
			
			processed++;
			
			boolean fail = false;
			if (s.errorMessage != null) {
				DefaultMutableTreeNode errorMessage = new DefaultMutableTreeNode(s.errorMessage);
				//leaf.add(errorMessage);
				//nodes.add(leaf);
				failed++;
				fail = true;
			}
			
			boolean warnings = false;
			if (s.writer != null) {
				for (IDendroFile file : s.writer.getFiles()) {
					DefaultMutableTreeNode fileNode = new DefaultMutableTreeNode(new DendroWrapper(file, argNaming));
					
					if (file.getDefaults().getWarnings().size() != 0) {
						// put them all in a hash set first so no duplicates.
						HashSet<String> set = new HashSet<String>();
						
						for (ConversionWarning warning : file.getDefaults().getWarnings()) {
							//set.add(warning.toStringWithField());
						}
						
						for(String warning : set){
							DefaultMutableTreeNode warningNode = new DefaultMutableTreeNode(warning);
							fileNode.add(warningNode);
						}
						warnings = true;
					}
					leaf.add(fileNode);
				}
			}
			
			if (s.reader != null && s.reader.getWarnings().length != 0) {
				warnings = true;
				/*DefaultMutableTreeNode readerWarnings = new DefaultMutableTreeNode(
						I18n.getText("control.convert.readerWarnings"));
				
				// put them all in a hash set first so no duplicates.
				HashSet<String> set = new HashSet<String>();
				
				for (ConversionWarning warning : s.reader.getWarnings()) {
					set.add(warning.toStringWithField());
				}
				for(String warning : set){
					DefaultMutableTreeNode warn = new DefaultMutableTreeNode(warning);
					readerWarnings.add(warn);
				}
				leaf.add(readerWarnings);*/
			}
			
			if (s.writer != null && s.writer.getWarnings().length != 0) {
				warnings = true;
				/*DefaultMutableTreeNode writerWarnings = new DefaultMutableTreeNode(
						I18n.getText("control.convert.writerWarnings"));
				
				// put them all in a hash set first so no duplicates.
				HashSet<String> set = new HashSet<String>();
				for (ConversionWarning warning : s.writer.getWarnings()) {
					set.add(warning.toStringWithField());
				}
				
				for(String warning : set){
					DefaultMutableTreeNode warn = new DefaultMutableTreeNode(warning);
					writerWarnings.add(warn);
				}
				leaf.add(writerWarnings);*/
			}
			
			if (warnings) {
				s.warnings = true;
				if (!fail) { // make sure we didn't already count this file as a fail
					convWWarnings++;
				}
			}
		}
		
		model.setProcessed(processed);
		model.setFailed(failed);
		model.setConvWithWarnings(convWWarnings);
		model.setNodes(nodes);
	}
	
	
	// wrapper for putting in tree nodes
	public static class StructWrapper {
		public ConvertModel.ReaderWriterObject struct;
		
		public StructWrapper(ConvertModel.ReaderWriterObject argStruct) {
			struct = argStruct;
		}
		
		@Override
		public String toString() {
			return struct.file;
		}
	}
	
	public static class DendroWrapper {
		public IDendroFile file;
		public INamingConvention convention;
		
		public DendroWrapper(IDendroFile argFile, INamingConvention argConvention) {
			file = argFile;
			convention = argConvention;
		}
		
		@Override
		public String toString() {
			return convention.getFilename(file) + "." + file.getExtension();
		}
	}
	
	
	private static TridasTridas mergeProjectsIntoOne(TridasTridas container)
	{
		if(container == null) return null;		
		if(container.getProjects()==null) return null;
		if(container.getProjects().size()==0) return null;
		if(container.getProjects().size()==1) return container;
		
		TridasTridas newContainer = new TridasTridas();
		newContainer.getProjects().add(container.getProjects().get(0));

		for(int i=1; i<container.getProjects().size(); i++)
		{
			List<TridasObject> objlist = container.getProjects().get(i).getObjects();
			newContainer.getProjects().get(0).getObjects().addAll(objlist);
		}
		
		return newContainer;
	}
	
	private static TridasTridas mergeObjectsIntoOne(TridasTridas container)
	{
		if(container == null) return null;		
		if(container.getProjects()==null) return null;
		if(container.getProjects().size()==0) return null;
	
		try{
		TridasTridas newContainer = new TridasTridas();
		TridasProject project = container.getProjects().get(0);
		TridasObject object = TridasUtils.getObjectList(project).get(0);
		project.getObjects().clear();

		ArrayList<TridasElement> ellist = new ArrayList<TridasElement>();
		
		for(TridasProject p: container.getProjects())
		{
			for(TridasObject o : TridasUtils.getObjectList(p))
			{
				ellist.addAll(o.getElements());
			}
			
			project.getDerivedSeries().addAll(p.getDerivedSeries());
		}
		
		object.getElements().addAll(ellist);
		project.getObjects().add(object);
			
		newContainer.getProjects().add(project);
		return newContainer;
		
		} catch (Exception e)
		{
			log.error("Exception caught when trying to merge objects into one");
			e.printStackTrace();
			return container;
		}
	}
}
