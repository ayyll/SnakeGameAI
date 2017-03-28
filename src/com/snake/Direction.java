package com.snake;

/**
 * @author yh
 * �ƶ�����
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
	 * �ж����뷽���Ƿ���Ч���޷���ͷ�������統ǰ����Ϊ�ϣ��޸ķ���Ϊ�£����޷��ı䣬����false
	 */
	public boolean compatibleWith(Direction newDirection) {
		if(Math.abs(newDirection.directionCode-this.directionCode) == 2)
			return false;
		return true;
	}
}