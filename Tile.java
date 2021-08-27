class Tile
{
  private final int x;
  private final int y;
  private boolean hit;

  public Tile(int x, int y)
  {
    this.x = x;
    this.y = y;
    this.hit = false;
  }

  public int x()
  {
    return this.x;
  }

  public int y()
  {
    return this.y;
  }

  public boolean isHit()
  {
    return this.hit;
  }

  public void setHit(boolean hit)
  {
    this.hit = hit;
  }
}