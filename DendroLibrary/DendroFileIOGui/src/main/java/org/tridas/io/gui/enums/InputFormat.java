/**
 * Created on May 25, 2010, 3:45:53 PM
 */
package org.tridas.io.gui.enums;

import org.tridas.io.TridasIO;

/**
 * @author Daniel
 */
public class InputFormat {
	public static String[] getInputFormats() {
		return TridasIO.getSupportedReadingFormats();
	}
}
