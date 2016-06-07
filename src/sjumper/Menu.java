package sjumper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import sjumper.Game;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.*;
import java.util.*;

public class Menu extends Pane {

  private static final double OPACITY_BACKGROUND = 0.6;
  private static final int LIST_VIEW_WIDTH = 400;
  private static final int LIST_VIEW_COORDINATE_Y = 102;
  private static final int LIST_VIEW_HIGH = 500;
  private static final int NOTATION_Y = 640;

  static SubMenu mainMenu;
  private SubMenu menuBox;
  private SubMenu replayMenu;
  static Pane menuRoot;
  private TableView<Replay> table = new TableView<>();
  private static ArrayList<Integer> percent = new ArrayList<>();
  private static ArrayList<String> files = new ArrayList<>();
  private boolean first = true;
  private ListView<String> listView;
  private ObservableList<String> fileNames;
  Game game;

  public Menu() {
    game = new Game();
    menuRoot = new Pane(this);

    createListView();
    clickedOnListViewItem();

    MenuItem newGame = new MenuItem("NEW GAME");
    MenuItem autoGame = new MenuItem("AUTO GAME");
    MenuItem replay = new MenuItem("REPLAY");
    MenuItem sort = new MenuItem("SORT");
    MenuItem statistics = new MenuItem("STATISTICS");
    MenuItem exitGame = new MenuItem("EXIT");
    mainMenu = new SubMenu(
        newGame, autoGame, replay, sort, statistics, exitGame
    );
    MenuItem easy = new MenuItem("EASY");
    MenuItem medium = new MenuItem("MEDIUM");
    MenuItem hard = new MenuItem("HARD");
    MenuItem backNewGame = new MenuItem("BACK");
    SubMenu newGameMenu = new SubMenu(easy, medium, hard, backNewGame);
    MenuItem save = new MenuItem("SAVE");
    MenuItem play = new MenuItem("PLAY");
    MenuItem backReplay = new MenuItem("BACK");
    replayMenu = new SubMenu(save, play, backReplay);
    MenuItem javaSort = new MenuItem("Java Sort");
    MenuItem scalaSort = new MenuItem("Scala Sort");
    MenuItem backSort = new MenuItem("Back");
    SubMenu sortMenu = new SubMenu(javaSort, scalaSort, backSort);

    Rectangle bg = new Rectangle(Game.SCENE_SIZE,
        Game.BACKGROUND_SIZE_Y, Color.BLACK);
    bg.setOpacity(OPACITY_BACKGROUND);
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
    sortButtonClicked(sort, sortMenu);
    javaSortButtonClicked(javaSort);
    scalaButtonClicked(scalaSort);
    backSortButtonClicked(backSort);
    statisticsButtonClicked(statistics);
    setTable();
  }

  private void backNewGameButtonClicked(MenuItem back, SubMenu mainMenu) {
    back.setOnMouseClicked(event -> setSubMenu(mainMenu));
  }

  private void exitButtonClicked(MenuItem exitGame) {
    exitGame.setOnMouseClicked(event -> System.exit(0));
  }

  private void backReplayButtonClicked(MenuItem backReplay, SubMenu mainMenu) {
    backReplay.setOnMouseClicked(event -> {
      listView.setVisible(false);
      setSubMenu(mainMenu);
    });
  }

  private void autoGameButtonClicked(MenuItem autoGame) {
    autoGame.setOnMouseClicked(event -> {
      Game.ninja.setTranslateX(Ninja.START_X);
      Game.ninja.setTranslateY(Ninja.START_Y);
      Game.gameRoot.setTranslateX(Ninja.START_X);
      Game.autoGameFlag = true;
      Game.pause = false;
      Replay.firstRecord = true;
      SJumper.scene.setRoot(Game.gameRoot);
    });
  }

  private void hardButtonClicked(MenuItem hard) {
    hard.setOnMouseClicked(event -> {
      Game.autoGameFlag = false;
      Game.humanGameFlag = true;
      Game.levelNumber = 2;
      Game.changeLevel = true;
      setSubMenu(mainMenu);
      SJumper.scene.setRoot(Game.gameRoot);
      Game.pause = false;
      Replay.firstRecord = true;
    });
  }

