/**
 * Created on May 25, 2010, 3:45:53 PM
 */
package org.tridas.io.gui.enums;

import java.util.ArrayList;

import org.tridas.io.TridasIO;

/**
 * @author Daniel
 */
public class InputFormat {
	public static final String AUTO = "Automatic";
	
	public static String[] getInputFormats() {
		ArrayList<String> list = new ArrayList<String>();
		//list.add(AUTO); TODO djm add when library supports
		for (String s : TridasIO.getSupportedReadingFormats()) {
			list.add(s);
		}
		return list.toArray(new String[0]);
	}
}
