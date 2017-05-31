<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>login</title>
<link href="./content/assets/css/main.css" rel="stylesheet" type="text/css">
</head>
<body class = "bodyLogin">
<script type="text/javascript" src="./content/assets/js/jquery.min.js"></script>
<center><div class = "divLogin">图形标记系统</div></center>

<center><div class="divFormLogin">
	<form id="formLogin" action="login.do" method="post">
	  	用户　<input class="inputLogin firstChild" type="text" name="id"></input><br>  
	  	密码　<input class="inputLogin" type="password" name="pwd"></input>
		<input type="submit" name="submit" value="登陆" style="display: none;"></input>
	</form>
	<button id="btnLogin">Login</button>
</div></center>

<script type="text/javascript">
	document.getElementById("btnLogin").onmousedown = function(e) {
		if(e.which == 1){
			$("input[name='submit']").click();
		}
	}
</script>
<%
   String errmsg=(String)session.getAttribute("errormsg");
   if(errmsg!=null){
     session.removeAttribute("errormsg");
     out.println(errmsg);
   }
%>
</body>
</html>