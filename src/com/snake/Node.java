package com.snake;

/**
 * @author yh
 * 定义节点
 */
public class Node {
	private  int x;
	private  int y;
	private Node pre;
	
	public Node getPre() {
		return pre;
	}
	public void setPre(Node pre) {
		this.pre = pre;
	}

	public Node(int x, int y) {
		this.x = x;
		this.y = y;
		pre = null;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
	@Override
	public String toString() {
		return "[x=" + x + ", y=" + y + "]";
	}
	

}
