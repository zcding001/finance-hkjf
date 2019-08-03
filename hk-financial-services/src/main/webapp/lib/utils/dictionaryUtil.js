var dictionaryUtil = {"data":{}};
(function () {
	'use strict';
	dictionaryUtil.initDictionary = function(){
		if (!containerUtil.get("DIC_CONSTANT")){
			ajaxUtil.post("/dicController/dicList.do", {}, function(data){
				dictionaryUtil.data = data;
				containerUtil.set("DIC_CONSTANT",data);
			});
		}else {
			dictionaryUtil.data = containerUtil.get("DIC_CONSTANT");
		}
	};

	dictionaryUtil.getName = function(businessName, subjectName, value){
		var arr = dictionaryUtil.getValueAndName(businessName, subjectName);
		var name = value + "(未定义)";
		for(var i in arr){
			var o = arr[i];
			if(o.value == value){
				name = o.name;
				break;
			}
		}
		return name;
	};
	dictionaryUtil.getValue = function (businessName, subjectName, name) {
        var arr = dictionaryUtil.getValueAndName(businessName, subjectName);
		var value = "";
		for (var i in arr){
			var o = arr[i];
			if (o.name == name){
                value = o.value;
				break;
			}
		}
		return value;
    }
	dictionaryUtil.getNames = function(businessName, subjectName, values,splitSign){
		var showValue =  values.split(",");
		var name = '' ;
		if(commonUtil.isEmpty(splitSign)){
            splitSign = ","
		}

		for(var i in dictionaryUtil.data){
			var o = dictionaryUtil.data[i];
			if(o.businessName == businessName && o.subjectName == subjectName)
			{
				for(var j in showValue){
					var v = showValue[j];
					if(o.value==v){
						if(name==''){
							name = o.name;
						}else{
							name = name +splitSign+ o.name;
						}
					}
				}
			}
		}
		return name;
	};
    /**
	 * 根据limitValues来获取字典中的值
     * @param businessName
     * @param subjectName
     * @param limitValues
     * @returns {Array}
     */
	dictionaryUtil.getDicObjInLimitValues = function(businessName, subjectName, limitValues){
        var showValue =  limitValues.split(",");
        var objs=[];
        for(var i in dictionaryUtil.data){
            var o = dictionaryUtil.data[i];
            if(o.businessName == businessName && o.subjectName == subjectName)
            {
                for(var j in showValue){
                    var v = showValue[j];
                    if(o.value==v){
                        objs.push(o);
                    }
                }
            }
        }
        return objs;

	};
	//通过字典表初始化option
	dictionaryUtil.getOption = function(businessName, subjectName,valueName,limitValue,limitFalg/*正向判断或者反向判断，默认正向*/,needAllOption/*是否需要【全部】选项*/, allName/*全部的名称*/){
    	if(limitFalg==null){
    		limitFalg=true;
		}
		var option ="";
		var showValue=null;
		if(limitValue!=null){
            limitValue += '';
            showValue = limitValue.split(",");
		}
		if(needAllOption==null){
            needAllOption=true;
		}
		if(needAllOption){
			if(allName == undefined || allName == ""){
				allName = "全部";
			}
            if(commonUtil.isEmpty(valueName)){
                option ="<option value='-999'>" + allName + "</option>";
            }else if(valueName=="empty"){
                option ="<option value=''  >" + allName + "</option>";
            }else{
                option="<option value='"+valueName+"' >" + allName + "</option>";
            }
		}

		for(var i in dictionaryUtil.data){
			var o = dictionaryUtil.data[i];
			if(o.businessName == businessName && o.subjectName == subjectName){
				if(limitValue==null){
                    option += "<option value='" + o.value + "'>" + o.name + "</option>";
				}else{
					if(showValue!=null&&showValue.length>0){
						var showAdd=false;
						var findFlag=$.inArray(o.value+'', showValue)!=-1;
						if((limitFalg&&findFlag)||(!limitFalg&&!findFlag)){
                            showAdd=true;
						}
                        if(showAdd) {
                            option += "<option value='" + o.value + "'>" + o.name + "</option>";
                        }
					}
				}

			}
		}
		return option;
	};
	/**
	 * 通过字典表初始化下拉菜单
	 * @param name select元素的name值
	 * @param businessName 服务标识
	 * @param subjectName 业务标识
	 */
	dictionaryUtil.initSelect = function(name, businessName, subjectName, allFlag, allName){
		$("select[name='" + name + "']").empty();
		$("select[name='" + name + "']").append(dictionaryUtil.getOption(businessName, subjectName, null, null, null, allFlag, allName));
	};
	dictionaryUtil.getValueAndName = function(businessName, subjectName){
		var valueNameArray=[];
		for(var i in dictionaryUtil.data){
			var o = dictionaryUtil.data[i];
			var valueNameObj={};
			if(o.businessName == businessName && o.subjectName == subjectName){
				valueNameObj.value=o.value;
				valueNameObj.name=o.name;
				valueNameArray.push(valueNameObj);
			}
		}
		return valueNameArray;
	};

	/**
	 * 加载区域字典表
	 * @type {{data: {}}}
	 */
	var AREA_CONSTANT = {"data":{}};
	dictionaryUtil.initArea = function(){
		if (!containerUtil.get("AREA_CONSTANT")){
			$.ajax({
				url : CONSTANTS.BASE_PATH + "/dicController/dicAreaList.do",
				type : 'POST',
				dataType : 'JSON',
				async : true,
				success : function(data) {
					AREA_CONSTANT.data = data;
					containerUtil.set("AREA_CONSTANT", data);
				}
			});
		}else{
			AREA_CONSTANT.data = containerUtil.get("AREA_CONSTANT")
		}
	};
	//合同类型常量类
    var CONTRACTTYPE_CONSTANT = {};
    dictionaryUtil.initContract = function () {
        //containerUtil已保存合同类型则从容器中获取，否则请求后台获取
        if (!containerUtil.get("contractTypeAndName")){
            ajaxUtil.post("/contractController/getContractTypeAndName.do", null, function(data){
                CONTRACTTYPE_CONSTANT.data = data;
                containerUtil.set("contractTypeAndName",data);
            });
        }else {
            CONTRACTTYPE_CONSTANT.data = containerUtil.get("contractTypeAndName");
        }
    };
    dictionaryUtil.getContractName = function (type) {
        var name = type + "(未定义)";
        for(var i in CONTRACTTYPE_CONSTANT.data){
            var o = CONTRACTTYPE_CONSTANT.data[i];
            if(o.type == type){
                name = o.name;
                return name;
            }
        }
        return name;
    };
    dictionaryUtil.getContractShowName = function (type) {
        var name = type + "(未定义)";
        for(var i in CONTRACTTYPE_CONSTANT.data){
            var o = CONTRACTTYPE_CONSTANT.data[i];
            if(o.type == type){
                name = o.showName;
                return name;
            }
        }
        return name;
    };
	/**
	 * 获得省、市、县
	 * @param parentCode 可以areaCode或是areaName
	 * @returns {Array}
	 */
	dictionaryUtil.getAreaData = function(parentCode){
		if(parentCode == -1 || parentCode == -999){
			return [];
		}
		var arr = [];
		var areaCode = -1;
		for(var i in AREA_CONSTANT.data){
			var o = AREA_CONSTANT.data[i];
			if(o.areaName == parentCode){
				areaCode = o.areaCode;
				break;
			}
		}
		for(var i in AREA_CONSTANT.data){
			var o = AREA_CONSTANT.data[i];
			if(o.parentCode == parentCode || o.parentCode == areaCode){
				if(validUtil.validNotEmpty(o.areaName)){
					arr.push(o);
				}
			}
		}
		return arr;
	};
	/**
	 * 初始化地址的option
	 * @param parentCode 可以areaCode或是areaName
	 * @param areaName option中的value是areaCode还是areaName， 默认是areaCode
	 * @returns {string}
	 */
	dictionaryUtil.initAreaOption = function(parentCode, areaName){
		var arr = dictionaryUtil.getAreaData(parentCode);
		var option = '';
		if(arr != undefined && arr.length > 0){
			for(var i in arr){
				var o = arr[i];
				if(areaName != undefined && (areaName + "").length > 0){
					option += "<option value='" + o.areaName + "'>" + o.areaName + "</option>";
				}else{
					option += "<option value='" + o.areaCode + "'>" + o.areaName + "</option>";
				}
			}
		}
		return option;
	};

	/**
	 * 初始化省市县下拉菜单，
	 * @param provinceName 省select中的name
	 * @param cityName 市select中的name
	 * @param countryName 县select中的name
	 * @param currProvince 当前选中的省的值 areaName或areaCode都支持
	 * @param currCity 当前选中的市的值 areaName或areaCode都支持
	 * @param areaName option中value填充areaCode还是areaName， 默认填充areaCode即中文
	 */
	dictionaryUtil.initAreaSelect = function(provinceName, cityName, countryName, currProvince, currCity, areaName){
		var _birthProvince = $("select[name='" + provinceName + "']");
		var _birthCity = $("select[name='" + cityName + "']");
		var _birthCountry = $("select[name='" + countryName+ "']");
		//初始化省市县
		_birthProvince.empty();
		_birthProvince.append('<option value="-999">--省--</option>');
		_birthProvince.append(dictionaryUtil.initAreaOption(0, areaName));
		_birthCity.empty();
		_birthCity.append('<option value="-999">--市--</option>');
		if(currProvince != '' && currProvince.length > 0){
			_birthCity.append(dictionaryUtil.initAreaOption(currProvince, areaName));
		}
		_birthCountry.empty();
		_birthCountry.append('<option value="-999">--县--</option>');
		if(currCity != '' && currCity.length > 0){
			_birthCountry.append(dictionaryUtil.initAreaOption(currCity, areaName));
		}

		//添加省市更新事件
		_birthProvince.change(function(){
			_birthCity.empty();
			_birthCity.append('<option value="-999">--市--</option>');
			_birthCity.append(dictionaryUtil.initAreaOption($(this).val(), areaName));
			_birthCountry.empty();
			_birthCountry.append('<option value="-999">--县--</option>');
			_birthCity.change(function(){
				_birthCountry.empty();
				_birthCountry.append('<option value="-999">--县--</option>');
				_birthCountry.append(dictionaryUtil.initAreaOption($(this).val(), areaName));
			});
		});
	};


    /**
     * 为用户初始银行开户行地址省市下拉菜单
     * @param provinceName 省select中的name
     * @param cityName 市select中的name
     * @param currProvince 当前选中的省的值 areaName或areaCode都支持
     * @param currCity 当前选中的市的值 areaName或areaCode都支持
     * @param areaName 决定select下拉框的value，不填时默认为区域code，传递"cn"时为区域中文
     */
    dictionaryUtil.initBankCardAreaSelect = function (provinceName, cityName,currProvince,areaName) {
        var _birthProvince = $("select[name='" + provinceName + "']");
        var _birthCity = $("select[name='" + cityName + "']");
        //初始化省
        _birthProvince.empty();
        _birthProvince.append('<option value="-999">--省--</option>');
        _birthProvince.append(dictionaryUtil.initAreaOption(0, areaName));
        //初始化市
        _birthCity.empty();
        _birthCity.append('<option value="-999">--市--</option>');
        var getCityCode = function (provinceCode) {
			if (provinceCode == '110000'){
                provinceCode = '110100';
			}else if(provinceCode == '120000') {
                provinceCode = '120100';
			}else if(provinceCode == '310000') {
                provinceCode = '310100';
			}else if(provinceCode == '500000') {
                provinceCode = '500100';
			}
			return provinceCode;
        };
        if(currProvince != '' && currProvince.length > 0){
            //如果为北京、天津、上海、重庆直辖市时直接获取其下第三级地址
			currProvince = getCityCode(currProvince);
            _birthCity.append(dictionaryUtil.initAreaOption(currProvince, areaName));
        }
        //添加省更新事件
        _birthProvince.change(function(){
            _birthCity.empty();
            _birthCity.append('<option value="-999">--市--</option>');
            //如果为北京、天津、上海、重庆直辖市时直接获取其下第三级地址
            var currProvince = getCityCode($(this).val());
            _birthCity.append(dictionaryUtil.initAreaOption(currProvince, areaName));
        });
    };

	/**
	 * 通过地区编码获得地址名称
	 * @param areaCode 地区编码
	 * @returns {*}
	 */
	dictionaryUtil.getAreaName = function(areaCode){
		var arr = AREA_CONSTANT.data;
		console.log(arr);
		for(var i in arr){
			var o = arr[i];
			if(o.areaCode == areaCode){
				return o.areaName;
			}
		}
		return "";
	}
}());

$(function(){
	dictionaryUtil.initDictionary();
	dictionaryUtil.initArea();
    dictionaryUtil.initContract();
});
