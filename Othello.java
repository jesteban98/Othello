package othello;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;



public class Othello extends JFrame implements Observer{

	private static final long serialVersionUID = 1L;
	ScorePanel score;
	GamePanel gamepanel = new GamePanel();
	JFrame frame;
	JButton[] buttons;
	ArrayList <String> transcription = new ArrayList <String>();
	BufferedWriter output;


	//The constructor takes care of the whole GUI initialization process
	public Othello(GameState game) {
        //Create and set up the main frame.
        frame = new JFrame("Othello");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 600);
        frame.setResizable(false);
        
        //Define new buttons for the JMenuBar
        JMenuItem [] buttons = new JMenuItem[5];
        buttons[0]= new JMenuItem("Play vs Human");
        buttons[1]= new JMenuItem("Play online");
        buttons[2]= new JMenuItem("How to play");
        buttons[3]= new JMenuItem("Exit");
        
        //Add the buttons to the JMenuBar
        JMenuBar mbar = new JMenuBar();
        for(int i=0;i<4;i++) {
        	mbar.add(buttons[i]);
        	}
        
        //Display the main window.
        frame.setJMenuBar(mbar);
        frame.setVisible(true);        
        
        //Set the listeners for the buttons of the menu bar
        
        //Play vs AI
        buttons[0].addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                score = new ScorePanel();
                score.update(game, game.getPlayer(GameState.BLACK));
                gamepanel.update(game, game.getPlayer(GameState.BLACK));
                frame.add(score,BorderLayout.EAST);
                frame.add(gamepanel,BorderLayout.CENTER);
                frame.pack();
                GamePanel.getObservable().addObserver(getThis());
                

            }});
        //Play online
        buttons[1].addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }});
        //How to play
        buttons[2].addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                Tutorial tutorial = new Tutorial();
                tutorial.setVisible(true);
            }});
        //Quit
        buttons[3].addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }});

    }
	
	public Othello getThis() {return this;}
	
	/*Updates the panels containing the board and the scores*/
	public void update(GameState game, Player pl) {
		try {
			this.gamepanel.update(game,pl);
			this.score.update(game,pl);
			
			
			transcription.addAll(game.getTranscription());
			//When the game is over, we write a new file with the movements and the score.
			if(game.gameFinished(game)) {
				output = new BufferedWriter(new FileWriter("./games/Game"+System.currentTimeMillis() + ".txt"));
	    		for(String move: transcription) {
					output.write(move);
				}
				output.write("End of game. Score :\n"
	    				+ "Black = " + game.getPlayerScore(game.getPlayer(GameState.BLACK)) +"\n" 
	    				+ "White = " + game.getPlayerScore(game.getPlayer(GameState.WHITE)));
	    		output.close();
			}
		
		
		}catch(IOException e) {
			System.err.println("Error copying the game to a text file");
			e.printStackTrace();
		}	
	}
	
	
    public static void main(String[] args) {
    	GameState game = new GameState();
    	Othello jan = new Othello(game);
    	jan.toFront();
    }

    /*Used to update the score panel when a player adds a piece*/
	@Override
	public void update(Observable arg0, Object gamepanel) {
		if(gamepanel instanceof GamePanel) {
			update(((GamePanel) gamepanel).getGame(),((GamePanel) gamepanel).currentPlayer);
		}
	}

}
