/**
 * Created on May 27, 2010, 2:37:40 AM
 */
package org.tridas.io.gui.control.convert;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

import org.grlea.log.SimpleLogger;
import org.tridas.io.AbstractDendroCollectionWriter;
import org.tridas.io.IDendroFile;
import org.tridas.io.AbstractDendroFileReader;
import org.tridas.io.TridasIO;
import org.tridas.io.gui.enums.InputFormat;
import org.tridas.io.gui.model.ConfigModel;
import org.tridas.io.gui.model.ConvertModel;
import org.tridas.io.gui.model.FileListModel;
import org.tridas.io.gui.model.MainWindowModel;
import org.tridas.io.gui.model.ModelLocator;
import org.tridas.io.gui.view.ConvertPanel;
import org.tridas.io.gui.view.popup.ConvertProgress;
import org.tridas.io.gui.view.popup.SavingProgress;
import org.tridas.io.naming.HierarchicalNamingConvention;
import org.tridas.io.naming.INamingConvention;
import org.tridas.io.naming.NumericalNamingConvention;
import org.tridas.io.naming.UUIDNamingConvention;
import org.tridas.io.util.IOUtils;
import org.tridas.io.warnings.ConversionWarning;
import org.tridas.io.warnings.ConversionWarningException;
import org.tridas.io.warnings.IncompleteTridasDataException;
import org.tridas.io.warnings.InvalidDendroFileException;
import org.tridas.schema.TridasProject;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.FrontController;

/**
 * @author Daniel
 */
public class ConvertController extends FrontController {
	private static final SimpleLogger log = new SimpleLogger(ConvertController.class);
	
	public static final String SAVE = "CONVERT_SAVE";
	public static final String CONVERT = "CONVERT_CONVERT";
	
	private SavingProgress savingProgress = null;
	private ConvertProgress convertProgress = null; 
	private ArrayList<ProjectToFiles> structList = new ArrayList<ProjectToFiles>();
	
