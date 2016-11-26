package EscapeGame;

import javax.swing.*;

public class Display extends JFrame{
	public Images i;
	public Keying k;
	
	public Display(){
		setSize(Client.w, Client.h);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Escape Game!");
		setLocationRelativeTo(null);
		setVisible(true);
		
		k = new Keying(this);
		add(k);
	}
}
