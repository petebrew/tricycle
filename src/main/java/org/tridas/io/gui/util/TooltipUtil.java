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
 * Created on Jul 9, 2010, 1:11:20 AM
 */
package org.tridas.io.gui.util;

/**
 * @author Daniel Murphy
 *
 */
public class TooltipUtil {
	
	/**
	 * Loosely enforces the wrap length, as it waits until the last word
	 * finishes before wrapping.
	 * @param argTooltip
	 * @param argCharacters
	 * @return
	 */
	public static String wrapTooltip(String argTooltip, int argCharacters){
		char[] chars = argTooltip.toCharArray();
		if(chars.length <= argCharacters){
			return argTooltip;
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		int numBreaks = 0;
		for(int i=0; i<chars.length; i++){
			if(i < argCharacters + numBreaks*argCharacters){
				sb.append(chars[i]);
			}else{
				if(Character.isWhitespace(chars[i])){
					sb.append("<br/>");
					numBreaks++;
				}else{
					sb.append(chars[i]);
				}
			}
		}
		sb.append("</html>");
		return sb.toString();
	}
}
