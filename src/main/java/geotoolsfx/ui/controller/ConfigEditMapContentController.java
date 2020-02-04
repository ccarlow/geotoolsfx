package geotoolsfx.ui.controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.TimeZone;
import org.geotools.data.DataAccessFactory.Param;
import dnddockfx.DockManager;
import dnddockfx.DockPane;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataStoreFinder;
import geotoolsfx.Config;
import geotoolsfx.Config.DataStore;
import geotoolsfx.Config.MapContent;
import geotoolsfx.Configs;
import geotoolsfx.listener.ConfigDataStoreListener;
import geotoolsfx.listener.ConfigListener;
import geotoolsfx.listener.ConfigMapContentListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class ConfigEditMapContentController implements Initializable, ConfigListener, ConfigMapContentListener {
  @FXML
  private TextField title;
  
//  @FXML
//  private TableView layerTableView;
  
  @FXML
  private TreeView layerTreeView;
  
  private Config config;
  private DockManager dockManager;
  
  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }
  
  public void openLayerConfig() {
    TreeItem treeItem = (TreeItem) layerTreeView.getSelectionModel().getSelectedItem();
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("geotoolsfx/fxml/ConfigEditMapLayer.fxml"));
    try {
      DockPane dockPane = fxmlLoader.load();
      ConfigEditMapLayerController configLayerController = fxmlLoader.getController();
      Config.Layer configLayer = (Config.Layer)treeItem.getGraphic().getUserData();
      configLayerController.configAdded(config);
      dockPane.setDockManager(dockManager);
      configLayerController.configMapLayerAdded(configLayer);
//      configController.setApp(app);
//      DockPane dockPane = new DockPane("Data Store Config", vbox);
      dockPane.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void configAdded(Config config) {
    this.config = config;
  }
  
  public void setDockManager(DockManager dockManager) {
    this.dockManager = dockManager;
  }
  
  @Override
  public void configMapContentAdded(MapContent mapContent) {
    for (Config.Layer layer : mapContent.layers) {
      Region region = new Region();
      region.setUserData(layer);
      TreeItem treeItem = new TreeItem(layer.title, region);
      layerTreeView.getRoot().getChildren().add(treeItem);
    }
  }
}
