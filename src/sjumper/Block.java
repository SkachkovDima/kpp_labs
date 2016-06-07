package sjumper;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Block extends Pane {
  static final int BLOCK_SIZE_X = 73;
  static final int BLOCK_SIZE_Y = 17;
  private static final int BLOCK_SPEED = 2;
  private static final int MOVE_BLOCK_LENGHT = 3;

  public enum BlockType {
    PLATFORM, MOVE_PLATFORM
  }

  private int startX;
  private int platformType;
  private boolean flagRoute = true;


  public Block(BlockType blockType, int x, int y) {
    Image blocksImg =
        new Image(getClass().getResourceAsStream("blockBlack.jpg"));
    ImageView block;
    block = new ImageView(blocksImg);
    block.setFitWidth(BLOCK_SIZE_X);
    block.setFitHeight(BLOCK_SIZE_Y);
    startX = x;;
    setTranslateX(x);
    setTranslateY(y);

    switch (blockType) {
      case PLATFORM:
        platformType = 0;
        block.setViewport(new Rectangle2D(0, 0, BLOCK_SIZE_X, BLOCK_SIZE_Y));
        break;
      case MOVE_PLATFORM:
        platformType = 1;
        block.setViewport(new Rectangle2D(0, 0, BLOCK_SIZE_X, BLOCK_SIZE_Y));
        break;
    }
    getChildren().add(block);
    Game.platforms.add(this);
    Game.gameRoot.getChildren().add(this);
  }

  void moveBlock(Ninja player) {
    if (platformType == 1) {
      if (flagRoute) {
        if (this.getTranslateX() - startX <
            MOVE_BLOCK_LENGHT * BLOCK_SIZE_X) {
          this.setTranslateX(this.getTranslateX() + BLOCK_SPEED);
          if (player.getTranslateX() >= this.getTranslateX() -
              Game.NINJA_SIZE_Y / 2 &&
              player.getTranslateX() <= this.getTranslateX() +
                  BLOCK_SIZE_X - Game.NINJA_SIZE_Y / 2
              && player.getTranslateY() == this.getTranslateY() -
              Game.NINJA_SIZE_X - 1) {
            player.setTranslateX(player.getTranslateX() + BLOCK_SPEED);
          }
        } else {
          flagRoute = false;
        }
      } else {
        if (this.getTranslateX() > startX) {
          this.setTranslateX(this.getTranslateX() - BLOCK_SPEED);
          if (player.getTranslateX() >= this.getTranslateX() -
              Game.NINJA_SIZE_Y / 2 &&
              player.getTranslateX() <= this.getTranslateX() +
                  BLOCK_SIZE_X - Game.NINJA_SIZE_Y / 2
              && player.getTranslateY() == this.getTranslateY() -
              Game.NINJA_SIZE_X - 1) {
            player.setTranslateX(player.getTranslateX() - BLOCK_SPEED);
          }
        } else {
          flagRoute = true;
        }
      }
    }
  }
}




