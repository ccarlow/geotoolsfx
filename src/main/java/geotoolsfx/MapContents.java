package geotoolsfx;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.Style;

import geotoolsfx.listener.MapContentsListener;

public class MapContents {
	private List<MapContent> mapContents = new ArrayList<MapContent>();
	private Styles styles;
	private FeatureCollections featureCollections;
	private List<MapContentsListener> mapContentsListeners = new ArrayList<MapContentsListener>();
	private Set<MapContent> selected = new HashSet<MapContent>();
	
	public MapContent getByTitle(String title) {
		for (MapContent mapContent : mapContents) {
			if (mapContent.getTitle().equals(title)) {
				return mapContent;
			}
		}
		return null;
	}
	
	public MapContent getByConfig(Config.MapContent configGeoMap) {
		for (MapContent mapContent : mapContents) {
			if (mapContent.getTitle().equals(configGeoMap.getTitle())) {
				return mapContent;
			}
		}
		return null;
	}
	
	public void setStyles(Styles styles) {
		this.styles = styles;
	}
	
	public void setFeatureCollections(FeatureCollections featureCollections) {
		this.featureCollections = featureCollections;
	}
	
	public void addMapContentsListener(MapContentsListener mapContentsListener) {
		mapContentsListeners.add(mapContentsListener);
	}
	
	public void setSelected(MapContent mapContent, boolean select) {
		if (select) {
			selected.add(mapContent);
		} else {
			selected.remove(mapContent);
		}
		fireMapContentSelected(mapContent);
	}
	
	public boolean isSelected(MapContent mapContent) {
		return selected.contains(mapContent);
	}
	
	protected void fireMapContentSelected(MapContent mapContent) {
		for (MapContentsListener listener : mapContentsListeners) {
			listener.mapContentChanged(mapContent);	
		}
	}
	
	public void addFromConfig(Config config) {
		for (Config.MapContent configMapContent : config.mapContents) {
			MapContent mapContent = new MapContent();
			mapContent.setTitle(configMapContent.getTitle());
			mapContents.add(mapContent);
			
			for (Config.Layer configLayer : configMapContent.layers) {
				Style style = styles.getByConfigLayer(configLayer);
				FeatureCollectionWrapper featureCollection = featureCollections.getByTitle(configLayer.featureCollection);
				Layer layer = new FeatureLayer(featureCollection.getFeatures(), style);
				layer.setTitle(configLayer.getTitle());
				mapContent.addLayer(layer);
			}
			
			for (MapContentsListener mapContentsListener : mapContentsListeners) {
				mapContentsListener.mapContentAdded(mapContent);
			}
		}
	}
}
