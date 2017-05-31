<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ page import="java.util.*,model.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>图片上传</title>
    <link rel="stylesheet" type="text/css" href="./content/assets/css/upload.css" />
</head>
<body>
	<center>
		<form action="uppicture.do" enctype="multipart/form-data" method="post">
		    <div class='upload_box'>
		    	<div class = "btnDiv">
			    	<input type="button" name="btnUpimg" id="btnUpimg" value="选择图片" />
			    	<input type="button" name="btnSubmit" id="btnSubmit" value="上传图片" />
			    	<p id="imageNum">0张图片</p>
		    	</div>

		    	<input type="file" name="upimg" id='upimg' multiple />
		    	<input type="submit" name="submit" id='submit' />
		        <span id="drag">拖拽区域</span>
		        <div class='show' id="show"></div>
		    </div>
	    </form>
    </center>
</body>
<script type="text/javascript" src="./content/assets/js/jquery.min.js"></script>
<script type="text/javascript" src="./content/assets/js/upload.js"></script>
</html>