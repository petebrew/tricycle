/**
 * Created on May 27, 2010, 1:54:37 AM
 */
package org.tridas.io.gui.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.tridas.io.gui.mvc.model.AbstractModel;
import org.tridas.io.gui.mvc.model.CloneableArrayList;

/**
 * @author Daniel
 *
 */
public class ConvertModel extends AbstractModel{
	
	private static final ConvertModel model = new ConvertModel();
	
	private String outputFormat = "tridas";
	private String namingConvention = "UUID";
	private CloneableArrayList<DefaultMutableTreeNode> nodes = new CloneableArrayList<DefaultMutableTreeNode>();
	
	private ConvertModel(){}
	
	public void setNamingConvention(String argNamingConvention){
		String old = namingConvention;
		namingConvention = argNamingConvention;
		firePropertyChange("namingConvention", old, namingConvention);
	}
	
	public String getNamingConvention(){
		return namingConvention;
	}
	
	public void setNodes(List<DefaultMutableTreeNode> argNodes){
		CloneableArrayList<DefaultMutableTreeNode> old = (CloneableArrayList<DefaultMutableTreeNode>) nodes.clone();
		nodes.clear();
		nodes.addAll(argNodes);
		firePropertyChange("nodes", old, nodes.clone());
	}
	
	public List<DefaultMutableTreeNode> getNodes(){
		return (List<DefaultMutableTreeNode>) nodes.clone();
	}
	
	/**
	 * @param outputFormat
	 *            the outputFormat to set
	 */
	public void setOutputFormat(String argOutputFormat) {
		String old = outputFormat;
		outputFormat = argOutputFormat;
		firePropertyChange("outputFormat", old, outputFormat);
	}

	/**
	 * @return the outputFormat
	 */
	public String getOutputFormat() {
		return outputFormat;
	}
	
	public static final ConvertModel getInstance(){
		return model;
	}
}
