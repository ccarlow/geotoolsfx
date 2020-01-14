package geotoolsfx.ui.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.geotools.data.Query;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.util.factory.GeoTools;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;

import geotoolsfx.ItemSelectableFeatureCollection;
import geotoolsfx.listener.FeatureCollectionListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

public class QueryController implements Initializable, FeatureCollectionListener {
	@FXML
	private TextArea filterTextArea;
	
	@FXML
	private TextArea sortByTextArea;
	
	private FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
	
	private ItemSelectableFeatureCollection featureCollection;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
	}
	
	public SortBy[] getSortByFromText() {
		List<SortBy> sortBy = new ArrayList<SortBy>();
		for (String sortByText : sortByTextArea.getText().split(",")) {
			if (!sortByText.trim().isEmpty()) {
				String[] sortByTextParts = sortByText.trim().split("\\s");
				SortOrder sortOrder = SortOrder.ASCENDING;
				if (sortByTextParts.length > 1) {
					
				}
				sortBy.add(ff.sort(sortByTextParts[0], sortOrder));	
			}
		}
		return sortBy.toArray(new SortBy[0]);
	}
	
	public void submit() {
		Query query = new Query();
		try {
			query.setFilter(CQL.toFilter(filterTextArea.getText()));
		} catch (CQLException e) {
			e.printStackTrace();
		}
		query.setSortBy(getSortByFromText());
		featureCollection.setQuery(query);
	}

	public void setFeatureCollection(ItemSelectableFeatureCollection featureCollection) {
		this.featureCollection = featureCollection;
	}
	
	@Override
	public void featuresSelected(ItemSelectableFeatureCollection featureCollection) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void queryChanged(ItemSelectableFeatureCollection featureCollection) {
		// TODO Auto-generated method stub
		
	}
}