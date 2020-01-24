package geotoolsfx.ui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import org.geotools.data.Query;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.util.factory.GeoTools;
import org.opengis.filter.FilterFactory2;
import geotoolsfx.FeatureCollectionWrapper;
import geotoolsfx.Queries;
import geotoolsfx.listener.QueriesListener;
import javafx.fxml.Initializable;

public class QueriesController implements Initializable, QueriesListener {
	
	private FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
	}

	@Override
	public void setFeatureCollection(FeatureCollectionWrapper featureCollection) {
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