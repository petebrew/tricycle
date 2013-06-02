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
package org.tridas.io.gui;

import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tridas.io.gui.model.TricycleModelLocator;

/**
 * Simple localization
 * @author Daniel
 *
 */
public class I18n {
	private final static Logger log = LoggerFactory.getLogger(I18n.class);

	public static Preferences localeprefs = Preferences.userNodeForPackage(App.class);

	// the resource bundle to use
	private final static ResourceBundle msg;
	
	static {
		ResourceBundle bundle;
		try {
			
			// Grab overriding language prefs if available
			String country = localeprefs.get("countrycode", "xxx"); 
			String language =localeprefs.get("languagecode", "xxx");
			
			
			if(country.equals("xxx") || language.equals("xxx"))
			{
				// No prefs specified so just go with the default from the system
				log.debug("No locale preferences specified.  Attempting to get system locale instead...");
				bundle = ResourceBundle.getBundle("locale/DendroFileIOGUI");
				log.debug("Using system locale: "+Locale.getDefault().getDisplayCountry()+" with lang "+Locale.getDefault().getDisplayLanguage());
				
			}
			else
			{
				// Prefs specified so use these instead
				log.debug("Locale preferences specified (lang: "+language+", country: "+country+") which override the system default.  Attempting to use this...");
				Locale specloc = new Locale(language, country);
				bundle = ResourceBundle.getBundle("locale/DendroFileIOGUI", specloc);
				log.debug("Successfully loaded locale: "+specloc.getDisplayCountry(specloc)+" with lang "+specloc.getDisplayLanguage(specloc));
			}
			
		} catch (MissingResourceException mre) {
			try {
				log.debug("Failed to find specified locale.  Falling back to TRiCYCLE default locale");
				
				bundle = ResourceBundle.getBundle("locale/DendroFileIOGUI");
			} catch (MissingResourceException mre2) {
				log.error("Couldn't even find TRiCYCLE locale, this really shouldn't have happened!");
				mre2.printStackTrace();
				bundle = new ResourceBundle() {
					
					@Override
					protected Object handleGetObject(String key) {
						return key;
					}
					
					@Override
					public Enumeration<String> getKeys() {
						return EMPTY_ENUMERATION;
					}
					
					private final Enumeration<String> EMPTY_ENUMERATION = new Enumeration<String>() {
						public boolean hasMoreElements() {
							return false;
						}
						
						public String nextElement() {
							throw new NoSuchElementException();
						}
					};
				};
			}
		}
		msg = bundle;
	}
	
	private I18n() {
	// don't instantiate me
	}
	
	/**
	 * Get the text for this key. The text has no special control characters in
	 * it, and can be presented to the user.
	 * <p>
	 * For example, if the localization file has the line
	 * <code>copy = &amp;Copy [accel C]</code>, the string "Copy" is returned.
	 * </p>
	 * 
	 * @param key
	 *            the key to look up in the localization file
	 * @return the text
	 */
	public static String getText(String key) {
		String value = null;
		
		try {
			value = msg.getString(key);
		} catch (MissingResourceException e) {
			System.err.println("Unable to find the translation for the key: " + key);
			return key;
		};
		
		StringBuffer buf = new StringBuffer();
		
		int n = value.length();
		boolean ignore = false;
		for (int i = 0; i < n; i++) {
			char c = value.charAt(i);
			switch (c) {
				case '&' :
					continue;
				case '[' :
					ignore = true;
					break;
				case ']' :
					ignore = false;
					break;
				default :
					if (!ignore) {
						buf.append(c);
					}
			}
		}
		
		return buf.toString().trim();
	}
	

	/**
	 * Look up a translation key with each {n} replaced
	 * with a value in the array
	 * 
	 * @param argKey
	 * @param argReplacing
	 * @return
	 */
	public static String getText(String argKey, String... argReplacing) {
		String text = getText(argKey);
		
		for (int i = 0; i < argReplacing.length; i++) {
			text = text.replace("{" + i + "}", argReplacing[i]);
		}
		
		return text;
	}
	
	/**
	 * Gets and integer value of the key.  If the 
	 * value isn't able to parse as an integer,
	 * null is returned
	 * @param argKey
	 * @return
	 */
	public static Integer getInteger(String argKey){
		String text = getText(argKey);
		try{
			int i = Integer.parseInt(text);
			return i;
		}catch( NumberFormatException e){
			return null;
		}
	}
	
	/**
	 * Gets the boolean value of the key.  If the 
	 * value is anything but "true", then false
	 * is returned
	 * @param argKey
	 * @return
	 */
	public static Boolean getBoolean(String argKey){
		String text = getText(argKey);
		try{
			boolean tf = Boolean.parseBoolean(text);
			return tf;
		}catch( NumberFormatException e){
			return null;
		}
	}
}
