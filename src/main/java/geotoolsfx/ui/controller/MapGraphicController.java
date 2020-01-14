package geotoolsfx.ui.controller;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;

import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapBoundsEvent;
import org.geotools.map.MapBoundsListener;
import org.geotools.map.MapContent;
import org.geotools.map.MapLayerEvent;
import org.geotools.map.MapLayerListEvent;
import org.geotools.map.MapLayerListListener;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.swing.tool.AbstractZoomTool;
import org.jfree.fx.FXGraphics2D;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import geotoolsfx.Config;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class MapGraphicController implements Initializable, MapLayerListListener, MapBoundsListener {
	@FXML
	private TreeView<Config.MapContent> treeView;
	
	@FXML
	private Rectangle zoomRectangle;
	
	@FXML
	private VBox mapGraphic;
	
	@FXML
	private ImageView mapImageView;
	
	private FXGraphics2D graphics;
	private StreamingRenderer renderer;
	private MapContent mapContent;
	private static final String ZOOM_IN_MODE = "zoomIn";
	private static final String ZOOM_OUT_MODE = "zoomOut";
	private static final String PAN_MODE = "pan";
	private java.awt.Rectangle paintArea;
	
	private String toolMode;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		renderer = new StreamingRenderer();
		paintArea = new java.awt.Rectangle((int) mapGraphic.getWidth(), (int) mapGraphic.getHeight());
		
		mapGraphic.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				setMapGraphics();
			}
		});
		
		mapGraphic.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				setMapGraphics();
			}
		});
	}
	
	public void setMapContent(MapContent mapContent) {
		this.mapContent = mapContent;
		renderer.setMapContent(mapContent);
		mapContent.getViewport().setMatchingAspectRatio(true);
		mapContent.addMapLayerListListener(this);
		mapContent.addMapBoundsListener(this);
	}
	
	public void setFullExtent() {
		mapContent.getViewport().setBounds(mapContent.getMaxBounds());
	}

	public void setMapGraphics() {
		if (mapGraphic.getWidth() > 0 && mapGraphic.getHeight() > 0) {
			java.awt.Rectangle rect = new java.awt.Rectangle(0, 0, (int)mapGraphic.getWidth(), (int)mapGraphic.getHeight());
			mapContent.getViewport().setScreenArea(rect);
			paintArea.setBounds(0, 0, (int)mapGraphic.getWidth(), (int)mapGraphic.getHeight());
			BufferedImage bufferedImage = new BufferedImage((int)mapGraphic.getWidth(), (int)mapGraphic.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D gr = bufferedImage.createGraphics();
			renderer.paint(gr, paintArea, mapContent.getViewport().getBounds());

			Image image = SwingFXUtils.toFXImage(bufferedImage, null);
			BackgroundImage myBI= new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
			mapGraphic.setBackground(new Background(myBI));	
		}
	}
	
	public void mapGraphicMouseClicked(MouseEvent event) {
		if (ZOOM_OUT_MODE.equals(toolMode)) {
			DirectPosition2D mapPos = getWorldPos(event.getX(), event.getY());

	        double scale = mapContent.getViewport().getWorldToScreen().getScaleX();
	        double newScale = scale / AbstractZoomTool.DEFAULT_ZOOM_FACTOR;

	        DirectPosition2D corner =
	                new DirectPosition2D(
	                        mapPos.getX() - 0.5d * mapGraphic.getWidth() / newScale,
	                        mapPos.getY() + 0.5d * mapGraphic.getHeight() / newScale);

	        Envelope2D newMapArea = new Envelope2D();
	        newMapArea.setFrameFromCenter(mapPos, corner);
	        
	        
	        CoordinateReferenceSystem crs = newMapArea.getCoordinateReferenceSystem();
            if (crs == null) {
                // assume that it is the current CRS
                crs = mapContent.getCoordinateReferenceSystem();
            }

            ReferencedEnvelope refEnv =
                    new ReferencedEnvelope(
                    		newMapArea.getMinimum(0),
                    		newMapArea.getMaximum(0),
                    		newMapArea.getMinimum(1),
                    		newMapArea.getMaximum(1),
                            crs);

            mapContent.getViewport().setBounds(refEnv);
		}
	}
	
	public void mapGraphicMousePressed(MouseEvent event) {
		if (PAN_MODE.equals(toolMode)) {
			zoomRectangle.setX(event.getX());
			zoomRectangle.setY(event.getY());
			mapImageView.setImage(mapGraphic.getBackground().getImages().get(0).getImage());
			mapImageView.setFitWidth(mapGraphic.getWidth());
			mapImageView.setFitHeight(mapGraphic.getHeight());
			mapImageView.setX(0);
			mapGraphic.setStyle("-fx-border-width:1px; -fx-border-color:black;"); 
			mapGraphic.setBackground(null);
			mapImageView.setY(0);
			mapImageView.setStyle("-fx-border-color:black; -fx-border-width:5");
			mapImageView.setVisible(true);
			mapImageView.setClip(new Rectangle(0, 0, mapGraphic.getWidth(), mapGraphic.getHeight()));
		} else if (ZOOM_IN_MODE.equals(toolMode)) {
			zoomRectangle.setX(event.getX());
			zoomRectangle.setY(event.getY());
			zoomRectangle.setVisible(true);		
		}
    }
	
	public void mapGraphicMouseDragged(MouseEvent event) {
		if (PAN_MODE.equals(toolMode)) {
			double xDiff = event.getX() - zoomRectangle.getX();
			double yDiff = event.getY() - zoomRectangle.getY();
			mapImageView.setX(xDiff);
			mapImageView.setY(yDiff);
		} else if (ZOOM_IN_MODE.equals(toolMode)) {
			zoomRectangle.setWidth(event.getX() - zoomRectangle.getX());
			zoomRectangle.setHeight(event.getY() - zoomRectangle.getY());
		}
    }
	
	public void mapGraphicMouseReleased(MouseEvent event) {
		if (PAN_MODE.equals(toolMode)) {
			double xDiff = event.getX() - zoomRectangle.getX();
			double yDiff = event.getY() - zoomRectangle.getY();

			DirectPosition2D newPos = new DirectPosition2D(xDiff, yDiff);
			mapContent.getViewport().getScreenToWorld().transform(newPos, newPos);

			ReferencedEnvelope env = new ReferencedEnvelope(mapContent.getViewport().getBounds(), mapContent.getViewport().getCoordinateReferenceSystem());
			env.translate(env.getMinimum(0) - newPos.x, env.getMaximum(1) - newPos.y);

			mapContent.getViewport().setBounds(env);
			mapImageView.setVisible(false);
		} else if (ZOOM_IN_MODE.equals(toolMode)) {
			DirectPosition2D rectPos = getWorldPos(zoomRectangle.getX(), zoomRectangle.getY());
			DirectPosition2D pos = getWorldPos(event.getX(), event.getY());

			Envelope2D env = new Envelope2D();
			env.setFrameFromDiagonal(rectPos, pos);

			mapContent.getViewport().setBounds(new ReferencedEnvelope(env, mapContent.getViewport().getCoordinateReferenceSystem()));

			zoomRectangle.setVisible(false);
			zoomRectangle.setWidth(0);
			zoomRectangle.setHeight(0);
		}
    }
	
	public DirectPosition2D getWorldPos(double x, double y) {
		AffineTransform tr = mapContent.getViewport().getScreenToWorld();
        DirectPosition2D pos = new DirectPosition2D(x, y);
        if (tr != null) tr.transform(pos, pos);
        pos.setCoordinateReferenceSystem(mapContent.getCoordinateReferenceSystem());
        return pos;
	}
	
	public void handleFullExtentButton() {
		setFullExtent();
	}
	
	public void handleZoomInButton() {
		toolMode = ZOOM_IN_MODE;
	}
	
	public void handleZoomOutButton() {
		toolMode = ZOOM_OUT_MODE;
	}
	
	public void handlePanButton() {
		toolMode = PAN_MODE;
	}
	
	public MapContent getMapContent() {
		return mapContent;
	}
	
	@Override
	public void layerAdded(MapLayerListEvent event) {
		setMapGraphics();
	}

	@Override
	public void layerRemoved(MapLayerListEvent event) {
		setMapGraphics();	
	}

	@Override
	public void layerChanged(MapLayerListEvent event) {
		setMapGraphics();
	}

	@Override
	public void layerMoved(MapLayerListEvent event) {
		setMapGraphics();
	}

	@Override
	public void layerPreDispose(MapLayerListEvent event) {
		
	}

	@Override
	public void mapBoundsChanged(MapBoundsEvent event) {
		setMapGraphics();
	}
}