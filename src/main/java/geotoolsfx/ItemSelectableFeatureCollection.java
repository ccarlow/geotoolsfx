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
import org.geotools.util.factory.GeoTools;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.sort.SortBy;

import collectionitemselector.AbstractCollectionItemSelector;
import geotoolsfx.listener.FeatureCollectionListener;

public class ItemSelectableFeatureCollection extends AbstractCollectionItemSelector implements FeatureListener {
	private SimpleFeatureSource featureSource;
	private List<SimpleFeature> selectedFeatures = new ArrayList<SimpleFeature>();
	private List<FeatureCollectionListener> featureCollectionListeners = new ArrayList<FeatureCollectionListener>();
	private Query query;
	private Queries queries = new Queries();
	private Filters filters = new Filters();
	private SortBys sortBys = new SortBys();
	private int currentIndex = 0;
	private int listSize = 0;
	private boolean isIndexWithFilter = true;
	private String title;
	private FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
	
	public ItemSelectableFeatureCollection(String title, SimpleFeatureSource featureSource) {
		this.title = title;
		this.featureSource = featureSource;
		featureSource.addFeatureListener(this);
	}
	
	public void setIsIndexWithFilter(boolean value) {
		this.isIndexWithFilter = value;
	}
	
	public SimpleFeatureSource getFeatureSource() {
		return featureSource;
	}
	
	public void updateFeature(SimpleFeature feature) {
		
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
			((SimpleFeatureStore)featureSource).modifyFeatures(attributes, values, ff.id(feature.getIdentifier()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void updateFeatures(List<SimpleFeature> features) {
		for (SimpleFeature feature : features) {
			updateFeature(feature);
		}
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
	
	public SimpleFeatureCollection getFeatures(boolean useFilter) {
		if (this.query == null) {
			this.query = new Query();
		}
		Query query = new Query();
		if (useFilter) {
			query = this.query;
		}
		try {
			return featureSource.getFeatures(query);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setQuery(Query query) {
		selectedFeatures.clear();
		if (query == null) {
			query = new Query();
		}
		this.query = query;
		fireQueryChanged();
		SimpleFeatureIterator features = null;
		try {
			features = (SimpleFeatureIterator) featureSource.getFeatures(query).features();
			if (features.hasNext()) {
				setSelected(Arrays.asList(features.next()));
			}	
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (features != null) {
				features.close();	
			}
		}
	}
	
	public Query getQuery() {
		if (query == null) {
			query = new Query();
		}
		return query;
	}
	
	public void setCurrentIndex(Feature currentFeature) {
		SimpleFeatureIterator features = getFeatures(isIndexWithFilter).features();
		int index = -1;
		try {
			int featureIndex = 1;
			while (features.hasNext()) {
				Feature feature = features.next();
				if (feature.equals(currentFeature)) {
					if (query.getFilter().evaluate(feature)) {
						index = featureIndex;
						break;
					}
				}
				featureIndex++;
			}
		} finally {
			if (features != null) {
				features.close();	
			}
		}
		currentIndex = index;
		notifyIndexChanged();
	}
	
	public void setSortBy(SortBy[] sortBy) {
		query.setSortBy(sortBy);
		fireQueryChanged();
	}
	
	public void addFeatureCollectionListener(FeatureCollectionListener featureCollectionListener) {
		this.featureCollectionListeners.add(featureCollectionListener);
	}
	
	public void setSelected(List<SimpleFeature> selected) {
		selectedFeatures.clear();
		selectedFeatures.addAll(selected);
		
		currentIndex = -1;
		if (selectedFeatures.size() > 0) {
			Feature feature = selectedFeatures.get(selectedFeatures.size() - 1);
			setCurrentIndex(feature);
		}
		
		fireFeaturesSelected();
	}
	
	public void fireQueryChanged() {
		for (FeatureCollectionListener featureCollectionListener : featureCollectionListeners) {
			featureCollectionListener.queryChanged(this);
		}
	}
	
	public void fireFeaturesSelected() {
		for (FeatureCollectionListener featureCollectionListener : featureCollectionListeners) {
			featureCollectionListener.featuresSelected(this);
		}
	}
	
	public List<SimpleFeature> getSelectedFeatures() {
		return selectedFeatures;
	}
	
	public SimpleFeature getCurrentFeature() {
		return selectedFeatures.size() > 0 ? selectedFeatures.get(selectedFeatures.size() - 1) : null;
	}

	@Override
	public void changed(FeatureEvent featureEvent) {
		System.out.println("Feature Event fired");
	}
	
	@Override
	public void setCurrentIndex(int index) {
		SimpleFeatureIterator features = getFeatures(isIndexWithFilter).features();
		int foundIndex = -1;
		int featureIndex = 1;
		try {
			while (features.hasNext()) {
				SimpleFeature feature = features.next();	
				if (featureIndex == index) {
					if (query.getFilter() == null || isIndexWithFilter || (!isIndexWithFilter && query.getFilter() != null && query.getFilter().evaluate(feature))) {
						selectedFeatures.clear();
						selectedFeatures.add(feature);
						foundIndex = featureIndex;
					}
				}
				featureIndex++;
			}
		} finally {
			if (features != null) {
				features.close();	
			}
		}
		listSize = featureIndex - 1;
		currentIndex = foundIndex;
		notifyIndexChanged();
		fireFeaturesSelected();
	}

	@Override
	public int getCurrentIndex() {
		return currentIndex;
	}

	@Override
	public void setNextIndex() {
		SimpleFeatureIterator features = getFeatures(isIndexWithFilter).features();
		int foundIndex = -1;
		int featureIndex = 1;
		try {
			while (features.hasNext()) {
				SimpleFeature feature = features.next();	
				if (featureIndex >= currentIndex) {
					if (query.getFilter() == null || isIndexWithFilter || (!isIndexWithFilter && query.getFilter() != null && query.getFilter().evaluate(feature))) {
						if (foundIndex == -1 || foundIndex == currentIndex) {
							selectedFeatures.clear();
							selectedFeatures.add(feature);
							foundIndex = featureIndex;
						}
					}
				}
				featureIndex++;
			}
		} finally {
			if (features != null) {
				features.close();	
			}
		}
		listSize = featureIndex - 1;
		currentIndex = foundIndex;
		notifyIndexChanged();
		fireFeaturesSelected();
	}

	@Override
	public void setPreviousIndex() {
		SimpleFeatureIterator features = getFeatures(isIndexWithFilter).features();
		int foundIndex = -1;
		int featureIndex = 1;
		SimpleFeature previousFeature = null;
		int previousIndex = currentIndex;
		try {
			while (features.hasNext()) {
				SimpleFeature feature = features.next();	
				if (featureIndex <= currentIndex) {
					if (query.getFilter() == null || isIndexWithFilter || (!isIndexWithFilter && query.getFilter() != null && query.getFilter().evaluate(feature))) {
						if (featureIndex >= currentIndex) {
							if (previousFeature == null) {
								previousFeature = feature;
							}
							selectedFeatures.clear();
							selectedFeatures.add(previousFeature);
							foundIndex = previousIndex;
						}
						previousFeature = feature;
						previousIndex = featureIndex;
					}
				}
				featureIndex++;
			}
		} finally {
			if (features != null) {
				features.close();	
			}
		}
		listSize = featureIndex - 1;
		currentIndex = foundIndex;
		notifyIndexChanged();
		fireFeaturesSelected();
	}

	@Override
	public int getListSize() {
		return listSize;
	}
}
