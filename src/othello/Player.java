package othello;

import java.awt.Point;

public abstract class Player {
	public int number;
	public boolean isHuman;
	
	/*Constructor that creates a new player. n can be either 1 or 2*/
	public Player(int n) {
		this.number=n;
	}
	/*Returns the number that identifies if a player is black(1) or white(2)*/
	public int getNumber(){
		return this.number;}
	/*If isHuman is true, the Player is human*/
	public boolean isPlayerHuman(){return isHuman;}
	/*Returns the point where the player wants to put a piece*/
	public abstract Point getMove(GameState state, GamePanel panel);
}
