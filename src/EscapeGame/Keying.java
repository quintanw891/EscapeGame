package EscapeGame;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;

import javax.swing.*;

public class Keying extends JPanel{
	//Objects
	public Rectangle player, goal, titleBox, titleBorder, messageBox, messageBorder;
	//whether the game has started
	public boolean start = false;
	//whether a level is being played
	public boolean play = false;
	//Whether the player is in movement
	public boolean right = false;
	public boolean left = false;
	public boolean up = false;
	public boolean down = false;
	//Whether the select button is being pressed
	public boolean enter = false;
	//Whether the player bound by an obstacle on a side
	public boolean rightBound = false;
	public boolean leftBound = false;
	public boolean upBound = false;
	public boolean downBound = false;
	//whether the player is seen by any guard
	public boolean isSeen = false;
	//whether a given guard sees the player
	public boolean sees = false;
	//The guard that sees the player
	public int seeingGuard = 0;
	//whether the goal has been reached
	public boolean goalReached = true;
	//the current level number
	public int levelNum = 0;
	//the current level
	public Level level;
	//Timer that determines when the game should update. (changes the speed of the game)
	public int timer = 0;
	
	public Keying(Display d) {
		//create room objects
		player = new Rectangle(0, 0, 20, 20);
		goal = new Rectangle(0, 0, 0, 0);
		titleBox = new Rectangle(266, 133, 65, 25);
		titleBorder = new Rectangle(268, 135, 60, 20);
		messageBox = new Rectangle(206, 168, 185, 25);
		messageBorder = new Rectangle(208, 170, 180, 20);
		//sight = new Line2D.Double(guard.x+10,guard.y+10,player.x+10,player.y+10);
		setSize(Client.w, Client.h);
		
		//interpret controls
		d.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode() == KeyEvent.VK_D){
					right = true;
				}if(e.getKeyCode() == KeyEvent.VK_A){
					left = true;
				}if(e.getKeyCode() == KeyEvent.VK_W){
					up = true;
				}if(e.getKeyCode() == KeyEvent.VK_S){
					down = true;
				}if(e.getKeyCode() == KeyEvent.VK_ENTER){
					enter = true;
				}
			}
			public void keyReleased(KeyEvent e){
				if(e.getKeyCode() == KeyEvent.VK_D){
					right = false;
				}if(e.getKeyCode() == KeyEvent.VK_A){
					left = false;
				}if(e.getKeyCode() == KeyEvent.VK_W){
					up = false;
				}if(e.getKeyCode() == KeyEvent.VK_S){
					down = false;
				}if(e.getKeyCode() == KeyEvent.VK_ENTER){
					enter = false;
				}
			}
		});
	}

	//update room
	public void paintComponent(Graphics g){
		
		//Display things that happen between levels (including at the start of the game)
		if(goalReached){
			if(!start){
				g.setColor(Color.BLACK);
				g.fillRect(titleBox.x,titleBox.y,titleBox.width,titleBox.height);
				g.fillRect(messageBox.x,messageBox.y,messageBox.width,messageBox.height);
				g.fillRect(messageBox.x,messageBox.y + 35,messageBox.width,messageBox.height);
				g.fillRect(messageBox.x,messageBox.y + 70,messageBox.width,messageBox.height);
				g.setColor(Color.WHITE);
				g.drawString("   RULES", titleBorder.x + 2, titleBorder.y + 15);
				g.drawString("Reach the exit, but don't be seen.", messageBorder.x + 2, messageBorder.y + 15);
				g.drawString("          Use W,A,S,D to move", messageBorder.x + 2, messageBorder.y + 50);
				g.drawString("         Press ENTER to start!", messageBorder.x + 2, messageBorder.y + 85);
				g.drawRect(titleBorder.x,titleBorder.y,titleBorder.width,titleBorder.height);
				g.drawRect(messageBorder.x,messageBorder.y,messageBorder.width,messageBorder.height);
				g.drawRect(messageBorder.x,messageBorder.y + 35,messageBorder.width,messageBorder.height);
				g.drawRect(messageBorder.x,messageBorder.y + 70,messageBorder.width,messageBorder.height);
			}else{
				play = false;
				g.setColor(Color.BLACK);
				g.fillRect(titleBox.x,titleBox.y,titleBox.width,titleBox.height);
				g.fillRect(messageBox.x,messageBox.y,messageBox.width,messageBox.height);
				g.setColor(Color.WHITE);
				g.drawString("ESCAPED", titleBorder.x + 2, titleBorder.y + 15);
				g.drawString("      Press ENTER to proceed", messageBorder.x + 2, messageBorder.y + 15);
				g.drawRect(titleBorder.x,titleBorder.y,titleBorder.width,titleBorder.height);
				g.drawRect(messageBorder.x,messageBorder.y,messageBorder.width,messageBorder.height);
			}
			if(enter){
				start = true;
				play = true;
				levelNum++;
				level = new Level(levelNum);
				player.x = level.startx;
				player.y = level.starty;
				goal.x = level.goalx;
				goal.y = level.goaly;
				goal.width = level.goalw;
				goal.height = level.goalh;
				goalReached = false;
				//message = "CAUGHT";
			}
		}
		
		//Display things that happen during the levels
		if(play){
			super.paintComponent(g);
			//draw room and objects
			this.setBackground(Color.BLACK);
			g.setColor(Color.ORANGE);
			g.fillRect(player.x, player.y, player.width, player.height);
			g.setColor(Color.GRAY);
			
			for(int i=0; i<level.walls.length; i++){	
				g.fillRect(level.walls[i].x, level.walls[i].y, level.walls[i].width, level.walls[i].height);
			}
			g.setColor(Color.GREEN);
			g.fillRect(goal.x,goal.y,goal.width,goal.height);
			g.setColor(Color.BLUE);
			for(int i=0; i<level.guards.length; i++){
				g.fillRect(level.guards[i].x, level.guards[i].y, level.guards[i].width, level.guards[i].height);
			}
			if(isSeen){
				g.setColor(Color.BLACK);
				g.fillRect(titleBox.x,titleBox.y,titleBox.width,titleBox.height);
				g.fillRect(messageBox.x,messageBox.y,messageBox.width,messageBox.height);
				g.setColor(Color.WHITE);
				g.drawRect(titleBorder.x,titleBorder.y,titleBorder.width,titleBorder.height);
				g.drawRect(messageBorder.x,messageBorder.y,messageBorder.width,messageBorder.height);
				g.drawString(" CAUGHT", titleBorder.x + 2, titleBorder.y + 15);
				g.drawString("      Press ENTER to try again", messageBorder.x + 2, messageBorder.y + 15);
				g.setColor(Color.RED);
				g.drawLine((int)level.guards[seeingGuard].sight.x1,(int)level.guards[seeingGuard].sight.y1,
						(int)level.guards[seeingGuard].sight.x2,(int)level.guards[seeingGuard].sight.y2);
				if(enter){
					player.x = level.startx;
					player.y = level.starty;
					isSeen = false;
				}
			}
			
			if(timer == 0 && !isSeen && !goalReached){
				//initialize collisions to false
				leftBound = false;
				rightBound = false;
				upBound = false;
				downBound = false;
				
				//test for collisions and move if possible
				//is there an object to the left?
				for(int i=0; i<level.walls.length; i++){				
					if(player.x == level.walls[i].x+level.walls[i].width){
						if(level.walls[i].y < player.y+player.height && player.y < level.walls[i].y+level.walls[i].height){
							leftBound = true;
						}
					}
				}
				//is the screen border to the left?
				if(player.x == 0)
					leftBound = true;
				//move left if possible
				if(left && !leftBound)
					player.x--;
				
				//is there an object to the right?
				for(int i=0; i<level.walls.length; i++){	
					if(player.x+player.width == level.walls[i].x){
						if(level.walls[i].y < player.y+player.height && player.y < level.walls[i].y+level.walls[i].height){
							rightBound = true;
						}
					}
				}
				//is the screen border to the right?
				if(player.x == Client.w-26)
					rightBound = true;
				//move right if possible
				if(right && !rightBound)
					player.x++;
				
				//is there an object above?
				for(int i=0; i<level.walls.length; i++){
					if(player.y == level.walls[i].y+level.walls[i].height){
						if(level.walls[i].x < player.x+player.width && player.x < level.walls[i].x+level.walls[i].width){
							upBound = true;
						}
					}
				}
				//is the screen border above?
				if(player.y == 0)
					upBound = true;
					//move up if possible
					if(up && !upBound)
						player.y--;
					
				//is there an object below?
				for(int i=0; i< level.walls.length; i++){	
					if(player.y+player.height == level.walls[i].y){
						if(level.walls[i].x < player.x+player.width && player.x < level.walls[i].x+level.walls[i].width){
							downBound = true;
						}
					}
				}
				//is the screen border below?
				if(player.y == Client.h-49)
					downBound = true;
				//move down if possible
				if(down && !downBound)
					player.y++;
			
				//Will the guards change direction?
				for(int i=0; i<level.guards.length; i++){
					for(int j=0; j<level.guards[i].path.length; j++){
						if(level.guards[i].x == level.guards[i].path[j].x
						&& level.guards[i].y == level.guards[i].path[j].y){
							level.guards[i].phase++;
							if(level.guards[i].phase > level.guards[i].path.length){
								level.guards[i].phase = 1;
							}
						}
					}
				}
				//move the guards
				for(int i=0; i<level.guards.length; i++){
					//if the guard is horizontal to its next turning point
					if(level.guards[i].y == level.guards[i].path[level.guards[i].phase-1].y){
						//if the guard is left of the turning point
						if(level.guards[i].x < level.guards[i].path[level.guards[i].phase-1].x){
							level.guards[i].x++;
						}else{//if the guard is right of the turning point
							level.guards[i].x--;
						}
					}else{//if the guard is vertical to its next turning point
						//if the guard is below the turning point
						if(level.guards[i].y > level.guards[i].path[level.guards[i].phase-1].y){
							level.guards[i].y--;
						}else{//if the guard is right of the turning point
							level.guards[i].y++;
						}
					}
				}
			
				//update the guard's line of sight
				for(int i=0; i<level.guards.length; i++){
					level.guards[i].sight.x1 = level.guards[i].x+10;
					level.guards[i].sight.y1 = level.guards[i].y+10;
					level.guards[i].sight.x2 = player.x+10;
					level.guards[i].sight.y2 = player.y+10;
				}
			
				//was the player seen?
				isSeen = false;
				for(int i=0; i<level.guards.length; i++){
					sees = true;
					for(int j=0;j<level.walls.length;j++){
						if(level.walls[j].intersectsLine(level.guards[i].sight))
							sees = false;
					}
					if(sees){
						isSeen = true;
						seeingGuard = i;
					}
				}
			
				//did the player reach the goal?
				if(player.y <= goal.y+goal.height && player.y+player.height >= goal.y
				&& player.x <= goal.x+goal.width && player.x+player.width >= goal.x){
					goalReached = true;
				}
			}
		
			//increment timer
			timer++;
			if(timer == 20){
				timer = 0;
			}
		}
		repaint();
	}
}