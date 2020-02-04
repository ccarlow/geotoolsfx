package geotoolsfx.ui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import dnddockfx.DockPane;
import geotoolsfx.App;
import geotoolsfx.Config;
import geotoolsfx.Config.DataStore;
import geotoolsfx.Config.FeatureCollection;
import geotoolsfx.Config.MapContent;
import geotoolsfx.Configs;
import geotoolsfx.listener.ConfigDataStoreListener;
import geotoolsfx.listener.ConfigFeatureCollectionListener;
import geotoolsfx.listener.ConfigListener;
import geotoolsfx.listener.ConfigMapContentListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class ConfigController implements Initializable, ConfigListener, ConfigDataStoreListener, ConfigFeatureCollectionListener, ConfigMapContentListener {
  @FXML
  private DockPane configDockPane;
  
  @FXML
  private TreeView treeView;
  
  @FXML
  private TreeItem dataStoresTreeItem;
  
  @FXML
  private TreeItem featureCollectionsTreeItem;
  
  @FXML
  private TreeItem mapContentsTreeItem;
  
  @FXML
  private TextField configLocation;
  
  private Config config;
  
  private App app;
  
  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }

  public void setApp(App app) {
    this.app = app;
  }
  
  public void openConfig() {
    TreeItem treeItem = (TreeItem) treeView.getSelectionModel().getSelectedItem();
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("geotoolsfx/fxml/ConfigEdit.fxml"));
    try {
      DockPane dockPane = fxmlLoader.load();
      ConfigEditController configController = fxmlLoader.getController();
      configController.setApp(app);
      configController.configAdded(config);
      dockPane.setDockManager(configDockPane.getDockManager());
      Object selectedItem = treeItem.getGraphic().getUserData();
      if (selectedItem != null) {
        if (selectedItem instanceof Config.DataStore) {
          Config.DataStore dataStore = (Config.DataStore)selectedItem;
          configController.setConfigDataStore(dataStore);
        } else if (selectedItem instanceof Config.FeatureCollection) {
          Config.FeatureCollection featureCollection = (Config.FeatureCollection)selectedItem;
          configController.setConfigFeatureCollection(featureCollection);
        } else if (selectedItem instanceof Config.MapContent) {
          Config.MapContent mapContent = (Config.MapContent)selectedItem;
          configController.setConfigMapContent(mapContent); 
        }
      }
      dockPane.setDockManager(configDockPane.getDockManager());
      dockPane.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public void setConfigLocation() {
    configLocation.getText();
  }
  
  @Override
  public void configMapContentAdded(MapContent mapContent) {
    Region region = new Region();
    region.setUserData(mapContent);
    TreeItem treeItem = new TreeItem(mapContent.title, region);
    mapContentsTreeItem.getChildren().add(treeItem);
  }

  @Override
  public void configFeatureCollectionAdded(FeatureCollection featureCollection) {
    Region region = new Region();
    region.setUserData(featureCollection);
    TreeItem treeItem = new TreeItem(featureCollection.title, region);
    featureCollectionsTreeItem.getChildren().add(treeItem);
  }

  @Override
  public void configDataStoreAdded(DataStore dataStore) {
    Region region = new Region();
    region.setUserData(dataStore);
    TreeItem treeItem = new TreeItem(dataStore.title, region);
    dataStoresTreeItem.getChildren().add(treeItem);
  }

  @Override
  public void configAdded(Config config) {
    this.config = config;
    for (Config.DataStore dataStore : config.dataStores) {
      configDataStoreAdded(dataStore);
    }
    
    for (Config.FeatureCollection featureCollection: config.featureCollections) {
      configFeatureCollectionAdded(featureCollection);
    }
    
    for (Config.MapContent mapContent : config.mapContents) {
      configMapContentAdded(mapContent);
    }
  }

}
