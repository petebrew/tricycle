/**
 * Copyright 2010 Daniel Murphy
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
