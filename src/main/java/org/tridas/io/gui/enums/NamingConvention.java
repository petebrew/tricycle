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
 * Created on May 25, 2010, 3:52:33 PM
 */
package org.tridas.io.gui.enums;

/**
 * @author Daniel
 */
public class NamingConvention {
	public static final String[] getNamingConventions() {
		return new String[]{
				org.tridas.io.I18n.getText("general.default"),
				org.tridas.io.I18n.getText("namingconvention.numerical"),
				org.tridas.io.I18n.getText("namingconvention.uuid"),
				org.tridas.io.I18n.getText("namingconvention.hierarchical"),
				org.tridas.io.I18n.getText("namingconvention.seriescode"),
				org.tridas.io.I18n.getText("namingconvention.seriescode8char"),
				org.tridas.io.I18n.getText("namingconvention.keycode")};
	}
}
