package com.snake;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * @author yh
 * ��ͼ��
 */
public class Grid {
	private final int width;//��
	private final int height;//��
	private boolean status[][];//�����Ƿ��߸��ǣ�trueΪ����,��Ҫ��������ʳ��ʱ�ж��Ƿ�ˢ�����ߵ�������
	private Snake snake;//��
	private Node food;//ʳ��
	private ArrayList<Node> path;
	//�������飬��������
	private static final int mov[][] = {{0,-1},{0,1},{-1,0},{1,0}};
	private  Direction snakeDirection = Direction.LEFT;//���˶�����Ĭ����
	/**
	 * ��ʼ����ͼ
	 */
	public Grid(int width, int height) {
		this.width = width;
		this.height = height;
		this.status = new boolean[width / Settings.DEFAULT_NODE_SIZE][height / Settings.DEFAULT_NODE_SIZE];
		this.path = new ArrayList<Node>();
		initSnake();
		createFood();
	}

	/**
	 * ��ʼ����
	 * @return ����snake����
	 */
	private Snake initSnake() {
		this.snake = new Snake();
		for(int i = 0; i < this.width / Settings.DEFAULT_NODE_SIZE; i++) {
			for(int j = 0; j < this.height / Settings.DEFAULT_NODE_SIZE; j++) {
				
				status[i][j] = false;
			}
		}
		
		/*for(int i = 0; i < this.height / Settings.DEFAULT_NODE_SIZE; i++) {
			status[0][i] = true;
			status[this.height / Settings.DEFAULT_NODE_SIZE - 1][i] = true;
			status[i][0] = true;
			status[i][this.height / Settings.DEFAULT_NODE_SIZE - 1] = true;
		}*/
		
		//�����ߵ�body,�������̸���״̬
		for(int i = 0; i < this.width / (3 * Settings.DEFAULT_NODE_SIZE); i++) {
			this.snake.addTail(new Node(i,this.height / ( 2 * Settings.DEFAULT_NODE_SIZE)));
			status[this.height / ( 2 * Settings.DEFAULT_NODE_SIZE )][i] = true;
		}
		return this.snake;
	}
	/**
	 * �������ʳ��
	 * @return ��������ʳ�������
	 */
	public Node createFood() {
		int x,y;
		Random r = new Random();
		do {
			x = r.nextInt(this.width / Settings.DEFAULT_NODE_SIZE - 1);
			y = r.nextInt(this.height / Settings.DEFAULT_NODE_SIZE - 1);
		} while(status[y][x] );
		//(status[y + 1][x] && status[y - 1][x] &&status[y][x - 1] && status[y][x + 1] )
		
		this.food = new Node(x,y);
		return this.food;
	}
	/**
	 * �ƶ�һ��
	 * @return true ��Ϸ���� false ��Ϸ����
	 */
	public boolean nextRound(){
		
		//�������ͷ����ʳ������·����,���ɸ����尴���·��ȥ��ʳ��
		if(this.bfsFood(this.snake.getHead(), this.getFood())) {
			//���������ʳ�����Ȼ��ȫ�����ñ���ȥ�Ը�ʳ��
			if(this.copySnakeGoIsSafe()) {
				goAsPath(this.path);
			}
			//���������ʳ��󲻰�ȫ,���ñ�����ͷ����β�������·��(ҧβ��)
			else {
				catchTail();
			}
		} 
		//�������ͷ����ʳ������·�����ڣ����ñ���ҧβ��
		else
			catchTail();
		
		return true;
	}
	/**
	 * ҧβ��
	 */
	private void catchTail() {
		
		int maxStep = 0;
		Direction dir = null;
		boolean flag = false;
		List<ArrayList<Node>> pathList = new ArrayList<ArrayList<Node>>();
		for(int i = 0; i < 4; i++) {
			this.bfsFood(this.snake.getHead(), new Node(this.snake.getEnd().getX() + mov[i][0],this.snake.getEnd().getY() + mov[i][1]));
			ArrayList<Node> arr = new ArrayList<Node>();
			for(int j = 0; j < this.path.size(); j++) {
				arr.add(this.path.get(j));
			}
			pathList.add(arr);
		}
		int ans = -1,index = -1;
		for(int i = 0; i < 4; i++) {
			if(ans < pathList.get(i).size()) {
				ans = pathList.get(i).size();
				index = i;
			}
		}
		goAsPath(pathList.get(index));
	}


