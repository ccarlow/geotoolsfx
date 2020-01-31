package geotoolsfx.ui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import org.geotools.styling.Style;
import org.geotools.styling.Symbolizer;
import geotoolsfx.listener.StylesListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeView;

public class StyleSymbolizerController implements Initializable {

  private Symbolizer symbolizer;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

  }

  public void setSymbolizer(Symbolizer symbolizer) {}
}
