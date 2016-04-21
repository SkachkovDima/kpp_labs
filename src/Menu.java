import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import javafx.util.Duration;

public class Menu extends Pane{
  static SubMenu menuBox;

  public Menu() {

    setVisible(false);
    MenuItem newGame = new MenuItem("NEW GAME");
    MenuItem autoGame = new MenuItem("AUTO GAME");
    MenuItem exitGame = new MenuItem("EXIT");
    SubMenu mainMenu = new SubMenu(
        newGame, autoGame, exitGame
    );
    MenuItem easy = new MenuItem("EASY");
    MenuItem medium = new MenuItem("MEDIUM");
    MenuItem hard = new MenuItem("HARD");
    MenuItem back = new MenuItem("BACK");
    SubMenu newGameMenu = new SubMenu(
        easy, medium, hard, back
    );
    Rectangle bg = new Rectangle(Game.BACKGROUND_SIZE_X,
        Game.BACKGROUND_SIZE_Y, Color.LIGHTGRAY);
    bg.setOpacity(0.6);
    menuBox = mainMenu;
    getChildren().addAll(bg, menuBox);
    newGame.setOnMouseClicked(event -> setSubMenu(newGameMenu));
    easy.setOnMouseClicked(event -> {
      Game.autoGameFlag = false;
      Game.levelNumber = 0;
      Game.changeLevel = true;
    });
    medium.setOnMouseClicked(event-> {
      Game.autoGameFlag = false;
      Game.levelNumber = 1;
      Game.changeLevel = true;
    });
    hard.setOnMouseClicked(event-> {
      Game.autoGameFlag = false;
      Game.levelNumber = 2;
      Game.changeLevel = true;
    });
    autoGame.setOnMouseClicked(event -> {
      Game.player.setTranslateX(Ninja.START_X);
      Game.player.setTranslateY(Ninja.START_Y);
      Game.gameRoot.setLayoutX(Ninja.START_X);
      FadeTransition ft = new FadeTransition(Duration.seconds(1), this);
      ft.setFromValue(1);
      ft.setToValue(0);
      ft.setOnFinished(evt -> this.setVisible(false));
      ft.play();
      Game.autoGameFlag = true;
    });
    exitGame.setOnMouseClicked(event -> System.exit(0));
    back.setOnMouseClicked(event -> setSubMenu(mainMenu));
  }

  private static class MenuItem extends StackPane {
    public MenuItem(String name) {
      Rectangle bg = new Rectangle(200, 20, Color.WHITE);
      bg.setOpacity(0.5);

      Text text = new Text(name);
      text.setFill(Color.WHITE);
      text.setFont(Font.font("Arial", FontWeight.BOLD, 14));

      setAlignment(Pos.CENTER);
      getChildren().addAll(bg, text);
      FillTransition st = new FillTransition(Duration.seconds(0.5), bg);
      setOnMouseEntered(event -> {
        st.setFromValue(Color.DARKGRAY);
        st.setToValue(Color.DARKGOLDENROD);
        st.setCycleCount(Animation.INDEFINITE);
        st.setAutoReverse(true);
        st.play();
      });
      setOnMouseExited(event -> {
        st.stop();
        bg.setFill(Color.WHITE);
      });
    }
  }

  public void setSubMenu(SubMenu subMenu) {
    getChildren().remove(menuBox);
    menuBox = subMenu;
    getChildren().add(menuBox);
  }

  private static class SubMenu extends VBox {
    public SubMenu(MenuItem... items) {
      setSpacing(15);
      setTranslateY(100);
      setTranslateX(50);
      for (MenuItem item : items) {
        getChildren().addAll(item);
      }
    }
  }
}
