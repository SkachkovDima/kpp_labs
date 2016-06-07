package sjumper;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;


public class Ninja extends Pane {


  private static final int DURATION_TIME = 600;
  static final int MOVING_DOWN = 640;
  static final int START_X = 0;
  static final int START_Y = 400;
  static final int PLAYER_VELOCITY_X = 0;
  static final int PLAYER_VELOCITY_Y = -30;
  static final int POINT_2D_X = 0;
  static final int POINT_2D_Y = 10;
  static boolean canJump = true;
  public volatile SpriteAnimation animation;
  volatile Point2D playerVelocity = new Point2D(0, 0);


  public Ninja() {
    Image ninjaImg = new Image(getClass().getResourceAsStream("ninja.png"));
    ImageView imageView = new ImageView(ninjaImg);
    imageView.setFitHeight(Game.NINJA_SIZE_Y);
    imageView.setFitWidth(Game.NINJA_SIZE_X);
    int count = 20, columns = 20, offsetX = 0, offsetY = 0, height = 49;
    double width = 39.5;
    imageView.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
    animation = new SpriteAnimation(imageView,
        Duration.millis(DURATION_TIME),
        count, columns, offsetX, offsetY, width, height);
    getChildren().addAll(imageView);
  }

  /**
   * <p>Перемещает ninja по горизонтали</p>
   * <p>Чтобы переместить игрока на MOVE_LENGHT вправо напишите
   * move(MOVE_LENGHT); Чтобы переместить игрока влево введите
   * отрицательное значение.</p>
   * <p>Учитывает соприкосновения с блоками. Не дает проходить сквозь них</p>
   *
   * @param value Количество пикселей, на которое перемещается ninja
   */
//  public void moveX(int value) {
//    boolean movingRight = value > 0;
//    for (int i = 0; i < Math.abs(value); i++) {
//      for (Node platform : sjumper.Game.platforms) {
//        if (this.getBoundsInParent().intersects(
//                platform.getBoundsInParent())) {
//          if (movingRight) {
//            if (this.getTranslateX() ==
//                    platform.getTranslateX()) {
//              this.setTranslateX(this.getTranslateX() - 1);
//              return;
//            }
//          } else {
//            if (this.getTranslateX() == platform.getTranslateX() +
//                    sjumper.Block.BLOCK_SIZE_X) {
//              this.setTranslateX(this.getTranslateX() + 1);
//              return;
//            }
//          }
//        }
//      }
//      this.setTranslateX(this.getTranslateX() + (movingRight ? 1 : -1));
//    }
//    if (this.getTranslateX() > sjumper.Game.gameRoot.getTranslateX() +
//            sjumper.Game.SCENE_SIZE / 2) {
//      sjumper.Game.gameRoot.setTranslateX(sjumper.Game.gameRoot.getTranslateX() - value);
//    }
//  }

  /**
   * <p>Перемещает ninja по вертикали</p>
   * <p>Учитывает соприкосновения с блоками: ninja стоит на блоках и при
   * прыжке не может прыгнуть сквозь блок</p>
   *
   * @param value Количество пикселей, на которое перемещается ninja
   */
//  public void moveY(int value) {
//    boolean movingDown = value > 0;
//    for (int i = 0; i < Math.abs(value); i++) {
//      for (sjumper.Block platform : sjumper.Game.platforms) {
//        if (getBoundsInParent().intersects(platform.getBoundsInParent())) {
//          if (movingDown) {
//            if (this.getTranslateY() + sjumper.Game.NINJA_SIZE_Y ==
//                platform.getTranslateY()) {
//              this.setTranslateY(this.getTranslateY() - 1);
//              canJump = true;
//              return;
//            }
//          } else {
//            if (this.getTranslateY() == platform.getTranslateY() +
//                sjumper.Block.BLOCK_SIZE_Y) {
//              this.setTranslateY(this.getTranslateY() + 1);
//              playerVelocity = new Point2D(POINT_2D_X, POINT_2D_Y);
//              return;
//            }
//          }
//        }
//      }
//      this.setTranslateY(this.getTranslateY() + (movingDown ? 1 : -1));
//      if (this.getTranslateY() > MOVING_DOWN) {
//        setOnStart();
//      }
//    }
//  }

  /**
   * <p>Перемещает ninja на старт</p>
   * <p>При вызове этого метода levelNumber изменяется на 0,
   * и ninja перемещается на старт</p>
   */
//  public void setOnStart() {
//    this.setTranslateX(START_X);
//    this.setTranslateY(START_Y);
//    sjumper.Game.gameRoot.setTranslateX(START_X);
//    sjumper.Bot.blockCounter = 0;
//    sjumper.Game.levelNumber = 0;
//    sjumper.Game.updateLevel();
//  }

  /**
   * <p>Прыжок ninja</p>
   * <p>Проверяет, возможен ли прыжок. Если возможен, то ninja прыгает</p>
   */
//  public void jumpPlayer() {
//    if (canJump) {
//      playerVelocity = playerVelocity.add(PLAYER_VELOCITY_X, PLAYER_VELOCITY_Y);
//      canJump = false;
//    }
//  }
}
