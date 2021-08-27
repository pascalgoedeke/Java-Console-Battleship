import java.util.Map;
import java.util.HashMap;

enum ShipType
{
  CARRIER(5, 1, 'A', 'Ⓐ'),
  BATTLESHIP(4, 1, 'B', 'Ⓑ'),
  CRUISER(3, 1, 'C', 'Ⓒ'),
  SUBMARINE(3, 2, 'S', 'Ⓢ'),
  DESTROYER(2, 2, 'D', 'Ⓓ');

  private final int size;
  private final int limit;
  private final char key;
  private final char keySunken;

  ShipType(int size, int limit, char key, char keySunken)
  {
    this.size = size;
    this.limit = limit;
    this.key = key;
    this.keySunken = keySunken;
  }

  public int getSize()
  {
    return this.size;
  }

  public int getLimit()
  {
    return this.limit;
  }

  public char getKey()
  {
    return this.key;
  }

  public char getKeySunken()
  {
    return this.keySunken;
  }

  private static final Map<Character, ShipType> map;
  static {
      map = new HashMap<Character, ShipType>();
      for(ShipType t : ShipType.values()) {
          map.put(t.getKey(), t);
      }
  }
  public static ShipType getByKey(char k) {
      return map.get(k);
  }
}