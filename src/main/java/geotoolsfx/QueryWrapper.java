package geotoolsfx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;
import geotoolsfx.listener.QueryListener;

public class QueryWrapper {
  private Query query;
  private List<QueryListener> queryListeners = new ArrayList<QueryListener>();

  public QueryWrapper() {
    query = new Query();
  }

  public void addQueryListener(QueryListener queryListener) {
    queryListeners.add(queryListener);
  }

  public void removeQueryListener(QueryListener queryListener) {
    queryListeners.remove(queryListener);
  }

  public void setQuery(QueryWrapper query) {
    this.query = query.query;
  }

  public void setQuery(String alias, Filter filter, SortBy[] sortBy) {
    query.setAlias(alias);
    query.setFilter(filter);
    query.setSortBy(sortBy);
    notifyQueryChanged();
  }

  public String getAlias() {
    return query.getAlias();
  }

  public SortBy[] getSortBy() {
    return query.getSortBy();
  }

  public Filter getFilter() {
    return query.getFilter();
  }

  public SimpleFeatureCollection getFeatures(SimpleFeatureSource featureSource) {
    try {
      return featureSource.getFeatures(query);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public boolean evaluate(SimpleFeature feature) {
    if (query.getFilter() != null) {
      return query.getFilter().evaluate(feature);
    }
    return true;
  }

  public void notifyQueryChanged() {
    for (QueryListener listener : queryListeners) {
      listener.queryChanged();
    }
  }
}
