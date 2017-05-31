//--------------------------------标记------------------------------//
var wId = "w";																					//虚线框id前缀
var sId = "s";																					//实线框id前缀
var index = 1;																					//标记框id数值
var startX = 0, startY = 0;																		//坐标
var flag = false;																				//画框标记
var retcLeft = "0px", retcTop = "0px", retcHeight = "0px", retcWidth = "0px";					//框长宽、外边距

//标记，点击图片内响应
document.getElementById("image").onmousedown = function(e){
	if(e.which == 1){
		flag = true;
		try{
			var evt = window.event || e;
			var scrollTop = document.body.scrollTop || document.documentElement.scrollTop;
			var scrollLeft = document.body.scrollLeft || document.documentElement.scrollLeft;
			startX = evt.clientX + scrollLeft;
			startY = evt.clientY + scrollTop;
			var div = document.createElement("div");
			div.id = wId + index;
			div.className = "div";
			div.style.marginLeft = startX + "px";
			div.style.marginTop = startY + "px";
			document.body.appendChild(div);
		}catch(e){
			//alert(e);
		}
	}
}

//移动，改变标记长宽
document.onmousemove = function(e){
	if(flag){
		try{
			var evt = window.event || e;
			var scrollTop = document.body.scrollTop || document.documentElement.scrollTop;
			var scrollLeft = document.body.scrollLeft || document.documentElement.scrollLeft;
			retcLeft = (startX - evt.clientX - scrollLeft > 0 ? evt.clientX + scrollLeft : startX) + "px";
			retcTop = (startY - evt.clientY - scrollTop > 0 ? evt.clientY + scrollTop : startY) + "px";
			retcHeight = Math.abs(startY - evt.clientY - scrollTop) + "px";
			retcWidth = Math.abs(startX - evt.clientX - scrollLeft) + "px";
			findId(wId + index).style.marginLeft = retcLeft;
			findId(wId + index).style.marginTop = retcTop;
			findId(wId + index).style.width = retcWidth;
			findId(wId + index).style.height = retcHeight;
		}catch(e){
		    //alert(e);
	   	} 
  	}
}
//松开左键，生成标记
document.onmouseup = function(){
	if((retcWidth!='0px')&&(retcHeight!='0px')){
	    try{
			if(parseFloat(retcTop)<parseFloat($("#header").css("height"))){
				retcHeight = parseFloat(retcHeight) - (parseFloat($("#header").css("height")) - parseFloat(retcTop)) + "px";
				retcTop = parseFloat($("#header").css("height")) + "px";
			}
			if((parseFloat(retcTop)+parseFloat(retcHeight))>
					(parseFloat($("#header").css("height"))
					+parseFloat($("#image").css("height")))){

				retcHeight = parseFloat($("#header").css("height"))
							+parseFloat($("#image").css("height"))
							-parseFloat(retcTop)- 4 + "px";
			}
			if(parseFloat(retcLeft)<parseFloat($("#image").css("margin-left"))){
				retcWidth = parseFloat(retcWidth) - (parseFloat($("#image").css("margin-left")) - parseFloat(retcLeft)) + "px";
				retcLeft = parseFloat($("#image").css("margin-left")) + "px";
			}
			if((parseFloat(retcLeft) + parseFloat(retcWidth)) >
					(parseFloat($("#image").css("margin-left"))
					+parseFloat($("#image").css("width")))){
				retcWidth = parseFloat($("#image").css("width"))
						   +parseFloat($("#image").css("margin-left"))
						   -parseFloat(retcLeft) - 4 + "px";
			}
			addSign(retcWidth,retcHeight,retcLeft,retcTop);
			document.body.removeChild(findId(wId + index));
		    index++;
	    }catch(e){
	     	//alert(e);
	    }	 
    }
    else{
    	document.body.removeChild(findId(wId + index));
    }
    
    startX = 0, startY = 0;
	retcLeft = "0px", retcTop = "0px", retcHeight = "0px", retcWidth = "0px";
    flag = false;
}
//-----------------------全局点击事件，BUG较高-----------------------------//
document.onmousedown = function(e){
	//选中sign
	if(e.which == 3&&(e.target.className=="retc")){
		$("[class = 'retcX']").attr("class","retc");
		e.target.className = "retcX";	//被选中的标记class修改为retcX
		// ---------------------右键点击标记，弹出自定义菜单-----------------------------//
        $("#rightMouseListMenu").css({
            top : parseFloat($("#"+e.target.id).css("margin-top"))+parseFloat($("#"+e.target.id).css("height"))+ 6 +"px",
            left : parseFloat($("#"+e.target.id).css("margin-left"))+parseFloat($("#"+e.target.id).css("width"))+ 6 +"px"
        }).slideDown(100);
        var targetSign = e.target.id;
        var btn_delete = $("li.rightMouseList")[0];
        //删除
        btn_delete.onmousedown = function(e){
        	if(e.which == 1){
        		deleteSign(document.getElementById(targetSign));
        		$("#rightMouseListMenu").slideUp(100);
        	}
        }
	}
	if(e.which == 1){
		$("[class = 'retcX']").attr("class","retc");
		$("#rightMouseListMenu").slideUp(100);
	}
}
//---------------------------------------标记初始化-----------------------------------------//
//从服务器获取数据
function getSignFromServer () {
	try{
		for(var i=0;i<xl.length;i++){
		    createSignFromServer(xl[i],yl[i],xr[i],yr[i],name[i]);
	    }	
	}catch(e){
		alert(e);
	}
    
}
//从LocalStorage中获取数据
function getSignFromLocalStorage (){
	try{
		for(var i=0; i<localStorage.length; i++){
			key = localStorage.key(i);
			if(key.slice(0,7) == "signAdd"){
				value = localStorage.getItem(key);
				var x = new Array();
				var y = 0;
				x[y++] = 0;
				for(var j = 0; j < value.length; j++){
					if(value[j] == ",")
						x[y++] = j;
				}

				x[y++] = value.length - 1;
				var xl = parseFloat(value.slice(x[0],x[1]));
				var yl = parseFloat(value.slice(x[1]+1,x[2]));
				var xr = parseFloat(value.slice(x[2]+1,x[3]));
				var yr = parseFloat(value.slice(x[3]+1,x[4]));

				var sizeR = referenceToSizeR(xl,yl,xr,yr);
				var size = sizeRTosize(sizeR[0],sizeR[1],sizeR[2],sizeR[3]);
				getSign(size[0],size[1],size[2],size[3]);
			}
	    }
	}catch(e){
		alert(e);
	}

	try{
	    for(var i=0; i<localStorage.length; i++){
	    	key = localStorage.key(i);
			if(key.slice(0,10) == "deleteSign"){
				value = localStorage.getItem(key);
				removeSign(value);
			}
	    }
	}catch(e){
		alert(e);
	}
}
//---------------------------------------标记操作函数---------------------------------------//
//传入服务器数据添加标记
function createSignFromServer(xl,yl,xr,yr,name) {
	var newSign = document.createElement("div");
	document.body.appendChild(newSign);

	newSign.id = sId + index;
	newSign.className = "retc";

	var SizeR = referenceToSizeR(xl,yl,xr,yr);
	var Size = sizeRTosize(SizeR[0],SizeR[1],SizeR[2],SizeR[3]);

	newSign.style.width = Size[0] + "px";
	newSign.style.height = Size[1] + "px";
	newSign.style.marginLeft = Size[2] + "px";
	newSign.style.marginTop = Size[3] + "px";
	newSign.name = name;

	document.getElementById("signNum").innerText = $("div.retc").length;
	index++;
}
//传入localStorage数据添加标记
function getSign(width,height,left,top) {
	var newSign = document.createElement("div");
	document.body.appendChild(newSign);

	newSign.id = sId + index;
	newSign.className = "retc";
	newSign.style.width = width + "px";
	newSign.style.height = height + "px";
	newSign.style.marginLeft = left + "px";
	newSign.style.marginTop = top + "px";
	newSign.name = "retc" + index;

	document.getElementById("signNum").innerText = $("div.retc").length;
	index++;
}
//传入localStorage数据移除标记
function removeSign(name) {
	var signList = $("div[class = 'retc']");
	for(var i = 0; i < signList.length; i++){
		if(signList[i].name == name){
			document.body.removeChild(signList[i]);
			document.getElementById("signNum").innerText = $("div.retc").length;
		}
	}
}
//添加标记并记录到localStorage
function addSign(retcWidth,retcHeight,retcLeft,retcTop){
	var div = document.createElement("div");
	document.body.appendChild(div);
	div.id = sId + index;
	div.className = "retc";
	div.style.width = retcWidth;
	div.style.height = retcHeight;
	div.style.marginLeft = retcLeft;
	div.style.marginTop = retcTop;
	div.name = "retc"+index;
	document.getElementById("signNum").innerText = $("div.retc").length;

	var size = new Array();
	size[0] = parseFloat(div.style.width);
	size[1] = parseFloat(div.style.height);
	size[2] = parseFloat(div.style.marginLeft);
	size[3] = parseFloat(div.style.marginTop);
	setAddSign(size[0],size[1],size[2],size[3]);	
}
//删除标记并记录到localStorage
function deleteSign(target){
	setDeleteSign(target);
	document.body.removeChild(target);
	document.getElementById("signNum").innerText = $("div.retc").length;
}
//---------------------------------------数据储存函数---------------------------------------//
//将标记删除信息储存到LocalStorage中
function setDeleteSign(target){
	reference = target.name;
	localStorage.setItem("deleteSign" + getDeleteSignNum(), reference);
}

