package com.snake;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;

public class SnakeApp {
	public void init() {
		
		JFrame window = new JFrame("�����߳�����@yh");
		
		//��ʼ��һ�����������������������һЩ�ؼ�
		Container contentPane = window.getContentPane();
		Grid grid = new Grid(Settings.DEFAULT_GRID_WIDTH,Settings.DEFAULT_GRID_HEIGHT);
		GameView gameView = new GameView(grid);
		GameController gameController = new GameController(grid,gameView);
		//������ͼ����
        gameView.init();
        //����gameView��JPanel�Ĵ�С
        gameView.getCanvas().setPreferredSize(new Dimension(Settings.DEFAULT_GRID_WIDTH, Settings.DEFAULT_GRID_HEIGHT));
		
        contentPane.add(gameView.getCanvas(), BorderLayout.CENTER);
        window.pack();
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        //���ô��ھ��е���(jdk1.4֮��İ취)
        window.setLocationRelativeTo(null);
        
        
        //window.addKeyListener(gameController);//��Ӽ��̼�����
        new Thread(gameController).start();
        
        
	}
	public static void main(String[] args) {
		SnakeApp snakeApp = new SnakeApp();
		snakeApp.init();
	}

}
