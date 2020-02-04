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
import dnddockfx.DockPane;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataStoreFinder;
import geotoolsfx.Config;
import geotoolsfx.Config.DataStore;
import geotoolsfx.Configs;
import geotoolsfx.listener.ConfigDataStoreListener;
import geotoolsfx.listener.ConfigListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ConfigEditDataStoreController implements Initializable, ConfigDataStoreListener {
  @FXML
  private TextField title;
  
  @FXML
  private ComboBox type;
  
  @FXML
  private GridPane paramsGridPane;
  
  private Config config;
  
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    for (Iterator<DataStoreFactorySpi> i = DataStoreFinder.getAvailableDataStores(); i
        .hasNext();) {
      DataStoreFactorySpi factory = (DataStoreFactorySpi) i.next();
      type.getItems().add(factory.getDisplayName());
    }
  }

  @Override
  public void configDataStoreAdded(DataStore dataStore) {
    title.setText(dataStore.title);

    for (Iterator<DataStoreFactorySpi> i = DataStoreFinder.getAvailableDataStores(); i.hasNext();) {
      DataStoreFactorySpi factory = (DataStoreFactorySpi) i.next();
      if (dataStore != null) {
        if (factory.canProcess((Map) dataStore.params)) {
          type.getSelectionModel().select(factory.getDisplayName());
          
          int rowIndex = 0;
          for (Param param : factory.getParametersInfo()) {
            paramsGridPane.add(new Label(param.getName()), 0, rowIndex);
            String value = dataStore.params.get(param.getName());
            if (value == null && param.getDefaultValue() != null) {
              value = param.getDefaultValue().toString();
            }
            Node node = null;
            System.out.println(param.getName() + " = " + param.getType().getName());
            if (param.getType().equals(TimeZone.class)) {
              node = new ComboBox();
              for (String id : TimeZone.getAvailableIDs()) {
                ((ComboBox)node).getItems().add(id);
              } 
              
              if (param.getDefaultValue() instanceof TimeZone) {
                value = ((TimeZone)param.getDefaultValue()).getID();
              }
            } else if (param.getType().equals(Charset.class)) {
              node = new ComboBox();
              for (Entry<String, Charset> entry : Charset.availableCharsets().entrySet()) {
                ((ComboBox)node).getItems().add(entry.getKey());
              }
            } else if (param.getType().equals(Boolean.class)) {
              node = new ComboBox();
              ((ComboBox)node).getItems().add(Boolean.TRUE);
              ((ComboBox)node).getItems().add(Boolean.FALSE);
            } else if (param.getType().equals(URL.class)) {
              node = new HBox();
              try {
                URL url = new URL(value);
                System.out.println(url.getProtocol());
                
                ComboBox protocols = new ComboBox();
                ((HBox)node).getChildren().add(protocols);
                protocols.getItems().add("file");
                protocols.getSelectionModel().select(url.getProtocol());
                TextField textField = new TextField();
                ((HBox)node).getChildren().add(textField);
                textField.setText(url.getPath());
              } catch (MalformedURLException e) {
                e.printStackTrace();
              }
            } else {
              node = new TextField(value);
            }
            
            if (node instanceof ComboBox) {
              ((ComboBox)node).getSelectionModel().select(value);
            }
            dataStore.params.put(param.getName(), value);
            paramsGridPane.add(node, 1, rowIndex++);
          }
          break;
        }
      }
    }
  }
}
