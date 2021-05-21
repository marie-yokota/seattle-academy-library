/**
 * bulkRegist.jsp内での動作
 */
$(function(){
	//読み込み時の初期設定：一括登録ボタン非活性
	$('#bulk-btn').prop('disabled',true).css('pointer-events','none');	
	//ファイルが読み込まれたときに、一括登録ボタンが活性化
	$('#file_name').on('change', function() {   
		$('#bulk-btn').prop('disabled',false).css('pointer-events','');	
	});		
});