package geotoolsfx;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.geotools.data.Query;

import geotoolsfx.listener.QueriesListener;

public class Queries {
	private List<Query> queries = new ArrayList<Query>();
	private List<QueriesListener> queriesListeners = new ArrayList<QueriesListener>();
	
	public Set<String> getAliases() {
		Set<String> aliases = new HashSet<String>();
		for (Query query : queries) {
			aliases.add(query.getAlias());
		}
		return aliases;
	}
	
	public void addQuery(Query query) {
		queries.add(query);
		fireQueryAdded(query);
	}
	
	public void removeQuery(Query query) {
		queries.remove(query);
		fireQueryRemoved(query);
	}
	
	public void setQuery(Query query) {
		fireQueryChanged(query);
	}
	
	public void addQueriesListener(QueriesListener listener) {
		queriesListeners.add(listener);
	}
	
	public void removeFiltersListener(QueriesListener listener) {
		queriesListeners.remove(listener);
	}
	
	public void fireQueryAdded(Query query) {
		for (QueriesListener listener : queriesListeners) {
			listener.queryAdded(this, query);
		}
	}
	
	public void fireQueryChanged(Query query) {
		for (QueriesListener listener : queriesListeners) {
			listener.queryChanged(this, query);
		}
	}
	
	public void fireQueryRemoved(Query query) {
		for (QueriesListener listener : queriesListeners) {
			listener.queryRemoved(this, query);
		}
	}
}
