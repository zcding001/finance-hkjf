/**
 * Created by dzc on 17.11.23.
 * RSA 加密工具类
 */

var rsaUtil = {};
(function(){
    //获得公钥的地址
    var _PUBLIC_KEY_URL = "managementLoginController/getPulibKey";
    
    /**
     * Rsa加密数据
     * @param data
     */
    rsaUtil.encryptData = function(data){
        var publicKey = "";
        $.ajax({
            type: 'POST',
            url: _PUBLIC_KEY_URL,
            dataType: "json",
            async: false,
            success: function(data){
                if(data.resStatus == CONFIG.CONSTANTS.SUCCESS_STATE){
                    publicKey = data.params.publicKey;
                }
            }
        });
        //实例化加密对象
        var encrypt = new JSEncrypt();
        //设置加密公钥
        encrypt.setPublicKey(publicKey);
        //进行加密操作
        return encrypt.encrypt(data);
    };

})();