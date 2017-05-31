//----------------------------------加载----------------------------------//
//禁止默认右键菜单
document.oncontextmenu = function (){
	return false;
}
//取消图片拖拽
document.ondragstart=function() {return false;}
//取消文本选择
document.onselectstart=function(){return false;}
//获取localStorage中的Signs
document.getElementById("image").onload = function(){
	try{
		if($("#workerType").attr("name") == "check"){
	    	getSignFromServer();
	    }
		getSignFromLocalStorage();
	}catch(e){
		alert(e);
	}
}
// --------------------------------相关变量-----------------------------------//
var windowWidth = window.innerWidth;								//屏幕宽
var windowHeight = window.innerHeight;								//屏幕高
var headerHeight = document.getElementById("header").offsetHeight;	//header高

//模块生成
try{
	var divContent = document.getElementById("content");
	var divContentLeft = document.getElementById("content_left");
	var divContentRight = document.getElementById("content_right");
	var image = document.getElementById("image");
	var signNum = document.getElementById("signNum");
	signNum.innerText = $("div.retc").length;

	//右键菜单
	var rightMouseListMenu = document.createElement("ul");
	rightMouseListMenu.className = "rightMouseListMenu";
	rightMouseListMenu.id = "rightMouseListMenu";
	document.body.appendChild(rightMouseListMenu);
	var rightMouseList_1 = document.createElement("li");
	rightMouseList_1.className = "rightMouseList";
	rightMouseList_1.innerText = "删除";
	rightMouseListMenu.appendChild(rightMouseList_1);
}catch(e){
	// alert("e");
}

//-------------------------模块尺寸与加载----------------------------------------//
function contentSize(){
	try{
		//content 模块位置与大小
		$("#content").css({
			"top":$("#header").css("height"),
			"width":"100%",
			"height": (window.innerHeight - parseInt($("#header").css("height")) + 1) + "px",
		});

		//image 部分位置与大小
		$("#image").css({
			"maxWidth": (window.innerWidth - parseInt($("#content_left").css("width")) * 2 - 2) + "px",
			"maxHeight": "100%",
			// "left": "10%",
		});

		//历史纪录 左边距
		$("#imageBtnHistory").css({"left":(parseInt($("#content_left").css("width")) - parseInt($("#imageBtnHistory").css("width"))) / 2 + "px"});
		//上一页 宽度
		imageBtnPrevious.style.width = $("#content_left").css("width");
		//下一页 宽度
		imageBtnNext.style.width = $("#content_right").css("width");
		//登出按钮样式
		document.getElementById("btnLoginOut").onmouseover = function (e) {
			document.getElementById(this.id).src = "./image/switch_hover.png";
		}
		document.getElementById("btnLoginOut").onmouseout = function (e) {
			document.getElementById(this.id).src = "./image/switch.png";
		}

	}catch(e){
		alert(e);
	}
}
contentSize();


//----------------------功能函数------------------------------//
//屏幕大小监听
$(window).resize(function(){
	contentSize();
	$("div.retc").remove();
	index = 1;
    if($("#workerType").attr("name") == "check"){
    	getSignFromServer();
    }
	getSignFromLocalStorage();
});
