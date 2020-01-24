package geotoolsfx;

import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import collectionitemselector.ListItemSelector;
import geotoolsfx.listener.FeatureCollectionListener;

public class ListItemSelectorFeatureCollection extends ListFeatureCollection {
  
  private ListItemSelector listItemSelector;
  
  public ListItemSelectorFeatureCollection(ListItemSelector listItemSelector) {
    super(((SimpleFeature)listItemSelector.getList().get(0)).getType(), listItemSelector.getList());
    this.listItemSelector = listItemSelector;
  }

  public SimpleFeatureIterator features() {
      return new ListItemSelectorFeatureCollectionIterator(listItemSelector);
  }
}