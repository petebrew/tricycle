package org.tridas.io.gui.command;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.tridas.io.AbstractDendroCollectionWriter;
import org.tridas.io.AbstractDendroFileReader;
import org.tridas.io.DendroFileFilter;
import org.tridas.io.TridasIO;
import org.tridas.io.exceptions.InvalidDendroFileException;
import org.tridas.io.formats.excelmatrix.ExcelMatrixReader;
import org.tridas.io.formats.past4.Past4Reader;
import org.tridas.io.formats.tridas.TridasReader;
import org.tridas.io.gui.model.FileListModel;
import org.tridas.io.gui.model.TricycleModelLocator;
import org.tridas.schema.TridasProject;

import com.dmurph.mvc.IllegalThreadException;
import com.dmurph.mvc.IncorrectThreadException;
import com.dmurph.mvc.MVC;
import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

public class GuessFormatCommand implements ICommand {

	@Override
	public void execute(MVCEvent argEvent) {
		
		try {
			MVC.splitOff();
		} catch (IllegalThreadException e1) {
			e1.printStackTrace();
		} catch (IncorrectThreadException e1) {
			e1.printStackTrace();
		}

		// custom jfilechooser
		File file = null;
		String format = null;
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled(false);

		// Add file filters
		ArrayList<DendroFileFilter> filters = TridasIO.getFileFilterArray();
		Collections.sort(filters);
		for(DendroFileFilter filter : filters)
		{
			fc.addChoosableFileFilter(filter);
		}
		fc.setAcceptAllFileFilterUsed(false);
		fc.setAcceptAllFileFilterUsed(true);

		
		File lastDirectory = TricycleModelLocator.getInstance().getLastDirectory();
		if(lastDirectory != null){
			fc.setCurrentDirectory(lastDirectory);
		}
		
		int retValue = fc.showOpenDialog(TricycleModelLocator.getInstance().getMainWindow());
		TricycleModelLocator.getInstance().setLastDirectory(fc.getCurrentDirectory());
		if (retValue == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();
			String formatDesc = fc.getFileFilter().getDescription();
			try{
				format = formatDesc.substring(0, formatDesc.indexOf("(")).trim();
			} catch (Exception e){}
		}
		if (file == null) {
			return;
		}
		
		
		
		String[] possibleReaders = getPossibleReaders(file);
		
		if(possibleReaders.length==0)
		{
			JOptionPane.showMessageDialog(null, 
					"Not a known dendro format",
					"File format",
					JOptionPane.ERROR_MESSAGE);
		}
		else if (possibleReaders.length==1)
		{
			JOptionPane.showMessageDialog(null,
				    "This is a "+possibleReaders[0]+ " file.", 
				    "File format",
				    JOptionPane.INFORMATION_MESSAGE);
			
			// Add this file to the list and set the file type
			FileListModel model = TricycleModelLocator.getInstance().getFileListModel();
			HashSet<String> s = new HashSet<String>();
			s.add(file.getAbsolutePath());
			model.addInputFiles(s);
			model.setInputFormat(possibleReaders[0]);
		}
		else if (possibleReaders.length>1)
		{
			String msg = "This file could be one of several formats:\n";
			
			for(String reader : possibleReaders)
			{
				msg+= "  - " + reader + "\n";
			}
			
			msg+= "Please check the TRiCYCLE documentation for more info.";
			
			JOptionPane.showMessageDialog(null,
				    msg, 
				    "File format",
				    JOptionPane.WARNING_MESSAGE);
		}
		

	}
	
	private String[] getPossibleReaders(File file)
	{
		ArrayList<String> possibleReaders = new ArrayList<String>();
		
		String[] supportedFormats = TridasIO.getSupportedReadingFormats();
		
		for(String format : supportedFormats)
		{
			AbstractDendroFileReader reader = TridasIO.getFileReader(format);
			
			try {
				reader.loadFile(file.getAbsolutePath());
				TridasProject[] projects = reader.getProjects();
				if(projects.length >0 )
				{
					possibleReaders.add(format);
				}
				
			} catch (Exception e) {
				continue;
			} 
			
		}
		
		// Certain readers are guaranteed to be right if present
		// so if they are override imposters
		TridasReader tridasReader = new TridasReader();
		ExcelMatrixReader excelReader =new ExcelMatrixReader();
		Past4Reader pastReader = new Past4Reader();
		if(possibleReaders.contains(tridasReader.getShortName()))
		{
			return  new String[]{tridasReader.getShortName()};
		}
		else if(possibleReaders.contains(excelReader.getShortName()))
		{
			return  new String[]{excelReader.getShortName()};
		}
		else if(possibleReaders.contains(pastReader.getShortName()))
		{
			return  new String[]{pastReader.getShortName()};
		}
		
		
		return possibleReaders.toArray(new String[0]);
	}
	
}
