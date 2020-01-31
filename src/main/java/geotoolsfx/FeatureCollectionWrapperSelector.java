package geotoolsfx;

import org.geotools.data.simple.SimpleFeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import collectionitemselector.AbstractCollectionItemSelector;

public class FeatureCollectionWrapperSelector extends AbstractCollectionItemSelector {
  private FeatureCollectionWrapper featureCollection;

  public void setFeatureCollection(FeatureCollectionWrapper featureCollection) {
    this.featureCollection = featureCollection;
    resetCurrentIndex();
  }

  @Override
  public void setCurrentIndex(int index) {
    if (index >= 1 && index <= featureCollection.getFeatures().size()) {
      currentIndex = index;
      notifyIndexChanged();
    }
  }

  @Override
  public void setNextIndex() {
    if (currentIndex + 1 <= featureCollection.getFeatures().size()) {
      SimpleFeatureIterator features = featureCollection.getFeatures().features();
      int index = 1;
      while (features.hasNext()) {
        SimpleFeature feature = features.next();
        if (index > currentIndex) {
          if (featureCollection.getQuery().evaluate(feature)) {
            currentIndex = index;
            notifyIndexChanged();
            break;
          }
        }
        index++;
      }
    }
  }

  @Override
  public void setPreviousIndex() {
    if (currentIndex - 1 >= 1) {

      SimpleFeatureIterator features = featureCollection.getFeatures().features();
      int previousIndex = currentIndex;
      int index = 1;
      while (features.hasNext()) {
        SimpleFeature feature = features.next();
        if (index == currentIndex) {
          currentIndex = previousIndex;
          notifyIndexChanged();
          break;
        }
        if (featureCollection.getQuery().evaluate(feature)) {
          previousIndex = index;
        }
        index++;
      }
    }
  }

  @Override
  public int getListSize() {
    return featureCollection.getFeatures().size();
  }
}
