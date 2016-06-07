package sjumper;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Pane;
import sjumper.Block;
import sjumper.Bot;
import sjumper.Client;

import java.util.ArrayList;
import java.util.HashMap;

public class Game{
  static volatile ArrayList<Block> platforms = new ArrayList<>();
  static HashMap<KeyCode, Boolean> keys = new HashMap<>();

  static final int NINJA_SIZE_Y = 49;
  static final int NINJA_SIZE_X = 40;
  static final int BACKGROUND_SIZE_Y = 704;
  static final int SCENE_SIZE = 1200;
  static final int HIGH = 88;
  static final int TEXT_SIZE = 25;
  static final int LABELS_Y = 20;
  static final int SCORE_X = 20;
  static final int LEVEL_X = 200;
  static final int MOVE_LENGHT = 5;

  public static Bot bot;
  public static Replay replay;
  public static volatile Pane gameRoot = new Pane();
  public static volatile Ninja ninja;
  public static volatile int levelNumber = 0;
  static volatile int lastLevel;
  static volatile int lastSucced;

  static volatile Label score, level;
  static volatile int levelWidth;
  static volatile boolean replayFlag = false;
  static boolean autoGameFlag = false;
  static boolean humanGameFlag = false;
  static boolean changeLevel = false;
  static boolean pause = true;
  static volatile boolean setMenu = false;
  //static boolean firstMove = true;



  public Game(){
    ninja = new Ninja();
    bot = new Bot();
    score = new Label();
    level = new Label();
    replay = new Replay();
    Image backgroundImg = new Image(getClass().getResourceAsStream("white.jpg"));
    ImageView background = new ImageView(backgroundImg);
    gameRoot.getChildren().addAll(background, ninja, score, level);

    AnimationTimer timer = new AnimationTimer() {
      @Override
      public void handle(long now) {
        update();
      }
    };
    timer.start();

    Client update = new Client();
    update.updateLevel();
  }

  private void humanGame() {
    if (isPressed(KeyCode.UP) && ninja.getTranslateY() >= 5) {
      Server jump = new Server();
      jump.jumpNinja(ninja);
      replay.record((int) ninja.getTranslateX(), (int) ninja.getTranslateY(),
              (int) ninja.getScaleX(), "Up");
    }
    if (isPressed(KeyCode.LEFT) && ninja.getTranslateX() >= 5) {
      ninja.setScaleX(-1);
      ninja.animation.play();
      Server move = new Server();
      move.moveX(-MOVE_LENGHT, ninja);
      replay.record((int) ninja.getTranslateX(), (int) ninja.getTranslateY(),
              (int) ninja.getScaleX(), "Left");
    }
    if (isPressed(KeyCode.RIGHT)) {
      ninja.setScaleX(1);
      ninja.animation.play();
      Server move = new Server();
      move.moveX(MOVE_LENGHT, ninja);
      replay.record((int) ninja.getTranslateX(), (int) ninja.getTranslateY(),
              (int) ninja.getScaleX(), "Right");
    }
  }

  private void update() {
    if(setMenu){
      setMenu = false;
      SJumper.scene.setRoot(Menu.menuRoot);
    }

    if(pause){
      return;
    }

    for(Block block : platforms){
      block.moveBlock(ninja);
    }

    if (ninja.playerVelocity.getY() < 10) {
      ninja.playerVelocity = ninja.playerVelocity.add(0, 1);
    }

    Server moveNinjaY = new Server();
    moveNinjaY.moveY((int) ninja.playerVelocity.getY(), ninja);

    if(changeLevel){
      Client threadUpdate = new Client();
      threadUpdate.updateLevel();
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

}