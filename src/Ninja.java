import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;


public class Ninja extends Pane {
  Image ninjaImg = new Image(getClass().getResourceAsStream("ninja.png"));
  ImageView imageView = new ImageView(ninjaImg);
  int count = 20;
  int columns = 20;
  int offsetX = 0;
  int offsetY = 0;
  double width = 39.5;
  int height = 49;
  public static final int DURATION_TIME = 600;
  public static final int MOVING_DOWN = 640;
  public static final int START_X = 0;
  public static final int START_Y = 400;
  public static final int PLAYER_VELOCITY_X = 0;
  public static final int PLAYER_VELOCITY_Y = -30;
  public static final int POINT_2D_X = 0;
  public static final int POINT_2D_Y = 10;
  public SpriteAnimation animation;
  public Point2D playerVelocity = new Point2D(0, 0);
  private boolean canJump = true;

  public Ninja() {
    imageView.setFitHeight(Game.NINJA_SIZE_Y);
    imageView.setFitWidth(Game.NINJA_SIZE_X);
    imageView.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
    animation = new SpriteAnimation(this.imageView,
        Duration.millis(DURATION_TIME),
        count, columns, offsetX, offsetY, width, height);
    getChildren().addAll(this.imageView);
  }

  public void moveX(int value) {
    boolean movingRight = value > 0;
    for (int i = 0; i < Math.abs(value); i++) {
      for (Node platform : Game.platforms) {
        if (this.getBoundsInParent().intersects(
            platform.getBoundsInParent())) {
          if (movingRight) {
            if (this.getTranslateX() + Game.NINJA_SIZE_X ==
                platform.getTranslateX()) {
              this.setTranslateX(this.getTranslateX() - 1);
              return;
            }
          } else {
            if (this.getTranslateX() == platform.getTranslateX() +
                Game.BLOCK_SIZE_X) {
              this.setTranslateX(this.getTranslateX() + 1);
              return;
            }
          }
        }
      }
      this.setTranslateX(this.getTranslateX() + (movingRight ? 1 : -1));
    }
  }

  public void moveY(int value) {
    boolean movingDown = value > 0;
    for (int i = 0; i < Math.abs(value); i++) {
      for (Block platform : Game.platforms) {
        if (getBoundsInParent().intersects(platform.getBoundsInParent())) {
          if (movingDown) {
            if (this.getTranslateY() + Game.NINJA_SIZE_Y ==
                platform.getTranslateY()) {
              this.setTranslateY(this.getTranslateY() - 1);
              canJump = true;
              return;
            }
          } else {
            if (this.getTranslateY() == platform.getTranslateY() +
                Game.BLOCK_SIZE_Y) {
              this.setTranslateY(this.getTranslateY() + 1);
              playerVelocity = new Point2D(POINT_2D_X, POINT_2D_Y);
              return;
            }
          }
        }
      }
      this.setTranslateY(this.getTranslateY() + (movingDown ? 1 : -1));
      if (this.getTranslateY() > MOVING_DOWN) {
        this.setTranslateX(START_X);
        this.setTranslateY(START_Y);
        Game.gameRoot.setLayoutX(START_X);
      }
    }
  }

  public void jumpPlayer() {
    if (canJump) {
      playerVelocity = playerVelocity.add(PLAYER_VELOCITY_X, PLAYER_VELOCITY_Y);
      canJump = false;
    }
  }
}
