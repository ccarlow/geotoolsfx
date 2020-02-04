package geotoolsfx;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.Style;
import geotoolsfx.listener.ConfigMapContentListener;
import geotoolsfx.listener.MapContentsListener;

public class MapContents implements ConfigMapContentListener {
  private List<MapContent> mapContents = new ArrayList<MapContent>();
  private Styles styles;
  private FeatureCollections featureCollections;
  private List<MapContentsListener> mapContentsListeners = new ArrayList<MapContentsListener>();
  private Set<MapContent> selected = new HashSet<MapContent>();

  public static final String CONFIG_USER_DATA = "mapContentConfig";
  
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

  public void addListener(MapContentsListener mapContentsListener) {
    mapContentsListeners.add(mapContentsListener);
  }

  public void setSelected(MapContent mapContent, boolean select) {
    if (select) {
      selected.add(mapContent);
    } else {
      selected.remove(mapContent);
    }
    notifyMapContentSelected(mapContent);
  }

  public boolean isSelected(MapContent mapContent) {
    return selected.contains(mapContent);
  }

  protected void notifyMapContentSelected(MapContent mapContent) {
    for (MapContentsListener listener : mapContentsListeners) {
      listener.mapContentChanged(mapContent);
    }
  }

  @Override
  public void configMapContentAdded(Config.MapContent configMapContent) {
    MapContent mapContent = new MapContent();
    mapContent.setTitle(configMapContent.getTitle());
    mapContents.add(mapContent);
    mapContent.getUserData().put(CONFIG_USER_DATA, configMapContent);

    for (Config.Layer configLayer : configMapContent.layers) {
      Style style = styles.getByConfigLayer(configLayer);
      FeatureCollectionWrapper featureCollection =
          featureCollections.getByTitle(configLayer.featureCollection);
      Layer layer = new FeatureLayer(featureCollection.getFeatures(), style);
      layer.setTitle(configLayer.getTitle());
      mapContent.addLayer(layer);
    }

    for (MapContentsListener mapContentsListener : mapContentsListeners) {
      mapContentsListener.mapContentAdded(mapContent);
    }
  }
}
