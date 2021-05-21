/**
 *　detail.jsp内での動作
 *
 */

$(function() {
	$(document).ready(function(){
	var status = $("#lendingStatus").text();
		if( status == '貸出中'){
			$("#btn_rentBook").prop('disabled', true).css('pointer-events','none');
			$("#btn_returnBook").prop('disabled', false);
			$("#btn_deleteBook").prop('disabled', true).css('pointer-events','none');			
		}else{
			$("#btn_rentBook").prop('disabled', false);
			$("#btn_returnBook").prop('disabled', true).css('pointer-events','none');
			$("#btn_deleteBook").prop('disabled', false);			
		}
	})
	
});