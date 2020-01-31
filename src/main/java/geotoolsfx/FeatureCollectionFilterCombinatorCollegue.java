package geotoolsfx;

import org.opengis.filter.Filter;

public interface FeatureCollectionFilterCombinatorCollegue {
  public void setCombinator(FeatureCollectionFilterCombinator mediator);

  public FeatureCollectionWrapper getFeatureCollection(FeatureCollectionFilterCombinator mediator);

  public Filter getFilter(FeatureCollectionFilterCombinator mediator);
}
