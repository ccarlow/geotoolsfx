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
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class ConfigsController implements Initializable, ConfigListener {
  @FXML
  private DockPane configsDockPane;
  
  @FXML
  private TreeView treeView;
  
  @FXML
  private TreeItem dataStoresTreeItem;
  
  @FXML
  private TreeItem featureCollectionsTreeItem;
  
  @FXML
  private TreeItem mapContentsTreeItem;
  
  private Configs configs;
  
  private App app;
  
  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }

  public void setApp(App app) {
    this.app = app;
  }
  
  public void openConfig() {
    TreeItem treeItem = (TreeItem) treeView.getSelectionModel().getSelectedItem();
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("geotoolsfx/fxml/Config.fxml"));
    try {
      DockPane dockPane = fxmlLoader.load();
      ConfigController configController = fxmlLoader.getController();
      Config config = (Config)treeItem.getGraphic().getUserData();
      configController.configAdded(config);
      configController.setApp(app);
//      DockPane dockPane = new DockPane("Data Store Config", vbox);
      dockPane.setDockManager(configsDockPane.getDockManager());
      dockPane.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void configAdded(Config config) {
    Region region = new Region();
    region.setUserData(config);
    TreeItem treeItem = new TreeItem(config.getId(), region);
    treeView.getRoot().getChildren().add(treeItem);
  }
}
