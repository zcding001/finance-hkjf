package com.hongkun.finance.roster.constants;

/**
 * @Description   : 黑白名单标识
 * @Project       : finance-roster-model
 * @Program Name  : com.hongkun.finance.roster.constants.RosterFlag.java
 * @Author        : zhichaoding@hongkun.com zc.ding
 */
public enum RosterFlag {

    DEFAULT(-999),
	/**
	 * 黑名单
	 */
	BLACK(0),
	/**
	 * 白名单
	 */
	WHITE(1);
	
	private int value;
	
	private RosterFlag(int value){
		this.value = value;
	}
	
	public int getValue(){
		return this.value;
	}

    public static RosterFlag getRosterFlag(Integer val){
        for(RosterFlag rt : RosterFlag.values()){
            if(rt.getValue() == val){
                return rt;
            }
        }
        return RosterFlag.DEFAULT;
    }
}
