import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;

public class Game extends Application {
  public static ArrayList<Block> platforms = new ArrayList<>();
  HashMap<KeyCode, Boolean> keys = new HashMap<>();

  public static final int NINJA_SIZE_Y = 49;
  public static final int NINJA_SIZE_X = 40;
  public static final int BACKGROUND_SIZE_Y = 704;
  public static final int SCENE_SIZE = 1200;
  public static final int HIGH = 88;
  public static final int TEXT_SIZE = 25;
  public static final int LABELS_Y = 20;
  public static final int SCORE_X = 20;
  public static final int LEVEL_X = 200;
  public static final int MOVE_LENGHT = 5;

  public static Pane appRoot = new Pane();
  public static Pane gameRoot = new Pane();
  public static Ninja player;
  public static Bot bot;
  public static Label score, level;
  public static Replay replay;
  public static int levelNumber = 0;
  public static int levelWidth;
  public static boolean autoGameFlag = false;
  public static boolean humanGameFlag = false;
  public static boolean changeLevel = false;
  public static volatile boolean replayFlag = false;

  Menu menu = new Menu();

  private void initContent() {
    player = new Ninja();
    bot = new Bot();
    score = new Label();
    level = new Label();
    replay = new Replay();
    gameRoot.getChildren().addAll(player);
    appRoot.getChildren().addAll(gameRoot, score, level);
    updateLevel();
  }

  public static void updateSucceed() {
    score.setText("Succeed: " + (int) (player.getTranslateX() /
            levelWidth * 100) + "%");
    score.setFont(Font.font("Arial", TEXT_SIZE));
    score.setTranslateX(SCORE_X);
    score.setTranslateY(LABELS_Y);
    if (levelNumber == 0) {
      level.setText("Level: EASY");
    }
    if (levelNumber == 1) {
      level.setText("Level: MEDIUM");
    }
    if (levelNumber == 2) {
      level.setText("Level: HARD");
    }
    level.setFont(Font.font("Arial", TEXT_SIZE));
    level.setTranslateX(LEVEL_X);
    level.setTranslateY(LABELS_Y);
  }

  public static void updateLevel() {
    for(Block block : platforms){
      gameRoot.getChildren().remove(block);
    }
    platforms.clear();
    levelWidth = Block.BLOCK_SIZE_X *
        WorldInformation.levels[levelNumber].length;
    for (int i = 0; i < WorldInformation.levels[levelNumber].length; i++) {
      String line = WorldInformation.levels[levelNumber][i];
      for (int j = 0; j < line.length(); j++) {
        switch (line.charAt(j)) {
          case '0':
            break;
          case '1':
            new Block(Block.BlockType.PLATFORM,
                i * Block.BLOCK_SIZE_X, (j + 1) * HIGH - Block.BLOCK_SIZE_Y);
            break;
          case '2':
            new Block(Block.BlockType.MOVE_PLATFORM,
                i * Block.BLOCK_SIZE_X, (j + 1) * HIGH - Block.BLOCK_SIZE_Y);
            break;
        }
      }
    }
    player.setTranslateX(Ninja.START_X);
    player.setTranslateY(Ninja.START_Y);
    gameRoot.setLayoutX(Ninja.START_X);
    updateSucceed();
  }

  public static void checkEndOfLevel() {
    if (player.getTranslateX() >
        (WorldInformation.levels[levelNumber].length - 2) * Block.BLOCK_SIZE_X){
      player.setTranslateX(Ninja.START_X);
      player.setTranslateY(Ninja.START_Y);
      Game.gameRoot.setLayoutX(Ninja.START_X);
      if (Game.levelNumber < 2) {
        Game.levelNumber++;
        Game.updateLevel();
      } else {
        Game.levelNumber = 0;
        Game.updateLevel();
      }
    }
  }

  private void humanGame() {
    if (isPressed(KeyCode.UP) && player.getTranslateY() >= 5) {
      player.jumpPlayer();
      replay.record((int)player.getTranslateX(), (int)player.getTranslateY(),
              (int)player.getScaleX());
    }
    if (isPressed(KeyCode.LEFT) && player.getTranslateX() >= 5) {
      player.setScaleX(-1);
      player.animation.play();
      player.moveX(-MOVE_LENGHT);
      replay.record((int)player.getTranslateX(), (int)player.getTranslateY(),
              (int)player.getScaleX());
    }
    if (isPressed(KeyCode.RIGHT)) {
      player.setScaleX(1);
      player.animation.play();
      player.moveX(MOVE_LENGHT);
      replay.record((int)player.getTranslateX(), (int)player.getTranslateY(),
              (int)player.getScaleX());
    }
  }

  private void update() {
    checkEndOfLevel();
    updateSucceed();

    for(Block block : platforms){
      block.moveBlock(player);
    }

    if (player.playerVelocity.getY() < 10) {
      player.playerVelocity = player.playerVelocity.add(0, 1);
    }
    player.moveY((int) player.playerVelocity.getY());

    if(changeLevel){
      updateLevel();
      changeLevel = false;
    }

    if(autoGameFlag) {
      bot.play();
    }

    if(humanGameFlag){
      humanGame();
    }
  }

  private boolean isPressed(KeyCode key) {
    return keys.getOrDefault(key, false);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    initContent();
    appRoot.getChildren().addAll(menu);
    Scene scene = new Scene(appRoot, SCENE_SIZE, BACKGROUND_SIZE_Y);
    scene.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ESCAPE) {
        if (!menu.isVisible()) {
          menu.setMainMenu();
          menu.setVisible(true);
        }
        else{
          menu.setVisible(false);
        }
      }
      else{
        if(!menu.isVisible()){
          keys.put(event.getCode(), true);
        }
      }
    });

    scene.setOnKeyReleased(event -> {
      keys.put(event.getCode(), false);
      player.animation.stop();
    });
    primaryStage.setTitle("SJumper");
    primaryStage.setScene(scene);
    primaryStage.show();
    AnimationTimer timer = new AnimationTimer() {
      @Override
      public void handle(long now) {
        update();
      }
    };
    timer.start();
  }

  public static void main(String[] args) {
    launch(args);
  }
}