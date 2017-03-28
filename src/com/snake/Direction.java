package com.snake;

/**
 * @author yh
 * 移动方向
 */
public enum Direction {
	UP(0),
	RIGHT(1),
	DOWN(2),
	LEFT(3);
	
	private final int directionCode;
	public int directionCode() {
		return this.directionCode; 
	}
	Direction( int directionCode ){
		this.directionCode = directionCode;
	}
	
	/**
	 * @param newDirection
	 * @return 
	 * 判断输入方向是否有效，无法回头，即假如当前方向为上，修改方向为下，则无法改变，返回false
	 */
	public boolean compatibleWith(Direction newDirection) {
		if(Math.abs(newDirection.directionCode-this.directionCode) == 2)
			return false;
		return true;
	}
}