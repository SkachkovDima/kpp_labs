import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;

public class Game extends Application {
  public static ArrayList<Block> platforms = new ArrayList<>();
  private HashMap<KeyCode, Boolean> keys = new HashMap<>();

  public static final int BLOCK_SIZE_X = 73;
  public static final int BLOCK_SIZE_Y = 17;
  public static final int NINJA_SIZE_Y = 49;
  public static final int NINJA_SIZE_X = 40;
  public static final int BACKGROUND_SIZE_X = 2560;
  public static final int BACKGROUND_SIZE_Y = 704;
  public static final int BACKGROUND_CHANGE = 640;
  public static final int SCENE_SIZE = 1200;
  public static final int MOVE_LENGHT = 5;
  public static final int HIGH = 88;
  public static final int BREAKPOINT = 40;

  public static Pane appRoot = new Pane();
  public static Pane gameRoot = new Pane();
  private Menu menu = new Menu();

  public static Ninja player;
  public static boolean autoGameFlag = false;
  private int blockCounter = 0;
  static int levelNumber = 0;
  private int levelWidth = WorldInformation.levels[0].length * BLOCK_SIZE_Y;
  private boolean nextBlockFlag = false;
  private int lineIndex = 0;
  private int symbolIndex = 0;
  private Block block;
  public static boolean changeLevel = false;

  private void initContent() {
    levelWidth = WorldInformation.levels[levelNumber][0].length() *
        BLOCK_SIZE_X;
    for (int i = 0; i < WorldInformation.levels[levelNumber].length; i++) {
      String line = WorldInformation.levels[levelNumber][i];
      for (int j = 0; j < line.length(); j++) {
        switch (line.charAt(j)) {
          case '0':
            break;
          case '1':
            new Block(Block.BlockType.PLATFORM,
                i * BLOCK_SIZE_X, (j + 1) * HIGH - BLOCK_SIZE_Y);
            break;
          case '2':
            new Block(Block.BlockType.MOVE_PLATFORM,
                i * BLOCK_SIZE_X, (j + 1) * HIGH - BLOCK_SIZE_Y);
            break;
        }
      }
    }

    player = new Ninja();
    player.setTranslateX(Ninja.START_X);
    player.setTranslateY(Ninja.START_Y);
    player.translateXProperty().addListener((obs, old, newValue) -> {
      int offset = newValue.intValue();
      if (offset > BACKGROUND_CHANGE &&
          offset < levelWidth - BACKGROUND_CHANGE) {
        gameRoot.setLayoutX(-(offset - BACKGROUND_CHANGE));
        //backgroundIV.setLayoutX(-(offset - BACKGROUND_CHANGE));
      }
    });
    gameRoot.getChildren().add(player);
    appRoot.getChildren().addAll(gameRoot);
  }

  private void updateLevel(){
    gameRoot.getChildren().removeAll(platforms);
    platforms.clear();
    for (int i = 0; i < WorldInformation.levels[levelNumber].length; i++) {
      String line = WorldInformation.levels[levelNumber][i];
      for (int j = 0; j < line.length(); j++) {
        switch (line.charAt(j)) {
          case '0':
            break;
          case '1':
            new Block(Block.BlockType.PLATFORM,
                i * BLOCK_SIZE_X, (j + 1) * HIGH - BLOCK_SIZE_Y);
            break;
          case '2':
            new Block(Block.BlockType.MOVE_PLATFORM,
                i * BLOCK_SIZE_X, (j + 1) * HIGH - BLOCK_SIZE_Y);
            break;
        }
      }
    }
    player.setTranslateX(Ninja.START_X);
    player.setTranslateY(Ninja.START_Y);
    gameRoot.setLayoutX(Ninja.START_X);
  }

  private void humanGame() {
    if (isPressed(KeyCode.UP) && player.getTranslateY() >= 5) {
      player.jumpPlayer();
    }
    if (isPressed(KeyCode.LEFT) && player.getTranslateX() >= 5) {
      player.setScaleX(-1);
      player.animation.play();
      player.moveX(-MOVE_LENGHT);
      if(player.getTranslateX() - gameRoot.getLayoutX() > SCENE_SIZE/2) {
        gameRoot.setLayoutX(gameRoot.getLayoutX() + MOVE_LENGHT);
      }
    }
    if (isPressed(KeyCode.RIGHT)) {
      player.setScaleX(1);
      player.animation.play();
      player.moveX(MOVE_LENGHT);
      if(player.getTranslateX() - gameRoot.getLayoutX() > SCENE_SIZE/2) {
        gameRoot.setLayoutX(gameRoot.getLayoutX() - MOVE_LENGHT);
      }
    }
  }

