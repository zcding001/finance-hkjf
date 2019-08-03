package com.hongkun.finance.payment.bfpayvo;

import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.DomDriver;

@XStreamAlias("trans_content")
public class TransContent<T> {

	public final static String DATA_TYPE_JSON = "json";

	public final static String DATA_TYPE_XML = "xml";

	public TransContent() {
	}

	@XStreamAlias("trans_head")
	private TransHead trans_head;

	@XStreamAlias("trans_reqDatas")
	private List<T> trans_reqDatas;

	@XStreamOmitField
	private String data_type;

	@XStreamOmitField
	private XStream xStream;

	/**
	 * 数据类型 xml/json
	 * 
	 * @param data_type
	 */
	public TransContent(String data_type) {
		if (DATA_TYPE_JSON.equals(data_type)) {
			xStream = new XStream(new JettisonMappedXmlDriver());
		} else {
			xStream = new XStream(new DomDriver("UTF-8", new NoNameCoder()));
		}
		// 启用Annotation
		xStream.autodetectAnnotations(true);
		this.data_type = data_type;
	}

	public TransHead getTrans_head() {
		return trans_head;
	}

	public void setTrans_head(TransHead trans_head) {
		this.trans_head = trans_head;
	}

	public List<T> getTrans_reqDatas() {
		return trans_reqDatas;
	}

	public void setTrans_reqDatas(List<T> trans_reqDatas) {
		this.trans_reqDatas = trans_reqDatas;
	}

	public String getData_type() {
		return data_type;
	}

	public void setData_type(String data_type) {
		this.data_type = data_type;
	}

	public XStream getxStream() {
		return xStream;
	}

	public void setxStream(XStream xStream) {
		this.xStream = xStream;
	}

	/**
	 * @Description : 将对象转换为报文字符串
	 * @Method_Name : obj2Str;
	 * @param obj
	 *            对象
	 * @return
	 * @return : String;
	 * @Creation Date : 2018年1月18日 上午11:45:53;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public String obj2Str(Object obj) {
		return xStream.toXML(obj);
	}

	/**
	 * @Description : 将报文字符串转换为对象
	 * @Method_Name : str2Obj;
	 * @param str
	 *            字符串
	 * @param clazz
	 *            要转换的对象的Class
	 * @return
	 * @return : Object;
	 * @Creation Date : 2018年1月18日 上午11:45:21;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public Object str2Obj(String str, Class<T> clazz) {
		xStream.alias("trans_content", this.getClass());
		xStream.alias("trans_reqData", clazz);
		return xStream.fromXML(str);
	}

}