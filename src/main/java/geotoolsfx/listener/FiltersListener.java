package geotoolsfx.listener;

import geotoolsfx.Filters;
import geotoolsfx.ItemSelectableFeatureCollection;

public interface FiltersListener {
	public void addFeatureCollection(ItemSelectableFeatureCollection featureCollection);
	public void filterAdded(Filters filters, String group, String alias);
	public void filterRemoved(Filters filters, String group, String alias);
	public void filterChanged(Filters filters, String group, String alias);
}