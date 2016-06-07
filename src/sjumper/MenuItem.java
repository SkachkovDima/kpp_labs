package sjumper;

import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

class MenuItem extends StackPane{

  private static final int BUTTON_WIDTH = 400;
  private static final int BUTTON_HIGH = 40;
  private static final double OPACITY_BUTTON = 0.5;
  private static final double BUTTON_DURATION = 0.5;
  private static final String FONT = "Arial";
  private static final int FONT_SIZE = 14;

  MenuItem(String name) {
    Rectangle bg = new Rectangle(BUTTON_WIDTH, BUTTON_HIGH, Color.WHITE);
    bg.setOpacity(OPACITY_BUTTON);

    Text text = new Text(name);
    text.setFill(Color.WHITE);
    text.setFont(Font.font(FONT, FontWeight.BOLD, FONT_SIZE));
    setAlignment(Pos.CENTER);
    getChildren().addAll(bg, text);
    FillTransition st = new FillTransition(
        Duration.seconds(BUTTON_DURATION), bg);
    setOnMouseEntered(event -> {
      st.setFromValue(Color.DARKGRAY);
      st.setToValue(Color.DARKGOLDENROD);
      st.setCycleCount(Animation.INDEFINITE);
      st.setAutoReverse(true);
      st.play();
    });
    setOnMouseExited(event -> {
      st.stop();
      bg.setFill(Color.WHITE);
    });
  }
}
