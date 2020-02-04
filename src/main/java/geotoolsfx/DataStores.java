package geotoolsfx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.geotools.data.DataAccessFactory.Param;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DefaultRepository;
import geotoolsfx.listener.ConfigDataStoreListener;
import geotoolsfx.listener.DataStoresListener;

public class DataStores implements ConfigDataStoreListener {
  private DefaultRepository dataStores = new DefaultRepository();
  private List<DataStoresListener> dataStoresListeners = new ArrayList<DataStoresListener>();
  private List<Config.DataStore> configDataStores = new ArrayList<Config.DataStore>();

  public void addDataStoresListener(DataStoresListener listener) {
    dataStoresListeners.add(listener);
  }

  public void removeDataStoresListener(DataStoresListener listener) {
    dataStoresListeners.remove(listener);
  }

  public Config.DataStore getConfigDataStores(String name) {
    for (Config.DataStore dataStore : configDataStores) {
      if (dataStore.title.equals(name)) {
        return dataStore;
      }
    }
    return null;
  }

  public DataStore getDataStore(String name) {
    DataStore dataStore = dataStores.dataStore(name);
    if (dataStore == null) {
      for (Iterator<DataStoreFactorySpi> i = DataStoreFinder.getAvailableDataStores(); i
          .hasNext();) {
        DataStoreFactorySpi factory = (DataStoreFactorySpi) i.next();
        Config.DataStore configDataStore = getConfigDataStores(name);
        
//        for (Param param : factory.getParametersInfo()) {
//          System.out.println(factory.getDisplayName() + " = " + param.key + " = " + param.description);
//        }
        
        if (configDataStore != null) {
          if (factory.canProcess((Map) configDataStore.params)) {
            try {
              dataStore = factory.createDataStore((Map) configDataStore.params);
              dataStores.register(configDataStore.title, dataStore);
            } catch (IOException e) {
              e.printStackTrace();
            }
            break;
          }
        }
      }
    }
    return dataStore;
  }

  @Override
  public void configDataStoreAdded(Config.DataStore dataStore) {
    configDataStores.add(dataStore);
    for (DataStoresListener dataStoresListener : dataStoresListeners) {
      dataStoresListener.dataStoreAdded(dataStore);
    }
  }
}
