package sjumper;

import java.util.Random;

public class Bot{
  private static final int BREAKPOINT = 50;
  private static final int LOSE_PERCENT = 30;

  static volatile int blockCounter = 0;

  private boolean endOfBlock = false;
  private boolean isJump;

  private boolean lose(){
    Random random = new Random();
    return(random.nextInt(100) <= LOSE_PERCENT);
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
        Server jump = new Server();
        jump.jumpNinja(Game.ninja);
        Game.replay.record((int) Game.ninja.getTranslateX(),
            (int) Game.ninja.getTranslateY(),
            (int) Game.ninja.getScaleX(), "UP");
        this.move(true);
        if(Game.ninja.getTranslateX() >= nextBlock.getTranslateX() + 1){
          endOfBlock = false;
        }
      }
    } else{
      Server jump = new Server();
      jump.jumpNinja(Game.ninja);
      Game.replay.record((int) Game.ninja.getTranslateX(),
          (int) Game.ninja.getTranslateY(), (int) Game.ninja.getScaleX(), "Up");
      if(Game.ninja.getTranslateY() <=
          nextBlock.getTranslateY() - Game.NINJA_SIZE_Y - 1){
        this.move(true);
        if(Game.ninja.getTranslateX() >=
            nextBlock.getTranslateX() - Game.NINJA_SIZE_X + 1){
          endOfBlock = false;
        }
      }
    }
  }

  private void moveOnBlock(){
    Block block = Game.platforms.get(blockCounter);
    Block nextBlock = Game.platforms.get(blockCounter + 1);
    if (Game.ninja.getTranslateX() >= block.getTranslateX() -
        Game.NINJA_SIZE_X + 1 && Game.ninja.getTranslateX()
        <= block.getTranslateX() + Block.BLOCK_SIZE_X - Game.MOVE_LENGHT + 1) {
      if (Game.ninja.getTranslateY() ==
          block.getTranslateY() - Game.NINJA_SIZE_Y - 1) {
        if(Game.ninja.getTranslateY() >
            nextBlock.getTranslateY() - Game.NINJA_SIZE_Y - 1){
          if(Game.ninja.getTranslateX() <= block.getTranslateX() +
              Block.BLOCK_SIZE_X - BREAKPOINT){
            this.move(true);
          } else{
            if(lose()){
              Server move = new Server();
              move.moveX(BREAKPOINT + 1, Game.ninja);
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
      Game.ninja.setScaleX(1);
      Game.ninja.animation.play();
      Server move = new Server();
      move.moveX(Game.MOVE_LENGHT, Game.ninja);
      Game.replay.record((int) Game.ninja.getTranslateX(),
          (int) Game.ninja.getTranslateY(),
          (int) Game.ninja.getScaleX(), "Right");
    } else {
      Game.ninja.setScaleX(-1);
      Game.ninja.animation.play();
      Server move = new Server();
      move.moveX(-Game.MOVE_LENGHT, Game.ninja);
      Game.replay.record((int) Game.ninja.getTranslateX(),
          (int) Game.ninja.getTranslateY(),
          (int) Game.ninja.getScaleX(), "Left");
    }
  }

  public void play(){
    Client check = new Client();
    check.checkEndOfLevel(Game.ninja);

    if(!endOfBlock) {
      moveOnBlock();
    } else{
      moveOnNextBlock();
    }
  }
}



//https://docs.google.com/document/d/1TthzE-U4g_qGZkwnwqu6SHquoahFELgAkJ0OdqlO92k/edit
