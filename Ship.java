class Ship
{
  private final ShipType type;
  private final Tile[] area;
  private boolean sunken;

  public Ship(ShipType type, Tile[] area)
  {
    this.type = type;
    this.area = area;
    this.sunken = false;
  }

  public ShipType getType()
  {
    return this.type;
  }

  public Tile[] getArea()
  {
    return this.area;
  }

  public boolean isSunken()
  {
    return this.sunken;
  }

  public void setSunken(boolean sunken)
  {
    this.sunken = sunken;
  }
}