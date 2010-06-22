/**
 * Created on Jun 7, 2010, 5:00:54 AM
 */
package org.tridas.io.gui.enums;

import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * @author Daniel Murphy
 */
public class Charsets {
	public static final String AUTO = "Automatic";
	public static final String DEFAULT = "System Default"; // djm TODO locale
	
	public static final String[] getReadingCharsets() {
		ArrayList<String> charsets = new ArrayList<String>();
		charsets.add(AUTO);
		for (String cs : Charset.availableCharsets().keySet()) {
			charsets.add(cs);
		}
		return charsets.toArray(new String[0]);
	}
	
	public static final String[] getWritingCharsets() {
		ArrayList<String> charsets = new ArrayList<String>();
		for (String key : Charset.availableCharsets().keySet()) {
			Charset cs = Charset.availableCharsets().get(key);
			if (cs.canEncode()) {
				charsets.add(key);
			}
		}
		return charsets.toArray(new String[0]);
	}
}
