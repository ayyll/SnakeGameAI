package com.snake;

import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.*;
/**
 * @author yh
 * 画地图和蛇
 */
public class GameView {
	private final Grid grid;
	private JPanel canvas;
	
	public GameView(Grid grid) {
		this.grid = grid;
	}
	public void init() {
        canvas = new JPanel() {
            @Override
            public void paintComponent(Graphics graphics) {
                drawGridBackground(graphics);
                drawSnake(graphics, grid.getSnake());
                drawFood(graphics, grid.getFood());
            }
        };
    }
	public void draw() {
        canvas.repaint();
    }
	public JPanel getCanvas() {
        return canvas;
    }
	public Grid getGrid() {
		return this.grid;
	}
	/**
	 * @param graphics
	 * @param snake
	 * 画蛇
	 */
	public void drawSnake(Graphics graphics, Snake snake) {
		LinkedList<Node> body = snake.getBody();
		for (Node node : body) {
			if(body.getFirst().getX() == node.getX() && body.getFirst().getY() == node.getY())
			drawSquare(graphics, node, Color.RED);
			else if(body.getLast().getX() == node.getX() && body.getLast().getY() == node.getY())
				drawSquare(graphics, node, Color.YELLOW);
			else
			drawSquare(graphics, node, Color.WHITE);	
		}
		
    }

    /**
     * @param graphics
     * @param squareArea
     * 画食物
     */
    public void drawFood(Graphics graphics, Node squareArea) {
    	drawCircle(graphics, squareArea, Color.BLUE);
    }

    /**
     * @param graphics
     * 画背景
     */
    public void drawGridBackground(Graphics graphics) {
    	graphics.setColor(Color.BLACK);
    	graphics.fillRect(0, 0, Settings.DEFAULT_GRID_WIDTH, Settings.DEFAULT_GRID_HEIGHT);
    }
	/**
	 * @param graphics
	 * @param squareArea
	 * @param color
	 * 画矩形
	 */
	private void drawSquare(Graphics graphics, Node squareArea, Color color) {
        graphics.setColor(color);
        int size = Settings.DEFAULT_NODE_SIZE;
        graphics.fillRect(squareArea.getX() * size, squareArea.getY() * size, size - 1, size - 1);
    }

    /**
     * @param graphics
     * @param squareArea
     * @param color
     * 画圆
     */
    private void drawCircle(Graphics graphics, Node squareArea, Color color) {
        graphics.setColor(color);
        int size = Settings.DEFAULT_NODE_SIZE;
        graphics.fillOval(squareArea.getX() * size, squareArea.getY() * size, size, size);
    }
    
    /**
     * 弹出游戏结束对话框
     */
    public void showGameOverMessage() {
        JOptionPane.showMessageDialog(null, "游戏结束", "游戏结束", JOptionPane.INFORMATION_MESSAGE);
    }

}
