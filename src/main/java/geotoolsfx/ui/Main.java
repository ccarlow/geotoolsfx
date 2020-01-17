package geotoolsfx.ui;

import java.io.IOException;

import dock.DockManager;
import dock.DockPane;
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
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("geotoolsfx/fxml/Main.fxml"));
		DockPane dockPane = fxmlLoader.load();
		MainController mainController = fxmlLoader.getController();
		mainController.addConfig("resources/config/geotoolsfx.xml");
		DockManager dockManager = new DockManager();
		dockManager.addDockPane(dockPane);
		dockManager.setLayoutConfigFile("resources/config/dockfx/geotoolsfx.xml");
		dockManager.loadDockLayout();
	}
}
