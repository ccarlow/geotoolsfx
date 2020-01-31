package geotoolsfx.ui;

import java.io.IOException;
import dnddockfx.DockManager;
import dnddockfx.DockPane;
import geotoolsfx.App;
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
    FXMLLoader fxmlLoader =
        new FXMLLoader(getClass().getClassLoader().getResource("geotoolsfx/fxml/Main.fxml"));
    DockPane dockPane = fxmlLoader.load();
    MainController mainController = fxmlLoader.getController();
    
    App app = new App();
    app.addConfig("resources/config/geotoolsfx.xml");
    mainController.setApp(app);
    app.getConfigs().notifyAllConfigsAdded();
    
    DockManager dockManager = new DockManager();
    dockManager.addDockPane(dockPane);
    dockManager.setLayoutConfigFile("resources/config/dockfx/geotoolsfx.xml");
    dockManager.loadDockLayout();
  }
}
