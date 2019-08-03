package com.hongkun.finance.payment.client.yeepay.heepayreturn;

public class SubmitOrderReturn {
	
	private String _pre_auth_uid;
	private String _hy_auth_uid;
	private String _agent_bill_id;
	private String _agent_id;
	private String _hy_token_id;
	private String _redirect_url;
	private boolean _success;	
	private String _error_message;
	
	
	public boolean is_success() {
		return _success;
	}

	public void set_success(boolean _success) {
		this._success = _success;
	}


	public String get_error_message() {
		return _error_message;
	}

	public void set_error_message(String _error_message) {
		this._error_message = _error_message;
	}

	public String get_pre_auth_uid()
	{
		return _pre_auth_uid;
	}

	public boolean is_Success() {
		return _success;
	}

	public void set_isSuccess(boolean _isSuccess) {
		this._success = _isSuccess;
	}

	public String get_hy_auth_uid() {
		return _hy_auth_uid;
	}

	public void set_hy_auth_uid(String _hy_auth_uid) {
		this._hy_auth_uid = _hy_auth_uid;
	}

	public String get_agent_bill_id() {
		return _agent_bill_id;
	}

	public void set_agent_bill_id(String _agent_bill_id) {
		this._agent_bill_id = _agent_bill_id;
	}

	public String get_agent_id() {
		return _agent_id;
	}

	public void set_agent_id(String _agent_id) {
		this._agent_id = _agent_id;
	}

	public String get_hy_token_id() {
		return _hy_token_id;
	}

	public void set_hy_token_id(String _hy_token_id) {
		this._hy_token_id = _hy_token_id;
	}

	public String get_redirect_url() {
		return _redirect_url;
	}

	public void set_redirect_url(String _redirect_url) {
		this._redirect_url = _redirect_url;
	}

	public void set_pre_auth_uid(String _pre_auth_uid) {
		this._pre_auth_uid = _pre_auth_uid;
	}	
	
	
	
}
