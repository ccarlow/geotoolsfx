package geotoolsfx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.geotools.data.FeatureEvent;
import org.geotools.data.FeatureListener;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollectionIteration;
import org.geotools.util.factory.GeoTools;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.sort.SortBy;
import collectionitemselector.AbstractCollectionItemSelector;
import geotoolsfx.listener.FeatureCollectionListener;
import geotoolsfx.listener.FeatureCollectionWrapperListener;

public class FeatureCollectionWrapper implements FeatureListener {
  private FeatureCollectionWrapperSelector featureCollectionSelector =
      new FeatureCollectionWrapperSelector();
  private SimpleFeatureSource featureSource;
  private List<FeatureCollectionWrapperListener> featureCollectionListeners =
      new ArrayList<FeatureCollectionWrapperListener>();
  private SimpleFeatureCollection featureCollection;
  private QueryWrapper query = new QueryWrapper();
  private Filters filters = new Filters();
  private SortBys sortBys = new SortBys();
  private String title;
  private FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());

  public FeatureCollectionWrapper(String title, SimpleFeatureSource featureSource) {
    this.title = title;
    this.featureSource = featureSource;
    if (featureSource != null) {
      featureSource.addFeatureListener(this);
    }
    featureCollectionSelector.setFeatureCollection(this);
  }

  public FeatureCollectionWrapper(String title, SimpleFeatureCollection featureCollection) {
    this.title = title;
    this.featureCollection = featureCollection;
    featureCollectionSelector.setFeatureCollection(this);
  }

  public SimpleFeatureSource getFeatureSource() {
    return featureSource;
  }

  public void updateFeature(SimpleFeature feature) {
    if (featureSource != null) {
      String[] attributes = new String[feature.getProperties().size()];
      Object[] values = new Object[feature.getProperties().size()];

      int index = 0;
      Iterator<Property> properties = feature.getProperties().iterator();
      while (properties.hasNext()) {
        Property property = properties.next();
        attributes[index] = property.getName().getLocalPart();
        values[index] = property.getValue();
        index++;
      }

      try {
        ((SimpleFeatureStore) featureSource).modifyFeatures(attributes, values,
            ff.id(feature.getIdentifier()));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void updateFeatures(List<SimpleFeature> features) {
    if (featureSource != null) {
      for (SimpleFeature feature : features) {
        updateFeature(feature);
      }
    }
  }

  public FeatureCollectionWrapperSelector getFeatureCollectionSelector() {
    return featureCollectionSelector;
  }

  public QueryWrapper getQuery() {
    return query;
  }

  public Filters getFilters() {
    return filters;
  }

  public SortBys getSortBys() {
    return sortBys;
  }

  public String getTitle() {
    return title;
  }

  public SimpleFeatureCollection getFeatures() {
    if (featureSource != null) {
      featureCollection = query.getFeatures(featureSource);
      return featureCollection;
    } else if (featureCollection != null) {
      return featureCollection;
    }
    return null;
  }

  public void setSortBy(SortBy[] sortBy) {
    query.setQuery(query.getAlias(), query.getFilter(), sortBy);
    fireQueryChanged();
  }

  public void addFeatureCollectionListener(FeatureCollectionWrapperListener listener) {
    this.featureCollectionListeners.add(listener);
  }

  public void fireQueryChanged() {
    for (FeatureCollectionWrapperListener listener : featureCollectionListeners) {
      listener.queryChanged(this);
    }
  }

  @Override
  public void changed(FeatureEvent featureEvent) {
    System.out.println("Feature Event fired");
  }
}