  private void mediumButtonClicked(MenuItem medium) {
    medium.setOnMouseClicked(event -> {
      Game.autoGameFlag = false;
      Game.humanGameFlag = true;
      Game.levelNumber = 1;
      Game.changeLevel = true;
      setSubMenu(mainMenu);
      SJumper.scene.setRoot(Game.gameRoot);
      Game.pause = false;
      Replay.firstRecord = true;
    });
  }

  private void easyButtonClicked(MenuItem easy) {
    easy.setOnMouseClicked(event -> {
      Game.autoGameFlag = false;
      Game.humanGameFlag = true;
      Game.levelNumber = 0;
      Game.changeLevel = true;
      setSubMenu(mainMenu);
      SJumper.scene.setRoot(Game.gameRoot);
      Game.pause = false;
      Replay.firstRecord = true;
    });
  }

  private void newGameButtonClicked(MenuItem newGame, SubMenu newGameMenu) {
    newGame.setOnMouseClicked(event -> setSubMenu(newGameMenu));
  }

  private void replayButtonClicked(MenuItem replay, SubMenu replayMenu) {
    replay.setOnMouseClicked(event -> setSubMenu(replayMenu));
  }

  private void saveButtonClicked(MenuItem save) {
    save.setOnMouseClicked(event -> {
      if (Replay.canSave) {
        Game.replay.save();
      }
    });
  }

