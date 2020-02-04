package geotoolsfx;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

@XmlRootElement(name = "Root")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Config", propOrder = {"dataStores", "featureCollections", "mapContents"})
public class Config {

  @XmlElementWrapper(name = "DataStores")
  @XmlElement(name = "DataStore")
  public List<DataStore> dataStores = new ArrayList<DataStore>();

  @XmlTransient
  private String id;
  public String getId() {
    if (id == null) {
      id = UUID.randomUUID().toString();
    }
    return id;
  }
  
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
  public List<FeatureCollection> featureCollections = new ArrayList<FeatureCollection>();

  public List<FeatureCollection> getFeatureCollections() {
    return featureCollections;
  }

  @XmlElementWrapper(name = "MapContents")
  @XmlElement(name = "MapContent")
  public List<MapContent> mapContents = new ArrayList<MapContent>();

  public List<MapContent> getMapContents() {
    return mapContents;
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "DataStore", propOrder = {"title", "params",})
  public static class DataStore {
    @XmlElement(name = "Title")
    public String title;
    @XmlElementWrapper(name = "Params")
    public Map<String, String> params = new HashMap<String, String>();

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
    public String title;
    @XmlElement(name = "DataStore")
    public String dataStore;
    @XmlElement(name = "IndexWithFilter")
    public Boolean indexWithFilter;
    @XmlElement(name = "TypeName")
    public String typeName;
    @XmlElementWrapper(name = "Queries")
    @XmlElement(name = "Query")
    public List<Query> queries = new ArrayList<Query>();
    @XmlElementWrapper(name = "FiltersFactories")
    @XmlElement(name = "Factory")
    public List<FilterFactory> filtersFactories = new ArrayList<FilterFactory>();
    @XmlElementWrapper(name = "SortBys")
    @XmlElement(name = "SortBy")
    public List<SortBy> sortBys = new ArrayList<SortBy>();

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
    public String group;
    @XmlElement(name = "Alias")
    public String alias;
    @XmlElement(name = "Value")
    public String value;

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
    public String method;
    @XmlElement(name = "Name")
    public String name;
    @XmlElementWrapper(name = "Params")
    public Map<String, String> params = new HashMap<String, String>();

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
    public String id;
    @XmlElement(name = "Title")
    public String title;
    @XmlElementWrapper(name = "Layers")
    @XmlElement(name = "Layer")
    public List<Layer> layers = new ArrayList<Layer>();

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
    public String title;
    @XmlElement(name = "FeatureCollection")
    public String featureCollection;
    @XmlElement(name = "Style")
    public Style style;

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
    public String location;
    @XmlElement(name = "Name")
    public String name;
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "Query", propOrder = {"alias", "filter",})
  public static class Query {
    @XmlElement(name = "Alias")
    public String alias;
    @XmlElement(name = "Filter")
    public String filter;

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
      Config config = (Config) unmarshaller.unmarshal(file);
      config.id = configFile;
      return config;
    } catch (JAXBException e) {
      e.printStackTrace();
    }
    return null;
  }
  
  public static void marshal(File file, Config config) {
    try {
//      File file = new File(location);
      JAXBContext jaxbContext = JAXBContext.newInstance(Config.class);
      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

      OutputStream out = new FileOutputStream(file);
      DOMResult domResult = new DOMResult();
      jaxbMarshaller.marshal(config, domResult);

      // Transformer used to beautify xml output that is otherwise uglified by faulty indentations
      Transformer transformer = TransformerFactory.newInstance().newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
      transformer.transform(new DOMSource(domResult.getNode()), new StreamResult(out));
    } catch (FileNotFoundException | TransformerFactoryConfigurationError | TransformerException e) {
      e.printStackTrace();
    } catch (JAXBException e) {
      e.printStackTrace();
    }

  }
}
