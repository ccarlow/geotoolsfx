package geotoolsfx.ui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import geotoolsfx.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class MainController implements Initializable {
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
  
//  @FXML
//  private ConfigController configController;
  
  @FXML
  private ConfigsController configsController;

  private App app;
  
  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }

  public void setApp(App app) {
    app.getDataStores().addDataStoresListener(catalogController);
    app.getStyles().addListener(stylesController);
    mapContentsController.setMapContents(app.getMapContents());
    featureCollectionsController.setFeatureCollections(app.getFeatureCollections());
    
    app.getConfigs().addConfigListener(configsController);
    
    configsController.setApp(app);
//    app.getConfigs().addConfigDataStoreListener(configController);
//    app.getConfigs().addConfigFeatureCollectionListener(configController);
//    app.getConfigs().addConfigMapContentListener(configController);
  }
}
