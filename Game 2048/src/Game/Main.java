package Game;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Main {
	
	public static ExecutorService es = Executors.newCachedThreadPool();
	public static Window window;
	public static Gson gson = new Gson();
	
	public static long lastMove = 0;
	public static long actionTime = 10, moveTime = 120;
	public static int size = 4;
	
	public static HashMap<Integer, Tile> tiles = new HashMap<>();
	
	public static void main(String[] args) throws Exception {
		JsonObject configJson = gson.fromJson(Utils.read("C:\\Users\\fangs\\Desktop\\2048_config.json"), JsonObject.class);
		size = configJson.get("boardSize").getAsInt();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new Window();
					Main.addRandomNewTile();
					window.setVisible(true);
				}catch(Exception ex) {
					ex.printStackTrace();
					System.exit(0);
				}
			}
		});
		
		GlobalScreen.registerNativeHook();
		GlobalScreen.addNativeKeyListener(new KeyLogger());
	}
	
	public static int moveUp() {
		int changed = 0;
		for(int x = 0;x < Main.size;x++) {
			Tile last = null;
			for(int y = 0;y < Main.size;y++) {
				Tile t = getTileAt(x, y);
				if(t != null) {
					if(last != null && last.num == t.num) {
						last.num *= 2;
						last.toCombine.add(t);
						t.removeFromMap();
						changed++;
						last = null;
					}else {
						last = t;
					}
				}
			}
		}
		
		for(int x = 0;x < Main.size;x++) {
			for(int y = 0;y < Main.size;y++) {
				Tile t = getTileAt(x, y);
				if(t != null) {
					for(int dy = 0;dy <= y;dy++) {
						if(getTileAt(x, dy) == null) {
							//System.out.println("fx: " + x + " fy: " + y);
							t.smoothMove(x, dy);
							t.smoothMoveCombined(x, dy);
							changed++;
							//System.out.println("x: " + x + " y: " + dy);
							break;
						}else if(y == dy) {
							t.smoothMoveCombined(x, dy);
						}
					}
				}
			}
		}
		
		return changed;
	}
	
	public static int moveDown() {
		int changed = 0;
		for(int x = 0;x < Main.size;x++) {
			Tile last = null;
			for(int y = Main.size - 1;y >= 0;y--) {
				Tile t = getTileAt(x, y);
				if(t != null) {
					if(last != null && last.num == t.num) {
						last.num *= 2;
						last.toCombine.add(t);
						t.removeFromMap();
						changed++;
						last = null;
					}else {
						last = t;
					}
				}
			}
		}
		
		for(int x = 0;x < Main.size;x++) {
			for(int y = Main.size - 1;y >= 0;y--) {
				Tile t = getTileAt(x, y);
				if(t != null) {
					for(int dy = Main.size - 1;dy >= y;dy--) {
						if(getTileAt(x, dy) == null) {
							//System.out.println("fx: " + x + " fy: " + y);
							t.smoothMove(x, dy);
							t.smoothMoveCombined(x, dy);
							changed++;
							//System.out.println("x: " + x + " y: " + dy);
							break;
						}else if(y == dy) {
							t.smoothMoveCombined(x, dy);
						}
					}
				}
			}
		}
		return changed;
	}
	
	public static int moveLeft() {
		int changed = 0;
		for(int y = 0;y < Main.size;y++) {
			Tile last = null;
			for(int x = 0;x < Main.size;x++) {
				Tile t = getTileAt(x, y);
				if(t != null) {
					if(last != null && last.num == t.num) {
						last.num *= 2;
						last.toCombine.add(t);
						t.removeFromMap();
						changed++;
						last = null;
					}else {
						last = t;
					}
				}
			}
		}
		
		for(int y = 0;y < Main.size;y++) {
			for(int x = 0;x < Main.size;x++) {
				Tile t = getTileAt(x, y);
				if(t != null) {
					for(int dx = 0;dx <= x;dx++) {
						if(getTileAt(dx, y) == null) {
							//System.out.println("fx: " + x + " fy: " + y);
							t.smoothMove(dx, y);
							t.smoothMoveCombined(dx, y);
							changed++;
							//System.out.println("x: " + x + " y: " + dy);
							break;
						}else if(x == dx) {
							t.smoothMoveCombined(dx, y);
						}
					}
				}
			}
		}
		return changed;
	}
	
	public static int moveRight() {
		int changed = 0;
		for(int y = 0;y < Main.size;y++) {
			Tile last = null;
			for(int x = Main.size - 1;x >= 0;x--) {
				Tile t = getTileAt(x, y);
				if(t != null) {
					if(last != null && last.num == t.num) {
						last.num *= 2;
						last.toCombine.add(t);
						t.removeFromMap();
						changed++;
						last = null;
					}else {
						last = t;
					}
				}
			}
		}
		
		for(int y = 0;y < Main.size;y++) {
			for(int x = Main.size - 1;x >= 0;x--) {
				Tile t = getTileAt(x, y);
				if(t != null) {
					for(int dx = Main.size - 1;dx >= x;dx--) {
						if(getTileAt(dx, y) == null) {
							//System.out.println("fx: " + x + " fy: " + y);
							t.smoothMove(dx, y);
							t.smoothMoveCombined(dx, y);
							changed++;
							//System.out.println("x: " + x + " y: " + dy);
							break;
						}else if(x == dx) {
							t.smoothMoveCombined(dx, y);
						}
					}
				}
			}
		}
		return changed;
	}
	
	public static Tile getTileAt(int x, int y) {
		return tiles.get(x + (y * Main.size));
	}
	
	public static void addRandomNewTile() {
		List<Integer> list = new ArrayList<>();
		for(int i = 0;i < Main.size * Main.size;i++) {
			if(tiles.get(i) == null) {
				list.add(i);
			}
		}
		if(list.size() == 0) {
			return;
		}
		int pos = list.get(Math.abs(new Random(System.currentTimeMillis()).nextInt()) % list.size());
		int x = pos % Main.size, y = pos / Main.size;
		tiles.put(pos, Main.createRandomTile(x, y));
	}
	
	public static Tile createRandomTile(int x, int y) {
		Tile t;
		JLabel label;
		
		if(Math.abs(new Random(System.currentTimeMillis()).nextInt()) % 10 <= 1) {
			label = new JLabel("4");
			t = new Tile(label, 2);
		}else {
			label = new JLabel("2");
			t = new Tile(label, 2);
		}
		label.setOpaque(true);
		label.setBackground(new Color(255, 255, 128));
		label.setFont(new Font("宋体", Font.BOLD, 38));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		t.setPosition(x, y);
		t.update();
		window.contentPane.add(label);
		Main.repaintBoard();
		
		return t;
	}
	
	public static boolean isDead() {
		for(int i = 0;i < Main.size * Main.size;i++) {
			if(tiles.get(i) == null) {
				return false;
			}
		}
		
		// check up
		for(int x = 0;x < Main.size;x++) {
			Tile last = null;
			for(int y = 0;y < Main.size;y++) {
				Tile t = getTileAt(x, y);
				if(t != null) {
					if(last != null && last.num == t.num) {
						return false;
					}else {
						last = t;
					}
				}
			}
		}
		
		// check down
		for(int x = 0;x < Main.size;x++) {
			Tile last = null;
			for(int y = Main.size - 1;y >= 0;y--) {
				Tile t = getTileAt(x, y);
				if(t != null) {
					if(last != null && last.num == t.num) {
						return false;
					}else {
						last = t;
					}
				}
			}
		}
		
		// check left
		for(int y = 0;y < Main.size;y++) {
			Tile last = null;
			for(int x = 0;x < Main.size;x++) {
				Tile t = getTileAt(x, y);
				if(t != null) {
					if(last != null && last.num == t.num) {
						return false;
					}else {
						last = t;
					}
				}
			}
		}
		
		// check right
		for(int y = 0;y < Main.size;y++) {
			Tile last = null;
			for(int x = Main.size - 1;x >= 0;x--) {
				Tile t = getTileAt(x, y);
				if(t != null) {
					if(last != null && last.num == t.num) {
						return false;
					}else {
						last = t;
					}
				}
			}
		}
		
		return true;
	}
	
	public static boolean hasWon() {
		for(int i = 0;i < Main.size * Main.size;i++) {
			if(tiles.get(i) != null && tiles.get(i).num >= 2048) {
				return true;
			}
		}
		return false;
	}
	
	public static void reset() {
		List<Tile> list = new ArrayList<>();
		for(int i = 0;i < Main.size * Main.size;i++) {
			if(tiles.get(i) != null) {
				list.add(tiles.get(i));
			}
		}
		
		for(Tile t : list) {
			t.destroy();
		}
		
		Main.addRandomNewTile();
		Main.repaintBoard();
	}
	
	
	public static void repaintBoard() {
		window.repaint();
	}
	
}
