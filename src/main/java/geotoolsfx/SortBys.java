package geotoolsfx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;

import geotoolsfx.listener.FiltersListener;
import geotoolsfx.listener.SortBysListener;

public class SortBys {
	private Map<String, Map<String, SortBy[]>> sortBys = new HashMap<String, Map<String, SortBy[]>>();
	private List<SortBysListener> sortBysListeners = new ArrayList<SortBysListener>();
	
	public Set<String> getGroupNames() {
		return sortBys.keySet();
	}
	
	public Set<String> getAliases(String group) {
		Map<String, SortBy[]> aliasMap = sortBys.get(group);
		if (aliasMap != null) {
			return aliasMap.keySet();
		}
		return null;
	}
	
	public void setFilter(String group, String alias, SortBy[] sortBy) {
		Map<String, SortBy[]> aliasMap = sortBys.get(group);
		if (aliasMap != null) {
			if (aliasMap.containsKey(alias)) {
				aliasMap.put(alias, sortBy);
				fireSortByChanged(group, alias);
				return;
			}
		}
		
		aliasMap.put(alias, sortBy);
		fireSortByAdded(group, alias);
	}
	
	public SortBy[] getSortBy(String group, String alias) {
		Map<String, SortBy[]> aliasMap = sortBys.get(group);
		if (aliasMap != null) {
			return aliasMap.get(alias);
		}
		return null;
	}
	
	public void removeSortBy(String group, String alias) {
		Map<String, SortBy[]> aliasMap = sortBys.get(group);
		if (aliasMap != null) {
			SortBy[] removed = aliasMap.remove(alias);
			if (removed != null) {
				fireSortByRemoved(group, alias);
			}
		}
	}
	
	public void addSortBysListener(SortBysListener listener) {
		sortBysListeners.add(listener);
	}
	
	public void removeSortBysListener(SortBysListener listener) {
		sortBysListeners.remove(listener);
	}
	
	public void fireSortByAdded(String group, String alias) {
		for (SortBysListener listener : sortBysListeners) {
			listener.sortByAdded(this, group, alias);
		}
	}
	
	public void fireSortByChanged(String group, String alias) {
		for (SortBysListener listener : sortBysListeners) {
			listener.sortByChanged(this, group, alias);
		}
	}
	
	public void fireSortByRemoved(String group, String alias) {
		for (SortBysListener listener : sortBysListeners) {
			listener.sortByRemoved(this, group, alias);
		}
	}
}
