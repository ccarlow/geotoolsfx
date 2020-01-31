package geotoolsfx;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Root")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Config", propOrder = {"dataStores", "featureCollections", "mapContents"})
public class Config {

  @XmlElementWrapper(name = "DataStores")
  @XmlElement(name = "DataStore")
  protected List<DataStore> dataStores = new ArrayList<DataStore>();

  public List<DataStore> getDataStores() {
    return dataStores;
  }

  public DataStore getDataStoreById(String id) {
    for (DataStore dataStore : dataStores) {
      if (dataStore.title.equals(id)) {
        return dataStore;
      }
    }
    return null;
  }

  @XmlElementWrapper(name = "FeatureCollections")
  @XmlElement(name = "FeatureCollection")
  protected List<FeatureCollection> featureCollections = new ArrayList<FeatureCollection>();

  public List<FeatureCollection> getFeatureCollections() {
    return featureCollections;
  }

  @XmlElementWrapper(name = "MapContents")
  @XmlElement(name = "MapContent")
  protected List<MapContent> mapContents = new ArrayList<MapContent>();

  public List<MapContent> getMapContents() {
    return mapContents;
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "DataStore", propOrder = {"title", "params",})
  public static class DataStore {
    @XmlElement(name = "Title")
    protected String title;
    @XmlElementWrapper(name = "Params")
    protected Map<String, String> params = new HashMap<String, String>();

    @Override
    public String toString() {
      return title;
    }
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "FeatureCollection", propOrder = {"title", "dataStore", "typeName",
      "indexWithFilter", "queries", "filtersFactories", "sortBys",})
  public static class FeatureCollection {
    @XmlElement(name = "Title")
    protected String title;
    @XmlElement(name = "DataStore")
    protected String dataStore;
    @XmlElement(name = "IndexWithFilter")
    protected Boolean indexWithFilter;
    @XmlElement(name = "TypeName")
    protected String typeName;
    @XmlElementWrapper(name = "Queries")
    @XmlElement(name = "Query")
    protected List<Query> queries = new ArrayList<Query>();
    @XmlElementWrapper(name = "FiltersFactories")
    @XmlElement(name = "Factory")
    protected List<FilterFactory> filtersFactories = new ArrayList<FilterFactory>();
    @XmlElementWrapper(name = "SortBys")
    @XmlElement(name = "SortBy")
    protected List<SortBy> sortBys = new ArrayList<SortBy>();

    public String getTitle() {
      return title;
    }

    public String getTypeName() {
      return typeName;
    }

    public String getDataStore() {
      return dataStore;
    }

    public List<Query> getQueries() {
      return queries;
    }

    public List<FilterFactory> getFiltersFactories() {
      return filtersFactories;
    }

    public List<SortBy> getSortBys() {
      return sortBys;
    }

    public Boolean isIndexWithFilter() {
      return indexWithFilter;
    }

    @Override
    public String toString() {
      return title;
    }
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "SortBy", propOrder = {"group", "alias", "value",})
  public static class SortBy {
    @XmlElement(name = "Group")
    protected String group;
    @XmlElement(name = "Alias")
    protected String alias;
    @XmlElement(name = "Value")
    protected String value;

    public String getGroup() {
      return group;
    }

    public String getAlias() {
      return alias;
    }

    public String getValue() {
      return value;
    }
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "FilterFactory", propOrder = {"method", "name", "params",})
  public static class FilterFactory {
    @XmlElement(name = "Method")
    protected String method;
    @XmlElement(name = "Name")
    protected String name;
    @XmlElementWrapper(name = "Params")
    protected Map<String, String> params = new HashMap<String, String>();

    public String getMethod() {
      return method;
    }

    public String getName() {
      return name;
    }

    public Map<String, String> getParams() {
      return params;
    }
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "GeoMap", propOrder = {"id", "title", "layers",})
  public static class MapContent {
    @XmlElement(name = "Id")
    protected String id;
    @XmlElement(name = "Title")
    protected String title;
    @XmlElementWrapper(name = "Layers")
    @XmlElement(name = "Layer")
    protected List<Layer> layers = new ArrayList<Layer>();

    public String getTitle() {
      return title;
    }

    @Override
    public String toString() {
      return title;
    }
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "Layer", propOrder = {"title", "featureCollection", "style",})
  public static class Layer {
    @XmlElement(name = "Title")
    protected String title;
    @XmlElement(name = "FeatureCollection")
    protected String featureCollection;
    @XmlElement(name = "Style")
    protected Style style;

    public String getTitle() {
      return title;
    }

    @Override
    public String toString() {
      return title;
    }
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "Style", propOrder = {"location", "name",})
  public static class Style {
    @XmlElement(name = "Location")
    protected String location;
    @XmlElement(name = "Name")
    protected String name;
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "Query", propOrder = {"alias", "filter",})
  public static class Query {
    @XmlElement(name = "Alias")
    protected String alias;
    @XmlElement(name = "Filter")
    protected String filter;

    public String getAlias() {
      return alias;
    }

    public String getFilter() {
      return filter;
    }
  }

  public static Config newInstance(String configFile) {
    try {
      File file = new File(configFile);
      JAXBContext jaxbContext = JAXBContext.newInstance(Config.class);
      Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
      return (Config) unmarshaller.unmarshal(file);
    } catch (JAXBException e) {
      e.printStackTrace();
    }
    return null;
  }
}
