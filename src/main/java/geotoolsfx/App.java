package geotoolsfx;

public class App {

  private DataStores dataStores = new DataStores();
  private FeatureCollections featureCollections = new FeatureCollections();
  private MapContents mapContents = new MapContents();
  private Styles styles  = new Styles();
  private Configs configs = new Configs();
  
  public App() {
    featureCollections.setDataStores(dataStores);
    mapContents.setStyles(styles);
    mapContents.setFeatureCollections(featureCollections);
    configs.addConfigDataStoreListener(dataStores);
    configs.addConfigFeatureCollectionListener(featureCollections);
    configs.addConfigMapContentListener(mapContents);
  }
  
  public MapContents getMapContents() {
    return mapContents;
  }
  
  public FeatureCollections getFeatureCollections() {
    return featureCollections;
  }
  
  public Styles getStyles() {
    return styles;
  }
  
  public DataStores getDataStores() {
    return dataStores;
  }
  
  public Configs getConfigs() {
    return configs;
  }
}
