package com.snake;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * @author yh
 * 地图类
 */
public class Grid {
	private final int width;//宽
	private final int height;//高
	private boolean status[][];//方格是否被蛇覆盖，true为覆盖,主要用于生成食物时判断是否刷新在蛇的身体上
	private Snake snake;//蛇
	private Node food;//食物
	private ArrayList<Node> path;
	//方向数组，上下左右
	private static final int mov[][] = {{0,-1},{0,1},{-1,0},{1,0}};
	private  Direction snakeDirection = Direction.LEFT;//蛇运动方向，默认左
	/**
	 * 初始化地图
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
	 * 初始化蛇
	 * @return 返回snake引用
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
		
		//设置蛇的body,更新棋盘覆盖状态
		for(int i = 0; i < this.width / (3 * Settings.DEFAULT_NODE_SIZE); i++) {
			this.snake.addTail(new Node(i,this.height / ( 2 * Settings.DEFAULT_NODE_SIZE)));
			status[this.height / ( 2 * Settings.DEFAULT_NODE_SIZE )][i] = true;
		}
		return this.snake;
	}
	/**
	 * 随机生成食物
	 * @return 返回生成食物的引用
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
	 * 移动一步
	 * @return true 游戏继续 false 游戏结束
	 */
	public boolean nextRound(){
		
		//如果本体头部到食物的最短路存在,就派复制体按最短路径去吃食物
		if(this.bfsFood(this.snake.getHead(), this.getFood())) {
			//复制体吃完食物后仍然安全，就让本体去吃该食物
			if(this.copySnakeGoIsSafe()) {
				goAsPath(this.path);
			}
			//复制体吃完食物后不安全,则让本体搜头部到尾部的最短路径(咬尾巴)
			else {
				catchTail();
			}
		} 
		//如果本体头部到食物的最短路不存在，则让本体咬尾巴
		else
			catchTail();
		
		return true;
	}
	/**
	 * 咬尾巴
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
	 * 复制一张地图，让复制体(蛇)在这张地图中模拟吃食物，并判断吃完之后是否安全
	 * @return 如果吃完食物后安全则返回true
	 */
	private boolean copySnakeGoIsSafe() {
		
		Grid newGrid = copyGrid();//复制地图
		//newGrid.path.add(this.snake.getHead());
		int len = newGrid.path.size();
		//复制体按最短路径移动
		/*for(int i = len - 1; i >0 ; i--) {
			System.out.print(newGrid.path.get(i) + " ");
		}System.out.println();*/
		//System.out.println(newGrid.snake.getBody());
		for(int i = len - 1; i > 0 ; i--) {
			//右
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
		//判断复制体头部和尾部的连通性，如果连通，说明安全，本体可以吃该食物
		if(bfsCopySnake(newGrid.snake.getHead(), newGrid.snake.getEnd(), newGrid))
			return true;
		return false;	
	}

	private Grid copyGrid() {
		
		Grid newGrid = new Grid(width, height);
		//复制状态数组
		for(int i = 0; i < this.status.length; i++) {
			for(int j = 0; j < this.status[i].length; j++) {
				newGrid.status[i][j] = this.status[i][j];
			}
		}
		//复制蛇
		newGrid.snake.getBody().clear();
		Iterator<Node> itThis = this.snake.getBody().iterator();
		while(itThis.hasNext()) {
			Node obj = itThis.next();
			newGrid.snake.getBody().add(obj);
		}
		//复制食物坐标
		newGrid.food.setX(this.food.getX());
		newGrid.food.setY(this.food.getY());
		
		//复制路径数组
		for(int i = 0; i < this.path.size(); i++) {
			//System.out.println(this.path.size());
			newGrid.path.add(this.path.get(i));
		}
			
		
		return newGrid;
	}
	
	/**
	 * 搜索本体头部距离食物的最短路径并保存路径
	 * @return 如果存在最短路径返回true,反之false
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
	    		//逆序路径
	    		while(xx.getPre() != null) {
	    			this.path.add(xx);
	    			xx = xx.getPre();
	    		}this.path.add(xx);
	    		
	    		return true;
	    	}
	    	for(int i = 0; i < 4; i++) {
	    		//搜索相邻的4个格
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
	 * 搜索复制体头部和尾部是否连通(不需要保存路径)
	 * @return 如果存在最短路径返回true,反之false
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
	    		//搜索相邻的4个格
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
	 * 真正的移动一步，返回被删除的尾节点
	 * @param direction
	 * @return
	 */
	public void trueMov(Direction direction) {
		
		this.changeDirection(direction);
		Node end = this.snake.move(direction);
		Node head = this.snake.getHead();
		//System.out.println("头部" + head);
		this.status[end.getY()][end.getX()] = false;
		this.status[head.getY()][head.getX()] = true;
		
		//如果吃到食物，把之前删掉的尾巴添加进来
		if(head.getX() == this.food.getX() && head.getY() == this.food.getY()) {
			this.snake.addTail(end);
			this.status[end.getY()][end.getX()] = true;
			this.createFood();
		}
	}

	/**
	 * 判断该节点是否有效
	 * @param node 
	 * @return 返回 true 有效 false 无效
	 */
	public boolean isValidHeadPos(Node node) {
		//超出边界,分上下左右四种情况
		if( node.getX() >= this.width / Settings.DEFAULT_NODE_SIZE ||  
			node.getY() >= this.height / Settings.DEFAULT_NODE_SIZE ||
			node.getX() < 0 ||
			node.getY() < 0 )
			return false;
		//碰到自己
		if(this.status[node.getY()][node.getX()]) return false;
		return true;	
	}
	//修改移动方向
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
