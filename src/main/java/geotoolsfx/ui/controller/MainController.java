package geotoolsfx.ui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import dock.DockGroup;
import dock.DockPane;
import geotoolsfx.Config;
import geotoolsfx.Configs;
import geotoolsfx.DataStores;
import geotoolsfx.FeatureCollections;
import geotoolsfx.MapContents;
import geotoolsfx.Styles;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class MainController implements Initializable{
	@FXML
	private CatalogController catalogController;
	
	@FXML
	private FeatureCollectionsController featureCollectionsController;
	
	@FXML
	private MapContentsController mapContentsController;
	
	@FXML
	private LayersController layersController;
	
	@FXML
	private StylesController stylesController;
	
	private Configs configs;
	private FeatureCollections featureCollections;
	private DataStores dataStores;
	private MapContents mapContents;
	private Styles styles;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dataStores = new DataStores();
		dataStores.addDataStoresListener(catalogController);
		featureCollections = new FeatureCollections();
		featureCollections.addListener(featureCollectionsController);
		featureCollections.setDataStores(dataStores);
		mapContents = new MapContents();
		mapContents.setFeatureCollections(featureCollections);
		mapContents.addMapContentsListener(mapContentsController);
		styles = new Styles();
		styles.addStylesListener(stylesController);
		mapContents.setStyles(styles);
		configs = new Configs();
		
		mapContentsController.setMapContents(mapContents);
		featureCollectionsController.setFeatureCollections(featureCollections);
	}
	
	public void addConfig(String location) {
		Config config = Config.newInstance("resources/config/geotoolsfx.xml");
		configs.addConfig(config);
		dataStores.addFromConfig(config);
		featureCollections.addFeatureCollectionsFromConfig(config);
		mapContents.addFromConfig(config);
	}
	
	public FeatureCollections getFeatureCollections() {
		return featureCollections;
	}
	
	public Styles getStyles() {
		return styles;
	}
	
	public MapContents getMapContents() {
		return mapContents;
	}
	
	public DataStores getDataStores() {
		return dataStores;
	}
}