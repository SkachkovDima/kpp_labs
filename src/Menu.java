import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.io.IOException;
import java.nio.file.*;


public class Menu extends Pane{
  static SubMenu menuBox, mainMenu;
  ListView<String> listView;
  ObservableList<String> fileNames;

  public Menu() {

    setVisible(true);
    MenuItem newGame = new MenuItem("NEW GAME");
    MenuItem autoGame = new MenuItem("AUTO GAME");
    MenuItem replay = new MenuItem("REPLAY");
    MenuItem exitGame = new MenuItem("EXIT");
    mainMenu = new SubMenu(
        newGame, autoGame, replay, exitGame
    );
    MenuItem easy = new MenuItem("EASY");
    MenuItem medium = new MenuItem("MEDIUM");
    MenuItem hard = new MenuItem("HARD");
    MenuItem backNewGame = new MenuItem("BACK");
    SubMenu newGameMenu = new SubMenu(easy, medium, hard, backNewGame);
    MenuItem save = new MenuItem("SAVE");
    MenuItem play = new MenuItem("PlAY");
    MenuItem backReplay = new MenuItem("BACK");
    SubMenu replayMenu = new SubMenu(save, play, backReplay);


    createListView();
    clickedOnListViewItem();

    Rectangle bg = new Rectangle(Game.SCENE_SIZE,
        Game.BACKGROUND_SIZE_Y, Color.BLACK);
    bg.setOpacity(0.6);
    menuBox = mainMenu;
    this.getChildren().addAll(bg, menuBox, listView);

    saveButtonClicked(save);
    playButtonClicked(play);
    replayButtonClicked(replay, replayMenu);
    newGameButtonClicked(newGame, newGameMenu);
    easyButtonClicked(easy);
    mediumButtonClicked(medium);
    hardButtonClicked(hard);
    autoGameButtonClicked(autoGame);
    exitButtonClicked(exitGame);
    backNewGameButtonClicked(backNewGame, mainMenu);
    backReplayButtonClicked(backReplay, mainMenu);
  }

  public void setMainMenu(){
    this.setSubMenu(mainMenu);
  }

  private void backNewGameButtonClicked(MenuItem back, SubMenu mainMenu){
    back.setOnMouseClicked(event -> setSubMenu(mainMenu));
  }

  private void exitButtonClicked(MenuItem exitGame){
    exitGame.setOnMouseClicked(event -> System.exit(0));
  }

  private void backReplayButtonClicked(MenuItem backReplay, SubMenu mainMenu){
    backReplay.setOnMouseClicked(event -> {
      listView.setVisible(false);
      setSubMenu(mainMenu);
    });
  }

  private void autoGameButtonClicked(MenuItem autoGame){
    autoGame.setOnMouseClicked(event -> {
      Game.player.setTranslateX(Ninja.START_X);
      Game.player.setTranslateY(Ninja.START_Y);
      Game.gameRoot.setTranslateX(Ninja.START_X);
      this.setVisible(false);
      Game.autoGameFlag = true;
    });
  }

  private void hardButtonClicked(MenuItem hard){
    hard.setOnMouseClicked(event-> {
      Game.autoGameFlag = false;
      Game.humanGameFlag = true;
      Game.levelNumber = 2;
      Game.changeLevel = true;
      this.setVisible(false);
    });

  }

  private void mediumButtonClicked(MenuItem medium){
    medium.setOnMouseClicked(event-> {
      Game.autoGameFlag = false;
      Game.humanGameFlag = true;
      Game.levelNumber = 1;
      Game.changeLevel = true;
      this.setVisible(false);
    });
  }

  private void easyButtonClicked(MenuItem easy){
    easy.setOnMouseClicked(event -> {
      Game.autoGameFlag = false;
      Game.humanGameFlag = true;
      Game.levelNumber = 0;
      Game.changeLevel = true;
      this.setVisible(false);
    });
  }

  private void newGameButtonClicked(MenuItem newGame, SubMenu newGameMenu){
    newGame.setOnMouseClicked(event -> setSubMenu(newGameMenu));
  }

  private void replayButtonClicked(MenuItem replay, SubMenu replayMenu){
    replay.setOnMouseClicked(event -> setSubMenu(replayMenu));
  }

  private void saveButtonClicked(MenuItem save){
    save.setOnMouseClicked(event -> {
      if(Replay.canSave) {
        Game.replay.save();
      }
    });
  }

  private void playButtonClicked(MenuItem play){
    play.setOnMouseClicked(event -> {
      try (DirectoryStream<Path> directoryStream =
               Files.newDirectoryStream(Paths.get(Replay.DIRECTORY), "*.save")){
        for (Path path : directoryStream) {
          fileNames.add(path.getFileName().toString());
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
      listView.setItems(fileNames);
      Game.player.setScaleX(1);
      Game.autoGameFlag = false;
      Game.humanGameFlag = false;
      listView.setVisible(true);
    });
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

  private void createListView(){
    listView = new ListView<>();
    listView.setVisible(false);
    listView.setTranslateX(400);
    listView.setPrefWidth(400);
    listView.setTranslateY(102);
    listView.setPrefHeight(500);
    fileNames = FXCollections.observableArrayList();
  }

  private void clickedOnListViewItem(){
    listView.setOnMouseClicked(event -> {
      if (event.getClickCount() == 2 &&
          !listView.getSelectionModel().selectedItemProperty().
              toString().equals("")) {
        listView.setVisible(false);
        this.setVisible(false);
        Game.humanGameFlag = false;
        Game.autoGameFlag = false;
        Game.replay.play(Paths.get(Replay.DIRECTORY +
            listView.getSelectionModel().getSelectedItem()));
      }
    });
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