	private void goAsPath(ArrayList<Node> path) {
		if(path.size() == 1) {
			if(this.snake.getHead().getX() + 1 == this.snake.getEnd().getX())
				this.trueMov(Direction.RIGHT);
			else if(this.snake.getHead().getX() - 1 == this.snake.getEnd().getX())
				this.trueMov(Direction.LEFT);
			else if(this.snake.getHead().getY() + 1 == this.snake.getEnd().getY())
				this.trueMov(Direction.DOWN);
			else if(this.snake.getHead().getY() - 1 == this.snake.getEnd().getY())
				this.trueMov(Direction.UP);	
		}
		else if(path.size() > 1){
			if(path.get(path.size() - 1).getX() + 1 == path.get(path.size() - 2).getX()) 
				this.trueMov(Direction.RIGHT);
			else if(path.get(path.size() - 1).getX() - 1 == path.get(path.size() - 2).getX())
				this.trueMov(Direction.LEFT);
			else if(path.get(path.size() - 1).getY() - 1 == path.get(path.size() - 2).getY())
				this.trueMov(Direction.UP);
			else
				this.trueMov(Direction.DOWN);
		}
		
	}

	/**
	 * ����һ�ŵ�ͼ���ø�����(��)�����ŵ�ͼ��ģ���ʳ����жϳ���֮���Ƿ�ȫ
	 * @return �������ʳ���ȫ�򷵻�true
	 */
	private boolean copySnakeGoIsSafe() {
		
		Grid newGrid = copyGrid();//���Ƶ�ͼ
		//newGrid.path.add(this.snake.getHead());
		int len = newGrid.path.size();
		//�����尴���·���ƶ�
		/*for(int i = len - 1; i >0 ; i--) {
			System.out.print(newGrid.path.get(i) + " ");
		}System.out.println();*/
		//System.out.println(newGrid.snake.getBody());
		for(int i = len - 1; i > 0 ; i--) {
			//��
			if(newGrid.path.get(i - 1).getX() == newGrid.path.get(i).getX() + 1) {
					//newGrid.changeDirection(Direction.RIGHT);
					newGrid.trueMov(Direction.RIGHT);
					
			} else if(newGrid.path.get(i - 1).getX() == newGrid.path.get(i).getX() - 1) {
				newGrid.trueMov(Direction.LEFT);
			} else if(newGrid.path.get(i - 1).getY() == newGrid.path.get(i).getY() - 1) {
				newGrid.trueMov(Direction.UP);
			} else{
				newGrid.trueMov(Direction.DOWN);
			}
				
		}
		//�жϸ�����ͷ����β������ͨ�ԣ������ͨ��˵����ȫ��������ԳԸ�ʳ��
		if(bfsCopySnake(newGrid.snake.getHead(), newGrid.snake.getEnd(), newGrid))
			return true;
		return false;	
	}

	private Grid copyGrid() {
		
		Grid newGrid = new Grid(width, height);
		//����״̬����
		for(int i = 0; i < this.status.length; i++) {
			for(int j = 0; j < this.status[i].length; j++) {
				newGrid.status[i][j] = this.status[i][j];
			}
		}
		//������
		newGrid.snake.getBody().clear();
		Iterator<Node> itThis = this.snake.getBody().iterator();
		while(itThis.hasNext()) {
			Node obj = itThis.next();
			newGrid.snake.getBody().add(obj);
		}
		//����ʳ������
		newGrid.food.setX(this.food.getX());
		newGrid.food.setY(this.food.getY());
		
		//����·������
		for(int i = 0; i < this.path.size(); i++) {
			//System.out.println(this.path.size());
			newGrid.path.add(this.path.get(i));
		}
			
		
		return newGrid;
	}
	
	/**
	 * ��������ͷ������ʳ������·��������·��
	 * @return ����������·������true,��֮false
	 */
	public boolean bfsFood(Node start, Node goal) {
		
		Queue<Node> Q = new LinkedList<Node>();
	    Q.clear();this.path.clear();
	    boolean vis[][] = new boolean[Settings.DEFAULT_GRID_WIDTH / Settings.DEFAULT_NODE_SIZE][Settings.DEFAULT_GRID_HEIGHT / Settings.DEFAULT_NODE_SIZE];
	    Node tem;
	    Q.offer(start);
	    vis[start.getY()][start.getX()] = true;
	    start.setPre(null);
	    while(!Q.isEmpty()) {
	    	Node v = Q.peek();Q.poll();	
	    	if(v.getX() == goal.getX() && v.getY() == goal.getY()) {
	    		Node xx = v;
	    		//����·��
	    		while(xx.getPre() != null) {
	    			this.path.add(xx);
	    			xx = xx.getPre();
	    		}this.path.add(xx);
	    		
	    		return true;
	    	}
	    	for(int i = 0; i < 4; i++) {
	    		//�������ڵ�4����
	    		tem = new Node(v.getX() + mov[i][0],v.getY() + mov[i][1]);
	    		tem.setPre(v);
	    		if(this.isValidHeadPos(tem) && !vis[tem.getY()][tem.getX()]) {
	    			vis[tem.getY()][tem.getX()] = true;
	    			Q.offer(tem);
	    		}	
	    	}
	    }
		return false;
	}
	
