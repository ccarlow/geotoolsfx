package geotoolsfx.ui.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.util.factory.GeoTools;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;

import com.sun.javafx.scene.control.skin.VirtualFlow;

import geotoolsfx.ItemSelectableFeatureCollection;
import geotoolsfx.listener.FeatureCollectionListener;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.TableView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class FeatureCollectionController implements Initializable, FeatureCollectionListener {	
	@FXML
	private collectionitemselector.CollectionItemSelectorController collectionItemSelectorController;
	
	@FXML
	private VBox tableContainer;
	
	private TableView<SimpleFeature> tableView;
	
	private ItemSelectableFeatureCollection itemSelectableFeatureCollection;
	
	private List<String> invisibleFields = new ArrayList<String>();
	private boolean idVisible;
	private boolean ignoreFeatureCollectionsEvent = false;
	private FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		invisibleFields.add("the_geom");
	}
	
	public void setTableView() {
		ignoreFeatureCollectionsEvent = true;
		tableView = new TableView<SimpleFeature>();
		tableContainer.getChildren().clear();
		tableContainer.getChildren().add(tableView);
		VBox.setVgrow(tableView, Priority.ALWAYS);
		tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		tableView.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<Feature>() {
			@Override
			public void onChanged(Change<? extends Feature> c) {
				if (!ignoreFeatureCollectionsEvent) {
					while (c.next()) {
						ignoreFeatureCollectionsEvent = true;
						itemSelectableFeatureCollection.setSelected(tableView.getSelectionModel().getSelectedItems());
						ignoreFeatureCollectionsEvent = false;
					}	
				}
			}
		});
		
		tableView.setSortPolicy(new Callback<TableView<SimpleFeature>, Boolean>() {
			@Override
			public Boolean call(TableView<SimpleFeature> param) {
				if (!ignoreFeatureCollectionsEvent && itemSelectableFeatureCollection != null && !param.getColumns().isEmpty()) {
					List<SortBy> sortBy = new ArrayList<SortBy>();
					for (TableColumn<SimpleFeature, ? extends Object> column : param.getSortOrder()) {
						SortOrder sortOrder = SortOrder.ASCENDING;
						if (column.getSortType().equals(SortType.DESCENDING)) {
							sortOrder = SortOrder.DESCENDING;
						}
						sortBy.add(ff.sort(column.getText(), sortOrder));
					}
					itemSelectableFeatureCollection.setSortBy(sortBy.toArray(new SortBy[0]));
				}
				return true;
			}
		});		

		tableView.getColumns().clear();
		tableView.getItems().clear();
		SimpleFeatureCollection featureCollection = itemSelectableFeatureCollection.getFeatures(true);
		List<TableColumn<SimpleFeature, Object>> sortedColumns = new ArrayList<TableColumn<SimpleFeature, Object>>();
		for (PropertyDescriptor descriptor : featureCollection.getSchema().getDescriptors()) {
			if (!invisibleFields.contains(descriptor.getName().getLocalPart())) {
				TableColumn<SimpleFeature, Object> column = new TableColumn<SimpleFeature, Object>(descriptor.getName().getLocalPart());
				tableView.getColumns().add(column);
				Query query = itemSelectableFeatureCollection.getQuery();
				if (query != null && query.getSortBy() != null) {
					for (SortBy sortBy : query.getSortBy()) {
						if (sortBy.getPropertyName().getPropertyName().equals(column.getText())) {
							SortType sortType = SortType.ASCENDING;
							if (sortBy.getSortOrder().equals(SortOrder.DESCENDING)) {
								sortType = SortType.DESCENDING;
							}
							column.setSortType(sortType);
							sortedColumns.add(column);
							break;
						}	
					}
				}
				
				column.setCellValueFactory(new Callback<CellDataFeatures<SimpleFeature, Object>, ObservableValue<Object>>() {
					@Override
					public ObservableValue<Object> call(CellDataFeatures<SimpleFeature, Object> param) {
						return new SimpleObjectProperty<Object>(param.getValue().getProperty(column.getText()).getValue());
					}
					
				});	
			}
		}
		
		tableView.getSortOrder().addAll(sortedColumns);
		
		SimpleFeatureIterator features = featureCollection.features();
		try {
			while (features.hasNext()) {
				tableView.getItems().add(features.next());
			}	
		} finally {
			features.close();	
		}
		
		itemSelectableFeatureCollection.setCurrentIndex(1);
		
		ignoreFeatureCollectionsEvent = false;
	}
	
	public VirtualFlow<IndexedCell<Feature>> getVirtualFlow() {
		return (VirtualFlow)tableView.lookup(".virtual-flow");
	}
	
	public List<String> getInvisibleFields() {
		return invisibleFields;
	}
	
	public boolean isIdVisible() {
		return idVisible;
	}
	
	public void setIdVisible(boolean visible) {
		idVisible = visible;
	}

	public void setFeatureCollection(ItemSelectableFeatureCollection featureCollection) {
		this.itemSelectableFeatureCollection = featureCollection;
		featureCollection.addFeatureCollectionListener(this);
		collectionItemSelectorController.setCollectionItemSelector(featureCollection);
		setTableView();
	}

	@Override
	public void featuresSelected(ItemSelectableFeatureCollection featureCollection) {
		// TODO Auto-generated method stub
		if (!ignoreFeatureCollectionsEvent) {
			ignoreFeatureCollectionsEvent = true;
			tableView.getSelectionModel().clearSelection();
			for (SimpleFeature feature : itemSelectableFeatureCollection.getSelectedFeatures()) {
				tableView.getSelectionModel().select(feature);
			}
			ignoreFeatureCollectionsEvent = false;
		} else {
			System.out.println("feature selected from table");
		}
	}

	@Override
	public void queryChanged(ItemSelectableFeatureCollection featureCollection) {
		ignoreFeatureCollectionsEvent = true;
		setTableView();
		ignoreFeatureCollectionsEvent = false;
	}
}