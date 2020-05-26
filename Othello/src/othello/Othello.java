package othello;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;



public class Othello extends JFrame{

	private static final long serialVersionUID = 1L;
	public ScorePanel scorepanel;
	public GamePanel gamepanel;
	public JFrame frame;
	public JMenuItem[] mitems;
	public JMenuBar mbar;
	public GameState game;
	public Transcription transcription;
	
	/*Constructor*/
	public Othello() {      
		createGUI();
		this.scorepanel = new ScorePanel();
		this.gamepanel = new GamePanel(this);
		this.game = new GameState(this);
		this.transcription = new Transcription();
    }
	
	/*Get methods*/
	public ScorePanel getScorePanel() {return this.scorepanel;}
	public GamePanel getGamePanel() {return this.gamepanel;}
	public JFrame getFrame() {return this.frame;}
	public JMenuItem[] getMenuItem() {return this.mitems;}
	public JMenuBar getmenuBar() {return this.mbar;}
	public GameState getGameState() {return this.game;}
	public Transcription getTranscription() {return this.transcription;}


	
	/*Set methods*/
	public void setScorePanel(ScorePanel score) {this.scorepanel=score;}
	public void setGamePanel(GamePanel gp) {this.gamepanel=gp;}
	public void setFrame(JFrame frame) {this.frame=frame;}
	public void setMenuItems(JMenuItem[] mitems) {this.mitems=mitems;}
	public void setMenuBar(JMenuBar mbar) {this.mbar=mbar;}
	public void setGameState(GameState st) {this.game=st;}
	public void setTranscription(Transcription tr) {this.transcription=tr;}
	
	/*Updates the scores in the scorepanel*/
	public void updateScore() {
		scorepanel.setBlackScore(game.getPlayerScore(game.getPlayer(GameState.BLACK)));
		scorepanel.setWhiteScore(game.getPlayerScore(game.getPlayer(GameState.WHITE)));
		scorepanel.labels[1].setText("    Player 1 : "+ scorepanel.getBlackScore() +"    ");
		scorepanel.labels[3].setText("    Player 2 : " + scorepanel.getWhiteScore() +"    ");
		scorepanel.setTurn(game.getTurn());
		if(!game.gameFinished()){
			scorepanel.labels[4].setText(new String("Turn "+(scorepanel.turn/2)));
		}
		else {
			scorepanel.labels[4].setText("Winner: player " +game.getWinner(game)+"  ");
		}
	}
	/*Updates the board according to the new GameState*/
	public void updateGamePanel() {
		boolean[][] moves = game.getAllLegalMovesBool(game.getCurrentPlayer());
		for(int i=0;i<8;i++) {
    		for(int j = 0;j<8;j++){
    			//The color of the legal squares changes
    			if(moves[i][j]== true) {gamepanel.othelloCells[i][j].setBackground(new Color(0,90,50));}
    			else {gamepanel.othelloCells[i][j].setBackground(new Color(0,70,30));}
    			if(game.getBoard()[i][j]==GameState.BLACK) {gamepanel.addPiece(new Point(i,j),GamePanel.BLACK);}
    			if(game.getBoard()[i][j]==GameState.WHITE) {gamepanel.addPiece(new Point(i,j),GamePanel.WHITE);}

    		}
    	}
	}
	
	/*Updates the GUI*/
	public void updateGUI() {
		updateScore();
		updateGamePanel();
	}
	
	public void loadGameGUI() {
		frame.add(scorepanel,BorderLayout.EAST);
        frame.add(gamepanel,BorderLayout.CENTER);
        frame.pack();
        updateGUI();
	}
	
	/*Creates the GUI*/
	public void createGUI() {
        //Create and set up the main frame.
        setFrame(new JFrame("Othello"));
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 600);
        frame.setResizable(false);
        
        //Define new buttons for the JMenuBar
        setMenuItems(new JMenuItem[4]);
        mitems[0]= new JMenuItem("Play vs Human");
        mitems[1]= new JMenuItem("Play online");
        mitems[2]= new JMenuItem("How to play");
        mitems[3]= new JMenuItem("Exit");
        
        //Add the buttons to the JMenuBar
        setMenuBar(new JMenuBar());
        for(int i=0;i<4;i++) {
        	mbar.add(mitems[i]);
        	}
        
      //Display the main window.
        frame.setJMenuBar(mbar);
        frame.setVisible(true);

        //Set the listeners for the buttons of the menu bar
        
        //Play vs AI
        mitems[0].addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                loadGameGUI();
                
            }});
        //Play online
        mitems[1].addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
            	System.exit(0);
            }});
        //How to play
        mitems[2].addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                Tutorial tutorial = new Tutorial();
                tutorial.setVisible(true);
            }});
        //Quit
        mitems[3].addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }});
        
        
	}
	
	
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Runnable() {
    		public void run() {
    			new Othello();
    		}
    	});
    }

}
