package geotoolsfx.listener;

import geotoolsfx.FeatureCollectionWrapper;
import geotoolsfx.FeatureCollections;

public interface FeatureCollectionsListener {
	public void featureCollectionAdded(FeatureCollections featureCollections, FeatureCollectionWrapper featureCollection);
}