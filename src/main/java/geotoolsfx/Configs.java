package geotoolsfx;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import geotoolsfx.listener.ConfigDataStoreListener;
import geotoolsfx.listener.ConfigFeatureCollectionListener;
import geotoolsfx.listener.ConfigListener;
import geotoolsfx.listener.ConfigMapContentListener;

public class Configs {
  private Map<String, Config> configs = new LinkedHashMap<String, Config>();
  
  private List<ConfigListener> configListeners = new ArrayList<ConfigListener>();
  private List<ConfigDataStoreListener> dataStoreListeners = new ArrayList<ConfigDataStoreListener>();
  private List<ConfigFeatureCollectionListener> featureCollectionListeners = new ArrayList<ConfigFeatureCollectionListener>();
  private List<ConfigMapContentListener> mapContentListeners = new ArrayList<ConfigMapContentListener>();

  public void addConfig(String location) {
    addConfig(Config.newInstance(location));
  }
  
  public void addConfig(Config config) {
    configs.put(config.getId(), config);
    
    for (Config.DataStore dataStore : config.dataStores) {
      notifyConfigDataStoreAdded(dataStore);
    }
    
    for (Config.FeatureCollection featureCollection : config.featureCollections) {
      notifyConfigFeatureCollectionAdded(featureCollection);
    }
    
    for (Config.MapContent mapContent : config.mapContents) {
      notifyConfigMapContentAdded(mapContent);
    }
    
    notifyConfigAdded(config);
  }
  
  public void notifyConfigAdded(Config config) {
    for (ConfigListener listener : configListeners) {
      try {
        listener.configAdded(config);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
  
  public void notifyConfigDataStoreAdded(Config.DataStore dataStore) {
    for (ConfigDataStoreListener listener : dataStoreListeners) {
      try {
        listener.configDataStoreAdded(dataStore);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
  
  public void notifyConfigFeatureCollectionAdded(Config.FeatureCollection featureCollection) {
    for (ConfigFeatureCollectionListener listener : featureCollectionListeners) {
      try {
        listener.configFeatureCollectionAdded(featureCollection);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
  
  public void notifyConfigMapContentAdded(Config.MapContent mapContent) {
    for (ConfigMapContentListener listener : mapContentListeners) {
      try {
        listener.configMapContentAdded(mapContent);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
  
  public void saveConfig(String configId) {
    Config config = configs.get(configId);
    if (config != null) {
      File file = new File(configId);
      if (file.exists()) {
        Config.marshal(file, config); 
      } else {
        //notify location needs to be set
      }
    }
  }

  public void addConfigDataStoreListener(ConfigDataStoreListener listener) {
    dataStoreListeners.add(listener);
  }

  public void removeConfigDataStoreListener(ConfigDataStoreListener listener) {
    dataStoreListeners.remove(listener);
  }
  
  public void addConfigFeatureCollectionListener(ConfigFeatureCollectionListener listener) {
    featureCollectionListeners.add(listener);
  }

  public void removeConfigFeatureCollectionListener(ConfigFeatureCollectionListener listener) {
    featureCollectionListeners.remove(listener);
  }
  
  public void addConfigMapContentListener(ConfigMapContentListener listener) {
    mapContentListeners.add(listener);
  }

  public void removeConfigMapContentListener(ConfigMapContentListener listener) {
    mapContentListeners.remove(listener);
  }
  
  public void addConfigListener(ConfigListener listener) {
    configListeners.add(listener);
  }

  public void removeConfigListener(ConfigListener listener) {
    configListeners.remove(listener);
  }
}
