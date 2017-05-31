var divHeader = document.createElement("div");
divHeader.id = "header";
document.body.appendChild(divHeader);

var User = new Array("admin","标记员","Leo.Hu");
// var User = getUser();

var pUserName = document.createElement("p");
pUserName.innerText = User[2];
divHeader.appendChild(pUserName);

var divUserType = document.createElement("div");
divUserType.innerText = User[1];
divHeader.appendChild(divUserType);

function getUser () {
	var id;
	var type;
	var wname;
	var User = new Array(id,type,wname);

	return User;
}
