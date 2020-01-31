package geotoolsfx;

import java.util.ArrayList;
import java.util.List;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.util.factory.GeoTools;
import org.opengis.filter.FilterFactory2;

public abstract class FeatureCollectionFilterCombinator {
  protected List<FeatureCollectionFilterCombinatorCollegue> collegues =
      new ArrayList<FeatureCollectionFilterCombinatorCollegue>();
  protected static final FilterFactory2 ff =
      CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());

  public abstract void setFilter();

  public void addCollegue(FeatureCollectionFilterCombinatorCollegue collegue) {
    collegues.add(collegue);
  }

  public void removeCollegue(FeatureCollectionFilterCombinatorCollegue collegue) {
    collegues.remove(collegue);
  }
}
