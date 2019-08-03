package com.hongkun.finance.payment.client.yeepay.heepayreturn;

public class HeepayQueryBanksReturn {
	
	private String _bank_id;//银行ID
	private String _bank_name;//银行名称
	private String _card_last_four;//卡号后四位
	private String _card_type;//卡类型ID( 0储蓄卡 1 信用卡)
	private String _card_auth_uid;//授权码
	public String get_card_type() {
		return _card_type;
	}
	public String get_card_last_four() {
		return _card_last_four;
	}
	public void set_card_last_four(String _card_last_four) {
		this._card_last_four = _card_last_four;
	}
	public String get_card_auth_uid() {
		return _card_auth_uid;
	}
	public void set_card_auth_uid(String _card_auth_uid) {
		this._card_auth_uid = _card_auth_uid;
	}
	public void set_card_type(String _card_type) {
		this._card_type = _card_type;
	}
	public String get_bank_id() {
		return _bank_id;
	}
	public void set_bank_id(String _bank_id) {
		this._bank_id = _bank_id;
	}
	public String get_bank_name() {
		return _bank_name;
	}
	public void set_bank_name(String _bank_name) {
		this._bank_name = _bank_name;
	}
	

}
