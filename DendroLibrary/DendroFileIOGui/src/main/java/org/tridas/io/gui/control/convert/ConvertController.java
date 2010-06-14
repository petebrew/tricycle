/**
 * Created on May 27, 2010, 2:37:40 AM
 */
package org.tridas.io.gui.control.convert;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.commons.lang.StringUtils;
import org.grlea.log.SimpleLogger;
import org.tridas.io.AbstractDendroCollectionWriter;
import org.tridas.io.AbstractDendroFileReader;
import org.tridas.io.IDendroFile;
import org.tridas.io.TridasIO;
import org.tridas.io.gui.I18n;
import org.tridas.io.gui.enums.InputFormat;
import org.tridas.io.gui.model.ConfigModel;
import org.tridas.io.gui.model.ConvertModel;
import org.tridas.io.gui.model.FileListModel;
import org.tridas.io.gui.model.MainWindowModel;
import org.tridas.io.gui.model.ModelLocator;
import org.tridas.io.gui.model.PreviewModel;
import org.tridas.io.gui.view.MainWindow;
import org.tridas.io.gui.view.popup.ConvertProgress;
import org.tridas.io.gui.view.popup.PreviewWindow;
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
import com.dmurph.mvc.ObjectEvent;
import com.dmurph.mvc.control.FrontController;

/**
 * @author Daniel
 */
public class ConvertController extends FrontController {
	private static final SimpleLogger log = new SimpleLogger(ConvertController.class);
	
	public static final String SAVE = "CONVERT_SAVE";
	public static final String CONVERT = "CONVERT_CONVERT";
	public static final String PREVIEW = "CONVERT_PREVIEW";
	
	private SavingProgress savingProgress = null;
	private ConvertProgress convertProgress = null;
	// TODO get rid of this, use model nodes instead
	private ArrayList<ReaderWriterObject> structList = new ArrayList<ReaderWriterObject>();
	
