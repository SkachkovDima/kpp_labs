package sjumper;

import javafx.scene.layout.VBox;
import sjumper.MenuItem;

public class SubMenu extends VBox {

  private static final int SPACING = 15;
  private static final int COORDINATE_Y = 250;
  static final int COORDINATE_X = 380;

  public SubMenu(MenuItem... items) {
    setSpacing(SPACING);
    setTranslateY(COORDINATE_Y);
    setTranslateX(COORDINATE_X);
    for (MenuItem item : items) {
      getChildren().addAll(item);
    }
  }
}
