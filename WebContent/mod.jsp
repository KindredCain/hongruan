<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ page import="java.util.*,model.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>图形标记系统</title>
<meta http-equiv="content-type" content="text/html;charset=utf-8">
<link href="./content/assets/css/main.css" rel="stylesheet"
	type="text/css" />
<link rel="stylesheet" type="text/css"
	href="./content/assets/css/content.css" />
</head>

<body>
	<%
		String id = (String) session.getAttribute("ID");
		if (id == null)
			out.println("<meta http-equiv=\"refresh\" content=\"0; url=login.jsp\" />");
		else {
	%>
	<!-- Header部分 -->
	<div id="header">
		<a href="out.do"><input id="btnLoginOut" class="btnLoginOut"
			type="image" name="btnLoginOut" src="./image/switch.png"></input></a>
		<p><%=id%></p>
		<div id="workerType">标记员</div>
	</div>

	<!-- content 部分-->
	<%
		int ok = (int) request.getAttribute("ok");
			int sum = (int) request.getAttribute("sum");
			Picture picture = (Picture) request.getAttribute("picture");
	%>
	<div id="content">
		<!-- Content_left 部分-->
		<div id="content_left">
			<!-- 任务 -->
			<p class="title">任务</p>
			<p style="color: skyblue"><%=ok%>/<%=sum%></p>
			<!-- 截止日期 -->
			<p class="title">截止日期</p>
			<p style="color: red">
				<%
					if (picture != null) {
				%>
				<%=picture.getPdeadline()%>
				<%
					}
				%>
			</p>
			<!-- 上一张 -->
			<input class="imageBtnPrevious" id="imageBtnPrevious" type="button"
				name="imageBtnPrevious" value="< Prev." />
			<!-- 历史纪录 -->
			<a href="selecthistory.do" target="_blank"><input class="imageBtnHistory" id="imageBtnHistory" type="button"
				name="imageBtnHistory" value="History" /></a>
		</div>
		<div id="content_right">
			<!-- 图片信息 -->
			<p class="title">图片名</p>
			<p style="color: skyblue">
				<%
					if (picture != null) {
				%>
				<%=picture.getPname()%>
				<%
					}
				%>
			</p>
			<!-- 标记数 -->
			<p class="title">标记数</p>
			<p id="signNum" style="color: red"></p>
			<!-- 下一张 -->
			<input class="imageBtnNext" id="imageBtnNext" type="button"
				name="imageBtnNext" value="Next >" />
		</div>
		<%
			if (picture != null) {
		%>
		<img id="image" src="<%=picture.getPadd()%>"></img>
		<%
			}else{
		%>
		<img id="image" src=""></img>
		<%} %>
	</div>

	<form id="formTest" method="post" action="addtagmod.do"
		style="display: none;">
		<input type="checkbox" name="xl" value='0' checked></input> <input
			type="checkbox" name="yl" value='0' checked></input> <input
			type="checkbox" name="xr" value='0' checked></input> <input
			type="checkbox" name="yr" value='0' checked></input>
		<%
			if (picture != null) {
		%>
		<input type="text" name="pid" value="<%=picture.getPid()%>"></input>
		<%
			}
		%>
		<input type="submit" name="submit"></input>
	</form>
	<%
		}
	%>
</body>

<script type="text/javascript" src="./content/assets/js/jquery.min.js"></script>
<script type="text/javascript" src="./content/assets/js/content.js"></script>
<script type="text/javascript" src="./content/assets/js/index.js"></script>

</html>