package sjumper;

import javafx.scene.text.Font;
import sjumper.Block;
import sjumper.Bot;

public class Client implements Runnable{
  Thread clientThread;
  Server serverThread;
  boolean mFinish = false;

  Client() {
    clientThread = new Thread(this, "Client_thread");
    clientThread.start();
  }

  @Override
  public void run() {
    if (mFinish == true) {
      return;
    }
    // System.out.println("sjumper.Client start!");
  }

  public void finish() {
    mFinish = true;
  }

  public void checkEndOfLevel(Ninja ninja) {
    if (ninja.getTranslateX() >
        (WorldInformation.levels[Game.levelNumber].length - 3) * Block.BLOCK_SIZE_X) {
      if (Game.levelNumber < 2) {
        Game.levelNumber++;
      } else {
        Game.levelNumber = 0;
      }
      this.updateLevel();
      Bot.blockCounter = 0;
    }
    finish();
  }

  public void updateSucceed() {
    Game.score.setText("Succeed: " + (int) (Game.ninja.getTranslateX() /
        Game.levelWidth * 100) + "%");
    Game.score.setFont(Font.font("Arial", Game.TEXT_SIZE));
    Game.score.setTranslateX(Game.SCORE_X);
    Game.score.setTranslateY(Game.LABELS_Y);
    if (Game.levelNumber == 0) {
      Game.level.setText("Level: EASY");
    }
    if (Game.levelNumber == 1) {
      Game.level.setText("Level: MEDIUM");
    }
    if (Game.levelNumber == 2) {
      Game.level.setText("Level: HARD");
    }
    Game.level.setFont(Font.font("Arial", Game.TEXT_SIZE));
    Game.level.setTranslateX(Game.LEVEL_X);
    Game.level.setTranslateY(Game.LABELS_Y);
    finish();
  }

  public void updateLevel() {
    for(Block block : Game.platforms){
      Game.gameRoot.getChildren().removeAll(block);
    }
    Game.platforms.clear();

    Game.levelWidth = Block.BLOCK_SIZE_X *
        WorldInformation.levels[Game.levelNumber].length;

    for (int i = 0; i < WorldInformation.levels[Game.levelNumber].length; i++){
      String line = WorldInformation.levels[Game.levelNumber][i];
      for (int j = 0; j < line.length(); j++) {
        switch (line.charAt(j)) {
          case '0':
            break;
          case '1':
            new Block(Block.BlockType.PLATFORM, i*Block.BLOCK_SIZE_X,
                (j + 1)* Game.HIGH - Block.BLOCK_SIZE_Y);
            break;
          case '2':
            new Block(Block.BlockType.MOVE_PLATFORM, i*Block.BLOCK_SIZE_X,
                (j + 1)* Game.HIGH - Block.BLOCK_SIZE_Y);
            break;
        }
      }
    }

    Game.ninja.setTranslateX(Ninja.START_X);
    Game.ninja.setTranslateY(Ninja.START_Y);
    Game.gameRoot.setTranslateX(Ninja.START_X);
    finish();
  }
}
