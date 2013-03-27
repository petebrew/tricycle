package org.tridas.io.gui.components;

import java.awt.Component;
import java.util.Locale;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tridas.io.util.IOUtils;


public class LocaleComboRenderer extends JLabel implements ListCellRenderer {

	private static final long serialVersionUID = 1L;
	private final static Logger log = LoggerFactory.getLogger(LocaleComboRenderer.class);
	
	public LocaleComboRenderer()
	{
	    setOpaque(true);
	    setHorizontalAlignment(LEFT);
	    setVerticalAlignment(CENTER);
	}
	
	@Override
	public Component getListCellRendererComponent(JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

		TricycleLocale loc;
		
		if(value instanceof TricycleLocale)
		{
			
			loc = (TricycleLocale) value;
		}
		else
		{
			log.error("Combo item is not a TellervoLocale");
			return null;
		}
		
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        ImageIcon icon = (ImageIcon) loc.getFlag();
        setIcon(icon);
        setText(loc.getName());
        
		return this;
	}

	public enum TricycleLocale{
		DEFAULT		  ("Default",      "xx", "unknown" ),		
		DANISH		  ("Dansk", 	   "da", "DK" ),
		GERMAN        ("Deutsch",      "de", "DE" ), 
		ENGLISH_PROPER("English (British)", "en", "GB" ),
		ENGLISH_US    ("English (American)", "en", "US" ),
		SPANISH		  ("Español", 	   "es", "ES" ),
		GREEK		  ("ελληνικά", 	   "el", "GR" ),
		FRENCH        ("Français",     "fr", "FR" ),
		DUTCH         ("Nederlands",   "nl", "NL" ),
		POLISH        ("Polski",       "pl", "PL" ),
		TURKISH       ("Türk",         "tr", "TR" );

		private String country;
		private String language;
		private String name;

		
		private TricycleLocale(String name, String language, String country)
		{
			this.name = name;
			this.country  = country;
			this.language = language;
		}
		
		public String getName()
		{
			TricycleLocale def = TricycleLocale.DEFAULT;
			if(this.equals(def))
			{
				Locale loc = Locale.getDefault();
				
				for(TricycleLocale l:TricycleLocale.values())
				{
					if(l.equals(TricycleLocale.DEFAULT)) continue;
					if(l.getCountryCode().equals(loc.getCountry()) && l.getLanguageCode().equals(loc.getLanguage()))
					{
						return "System default - "+l.name;
					}
				}
				

				return "System default - "+TricycleLocale.ENGLISH_PROPER.name;
				
			}
			
			
			return name;
		}
		
		public String toString()
		{
			return getName();
		}
		
		public String getCountryCode()
		{
			TricycleLocale def = TricycleLocale.DEFAULT;
			if(this.equals(def))
			{
				Locale locale = Locale.getDefault();
				return locale.getCountry();
			}
			
			return country;
		
		}
		
		public String getLanguageCode()
		{
			TricycleLocale def = TricycleLocale.DEFAULT;
			if(this.equals(def))
			{
				Locale locale = Locale.getDefault();
				return locale.getLanguage();
			}
			
			return language;
		}
		
		public Locale getLocale()
		{
			Locale locale = new Locale(language, country);
			return locale;	
		}
		
		public Icon getFlag()
		{
			TricycleLocale def = TricycleLocale.DEFAULT;
			if(this.equals(def))
			{
				Locale loc = Locale.getDefault();
				
				for(TricycleLocale l:TricycleLocale.values())
				{
					if(l.equals(TricycleLocale.DEFAULT)) continue;
					if(l.getCountryCode().equals(loc.getCountry()) && l.getLanguageCode().equals(loc.getLanguage()))
					{
						return getIcon(l.country, 16);
					}
				}

				return getIcon(TricycleLocale.ENGLISH_PROPER.country, 16);				
			}
			
			
			return getIcon(country, 16);
		}
		
		public static Icon getIcon(String name, int size) {
			
			try{
				String filename = "icons/"+size+"x"+size+"/"+name+".png";
				ImageIcon aicon = new ImageIcon(IOUtils.getFileInJarURL(filename));
				return aicon;
			} catch (Exception e)
			{
				
			}
			
			return null;
		}	
		
	};


}
