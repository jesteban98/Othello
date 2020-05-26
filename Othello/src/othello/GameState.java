package othello;

import java.awt.Point;
import java.util.ArrayList;


public class GameState{

	private Othello mediator;
	private int[][] board = new int[8][8];
    private boolean[][] boolMoves = new boolean[8][8];
	private Player[] players = new Player[2];
	private Player currentPlayer;
	private int turn = 0;
	
	public static final int BLACK = 1, WHITE = 2;
	
	/*Constructor used to clone a previous game state*/
	public GameState (GameState clone) {
		this.mediator = clone.mediator;
		this.board=clone.getBoard();
		this.players[0]=clone.getPlayer(BLACK);
		this.players[1]=clone.getPlayer(WHITE);
		this.currentPlayer=clone.getCurrentPlayer();
		this.turn = clone.getTurn();
	}
	/*Constructor for the initial game state*/
	public GameState(Othello mediator) {
		this.mediator=mediator;
		this.players[0]=new Human(BLACK);
		this.players[1]=new Human(WHITE);
		this.currentPlayer=this.players[0];
		this.board[3][3]=players[1].getNumber();
		this.board[4][4]=players[1].getNumber();
		this.board[3][4]=players[0].getNumber();
		this.board[4][3]=players[0].getNumber();
		this.boolMoves=getAllLegalMovesBool(players[0]);
		this.turn=0;
	}
	/*Runs a game. If the game is not finished, we wait for a player to make a move, refresh the GUI when the move is done,
	 *  save the move and change which one is the current player*/
	public void runGame() {
		Point move = new Point();
		if(!gameFinished()) {
			move = currentPlayer.getMove(this, mediator.gamepanel);
			GameState newstate = playMove(move,this.currentPlayer);
			mediator.getTranscription().addMove(move.y,move.x,this.turn);
			/*If we are playing against the AI, we make it do the next move*/
			if(!newstate.currentPlayer.isPlayerHuman()) {
				Point AImove = newstate.currentPlayer.getMove(newstate, mediator.gamepanel);
				newstate = playMove(AImove,newstate.currentPlayer);
				mediator.getTranscription().addMove(AImove.y,AImove.x,newstate.turn);
			}
			mediator.setGameState(newstate);
			mediator.updateGUI();
			if(newstate.gameFinished()){
				mediator.getTranscription().setBlackScore(this.getPlayerScore(players[0]));
				mediator.getTranscription().setWhiteScore(this.getPlayerScore(players[1]));
				mediator.getTranscription().saveGame();	
				mediator.updateGUI();
			}
		}
	}
	
	/*Returns the turn the game is at.
	 * */
	public int getTurn() {return turn;}
	/*Increments the turn.*/
	public void incTurn() {turn++;}

	/*Returns the int[][] board global variable*/
	public int[][] getBoard(){return this.board.clone();}
	
	/*Sets the new board*/
	public void setBoard(int [][] newBoard) {this.board= newBoard;}	
	
	public void setCurrentPlayer(Player player) {this.currentPlayer=player;}
	public Player getCurrentPlayer() {return this.currentPlayer;}
	
	/*Returns the player with the same number (identificator) as the parameter*/
	public Player getPlayer(int number) {
		for(Player pl : this.players) {
			if(number == pl.getNumber()) {return pl;}
		}
		throw new IllegalArgumentException("No player with number=" + number);
	}
	/*Returns the number of pieces the player pl has on the board.*/
	public int getPlayerScore(Player pl) {
		int total = 0;
		for(int i=0;i<=7;i++){
			for(int j=0;j<=7;j++) {
				if (board[i][j]==pl.getNumber())
					total++;
			}
		}
		return total;
	}

	
	/* Will check one point of the board in a chosen direction. If the player has a piece in the chosen square
	 * we check if there is a free square in the horizontal, vertical or diagonal direction.
	 * If there is and between both squares are only pieces from the other player, the move is legal.
	 * */
	private ArrayList<Point> getLegalMovesFrom(Player p, int initialrow, int initialcol, int incrow, int inccol) {
		ArrayList <Point> legalMoves = new ArrayList<Point>();
		int row = initialrow + incrow;
		int col = initialcol + inccol;
		int numflips =0;
		
		if(this.getBoard()[initialrow][initialcol]==0) {
			while (row >= 0 && row < 8 && col >= 0 && col < 8) {
				if(this.getBoard()[row][col]==p.getNumber()) {
					if(numflips>0) {
						legalMoves.add(new Point(initialrow,initialcol));
					}
					break;
				}			
				else
					if(this.getBoard()[row][col]!=0){
						numflips++;
					}
					else {
						break;
					}			
				row+=incrow;
				col+=inccol;
			}
		}
		return legalMoves;
	}
	
	
	public boolean hasLegalMoves(Player p) {
		ArrayList<Point> legalMoves = getAllLegalMoves(p);
		if(legalMoves.isEmpty()==true)
			return false;
		else
			return true;
	}
	
