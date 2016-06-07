package sjumper;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import sjumper.Game;
import sjumper.Menu;

public class SJumper extends Application{

  static Scene scene;

  @Override
  public void start(Stage primaryStage) throws Exception {
    Menu menu = new Menu();

    scene = new Scene(Menu.menuRoot, Game.SCENE_SIZE, Game.BACKGROUND_SIZE_Y);
    scene.setOnKeyPressed(event -> {
      if(event.getCode() == KeyCode.ESCAPE){
        if(scene.getRoot() == Game.gameRoot) {
          Game.pause = true;
          menu.setSubMenu(Menu.mainMenu);
          scene.setRoot(Menu.menuRoot);
        } else{
          Game.pause = false;
          menu.setSubMenu(Menu.mainMenu);
          scene.setRoot(Game.gameRoot);
        }
      } else{
        Game.keys.put(event.getCode(), true);
      }
    });

    scene.setOnKeyReleased(event -> {
      Game.keys.put(event.getCode(), false);
      Game.ninja.animation.stop();
    });

    primaryStage.setTitle("SJumper");
    primaryStage.setScene(scene);
    primaryStage.show();

    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
