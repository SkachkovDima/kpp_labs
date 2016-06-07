package sjumper;

import javafx.geometry.Point2D;
import javafx.scene.Node;;

class Server implements Runnable{
  private Client clientThread;
  private Thread serverThread;
  private boolean mFinish = false;

  Server() {
    if (serverThread == null) {
      serverThread = new Thread(this, "Server_thread");
      serverThread.start();
    }
  }

  public void run() {
    if(mFinish){
      return;
    }
  }

  private void finish() {
    mFinish = true;
  }

  void moveX(int value, Ninja ninja) {
    boolean movingRight = value > 0;
    for (int i = 0; i < Math.abs(value); i++) {
      for (Node platform : Game.platforms) {
        if (ninja.getBoundsInParent().intersects(
            platform.getBoundsInParent())) {
          if (movingRight) {
            if (ninja.getTranslateX() ==
                platform.getTranslateX()) {
              ninja.setTranslateX(ninja.getTranslateX() - 1);
              finish();
              return;
            }
          } else {
            if (ninja.getTranslateX() == platform.getTranslateX() +
                Block.BLOCK_SIZE_X) {
              ninja.setTranslateX(ninja.getTranslateX() + 1);
              finish();
              return;
            }
          }
        }
      }
      ninja.setTranslateX(ninja.getTranslateX() + (movingRight ? 1 : -1));
      clientThread = new Client();
      clientThread.checkEndOfLevel(ninja);
      clientThread.updateSucceed();
    }
    if (ninja.getTranslateX() > Game.gameRoot.getTranslateX() +
        Game.SCENE_SIZE / 2) {
      Game.gameRoot.setTranslateX(Game.gameRoot.getTranslateX() - value);
    }
    Game.lastLevel = Game.levelNumber;
    Game.lastSucced = (int)Game.ninja.getTranslateX() / Block.BLOCK_SIZE_X;
    finish();
  }

  void moveY(int value, Ninja ninja){
    boolean movingDown = value > 0;
    for (int i = 0; i < Math.abs(value); i++) {
      for (Block platform : Game.platforms) {
        if (ninja.getBoundsInParent().intersects(platform.getBoundsInParent())){
          if (movingDown) {
            if (ninja.getTranslateY() + Game.NINJA_SIZE_Y ==
                platform.getTranslateY()) {
              ninja.setTranslateY(ninja.getTranslateY() - 1);
              Ninja.canJump = true;
              finish();
              return;
            }
          } else {
            if (ninja.getTranslateY() == platform.getTranslateY() +
                Block.BLOCK_SIZE_Y) {
              ninja.setTranslateY(ninja.getTranslateY() + 1);
              ninja.playerVelocity = new Point2D(Ninja.POINT_2D_X,
                  Ninja.POINT_2D_Y);
              finish();
              return;
            }
          }
        }
      }
      ninja.setTranslateY(ninja.getTranslateY() + (movingDown ? 1 : -1));
      if (ninja.getTranslateY() > Ninja.MOVING_DOWN) {
        this.setOnStart(ninja);
      }
    }
    finish();
  }

  void jumpNinja(Ninja ninja) {
    if (Ninja.canJump) {
      ninja.playerVelocity = ninja.playerVelocity.add(Ninja.PLAYER_VELOCITY_X,
          Ninja.PLAYER_VELOCITY_Y);
      Ninja.canJump = false;
    }
    finish();
  }

  void setOnStart(Ninja ninja) {
    //Game.setMenu = true;
    if(Replay.canSave) {
      Game.replay.save();
    }
    ninja.setTranslateX(Ninja.START_X);
    ninja.setTranslateY(Ninja.START_Y);
    Game.gameRoot.setTranslateX(Ninja.START_X);
    Bot.blockCounter = 0;
    Game.levelNumber = 0;

    clientThread = new Client();
    clientThread.updateLevel();
     // надо подождать этот поток!!!!

    Replay.firstRecord = true;

    finish();
  }

}
