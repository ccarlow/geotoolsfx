package geotoolsfx.listener;

import geotoolsfx.FeatureCollectionWrapper;
import geotoolsfx.SortBys;

public interface SortBysListener {
	public void setFeatureCollection(FeatureCollectionWrapper featureCollection);
	public void sortByAdded(SortBys sortBys, String group, String alias);
	public void sortByRemoved(SortBys sortBys, String group, String alias);
	public void sortByChanged(SortBys sortBys, String group, String alias);
}