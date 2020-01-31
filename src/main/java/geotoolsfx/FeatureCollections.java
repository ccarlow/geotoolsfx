package geotoolsfx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureSource;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.Feature;
import geotoolsfx.listener.ConfigListener;
import geotoolsfx.listener.FeatureCollectionsListener;

public class FeatureCollections implements ConfigListener {
  private Map<String, FeatureCollectionWrapper> featureCollections =
      new HashMap<String, FeatureCollectionWrapper>();
  private DataStores dataStores;
  private List<FeatureCollectionsListener> featureCollectionsListeners =
      new ArrayList<FeatureCollectionsListener>();

  public void addListener(FeatureCollectionsListener featureCollectionsListener) {
    this.featureCollectionsListeners.add(featureCollectionsListener);
  }

  public void removeListener(FeatureCollectionsListener featureCollectionsListener) {
    this.featureCollectionsListeners.remove(featureCollectionsListener);
  }

  public void setDataStores(DataStores dataStores) {
    this.dataStores = dataStores;
  }

  public void addFeatureCollectionFromConfig(Config.FeatureCollection configFeatureCollection) {
    addFeatureCollection(configFeatureCollection.title, configFeatureCollection.typeName,
        configFeatureCollection.dataStore, configFeatureCollection.indexWithFilter);
  }

  public void notifyFeatureCollectionAdded(FeatureCollectionWrapper featureCollection) {
    for (FeatureCollectionsListener listener : featureCollectionsListeners) {
      listener.featureCollectionAdded(this, featureCollection);
    }
  }

  public void addFeatureCollection(FeatureCollectionWrapper featureCollection) {
    featureCollections.put(featureCollection.getTitle(), featureCollection);
    notifyFeatureCollectionAdded(featureCollection);
  }

  public void addFeatureCollection(String collectionTitle, String typeName, String dataStoreTitle,
      Boolean indexFeaturesWithFilter) {
    FeatureCollectionWrapper featureCollection = featureCollections.get(collectionTitle);
    if (!featureCollections.containsKey(collectionTitle)) {
      try {
        DataStore dataStore = dataStores.getDataStore(dataStoreTitle);
        SimpleFeatureSource featureSource = null;
        if (dataStore != null) {
          featureSource = dataStore.getFeatureSource(typeName);
        }
        featureCollection = new FeatureCollectionWrapper(collectionTitle, featureSource);
        // if (indexFeaturesWithFilter != null) {
        // featureCollection.setIsIndexWithFilter(indexFeaturesWithFilter);
        // }
        addFeatureCollection(featureCollection);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public FeatureCollectionWrapper getByTitle(String title) {
    return featureCollections.get(title);
  }

  @Override
  public void configAdded(Config config) {
    for (Config.FeatureCollection configFeatureCollection : config.featureCollections) {
      addFeatureCollectionFromConfig(configFeatureCollection);
    }
  }
}
