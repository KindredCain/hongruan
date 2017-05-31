var Upload = (function(){
    var upimg = document.getElementById('upimg');
    var show  = document.getElementById('show');
    var drag = document.getElementById('drag');

    function init(){
        if(!(window.FileReader && window.File && window.FileList && window.Blob)){
            show.innerHTML = '您的浏览器不支持fileReader';
            upimg.setAttribute('disabled', 'disabled');
            return false;
        }
        handler();
    }

    function handler(){
        upimg.addEventListener('change', function(e){
            var files = this.files;
            if(files.length){
                checkFile(this.files);
            }
        });

        drag.addEventListener('dragenter', function(e){
            this.className = 'drag_hover';
        }, false);
        drag.addEventListener('dragleave', function(e){
            this.className = '';
        }, false);
        drag.addEventListener('drop', function(e){
            var files = e.dataTransfer.files;
            this.className = '';
            if (files.length != 0) {
                checkFile(files);
            };

            e.preventDefault();
        }, false)
        drag.addEventListener('dragover', function(e){
            e.dataTransfer.dragEffect = 'copy';
            e.preventDefault();
        }, false);

        show.addEventListener('click', function(e){
            var target = e.target;
            if(target.tagName.toUpperCase()=='IMG'){
                var parent = target.parentNode;
                var item = this.childNodes;
                for(var i=0; i<item.length; i++){
                    item[i].className = 'item';
                    item[i].firstElementChild.style.cssText = '';
                }

                var parent = target.parentNode;
            }
        }, false)
    }

    function checkFile(files){
        if (files.length != 0) {
            //获取文件并用FileReader进行读取
            var html = '';
            var i = 0, j = show.childElementCount;
            var funcs = function(){
                if(files[i]){
                    var x = parseInt((i+j)/4)*250;
                    var y = ((i+j)%4)*250;
                    var reader = new FileReader();
                    if(!/image\/\w+/.test(files[i].type)){
                        show.innerHTML = "请确保文件为图像类型";
                        return false;
                    }
                    reader.onload = function(e) {
                        html += '<div class="item"><img src="'+e.target.result+'" alt="img"><p>'+files[i].name+'</p></div>';
                        i++;
                        funcs(); // onload为异步调用
                        document.getElementById("imageNum").innerText = parseInt($("div[class='item']").length) + "张图片";
                    };
                    reader.readAsDataURL(files[i]);
                    // reader.readAsText(files[i]);
                }else{
                    show.innerHTML += html;
                }
            }
            funcs();
        }
    }
    return {
        init : init
    }
})();

Upload.init();

document.getElementById("btnUpimg").onmousedown = function(){
        $("#upimg").click();
}
document.getElementById("btnSubmit").onmousedown = function(){
    $("#submit").click();
}
