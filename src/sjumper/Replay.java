package sjumper;

import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.WorkerStateEvent;

import java.io.IOException;
import java.nio.channels.InterruptedByTimeoutException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Replay {
  static final String DIRECTORY = "Save/";
  static final String NOTATION = "Notation/";
  private static final String FORMAT = ".save";
  static volatile List<String> commands;
  static boolean firstRecord = true;
  static boolean canSave = false;

  private Path filePath;
  private Path tempPath;

  private SimpleStringProperty act;
  private SimpleStringProperty count;

  public Replay(String _act, int _count){
    act = new SimpleStringProperty(_act);
    count = new SimpleStringProperty(Integer.toString(_count));
  }

  public Replay(){}

  /**
   * <p>Record coordinates ninja in temporary file</p>
   *
   * @param x Coordinate X
   * @param y Coordinate Y
   * @param scale Display ninja
   */
  void record(int x, int y, int scale, String act){
    try {
      if(firstRecord) {
        tempPath = Files.createTempFile("sjumper.SJumper-", ".save");
        Files.write(tempPath,
                (Long.toString(System.currentTimeMillis()) + " " +
                    Integer.toString(Game.levelNumber) + "\n").getBytes());
        firstRecord = false;
      }

      Files.write(tempPath, (Integer.toString(x) + " " + Integer.toString(y) +
                      " " + Integer.toString(scale) + " " +
                      Long.toString(System.currentTimeMillis()) + " " + act +
                      "\n").getBytes(), StandardOpenOption.APPEND);
      canSave = true;
    } catch(IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * <p>Copy information from temporary file to permanent</p>
   * <p>Name of permanent file - date and time</p>
   */
  void save() {
//    double score = Game.lastSucced;
//    for(int i = 0; i < Game.lastLevel; i++){
//      score += WorldInformation.levels[i].length;
//    }
//    score *=  100.0 / WorldInformation.getLengths();
    Date date = new Date();
    filePath = Paths.get(DIRECTORY + DateFormat.getDateTimeInstance(
        DateFormat.MEDIUM, DateFormat.MEDIUM).format(date).
        replace(":", ".") + " " + Integer.toString(
        (int)Game.ninja.getTranslateX() * 100 /
            (WorldInformation.levels[Game.levelNumber].length *
                Block.BLOCK_SIZE_X)) + FORMAT);
    try {
      Files.copy(tempPath, filePath);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * <p>Play information from file</p>
   * <p>Read information from file and execute commands in new Thread</p>
   * @see ExecuteCommands
   *
   * @param path File path
   */
  public void play(Path path){
    filePath = path;
    Game.replayFlag = false;
    Server ninjaOnStart = new Server();
    ninjaOnStart.setOnStart(Game.ninja);
    Game.setMenu = false;
    try {
      commands = Files.readAllLines(filePath);
    } catch (IOException e){
      e.printStackTrace();
    }

    String separeteCommands[] = commands.get(0).split(" ");
    Game.levelNumber = Integer.parseInt(separeteCommands[1]);

    Client client = new Client();
    client.updateLevel();

    Thread executeCommands;
    executeCommands = new Thread(new ExecuteCommands());
    executeCommands.start();
  }

  public String getAct(){
    return act.get();
  }

  public int getCount(){
    return Integer.parseInt(count.get());
  }

  public void setAct(String _act){
    act.set(_act);
  }

  public void setCount(int _count){
    count.set(Integer.toString(_count));
  }

  void makeNotation(String fileName){
    try{
      Path file = Paths.get(DIRECTORY + fileName);
      Path notation = Paths.get(NOTATION + "notation " + fileName);
      Statistics stat = new Statistics();

      List<String> strings = Files.readAllLines(file);
      String[] separateCommands = strings.get(0).split(" ");
      long startTime = Long.parseLong(separateCommands[0]);
      strings.remove(0);

      Files.write(notation, ("Level: "+ (Integer.parseInt(separateCommands[1])
          + 1) + "\n").getBytes());

      double prevX = 0, prevY = 0;
      String act;
      for(String string : strings){
        separateCommands = string.split(" ");
        act = stat.makeNotationX(
            Double.parseDouble(separateCommands[0]) - prevX);
        if(!act.equals("None")){
          Files.write(notation, (act + " after " +
              (Long.parseLong(separateCommands[3]) - startTime) +
              " msec\n").getBytes(), StandardOpenOption.APPEND);
        }

        act = stat.makeNotationY(
            Double.parseDouble(separateCommands[1]) - prevY);
        if(!act.equals("None")){
          Files.write(notation, (act + " after " +
              (Long.parseLong(separateCommands[3]) - startTime) +
              " msec\n").getBytes(), StandardOpenOption.APPEND);
        }

        prevX = Double.parseDouble(separateCommands[0]);
        prevY = Double.parseDouble(separateCommands[1]);
        startTime = Long.parseLong(separateCommands[3]);
      }
    } catch (IOException e){
      e.printStackTrace();
    }
  }
}
