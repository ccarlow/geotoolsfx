package geotoolsfx.listener;

import geotoolsfx.FeatureCollectionWrapper;
import geotoolsfx.Filters;

public interface FiltersListener {
  public void addFeatureCollection(FeatureCollectionWrapper featureCollection);

  public void filterAdded(Filters filters, String group, String alias);

  public void filterRemoved(Filters filters, String group, String alias);

  public void filterChanged(Filters filters, String group, String alias);
}
