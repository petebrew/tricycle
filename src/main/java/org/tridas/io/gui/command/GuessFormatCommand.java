package org.tridas.io.gui.command;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.tridas.io.AbstractDendroFileReader;
import org.tridas.io.TridasIO;
import org.tridas.io.formats.excelmatrix.ExcelMatrixReader;
import org.tridas.io.formats.past4.Past4Reader;
import org.tridas.io.formats.tridas.TridasReader;
import org.tridas.io.gui.I18n;
import org.tridas.io.gui.model.FileListModel;
import org.tridas.io.gui.model.TricycleModelLocator;
import org.tridas.io.gui.model.popup.GuessFormatDialogModel;
import org.tridas.io.gui.view.popup.GuessFormatProgress;
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

		// custom JFileChooser
		File file = null;
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled(false);

		File lastDirectory = TricycleModelLocator.getInstance().getLastDirectory();
		if(lastDirectory != null){
			fc.setCurrentDirectory(lastDirectory);
		}
		
		int retValue = fc.showOpenDialog(TricycleModelLocator.getInstance().getMainWindow());
		TricycleModelLocator.getInstance().setLastDirectory(fc.getCurrentDirectory());
		if (retValue == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();
		}
		if (file == null) {
			return;
		}
		
		// Check which readers can open the file
		String[] possibleReaders = getPossibleReaders(file);
		if(possibleReaders==null)
		{
			return;
		}
		else if(possibleReaders.length==0)
		{
			// No matches
			JOptionPane.showMessageDialog(TricycleModelLocator.getInstance().getMainWindow(), 
					I18n.getText("view.popup.unknownFormat"),
					I18n.getText("view.popup.fileFormat"),
					JOptionPane.ERROR_MESSAGE);
		}
		else if (possibleReaders.length==1)
		{
			// One match
			JOptionPane.showMessageDialog(TricycleModelLocator.getInstance().getMainWindow(),
					I18n.getText("view.popup.detectedFormat",possibleReaders), 
					I18n.getText("view.popup.fileFormat"),
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
			// Multiple matches
			String msg = I18n.getText("view.popup.ambiguousFormat")+":\n";
			
			for(String reader : possibleReaders)
			{
				msg+= "  - " + reader + "\n";
			}
			
			msg+= I18n.getText("view.popup.checkDocs");
			
			JOptionPane.showMessageDialog(TricycleModelLocator.getInstance().getMainWindow(),
				    msg, 
				    I18n.getText("view.popup.fileFormat"),
				    JOptionPane.WARNING_MESSAGE);
		}
	}
	
	/**
	 * Attempts to read a file with all the available readers.  Returns an
	 * array of reader names that opened the file without exceptions.
	 * 
	 * @param file
	 * @return
	 */
	private String[] getPossibleReaders(File file)
	{
		
		
		GuessFormatDialogModel model = new GuessFormatDialogModel();
		final GuessFormatProgress dialog = new GuessFormatProgress(TricycleModelLocator.getInstance()
				.getMainWindow(), model);
		
		
		model.setProgressPercent(0);
		
		model.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				String prop = evt.getPropertyName();
				
				if (prop.equals("cancelled")) {
					
					dialog.setVisible(false);
				}
			}
		});
		

			
		// i have to do this in a different thread
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				dialog.setVisible(true);
			}
		});
		
		while(!dialog.isVisible()){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		ArrayList<String> possibleReaders = new ArrayList<String>();
		
		String[] supportedFormats = TridasIO.getSupportedReadingFormats();
		
		int i = 1;
		for(String format : supportedFormats)
		{
			if(model.isCancelled()) return null;
			
			i++;
			Integer barval = i * 100 / supportedFormats.length;
			model.setProgressPercent(barval);
			
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
		
		dialog.dispose();
		
		// Certain readers are guaranteed to be right if present
		// so if they are, we need to override imposters!
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
