/***
 *  工具类
 */
var moneyUtil = {};
var Nums = new Array("零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"); 
var Digits = new Array("", "拾", "佰", "仟"); 
var Units = new Array("", "万", "亿", "万亿","仟兆"); 
(function(){
	'use strict';
	
	 /**
     * 金额四舍五入方法
     * @param money 需要四舍五入的数
     * @param afterDot 需要四舍五入的数位，如精确到小数点后两位小数，则 afterDot=2
     * return String
     */
	moneyUtil.round = function(money,afterDot){
		  var money,afterDot,resultStr,nTen;
		  money = "" + money + "";
		  var strLen = money.length;
		  var  dotPos = money.indexOf("." , 0);
		  if (dotPos == -1){
			  resultStr = money + ".";
			  for (var i = 0 ; i < afterDot; i++){
				  resultStr = resultStr + "0";
			  }
		  }else{
			  if ((strLen - dotPos - 1) >= afterDot){
			      nTen = 1;
			      for(var j = 0 ;j < afterDot; j++){
			    	  nTen = nTen * 10;
			      }
			      resultStr = Math.round(parseFloat(money) * nTen) / nTen;
			  }else{
			      resultStr = money;
			      for (var i = 0 ;i < (afterDot - strLen + dotPos + 1); i++){
			        resultStr = resultStr + "0";
			      }
			  }
		  }
		 return resultStr;
	};
    /**
     * 格式化金额：每隔断3位打逗号，保留afterDot位小数
     * @param money
     * @param afterDot
     * @returns {string}
     */
    moneyUtil.formatAndRoundMoney=function (money,afterDot){
        afterDot = (afterDot == null ? 2 : afterDot);
        var b=parseInt(money).toString();
        var len=b.length;
        if(len<=3){return moneyUtil.round(b, afterDot);}
        var r=len%3;
        var result=r>0?b.slice(0,r)+","+b.slice(r,len).match(/\d{3}/g).join(","):b.slice(r,len).match(/\d{3}/g).join(",");
        return moneyUtil.round(result, afterDot);
    }
	/**
	* 转换为中文金额表示
	* @param cash 要转换的金额字符串
	* @return  转换后的金额字符串
	*/
	moneyUtil.toChineseCash = function( cash ){
		if ( cash == null || cash.length == 0 ){
			return '';
		}
		var noCommaCash = _removeComma(cash);
		if ( parseFloat(cash) == 0 ){
			return '';
		}
		if( !isFloat( noCommaCash ) ){
			return convertIntegerToChineseCash(noCommaCash) + "整";
		}	
		var dotIndex = noCommaCash.indexOf('.');
		var integerCash = noCommaCash.substring( 0, dotIndex );
		var decimalCash = noCommaCash.substring( dotIndex + 1 );
		
		return convertIntegerToChineseCash(integerCash) + convertDecimalToChineseCash(decimalCash);
	};
	/**
	* 检查字符串是否为合法的金额
	* @param money
	* @return {bool} 是否为合法金额 true合法，false不合法
	*/
	moneyUtil.isMoney = function (money) {
		if(!validUtil.validNotEmpty(money)){ 
			return false
		}
		var isMoney = RegExp(/^[0-9]*\.?[0-9]{0,2}$/);
		return ( isMoney.test(money) );
	}
	/**
	 * 移除特殊字符
	 * @param str
	 */
	function _removeComma(str){
		return str.replace(new RegExp('\,',["g"]),'');
	}
	/**
	 * 将整形金额转换为中文金额
	 * @param cash
	 */
	function _convertIntegerToChineseCash(cash){
		if ( cash == "0" )
			return "";
	    var S = ""; //返回值 
	    var p = 0; //字符位置指针 
	    var m = cash.length % 4; //取模 
	    // 四位一组得到组数 
	    var k = (m > 0 ? Math.floor(cash.length / 4) + 1 : Math.floor(cash.length / 4)); 
	    // 外层循环在所有组中循环 
	    // 从左到右 高位到低位 四位一组 逐组处理 
	    // 每组最后加上一个单位: "[万亿]","[亿]","[万]" 
	    for (var i = k; i > 0; i--) {
	        var L = 4; 
	        if (i == k && m != 0){
	            L = m;
	        }
	        // 得到一组四位数 最高位组有可能不足四位 
	        var s = cash.substring(p, p + L);
	        var l = s.length;
	        // 内层循环在该组中的每一位数上循环 从左到右 高位到低位 
	        for (var j = 0;j < l; j++){
	            var n = parseInt(s.substring(j, j+1));
	            if (n == 0){
	                if ((j < l - 1) && (parseInt(s.substring(j + 1, j + 1+ 1)) > 0) //后一位(右低) 
	                    && S.substring(S.length-1,S.length) != Nums[n]){
	                    S += Nums[n];
	                }
	            }else {
	                 S += Nums[n];
	                 S +=  Digits[l - j - 1];
	            }
	        }
	        p += L;
	        // 每组最后加上一个单位: [万],[亿] 等 
	        if (i < k) {//不是最高位的一组
	            if (parseInt(s) != 0){
	                //如果所有 4 位不全是 0 则加上单位 [万],[亿] 等 
	                S += Units[i - 1];
	            }
	        }else{
	            //处理最高位的一组,最后必须加上单位 
	            S += Units[i - 1];
	        }
	    }
		return S+"圆";
	}
	/**
	 * 将浮点形金额转换为中文金额
	 * @param cash
	 */
	function _convertDecimalToChineseCash( cash ){
		var returnCash = "";
		if ( cash == "00" ){
			returnCash = "整";
		}else{
			for( var i = 0;i < cash.length; i++ ){
				if( i >= 2 )
					break;
				var intValue = parseInt(cash.charAt(i));
			
				switch( i ){
					case 0:
						if ( intValue != 0 )
							returnCash += Nums[intValue]+"角";
						break;
					case 1:
						returnCash += Nums[intValue]+"分";
						break;
					default:
						break;
				}
			}
			
		}
		return returnCash;	
	}



})();










