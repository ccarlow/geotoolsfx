package geotoolsfx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.util.factory.GeoTools;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

public class FiltersFactory {
	private static final FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
	
	public static void unique(ItemSelectableFeatureCollection featureCollection, String factoryName, boolean useFilter, List<String> attributes) {
		Map<Object, Map> valueTree = new HashMap<Object, Map>();
		SimpleFeatureIterator features = featureCollection.getFeatures(useFilter).features();
		try {
			while (features.hasNext()) {
				SimpleFeature feature = features.next();
				
				Map<Object, Map> root = valueTree;
				for (String attribute : attributes) {
					Object value = feature.getAttribute(attribute);
					if (!root.containsKey(value)) {
						Map<Object, Map> newRoot = new HashMap<Object, Map>();
						root.put(value, newRoot);
						root = newRoot;
					} else {
						root = root.get(value);
					}
				}
			}	
		} finally {
			features.close();
		}
		
		for (Entry<Object, Map> treeEntry : valueTree.entrySet()) {
			List<Map<String, Object>> uniqueValueItems = new ArrayList<Map<String, Object>>();
			getUniqueValuesFromTree(attributes, 0, treeEntry, uniqueValueItems);
			
			List<Filter> filters = new ArrayList<Filter>();
			StringBuilder alias = new StringBuilder();
			for (Map<String, Object> uniqueValue : uniqueValueItems) {
				for (Entry<String, Object> entry : uniqueValue.entrySet()) {
					if (alias.length() > 0) {
						alias.append(", ");
					}
					filters.add(ff.equals(ff.property(entry.getKey()), ff.literal(entry.getValue())));
					alias.append(entry.getValue());
				}
			}
			
			if (!filters.isEmpty()) {
				if (filters.size() == 1) {
					featureCollection.getFilters().setFilter(factoryName, alias.toString(), filters.get(0));
				} else {
					featureCollection.getFilters().setFilter(factoryName, alias.toString(), ff.and(filters));
				}
			}
		}
	}
	
	private static void getUniqueValuesFromTree(List<String> attributes, int attributeIndex, Entry<Object, Map> rootEntry, List<Map<String, Object>> uniqueValues) {
		Map<String, Object> uniqueValue = new HashMap<String, Object>();
		uniqueValue.put(attributes.get(attributeIndex), rootEntry.getKey());
		uniqueValues.add(uniqueValue);
		if (!rootEntry.getValue().isEmpty()) {
			for (Object entry : rootEntry.getValue().entrySet()) {
				getUniqueValuesFromTree(attributes, attributeIndex + 1, (Entry<Object, Map>)entry, uniqueValues);	
			}
		}
	}
	
	public static List<List<Map<String, Object>>> within(ItemSelectableFeatureCollection featureCollection, String factoryName, boolean useFilter, ItemSelectableFeatureCollection containerFeatureCollection, String aliasAttribute) {
		SimpleFeatureIterator containerFeatures = containerFeatureCollection.getFeatures(false).features();
		try {
			while (containerFeatures.hasNext()) {
				SimpleFeature containerFeature = containerFeatures.next();
				Filter filter = ff.within(ff.property("the_geom"), ff.literal(containerFeature.getDefaultGeometryProperty().getValue()));
				SimpleFeatureCollection containedCollection = featureCollection.getFeatures(useFilter).subCollection(filter);
				Filters filters = featureCollection.getFilters();
				if (!containedCollection.isEmpty()) {
					String alias = containerFeature.getAttribute(aliasAttribute).toString();
					Filter existingFilter = filters.getFilter(factoryName, alias);
					if (existingFilter != null) {
						filters.setFilter(factoryName, alias, ff.or(existingFilter, filter));
					}
				}
			}
		} finally {
			containerFeatures.close();	
		}
		return null;
	}
}