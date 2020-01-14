package geotoolsfx;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.xml.styling.SLDParser;

import geotoolsfx.listener.StylesListener;

public class Styles {
	private StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
	private Map<String, List<Style>> sldFiles = new HashMap<String, List<Style>>();
	private List<StylesListener> stylesListeners = new ArrayList<StylesListener>();
	
	public void addStylesListener(StylesListener stylesListener) {
		this.stylesListeners.add(stylesListener);
	}
	
	public void removeStylesListener(StylesListener stylesListener) {
		this.stylesListeners.remove(stylesListener);
	}
	
	public Style getByConfigLayer(Config.Layer configLayer) {
		return getStyle(configLayer.style.name, configLayer.style.location);
	}
	
	public Style getStyle(String name, String sldLocation) {
		try {
			URL url = new URL(sldLocation);
			if (!sldFiles.containsKey(url.getFile())) {
				SLDParser sldParser = new SLDParser(styleFactory, url);
				Style[] styles = sldParser.readXML();
				sldFiles.put(url.getFile(), Arrays.asList(styles));
				for (Style style : sldFiles.get(url.getFile())) {
					for (StylesListener styleListener : stylesListeners) {
						styleListener.styleAdded(style);
					}
				}
			}
			
			for (Style style : sldFiles.get(url.getFile())) {
				if (style.getName().equals(name)) {
					return style;
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