	public ConvertController() {
		try {
			registerCommand(PREVIEW, "preview");
			registerCommand(CONVERT, "convert");
			registerCommand(SAVE, "save");
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void preview(MVCEvent argEvent) {
		ObjectEvent<DefaultMutableTreeNode> event = (ObjectEvent<DefaultMutableTreeNode>) argEvent;
		
		DefaultMutableTreeNode node = event.getValue();
		
		if (node.getUserObject() == null) {
			return;
		}
		if (node.getUserObject() instanceof DendroWrapper) {
			DendroWrapper file = (DendroWrapper) node.getUserObject();
			
			PreviewModel pmodel = new PreviewModel();
			pmodel.setFilename(file.toString());
			
			boolean worked = false;
			String[] strings = null;
			try {
				strings = file.file.saveToString();
				worked = strings != null;
			} catch (Exception e) {
				worked = false;
			}
			
			if (worked) {
				pmodel.setFileString(StringUtils.join(strings, "\n"));
			}
			else {
				pmodel.setFileString(I18n.getText("view.popup.preview.error"));
			}
			
			MainWindow window = ModelLocator.getInstance().getMainWindow();
			Dimension size = window.getSize();
			
			PreviewWindow preview = new PreviewWindow(window, size.width - 40, size.height - 40, pmodel);
			preview.setVisible(true);
			preview.toFront();
		}
	}
	
	@SuppressWarnings("unused")
	public void save(MVCEvent argEvent) {
		// get rid of popups
		
		File folder = IOUtils.outputFolder(null);
		if (folder == null) {
			return;
		}
		
		ConvertModel model = ConvertModel.getInstance();
		model.setSavingFilename("");
		model.setSavingPercent(0);
		
		int totalFiles = 0;
		for (ReaderWriterObject p : structList) {
			if (p.writer == null) {
				continue;
			}
			for (IDendroFile f : p.writer.getFiles()) {
				totalFiles++;
			}
		}
		if (totalFiles == 0) {
			JOptionPane.showMessageDialog(null, I18n.getText("control.convert.noFiles"), "", JOptionPane.PLAIN_MESSAGE);
			return;
		}
		
		MainWindowModel mwm = MainWindowModel.getInstance();
		mwm.setLock(true);
		
		int currFile = 0;
		if (savingProgress == null) {
			savingProgress = new SavingProgress(ModelLocator.getInstance().getMainWindow());
			savingProgress.setAlwaysOnTop(true);
		}
		savingProgress.setVisible(true);
		savingProgress.toFront();
		for (int i = 0; i < structList.size(); i++) {
			ReaderWriterObject p = structList.get(i);
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
		
		JOptionPane.showMessageDialog(null,
				I18n.getText("control.convert.save.folderInfo", folder.getAbsolutePath()),
				I18n.getText("control.convert.save.complete"),
				JOptionPane.PLAIN_MESSAGE);
	}
	
	public void convert(MVCEvent argEvent) {
		ConvertEvent event = (ConvertEvent) argEvent;
		String outputFormat = event.getOutputFormat();
		INamingConvention naming;
		
		boolean outputFormatFound = false;
		for (String format : TridasIO.getSupportedWritingFormats()) {
			if (format.equalsIgnoreCase(outputFormat)) {
				outputFormat = format;
				outputFormatFound = true;
			}
		}
		if (!outputFormatFound) {
			JOptionPane.showMessageDialog(null,
					I18n.getText("control.convert.noOutput",outputFormat),
					I18n.getText("control.convert.error"),
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
			JOptionPane.showMessageDialog(null,
					I18n.getText("control.convert.noInput",outputFormat),
					I18n.getText("control.convert.error"),
					JOptionPane.ERROR_MESSAGE);
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
			JOptionPane.showMessageDialog(null,
					I18n.getText("control.convert.noNaming", event.getNamingConvention()),
					I18n.getText("control.convert.error"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		convertFiles(fileList.getInputFiles().toArray(new String[0]), config.getInputFormat(), outputFormat, naming);
	}
	
	private void convertFiles(String[] argFiles, String argInputFormat, String argOutputFormat,
			INamingConvention argNaming) {
		
		MainWindowModel mwm = MainWindowModel.getInstance();
		mwm.setLock(true);
		ArrayList<ReaderWriterObject> list = new ArrayList<ReaderWriterObject>();
		
		ConvertModel model = ConvertModel.getInstance();
		
		if (convertProgress == null) {
			convertProgress = new ConvertProgress(ModelLocator.getInstance().getMainWindow());
			convertProgress.setAlwaysOnTop(true);
		}
		convertProgress.setVisible(true);
		convertProgress.toFront();
		
		for (int i = 0; i < argFiles.length; i++) {
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
			
			ReaderWriterObject struct = new ReaderWriterObject();
			list.add(struct);
			struct.file = file;
			
			if (reader == null) {
				struct.errorMessage = I18n.getText("control.convert.readerNull");
				continue;
			}
			
			try {
				reader.loadFile(file);
			} catch (IOException e) {
				struct.errorMessage = I18n.getText("control.convert.ioException", e.toString());
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
			
			if(writer.getFiles().length == 0){
				struct.errorMessage = I18n.getText("control.convert.noFilesWritten");
			}
			
			model.setConvertingPercent(i * 100 / argFiles.length);
		}
		convertProgress.setVisible(false);
		
		constructNodes(list, argNaming);
		mwm.setLock(false);
	}
	
	private void constructNodes(ArrayList<ReaderWriterObject> list, INamingConvention argNaming) {
		ArrayList<DefaultMutableTreeNode> nodes = new ArrayList<DefaultMutableTreeNode>();
		
		structList.clear();
		structList.addAll(list);
		
		// we need to set processed/failed/convertedWithWarnings
		int processed = 0;
		int failed = 0;
		int convWWarnings = 0;
		
		for (ReaderWriterObject s : list) {
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
			if(s.writer != null){
				for (IDendroFile file : s.writer.getFiles()) {
					DefaultMutableTreeNode fileNode = new DefaultMutableTreeNode(new DendroWrapper(file, argNaming));
					if (file.getDefaults().getConversionWarnings().size() != 0) {
						for (ConversionWarning warning : file.getDefaults().getConversionWarnings()) {
							DefaultMutableTreeNode warningNode = new DefaultMutableTreeNode(warning.toString());
							fileNode.add(warningNode);
						}
						warnings = true;
					}
					leaf.add(fileNode);
				}
			}
			
			if (s.reader != null && s.reader.getWarnings().length != 0) {
				warnings = true;
				DefaultMutableTreeNode readerWarnings = new DefaultMutableTreeNode(I18n.getText("control.convert.readerWarnings"));
				for (ConversionWarning warning : s.reader.getWarnings()) {
					DefaultMutableTreeNode warn = new DefaultMutableTreeNode(warning.toString());
					readerWarnings.add(warn);
				}
				leaf.add(readerWarnings);
			}
			
			if (s.writer != null && s.writer.getWarnings().length != 0) {
				warnings = true;
				DefaultMutableTreeNode writerWarnings = new DefaultMutableTreeNode(I18n.getText("control.convert.writerWarnings"));
				for (ConversionWarning warning : s.writer.getWarnings()) {
					DefaultMutableTreeNode warn = new DefaultMutableTreeNode(warning.toString());
					writerWarnings.add(warn);
				}
				leaf.add(writerWarnings);
			}
			
			if (warnings) {
				s.warnings = true;
				if(!fail){ // make sure we didn't already count this file as a fail
					convWWarnings++;
				}
			}
		}
		
		ConvertModel model = ConvertModel.getInstance();
		model.setProcessed(processed);
		model.setFailed(failed);
		model.setConvWithWarnings(convWWarnings);
		model.setNodes(nodes);
	}
	
	public static class ReaderWriterObject {
		public String file;
		public String errorMessage = null;
		public AbstractDendroFileReader reader = null;
		public AbstractDendroCollectionWriter writer = null;
		public boolean warnings = false;
	}
	
	// wrapper for putting in tree nodes
	public static class StructWrapper {
		public ReaderWriterObject struct;
		
		public StructWrapper(ReaderWriterObject argStruct) {
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