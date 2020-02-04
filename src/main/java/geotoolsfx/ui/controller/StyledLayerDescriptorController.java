package geotoolsfx.ui.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.xml.styling.SLDParser;
import geotoolsfx.listener.StyledLayerDescriptorListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class StyledLayerDescriptorController implements Initializable {
  @FXML
  private ListView<String> listView;
  
  @FXML
  private TextField sldFile;
  
  private StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
  private List<StyledLayerDescriptorListener> listeners = new ArrayList<StyledLayerDescriptorListener>();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    sldFile.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        listView.getItems().clear();
        
        File file = new File(newValue);
        if (file.exists()) {
          try {
            SLDParser sldParser = new SLDParser(styleFactory, file);
            Style[] styles = sldParser.readXML();
            for (Style style : styles) {
              listView.getItems().add(style.getName());
            } 
          } catch (FileNotFoundException e) {
            e.printStackTrace();
          }
        }
      }
    });
    
    listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (newValue != null) {
          for (StyledLayerDescriptorListener listener : listeners) {
            listener.styleSelected(newValue);
          } 
        }
      }
    });
  }
  
  public void addListener(StyledLayerDescriptorListener listener) {
    listeners.add(listener);
  }
  
  public void openStyle() {
    
  }
  
  public void setSldFile(String sldFile) {
    this.sldFile.setText(sldFile);
  }
  
  public void setStyleName(String styleName) {
    listView.getSelectionModel().select(styleName);
  }
}
