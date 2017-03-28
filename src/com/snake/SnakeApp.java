package com.snake;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;

public class SnakeApp {
	public void init() {
		
		JFrame window = new JFrame("呆呆蛇成熟体@yh");
		
		//初始化一个容器，用来在容器上添加一些控件
		Container contentPane = window.getContentPane();
		Grid grid = new Grid(Settings.DEFAULT_GRID_WIDTH,Settings.DEFAULT_GRID_HEIGHT);
		GameView gameView = new GameView(grid);
		GameController gameController = new GameController(grid,gameView);
		//画出地图和蛇
        gameView.init();
        //设置gameView中JPanel的大小
        gameView.getCanvas().setPreferredSize(new Dimension(Settings.DEFAULT_GRID_WIDTH, Settings.DEFAULT_GRID_HEIGHT));
		
        contentPane.add(gameView.getCanvas(), BorderLayout.CENTER);
        window.pack();
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        //设置窗口居中弹出(jdk1.4之后的办法)
        window.setLocationRelativeTo(null);
        
        
        //window.addKeyListener(gameController);//添加键盘监听器
        new Thread(gameController).start();
        
        
	}
	public static void main(String[] args) {
		SnakeApp snakeApp = new SnakeApp();
		snakeApp.init();
	}

}
