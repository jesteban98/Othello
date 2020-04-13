package othello;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*This panel displays the color of the pieces of each player,
 * the score of each player and if the game ends it also shows who is the winner. */
public class ScorePanel extends JPanel {

	public int whiteScore=2;
	public int blackScore=2;
	public int turn=0;
	public String winner;
	public JLabel[] labels = new JLabel[5];


	private static final long serialVersionUID = 1L;
	public ImageIcon[] pieces = new ImageIcon[2];

	public ScorePanel() {
	    pieces[0] = new ImageIcon("images/negras.png");
	    pieces[1] = new ImageIcon("images/blancas.png");
	    
	    labels[0] = new JLabel();
	    labels[0].setIcon(pieces[0]);
	    labels[0].setHorizontalAlignment(JLabel.CENTER);
	    
	    labels[1] = new JLabel("  Player 1 : "+ blackScore +"  ");
	    labels[1].setHorizontalAlignment(JLabel.CENTER);
	    
	    labels[2] = new JLabel();
	    labels[2].setIcon(pieces[1]);
	    labels[2].setHorizontalAlignment(JLabel.CENTER);
	    
	    labels[3] = new JLabel("  Player 2 : " + whiteScore+"  ");
	    labels[3].setHorizontalAlignment(JLabel.CENTER);
	    
	    labels[4] = new JLabel(winner);	
	    labels[4].setHorizontalAlignment(JLabel.CENTER);

	    
		this.setLayout(new GridLayout(5,1));
		this.setBackground(new Color(170,170,170));
		this.setSize(200,600);
		for(int i=0;i<5;i++) {this.add(labels[i]);}

	}
		
	public void update(GameState game, Player pl) {
		this.blackScore=game.getPlayerScore(game.getPlayer(GameState.BLACK));
		this.whiteScore=game.getPlayerScore(game.getPlayer(GameState.WHITE));
		this.turn=game.getTurn();
		if(!game.gameFinished(game)){
			this.winner = new String("Turn "+(turn/2));
		}
		else {
			this.winner = new String("Winner: player " +game.getWinner(game));
		}
		labels[1].setText("  Player 1 : "+ blackScore +"  ");
		labels[3].setText("  Player 2 : " + whiteScore+"  ");
		labels[4].setText(winner);
 
	}
}
