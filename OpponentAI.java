import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;

class OpponentAI
{
  private final Screen screen;
  private final HashMap<Tile, Ship> leftover;

  public OpponentAI(Screen screen)
  {
    this.screen = screen;
    this.leftover = new HashMap<>();
  }

  public Tile nextMove()
  {
    Tile move = null;

    if(leftover.isEmpty())
    {
      boolean valid = false;
      while(!valid)
      {
        int randX = Main.randomInt(1, screen.getWidth());
        int randY = Main.randomInt(1, screen.getHeight());

        valid = true;
        if(Main.isUncoveredOceanPlayer(randX, randY))
          valid = false;

        Ship ship = Main.getPlayerShip(randX, randY);
        if(ship != null)
          if(Main.getTile(ship, randX, randY).isHit())
            valid = false;
        
        if(valid)
          move = new Tile(randX, randY);
        else
          continue;
      }
    }
    else
    {
      // try to find a next move for each leftover
      for(Tile t : leftover.keySet())
      {
        if(move != null)
          break;

        // only two patterns when there are two Tiles of the same ship
        // (direction is obvious by this point)

        Ship ship = Main.getPlayerShip(t.x(), t.y());

        // find Tiles of same ship
        ArrayList<Tile> same = new ArrayList<>();
        for(Map.Entry<Tile, Ship> entry : leftover.entrySet())
        {
          Tile key = entry.getKey();
          Ship value = entry.getValue();

          if(value == ship)
            same.add(key);
        }

        ArrayList<int[]> patterns = new ArrayList<>();
        
        if(same.size() > 1)
        {
          // use two Tiles to calculate direction
          Tile calc1 = same.get(0);
          Tile calc2 = same.get(1);

          if(calc1.x() - calc2.x() == 0)
          {
            // same x
            patterns.add(new int[]{0, -1});
            patterns.add(new int[]{0, 1});
          }
          else
          {
            // same y
            patterns.add(new int[]{-1, 0});
            patterns.add(new int[]{1, 0});
          }
        }
        else
        {
          // only one found, search in all directions
          patterns.add(new int[]{-1, 0});
          patterns.add(new int[]{1, 0});
          patterns.add(new int[]{0, -1});
          patterns.add(new int[]{0, 1});
        }

        // randomise search order
        Collections.shuffle(patterns);

        for(int[] pattern : patterns)
        {
          int x = t.x() + pattern[0];
          int y = t.y() + pattern[1];

          if(screen.doesTileExist(x, y))
          {
            if(!Main.isUncoveredOceanPlayer(x, y))
            {
              Ship newShip = Main.getPlayerShip(x, y);
              if(newShip == null)
              {
                move = new Tile(x, y);
                break;
              }
              else
              {
                if(!Main.getTile(newShip, x, y).isHit())
                {
                  move = new Tile(x, y);
                  break;
                }
              }
            }
          }
        }
      }
    }

    // if move is a new hit, add to leftover
    Ship ship = Main.getPlayerShip(move.x(), move.y());
    if(ship != null)
    {
      Tile tile = Main.getTile(ship, move.x(), move.y());
      if(!tile.isHit())
        leftover.put(move, ship);
    }

    return move;
  }

  public void removeShip(Ship ship)
  {
    ArrayList<Tile> remove = new ArrayList<>();

    for(Map.Entry<Tile, Ship> entry : leftover.entrySet())
    {
      Tile key = entry.getKey();
      Ship value = entry.getValue();

      if(value == ship)
        remove.add(key);
    }

    for(Tile tile : remove)
      leftover.remove(tile);
  }
}