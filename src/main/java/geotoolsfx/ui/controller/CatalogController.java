package geotoolsfx.ui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import geotoolsfx.Config;
import geotoolsfx.listener.DataStoresListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class CatalogController implements Initializable, DataStoresListener {
  @FXML
  private TreeView<Config.DataStore> treeView;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

  }

  @Override
  public void dataStoreAdded(Config.DataStore dataStore) {
    treeView.getRoot().getChildren().add(new TreeItem<Config.DataStore>(dataStore));
  }
}