	/**
	 * ����������ͷ����β���Ƿ���ͨ(����Ҫ����·��)
	 * @return ����������·������true,��֮false
	 */
	public boolean bfsCopySnake(Node start, Node goal,Grid newGrid) {
		
		Queue<Node> Q = new LinkedList<Node>();
	    Q.clear();
	    boolean vis[][] = new boolean[Settings.DEFAULT_GRID_WIDTH / Settings.DEFAULT_NODE_SIZE][Settings.DEFAULT_GRID_HEIGHT / Settings.DEFAULT_NODE_SIZE];
	    Node tem;
	    Q.offer(start);
	    vis[start.getY()][start.getX()] = true;
	    
	    while(!Q.isEmpty()) {
	    	Node v = Q.peek();Q.poll();	
	    	if( (v.getX() == goal.getX() - 1 && v.getY() == goal.getY()) || 
	    		(v.getX() == goal.getX() + 1 && v.getY() == goal.getY()) || 
	    		(v.getX() == goal.getX() && v.getY() == goal.getY() + 1) || 
	    		(v.getX() == goal.getX() && v.getY() == goal.getY() - 1) ){ 
	    		return true;
	    	}
	    	for(int i = 0; i < 4; i++) {
	    		//�������ڵ�4����
	    		tem = new Node(v.getX() + mov[i][0],v.getY() + mov[i][1]);
	    		if(newGrid.isValidHeadPos(tem) && !vis[tem.getY()][tem.getX()]) {
	    			vis[tem.getY()][tem.getX()] = true;
	    			Q.offer(tem);
	    		}	
	    	}
	    }
		return false;
	}
	/**
	 * �������ƶ�һ�������ر�ɾ����β�ڵ�
	 * @param direction
	 * @return
	 */
	public void trueMov(Direction direction) {
		
		this.changeDirection(direction);
		Node end = this.snake.move(direction);
		Node head = this.snake.getHead();
		//System.out.println("ͷ��" + head);
		this.status[end.getY()][end.getX()] = false;
		this.status[head.getY()][head.getX()] = true;
		
		//����Ե�ʳ���֮ǰɾ����β����ӽ���
		if(head.getX() == this.food.getX() && head.getY() == this.food.getY()) {
			this.snake.addTail(end);
			this.status[end.getY()][end.getX()] = true;
			this.createFood();
		}
	}

	/**
	 * �жϸýڵ��Ƿ���Ч
	 * @param node 
	 * @return ���� true ��Ч false ��Ч
	 */
	public boolean isValidHeadPos(Node node) {
		//�����߽�,�����������������
		if( node.getX() >= this.width / Settings.DEFAULT_NODE_SIZE ||  
			node.getY() >= this.height / Settings.DEFAULT_NODE_SIZE ||
			node.getX() < 0 ||
			node.getY() < 0 )
			return false;
		//�����Լ�
		if(this.status[node.getY()][node.getX()]) return false;
		return true;	
	}
	//�޸��ƶ�����
	public boolean changeDirection(Direction newDirection) {
		if(this.snakeDirection.compatibleWith(newDirection)) {
			this.snakeDirection = newDirection;
			return true;
		}
		return false;
	}

	public Snake getSnake() {
		// TODO Auto-generated method stub
		return this.snake;
	}

	public Node getFood() {
		// TODO Auto-generated method stub
		return this.food;
	}

	public int getWidth() {
		// TODO Auto-generated method stub
		return this.width;
	}

	public int getHeight() {
		// TODO Auto-generated method stub
		return this.height;
	}
	public void print() {
		for(int i = this.path.size() - 1; i >= 0; i--) {
				System.out.print(this.path.get(i));
		}
			System.out.println();
		
	}
}
