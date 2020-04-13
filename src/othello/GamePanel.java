package othello;
import java.awt.*;
import java.awt.event.*;
import java.util.Observable;

import javax.swing.*;
import javax.swing.border.*;


public class GamePanel extends JPanel{

	private static final long serialVersionUID = 1L;

    public JButton[][] othelloCells = new JButton[8][8];
    public ImageIcon[] pieces = new ImageIcon[2];
    public Point move = new Point();
    public boolean[][] legalMoves = new boolean[8][8];
    public Player[] players = new Player[2];
    public int[][] board = new int[8][8];
    public Player currentPlayer;
    public GameState g;
    private static final PanelObservable OBSERVABLE = new PanelObservable();
    
    public static final int BLACK = 0, WHITE = 1;	//For the images of the pieces

    /*Constructor of the game board.
     * */
    public GamePanel() {
    	this.g = new GameState();
    	this.players[BLACK] = g.getPlayer(GameState.BLACK);
    	this.players[WHITE] = g.getPlayer(GameState.WHITE);
    	this.currentPlayer=this.players[BLACK];
    	this.board=g.getBoard();
    	
    	// create the images for the Othello pieces
        createImages();

        this.setLayout(new GridLayout(8,8));
        this.setBorder(new CompoundBorder(new EmptyBorder(1,1,1,1),new LineBorder(Color.BLACK)));
        // create the othello board squares
        Insets buttonMargin = new Insets(1,1,1,1);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                othelloCells[i][j] = new JButton();
                othelloCells[i][j].setSize(80,80);
                othelloCells[i][j].setMargin(buttonMargin);
                othelloCells[i][j].setBackground(new Color(0,70,30));
                othelloCells[i][j].putClientProperty("row",i);
                othelloCells[i][j].putClientProperty("col",j);
                othelloCells[i][j].addMouseListener(new ListenerMA());
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.add(othelloCells[i][j]);
            }
        }
        
        this.setBackground(new Color(0,0,0));
        this.setSize(800, 800);
        this.setVisible(true);
        
        setupNewGame();
    }
    
    public GamePanel getThis() {return this;}

    /*Method to get the images of the pieces.*/
    public void createImages() {
        try {
            pieces[BLACK] = new ImageIcon("images/negras.png");
            pieces[WHITE] = new ImageIcon("images/blancas.png");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        
    }
    
    public static Observable getObservable() {
        return OBSERVABLE;
    }


    public void setMove(int x, int y){move=new Point(x,y);}
    
    public Point getMove() {return this.move;}
    
    /**
     * Sets up the pieces to begin a new game
     */
    public void setupNewGame() {
        othelloCells[3][3].setIcon(
                pieces[WHITE]);
        othelloCells[4][4].setIcon(
                pieces[WHITE]);
        othelloCells[4][3].setIcon(
                pieces[BLACK]);
        othelloCells[3][4].setIcon(
                pieces[BLACK]);
    }
    
    public GameState getGame() {return this.g;}
    
    /*If a human player clicked a cell and the cell has a legal move, we put one piece on the cell
     * */
    public void addPiece(Point po,int plnumber) {
    	othelloCells[po.x][po.y].setIcon(
    			pieces[plnumber]);
    }
    
    public void update(GameState g, Player p) {
    	this.g = g;
    	this.board = g.getBoard();
    	this.legalMoves = g.getAllLegalMovesBool(p);
    	this.players[0] = g.getPlayer(GameState.BLACK);
    	this.players[1] = g.getPlayer(GameState.WHITE);
    	this.currentPlayer = p;
    	for(int i=0;i<8;i++) {
    		for(int j = 0;j<8;j++){
    			//The color of the legal squares changes
    			if(legalMoves[i][j]== true) {othelloCells[i][j].setBackground(new Color(0,90,50));}
    			else {othelloCells[i][j].setBackground(new Color(0,70,30));}
    			if(board[i][j]==GameState.BLACK) {addPiece(new Point(i,j),BLACK);}
    			if(board[i][j]==GameState.WHITE) {addPiece(new Point(i,j),WHITE);}

    		}
    	}

    }
    
    /*When we click in a square of the board we put a piece, flip the adversary's pieces
     * and notify to our Othello frame that we have changed the state of the board, so that it can change the scores.
     * */
    class ListenerMA extends MouseAdapter {
    	public void mouseClicked(MouseEvent e) {
    		JButton btn = (JButton) e.getSource();
    		setMove((int) btn.getClientProperty("row"),(int) btn.getClientProperty("col"));

    		if(legalMoves[move.x][move.y]==true && currentPlayer==players[BLACK]){
    			addPiece(move,BLACK);
    			g=g.playMove(move, currentPlayer);
    			currentPlayer = g.getPlayer(GameState.WHITE);
    			synchronized (OBSERVABLE) {
    	            OBSERVABLE.setChanged();
    	            OBSERVABLE.notifyObservers(getThis());            
    	        }

    		}
    		if(legalMoves[move.x][move.y]==true && currentPlayer==players[WHITE]){
    			addPiece(move,WHITE);
    			g=g.playMove(move, currentPlayer);
    			currentPlayer = g.getPlayer(GameState.BLACK);
    			synchronized (OBSERVABLE) {
    	            OBSERVABLE.setChanged();
    	            OBSERVABLE.notifyObservers(getThis());            
    	        }
    		}
    	}
    }
    
    private static class PanelObservable extends Observable {
        @Override
        public synchronized void setChanged() {
            super.setChanged();
        }
    }
    
}