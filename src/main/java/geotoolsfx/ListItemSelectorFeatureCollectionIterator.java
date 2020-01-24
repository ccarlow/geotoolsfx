package geotoolsfx;

import java.util.NoSuchElementException;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import collectionitemselector.ListItemSelector;

public class ListItemSelectorFeatureCollectionIterator implements SimpleFeatureIterator {
  private ListItemSelector listItemSelector;
  private boolean nextUsed = true;
	public ListItemSelectorFeatureCollectionIterator(ListItemSelector listItemSelector) {
	  this.listItemSelector = listItemSelector;
	}
	
  @Override
  public boolean hasNext() {
    int currentIndex = listItemSelector.getCurrentIndex();
    if (nextUsed) {
      listItemSelector.setNextIndex();
      nextUsed = false; 
    }
    return currentIndex < listItemSelector.getCurrentIndex();
  }
  
  @Override
  public SimpleFeature next() throws NoSuchElementException {
    if (nextUsed) {
      listItemSelector.setNextIndex();
    }
    nextUsed = true;
    return (SimpleFeature)listItemSelector.getList().get(listItemSelector.getCurrentIndex() - 1);
  }
  
  @Override
  public void close() {
    listItemSelector.resetCurrentIndex();
  }

}
