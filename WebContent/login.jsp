<%@ page contentType="text/html; charset=utf-8"%>
<%request.setCharacterEncoding("UTF-8");%>
<% String LOGIN_URL = "http://11.10.51.103:8080/geologicalDisasterWarning/login.jsp";
   String CAS_LOGIN = "http://11.10.51.68:8188/DMGeoSSO/remoteLogin";
   String CAS_LOGOUT = "http://11.10.51.68:8188/DMGeoSSO/logout";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>login</title>
<script type="text/javascript">
function do_login()
{
	if (document.form1.username.value == ""){
		alert("用户名称不能为空！");
		document.form1.username.focus();
		return false;
	}
	if (document.form1.password.value == ""){
		alert("密码不能为空！");
		document.form1.password.focus();
		return false;
	}
	document.form1.submit();
	return true;
}
</script>
<script>
	function getParam(name) {
        var queryString = window.location.search;
        var param = queryString.substr(1, queryString.length - 1).split("&");
        for (var i = 0; i < param.length; i++) {
            var keyValue = param[i].split("=");
            if (keyValue[0] == name) return keyValue[1];
        }
        return null;
    }
    function init() {
      // 显示异常信息
        var error = getParam("errorMessage");
        if (error) {
            alert(decodeURIComponent(error));
        }
        // 注入service
        var service = getParam("service");
        if (service)
            document.getElementById("service").value = decodeURIComponent(service);
        else
            document.getElementById("service").value = location.href;
    }
</script>
</head>
<body onkeypress="if (event.keyCode == 13) { do_login(); }">
<div>
<div>
<%if (request.getRemoteUser() == null){%>
<form name="form1" action="<%=CAS_LOGIN %>?submit=true" method="post" >
<input type="hidden" id="service" name="service" value="" />
<input type="hidden" name="loginUrl" value="<%=LOGIN_URL %>" />
<table cellspacing="0" cellpadding="0">
  <tr>
    <td width="70" height="40"><div align="right">用户名：</div></td>
    <td><div align="left">
      <input type="text" name="username" />
    </div></td>
  </tr>
  <tr>
    <td height="40"><div align="right">密　码：</div></td>
    <td><div align="left">
      <input type="password" name="password" />
    </div></td>
  </tr>
  <tr>
    <td colspan="2"><input type="button" onFocus="this.blur()" onclick="do_login()" value="登录" /></td>
  </tr>
</table>
</form>
<script type="text/javascript">init(); document.form1.username.focus();</script>
<%}else{
response.sendRedirect(response.encodeURL("/geologicalDisasterWarning/timechange.jsp"));
}%>
</div>
</div>
</body>
</html>