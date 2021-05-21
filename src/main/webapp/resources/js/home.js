$(function() {
  searchWord = function(){
    var searchText = $("#search-text").val(),// 検索ボックスに入力されたテキスト
        targetText;
    $(".books").each(function() {
       $('.books li').each(function() {
 
    // コンソールに1つずつ出力
    console.log($(this).text());
	console.log("");
 // 検索対象となるリストに入力された文字列が存在するかどうかを判断
      if (targetText.indexOf(searchText) != -1) {
        $(".books").removeClass('hidden');
      } else {
        $(".books").addClass('hidden');
      }
    });
  	});
      
  };

  // searchWordの実行
  $('#search-text').on('input', searchWord);
});