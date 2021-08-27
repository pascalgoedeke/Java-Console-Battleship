class Screen
{
  // dimensions
  private final int height = 10;
  private final int width = 10;

  // characters for grid
  private final char borderUpperLeft = '╔';
  private final char borderUpperRight = '╗';
  private final char borderLowerLeft = '╚';
  private final char borderLowerRight = '╝';
  private final char borderHorizontal = '═';
  private final char borderVertical = '║'; 
  private final char borderHorizontalConnectDown = '╤';
  private final char borderHorizontalConnectUp = '╧';
  private final char borderVerticalConnectRight = '╟';
  private final char borderVerticalConnectLeft = '╢';
  private final char innerHorizontal = '─';
  private final char innerVertical = '│';
  private final char innerConnect = '┼';

  private final char ocean = '~';

  public void update()
  {
    // clear console
    System.out.print("\033[H\033[2J");
    System.out.flush();

    // calculate offset depending on number length
    int offset = String.valueOf(width).length();

    // grid coorinates letters
    StringBuilder letters = new StringBuilder();
    for(int i = 0; i < offset + 2; i++)
      letters.append(" ");
    
    for(int i = 0; i < width; i++)
      letters.append(((char)(i + 65)) + " ");

    System.out.println(letters.toString() + " " + letters.toString());

    // grid top border
    StringBuilder top = new StringBuilder();
    for(int i = 0; i < offset + 1; i++)
      top.append(" ");
    top.append(borderUpperLeft);

    for(int i = 0; i < width * 2 - 1; i++)
    {
      if(i % 2 == 0)
        top.append(borderHorizontal);
      else if(i % 2 == 1)
        top.append(borderHorizontalConnectDown);
    }
    top.append(borderUpperRight);

    System.out.println(top.toString() + " " + top.toString());

    // grid horizontal spacers
    StringBuilder spacer = new StringBuilder();
    for(int i = 0; i < offset + 1; i++)
      spacer.append(" ");
    spacer.append(borderVerticalConnectRight);

    for(int i = 0; i < width * 2 - 1; i++)
    {
      if(i % 2 == 0)
        spacer.append(innerHorizontal);
      else if(i % 2 == 1)
        spacer.append(innerConnect);
    }
    spacer.append(borderVerticalConnectLeft);

    for(int i = 0; i < offset + 2; i++)
      spacer.append(" ");
    spacer.append(borderVerticalConnectRight);

    for(int i = 0; i < width * 2 - 1; i++)
    {
      if(i % 2 == 0)
        spacer.append(innerHorizontal);
      else if(i % 2 == 1)
        spacer.append(innerConnect);
    }
    spacer.append(borderVerticalConnectLeft);

    // actual content
    for(int i = 0; i < height * 2 - 1; i++)
    {
      if(i % 2 == 0)
      {
        StringBuilder line = new StringBuilder();
        int number = i / 2 + 1;

        // left grid
        for(int k = 0; k < offset - String.valueOf(number).length(); k++)
          line.append(" ");
        line.append(number).append(" ").append(borderVertical);
        for(int j = 0; j < width * 2 - 1; j++)
        {
          if(j % 2 == 0)
          {
            int x = j / 2 + 1;
            int y = i / 2 + 1;

            Ship ship = Main.getPlayerShip(x, y);
            if(ship != null)
            {
              if(Main.getTile(ship, x, y).isHit())
                line.append(ship.getType().getKeySunken());
              else
                line.append(ship.getType().getKey());
            }
            else
            {
              if(Main.isUncoveredOceanPlayer(x, y))
                line.append(ocean);
              else
                line.append(" ");
            }
          }
          else if(j % 2 == 1)
            line.append(innerVertical);
        }
        line.append(borderVertical).append(" ");
        
        // right grid
        for(int k = 0; k < offset - String.valueOf(number).length(); k++)
          line.append(" ");
        line.append(number).append(" ").append(borderVertical);
        for(int j = 0; j < width * 2 - 1; j++)
        {
          if(j % 2 == 0)
          {
            int x = j / 2 + 1;
            int y = i / 2 + 1;

            Ship ship = Main.getOpponentShip(x, y);
            if(ship != null)
            {
              if(Main.getTile(ship, x, y).isHit())
              {
                if(ship.isSunken())
                  line.append(ship.getType().getKeySunken());
                else
                  line.append(ship.getType().getKey());
              }
              else
                line.append(" ");
            }
            else
            {
              if(Main.isUncoveredOceanOpponent(x, y))
                line.append(ocean);
              else
                line.append(" ");
            }
          }
          else if(j % 2 == 1)
            line.append(innerVertical);
        }
        line.append(borderVertical).append(" ").append(number);
        for(int k = 0; k < offset - String.valueOf(number).length(); k++)
          line.append(" ");

        System.out.println(line);
      }
      else if(i % 2 == 1)
        System.out.println(spacer);
    }

    // grid bottom border
    StringBuilder bottom = new StringBuilder();
    for(int i = 0; i < offset + 1; i++)
      bottom.append(" ");
    bottom.append(borderLowerLeft);

    for(int i = 0; i < width * 2 - 1; i++)
    {
      if(i % 2 == 0)
        bottom.append(borderHorizontal);
      else if(i % 2 == 1)
        bottom.append(borderHorizontalConnectUp);
    }
    bottom.append(borderLowerRight);

    System.out.println(bottom.toString() + " " + bottom.toString());

    System.out.println(letters.toString() + " " + letters.toString());
  }

  public boolean doesTileExist(int x, int y)
  {
    if(x > 0 && y > 0 && x < width + 1 && y < height + 1)
      return true;
    else
      return false;
  }

  public int getHeight()
  {
    return this.height;
  }

  public int getWidth()
  {
    return this.width;
  }
}