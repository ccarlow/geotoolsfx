package geotoolsfx.listener;

import geotoolsfx.ItemSelectableFeatureCollection;

public interface FeatureCollectionListener {
	public void featuresSelected(ItemSelectableFeatureCollection featureCollection);
	public void queryChanged(ItemSelectableFeatureCollection featureCollection);
}