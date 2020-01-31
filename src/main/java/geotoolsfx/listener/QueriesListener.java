package geotoolsfx.listener;

import org.geotools.data.Query;
import geotoolsfx.FeatureCollectionWrapper;
import geotoolsfx.Queries;

public interface QueriesListener {
  public void setFeatureCollection(FeatureCollectionWrapper featureCollection);

  public void queryAdded(Queries queries, Query query);

  public void queryRemoved(Queries queries, Query query);

  public void queryChanged(Queries queries, Query query);
}
