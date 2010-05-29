/**
 * Created on May 27, 2010, 2:37:40 AM
 */
package org.tridas.io.gui.control.main.convert;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

import org.tridas.io.IDendroCollectionWriter;
import org.tridas.io.IDendroFile;
import org.tridas.io.IDendroFileReader;
import org.tridas.io.TridasIO;
import org.tridas.io.gui.model.ConvertModel;
import org.tridas.io.gui.model.FileListModel;
import org.tridas.io.gui.mvc.control.FrontController;
import org.tridas.io.gui.mvc.control.MVCEvent;
import org.tridas.io.naming.HierarchicalNamingConvention;
import org.tridas.io.naming.INamingConvention;
import org.tridas.io.naming.NumericalNamingConvention;
import org.tridas.io.naming.UUIDNamingConvention;
import org.tridas.io.util.FileHelper;
import org.tridas.io.util.IOUtils;
import org.tridas.io.warnings.ConversionWarning;
import org.tridas.io.warnings.ConversionWarningException;
import org.tridas.io.warnings.IncompleteTridasDataException;
import org.tridas.io.warnings.InvalidDendroFileException;
import org.tridas.schema.TridasProject;

/**
 * @author Daniel
 *
 */
public class ConvertController extends FrontController {
	public static final String SAVE = "CONVERT_SAVE";
	public static final String CONVERT = "CONVERT_CONVERT";
	
	
	private ArrayList<ProjectToFiles> structList = new ArrayList<ProjectToFiles>();
	
	public ConvertController(){
		registerEventKey(CONVERT, "convert");
		registerEventKey(SAVE, "save");
	}
	
	public void save(MVCEvent argEvent){
		File folder = IOUtils.outputFolder(null);
		if(folder == null){
			return;
		}
		
		FileHelper help = new FileHelper(folder.getAbsolutePath());
		
		for(ProjectToFiles p : structList){
			if(p.files != null){
				for(IDendroFile file : p.files){
					String[] fileStrings = file.saveToString();
				}
			}
		}
	}
	
