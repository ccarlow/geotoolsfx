package geotoolsfx.ui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import org.geotools.data.DataStore;
import geotoolsfx.App;
import geotoolsfx.Config;
import geotoolsfx.Config.FeatureCollection;
import geotoolsfx.listener.ConfigFeatureCollectionListener;
import geotoolsfx.listener.ConfigListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class ConfigEditFeatureCollectionController implements Initializable, ConfigFeatureCollectionListener, ConfigListener {
  @FXML
  private TextField title;
  
  @FXML
  private ComboBox<String> dataStore;
  
  @FXML
  private ComboBox<String> typeName;
  
  private Config config;
  private Config.FeatureCollection featureCollection;
  private App app;
  
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    dataStore.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        typeName.getItems().clear();
        
        DataStore dataStore = app.getDataStores().getDataStore(newValue);
        if (dataStore != null) {
          try {
            typeName.getItems().addAll(dataStore.getTypeNames());
          } catch (IOException e) {
            e.printStackTrace();
          }
          typeName.getSelectionModel().select(featureCollection.typeName);
        }
      }
    });
  }

  public void setApp(App app) {
    this.app = app;
  }
  
  @Override
  public void configFeatureCollectionAdded(FeatureCollection featureCollection) {
    this.featureCollection = featureCollection;
    this.title.setText(featureCollection.title);
    this.dataStore.getSelectionModel().select(featureCollection.dataStore);
  }

  @Override
  public void configAdded(Config config) {
    for (Config.DataStore dataStore : config.dataStores) {
      this.dataStore.getItems().add(dataStore.title);
    }
  }
}
