app.controller('userAuditController', function ($scope, $timeout, $http, $CommonService, $compile, $location) {
	var regUserId = $location.search().id;
	$CommonService.httpPost({"regUserId":regUserId}, CONFIG.interface.userAudit)
		.success(function (data) {
			$scope.regUser = data.params.regUser;
			$scope.list = data.params.list;
		});
	
    $scope.$on('ngRepeatFinished', function (ngRepeatFinishedEvent) {
        //下面是在table render完成后执行的js
        var list = $scope.list;
        if(list != undefined && list.length > 0){
            for(var i in list) {
                $scope.initUploadBtn(list[i].id, regUserId, list[i].type, list[i].picList);
            }
        }
    });

    $scope.initUploadBtn = function(index, regUserId, auditType, picList){
        // 初始化Web Uploader
        var $list = $('#fileList' + index),
            ratio = window.devicePixelRatio || 1,
            thumbnailWidth = 50 * ratio,
            thumbnailHeight = 50 * ratio;
        
        var uploader = WebUploader.create({
            // 选完文件后，是否自动上传。
            auto: true,
            // swf文件路径
            swf: 'jslib/webuploader/Uploader.swf',
            // 文件接收服务端。
            server: CONFIG.interface.userAuditUpload,
            // 选择文件的按钮。可选;内部根据当前运行是创建，可能是input元素，也可能是flash.
            pick: '#filePicker' + index,
            // 只允许选择图片文件。
            accept: {
                title: 'Images',
                extensions: 'gif,jpg,jpeg,bmp,png',
                mimeTypes: 'image/*'
            },
            formData : {regUserId : regUserId, auditType:auditType}
        });
        
       uploader.on( 'fileQueued', function(file) {
            var $li = $(
                '<div id="' + file.id + '" class="file-item thumbnail" auditType="' + auditType+ '">' +
                '<img>' +
                '<div class="info">' + file.name + '</div>' +
                '<div id="del' + file.id + '" class="delete">×</div>' +
                '</div>'
                ),
                $img = $li.find('img');
            // $list为容器jQuery实例
            $list.append( $li );
            // 创建缩略图,如果为非图片文件，可以不用调用此方法。thumbnailWidth x thumbnailHeight 为 100 x 100
            uploader.makeThumb( file, function( error, src ) {
                if ( error ) {
                    $img.replaceWith('<span>不能预览</span>');
                    return;
                }
                $img.attr( 'src', src );
            }, thumbnailWidth, thumbnailHeight );
        });

        // 文件上传过程中创建进度条实时显示。
        uploader.on( 'uploadProgress', function( file, percentage ) {
            var $li = $( '#'+file.id ),
                $percent = $li.find('.progress span');
            // 避免重复创建
            if ( !$percent.length ) {
                $percent = $('<p class="progress"><span></span></p>')
                    .appendTo( $li )
                    .find('span');
            }
            $percent.css( 'width', percentage * 100 + '%' );
        });

        // 文件上传成功，给item添加成功class, 用样式标记上传成功。
        uploader.on( 'uploadSuccess', function( file ) {
            $( '#'+file.id ).addClass('upload-state-done');
        });

        // 文件上传失败，显示上传出错。
        uploader.on( 'uploadError', function( file ) {
            var $li = $( '#'+file.id ),
                $error = $li.find('div.error');
            // 避免重复创建
            if ( !$error.length ) {
                $error = $('<div class="error"></div>').appendTo( $li );
            }
            $error.text('上传失败');
        });

        // 完成上传完了，成功或者失败，先删除进度条。
        uploader.on( 'uploadComplete', function( file ) {
            $( '#'+file.id ).find('.progress').remove();
        });

        // 完成上传完了，成功或者失败，先删除进度条。
        uploader.on( 'uploadSuccess', function(file, response) {
            var fileName = response._raw.substr(response._raw.lastIndexOf("/") + 1);
            $( '#del'+file.id ).attr("title", response._raw);
            $( '#'+file.id ).attr("id", fileName.split(".")[0]);
            $scope.bindDelEvent();
        });

        //用于图片回显
        if(picList != undefined && picList.length > 0){
            for(var i in picList){
                var fileName = picList[i].substr(picList[i].lastIndexOf("/") + 1);
                var $li = $(
                    '<div id="' + fileName.split(".")[0] + '" class="file-item thumbnail">' +
                    '<img src="' + CONFIG.CONSTANTS.IMG_URL_HKJF_ROOT + '/' + picList[i] + '" style="width: ' + thumbnailWidth + 'px;height: ' + thumbnailHeight+ 'px;">' +
                    '<div class="delete" title="' + picList[i] + '">×</div>'+
                    '</div>'
                    ),
                    $img = $li.find('img');
                // $list为容器jQuery实例
                $list.append( $li );
            }
        }
        
        $scope.bindDelEvent();
    };
    
    $scope.bindDelEvent = function(){
        $(".delete").each(function(index, obj){
            $(this).unbind("click").click(function(e){
                var picPath = e.currentTarget.title;
                var content = "<input type='hidden' name='picPath' value='" + picPath+ "'>确定删除资料?";
                commonUtil.createCustomConfirmBox("删除资料", content, CONFIG.interface.delUserAudit, null, function(data){
                    $("#modalText").modal('hide');
                    if(data.resStatus == CONFIG.CONSTANTS.SUCCESS_STATE){
                        var delId = picPath.substr(picPath.lastIndexOf("/") + 1).split(".")[0];
                        $("#" + delId).remove();
                    }else{
                        var message = Array.isArray(data.resMsg) ? data.resMsg[0] : data.resMsg;
                        $("#alertBody").html("操作失败:[" + message + "]");
                        $("#modalAlert").modal('show');
                    }
                });
            });
        });
    }
});

app.directive('onFinishRenderFilters', function ($timeout) {
    return {
        restrict: 'A',
        link: function(scope, element, attr) {
            if (scope.$last === true) {
                $timeout(function() {
                    scope.$emit('ngRepeatFinished');
                });
            }
        }
    };
});
