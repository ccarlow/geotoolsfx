package geotoolsfx.ui.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import geotoolsfx.Config;
import geotoolsfx.listener.ConfigListener;
import geotoolsfx.listener.ConfigMapLayerListener;
import geotoolsfx.listener.StyledLayerDescriptorListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class ConfigEditMapLayerController implements Initializable, ConfigListener, ConfigMapLayerListener, StyledLayerDescriptorListener {
  @FXML
  private TextField title;
  
  @FXML
  private ComboBox featureCollection;
  
  @FXML
  private StyledLayerDescriptorController styledLayerDescriptorController;
  
  private Config config;
  private Config.Layer layer;
  
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    styledLayerDescriptorController.addListener(this);
  }

  @Override
  public void configAdded(Config config) {
    this.config = config;
  }
  
  @Override
  public void configMapLayerAdded(Config.Layer layer) {
    this.layer = layer;
    title.setText(layer.title);
    
    for (Config.FeatureCollection featureCollection : config.getFeatureCollections()) {
      this.featureCollection.getItems().add(featureCollection.title);
    }
    this.featureCollection.getSelectionModel().select(layer.featureCollection);
    
    try {
      URL url = new URL(layer.style.location);
      styledLayerDescriptorController.setSldFile(url.getPath());
      styledLayerDescriptorController.setStyleName(layer.style.name);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void styleSelected(String styleName) {
    layer.style.name = styleName;
  }
}
