/**
 * Created on May 25, 2010, 3:48:35 PM
 */
package org.tridas.io.gui.enums;

import org.tridas.io.TridasIO;

/**
 * @author Daniel
 *
 */
public class OutputFormat {
	public static String[] getOutputFormats(){
		return TridasIO.getSupportedWritingFormats();
	}
}
