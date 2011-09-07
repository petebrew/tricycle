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
 * Created on May 27, 2010, 1:54:37 AM
 */
package org.tridas.io.gui.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tridas.io.AbstractDendroCollectionWriter;
import org.tridas.io.AbstractDendroFileReader;
import org.tridas.io.defaults.IMetadataFieldSet;

import org.tridas.io.gui.I18n;
import com.dmurph.mvc.model.AbstractModel;
import com.dmurph.mvc.model.MVCArrayList;

/**
 * @author Daniel
 */
public class ConvertModel extends AbstractModel {

	private static final Logger log = LoggerFactory.getLogger(ConvertModel.class);

	private static final long serialVersionUID = 1L;
	
	private String outputFormat = "TRiDaS";
	private TreatFilesAsOption treatFilesAs = TreatFilesAsOption.SEPARATE;
	private MVCArrayList<DefaultMutableTreeNode> nodes = new MVCArrayList<DefaultMutableTreeNode>();
	
	private final MVCArrayList<ConvertModel.ReaderWriterObject> structList = new MVCArrayList<ConvertModel.ReaderWriterObject>();
	
	private int processed = 0;
	private int failed = 0;
	private int convWithWarnings = 0;
	
	private volatile boolean saveRunning = false;
	private volatile boolean convertRunning = false;
	
	private DefaultMutableTreeNode selectedNode = null;
	
	public ConvertModel() {}
			
	public enum TreatFilesAsOption {
		SEPARATE(I18n.getText("view.files.treatas.separate")), 
		ONE_PROJECT(I18n.getText("view.files.treatas.oneproject")),
		ONE_OBJECT(I18n.getText("view.files.treatas.oneobject"));
		
		private String str;
		
		TreatFilesAsOption(String c) {
			str = c;
		}
		
		@Override
		public final String toString() {
			return str;
		}
		
		public static TreatFilesAsOption fromStr(String strng) {
			for (TreatFilesAsOption val : TreatFilesAsOption.values()) {				
				if (val.toString().equalsIgnoreCase(strng)) {
					return val;
				}
			}
			return null;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public void setNodes(List<DefaultMutableTreeNode> argNodes) {
		MVCArrayList<DefaultMutableTreeNode> old = (MVCArrayList<DefaultMutableTreeNode>) nodes.clone();
		nodes.clear();
		for(DefaultMutableTreeNode tn : argNodes){
			nodes.add(tn); // djm FIXME change back to addAll once MVC is updated
		}
		firePropertyChange("nodes", old, nodes.clone());
	}
	
	@SuppressWarnings("unchecked")
	public List<DefaultMutableTreeNode> getNodes() {
		return (List<DefaultMutableTreeNode>) nodes.clone();
	}

	public void setSelectedNode(DefaultMutableTreeNode argSelectedNode) {
		DefaultMutableTreeNode old = selectedNode;
		selectedNode = argSelectedNode;
		firePropertyChange("selectedNode", old, selectedNode);
	}

	public DefaultMutableTreeNode getSelectedNode() {
		return selectedNode;
	}

	// don't bother with property change
	public void setSaveRunning(boolean saveRunning) {
		this.saveRunning = saveRunning;
	}

	public boolean isSaveRunning() {
		return saveRunning;
	}

	public void setConvertRunning(boolean convertRunning) {
		this.convertRunning = convertRunning;
	}

	public boolean isConvertRunning() {
		return convertRunning;
	}

	public ArrayList<ConvertModel.ReaderWriterObject> getConvertedList() {
		return structList;
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
	

	public void setTreatFilesAs(TreatFilesAsOption tfao) {
		TreatFilesAsOption old = treatFilesAs;
		treatFilesAs = tfao;
		firePropertyChange("treatFilesAs", old, treatFilesAs);
	}
	
	public TreatFilesAsOption getTreatFilesAs()
	{
		return treatFilesAs;
	}
	
	/**
	 * @return the outputFormat
	 */
	public String getOutputFormat() {
		return outputFormat;
	}
	
	public void setProcessed(int argProcessed) {
		int old = processed;
		processed = argProcessed;
		firePropertyChange("processed", old, processed);
	}
	
	public int getProcessed() {
		return processed;
	}
	
	public void setFailed(int argFailed) {
		int old = failed;
		failed = argFailed;
		firePropertyChange("failed", old, failed);
	}
	
	public int getFailed() {
		return failed;
	}
	
	public void setConvWithWarnings(int argConvWithWarnings) {
		int old = convWithWarnings;
		convWithWarnings = argConvWithWarnings;
		firePropertyChange("convWithWarnings", old, convWithWarnings);
	}
	
	public int getConvWithWarnings() {
		return convWithWarnings;
	}
	
	public static class ReaderWriterObject {
		public String file;
		public String errorMessage = null;
		public AbstractDendroFileReader reader = null;
		public IMetadataFieldSet readerDefaults = null;
		public AbstractDendroCollectionWriter writer = null;
		public IMetadataFieldSet writerDefaults = null;
		public boolean warnings = false;
	}
}
