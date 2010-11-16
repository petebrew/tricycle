/**
 * Created at Sep 10, 2010, 8:19:45 PM
 */
package org.tridas.io.gui.command;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

import org.grlea.log.DebugLevel;
import org.grlea.log.SimpleLogger;
import org.tridas.io.AbstractDendroCollectionWriter;
import org.tridas.io.AbstractDendroFileReader;
import org.tridas.io.IDendroFile;
import org.tridas.io.TridasIO;
import org.tridas.io.defaults.IMetadataFieldSet;
import org.tridas.io.exceptions.ConversionWarning;
import org.tridas.io.exceptions.ConversionWarningException;
import org.tridas.io.exceptions.IncompleteTridasDataException;
import org.tridas.io.exceptions.IncorrectDefaultFieldsException;
import org.tridas.io.exceptions.InvalidDendroFileException;
import org.tridas.io.gui.I18n;
import org.tridas.io.gui.control.convert.ConvertEvent;
import org.tridas.io.gui.enums.InputFormat;
import org.tridas.io.gui.model.ConvertModel;
import org.tridas.io.gui.model.FileListModel;
import org.tridas.io.gui.model.MainWindowModel;
import org.tridas.io.gui.model.TricycleModelLocator;
import org.tridas.io.gui.model.popup.ConvertingDialogModel;
import org.tridas.io.gui.view.popup.ConvertProgress;
import org.tridas.io.naming.HierarchicalNamingConvention;
import org.tridas.io.naming.INamingConvention;
import org.tridas.io.naming.NumericalNamingConvention;
import org.tridas.io.naming.UUIDNamingConvention;
import org.tridas.schema.TridasProject;

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
	private final static SimpleLogger log = new SimpleLogger(ConvertCommand.class);
	
	public void execute(MVCEvent argEvent) {
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
			}
		}
		if (!outputFormatFound) {
			JOptionPane.showMessageDialog(null, I18n.getText("control.convert.noOutput", outputFormat),
					I18n.getText("control.convert.error"), JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		FileListModel fileList = FileListModel.getInstance();
		
		boolean inputFormatFound = false;
		for (String format : TridasIO.getSupportedReadingFormats()) {
			if (format.equalsIgnoreCase(event.getInputFormat())) {
				fileList.setInputFormat(format);
				inputFormatFound = true;
			}
		}
		if (!inputFormatFound && !event.getInputFormat().equals(InputFormat.AUTO)) {
			JOptionPane.showMessageDialog(null, I18n.getText("control.convert.noInput", outputFormat),
					I18n.getText("control.convert.error"), JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (event.getNamingConvention().equals("UUID")) {
			naming = new UUIDNamingConvention();
		}
		else if (event.getNamingConvention().equals("Hierarchical")) {
			naming = new HierarchicalNamingConvention();
		}
		else if (event.getNamingConvention().equals("Numerical")) {
			naming = new NumericalNamingConvention();
		}
		else {
			JOptionPane.showMessageDialog(null, I18n.getText("control.convert.noNaming", event.getNamingConvention()),
					I18n.getText("control.convert.error"), JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		convertFiles(fileList.getInputFiles().toArray(new String[0]), fileList.getInputFormat(),
				event.getReaderDefaults(), outputFormat, event.getWriterDefaults(), naming);
	}
	
	private void convertFiles(String[] argFiles, String argInputFormat, IMetadataFieldSet argInputDefaults,
			String argOutputFormat, IMetadataFieldSet argOutputDefaults, INamingConvention argNaming) {
		
		MainWindowModel mwm = MainWindowModel.getInstance();
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
			ConvertModel cmodel =ConvertModel.getInstance();
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
				
				TridasProject project = reader.getProject();
				
				AbstractDendroCollectionWriter writer = TridasIO.getFileWriter(argOutputFormat);
				
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
				writer.setNamingConvention(argNaming);
				
				if (struct.errorMessage != null) {
					continue;
				}
				
				try {
					if (argOutputDefaults == null) {
						writer.loadProject(project);
					}
					else {
						writer.loadProject(project, (IMetadataFieldSet) argOutputDefaults.clone());
					}
				} catch (IncompleteTridasDataException e) {
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
			log.error("Exception thrown while converting.");
			log.dbe(DebugLevel.L2_ERROR, e);
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
		
		ConvertModel model = ConvertModel.getInstance();
		model.getConvertedList().clear();
		model.getConvertedList().addAll(list);
		
		// we need to set processed/failed/convertedWithWarnings
		int processed = 0;
		int failed = 0;
		int convWWarnings = 0;
		
		for (ConvertModel.ReaderWriterObject s : list) {
			DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(new StructWrapper(s));
			nodes.add(leaf);
			
			processed++;
			
			boolean fail = false;
			if (s.errorMessage != null) {
				DefaultMutableTreeNode errorMessage = new DefaultMutableTreeNode(s.errorMessage);
				leaf.add(errorMessage);
				nodes.add(leaf);
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
							set.add(warning.toStringWithField());
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
				DefaultMutableTreeNode readerWarnings = new DefaultMutableTreeNode(
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
				leaf.add(readerWarnings);
			}
			
			if (s.writer != null && s.writer.getWarnings().length != 0) {
				warnings = true;
				DefaultMutableTreeNode writerWarnings = new DefaultMutableTreeNode(
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
				leaf.add(writerWarnings);
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
}
