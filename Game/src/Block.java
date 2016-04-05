import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Block extends Pane {
    Image blocksImg = new Image(getClass().getResourceAsStream("blockBlack.jpg"));
    ImageView block;

    public enum BlockType {
        PLATFORM, MOVE_PLATFORM
    }

    int startX, startY;
    int platformType;

    public Block(BlockType blockType, int x, int y) {
        block = new ImageView(blocksImg);
        block.setFitWidth(Game.BLOCK_SIZE_X);
        block.setFitHeight(Game.BLOCK_SIZE_Y);
        startX = x;
        startY = y;
        setTranslateX(x);
        setTranslateY(y);

        switch (blockType) {
            case PLATFORM:
                platformType = 0;
                block.setViewport(new Rectangle2D(0, 0, 73, 17));
                break;
            case MOVE_PLATFORM:
                platformType = 1;
                block.setViewport(new Rectangle2D(0, 0, 73, 17));
                break;
        }
        getChildren().add(block);
        Game.platforms.add(this);
        Game.gameRoot.getChildren().add(this);
    }

    boolean flagRoute = true;

    public void moveBlock(Ninja player) {
        if (platformType == 1) {
            if (flagRoute) {
                if (this.getTranslateX() - startX < 3 * Game.BLOCK_SIZE_X) {
                    this.setTranslateX(this.getTranslateX() + 2);
                    if (player.getTranslateX() >= this.getTranslateX() - Game.NINJA_SIZE_Y / 2 &&
                            player.getTranslateX() <= this.getTranslateX() + Game.BLOCK_SIZE_X -Game.NINJA_SIZE_Y / 2 &&
                            player.getTranslateY() == this.getTranslateY() - Game.NINJA_SIZE_X - 1) {
                        player.setTranslateX(player.getTranslateX() + 2);
                    }
                } else {
                    flagRoute = false;
                }
            } else {
                if (this.getTranslateX() > startX) {
                    this.setTranslateX(this.getTranslateX() - 2);
                    if (player.getTranslateX() >= this.getTranslateX() - Game.NINJA_SIZE_Y / 2 &&
                            player.getTranslateX() <= this.getTranslateX() + Game.BLOCK_SIZE_X -Game.NINJA_SIZE_Y / 2 &&
                            player.getTranslateY() == this.getTranslateY() - Game.NINJA_SIZE_X - 1) {
                        player.setTranslateX(player.getTranslateX() - 2);
                    }
                } else {
                    flagRoute = true;
                }
            }
        }
    }
}




