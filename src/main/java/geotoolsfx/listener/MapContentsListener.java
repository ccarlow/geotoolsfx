package geotoolsfx.listener;

import java.util.EventListener;
import org.geotools.map.MapContent;

public interface MapContentsListener extends EventListener {
	public void mapContentAdded(MapContent mapContent);
	public void mapContentChanged(MapContent mapContent);
}