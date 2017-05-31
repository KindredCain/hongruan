<%@page import="java.sql.Timestamp"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ page import="java.util.*,model.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>图形标记系统</title>
<link href="./content/assets/css/main.css" rel="stylesheet"
	type="text/css">
<script language="JavaScript">
	function DateSelector(selYear, selMonth, selDay) {
		this.selYear = selYear;
		this.selMonth = selMonth;
		this.selDay = selDay;
		this.selYear.Group = this;
		this.selMonth.Group = this;
		// 给年份、月份下拉菜单添加处理onchange事件的函数 
		if (window.document.all != null) // IE 
		{
			this.selYear.attachEvent("onchange", DateSelector.Onchange);
			this.selMonth.attachEvent("onchange", DateSelector.Onchange);
		} else // Firefox 
		{
			this.selYear.addEventListener("change", DateSelector.Onchange,
					false);
			this.selMonth.addEventListener("change", DateSelector.Onchange,
					false);
		}
		if (arguments.length == 4) // 如果传入参数个数为4，最后一个参数必须为Date对象 
			this.InitSelector(arguments[3].getFullYear(), arguments[3]
					.getMonth() + 1, arguments[3].getDate());
		else if (arguments.length == 6) // 如果传入参数个数为6，最后三个参数必须为初始的年月日数值 
			this.InitSelector(arguments[3], arguments[4], arguments[5]);
		else // 默认使用当前日期 
		{
			var dt = new Date();
			this
					.InitSelector(dt.getFullYear(), dt.getMonth() + 1, dt
							.getDate());
		}
	}
	// 增加一个最大年份的属性 
	DateSelector.prototype.MinYear = 1950;
	// 增加一个最大年份的属性 
	DateSelector.prototype.MaxYear = 2050;
	// 初始化年份 
	DateSelector.prototype.InitYearSelect = function() {
		// 循环添加OPION元素到年份select对象中
		// 新建一个OPTION对象 
		var op = window.document.createElement("OPTION");
		// 设置OPTION对象的值 
		op.value = 0;
		// 设置OPTION对象的内容 
		op.innerHTML = "";
		// 添加到年份select对象 
		this.selYear.appendChild(op);
		for (var i = this.MaxYear; i >= this.MinYear; i--) {
			// 新建一个OPTION对象 
			var op = window.document.createElement("OPTION");
			// 设置OPTION对象的值 
			op.value = i;
			// 设置OPTION对象的内容 
			op.innerHTML = i;
			// 添加到年份select对象 
			this.selYear.appendChild(op);
		}
	}
	// 初始化月份 
	DateSelector.prototype.InitMonthSelect = function() {
		// 循环添加OPION元素到月份select对象中 
		for (var i = 0; i < 13; i++) {
			// 新建一个OPTION对象 
			var op = window.document.createElement("OPTION");
			// 设置OPTION对象的值 
			op.value = i;
			// 设置OPTION对象的内容 
			if (i == 0)
				op.innerHTML = "";
			else
				op.innerHTML = i;
			// 添加到月份select对象 
			this.selMonth.appendChild(op);
		}
	}
	// 根据年份与月份获取当月的天数 
	DateSelector.DaysInMonth = function(year, month) {
		var date = new Date(year, month, 0);
		return date.getDate();
	}
	// 初始化天数 
	DateSelector.prototype.InitDaySelect = function() {
		// 使用parseInt函数获取当前的年份和月份 
		var year = parseInt(this.selYear.value);
		var month = parseInt(this.selMonth.value);
		// 获取当月的天数 
		var daysInMonth = DateSelector.DaysInMonth(year, month);
		// 清空原有的选项 
		this.selDay.options.length = 0;
		// 循环添加OPION元素到天数select对象中 
		for (var i = 0; i <= daysInMonth; i++) {
			// 新建一个OPTION对象 
			var op = window.document.createElement("OPTION");
			// 设置OPTION对象的值 
			op.value = i;
			// 设置OPTION对象的内容
			if (i == 0)
				op.innerHTML = "";
			else
				op.innerHTML = i;
			// 添加到天数select对象 
			this.selDay.appendChild(op);
		}
	}
	// 处理年份和月份onchange事件的方法，它获取事件来源对象（即selYear或selMonth） 
	// 并调用它的Group对象（即DateSelector实例，请见构造函数）提供的InitDaySelect方法重新初始化天数 
	// 参数e为event对象 
	DateSelector.Onchange = function(e) {
		var selector = window.document.all != null ? e.srcElement : e.target;
		selector.Group.InitDaySelect();
	}
	// 根据参数初始化下拉菜单选项 
	DateSelector.prototype.InitSelector = function(year, month, day) {
		// 由于外部是可以调用这个方法，因此我们在这里也要将selYear和selMonth的选项清空掉 
		// 另外因为InitDaySelect方法已经有清空天数下拉菜单，因此这里就不用重复工作了 
		this.selYear.options.length = 0;
		this.selMonth.options.length = 0;
		// 初始化年、月 
		this.InitYearSelect();
		this.InitMonthSelect();
		// 设置年、月初始值 
		this.selYear.selectedIndex = 0;
		this.selMonth.selectedIndex = 0;
		// 初始化天数 
		this.InitDaySelect();
		// 设置天数初始值 
		this.selDay.selectedIndex = 0;
	}
