/**
 * Created by dzc on 17.11.24.
 * 公共js合集
 */
// 此版本号后期可剔除
var version = 201711301;

document.write('<script language="javascript" src="${project_base_path}/lib/jquery/jquery.min.js" > <\/script>');
document.write('<script language="javascript" src="${project_base_path}/lib/bootstrap/js/bootstrap.min.js" > <\/script>');
// // custom js util
document.write('<script language="javascript" src="${project_base_path}/lib/constants.js?_v=' + version + '" > <\/script>');
document.write('<script language="javascript" src="${project_base_path}/lib/utils/pageUtil.js?_v=' + version + '" > <\/script>');
document.write('<script language="javascript" src="${project_base_path}/lib/utils/cookieUtil.js?_v=' + version + '" > <\/script>');
document.write('<script language="javascript" src="${project_base_path}/lib/utils/extendUtil.js?_v=' + version + '" > <\/script>');
document.write('<script language="javascript" src="${project_base_path}/lib/utils/containerUtil.js?_v=' + version + '" > <\/script>');
document.write('<script language="javascript" src="${project_base_path}/lib/utils/dateUtil.js?_v=' + version + '" > <\/script>');
document.write('<script language="javascript" src="${project_base_path}/lib/utils/ssoUtil.js?_v=' + version + '" > <\/script>');
document.write('<script language="javascript" src="${project_base_path}/lib/utils/validUtil.js?_v=' + version + '" > <\/script>');
document.write('<script language="javascript" src="${project_base_path}/lib/utils/searchChooseUtil.js?_v=' + version + '" > <\/script>');
document.write('<script language="javascript" src="${project_base_path}/lib/utils/renderUtil.js?_v=' + version + '" > <\/script>');
document.write('<script language="javascript" src="${project_base_path}/lib/utils/dialogUtil.js?_v=' + version + '"> <\/script>');
document.write('<script language="javascript" src="${project_base_path}/lib/utils/ajaxUtil.js?_v=' + version + '" > <\/script>');
document.write('<script language="javascript" src="${project_base_path}/lib/utils/securityUtil.js?_v=' + version + '" > <\/script>');
//含有$(function(){})加载及运行的工具类尽量放在后面
document.write('<script language="javascript" src="${project_base_path}/lib/utils/commonUtil.js?_v=' + version + '" > <\/script>');
document.write('<script language="javascript" src="${project_base_path}/lib/utils/dictionaryUtil.js?_v=' + version + '" > <\/script>');
document.write('<script language="javascript" src="${project_base_path}/lib/utils/moneyUtil.js?_v=' + version + '" > <\/script>');
// document.write('<script language="javascript" src="../lib/validform/js/Validform_v5.3.2_min.js?_v=' + version + '" > <\/script>');
// document.write('<script language="javascript" src="../lib/validform/js/Validform_Datatype.js?_v=' + version + '" > <\/script>');