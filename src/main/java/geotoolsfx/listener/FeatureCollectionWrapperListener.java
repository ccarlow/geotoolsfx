package geotoolsfx.listener;

import collectionitemselector.CollectionItemSelectorListener;
import geotoolsfx.FeatureCollectionWrapper;

public interface FeatureCollectionWrapperListener extends CollectionItemSelectorListener {
  public void queryChanged(FeatureCollectionWrapper featureCollection);
}
