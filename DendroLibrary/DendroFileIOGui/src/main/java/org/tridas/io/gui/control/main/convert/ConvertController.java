/**
 * Created on May 27, 2010, 2:37:40 AM
 */
package org.tridas.io.gui.control.main.convert;

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
	
	public ConvertController(){
		registerEventKey(CONVERT, "convert");
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
	
	private IDendroCollectionWriter writer;
	private void convertFiles(String[] argFiles, String argInputFormat, String argOutputFormat, INamingConvention argNaming){
		
		ArrayList<ProjectToFiles> list = new ArrayList<ProjectToFiles>();
		
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
			struct.warnings.addAll(reader.getWarnings());
		}
		
		writer = TridasIO.getFileWriter(argOutputFormat);
		writer.setNamingConvention(argNaming);
		
		for(ProjectToFiles struct : list){
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
			struct.warnings.addAll(writer.getWarnings());
		}
		
		constructNodes(list);
	}
	
	/**
	 * @param list
	 */
	private void constructNodes(ArrayList<ProjectToFiles> list) {
		ArrayList<DefaultMutableTreeNode> nodes = new ArrayList<DefaultMutableTreeNode>();
		
		for(ProjectToFiles s : list){
			DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(s.file);
			DefaultMutableTreeNode files = new DefaultMutableTreeNode("Files");

			for(IDendroFile file : s.files){
				DefaultMutableTreeNode fileNode = new DefaultMutableTreeNode(writer.getNamingConvention().getFilename(file)+"."+file.getExtension());
				files.add(fileNode);
			}
			
			DefaultMutableTreeNode readerWarnings = new DefaultMutableTreeNode("Warnings");
			for(ConversionWarning warning : s.warnings){
				DefaultMutableTreeNode warn = new DefaultMutableTreeNode(warning.toString());
				readerWarnings.add(warn);
			}
			leaf.add(files);
			if(s.warnings.size() !=0){
				leaf.add(readerWarnings);
			}
			nodes.add(leaf);
		}
		
		ConvertModel model = ConvertModel.getInstance();
		model.setNodes(nodes);
	}

	private class ProjectToFiles{
		String file;
		String errorMessage = null;
		TridasProject project = null;
		IDendroFile[] files = null;
		ArrayList<ConversionWarning> warnings = new ArrayList<ConversionWarning>();
	}
}
