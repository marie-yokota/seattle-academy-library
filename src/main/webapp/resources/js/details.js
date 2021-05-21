/**Idを
 * 画面でViewするタイミングのファンクションを調べる
*Jsでデバック
 */

$(function() {
	$(document).ready(function(){
	var status = $("#lendingStatus").text();
		if( status == '貸出中'){
			$("#btn_rentBook").prop('disabled', true);
			$("#btn_returnBook").prop('disabled', false);
			$("#btn_deleteBook").prop('disabled', true);
			
		}else{
			$("#btn_rentBook").prop('disabled', false);
			$("#btn_returnBook").prop('disabled', true);
			$("#btn_deleteBook").prop('disabled', false);
			
		}
	})
	
});