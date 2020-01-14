package geotoolsfx.ui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.geotools.styling.Style;

import geotoolsfx.listener.StylesListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeView;

public class StyleController implements Initializable, StylesListener {
	@FXML
	private TreeView<Style> treeView;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

	public void setStyle(Style style) {
	}

	@Override
	public void styleAdded(Style layer) {
	}
}