</script>
</head>
<body class="admin">
	<script type="text/javascript" src="./content/assets/js/jquery.min.js"></script>

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
		<div>管理员</div>
	</div>


	<div id="leftMode" class="leftMode">
		<center>
			<a href="upload.jsp" target="_blank"><button id="btnUpload"
					class="btnLeftMode">上传图片</button></a>
		</center>
		<br>
		<center>
			<a href="selectpicturemod.do"><button id="btnPictureMod"
					class="btnLeftMode">任务分配</button></a>
		</center>
		<br>
		<center>
			<a href="selectworker.do"><button id="btnWorker"
					class="btnLeftMode">人员管理</button></a>
		</center>
		<br>
		<center>
			<a href="selectpicture.do"><button id="btnPicture"
					class="btnLeftMode">图片管理</button></a>
		</center>
		<br>
		<center>
			<a href="selectsum.do"><button id="btnDataCount"
					class="btnLeftMode">数据统计</button></a>
		</center>
		<br>
	</div>

	<div class="contentMode">
		<%
			List objlist = (List) request.getAttribute("objlist");
				String cname = (String) request.getAttribute("cname");
				if (cname != null) {
		%>

		<!-- 输出工人表格 -->
		<%
			if (cname.equals("worker")) {
		%>
		<form class="formSearch" action="selectworkerid.do" method="post">
			工号：<input class="text" type="text" name="id" /> <input
				class="submit" type="submit" name="submit" value="查找" />
		</form>
		<form id="workerFormSubmit" name="workerFormSubmit" class="formSubmit"
			action="modtype.do" method="post">
			<table>
				<tr>
					<td>工号</td>
					<td>姓名</td>
					<td>职位</td>
					<td>选择</td>
				</tr>
				<%
					for (int i = 0; i < objlist.size(); i += 1) {
									Worker worker = (Worker) objlist.get(i);
				%>
				<tr>
					<td><%=worker.getId()%></td>
					<td><%=worker.getWname()%></td>
					<td><%=worker.getType()%></td>
					<td><center>
							<input type="radio" name="id" value=<%=worker.getId()%>></input>
						</center></td>
				</tr>
				<%
					}
				%>
			</table>
			<div id="workerActionBoard" class="actionBoard">
				<p>
					职位<select name="type">
						<option value="0" selected></option>
						<option value="admin">管理员</option>
						<option value="mod">标记者</option>
						<option value="check">检查者</option>
					</select>
				</p>
				<input id="btnView" class="submit" type="submit" name="submit"
					value="查看" /> <input class="submit" type="submit" name="submit"
					value="确定" />
			</div>
		</form>
		<%
			}
		%>
		<!-- 输出工人表格结束 -->

		<!-- 输出图片统计表格 -->
		<%
			if (cname.equals("picture")) {
		%>
		<form class="formSearch" id="formSearch" action="selectpictureask.do"
			method="post">
			<p>
				最终期限： 年 <select name="year" id="year" form="formSearch"></select>-月
				<select name="month" id="month" form="formSearch"></select>-日 <select
					name="day" id="day" form="formSearch"></select> 标签： <select
					name="lname">
					<option value="0"></option>
					<%
						List labellist = (List) request.getAttribute("labellist");
									String lname = (String) request.getAttribute("lname");
									for (int i = 0; i < labellist.size(); i += 1) {
										Label label = (Label) labellist.get(i);
					%>
					<%
						if (lname != null && lname.equals(label.getLname())) {
					%>
					<option value="<%=label.getLname()%>" selected><%=label.getLname()%></option>
					<%
						} else {
					%>
					<option value="<%=label.getLname()%>"><%=label.getLname()%></option>
					<%
						}
									}
					%>
				</select> <input class="submit" type="submit" name="submit" value="查找" />
			</p>
			<input type="button" name="btnAllSelect" class="btnAllSelect"
				value="全选"></input> <input type="button" name="btnAllNoSelect"
				class="btnAllNoSelect" value="重选"></input> <input type="button"
				name="btnReSelect" class="btnReSelect" value="反选"></input>
			<script type="text/javascript">
				var selYear = window.document.getElementById("year");
				var selMonth = window.document.getElementById("month");
				var selDay = window.document.getElementById("day");
				new DateSelector(selYear, selMonth, selDay);
			</script>
		</form>
		<form id="pictureFormSubmit" name="pictureFormSubmit"
			class="formSubmit" action="addlabel.do" method="post">
			<table>
				<tr>
					<td>图片名字</td>
					<td>上传时间</td>
					<td>标记者</td>
					<td>标记时间</td>
					<td>检查者</td>
					<td>检查时间</td>
					<td>标记正确</td>
					<td>最终时间</td>
					<td>选择</td>
				</tr>
				<%
					for (int i = 0; i < objlist.size(); i += 1) {
									Picture picture = (Picture) objlist.get(i);
				%>
				<tr>
					<td><%=picture.getPname()%></td>
					<td>
						<%
							if (picture.getPuptime().toString().equals("1970-01-02 00:00:00.0")) {
						%> -- <%
							} else {
						%> <%=picture.getPuptime()%> <%
 	}
 %>
					</td>
					<td><%=picture.getPmodpeo()%></td>
					<td>
						<%
							if (picture.getPmodtime().toString().equals("1970-01-02 00:00:00.0")) {
						%> -- <%
							} else {
						%> <%=picture.getPmodtime()%> <%
 	}
 %>
					</td>
					<td><%=picture.getPcheckpeo()%></td>
					<td>
						<%
							if (picture.getPchecktime().toString().equals("1970-01-02 00:00:00.0")) {
						%> -- <%
							} else {
						%> <%=picture.getPchecktime()%> <%
 	}
 %>
					</td>
					<td><%=picture.getPok()%></td>
					<td>
						<%
							if (picture.getPdeadline().toString().equals("1970-01-02 00:00:00.0")) {
						%> -- <%
							} else {
						%> <%=picture.getPdeadline()%> <%
 	}
 %>
					</td>
					<td><center>
							<input type="checkbox" name="pid" value=<%=picture.getPid()%>></input>
						</center></td>
				</tr>
				<%
					}
				%>
			</table>
			<div id="pictureActionBoard" class="actionBoard">
				<p>
					添加标签 <input class="text" type="text" name="lname"></input>
				</p>
				<%
					if (lname != null) {
				%>
				<input type="hidden" name="dlname" value="<%=lname%>" /> <input
					id="deleteLabelSubmit" class="submit" type="submit" name="submit"
					value="删除" />
				<%
					}
				%>
				<input class="submit" type="submit" name="submit" value="添加" />
			</div>
		</form>
		<%
			}
		%>
		<!-- 输出图片统计表格结束 -->

		<!-- 输出图片任务分配表格 -->
		<%
			if (cname.equals("picturemod")) {
		%>
		<form class="formSearch" id="formSearch"
			action="selectpicturemodask.do" method="post">
			<p>
				上传日期： 年 <select name="year" id="year" form="formSearch"></select>-月
				<select name="month" id="month" form="formSearch"></select>-日 <select
					name="day" id="day" form="formSearch"></select> 标签： <select
					name="lname">
					<option value="0"></option>
					<%
						List labellist = (List) request.getAttribute("labellist");
									String lname = (String) request.getAttribute("lname");
									for (int i = 0; i < labellist.size(); i += 1) {
										Label label = (Label) labellist.get(i);
					%>
					<%
						if (lname != null && lname.equals(label.getLname())) {
					%>
					<option value="<%=label.getLname()%>" selected><%=label.getLname()%></option>
					<%
						} else {
					%>
					<option value="<%=label.getLname()%>"><%=label.getLname()%></option>
					<%
						}
									}
					%>
				</select> <input class="submit" type="submit" name="submit" value="查找" />
			</p>
			<input type="button" name="btnAllSelect" class="btnAllSelect"
				value="全选"></input> <input type="button" name="btnAllNoSelect"
				class="btnAllNoSelect" value="重选"></input> <input type="button"
				name="btnReSelect" class="btnReSelect" value="反选"></input>
			<script type="text/javascript">
				var selYear = window.document.getElementById("year");
				var selMonth = window.document.getElementById("month");
				var selDay = window.document.getElementById("day");
				new DateSelector(selYear, selMonth, selDay);
			</script>
		</form>
		<form id="formSubmit" name="formSubmit" class="formSubmit"
			action="moddead.do" method="post">

			<table>
				<tr>
					<td>图片名字</td>
					<td>上传时间</td>
					<td>选择</td>
				</tr>
				<%
					for (int i = 0; i < objlist.size(); i += 1) {
									Picture picture = (Picture) objlist.get(i);
				%>
				<tr>
					<td><%=picture.getPname()%></td>
					<td><%=picture.getPuptime()%></td>
					<td><center>
							<input type="checkbox" name="pid" value=<%=picture.getPid()%>></input>
						</center></td>
				</tr>
				<%
					}
				%>
			</table>
			<div id="picturemodActionBoard" class="actionBoard">
				<p>
					年<select name="year" id="yearSub" form="formSubmit"></select>
				</p>
				<p>
					月 <select name="month" id="monthSub" form="formSubmit"></select>
				</p>
				<p>
					日 <select name="day" id="daySub" form="formSubmit"></select>
				</p>
				<p>
					标记者<select name="pmodpeo">
						<%
							List workerlist = (List) request.getAttribute("workerlist");
										for (int i = 0; i < workerlist.size(); i += 1) {
											Worker worker = (Worker) workerlist.get(i);
						%>
						<option value="<%=worker.getId()%>"><%=worker.getId()%></option>
						<%
							}
						%>
					</select>
				</p>
				<input class="submit" type="submit" name="submit" value="确定" />
				<script type="text/javascript">
					var selYear = window.document.getElementById("yearSub");
					var selMonth = window.document.getElementById("monthSub");
					var selDay = window.document.getElementById("daySub");
					new DateSelector(selYear, selMonth, selDay);
				</script>
			</div>
		</form>
		<%
			}
		%>
		<!-- 输出图片任务分配表格结束 -->

		<!-- 输出总统计表格 -->
		<%
			if (cname.equals("sum")) {
		%>
		<form class="formSearch" id="formSearch" action="selectsumask.do"
			method="post">
			<p>
				最终期限： 年 <select name="year" id="year" form="formSearch"></select>-月
				<select name="month" id="month" form="formSearch"></select>-日 <select
					name="day" id="day" form="formSearch"></select> 标签： <select
					name="lname">
					<option value="0"></option>
					<%
						List labellist = (List) request.getAttribute("labellist");
									String lname = (String) request.getAttribute("lname");
									for (int i = 0; i < labellist.size(); i += 1) {
										Label label = (Label) labellist.get(i);
					%>
					<%
						if (lname != null && lname.equals(label.getLname())) {
					%>
					<option value="<%=label.getLname()%>" selected><%=label.getLname()%></option>
					<%
						} else {
					%>
					<option value="<%=label.getLname()%>"><%=label.getLname()%></option>
					<%
						}
									}
					%>
				</select> <input class="submit" type="submit" name="submit" value="查找" />
			</p>
			<script type="text/javascript">
				var selYear = window.document.getElementById("year");
				var selMonth = window.document.getElementById("month");
				var selDay = window.document.getElementById("day");
				new DateSelector(selYear, selMonth, selDay);
			</script>
		</form>
		<table>
			<tr>
				<td>上传图片数</td>
				<td>标记图片数</td>
				<td>检查图片数</td>
			</tr>
			<%
				int sum = (int) request.getAttribute("sum");
							int msum = (int) request.getAttribute("msum");
							int csum = (int) request.getAttribute("csum");
			%>
			<tr>
				<td><%=sum%></td>
				<td><%=msum%></td>
				<td><%=csum%></td>
			</tr>
		</table>
		<%
			}
		%>
		<!-- 输出总统计表格结束 -->

		<%
			}
		%>
	</div>
	<%
		}
	%>
	<script type="text/javascript" src="./content/assets/js/admin.js"></script>
</body>
</html>