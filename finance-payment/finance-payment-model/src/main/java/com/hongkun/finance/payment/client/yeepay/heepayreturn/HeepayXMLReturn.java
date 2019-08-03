package com.hongkun.finance.payment.client.yeepay.heepayreturn;

public class HeepayXMLReturn {

	private String _ret_code;
	private String _ret_msg;
	private String _encrypt_data;
	
	public String get_retcode()
	{
		return _ret_code;
	}
	
	public void set_retcode(String value)
	{
		 _ret_code=value;
	}
	
	
	public String get_retmsg()
	{
		return _ret_msg;
	}
	
	public void set_retmsg(String value)
	{
		_ret_msg=value;
	}	
	
	public String get_encryptdata()
	{
		return _encrypt_data;
	}
	
	public void set_encryptdata(String value)
	{
		_encrypt_data=value;
	}
}
