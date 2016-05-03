import java.util.Random;

public class Bot{
  public static final int BREAKPOINT = 50;
  public static final int LOSE_PERCENT = 10;

  public static int blockCounter = 0;

  boolean endOfBlock = false;
  boolean isJump;

  private boolean lose(){
    Random random = new Random();
    return(random.nextInt(100) <= LOSE_PERCENT);
  }

  private void checkEndOfLevel(){
    Game.checkEndOfLevel();
    if(Game.player.getTranslateX() == Ninja.START_X &&
            Game.player.getTranslateY() == Ninja.START_Y) {
      blockCounter = 0;
    }
  }

  private void moveOnNextBlock(){
    Block nextBlock = Game.platforms.get(blockCounter);
    if(!isJump){
      if(nextBlock.getTranslateX() -
          Game.platforms.get(blockCounter - 1).getTranslateX() ==
          Block.BLOCK_SIZE_X) {
        this.move(true);
        endOfBlock = false;
      } else{
        Game.player.jumpPlayer();
        Game.replay.record((int)Game.player.getTranslateX(),
            (int)Game.player.getTranslateY(), (int)Game.player.getScaleX());
        this.move(true);
        if(Game.player.getTranslateX() >= nextBlock.getTranslateX() + 1){
          endOfBlock = false;
        }
      }
    } else{
      Game.player.jumpPlayer();
      Game.replay.record((int)Game.player.getTranslateX(),
          (int)Game.player.getTranslateY(), (int)Game.player.getScaleX());
      if(Game.player.getTranslateY() <=
          nextBlock.getTranslateY() - Game.NINJA_SIZE_Y - 1){
        this.move(true);
        if(Game.player.getTranslateX() >=
            nextBlock.getTranslateX() - Game.NINJA_SIZE_X + 1){
          endOfBlock = false;
        }
      }
    }
  }

  private void moveOnBlock(){
    Block block = Game.platforms.get(blockCounter);
    Block nextBlock = Game.platforms.get(blockCounter + 1);
    if (Game.player.getTranslateX() >= block.getTranslateX() -
        Game.NINJA_SIZE_X + 1 && Game.player.getTranslateX()
        <= block.getTranslateX() + Block.BLOCK_SIZE_X - Game.MOVE_LENGHT + 1) {
      if (Game.player.getTranslateY() ==
          block.getTranslateY() - Game.NINJA_SIZE_Y - 1) {
        if(Game.player.getTranslateY() >
            nextBlock.getTranslateY() - Game.NINJA_SIZE_Y - 1){
          if(Game.player.getTranslateX() <= block.getTranslateX() +
              Block.BLOCK_SIZE_X - BREAKPOINT){
            this.move(true);
          } else{
            if(lose()){
              Game.player.moveX(BREAKPOINT + 1);
              return;
            }
            endOfBlock = true;
            isJump = true;
            blockCounter++;
          }
        } else{
          this.move(true);
        }
      }
    } else {
      if(lose()){
        this.move(true);
      }
      endOfBlock = true;
      isJump = false;
      blockCounter++;
    }
  }

  private void move(boolean moveRight){
    if(moveRight){
      Game.player.setScaleX(1);
      Game.player.animation.play();
      Game.player.moveX(Game.MOVE_LENGHT);
      Game.replay.record((int)Game.player.getTranslateX(),
          (int)Game.player.getTranslateY(), (int)Game.player.getScaleX());
    } else {
      Game.player.setScaleX(-1);
      Game.player.animation.play();
      Game.player.moveX(-Game.MOVE_LENGHT);
      Game.replay.record((int)Game.player.getTranslateX(),
          (int)Game.player.getTranslateY(), (int)Game.player.getScaleX());
    }
  }

  public void play(){
    checkEndOfLevel();

    if(!endOfBlock) {
      moveOnBlock();
    } else{
      moveOnNextBlock();
    }
  }
}