	public ConvertController() {
		try {
			registerCommand(CONVERT, "convert");
			registerCommand(SAVE, "save");
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	
	public void save(MVCEvent argEvent) {
		File folder = IOUtils.outputFolder(null);
		if (folder == null) {
			return;
		}
		
		ConvertModel model = ConvertModel.getInstance();
		model.setSavingFilename("");
		model.setSavingPercent(0);
		
		int totalFiles = 0;
		for (ProjectToFiles p : structList) {
			if (p.writer == null) {
				continue;
			}
			for (IDendroFile f : p.writer.getFiles()) {
				totalFiles++;
			}
		}
		if (totalFiles == 0) {
			JOptionPane.showMessageDialog(null, "No files to save", "", JOptionPane.PLAIN_MESSAGE);
			return;
		}
		
		MainWindowModel mwm = MainWindowModel.getInstance();
		mwm.setLock(true);
		
		int currFile = 0;
		if(savingProgress == null){
			savingProgress = new SavingProgress(ModelLocator.getInstance().getMainWindow());
			savingProgress.setAlwaysOnTop(true);
		}
		savingProgress.setVisible(true);
		savingProgress.toFront();
		for (int i = 0; i < structList.size(); i++) {
			ProjectToFiles p = structList.get(i);
			if (p.writer != null) {
				
				String outputFolder = folder.getAbsolutePath();
				// custom implementation of saveAllToDisk, as we need to
				// keep track of each dendro file for the progress window
				if (outputFolder.contains("\\")) {
					outputFolder.replaceAll("\\\\", "/");
				}
				
				if (!outputFolder.endsWith("/") && !outputFolder.equals("")) {
					outputFolder += File.separator;
				}
				
				for (IDendroFile dof : p.writer.getFiles()) {
					currFile++;
					String filename = p.writer.getNamingConvention().getFilename(dof);
					
					model.setSavingFilename(filename + "." + dof.getExtension());
					p.writer.saveFileToDisk(outputFolder, dof);
					model.setSavingPercent(currFile * 100 / totalFiles);
				}
			}
		}
		savingProgress.setVisible(false);
		mwm.setLock(false);
		
		JOptionPane.showMessageDialog(null, "Files saved to '" + folder.getAbsolutePath() + "'", "Save complete",
				JOptionPane.PLAIN_MESSAGE);
	}
	
	public void convert(MVCEvent argEvent) {
		ConvertEvent event = (ConvertEvent) argEvent;
		String outputFormat = event.getOutputFormat();
		INamingConvention naming;
		ConvertModel model = ConvertModel.getInstance();
		
		boolean outputFormatFound = false;
		for (String format : TridasIO.getSupportedWritingFormats()) {
			if (format.equalsIgnoreCase(outputFormat)) {
				outputFormat = format;
				outputFormatFound = true;
			}
		}
		if (!outputFormatFound) {
			JOptionPane.showMessageDialog(null, "Could not find output format '" + outputFormat + "'", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		ConfigModel config = ConfigModel.getInstance();
		FileListModel fileList = FileListModel.getInstance();
		
		boolean inputFormatFound = false;
		for (String format : TridasIO.getSupportedReadingFormats()) {
			if (format.equalsIgnoreCase(config.getInputFormat())) {
				config.setInputFormat(format);
				inputFormatFound = true;
			}
		}
		if (!inputFormatFound && !config.getInputFormat().equals(InputFormat.AUTO)) {
			JOptionPane.showMessageDialog(null, "Could not find input format '" + config.getInputFormat() + "'",
					"Error", JOptionPane.ERROR_MESSAGE);
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
			JOptionPane.showMessageDialog(null, "Could not find naming convention '" + event.getNamingConvention()
					+ "'", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		convertFiles(fileList.getInputFiles().toArray(new String[0]), config.getInputFormat(), outputFormat, naming);
	}
	
	private void convertFiles(String[] argFiles, String argInputFormat, String argOutputFormat,
			INamingConvention argNaming) {
		
		MainWindowModel mwm = MainWindowModel.getInstance();
		mwm.setLock(true);
		ArrayList<ProjectToFiles> list = new ArrayList<ProjectToFiles>();
		
		ConvertModel model = ConvertModel.getInstance();
		
		if(convertProgress == null){
			convertProgress = new ConvertProgress(ModelLocator.getInstance().getMainWindow());
			convertProgress.setAlwaysOnTop(true);
		}
		convertProgress.setVisible(true);
		convertProgress.toFront();
		
		for (int i=0; i<argFiles.length; i++){
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
			
			ProjectToFiles struct = new ProjectToFiles();
			list.add(struct);
			struct.file = file;
			
			if (reader == null) {
				struct.errorMessage = "Reader was null, not supposed to happen.  Please report bug.";
				continue;
			}
			
			try {
				reader.loadFile(file);
			} catch (IOException e) {
				struct.errorMessage = "IOException while loading file: " + e;
				continue;
			} catch (InvalidDendroFileException e) {
				struct.errorMessage = e.toString();
				continue;
			}
			
			TridasProject project = reader.getProject();
			
			AbstractDendroCollectionWriter writer = TridasIO.getFileWriter(argOutputFormat);
			writer.setNamingConvention(argNaming);
			
			if (struct.errorMessage != null) {
				continue;
			}
			
			try {
				writer.loadProject(project);
			} catch (IncompleteTridasDataException e) {
				struct.errorMessage = e.toString();
			} catch (ConversionWarningException e) {
				struct.errorMessage = e.toString();
			}
			
			struct.reader = reader;
			struct.writer = writer;
			
			model.setConvertingPercent(i*100/argFiles.length);
		}
		convertProgress.setVisible(false);
		
		constructNodes(list, argNaming);
		mwm.setLock(false);
	}
	
	/**
	 * @param list
	 */
	private void constructNodes(ArrayList<ProjectToFiles> list, INamingConvention argNaming) {
		ArrayList<DefaultMutableTreeNode> nodes = new ArrayList<DefaultMutableTreeNode>();
		
		structList.clear();
		structList.addAll(list);
		
		// we need to set processed/failed/convertedWithWarnings
		int processed = 0;
		int failed = 0;
		int convWWarnings = 0;
		
		for (ProjectToFiles s : list) {
			DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(s.file);
			nodes.add(leaf);
			
			processed++;
			
			if (s.errorMessage != null) {
				DefaultMutableTreeNode errorMessage = new DefaultMutableTreeNode(s.errorMessage);
				leaf.add(errorMessage);
				nodes.add(leaf);
				failed++;
				continue;
			}
			
			boolean warnings = false;
			for (IDendroFile file : s.writer.getFiles()) {
				DefaultMutableTreeNode fileNode = new DefaultMutableTreeNode(argNaming.getFilename(file) + "."
						+ file.getExtension());
				if (file.getDefaults().getConversionWarnings().size() != 0) {
					for (ConversionWarning warning : file.getDefaults().getConversionWarnings()) {
						DefaultMutableTreeNode warningNode = new DefaultMutableTreeNode(warning.toString());
						fileNode.add(warningNode);
					}
					warnings = true;
				}
				leaf.add(fileNode);
			}
			
			
			if(s.reader.getWarnings().length != 0){
				warnings = true;
				DefaultMutableTreeNode readerWarnings = new DefaultMutableTreeNode("Reader Warnings");
				for (ConversionWarning warning : s.reader.getWarnings()) {
					DefaultMutableTreeNode warn = new DefaultMutableTreeNode(warning.toString());
					readerWarnings.add(warn);
				}
				leaf.add(readerWarnings);
			}
			
			if (s.writer.getWarnings().length != 0) {
				warnings = true;
				DefaultMutableTreeNode writerWarnings = new DefaultMutableTreeNode("Writer Warnings");
				for (ConversionWarning warning : s.writer.getWarnings()) {
					DefaultMutableTreeNode warn = new DefaultMutableTreeNode(warning.toString());
					writerWarnings.add(warn);
				}
				leaf.add(writerWarnings);
			}
			
			if(warnings){
				convWWarnings++;
			}
		}
		
		ConvertModel model = ConvertModel.getInstance();
		model.setProcessed(processed);
		model.setFailed(failed);
		model.setConvWithWarnings(convWWarnings);
		model.setNodes(nodes);
	}
	
	private class ProjectToFiles {
		String file;
		String errorMessage = null;
		AbstractDendroFileReader reader = null;
		AbstractDendroCollectionWriter writer = null;
	}
}
