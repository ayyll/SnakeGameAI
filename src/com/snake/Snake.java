package com.snake;

import java.util.LinkedList;

public class Snake {
	private LinkedList<Node> body = new LinkedList<Node>();
	
	public Node getHead() {
		return body.getFirst();
	} 
	public Node addTail(Node area) {
		this.body.addLast(area);
		return area;
	}
	public LinkedList<Node> getBody() {
		return body;
	}
	public Node getEnd() {
		return body.getLast();
	}
	
	/**
	 * 吃食物
	 * @param food
	 * @return 如果碰到食物，吃并返回食物节点，否则返回null
	 */
	public Node eat(Node food) {
		Node headNode = getHead();
		if(Math.abs(headNode.getX() - food.getX()) + Math.abs(headNode.getY() - food.getY()) == 1) {
			addTail(food);
			return food;
		} else 
			return null;
	}
	/**
	 * 移动一步
	 * @param direction
	 * @return 删除原先的尾节点，在头部添加一个新节点，并返回删除的尾节点
	 */
	public Node move(Direction direction) {
		Node endNode = this.body.getLast();
		Node headNode = this.body.getFirst();
		if(direction == Direction.UP) {
			this.body.addFirst(new Node(headNode.getX(),headNode.getY() - 1));
			this.body.removeLast();
		} else if(direction == Direction.DOWN) {
			this.body.addFirst(new Node(headNode.getX(),headNode.getY() + 1 ));
			this.body.removeLast();
		} else if(direction == Direction.LEFT) {
			this.body.addFirst(new Node(headNode.getX() - 1,headNode.getY()));
			this.body.removeLast();
		} else if(direction == Direction.RIGHT) {
			this.body.addFirst(new Node(headNode.getX() + 1,headNode.getY()));
			this.body.removeLast();
		}
		return endNode;
	}
}
