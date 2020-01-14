package geotoolsfx;

import org.opengis.filter.Filter;

public interface FeatureCollectionFilterCombinatorCollegue {
	public void addMediator(FeatureCollectionFilterCombinator mediator);
	public ItemSelectableFeatureCollection getFeatureCollection(FeatureCollectionFilterCombinator mediator);
	public Filter getFilter(FeatureCollectionFilterCombinator mediator);
}
