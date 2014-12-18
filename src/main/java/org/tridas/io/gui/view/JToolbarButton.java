package org.tridas.io.gui.view;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class JToolbarButton extends JButton {

	private static final long serialVersionUID = 1L;

	public JToolbarButton(String actioncommand, ImageIcon icon)
	{
		super();
		
		setIcon(icon);
		setActionCommand(actioncommand);
		setFocusable(false);
		setRolloverEnabled(false);
		
	}
	
	public JToolbarButton(TricycleAction action)
	{
		super();
		this.setAction(action);	
		this.setText("");
		this.setToolTipText(action.getShortName());
		setFocusable(false);
		setRolloverEnabled(false);
	}
}
