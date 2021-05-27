/**
 * usersIdを取得する
 */

$(function() {
	$(document).ready(function(){
	if($(".usersId").val() != ""){	
		//初回アクセス時usersIdを保存する
		var usersId = $(".usersId").val();
		sessionStorage.setItem('settionUserId',usersId);
		console.log(usersId);
	}else{
		//保持している場合、usersIdを任意のjspで表示
		var id = sessionStorage.getItem('settionUserId');
		$(".usersId").val(id);
		console.log(id);
	}	
	});
	
	$("#logOut").click(function(){
		//データを削除
		sessionStorage.removeItem('settionUserId');
	});
	
});