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
