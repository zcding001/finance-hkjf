/**
 * Created by dzc on 17.11.23.
 * RSA 加密工具类
 */

var rsaUtil = {};
(function(){
    /**
     * Rsa加密数据
     * @param data
     */
    rsaUtil.encryptData = function(data){
        var publicKey = "";
        $.ajax({
            type: 'POST',
            url: CONSTANTS.BASE_PATH + "/indexController/getPulibKey.do",
            dataType: "json",
            async: false,
            success: function(data){
                if(data.resStatus == CONSTANTS.SUCCESS){
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