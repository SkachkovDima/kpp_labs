package sjumper;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class SpriteAnimation extends Transition {
  private final ImageView imageView;
  private final int count;
  private final int columns;
  private int offsetX;
  private int offsetY;
  private final double width;
  private final int height;

  public SpriteAnimation(
      ImageView imageView,
      Duration duration,
      int count, int columns,
      int offsetX, int offsetY,
      double width, int height
  ) {
    this.imageView = imageView;
    this.count = count;
    this.columns = columns;
    this.offsetX = offsetX;
    this.offsetY = offsetY;
    this.width = width;
    this.height = height;
    setCycleDuration(duration);
    setCycleCount(Animation.INDEFINITE);
    setInterpolator(Interpolator.LINEAR);
    this.imageView.setViewport(new Rectangle2D
        (offsetX, offsetY, width, height));

  }

  protected void interpolate(double frac) {
    final int index = Math.min((int) Math.floor(count * frac), count - 1);
    final double x = (index % columns) * width + offsetX;
    final int y = (index / columns) * height + offsetY;
    imageView.setViewport(new Rectangle2D(x, y, width, height));
  }
}
