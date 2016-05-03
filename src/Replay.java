import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class Replay {
  public static final String DIRECTORY = "Save\\";
  public static final String FORMAT = ".save";
  public static volatile List<String> commands;
  public static boolean firstRecord = true;
  public static boolean canSave = false;

  Path filePath;
  Path tempPath;


  public void record(int x, int y, int scale){
    try {
      if(firstRecord) {
        tempPath = Files.createTempFile("SJumper-", ".save");
        Files.write(tempPath,
                (Long.toString(System.currentTimeMillis()) + "\n").getBytes());
        firstRecord = false;
      }

      Files.write(tempPath, (Integer.toString(x) + " " + Integer.toString(y) +
                      " " + Integer.toString(scale) + " " +
                      Long.toString(System.currentTimeMillis())
                      + "\n").getBytes(), StandardOpenOption.APPEND);
      canSave = true;
    } catch(IOException e) {
      e.printStackTrace();
    }
  }

  public void save() {
    Date date = new Date();
    filePath = Paths.get(DIRECTORY + DateFormat.getDateTimeInstance(
        DateFormat.MEDIUM, DateFormat.MEDIUM).format(date).
        replace(":", ".") + FORMAT);
    try {
      Files.copy(tempPath, filePath);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void play(Path path){
    filePath = path;
    Game.replayFlag = false;
    Game.player.setOnStart();
    try {
      commands = Files.readAllLines(filePath);
    } catch (IOException e){
      e.printStackTrace();
    }

    Thread executeCommands;
    executeCommands = new Thread(new ExecuteCommands());
    executeCommands.start();
  }
}
