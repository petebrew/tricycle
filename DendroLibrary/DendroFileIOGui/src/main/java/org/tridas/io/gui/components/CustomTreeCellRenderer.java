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
 * Created at Jun 13, 2010, 1:13:41 PM
 */
package org.tridas.io.gui.components;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.tridas.io.gui.I18n;
import org.tridas.io.gui.control.convert.ConvertController.DendroWrapper;
import org.tridas.io.gui.control.convert.ConvertController.StructWrapper;
import org.tridas.io.gui.util.TooltipUtil;

/**
 * @author daniel
 *
 */
@SuppressWarnings("serial")
public class CustomTreeCellRenderer extends DefaultTreeCellRenderer {
	private final Icon successIcon;
	private final Icon warningIcon;
	private final Icon failIcon;
	private final Icon fileSuccessIcon;
	private final Icon fileWarningIcon;
	private final Icon infoIcon;
	
	public CustomTreeCellRenderer(Icon argSuccessIcon, Icon argWarningIcon, Icon argFailIcon, Icon argFileSuccessIcon, Icon argFileWarningIcon, Icon argInfoIcon){
		successIcon = argSuccessIcon;
		warningIcon = argWarningIcon;
		failIcon = argFailIcon;
		fileSuccessIcon = argFileSuccessIcon;
		fileWarningIcon = argFileWarningIcon;
		infoIcon = argInfoIcon;
		
	}
	
	/**
	 * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {
		Component def = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		
		Object userObject = ((DefaultMutableTreeNode)value).getUserObject();
		if(userObject instanceof StructWrapper){
			StructWrapper wrapper = (StructWrapper) userObject;
			
			if(wrapper.struct.errorMessage != null){
				setIcon(failIcon);
			}else if(wrapper.struct.warnings){
				setIcon(warningIcon);
			}else{
				setIcon(successIcon);
			}
		}else if(userObject instanceof DendroWrapper){
			DendroWrapper wrapper = (DendroWrapper) userObject;
			if(wrapper.file.getDefaults().getWarnings().size() != 0){
				setIcon(fileWarningIcon);
			}else{
				setIcon(fileSuccessIcon);
			}
		}else if(userObject.toString().equals(I18n.getText("control.convert.readerWarnings"))){
			setIcon(warningIcon);
		}else if(userObject.toString().equals(I18n.getText("control.convert.writerWarnings"))){
			setIcon(warningIcon);
		}
		else{
			// warning
			setIcon(infoIcon);
			setToolTipText(TooltipUtil.wrapTooltip(userObject.toString(), 60));
			return this;
		}
		setToolTipText(null);
		return this;
	}
}
