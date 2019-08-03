package com.hongkun.finance.user.constants;

/**
*  注册来源枚举类
*  @return 
*  @Creation Date           ：2018/5/18
*  @Author                  ：zhichaoding@hongkun.com.cn
*/
public enum UserRegistSource {

    /**
    *  乾坤袋PC
    */
	QKD_PC("QKD_PC", 1),
    /**
     *  乾坤袋wap
     */ 
    QKD_WAP("QKD_WAP", 2),
    /**
     *  乾坤袋app
     */
    QKD_APP("QKD_APP", 3),
    /**
     *  前生活PC
     */
    QSH_PC("QSH_PC", 11),
    /**
     *  前生活wap
     */
    QSH_WAP("QSH_WAP", 12),
    /**
     *  前生活app
     */
    QSH_APP("QSH_APP", 13),
    /**
     *  鸿坤金服pc
     */
    HKJF_PC("HKJF_PC", 21),
    /**
     *  鸿坤金服wap
     */
    HKJF_WAP("HKJF_WAP", 22),
    /**
     *  鸿坤金服app
     */
    HKJF_APP("HKJF_APP", 23),
    /**
     *  鸿坤金服pc
     */
    CXJ_PC("CXJ_PC", 31), 
    /**
     *  鸿坤金服wap
     */
    CXJ_WAP("CXJ_WAP", 32),
    /**
     *  鸿坤金服 app
     */
    CXJ_APP("CXJ_APP", 33),
    /**
     *  导入
     */
    IMPORT("IMPORT", 99);

	/**
	 * 标识
	 */
	private String type;
	/**
	 * 名称
	 */
	private int value;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	private UserRegistSource(String type, int value) {

		this.type = type;
		this.value = value;

	}
}
