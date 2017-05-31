$(document).ready(function (e) {
	//contentMode 尺寸
	$(".contentMode").css({
		"top":$("#header").css("height"),
		"width":"85%",
		"min-height": (window.innerHeight - parseInt($("#header").css("height")) + 1) + "px",
	});
	//leftMode 尺寸
	$(".leftMode").css({
		"top":$("#header").css("height"),
		"width":"15%",
		"height": (window.innerHeight - parseInt($("#header").css("height")) + 1) + "px",
	});
	//btnLeftMode 尺寸
	$(".btnLeftMode").css({
		"margin-top": (parseInt($(".leftMode").css("height"))) * 0.025 + "px",
		"height": (parseInt($(".leftMode").css("height"))) * 0.14 + "px"
	});
	$("#btnUpload").css({
		"margin-top": (parseInt($(".leftMode").css("height"))) * 0.05 + "px",
		"height": (parseInt($(".leftMode").css("height"))) * 0.14 + "px"
	});
})
//屏幕大小监听
$(window).resize(function(){
	$(".contentMode").css({
		"top":$("#header").css("height"),
		"width":"100%",
		"height": (window.innerHeight - parseInt($("#header").css("height")) + 1) + "px",
	});
	$(".leftMode").css({
		"top":$("#header").css("height"),
		"width":"15%",
		"height": (window.innerHeight - parseInt($("#header").css("height")) + 1) + "px",
	});
	$(".btnLeftMode").css({
		"margin-top": (parseInt($(".leftMode").css("height"))) * 0.025 + "px",
		"height": (parseInt($(".leftMode").css("height"))) * 0.14 + "px"
	});
	$("#btnUpload").css({
		"margin-top": (parseInt($(".leftMode").css("height"))) * 0.05 + "px",
		"height": (parseInt($(".leftMode").css("height"))) * 0.14 + "px"
	});
});

$("#deleteLabelSubmit").click(function(){
	document.pictureFormSubmit.action = "deletelabel.do";
});
$("input[class = 'btnAllSelect']").click(function(){
    $("input[type = 'checkbox']").prop("checked",true);
});
$("input[class = 'btnAllNoSelect']").click(function(){
    $("input[type = 'checkbox']").prop("checked",false);
});
$("input[class = 'btnReSelect']").click(function(){
	$("input[type = 'checkbox']").each(function (){
		var x = $(this).prop("checked");
		if(x == true){
			x = false;
		}
		else{
			x = true;
		}
		$(this).prop("checked",x);
	});
});
document.getElementById("btnLoginOut").onmouseover = function (e) {
	document.getElementById(this.id).src = "./image/switch_hover.png";
}
document.getElementById("btnLoginOut").onmouseout = function (e) {
	document.getElementById(this.id).src = "./image/switch.png";
}


//查看按钮点击效果看这里
$("#btnView").click(function(){
	document.workerFormSubmit.action = "selecthistoryadmin.do";
	document.workerFormSubmit.target = "_blank";
});

