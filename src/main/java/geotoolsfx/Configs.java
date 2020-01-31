package geotoolsfx;

import java.util.ArrayList;
import java.util.List;
import geotoolsfx.listener.ConfigListener;

public class Configs {
  private List<Config> configs = new ArrayList<Config>();
  private List<ConfigListener> listeners = new ArrayList<ConfigListener>();

  public void addConfig(Config config) {
    configs.add(config);
    notifyConfigAdded(config);
  }
  
  public void notifyAllConfigsAdded() {
    for (Config config : configs) {
      notifyConfigAdded(config);
    }
  }
  
  public void notifyConfigAdded(Config config) {
    for (ConfigListener listener : listeners) {
      listener.configAdded(config);
    }
  }

  public void addListener(ConfigListener listener) {
    listeners.add(listener);
  }

  public void removeListener(ConfigListener listener) {
    listeners.remove(listener);
  }
}