	/*
	 * Returns a bidimensional boolean array that will be used by the JButtons to get the
	 * legal moves on the board*/
	public boolean[][] getAllLegalMovesBool(Player p) {
		ArrayList<Point> legalMoves = getAllLegalMoves(p);
		for(Point legalMove : legalMoves) {
			boolMoves[legalMove.x][legalMove.y] = true;
		}
			return boolMoves;
	}
	
	/*
	 * Returns an ArrayList that will be used by the JButtons to get the
	 * legal moves on the board*/
	public ArrayList<Point> getAllLegalMoves(Player p) {
		ArrayList<Point> legalMoves = new ArrayList<Point>();
		for(int i=0;i<8;i++) {
			for (int j=0;j<8;j++) {
				legalMoves.addAll(getLegalMovesFrom(p,i,j,1,0));
				legalMoves.addAll(getLegalMovesFrom(p,i,j,-1,0));
				legalMoves.addAll(getLegalMovesFrom(p,i,j,0,1));
				legalMoves.addAll(getLegalMovesFrom(p,i,j,0,-1));
				legalMoves.addAll(getLegalMovesFrom(p,i,j,1,1));
				legalMoves.addAll(getLegalMovesFrom(p,i,j,-1,-1));
				legalMoves.addAll(getLegalMovesFrom(p,i,j,-1,1));
				legalMoves.addAll(getLegalMovesFrom(p,i,j,1,-1));
			}
		}
			return legalMoves;
	}
	
	/*placePiece will put the number of the player p at the point po and will flip the pieces from the
	 * other player between them. After that, it will return the new state of the game.
	 * */
	public GameState playMove(Point po, Player p) {
		boolean[][] legalMoves = getAllLegalMovesBool(p);
		GameState newState = new GameState(this);
		if(legalMoves[po.x][po.y]==true) {	
			int[][] lines = getLines(po,p);
			/*Here we put the new piece and flip the pieces*/
			newState.getBoard()[po.x][po.y]=p.getNumber();
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					int lineLength = lines[i][j];
					int localRow = po.x;
					int localCol = po.y;
					while (lineLength > 0) {
						switch(i) {
						case 0:
							if(j==1) {localRow += 0;
										localCol += 1;}
							if(j==2) {localRow += 0;
										localCol -= 1;}
							break;
						case 1:
							if(j==0) {localRow += 1;
										localCol += 0;}
							if(j==1) {localRow -= 1;
										localCol += 0;}
							if(j==2) {localRow += 1;
										localCol += 1;}
							break;
						case 2:
							if(j==0) {localRow += 1;
										localCol -= 1;}
							if(j==1) {localRow -= 1;
										localCol += 1;}
							if(j==2) {localRow -= 1;
										localCol -= 1;}
							break;
						}

						newState.getBoard()[localRow][localCol]=p.getNumber();
						lineLength--;
					}
				}
			}
			newState.incTurn();
			if(this.currentPlayer.getNumber()==BLACK) {
				newState.setCurrentPlayer(players[1]);
			}
			else {
				newState.setCurrentPlayer(players[0]);
			}
			return new GameState(newState);
		}
		else {
			throw new IllegalArgumentException("No legal move at the chosen square");
		}
	}
	
	/*
	 * Returns the number of pieces of the other player in a certain direction, counting from a given point
	 * (initialrow,initialcol)
	 * */
	private int getLineLength(Player p, int initialrow, int initialcol, int incrow, int inccol) {
		int row = initialrow + incrow;
		int col = initialcol + inccol;
		int numflips =0;
		
		while (row >= 0 && row < 8 && col >= 0 && col < 8) {
			if(this.getBoard()[row][col]==p.getNumber()) {
				return numflips;
			}			
			else
				if(this.getBoard()[row][col]!=0){
					numflips++;
				}
				else {
						return 0;
					}
			
			row+=incrow;
			col+=inccol;
		}
		
		return 0;
	}
	
	/*
	 * Returns a bidimensional array with the length of the lines in each direction.
	 * If the length is 3, 3 pieces can be changed in that direction.
	 * */
	private int[][] getLines(Point po, Player p) {
		int[][] lines = new int[3][3];
		lines[0][0] = 0;
		lines[0][1] = getLineLength(p,po.x, po.y, 0, 1);
		lines[0][2] = getLineLength(p,po.x, po.y, 0, -1);
		lines[1][0] = getLineLength(p,po.x, po.y, 1, 0);
		lines[1][1] = getLineLength(p,po.x, po.y, -1, 0);
		lines[1][2] = getLineLength(p,po.x, po.y, 1, 1);
		lines[2][0] = getLineLength(p,po.x, po.y, 1, -1);
		lines[2][1] = getLineLength(p,po.x, po.y, -1, 1);
		lines[2][2] = getLineLength(p,po.x, po.y, -1, -1);
		return lines;
	}
	
	public int getWinner(GameState g) {
		int winner;
		winner = (g.getPlayerScore(g.getPlayer(BLACK))>g.getPlayerScore(g.getPlayer(WHITE)))?BLACK:WHITE;
		return winner;
	}
	
	/*Returns true if there are no more legal moves for any of the players, false otherwise*/
	public boolean gameFinished() {return !hasLegalMoves(getPlayer(BLACK))&&!hasLegalMoves(getPlayer(WHITE));}

	
}
