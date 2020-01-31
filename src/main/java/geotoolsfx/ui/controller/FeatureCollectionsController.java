package geotoolsfx.ui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import dnddockfx.DockPane;
import geotoolsfx.FeatureCollectionWrapper;
import geotoolsfx.FeatureCollections;
import geotoolsfx.listener.FeatureCollectionsListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class FeatureCollectionsController implements Initializable, FeatureCollectionsListener {
  @FXML
  private TreeView<String> treeView;

  @FXML
  private DockPane featureCollectionsDockPane;

  private FeatureCollections featureCollections;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

  }

  public void setFeatureCollections(FeatureCollections featureCollections) {
    featureCollections.addListener(this);
    this.featureCollections = featureCollections;
  }

  public void openFeatures() {
    TreeItem<String> treeItem = treeView.getSelectionModel().getSelectedItem();
    FXMLLoader fxmlLoader = new FXMLLoader(
        getClass().getClassLoader().getResource("geotoolsfx/fxml/FeatureCollection.fxml"));
    try {
      Node root = (Node) fxmlLoader.load();
      FeatureCollectionController controller = fxmlLoader.getController();
      FeatureCollectionWrapper featureCollection =
          featureCollections.getByTitle(treeItem.getValue());
      controller.setFeatureCollection(featureCollection);
      DockPane dockPane = new DockPane(treeItem.getValue(), root);
      dockPane.setDockManager(featureCollectionsDockPane.getDockManager());
      dockPane.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void openQuery() {
    TreeItem<String> treeItem = treeView.getSelectionModel().getSelectedItem();
    FXMLLoader fxmlLoader =
        new FXMLLoader(getClass().getClassLoader().getResource("geotoolsfx/fxml/Query.fxml"));
    try {
      Node root = (Node) fxmlLoader.load();
      QueryController controller = fxmlLoader.getController();
      FeatureCollectionWrapper featureCollection =
          featureCollections.getByTitle(treeItem.getValue());
      controller.setFeatureCollection(featureCollection);
      new DockPane(treeItem.getValue(), root).show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void openQueries() {
    TreeItem<String> treeItem = treeView.getSelectionModel().getSelectedItem();
    FXMLLoader fxmlLoader =
        new FXMLLoader(getClass().getClassLoader().getResource("geotoolsfx/fxml/Queries.fxml"));
    try {
      Node root = (Node) fxmlLoader.load();
      QueriesController controller = fxmlLoader.getController();
      FeatureCollectionWrapper featureCollection =
          featureCollections.getByTitle(treeItem.getValue());
      controller.setFeatureCollection(featureCollection);
      new DockPane(treeItem.getValue(), root).show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void openFilters() {
    TreeItem<String> treeItem = treeView.getSelectionModel().getSelectedItem();
    FXMLLoader fxmlLoader =
        new FXMLLoader(getClass().getClassLoader().getResource("geotoolsfx/fxml/Filters.fxml"));
    try {
      Node root = (Node) fxmlLoader.load();
      FiltersController controller = fxmlLoader.getController();
      FeatureCollectionWrapper featureCollection =
          featureCollections.getByTitle(treeItem.getValue());
      controller.addFeatureCollection(featureCollection);
      new DockPane(treeItem.getValue(), root).show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void openSortBys() {
    TreeItem<String> treeItem = treeView.getSelectionModel().getSelectedItem();
    FXMLLoader fxmlLoader =
        new FXMLLoader(getClass().getClassLoader().getResource("geotoolsfx/fxml/SortBys.fxml"));
    try {
      Node root = (Node) fxmlLoader.load();
      SortBysController controller = fxmlLoader.getController();
      FeatureCollectionWrapper featureCollection =
          featureCollections.getByTitle(treeItem.getValue());
      controller.setFeatureCollection(featureCollection);
      new DockPane(treeItem.getValue(), root).show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void featureCollectionAdded(FeatureCollections featureCollections,
      FeatureCollectionWrapper featureCollection) {
    treeView.getRoot().getChildren().add(new TreeItem<String>(featureCollection.getTitle()));
  }
}
