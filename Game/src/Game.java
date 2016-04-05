import javafx.animation.AnimationTimer;
import  javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;

public class Game extends Application {
    public static ArrayList<Block> platforms = new ArrayList<>();
    private HashMap<KeyCode,Boolean> keys = new HashMap<>();
    private int i;

    Image backgroundImg = new Image(getClass().getResourceAsStream("background.jpg"));
    public static final int BLOCK_SIZE_X = 76;
    public static final int BLOCK_SIZE_Y = 17;
    public static final int NINJA_SIZE_Y = 40;
    public static final int NINJA_SIZE_X = 49;
    public static final int BACKGROUND_SIZE_X = 2560;
    public static final int BACKGROUND_SIZE_Y = 620;
    public static final int BACKGROUND_CHANGE = 640;
    public static final int SCENE_SIZE = 1200;

    public static Pane appRoot = new Pane();
    public static Pane gameRoot = new Pane();

    public WorldInformation levelData = new WorldInformation();

    public Ninja player;
    int levelNumber = 0;
    private int levelWidth = 14*BLOCK_SIZE_Y;

    private void initContent(){  // разделить на две. Надо чтобы контент обновлялся, но переменные не переприсваивались
                                 // тысячу раз
        ImageView backgroundIV = new ImageView(backgroundImg);
        backgroundIV.setFitHeight(BACKGROUND_SIZE_Y);
        backgroundIV.setFitWidth(BACKGROUND_SIZE_X);

        levelWidth = WorldInformation.levels[levelNumber][0].length()*BLOCK_SIZE_X;
        for(int i = 0; i < WorldInformation.levels[levelNumber].length; i++){
            String line = WorldInformation.levels[levelNumber][i];
            for(int j = 0; j < line.length();j++){
                switch (line.charAt(j)){
                    case '0':
                        break;
                    case '1':
                        Block platformFloor = new Block(Block.BlockType.PLATFORM, j * BLOCK_SIZE_X, i * 45);
                        break;
                    case '2':
                        Block brick = new Block(Block.BlockType.MOVE_PLATFORM, j*BLOCK_SIZE_X,i * 45);
                        break;
                }
            }

        }

        player = new Ninja();
        player.setTranslateX(0);
        player.setTranslateY(400);
        player.translateXProperty().addListener((obs,old,newValue)->{
            int offset = newValue.intValue();
            if(offset>BACKGROUND_CHANGE && offset<levelWidth-BACKGROUND_CHANGE){
                gameRoot.setLayoutX(-(offset-BACKGROUND_CHANGE));
                backgroundIV.setLayoutX(-(offset-BACKGROUND_CHANGE));
            }
        });
        gameRoot.getChildren().add(player);
        appRoot.getChildren().addAll(backgroundIV,gameRoot);
    }

    private void update(){
        if(isPressed(KeyCode.UP) && player.getTranslateY()>=5){
            player.jumpPlayer();
        }
        if(isPressed(KeyCode.LEFT) && player.getTranslateX()>=5){
            player.setScaleX(-1);
            player.animation.play();
            player.moveX(-5);
        }
        if(isPressed(KeyCode.RIGHT)){
            player.setScaleX(1);
            player.animation.play();
            player.moveX(5);
        }
        if(player.playerVelocity.getY()<10){
            player.playerVelocity = player.playerVelocity.add(0,1);
        }
        player.moveY((int)player.playerVelocity.getY());
        for(Block block : platforms) {
            block.moveBlock(player);
        }
    }

    private boolean isPressed(KeyCode key){
        return keys.getOrDefault(key,false);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        initContent();
        Scene scene = new Scene(appRoot,SCENE_SIZE,BACKGROUND_SIZE_Y);
        scene.setOnKeyPressed(event-> keys.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> {
            keys.put(event.getCode(), false);
            player.animation.stop();
        });
        primaryStage.setTitle("SJumper");
        primaryStage.setScene(scene);
        primaryStage.show();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        timer.start();
    }
    public static void main(String[] args) {
        launch(args);
    }
}