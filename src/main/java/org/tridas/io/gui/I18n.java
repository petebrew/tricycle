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
import java.util.MissingResourceException;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;

import javax.swing.KeyStroke;

/**
 * Provide localization strings.
 * <p>
 * Java's ResourceBundles are intended to do this, but they don't provide the level of
 * support that most other libraries do.
 * </p>
 * <p>
 * For example, suppose you have a "Copy" menuitem.
 * 
 * <pre>
 * JMenuItem copy = new JMenuItem(&quot;Copy&quot;);
 * </pre>
 * 
 * Of course, you should internationalize this. But you want to change the text, the
 * mnemonic, and the keyboard accelerator. If you only had ResourceBundle to work with,
 * you might end up with 3 lines of translation for every word in your program. Plus,
 * you'd have to know the name of the I18n file you used, each time you wanted to use it.
 * Most other libraries (like Powerplant on Mac and Win95 resources) let you put them all
 * in one line, which makes the translator's job much easier. That's what this class does.
 * </p>
 * <p>
 * Now, all you have to say is:
 * 
 * <pre>
 * copy = &amp;Copy [accel C]
 * </pre>
 * <p>
 * There are several things going on here:
 * </p>
 * <ul>
 * <li>"copy" is the <i>key</i>. When you want to refer to this entry, you'll ask for it
 * by this name. The user never sees this.
 * <li>"Copy" (without the &amp;) is the <i>text</i> of this key. This is what users will
 * see in menuitems, buttons, and so forth.
 * <li>"C" (the thing right after the &amp;) is the <i>mnemonic</i>. On most platforms
 * (all except Mac), this letter is underlined, and users can jump to it by pressing this
 * letter, or Alt and this letter (depending on platform and context).
 * <li>"accel C" is the <i>keystroke</i>. The user can press this at any time to invoke
 * this command. You can use the modifiers "shift", "control", "alt", and "meta", or the
 * special modifier "accel". "accel" is automatically turned into "control" on PCs
 * (Windows and Linux), and "meta" (Java's term for "command") on Macs. You should
 * normally use the generic "accel" modifier. You can also combine modifiers, like
 * "shift accel S" (the standard keystroke for "Save As..."). The class
 * javax.swing.KeyStroke takes these strings as input: you can say
 * 
 * <pre>
 * KeyStroke.getKeyStroke(I18n.getKeyStroke(&quot;copy&quot;))
 * </pre>
 * 
 * to get a Swing KeyStroke object.
 * </ul>
 * <p>
 * This is convenient, but it gets even better: normally, you don't even have to mess with
 * keystrokes and mnemonics. You can simply use the Builder factory to do the dirty work
 * for you:
 * 
 * <pre>
 * JMenuItem copy = Builder.makeMenuItem(&quot;copy&quot;);
 * </pre>
 * 
 * Of course, if you just want the text (for making a printout, for example), you should
 * still use I18n.getText().
 * </p>
 * <p>
 * Not all keys must have a keystroke, or a mnemonic. Note that those methods can return
 * nulls.
 * </p>
 * <p>
 * These methods get their values from the resource bundle called "TextBundle". That is,
 * the file is called "TextBundle.properties", or some variant, like
 * "TextBundle_de_DE.properties".
 * </p>
 * <h2>Left to do</h2>
 * <ul>
 * <li>The way getMnemonic() is set up, "Save &as..." will still show it as "S&ave as...".
 * Bad, but not fatal.
 * <li>Add way to escape [/]/&amp; in text; also, add markers so
 * "this text is mnemonic/text/keystroke" and "this text is just a string" are separate(?)
 * <li>in getKeyStroke(), why not use Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()
 * instead of (?:) for command/control choice?
 * </ul>
 * 
 * @see java.util.ResourceBundle
 * @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i
 *         style="color: gray">dot</i> 
 *         edu&gt;
 * @author Daniel Murphy
 * @version $Id: I18n.java 2285 2010-06-08 16:35:25Z dmurphy $
 */
public class I18n {
	
	
	// the resource bundle to use
	private final static ResourceBundle msg;
	
	static {
		ResourceBundle bundle;
		try {
			bundle = ResourceBundle.getBundle("locale/DendroFileIOGUI");
		} catch (MissingResourceException mre) {
			try {
				bundle = ResourceBundle.getBundle("DendroFileIOGUI");
			} catch (MissingResourceException mre2) {
				System.out.println("Could not find locale file.");
				mre2.printStackTrace();
				bundle = new ResourceBundle() {
					
					@Override
					protected Object handleGetObject(String key) {
						return key;
					}
					
					@SuppressWarnings("unchecked")
					@Override
					public Enumeration getKeys() {
						return EMPTY_ENUMERATION;
					}
					
					@SuppressWarnings("unchecked")
					private final Enumeration EMPTY_ENUMERATION = new Enumeration() {
						public boolean hasMoreElements() {
							return false;
						}
						
						public Object nextElement() {
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
	
	/**
	 * Get the keystroke string for this key. This string can be passed directly
	 * to the Keystroke.getKeyStroke() method.
	 * <p>
	 * For example, if the localization file has the line
	 * <code>copy = &amp;Copy [accel C]</code>, the string "control C" is returned (or on
	 * the Mac, "meta C").
	 * </p>
	 * <p>
	 * If the string has no [keystroke] listed, null is returned.
	 * </p>
	 * 
	 * @param key
	 *            the key to look up in the localization file
	 * @return the keystroke
	 */
	public static KeyStroke getKeyStroke(String key) {
		String value = msg.getString(key);
		
		int left = value.indexOf('[');
		int right = value.indexOf(']');
		
		if (left == -1 || right == -1) {
			return null;
		}
		
		String stroke = value.substring(left + 1, right).trim();
		
		// accel = command (in java-ese: "meta") on mac, control on pc
		// String accel = (App.platform.isMac() ? "meta" : "control");
		// stroke = StringUtils.substitute(stroke, "accel", accel);
		
		return KeyStroke.getKeyStroke(stroke);
	}
	
	/**
	 * Get the mnemonic character this key.
	 * <p>
	 * For example, if the localization file has the line
	 * <code>copy = &amp;Copy [accel C]</code>, the character "C" is returned.
	 * </p>
	 * <p>
	 * If the string has no &amp;mnemonic listed, null is returned.
	 * </p>
	 * 
	 * @param key
	 *            the key to look up in the localization file
	 * @return the integer representing the mnemonic character
	 */
	public static Integer getMnemonic(String key) {
		String value = msg.getString(key);
		
		int amp = value.indexOf('&');
		
		if (amp == -1 || amp == value.length() - 1) {
			return null;
		}
		
		return new Integer(Character.toUpperCase(value.charAt(amp + 1)));
	}
	
	/**
	 * Get the position of the mnemonic character in the string
	 * Used for setDisplayedMnemonicIndex
	 * 
	 * @param key
	 * @return an Integer, or null
	 */
	public static Integer getMnemonicPosition(String key) {
		String value = msg.getString(key);
		
		int amp = value.indexOf('&');
		
		if (amp == -1 || amp == value.length() - 1) {
			return null;
		}
		
		return amp;
	}
}
