/**
 * Created at Nov 22, 2010, 7:37:50 AM
 */
package org.tridas.io.gui.control.config;

import com.dmurph.mvc.ObjectEvent;
import com.dmurph.mvc.StringEvent;
import com.dmurph.mvc.tracking.ITrackable;

/**
 * @author Daniel
 *
 */
public class ConfigEvent extends StringEvent implements ITrackable{
	private static final long serialVersionUID = 1L;

	/**
	 * @param argKey
	 * @param argValue
	 */
	public ConfigEvent(String argKey, String argValue) {
		super(argKey, argValue);
	}

	/**
	 * @see com.dmurph.mvc.tracking.ITrackable#getTrackingCategory()
	 */
	@Override
	public String getTrackingCategory() {
		return "Tricycle";
	}

	/**
	 * @see com.dmurph.mvc.tracking.ITrackable#getTrackingAction()
	 */
	@Override
	public String getTrackingAction() {
		return key;
	}

	/**
	 * @see com.dmurph.mvc.tracking.ITrackable#getTrackingLabel()
	 */
	@Override
	public String getTrackingLabel() {
		return getValue();
	}

	/**
	 * @see com.dmurph.mvc.tracking.ITrackable#getTrackingValue()
	 */
	@Override
	public Integer getTrackingValue() {
		return null;
	}

}
