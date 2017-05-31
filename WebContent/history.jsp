<%@page import="java.sql.Timestamp"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ page import="java.util.*,model.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>History</title>
<link rel="stylesheet" type="text/css"
	href="./content/assets/css/history.css">
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
<body>
	<%
		List objlist = (List) request.getAttribute("objlist");
		int csum = (int) request.getAttribute("csum");
		int msum = (int) request.getAttribute("msum");
		int osum = (int) request.getAttribute("osum");
		int dsum = (int) request.getAttribute("dsum");
		int nsum = (int) request.getAttribute("nsum");
		String id = (String) request.getAttribute("id");
	%>
	<table>
		<tr>
			<td>工号</td>
			<td>正确标记数</td>
			<td>未检查标记数</td>
			<td>总标记数</td>
			<td>超期限完成标记数</td>
			<td>总检查数</td>
		</tr>
		<tr>
			<td><%=id%></td>
			<td><%=osum%></td>
			<td><%=nsum%></td>
			<td><%=msum%></td>
			<td><%=dsum%></td>
			<td><%=csum%></td>
		</tr>
	</table>
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
		</tr>
		<%
			Timestamp time = new Timestamp(System.currentTimeMillis());
			time.setNanos(0);
			for (int i = 0; i < objlist.size(); i += 1) {
				Picture picture = (Picture) objlist.get(i);
				if (picture.getPmodpeo().equals(id)
						&& ((picture.getPmodtime().toString().equals("1970-01-02 00:00:00.0")
								&& time.after(picture.getPdeadline()))
								|| picture.getPmodtime().after(picture.getPdeadline()))) {
		%>
		<tr class="special">
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
		</tr>
		<%
			} else {
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
		</tr>
		<%
			}
			}
		%>
	</table>
</body>
<script type="text/javascript" src="./content/assets/js/jquery.min.js"></script>
</html>