  private void moveOnBlock() {
    if (player.getTranslateX() >= lineIndex * BLOCK_SIZE_X - NINJA_SIZE_X / 2 &&
        player.getTranslateX() <= (lineIndex + 1) * BLOCK_SIZE_X -
            NINJA_SIZE_X / 2 - 1) {
      if ((int) player.getTranslateY() == BACKGROUND_SIZE_Y -
          (7 - symbolIndex) * HIGH - BLOCK_SIZE_Y - NINJA_SIZE_Y - 1) {
        if ((lineIndex + 1) * BLOCK_SIZE_X - NINJA_SIZE_X / 2 -
            player.getTranslateX() >= 1.5 * MOVE_LENGHT) {
          System.out.println(2);
          player.setScaleX(1);
          player.animation.play();
          player.moveX(MOVE_LENGHT);
          if(player.getTranslateX() - gameRoot.getLayoutX() > SCENE_SIZE/2) {
            gameRoot.setLayoutX(gameRoot.getLayoutX() - MOVE_LENGHT);
          }
        } else {
          for (int n = 1; n <= 3; n++) {
            if(lineIndex + n >= 36){
              Game.player.setTranslateX(Ninja.START_X);
              Game.player.setTranslateY(Ninja.START_Y);
              Game.gameRoot.setLayoutX(Ninja.START_X);
              break;
            }
            String nextLine =
                WorldInformation.levels[levelNumber][lineIndex + n];
            for (int k = 0; k < nextLine.length(); k++) {
              if (nextLine.charAt(k) == '1') {
                nextBlockFlag = true;
                blockCounter++;
                break;
              }
            }
          }
          player.animation.stop();
        }
      }
    }
  }

  private void moveOnNextBlock() {
    block = platforms.get(blockCounter);
    if (block.getTranslateY() >= player.getTranslateY() + NINJA_SIZE_Y + 1) {
      if (player.getTranslateX() <= block.getTranslateX()) {
        System.out.println(1);
        player.setScaleX(1);
        player.animation.play();
        player.moveX(MOVE_LENGHT);
        if(player.getTranslateX() - gameRoot.getLayoutX() > SCENE_SIZE/2) {
          gameRoot.setLayoutX(gameRoot.getLayoutX() - MOVE_LENGHT);
        }
        if (player.getTranslateX() == block.getTranslateX() - NINJA_SIZE_X/2+1){
          nextBlockFlag = false;
        }
      }
    } else {
      player.animation.play();
      if (block.getTranslateX() - player.getTranslateX() < BREAKPOINT) {
        player.setScaleX(-1);
        player.animation.play();
        player.moveX(-MOVE_LENGHT);
        if(player.getTranslateX() - gameRoot.getLayoutX() > SCENE_SIZE/2) {
          gameRoot.setLayoutX(gameRoot.getLayoutX() + MOVE_LENGHT);
        }
      } else {
        player.setScaleX(1);
        player.jumpPlayer();
        if (player.getTranslateX() < block.getTranslateX() - NINJA_SIZE_X/2+1) {
          if (player.getTranslateY() - BLOCK_SIZE_Y - NINJA_SIZE_Y - 1 >=
              block.getTranslateY()) {
            player.moveX(MOVE_LENGHT);
            if(player.getTranslateX() - gameRoot.getLayoutX() > SCENE_SIZE/2) {
              gameRoot.setLayoutX(gameRoot.getLayoutX() - MOVE_LENGHT);
            }
          }

        } else {
          nextBlockFlag = false;
        }
      }
    }
  }

  private void autoGame() {
    if (lineIndex == WorldInformation.levels[levelNumber].length) {
      lineIndex = 0;
//      Game.player.setTranslateX(Ninja.START_X);
//      Game.player.setTranslateY(Ninja.START_Y);
//      Game.gameRoot.setLayoutX(Ninja.START_X);
      // переход на новый уровень!
      // надо менять symbolIndex = 0
    }

    for (int blockIndex = 0; blockIndex < platforms.size(); blockIndex++) {
      if (platforms.get(blockIndex).getTranslateX() - NINJA_SIZE_X / 2 <
          player.getTranslateX() &&
          platforms.get(blockIndex).getTranslateX() +
              BLOCK_SIZE_X - NINJA_SIZE_X / 2 > player.getTranslateX()) {
        blockCounter = blockIndex + 1;
      }
    }

    if (!nextBlockFlag) {
      for (; lineIndex < WorldInformation.levels[levelNumber].length;
           lineIndex++) {
        String line = WorldInformation.levels[levelNumber][lineIndex];
        for (symbolIndex = 0; symbolIndex < line.length(); symbolIndex++) {
          if (line.charAt(symbolIndex) >= '1') {
            moveOnBlock();
          }
        }
      }
    } else {
      moveOnNextBlock();
    }
  }

  private void update() {
    if(changeLevel){
      updateLevel();
      changeLevel = false;
      lineIndex = 0;
    }
    if (!autoGameFlag) {
      humanGame();
    } else {
      autoGame();
    }

    if (isPressed(KeyCode.ESCAPE)) {
      FadeTransition ft = new FadeTransition(Duration.seconds(1), menu);
      if (!menu.isVisible()) {
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
        menu.setVisible(true);
      } else {
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.setOnFinished(evt -> menu.setVisible(false));
        ft.play();

      }
    }

    for(Block block : platforms){
      block.moveBlock(player);
    }

    if (player.playerVelocity.getY() < 10) {
      player.playerVelocity = player.playerVelocity.add(0, 1);
    }
    player.moveY((int) player.playerVelocity.getY());
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
        FadeTransition ft = new FadeTransition(Duration.seconds(0.5),menu);
        if (!menu.isVisible()) {
          ft.setFromValue(0);
          ft.setToValue(1);
          ft.play();
          menu.setVisible(true);
        }
        else{
          ft.setFromValue(1);
          ft.setToValue(0);
          ft.setOnFinished(evt ->   menu.setVisible(false));
          ft.play();
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