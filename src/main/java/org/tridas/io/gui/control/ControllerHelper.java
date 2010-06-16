/**
 * Created at Jun 15, 2010, 9:52:02 PM
 */
package org.tridas.io.gui.control;

import org.tridas.io.AbstractDendroFileReader;
import org.tridas.io.TridasIO;

/**
 * @author daniel
 *
 */
public class ControllerHelper {

	public static AbstractDendroFileReader getReader(String argInputFormat){
		if(argInputFormat == null){
			return null;
		}
		
		return TridasIO.getFileReader(argInputFormat);
	}
}
