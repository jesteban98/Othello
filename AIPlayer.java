package othello;

import java.awt.Point;
import java.util.ArrayList;

public class AIPlayer extends Player{
	//Number of states the AI will look ahead
	public static final int DEPTH = 4;
	/*Schema with the weights of each board position. Corners can't be flipped,so they are the most valuable positions.
	 * It will be used by the evaluatePosition function to return a score that will be used by the minimax method*/
	public static final int[][] boardWeights = {
												{10,-5,3,3,3,3,-5,10},
												{-5,-5,1,1,1,1,-5,-5},
												  {3,1,1,1,1,1,1,3},
												  {3,1,1,1,1,1,1,3},
												  {3,1,1,1,1,1,1,3},
												  {3,1,1,1,1,1,1,3},
												{-5,-5,1,1,1,1,-5,-5},
												{10,-5,3,3,3,3,-5,10}		
											 };
	public int Score;
	public Point bestMove= new Point();

	
	/*Constructor that creates a new player. n can be either 1(black) or 2(white)*/
	public AIPlayer(int n) {
		super(n);
		this.number=n;
		this.isHuman=false;
	}

	/*Returns the maximum score the machine can get from making a move in the given turn*/
	public int getMaxScore(Player p, GameState g ,int depth){
		ArrayList<Point> legalMoves = g.getAllLegalMoves(p);
		int bestScore=-100000;
		
		if(g.gameFinished(g) || depth == 0) {
			return evaluatePosition(p,g);
		}
		else {
			int tempScore=0;
			if(g.hasLegalMoves(p)) {
				GameState newState = new GameState(g);
				for(Point pt : legalMoves) {
					newState.playMove(pt, p);
					tempScore = getMinScore(newState.getPlayer(1),newState,DEPTH-1);//Puede haber errores con p y depth
					if(tempScore>bestScore){bestScore=tempScore;}
				}
			}
			return bestScore;
		}
	}
	/*Returns the best score that the human player can get to minimize the chances of winning of the AI*/
	public int getMinScore(Player p, GameState g,int depth){
		ArrayList<Point> legalMoves = g.getAllLegalMoves(p);
		int bestScore=100000;
		
		if(g.gameFinished(g) || depth == 0) {
			return evaluatePosition(p,g);
			}
		else {
			int tempScore=0;
			if(g.hasLegalMoves(p)) {
				GameState newState = new GameState(g);
				for(Point pt : legalMoves) {
					newState.playMove(pt, p);
					tempScore = getMaxScore(p,newState,DEPTH-1);//Puede haber errores con p y depth
					if(tempScore<bestScore){bestScore=tempScore;}
				}
			}
			return bestScore;
		}
	}
	
	/*Chooses the point where the AI wants to put a piece by using the Minimax method*/
	public synchronized Point getMove(GameState state,GamePanel panel) {
		Point bestMove = new Point();
		int bestScore=-1000000;
		//If the player is the AI we want to get the maximum score
		if(state.getPlayer(1).getNumber()==2) {
			for(Point move: state.getAllLegalMoves(state.getPlayer(1))) {
				int moveScore = getMinScore(state.getPlayer(1),state.playMove(move,state.getPlayer(2)),DEPTH-1);
				if(moveScore>bestScore) {
					bestScore=moveScore;
					bestMove=move;
					}
			}
		}
		return bestMove;
	}

	/*Returns the best score the AI can expect from the given gamestate*/
	public int evaluatePosition(Player p, GameState g) {
		int score =0;
		int[][] board= g.getBoard();
		int AInumber = p.getNumber();
		int HumanNumber;
		HumanNumber=(AInumber==2)?1:2;
		
		/*If the game is finished and we won we return the best possible score. 
		 * If we lost, we return the worst possible score*/
		if(g.gameFinished(g)) {
			if(g.getPlayerScore(g.getPlayer(AInumber))>g.getPlayerScore(g.getPlayer(HumanNumber))) {
				score = 10000;
			}
			else {
				score = -10000;
			}
		}
		/*If the game is not finished we will return a score based on the weight of each position of
		 * the board and the pieces that are on the board.*/
		else {
			int rivalScore=0;
			for(int i=0;i<8;i++) {
				for(int j=0;j<8;j++) {
					if(board[i][j]==AInumber) {score+=boardWeights[i][j];}
					else if(board[i][j]==HumanNumber) {rivalScore+=boardWeights[i][j];}
				}
			}
			score-=rivalScore;	
		}
		return score;
	}

}