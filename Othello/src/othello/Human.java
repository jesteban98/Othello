package othello;

import java.awt.Point;

public class Human extends Player{

	public Human(int n) {
		super(n);
		this.isHuman=true;
	}

	/*returns the Point where the player wants to put a piece*/
	public Point getMove(GameState state, GamePanel game) {
		Point move = new Point();
		if(this.isPlayerHuman()){
			move=game.getMove();
		}		
		return move;
	}

}
