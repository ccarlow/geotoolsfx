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

import geotoolsfx.listener.FeatureCollectionsListener;

public class FeatureCollections {
	private Map<String, ItemSelectableFeatureCollection> featureCollections = new HashMap<String, ItemSelectableFeatureCollection>();
	private DataStores dataStores;
	private List<FeatureCollectionsListener> featureCollectionsListeners = new ArrayList<FeatureCollectionsListener>();
	
	public void addFeatureCollectionsFromConfig(Config config) {
		for (Config.FeatureCollection configFeatureCollection : config.featureCollections) {
			addFeatureCollectionFromConfig(configFeatureCollection);
		}
	}
	
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
		addFeatureCollection(configFeatureCollection.title, configFeatureCollection.typeName, configFeatureCollection.dataStore, configFeatureCollection.indexWithFilter);
	}
	
	public void fireFeatureCollectionAdded(ItemSelectableFeatureCollection featureCollection) {
		for (FeatureCollectionsListener listener : featureCollectionsListeners) {
			listener.featureCollectionAdded(this, featureCollection);
		}
	}
	
	public void addFeatureCollection(String collectionTitle, String typeName, String dataStoreTitle, Boolean indexFeaturesWithFilter) {
		ItemSelectableFeatureCollection featureCollection = featureCollections.get(collectionTitle);
		if (!featureCollections.containsKey(collectionTitle)) {
			try {
				DataStore dataStore = dataStores.getDataStore(dataStoreTitle);
				SimpleFeatureSource featureSource = dataStore.getFeatureSource(typeName);
				featureCollection = new ItemSelectableFeatureCollection(collectionTitle, featureSource);
				if (indexFeaturesWithFilter != null) {
					featureCollection.setIsIndexWithFilter(indexFeaturesWithFilter);	
				}
				featureCollections.put(collectionTitle, featureCollection);
				fireFeatureCollectionAdded(featureCollection);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public ItemSelectableFeatureCollection getByTitle(String title) {
		return featureCollections.get(title);
	}
}
