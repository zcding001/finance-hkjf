/**
 * Created by dzc on 17.12.27.
 */

var userInfoController = {};

(function(){
    'use strict';
    var _userInfoForm = '';
    /**
     * load user info
     */
    userInfoController.loadUserInfo = function(){
        _loadUserData();
        //按钮绑定事件
        $("#preUpdate").click(function(){//修改事件
            // commonUtil.addFocusBlurEvent();
            _showModifyUserInfo();
            _userInfoForm = validFormUtil.initForm("/userController/updateUserInfo.do", function(data){
                if(data.resStatus == CONSTANTS.SUCCESS){
                    _loadUserData();
                }
            }, "", "", "", "", "", 4);
            $.Tipmsg.p = "";
        });
        $("#cancelUpdate").click(_showUserInfo);
        $("#submitBtn").click(function(){
            _userInfoForm.submitForm();
        });
    };

    /**
     * 加载账户管理-安全中心
     */
    userInfoController.loadSecurityInfo = function(){
        ajaxUtil.post("/userController/preUpdateUserInfo.do", {}, function(data){
            var regUser = data.params.regUser;
            delete regUser.lastLoginTime;
            var regUserDetail = data.params.regUserDetail;
            var regUserInfo = data.params.regUserInfo;
            if(regUser.identify == 0){ //显示实名页面
                $(".infoDiv8").show();
                validFormUtil.initForm("/userController/realName.do", function(data){
                    if(data.resStatus == CONSTANTS.SUCCESS){
                        $(".infoDiv8").hide();
                        userInfoController.loadSecurityInfo();
                    }else{
                        dialogUtil.alert("实名验证失败", data.resMsg);
                    }
                }, "", "realNameForm", "realNameBtn", "", "", 4).tipmsg.p='';
            }else{
                $(".infoDiv-1").show();
                if(regUser.nickName != "" && regUser.nickName.length > 0){
                    regUser["nickNameView"] = regUser.nickName;
                }else{
                    regUser["nickNameView"] = "请输入昵称";
                }
                regUser["identifyView"] = dictionaryUtil.getName("user", "identify", regUser.identify);
                regUserInfo["emailStateView"] = dictionaryUtil.getName("user", "email_state", regUserInfo.emailState);
                //显示邮箱绑定状态
                if(regUserInfo.emailState == 0){
                    $(".emailStateSpan").removeClass("colr-org").addClass("colr-red");
                    $(".emailStateLi").removeClass("icon-zhengque").addClass("icon-jinggao icon_jg");
                    $("#bindEmailBtn").show();
                }
                //显示密码强度
                var pwdLevel = containerUtil.get(CONSTANTS.PWD_LEVEL);
                if(pwdLevel == 1){
                    commonUtil.displayElement(["safetyFactor1"], "", ["safetyFactor2", "safetyFactor3"]);
                }else if(pwdLevel == 3){
                    commonUtil.displayElement(["safetyFactor3"], "", ["safetyFactor1", "safetyFactor2"]);
                }else{
                    commonUtil.displayElement(["safetyFactor2"], "", ["safetyFactor1", "safetyFactor3"]);
                }
                //渲染页面中的元素
                renderUtil.renderElement([regUser, regUserInfo, regUserDetail], "updateNickForm, #emergencyForm, #updateInfoForm");

                //更新昵称
                $("#preUpdateBtn0").click(function(){
                    commonUtil.displayElement([], ["infoDiv0"], ["preUpdateBtn0"], ["infoDiv"]);
                    validFormUtil.initForm("/userController/updateNicName.do", function(){
                    	var o = {};
                    	o.nickName = $("input[name=nickName]").val();
                    	o.nickNameView = o.nickName;
                    	renderUtil.renderElement(o);
                        var cacheUser = containerUtil.get(CONSTANTS.LOGIN_REG_USER);
                        cacheUser.nickName = o.nickName;
                        containerUtil.set(CONSTANTS.LOGIN_REG_USER, cacheUser);
                        commonUtil.displayElement([], ["infoDiv0"], ["preUpdateBtn0"], ["infoDiv"], 1);
                    }, function(){
                        var nickName = $("input[name=nickName]").val();
                        if(nickName == $("#nickNameTmp").text()){
                            commonUtil.displayElement([], ["infoDiv0"], ["preUpdateBtn0"], ["infoDiv"], 1);
                            return false;
                        }
                        return true;
                    }, "updateNickForm", "saveBtn0", "", "", 4).tipmsg.p='';
                });
                //此处逻辑是由我的账户中绑定邮箱直接跳转到此处
                var bandObj = containerUtil.getTransient();
                if(bandObj != undefined && bandObj.bandEmail != undefined && bandObj.bandEmail != null && bandObj.bandEmail == 1){
                    _doBandEmail(regUserInfo)
                }
                //绑定邮箱
                $("#bindEmailBtn").click(function(){
                    _doBandEmail(regUserInfo)
                });

                //绑定修改密码
                $("#updatePwdBtn").click(function(){
                    commonUtil.addFocusBlurEvent("validInfoForm");
                    $("#saveBtn3").text("保存");
                    commonUtil.displayElement("", ["infoDiv5", "infoDiv7"], "", ["infoDiv-1"]);
                    var form = validFormUtil.initForm("/userController/resetPwd.do", function(data){
                        if(data.resStatus == CONSTANTS.SUCCESS){
                            commonUtil.displayElement("", ["infoDiv5", "infoDiv7"], "", ["infoDiv-1"], 1);
                        }else{
                            dialogUtil.alert("更新密码", data.resMsg);
                        }
                    }, function(){
                        $("#oldPasswd").val(rsaUtil.encryptData($("#oldPasswd1").val()));
                        $("#passwd").val(rsaUtil.encryptData($("#pwd1").val()));
                        $("#rePasswd").val(rsaUtil.encryptData($("#pwd2").val()));
                        commonUtil.pwdStrong($("#pwd1").val());
                        $("#pwd1").val("");
                        $("#pwd2").val("");
                        return true;
                    }, "validInfoForm", "saveBtn3", "", "", 4);
                    form.tipmsg.p = '';
                    form.ignore($("#emailId"));
                });

                $("#cancelUpdate3").click(function(){
                    commonUtil.displayElement("", ["infoDiv5", "infoDiv6", "infoDiv7"], "", ["infoDiv-1"], 1);
                });

                //更新紧急联系人
                $("#preUpdateBtn1").click(function(){
                    commonUtil.displayElement(["preUpdateBtn1"], ["infoDiv2"], ["saveBtn1"], ["infoDiv1"], 1);
                    validFormUtil.initForm("/userController/updateUserInfo.do", function(){
                        renderUtil.renderElement(validFormUtil.getFormData(["emergencyTel", "emergencyContact"]), "emergencyForm");
                        commonUtil.displayElement(["preUpdateBtn1"], ["infoDiv2"], ["saveBtn1"], ["infoDiv1"]);
                    }, "", "emergencyForm", "saveBtn1", "", "", 4).tipmsg.p='';
                });

                //更新地址信息
                $("#preUpdateBtn2").click(function(){
                    commonUtil.displayElement(["saveBtn2"], ["infoDiv3"], ["preUpdateBtn2"], ["infoDiv4"]);
                    validFormUtil.initForm("/userController/updateUserInfo.do", function(){
                        renderUtil.renderElement(validFormUtil.getFormData(["contactAddress", "postCode"]), "emergencyForm");
                        commonUtil.displayElement(["saveBtn2"], ["infoDiv3"], ["preUpdateBtn2"], ["infoDiv4"], 1);
                    }, "", "updateInfoForm", "saveBtn2", "", "", 4).tipmsg.p = '';
                });
            }
        });
    };

    /**
     * 显示用户信息的div
     * @private
     */
    function _showUserInfo(){
        $("#preUpdate").show();
        $(".userInfoDiv").show();
        $("#submitBtn").hide();
        $("#cancelUpdate").hide();
        $(".modifiedDiv").hide();
    }

    function _showModifyUserInfo(){
        $("#preUpdate").hide();
        $(".userInfoDiv").hide();
        $("#submitBtn").show();
        $("#cancelUpdate").show();
        $(".modifiedDiv").show();
    }

    /**
     *  加载用户人基本信息
     * @private
     */
    function _loadUserData(){
        ajaxUtil.post("/userController/preUpdateUserInfo.do", {}, function(data){
            var regUser = data.params.regUser;
            delete regUser.lastLoginTime;
            var regUserDetail = data.params.regUserDetail;
            var regUserInfo = data.params.regUserInfo;
            var sex = regUserInfo.sex;
            if(sex != 0){
                regUserInfo["sexView"] = sex == 1 ? "男" : "女";
            }else{
                regUserInfo["sexView"] = '';
            }
            if(regUserInfo.birthday > 0){
                regUserInfo["birthday"] = dateUtil.date(regUserInfo.birthday, "");
            }else{
                regUserInfo["birthday"] = '';
            }
            regUserInfo["address"] = dictionaryUtil.getAreaName(regUserInfo.birthProvince) + " " +
                dictionaryUtil.getAreaName(regUserInfo.birthCity)+ " " + dictionaryUtil.getAreaName(regUserInfo.birthCountry);
            dictionaryUtil.getAreaData()
            regUserInfo["workIncomeView"] = dictionaryUtil.getName("user", "work_income", regUserInfo.workIncome);
            //初始化省市县、及更新操作
            dictionaryUtil.initAreaSelect("birthProvince", "birthCity", "birthCountry", regUserInfo.birthProvince, regUserInfo.birthCity);

            renderUtil.renderElement([regUser, regUserDetail, regUserInfo], 'dataForm');
            _showUserInfo();
        });
    }

    function _doBandEmail(regUserInfo){
        $("#emailId").val(regUserInfo.email);
        $("#saveBtn3").text("验证");
        commonUtil.displayElement("", ["infoDiv5", "infoDiv6"], "", ["infoDiv-1"]);
        commonUtil.addFocusBlurEvent("validInfoForm");
        var form = validFormUtil.initForm("/userController/sendVerifyEmail.do", function(data){
            if(data.resStatus == CONSTANTS.SUCCESS){
                dialogUtil.confirm("title", data.resMsg, function(){
                    userInfoController.loadSecurityInfo();
                });
            }else{
                dialogUtil.alert("验证邮箱", data.resMsg);
            }
        }, "", "validInfoForm", "saveBtn3", "", "", 4);
        form.tipmsg.p = '';
        form.ignore($("#oldPasswd1, #pwd1, #pwd2"));
    }
})();
