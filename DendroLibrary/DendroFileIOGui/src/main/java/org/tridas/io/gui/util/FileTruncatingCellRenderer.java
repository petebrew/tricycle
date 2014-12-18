package org.tridas.io.gui.util;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import org.tridas.io.util.FileUtils;

public class FileTruncatingCellRenderer extends DefaultListCellRenderer {


	private static final long serialVersionUID = 1L;

	@Override
	public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);		
		label.setText(FileUtils.pathLengthShortener(label.getText(), list.getWidth()/7));
		return label;
	}

}
