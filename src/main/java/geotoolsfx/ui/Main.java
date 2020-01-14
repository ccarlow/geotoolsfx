package geotoolsfx.ui;

import java.io.IOException;

import dock.DockManager;
import geotoolsfx.Config;
import geotoolsfx.ui.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class Main extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws IOException {
//		DockFX.getInstance().forceFloatingOnLoad(true);
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("geotoolsfx/fxml/Main.fxml"));
		fxmlLoader.load();
		MainController mainController = fxmlLoader.getController();
		mainController.addConfig("resources/config/geotoolsfx.xml");
		DockManager.getInstance().setLayoutConfigFile("resources/config/dockfx/geotoolsfx.xml");
		DockManager.getInstance().loadDockLayout();
	}
}
