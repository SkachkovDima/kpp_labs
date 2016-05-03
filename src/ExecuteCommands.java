
public class ExecuteCommands implements Runnable{

      public void run() {
      long startTime = Long.parseLong(Replay.commands.get(0));
      Replay.commands.remove(0);
      String[] separeteCommands;
      try {
        for (String line : Replay.commands) {
          separeteCommands = line.split(" ");
          Thread.sleep((Long.parseLong(separeteCommands[3]) - startTime));
          Game.player.setScaleX(Integer.parseInt(separeteCommands[2]));
          Game.player.animation.play();
          Game.player.setTranslateX(Integer.parseInt(separeteCommands[0]));
          Game.player.setTranslateY(Integer.parseInt(separeteCommands[1]));
         if (Game.player.getTranslateX() > Game.gameRoot.getTranslateX() +
             Game.SCENE_SIZE / 2) {
          Game.gameRoot.setTranslateX(-Integer.parseInt(separeteCommands[0]) +
              Game.SCENE_SIZE/2);
         } else{
           Game.gameRoot.setTranslateX(0);
         }
          startTime = Long.parseLong(separeteCommands[3]);
        }
        Game.player.animation.stop();
        Game.humanGameFlag = true;
        Game.autoGameFlag = false;
      } catch (InterruptedException e) {
        System.err.print(e);
      }
    }
}