//将标记添加信息储存到LocalStorage中
function setAddSign (w,h,l,t) {
	var sizeR = sizeToSizeR(w,h,l,t);
	var reference = sizeRToReference(sizeR[0],sizeR[1],sizeR[2],sizeR[3]);
	localStorage.setItem("signAdd" + getAddSignNum(), reference);
}

//---------------------------------------数据获取函数---------------------------------------//
//获取LocalStorage中添加信息数目
function getAddSignNum(){
	var x = 1;
	for(var i=0; i<localStorage.length; ++i){
		key = localStorage.key(i);
		if(key.slice(0,7) == "signAdd"){
			x++;
		}
	}
	return x;
}
//获取LocalStorage中删除信息数目
function getDeleteSignNum(){
	var x = 1;
	for(var i=0; i<localStorage.length; ++i){
		key = localStorage.key(i);
		if(key.slice(0,10) == "deleteSign"){
			x++;
		}
	}
	return x;
}
//寻找id
var findId = function(id){
	return document.getElementById(id);
}

//-----------------------数据转换----------------------------//
//标记实际尺寸转尺寸比
function sizeToSizeR(width,height,left,top){
	var imageWidth = document.getElementById("image").offsetWidth;
	var imageHeight = parseFloat($("#image").css("height"));
	var imageMarginLeft = parseFloat($("#image").css("margin-left"));
	var imageMarginTop = parseFloat($("#header").css("height"));

	var widthR = width / imageWidth;
	var heightR = height / imageHeight;
	var leftR = (left - imageMarginLeft) / imageWidth;
	var topR = (top - imageMarginTop) / imageHeight;
	var sizeR = new Array(widthR,heightR,leftR,topR);
	return sizeR;
}

