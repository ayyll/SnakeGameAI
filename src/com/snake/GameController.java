package com.snake;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameController implements Runnable,KeyListener{

	private Grid grid;
	private GameView gameView;
	private boolean running;
	public GameController(Grid grid,GameView gameView) {
		this.grid = grid;
		this.gameView = gameView;
		this.running = true;
	}
	public void setRunning(boolean running) {
		this.running = running;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 * 按键监听,按下某个键会调用该方法
	 */
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_UP) {
            //设置输入方向为上
        	//grid.changeDirection(Direction.UP);
        } else if (keyCode == KeyEvent.VK_DOWN) {
            //设置输入方向为下
        	//grid.changeDirection(Direction.DOWN);
        } else if (keyCode == KeyEvent.VK_LEFT) {
            //设置输入方向为左
        	//grid.changeDirection(Direction.LEFT);
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            //设置输入方向为右
        	//grid.changeDirection(Direction.RIGHT);
        }     
        //刷新界面
        //gameView.getCanvas().repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 * 蛇的自动移动线程
	 */
	@Override
	public void run() {
		while(running) {
			try {
				Thread.sleep(Settings.DEFAULT_MOVE_INTERVAL);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			running = grid.nextRound();
			
			if(running) {
				gameView.getCanvas().repaint();
			}else {
				gameView.showGameOverMessage();
			}
		}
	}
	
}
