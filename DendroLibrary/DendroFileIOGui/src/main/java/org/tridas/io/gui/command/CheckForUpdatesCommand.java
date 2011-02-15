package org.tridas.io.gui.command;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JOptionPane;

import org.tridas.io.gui.App;
import org.tridas.io.gui.I18n;
import org.tridas.io.gui.control.CheckForUpdateEvent;
import org.tridas.io.gui.model.TricycleModelLocator;

import com.dmurph.mvc.IllegalThreadException;
import com.dmurph.mvc.IncorrectThreadException;
import com.dmurph.mvc.MVC;
import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

/**
 * Simple class to compare internal build number against that which is available
 * from the tridas.org server
 * 
 * @author pwb48
 *
 */
public class CheckForUpdatesCommand implements ICommand {

	private static String urlstring = "http://www.tridas.org/update-check/.available-tricycle-build";
	private static String updateSite = "http://www.tridas.org/tricycle";
	private boolean showConfirmation = true;
	
	public void execute(MVCEvent ev) {
		
		CheckForUpdateEvent event = (CheckForUpdateEvent) ev;
		showConfirmation = event.showConfirmation;
		
		try {
			MVC.splitOff();
		} catch (IllegalThreadException e1) {
			e1.printStackTrace();
		} catch (IncorrectThreadException e1) {
			e1.printStackTrace();
		}

		// Check server for current available version
		String availableVersion = getAvailableVersion();
		if(availableVersion==null)
		{
			if(showConfirmation)
			{
				JOptionPane.showMessageDialog(null, I18n.getText("view.popup.updateServerIOE"));
			}
			return;
		}
		
		// Parse string version numbers into Integers
		Integer available = 0;
		Integer current = 0;
		try{
			available = Integer.parseInt(availableVersion);
			current = Integer.parseInt(App.getBuildRevision());
		} catch (NumberFormatException e)
		{
			if(showConfirmation)
			{	
				JOptionPane.showMessageDialog(null, I18n.getText("view.popup.updateVersionParseError"));
			}
			return;
		}

		// Compare available and current build numbers
		if(available.compareTo(current)>0)
		{
			// Update required, try to open browser
			if(Desktop.isDesktopSupported())	
			{
			
				Object[] options = {"Yes",
				                    "Maybe later",
									"No, don't ask again"};
				
				int n = JOptionPane.showOptionDialog(null, 
						I18n.getText("view.popup.updateVersionAvailable"),
						"Update available",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null,
						options,
						options[0]);

				if(n==JOptionPane.CANCEL_OPTION){
					TricycleModelLocator.getInstance().getTricycleModel().setAutoUpdate(false);
					return;
				}
				if(n==JOptionPane.NO_OPTION) return;
				
				Desktop desktop = Desktop.getDesktop();
				
				try {
					URI updateURL = new URI(updateSite);
					desktop.browse(updateURL);
					return;
				} catch (URISyntaxException e) {
				} catch (IOException e) {
				}
				
				
				// Problems opening browser so get user to download manually
				JOptionPane.showMessageDialog(null, I18n.getText("view.popup.updatePleaseDownload") +
						"\n"+updateSite);
			}
		}
		else
		{
			// Update not required
			if(showConfirmation)
			{
				JOptionPane.showMessageDialog(null, I18n.getText("view.popup.upToDate"));
			}
			return;
		}
	}
	
	/**
	 * Query the tridas.org server for the latest available build number.
	 * Returns null on IO error.
	 * 
	 * @return
	 */
	private String getAvailableVersion()
	{
		URL url;
	
		try {
			url = new URL(urlstring);
		} catch (MalformedURLException e) {
			return null;
		}
		
		try {
	        URLConnection conn = url.openConnection();
	        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        String inputLine;

	        while ((inputLine = in.readLine()) != null) 
	        	return inputLine;
	        in.close();
			 
		} catch (IOException e) {
			return null;
		}
				
		return null;	
	}
}
