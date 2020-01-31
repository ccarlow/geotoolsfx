package geotoolsfx.ui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import org.geotools.map.Layer;
import org.geotools.map.MapLayerEvent;
import org.geotools.map.MapLayerListener;
import geotoolsfx.Config;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;

public class LayerController implements Initializable, MapLayerListener {
  @FXML
  private TreeView<Config.MapContent> treeView;

  @FXML
  private CheckBox isVisibleCheckBox;

  @FXML
  private TextField titleTextField;

  private boolean ignoreMapLayerEvent = false;

  public Layer layer;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    isVisibleCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
      @Override
      public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
          Boolean newValue) {
        if (!ignoreMapLayerEvent) {
          ignoreMapLayerEvent = true;
          layer.setVisible(newValue);
        }
        ignoreMapLayerEvent = false;
      }
    });
  }

  public void setLayer(Layer layer) {
    this.layer = layer;
    layer.addMapLayerListener(this);
    titleTextField.setText(layer.getTitle());
    isVisibleCheckBox.setSelected(layer.isVisible());
  }

  @Override
  public void layerChanged(MapLayerEvent event) {

  }

  @Override
  public void layerShown(MapLayerEvent event) {
    if (!ignoreMapLayerEvent) {
      ignoreMapLayerEvent = true;
      isVisibleCheckBox.setSelected(layer.isSelected());
    }
  }

  @Override
  public void layerHidden(MapLayerEvent event) {
    if (!ignoreMapLayerEvent) {
      ignoreMapLayerEvent = true;
      isVisibleCheckBox.setSelected(layer.isSelected());
    }
  }

  @Override
  public void layerSelected(MapLayerEvent event) {
    // TODO Auto-generated method stub

  }

  @Override
  public void layerDeselected(MapLayerEvent event) {
    // TODO Auto-generated method stub

  }

  @Override
  public void layerPreDispose(MapLayerEvent event) {
    // TODO Auto-generated method stub

  }
}
