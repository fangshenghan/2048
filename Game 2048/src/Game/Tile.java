package Game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;

public class Tile {
	
	public JLabel label;
	public int x = -1, y = -1;
	public int num = 2;
	public float moveBaseX = 0, moveBaseY = 0, remainTimes, combinedRemainTimes;
	public List<Tile> toCombine = new ArrayList<Tile>();
	
	public Tile(JLabel label, int num) {
		this.label = label;
		this.num = num;
	}
	
	public void setPosition(int x, int y) {
		Main.tiles.remove(this.x + (this.y * Main.size));
		this.x = x;
		this.y = y;
		Main.tiles.put(x + (y * Main.size), this);
		label.setBounds(15 + (x * 100), 15 + (y * 100), 90, 90);
	}
	
	public void update() {
		label.setText(num + "");
		label.setBounds(15 + (x * 100), 15 + (y * 100), 90, 90);
		if(num == 2) {
			label.setBackground(new Color(255, 255, 128));
		}else if(num == 4) {
			label.setBackground(new Color(255, 200, 128));
		}else if(num == 8) {
			label.setBackground(new Color(255, 160, 100));
		}else if(num == 16) {
			label.setBackground(new Color(255, 128, 128));
		}else if(num == 32) {
			label.setBackground(new Color(255, 0, 128));
		}else if(num == 64) {
			label.setBackground(new Color(200, 0, 0));
		}else if(num == 128) {
			label.setBackground(new Color(255, 0, 0));
		}else if(num == 256) {
			label.setBackground(new Color(200, 100, 200));
		}else if(num == 512) {
			label.setBackground(new Color(200, 0, 200));
		}else if(num == 1024) {
			label.setBackground(new Color(0, 200, 100));
		}else if(num == 2048) {
			label.setBackground(new Color(0, 255, 255));
		}
	}
	
	public void destroy() {
		Main.tiles.remove(x + (y * Main.size));
		Main.window.contentPane.remove(label);
	}
	
	public void removeFromMap() {
		Main.tiles.remove(x + (y * Main.size));
	}
	
	public void removeFromBoard() {
		Main.window.contentPane.remove(label);
	}
	
	public void smoothMove(final int x, final int y) {
		Main.tiles.remove(this.x + (this.y * Main.size));
		remainTimes = Main.moveTime / Main.actionTime;
		moveBaseX = (x * 100 - this.x * 100) / remainTimes;
		moveBaseY = (y * 100 - this.y * 100) / remainTimes;
		this.x = x;
		this.y = y;
		Main.tiles.put(x + (y * Main.size), this);
		
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				remainTimes--;
				if(remainTimes <= 0) {
					this.cancel();
					setPosition(x, y);
					update();
					return;
				}
				label.setLocation((int) (label.getX() + moveBaseX), (int) (label.getY() + moveBaseY));
				Main.repaintBoard();
			}
		}, 0L, Main.actionTime);
	}
	
	public void smoothMoveCombined(final int x, final int y) {
		List<Float> moveX = new ArrayList<>();
		List<Float> moveY = new ArrayList<>();
		combinedRemainTimes = Main.moveTime / Main.actionTime;
		
		for(int i = 0;i < toCombine.size();i++) {
			moveX.add((x * 100 - toCombine.get(i).x * 100) / combinedRemainTimes);
			moveY.add((y * 100 - toCombine.get(i).y * 100) / combinedRemainTimes);
		}
		
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				combinedRemainTimes--;
				if(combinedRemainTimes <= 0) {
					this.cancel();
					update();
					for(Tile t : toCombine) {
						t.removeFromBoard();
					}
					toCombine.clear();
					Main.repaintBoard();
					return;
				}
				for(int i = 0;i < toCombine.size();i++) {
					JLabel label = toCombine.get(i).label;
					label.setLocation((int) (label.getX() + moveX.get(i)), (int) (label.getY() + moveY.get(i)));
				}
				Main.repaintBoard();
			}
		}, 0L, Main.actionTime);
	}

}
