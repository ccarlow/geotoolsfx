package geotoolsfx.listener;

import org.geotools.data.Query;

import geotoolsfx.ItemSelectableFeatureCollection;
import geotoolsfx.Queries;

public interface QueriesListener {
	public void setFeatureCollection(ItemSelectableFeatureCollection featureCollection);
	public void queryAdded(Queries queries, Query query);
	public void queryRemoved(Queries queries, Query query);
	public void queryChanged(Queries queries, Query query);
}