package geotoolsfx.ui.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import dnddockfx.DockPane;
import geotoolsfx.App;
import geotoolsfx.Config;
import geotoolsfx.listener.ConfigListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

public class ConfigEditController implements Initializable, ConfigListener {
  @FXML
  private VBox configContainer;
  
  @FXML
  private DockPane configEditDockNode;
  
  private Config config;
  private App app;
  
  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }
  
  public void setApp(App app) {
    this.app = app;
  }
  
  public void setConfigFeatureCollection(Config.FeatureCollection featureCollection) {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("geotoolsfx/fxml/ConfigEditFeatureCollection.fxml"));
    try {
      VBox root = fxmlLoader.load();
      configContainer.getChildren().add(root);
      ConfigEditFeatureCollectionController controller = fxmlLoader.getController();
      controller.setApp(app);
      controller.configAdded(config);
      controller.configFeatureCollectionAdded(featureCollection);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public void setConfigMapContent(Config.MapContent mapContent) {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("geotoolsfx/fxml/ConfigEditMapContent.fxml"));
    try {
      VBox root = fxmlLoader.load();
      configContainer.getChildren().add(root);
      ConfigEditMapContentController controller = fxmlLoader.getController();
      controller.configAdded(config);
      controller.configMapContentAdded(mapContent);
      controller.setDockManager(configEditDockNode.getDockManager());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public void setConfigDataStore(Config.DataStore dataStore) {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("geotoolsfx/fxml/ConfigEditDataStore.fxml"));
    try {
      VBox root = fxmlLoader.load();
      configContainer.getChildren().add(root);
      ConfigEditDataStoreController controller = fxmlLoader.getController();
      controller.configDataStoreAdded(dataStore);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public void saveConfig() {
    Config.marshal(new File("/home/me/Desktop/config_test.xml"), config);
  }

  @Override
  public void configAdded(Config config) {
    this.config = config;
  }
}
