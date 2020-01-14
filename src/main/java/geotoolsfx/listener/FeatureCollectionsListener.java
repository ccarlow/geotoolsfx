package geotoolsfx.listener;

import geotoolsfx.FeatureCollections;
import geotoolsfx.ItemSelectableFeatureCollection;

public interface FeatureCollectionsListener {
	public void featureCollectionAdded(FeatureCollections featureCollections, ItemSelectableFeatureCollection featureCollection);
}