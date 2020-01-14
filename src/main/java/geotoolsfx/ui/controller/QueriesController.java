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
import geotoolsfx.Queries;
import geotoolsfx.listener.FeatureCollectionListener;
import geotoolsfx.listener.QueriesListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

public class QueriesController implements Initializable, QueriesListener {
	
	private FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
	}

	@Override
	public void setFeatureCollection(ItemSelectableFeatureCollection featureCollection) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void queryAdded(Queries queries, Query query) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void queryRemoved(Queries queries, Query query) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void queryChanged(Queries queries, Query query) {
		// TODO Auto-generated method stub
		
	}
}