package geotoolsfx.ui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.map.MapLayerEvent;
import org.geotools.map.MapLayerListEvent;
import org.geotools.map.MapLayerListListener;

import dock.DockPane;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class LayersController implements Initializable, MapLayerListListener {
	@FXML
	private TreeView<String> treeView;
	
	private MapContent mapContent;
	
	public static final String GRAPHIC_LAYER_PROPERTY = "layersControllerLayer";
	
	private boolean ignoreMapLayerListEvent = false;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

	public void setMapContent(MapContent mapContent) {
		this.mapContent = mapContent;
		for (Layer layer : mapContent.layers()) {
			addLayer(layer);
		}
		mapContent.addMapLayerListListener(this);
		
	}
	
	private void addLayer(Layer layer) {
		CheckBox checkBox = new CheckBox();
		checkBox.setSelected(layer.isVisible());
		checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!ignoreMapLayerListEvent) {
					ignoreMapLayerListEvent = true;
					layer.setVisible(newValue);	
				}
				ignoreMapLayerListEvent = false;
			}
		});
		checkBox.getProperties().put(GRAPHIC_LAYER_PROPERTY, layer);
		TreeItem<String> treeItem = new TreeItem<String>(layer.getTitle(), checkBox);
		treeView.getRoot().getChildren().add(treeItem);
	}
	
	public void removeLayer(Layer layer) {
		TreeItem<String> removedTreeItem = null;
		for (TreeItem<String> treeItem : treeView.getRoot().getChildren()) {
			if (layer == getLayerFromTreeItem(treeItem)) {
				removedTreeItem = treeItem;
				break;
			}
		}
		if (removedTreeItem != null) {
			treeView.getRoot().getChildren().remove(removedTreeItem);
		}
	}
	
	public void openTreeItem() {
		TreeItem<String> treeItem = treeView.getSelectionModel().getSelectedItem();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("geotoolsfx/fxml/Layer.fxml"));
		try {
			Node root = (Node)fxmlLoader.load();
			LayerController layerController = fxmlLoader.getController();
//			mediator.addLayerListener(layerController);
			Layer layer = getLayerFromTreeItem(treeItem);
			layerController.setLayer(layer);
			new DockPane(layer.getTitle(), root).show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Layer getLayerFromTreeItem(TreeItem item) {
		return (Layer)item.getGraphic().getProperties().get(GRAPHIC_LAYER_PROPERTY);
	}

	@Override
	public void layerAdded(MapLayerListEvent event) {
		// TODO Auto-generated method stub
		addLayer(event.getElement());
	}

	@Override
	public void layerRemoved(MapLayerListEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void layerChanged(MapLayerListEvent event) {
		// TODO Auto-generated method stub
		if (!ignoreMapLayerListEvent) {
			if (event.getMapLayerEvent().getReason() == MapLayerEvent.VISIBILITY_CHANGED) {
				for (TreeItem<String> treeItem : treeView.getRoot().getChildren()) {
					if (getLayerFromTreeItem(treeItem) == event.getElement()) {
						ignoreMapLayerListEvent = true;
						((CheckBox)treeItem.getGraphic()).setSelected(event.getElement().isVisible());
					}
				}
			}
		}
	}

	@Override
	public void layerMoved(MapLayerListEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void layerPreDispose(MapLayerListEvent event) {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public void mapContentAdded(MapContentsEvent event) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void mapContentChanged(MapContentsEvent event) {
//		treeView.getRoot().getChildren().clear();
//		if (event.getReason() == MapContentsEvent.SELECTION_CHANGED) {
//			for (Layer layer : event.getElement().layers()) {
//				Region region = new Region();
//				region.getProperties().put(GRAPHIC_LAYER_PROPERTY, layer);
//				TreeItem<String> treeItem = new TreeItem<String>(layer.getTitle(), region);
//				treeView.getRoot().getChildren().add(treeItem);
//			}
//		}
//	}
}