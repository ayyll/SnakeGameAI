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
	 * ��������,����ĳ��������ø÷���
	 */
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_UP) {
            //�������뷽��Ϊ��
        	//grid.changeDirection(Direction.UP);
        } else if (keyCode == KeyEvent.VK_DOWN) {
            //�������뷽��Ϊ��
        	//grid.changeDirection(Direction.DOWN);
        } else if (keyCode == KeyEvent.VK_LEFT) {
            //�������뷽��Ϊ��
        	//grid.changeDirection(Direction.LEFT);
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            //�������뷽��Ϊ��
        	//grid.changeDirection(Direction.RIGHT);
        }     
        //ˢ�½���
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
	 * �ߵ��Զ��ƶ��߳�
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
