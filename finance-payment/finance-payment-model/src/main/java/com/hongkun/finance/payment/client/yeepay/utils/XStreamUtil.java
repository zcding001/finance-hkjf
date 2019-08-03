package com.hongkun.finance.payment.client.yeepay.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hongkun.finance.payment.client.yeepay.vo.TransferItem;
import com.hongkun.finance.payment.client.yeepay.vo.TransferList;
import com.hongkun.finance.payment.client.yeepay.vo.TransferSingleReq;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

public class XStreamUtil {
	private static XStream xStream;  
    
    //JVM加载类时会执行这些静态的代码块，如果static代码块有多个，JVM将按照它们在类中出现的先后顺序依次执行它们，每个代码块只会被执行一次。  
    static{  
        xStream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
       xStream.alias("data", TransferSingleReq.class);
       xStream.alias("list", java.util.List.class);
       //xStream.alias("list", java.util.Map.class);
       xStream.alias("item", TransferItem.class);
      // xStream.addDefaultImplementation(java.util.List.class, TransferItem.class);
       xStream.addImmutableType(java.util.List.class);
       //xStream.useAttributeFor(java.util.List.class);
      // xStream.addImplicitCollection(java.util.List.class, "list", TransferItem.class);
       //xStream.addImplicitCollection(TransferItem.class, "item");
    }  
      
    //xml转java对象  
    public static Object xmlToBean(String xml){  
        return xStream.fromXML(xml);  
    }  
      
    //java对象转xml  
    public static String beanToXml(Object obj){  
        return "<?xml version=\"1.0\" encoding=\"GBK\"?>\n" + xStream.toXML(obj);  
    }  
    public static void main(String[] args){
    	TransferSingleReq req = new TransferSingleReq();
    	req.setBank_Code("1233");
    	TransferItem item = new TransferItem();
    	item.setAmount("100");
    	List<TransferItem> items = new ArrayList<TransferItem>();
    	items.add(item);
    	TransferList list = new TransferList();
    	list.setItems(items);
    	req.setList(list);
    	
    	System.out.println(XStreamUtil.beanToXml(req));
    }
}