//标记尺寸比转实际尺寸
function sizeRTosize(widthR,heightR,leftR,topR){
	var imageWidth = document.getElementById("image").offsetWidth;
	var imageHeight = document.getElementById("image").offsetHeight;;
	var imageMarginLeft = parseFloat($("#image").css("margin-left"));
	var imageMarginTop = parseFloat($("#header").css("height"));

	var width = widthR * imageWidth;
	var height = heightR * imageHeight;
	var left = leftR * imageWidth + imageMarginLeft;
	var top = topR * imageHeight + imageMarginTop;
	var size = new Array(width,height,left,top);
	return size;
}

//标记尺寸比转坐标
function sizeRToReference(widthR,heightR,leftR,topR){
	var xl = leftR * 100;
	var yl = topR * 100;
	var xr = (leftR + widthR) * 100;
	var yr = (topR + heightR) * 100;
	var reference = new Array(xl,yl,xr,yr);
	return reference;
}

//标记坐标换尺寸比
function referenceToSizeR(xl,yl,xr,yr){
	var widthR = (xr - xl) / 100;
	var heightR = (yr - yl) / 100;
	var leftR = xl / 100;
	var topR = yl / 100;
	var sizeR = new Array(widthR,heightR,leftR,topR);
	return sizeR;
}

//-----------------------其他事件函数-------------------------//
//下一页点击事件
document.getElementById("imageBtnNext").onmousedown = function(e){
	if(e.which == 1){
		var arraySigns = new Array();
		var xl = new Array();
		var yl = new Array();
		var xr = new Array();
		var yr = new Array();

		for(var i=0; i<localStorage.length; ++i){
			key = localStorage.key(i);
			value = localStorage.getItem(key);
			if(key.slice(0,7) == "signAdd"){
				var x = new Array();
				var y = 0;
				x[y++] = 0;
				for(var j = 0; j < value.length; j++){
					if(value[j] == ",")
						x[y++] = j;
				}

				x[y++] = value.length - 1;
				var widthR = parseFloat(value.slice(x[0],x[1]));
				var heightR = parseFloat(value.slice(x[1]+1,x[2]));
				var leftR = parseFloat(value.slice(x[2]+1,x[3]));
				var topR = parseFloat(value.slice(x[3]+1,x[4]));
				var arraySign = new Array(widthR,heightR,leftR,topR,key);
				arraySigns[i] = arraySign;

				xl[i] = widthR;
				yl[i] = heightR;
				xr[i] = leftR;
				yr[i] = topR;

				$("#formTest").append('<input type="checkbox" name="xl" checked="true" value='+xl[i]+' ></input>');
				$("#formTest").append('<input type="checkbox" name="yl" checked="true" value='+yl[i]+' ></input>');
				$("#formTest").append('<input type="checkbox" name="xr" checked="true" value='+xr[i]+' ></input>');
				$("#formTest").append('<input type="checkbox" name="yr" checked="true" value='+yr[i]+' ></input>');
			}
			else if(key.slice(0,10) == "deleteSign"){
				$("#formTest").append('<input type="checkbox" name="delteList" checked="true" value='+value+' ></input>');
			}
			else ;
    	}
		$("input[name='submit']").click();
		localStorage.clear();	//提交表单后，清除LocalStorage内容
	}
}