	public void convert(MVCEvent argEvent){
		ConvertEvent event = (ConvertEvent) argEvent;
		String outputFormat = event.getOutputFormat();
		INamingConvention naming;
		
		boolean outputFormatFound = false;
		for(String format : TridasIO.getSupportedWritingFormats()){
			if(format.equalsIgnoreCase(outputFormat)){
				outputFormat = format;
				outputFormatFound = true;
			}
		}
		if(!outputFormatFound){
			JOptionPane.showMessageDialog(null, "Could not find output format '"+outputFormat+"'", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		FileListModel model = FileListModel.getInstance();
		
		boolean inputFormatFound = false;
		for(String format : TridasIO.getSupportedReadingFormats()){
			if(format.equalsIgnoreCase(model.getInputFormat())){
				model.setInputFormat(format);
				inputFormatFound = true;
			}
		}
		if(!inputFormatFound && !model.getInputFormat().equals("automatic")){
			JOptionPane.showMessageDialog(null, "Could not find input format '"+model.getInputFormat()+"'", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if(event.getNamingConvention().equals("UUID")){
			naming = new UUIDNamingConvention();
		}else if(event.getNamingConvention().equals("Hierarchical")){
			naming = new HierarchicalNamingConvention();
		}else if(event.getNamingConvention().equals("Numerical")){
			naming = new NumericalNamingConvention();
		}else{
			JOptionPane.showMessageDialog(null, "Could not find naming convention '"+event.getNamingConvention()+"'", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		convertFiles(model.getInputFiles().toArray(new String[0]), model.getInputFormat(), outputFormat, naming);
	}
	
	private void convertFiles(String[] argFiles, String argInputFormat, String argOutputFormat, INamingConvention argNaming){
		
		ArrayList<ProjectToFiles> list = new ArrayList<ProjectToFiles>();
		IDendroCollectionWriter writer;
		for(String file : argFiles){
			IDendroFileReader reader;
			if(argInputFormat.equals("automatic")){
				reader = TridasIO.getFileReaderFromExtension(file.substring(file.lastIndexOf(".")+1));
			}else {
				reader = TridasIO.getFileReader(argInputFormat);
			}
			
			ProjectToFiles struct = new ProjectToFiles();
			list.add(struct);
			struct.file = file;
			if(reader == null){
				struct.errorMessage = "Reader was null, not supposed to happen.  Please report bug.";
				continue;
			}
			
			try {
				reader.loadFile(file);
			}
			catch (IOException e) {
				struct.errorMessage = "IOException while loading file: "+e;
				continue;
			}
			catch (InvalidDendroFileException e) {
				struct.errorMessage = e.toString();
				continue;
			}
			
			TridasProject project = reader.getProject();
			struct.project = project;
			struct.readerWarnings.addAll(reader.getWarnings());
			struct.readerWarnings.addAll(reader.getDefaults().getConversionWarnings());
		}
		
		for(ProjectToFiles struct : list){
			writer = TridasIO.getFileWriter(argOutputFormat);
			writer.setNamingConvention(argNaming);
			
			if(struct.errorMessage != null){
				continue;
			}
			
			writer.clearFiles();
			try {
				writer.loadProject(struct.project);
			}
			catch (IncompleteTridasDataException e) {
				struct.errorMessage = e.toString();
			}
			catch (ConversionWarningException e) {
				struct.errorMessage = e.toString();
			}
			
			struct.files = writer.getFiles();
			struct.writerWarnings.addAll(writer.getWarnings());
			writer.clearWarnings();
		}
		
		constructNodes(list, argNaming);
	}
	
	/**
	 * @param list
	 */
	private void constructNodes(ArrayList<ProjectToFiles> list, INamingConvention argNaming) {
		ArrayList<DefaultMutableTreeNode> nodes = new ArrayList<DefaultMutableTreeNode>();
		
		structList.clear();
		structList.addAll(list);
		
		for(ProjectToFiles s : list){
			DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(s.file);
			DefaultMutableTreeNode files = new DefaultMutableTreeNode("Files");
			nodes.add(leaf);

			if(s.errorMessage != null){
				DefaultMutableTreeNode errorMessage = new DefaultMutableTreeNode(s.errorMessage);
				leaf.add(errorMessage);
				nodes.add(leaf);
				continue;
			}
			
			for(IDendroFile file : s.files){
				DefaultMutableTreeNode fileNode = new DefaultMutableTreeNode(argNaming.getFilename(file)+"."+file.getExtension());
				if(file.getDefaults().getConversionWarnings().size() != 0){
					for(ConversionWarning warning : file.getDefaults().getConversionWarnings()){
						DefaultMutableTreeNode warningNode = new DefaultMutableTreeNode(warning.toString());
						fileNode.add(warningNode);
					}
				}
				files.add(fileNode);
			}
			
			if(s.readerWarnings.size() != 0){
				DefaultMutableTreeNode readerWarnings = new DefaultMutableTreeNode("Reader Warnings");
				for(ConversionWarning warning : s.readerWarnings){
					DefaultMutableTreeNode warn = new DefaultMutableTreeNode(warning.toString());
					readerWarnings.add(warn);
				}
				leaf.add(readerWarnings);
			}
			
			if(s.writerWarnings.size() != 0){
				DefaultMutableTreeNode writerWarnings = new DefaultMutableTreeNode("Writer Warnings");
				for(ConversionWarning warning : s.writerWarnings){
					DefaultMutableTreeNode warn = new DefaultMutableTreeNode(warning.toString());
					writerWarnings.add(warn);
				}
				leaf.add(writerWarnings);
			}
			
			leaf.add(files);
		}
		
		ConvertModel model = ConvertModel.getInstance();
		model.setNodes(nodes);
	}

	private class ProjectToFiles{
		String file;
		String errorMessage = null;
		TridasProject project = null;
		IDendroFile[] files = null;
		ArrayList<ConversionWarning> readerWarnings = new ArrayList<ConversionWarning>();
		ArrayList<ConversionWarning> writerWarnings = new ArrayList<ConversionWarning>();
	}
}
