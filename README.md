# Java-Console-Battleship
Battleship Game in Java Console with Opponent AI

## Place ships  
Before you start playing, you need to place your ships on the left board.

1. Find the letter of the ship you want to place (e.g. A, B, ...)
2. Select an orientation (H - Horizontal, V - Vertical)
3. Select a coordinate (left upper corner, beginning with the letter, e.g. F4)

Enter all letters and numbers in the console.  
Example:  
Place Submarine horizontally beginning in B3 -> SHB3

## Start playing
Start guessing a location on the right board and enter the coordinate (beginning with the letter) in the console.  
A water tile is marked with ~.  
After your guess, the AI also takes a guess and it is your turn again.  
You win if you manage to locate all of your opponent's ships.  

## Opponent AI
Before playing, the program generates a random ship arrangement.  
While playing, the Opponent AI has two different modes:

1. Pick a random spot until there is a ship
2. Pick one of the 4 random spots around the ship until the orientation is obvious  
After that, uncover the whole ship