  private void playButtonClicked(MenuItem play) {
    MenuItem notation = new MenuItem("Notation");
    notation.setTranslateY(NOTATION_Y);
    notation.setTranslateX(SubMenu.COORDINATE_X);
    notationButtonClicked(notation);
    notation.setVisible(false);
    menuRoot.getChildren().addAll(notation);
    play.setOnMouseClicked(event -> {
      if(first) {
        try (DirectoryStream<Path> directoryStream =
                 Files.newDirectoryStream(Paths.get(Replay.DIRECTORY),
                     "*.save")) {
          for (Path path : directoryStream) {
            fileNames.add(path.getFileName().toString());
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
        listView.setItems(fileNames);
        first = false;
      }
      Game.ninja.setScaleX(1);
      Game.autoGameFlag = false;
      Game.humanGameFlag = false;
      notation.setVisible(true);
      listView.setVisible(true);
      replayMenu.setVisible(false);
    });

    listView.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ESCAPE && listView.isVisible()) {
        listView.setVisible(false);
        replayMenu.setVisible(true);
        setSubMenu(replayMenu);
        notation.setVisible(false);
      }
    });
  }

  private void notationButtonClicked(MenuItem notation){
    notation.setOnMouseClicked(event -> {
      Replay replay = new Replay();
      replay.makeNotation(listView.getFocusModel().getFocusedItem());
    });
  }

  private void createListView() {
    listView = new ListView<>();
    listView.setVisible(false);
    listView.setTranslateX(SubMenu.COORDINATE_X);
    listView.setPrefWidth(LIST_VIEW_WIDTH);
    listView.setTranslateY(LIST_VIEW_COORDINATE_Y);
    listView.setPrefHeight(LIST_VIEW_HIGH);
    fileNames = FXCollections.observableArrayList();
  }

  private void clickedOnListViewItem() {
    listView.setOnMouseClicked(event -> {
      if (event.getClickCount() == 2 &&
          !listView.getSelectionModel().selectedItemProperty().
              toString().equals("")) {

        listView.setVisible(false);
        Game.humanGameFlag = false;
        Game.autoGameFlag = false;
        SJumper.scene.setRoot(Game.gameRoot);

        Game.pause = false;

        Game.score.setVisible(false);
        Game.level.setVisible(false);

        Game.replay.play(Paths.get(Replay.DIRECTORY +
            listView.getSelectionModel().getSelectedItem()));

        setSubMenu(mainMenu);
      }
    });
  }

  void setSubMenu(SubMenu subMenu) {
    getChildren().remove(menuBox);
    menuBox = subMenu;
    getChildren().add(menuBox);
  }

  private void sortButtonClicked(MenuItem sort, SubMenu sortMenu) {
    sort.setOnMouseClicked(event -> {
      setSubMenu(sortMenu);
      getReplayInfo();
    });
  }

  private void getReplayInfo() {
    files.clear();
    try (DirectoryStream<Path> directoryStream =
             Files.newDirectoryStream(Paths.get(Replay.DIRECTORY), "*.save")) {
      for (Path path : directoryStream) {
        String string = path.getFileName().toString();
        files.add(string);
        percent.add(Integer.parseInt(string.substring(string.lastIndexOf(' ')
            + 1, string.lastIndexOf(' ') + 2)));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void javaSortButtonClicked(MenuItem javaSort) {
    javaSort.setOnMouseClicked(event -> {
      JavaSort javasort = new JavaSort();
      int[] arrayInt = new int[files.size()];
      String[] arrayString = new String[files.size()];
      for (int i = 0; i < files.size(); i++) {
        arrayString[i] = files.get(i);
        arrayInt[i] = percent.get(i);
      }

      long start = System.nanoTime();
      javasort.sort(arrayInt, arrayString);
      long finish = System.nanoTime();
      System.out.println("JavaSort time: " + (finish - start));

      fileNames.clear();
      listView.refresh();
      fileNames = FXCollections.observableArrayList(arrayString);
      //listView.setItems(sortFiles);
    });
  }

  private void scalaButtonClicked(MenuItem scalaSort) {
    scalaSort.setOnMouseClicked(event -> {
      ScalaSort scalasort = new ScalaSort();
      int[] arrayInt = new int[files.size()];
      String[] arrayString = new String[files.size()];
      for (int i = 0; i < files.size(); i++) {
        arrayString[i] = files.get(i);
        arrayInt[i] = percent.get(i);
      }

      long start = System.nanoTime();
      scalasort.sort(arrayInt, arrayString);
      long finish = System.nanoTime();
      System.out.println("ScalaSort time: " + (finish - start));

      fileNames.clear();
      fileNames = FXCollections.observableArrayList(arrayString);
      listView.setItems(fileNames);
      listView.refresh();
    });
  }

  private void backSortButtonClicked(MenuItem backSort) {
    backSort.setOnMouseClicked(event -> {
      setSubMenu(mainMenu);
    });
  }

  private void setTable(){
    menuRoot.getChildren().add(table);
    table.setVisible(false);
    table.setEditable(true);
    table.setTranslateX(500);
    table.setTranslateY(250);
    table.setMaxWidth(251);
    table.setMaxHeight(300);

    table.setOnKeyPressed(event -> {
      if(event.getCode() == KeyCode.ESCAPE){
        mainMenu.setVisible(true);
        table.setVisible(false);
      }
    });
  }

  private void statisticsButtonClicked(MenuItem statistics) {
    statistics.setOnMouseClicked(event -> {
      int replayCount = 0;
      Statistics stat = new Statistics();
      try (DirectoryStream<Path> directoryStream =
               Files.newDirectoryStream(Paths.get(Replay.DIRECTORY), "*.save")){
        List<String> tmp;
        String[] separeteCommands;
        for (Path path : directoryStream) {
          replayCount++;
          tmp = Files.readAllLines(path);
          String levelInfo = tmp.get(0);
          tmp.remove(0);
          separeteCommands = levelInfo.split(" ");
          stat.add(separeteCommands[1]);
          for (String string : tmp) {
            separeteCommands = string.split(" ");
            stat.add(separeteCommands[4]);
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
      TableColumn name = new TableColumn("Action");
      name.setMinWidth(100);
      name.setCellValueFactory(new PropertyValueFactory<>("act"));
      name.setMinWidth(125);
      TableColumn count = new TableColumn("Count");
      count.setCellValueFactory(new PropertyValueFactory<>("count"));
      count.setMinWidth(125);
      ObservableList<Replay> info = FXCollections.observableArrayList(
          new Replay("Left", stat.getMoveLeftCount()/replayCount),
          new Replay("Right", stat.getMoveRightCount()/replayCount),
          new Replay("Up", stat.getMoveUpCount()/replayCount),
          new Replay("Easy", stat.getEasyCount()),
          new Replay("Medium", stat.getMediumCount()),
          new Replay("Hard", stat.getHardCount())
      );
      table.setItems(info);
      table.getColumns().addAll(name, count);
      mainMenu.setVisible(false);
      table.setVisible(true);
    });
  }


}