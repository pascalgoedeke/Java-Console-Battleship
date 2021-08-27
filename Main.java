import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main
{
  private static Screen screen;
  private static OpponentAI ai;
  private static Status status;

  private static ArrayList<Ship> shipsPlayer;
  private static ArrayList<Ship> shipsOpponent;

  private static ArrayList<Tile> oceanPlayer;
  private static ArrayList<Tile> oceanOpponent;

  public static void main(String[] args)
  {
    screen = new Screen();
    ai = new OpponentAI(screen);
    status = Status.PLACE;
    shipsPlayer = new ArrayList<>();
    shipsOpponent = generateRandomShips();
    oceanPlayer = new ArrayList<>();
    oceanOpponent = new ArrayList<>();

    // for testing
    // shipsPlayer = generateRandomShips();
    // status = Status.PLAY;

    Scanner scanner = new Scanner(System.in);

    // place ships
    while(status == Status.PLACE)
    {
      screen.update();
      System.out.println("Place your Ships");
      System.out.println("H - Horizontal, V - Vertical");
      
      boolean done = true;
      for(ShipType type : ShipType.values())
      {
        int left = type.getLimit();
        for(Ship ship : shipsPlayer)
          if(ship.getType() == type)
            left--;

        if(left > 0)
        {
          done = false;
          StringBuilder line = new StringBuilder();
          line.append(type).append(" ");
          for(int i = 0; i < type.getSize(); i++)
            line.append(type.getKey());
          line.append(" (").append(left).append(" left)");
          System.out.println(line);
        }
      }
      if(done)
      {
        status = Status.PLAY;
        continue;
      }

      String input = scanner.nextLine().toUpperCase();

      if(input.equals(""))
        continue;
      
      char key = input.charAt(0);
      char orientation = input.charAt(1);
      String[] sorted = input.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
      int inputX = (int)sorted[0].charAt(2) - 64;
      int inputY = Integer.valueOf(sorted[1]);

      ArrayList<Tile> tempLocations = new ArrayList<>();

      ShipType type = ShipType.getByKey(key);
      if(type == null)
        continue;
      
      int left = type.getLimit();
        for(Ship ship : shipsPlayer)
          if(ship.getType() == type)
            left--;
      if(left < 1)
        continue;

      if(orientation == 'H')
      {
        // horizontal
        for(int j = 0; j < type.getSize(); j++)
          tempLocations.add(new Tile(inputX + j, inputY));
      }
      else if(orientation == 'V')
      {
        // vertical
        for(int j = 0; j < type.getSize(); j++)
          tempLocations.add(new Tile(inputX, inputY + j));
      }
      else
        continue;
      
      boolean valid = true;
      for(Tile location : tempLocations)
      {
        if(!screen.doesTileExist(location.x(), location.y()))
        {
          valid = false;
          continue;
        }
        if(getPlayerShip(location.x(), location.y()) != null)
          valid = false;
      }
      if(!valid)
        continue;
      
      Tile[] locations = new Tile[tempLocations.size()];
      locations = tempLocations.toArray(locations);
      shipsPlayer.add(new Ship(type, locations));
    }

    // actual game
    while(status == Status.PLAY)
    {
      screen.update();

      System.out.println("Enter Torpedo Location");
      String input = scanner.nextLine().toUpperCase();

      if(input.equals(""))
        continue;
      
      String[] sorted = input.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
      int inputX = (int)sorted[0].charAt(0) - 64;
      int inputY = Integer.valueOf(sorted[1]);

      if(!screen.doesTileExist(inputX, inputY))
        continue;
      
      Ship guess = getOpponentShip(inputX, inputY);
      if(guess != null)
      {
        Tile tile = getTile(guess, inputX, inputY);
        if(tile.isHit())
          continue;
        
        tile.setHit(true);

        // check entire ship
        boolean entire = true;
        for(Tile t : guess.getArea())
          if(!t.isHit())
            entire = false;
        
        if(entire)
          guess.setSunken(true);
        
        // check for victory
        boolean won = true;
        for(Ship ship : shipsOpponent)
          if(!ship.isSunken())
            won = false;
        
        if(won)
        {
          status = Status.VICTORY;
          continue;
        }
      }
      else
      {
        if(!isUncoveredOceanOpponent(inputX, inputY))
          oceanOpponent.add(new Tile(inputX, inputY));
        else
          continue;
      }

      // computer's move
      Tile move = ai.nextMove();
      int moveX = move.x();
      int moveY = move.y();

      guess = getPlayerShip(moveX, moveY);
      if(guess != null)
      {
        Tile tile = getTile(guess, moveX, moveY);
        if(!tile.isHit())        
          tile.setHit(true);

        // check entire ship
        boolean entire = true;
        for(Tile t : guess.getArea())
          if(!t.isHit())
            entire = false;
        
        if(entire)
        {
          guess.setSunken(true);
          ai.removeShip(guess);
        }
        
        // check for defeat
        boolean defeat = true;
        for(Ship ship : shipsPlayer)
          if(!ship.isSunken())
            defeat = false;
        
        if(defeat)
        {
          status = Status.DEFEAT;
          continue;
        }
      }
      else
      {
        if(!isUncoveredOceanPlayer(moveX, moveY))
          oceanPlayer.add(new Tile(moveX, moveY));
      }
    }

    screen.update();

    if(status == Status.VICTORY)
    {
      System.out.println("Victory");
    }
    else if(status == Status.DEFEAT)
    {
      System.out.println("Defeat");
    }

    scanner.close();
  }

  public static ArrayList<Ship> generateRandomShips()
  {
    ArrayList<Ship> list = new ArrayList<>();

    for(ShipType type : ShipType.values())
    {
      for(int i = 0; i < type.getLimit(); i++)
      {
        boolean valid = false;
        while(!valid)
        {
          int randX = randomInt(1, screen.getWidth());
          int randY = randomInt(1, screen.getHeight());
          ArrayList<Tile> tempLocations = new ArrayList<>();

          // orientation
          if(randomInt(0, 1) == 0)
          {
            // horizontal
            for(int j = 0; j < type.getSize(); j++)
              tempLocations.add(new Tile(randX + j, randY));
          }
          else
          {
            // vertical
            for(int j = 0; j < type.getSize(); j++)
              tempLocations.add(new Tile(randX, randY + j));
          }

          valid = true;
          for(Tile location : tempLocations)
          {
            if(!screen.doesTileExist(location.x(), location.y()))
            {
              valid = false;
              continue;
            }
            if(getShip(list, location.x(), location.y()) != null)
              valid = false;
          }

          if(valid)
          {
            Tile[] locations = new Tile[tempLocations.size()];
            locations = tempLocations.toArray(locations);
            list.add(new Ship(type, locations));
          }
        }
      }
    }

    return list;
  }

  public static Ship getPlayerShip(int x, int y)
  {
    return getShip(shipsPlayer, x, y);
  }

  public static Ship getOpponentShip(int x, int y)
  {
    return getShip(shipsOpponent, x, y);
  }

  public static Ship getShip(ArrayList<Ship> list, int x, int y)
  {
    for(Ship ship : list)
      for(Tile tile : ship.getArea())
        if(tile.x() == x && tile.y() == y)
          return ship;
    
    return null;
  }

  public static Tile getTile(Ship ship, int x, int y)
  {
    Tile[] area = ship.getArea();
    for(Tile tile : area)
      if(tile.x() == x && tile.y() == y)
        return tile;
    
    return null;
  }

  public static boolean isUncoveredOceanPlayer(int x, int y)
  {
    boolean exists = false;
    for(Tile tile : oceanPlayer)
      if(tile.x() == x && tile.y() == y)
        exists = true;
    
    return exists;
  }

  public static boolean isUncoveredOceanOpponent(int x, int y)
  {
    boolean exists = false;
    for(Tile tile : oceanOpponent)
      if(tile.x() == x && tile.y() == y)
        exists = true;
    
    return exists;
  }

  public static int randomInt(int min, int max)
  {
    Random random = new Random();
    return random.nextInt((max - min) + 1) + min;
  }
}