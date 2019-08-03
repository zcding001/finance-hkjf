/**
 * 房产投资JS-Controller
 * @type {{}}
 */
var houseController = {};
(function () {
    $(function () {
        //加载项目列表
        houseController.loadHouseInfoList = function (params) {
            params = (params == null ? {} : params);
            var result = pageUtil.initPaging("/houseController/investHouseList.do", params, _renderHousePage, 1,"houseList_paging");
            _renderHousePage(result,"houseList" );
        };

        function _renderHousePage(data,stickId){
            if(stickId == '' || stickId == undefined){
                stickId = "houseList";
            }
            var content = '';
            if (data == null || data.resMsg==null||data.resMsg.data == null)  {
                content = '<span class="kk">'+'暂无项目'+'</span>';
                $("." + stickId).after(content);
                return;
            }
            var list = data.resMsg.data;
            if(list.length > 0 ){
                for(var i in list){
                    var vo = list[i];
                    var indexUrl ;
                    if (vo.picUrl == null || vo.picUrl == ''){
                        indexUrl =  '${project_base_path}/src/img/house/index_house.jpg';
                    }else{
                        indexUrl = vo.picUrl;
                    }
                    content +=  '<li class="clearfix">'+
                                    '<div class="fl"><img src="'+indexUrl+'"  alt="鸿坤理想澜湾"/></div>'+
                                    '<div class="fl h_cont" style="width:500px;">'+
                                        '<h4>'+ vo.name +'</h4>'+
                                        '<p class="h_firstp">'+ vo.price +'</p>';
                    if(vo.roomType != null && vo.roomType != ''){
                        content += '<p><span>'+ vo.roomType +'</span>&nbsp;&nbsp;&nbsp;&nbsp;</p>';
                    }
                    if(vo.address != null && vo.address != ''){
                        content += '<p>'+ vo.address +'</p>';
                    }
                     content += '</div>'+
                                '<div class="fr" style="margin-top: 97px;margin-right: 50px;"><a href="javascript:;" onclick="houseController.toHouseDetail(\''+vo.id+'\')" class="xsb_tzbt" id="">预约看房</a></div>'+
                                '</li>';
                }
            }
            $("." + stickId).html(content);
        }

        /**
         * 跳转到房产详情页面
         * @param id
         */
        houseController.toHouseDetail = function(id){
            if(commonUtil.needLogin()){
                containerUtil.add("infoId",id);
                commonUtil.jump("house/investHouseDetails.html");
            }
        }

    });
})();