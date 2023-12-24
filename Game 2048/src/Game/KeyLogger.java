package Game;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

public class KeyLogger implements NativeKeyListener {
	
    public void nativeKeyPressed(NativeKeyEvent e) {
        if(System.currentTimeMillis() - Main.lastMove <= Main.moveTime + 100) {
			return;
		}
        Main.lastMove = System.currentTimeMillis();
		
		int changed = 0;
		if(e.getKeyCode() == NativeKeyEvent.VC_UP) {
			changed = Main.moveUp();
		}else if(e.getKeyCode() == NativeKeyEvent.VC_DOWN) {
			changed = Main.moveDown();
		}else if(e.getKeyCode() == NativeKeyEvent.VC_LEFT) {
			changed = Main.moveLeft();
		}else if(e.getKeyCode() == NativeKeyEvent.VC_RIGHT) {
			changed = Main.moveRight();
		}
		
		if(changed > 0) {
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					for(int i = 4;i <= Main.size;i++) {
						Main.addRandomNewTile();
					}
					if(Main.hasWon()) {
						JOptionPane.showMessageDialog(null, "You Won!");
						Main.reset();
					}else if(Main.isDead()) {
						JOptionPane.showMessageDialog(null, "Game Over!");
						Main.reset();
					}
				}
			}, 100L);
		}else {
			Main.repaintBoard();
		}
    }
    
}
