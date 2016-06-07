package sjumper;

public class ExecuteCommands implements Runnable{
  public void run() {
    String[] separeteCommands;
    separeteCommands = Replay.commands.get(0).split(" ");

    long startTime = Long.parseLong(separeteCommands[0]);

    Game.ninja.setTranslateX(Ninja.START_X);
    Game.ninja.setTranslateY(Ninja.START_Y);
    Game.gameRoot.setTranslateX(Ninja.START_X);

    Replay.commands.remove(0);

    try {
      for (String line : Replay.commands) {
        separeteCommands = line.split(" ");

        Thread.sleep((Long.parseLong(separeteCommands[3]) - startTime));

        Game.ninja.setScaleX(Integer.parseInt(separeteCommands[2]));
        Game.ninja.animation.play();

        Game.ninja.setTranslateX(Integer.parseInt(separeteCommands[0]));
        Game.ninja.animation.play();

        Client check = new Client();
        check.checkEndOfLevel(Game.ninja);

        Game.ninja.setTranslateY(Integer.parseInt(separeteCommands[1]));
        Game.ninja.animation.play();

        if (Game.ninja.getTranslateX() > Game.gameRoot.getTranslateX() +
            Game.SCENE_SIZE / 2) {
          Game.gameRoot.setTranslateX(-Integer.parseInt(separeteCommands[0]) +
              Game.SCENE_SIZE / 2);
        } else {
          Game.gameRoot.setTranslateX(0);
        }
        startTime = Long.parseLong(separeteCommands[3]);
      }

      Game.ninja.animation.stop();
      Game.humanGameFlag = true;
      Game.autoGameFlag = false;
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
