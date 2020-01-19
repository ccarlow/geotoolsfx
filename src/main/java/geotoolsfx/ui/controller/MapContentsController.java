package geotoolsfx.ui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

import org.geotools.map.MapContent;
import dnddockfx.DockPane;
import geotoolsfx.MapContents;
import geotoolsfx.listener.MapContentsListener;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Region;

public class MapContentsController implements Initializable, MapContentsListener {
	@FXML
	private TreeView<String> treeView;
	
	@FXML
	private DockPane mapContentsDockPane;
	
	private MapContents mapContents;
	
	private Boolean ignoreMapContentsEvents = false;
	
	public static final String GRAPHIC_MAPCONTENT_PROPERTY = "mapContentsControllerMapContent";
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		treeView.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<TreeItem<String>>() {
			@Override
			public void onChanged(Change<? extends TreeItem<String>> c) {
				while (c.next()) {
					for (TreeItem<String> treeItem : c.getAddedSubList()) {
						ignoreMapContentsEvents = true;
						mapContents.setSelected(getMapContentFromTreeItem(treeItem), true);	
						ignoreMapContentsEvents = false;
					}
				}
			}
		});
	}
	
	public void setMapContents(MapContents mapContents) {
		this.mapContents = mapContents;
	}
	
	public void openMapGraphic() {
		TreeItem<String> treeItem = treeView.getSelectionModel().getSelectedItem();
		MapContent mapContent = getMapContentFromTreeItem(treeItem);
		DockPane dockPane = getMapGraphicDockPane(mapContent);
		dockPane.setId(UUID.randomUUID().toString());
		mapContentsDockPane.getDockPanes().add(dockPane);
		mapContentsDockPane.getDockManager().addDockPane(dockPane);
		dockPane.show();
	}
	
	public static DockPane getMapGraphicDockPane(MapContent mapContent) {
		DockPane dockPane = null;
		FXMLLoader fxmlLoader = new FXMLLoader(MapContentsController.class.getClassLoader().getResource("geotoolsfx/fxml/MapGraphic.fxml"));
		try {
			Node root = (Node)fxmlLoader.load();
			MapGraphicController mapGraphicController = fxmlLoader.getController();
			mapGraphicController.setMapContent(mapContent);
			dockPane = new DockPane(mapContent.getTitle(), root);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dockPane;
	}
	
	private MapContent getMapContentFromTreeItem(TreeItem<String> treeItem) {
		return (MapContent)treeItem.getGraphic().getProperties().get(GRAPHIC_MAPCONTENT_PROPERTY);
	}
	
	public void openMapLayers() {
		TreeItem<String> treeItem = treeView.getSelectionModel().getSelectedItem();
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("geotoolsfx/fxml/Layers.fxml"));
		try {
			Node root = (Node)fxmlLoader.load();
			LayersController layersController = fxmlLoader.getController();
			MapContent mapContent = getMapContentFromTreeItem(treeItem);
			layersController.setMapContent(mapContent);
			new DockPane(mapContent.getTitle() + " Layers", root).show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void mapContentAdded(MapContent mapContent) {
		Region region = new Region();
		region.getProperties().put(GRAPHIC_MAPCONTENT_PROPERTY, mapContent);
		treeView.getRoot().getChildren().add(new TreeItem<String>(mapContent.getTitle(), region));
	}

	@Override
	public void mapContentChanged(MapContent mapContent) {
		
	}
}