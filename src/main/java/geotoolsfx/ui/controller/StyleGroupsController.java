package geotoolsfx.ui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.geotools.styling.Style;

import dock.DockPane;
import geotoolsfx.StyleGroup;
import geotoolsfx.listener.StyleGroupListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class StyleGroupsController implements Initializable, StyleGroupListener {
	@FXML
	private TreeView<Style> treeView;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

	public void openTreeItem() {
		TreeItem<Style> treeItem = treeView.getSelectionModel().getSelectedItem();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("geotoolsfx/fxml/Style.fxml"));
		try {
			Node root = (Node)fxmlLoader.load();
			StyleController styleController = fxmlLoader.getController();
//			mediator.addStyleListener(styleController);
			styleController.setStyle(treeItem.getValue());
			DockPane dockPane = new DockPane();
			dockPane.setTitle(treeItem.getValue().getName());
			dockPane.setContent(root);
			dockPane.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void styleGroupAdded(StyleGroup styleGroup) {
		
	}
}