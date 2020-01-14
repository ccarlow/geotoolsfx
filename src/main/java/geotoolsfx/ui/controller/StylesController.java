package geotoolsfx.ui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.geotools.styling.Style;
import org.opengis.feature.Feature;

import dock.DockPane;
import geotoolsfx.Config.FeatureCollection;
import geotoolsfx.listener.FeatureCollectionsListener;
import geotoolsfx.listener.StylesListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Region;

public class StylesController implements Initializable, StylesListener {
	@FXML
	private TreeView<String> treeView;
	
	public static final String GRAPHIC_STYLE_PROPERTY = "stylesControllerStyle";
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

	public void openTreeItem() {
		TreeItem<String> treeItem = treeView.getSelectionModel().getSelectedItem();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("geotoolsfx/fxml/Style.fxml"));
		try {
			Node root = (Node)fxmlLoader.load();
			StyleController styleController = fxmlLoader.getController();
//			mediator.addStyleListener(styleController);
			Style style = (Style)treeItem.getGraphic().getUserData();
			styleController.setStyle(style);
			DockPane dockPane = new DockPane();
			dockPane.setTitle(treeItem.getValue() + " Style");
			dockPane.setContent(root);
			dockPane.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void styleAdded(Style style) {
		Region region = new Region();
		region.getProperties().put(GRAPHIC_STYLE_PROPERTY, style);
		TreeItem<String> treeItem = new TreeItem<String>(style.getName(), region);
		treeView.getRoot().getChildren().add(treeItem);
	}
}