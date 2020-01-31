package geotoolsfx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.opengis.filter.Filter;
import geotoolsfx.listener.FiltersListener;

public class Filters {
  private Map<String, Map<String, Filter>> filtersMap = new HashMap<String, Map<String, Filter>>();
  private List<FiltersListener> filtersListeners = new ArrayList<FiltersListener>();

  public Set<String> getGroupNames() {
    return filtersMap.keySet();
  }

  public Set<String> getAliases(String group) {
    Map<String, Filter> aliasMap = filtersMap.get(group);
    if (aliasMap != null) {
      return aliasMap.keySet();
    }
    return null;
  }

  public void clear() {
    List<Map<String, String>> groupAliases = new ArrayList<Map<String, String>>();
    for (Entry<String, Map<String, Filter>> entry : filtersMap.entrySet()) {
      Map<String, String> groupAlias = new HashMap<String, String>();
      groupAliases.add(groupAlias);
      for (Entry<String, Filter> entry2 : entry.getValue().entrySet()) {
        groupAlias.put(entry.getKey(), entry2.getKey());
      }
    }
    filtersMap.clear();
    for (Map<String, String> groupAlias : groupAliases) {
      for (Entry<String, String> entry : groupAlias.entrySet()) {
        fireFilterRemoved(entry.getKey(), entry.getValue());
      }
    }
  }

  public void setFilter(String group, String alias, Filter filter) {
    Map<String, Filter> aliasMap = filtersMap.get(group);
    if (aliasMap != null) {
      if (aliasMap.containsKey(alias)) {
        aliasMap.put(alias, filter);
        fireFilterChanged(group, alias);
        return;
      }
    } else {
      aliasMap = new HashMap<String, Filter>();
      filtersMap.put(group, aliasMap);
    }

    aliasMap.put(alias, filter);
    fireFilterAdded(group, alias);
  }

  public Filter getFilter(String group, String alias) {
    Map<String, Filter> aliasMap = filtersMap.get(group);
    if (aliasMap != null) {
      return aliasMap.get(alias);
    }
    return null;
  }

  public void removeFilter(String group, String alias) {
    Map<String, Filter> aliasMap = filtersMap.get(group);
    if (aliasMap != null) {
      Filter removed = aliasMap.remove(alias);
      if (removed != null) {
        fireFilterRemoved(group, alias);
      }
    }
  }

  public void addFiltersListener(FiltersListener listener) {
    filtersListeners.add(listener);
  }

  public void removeFiltersListener(FiltersListener listener) {
    filtersListeners.remove(listener);
  }

  public void fireFilterAdded(String group, String alias) {
    for (FiltersListener listener : filtersListeners) {
      listener.filterAdded(this, group, alias);
    }
  }

  public void fireFilterChanged(String group, String alias) {
    for (FiltersListener listener : filtersListeners) {
      listener.filterChanged(this, group, alias);
    }
  }

  public void fireFilterRemoved(String group, String alias) {
    for (FiltersListener listener : filtersListeners) {
      listener.filterRemoved(this, group, alias);
    }
  }
}
