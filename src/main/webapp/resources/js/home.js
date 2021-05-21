$(function() {
	//検索窓に入力された時にイベントが発火
	//$('#search-text').on('input',function(){
		searchWord = function(){
		//検索窓に入力された値を格納
		var searchText = $(this).val();
		//eachメソッドで同じ名前のクラスやタグの1要素ごとに処理することをidで指定することなく、
		//簡単にコードを書くことができる。
		//$(繰り返す対象のセレクタ).each(function(index番号,取得したいvalue)
		$('.books').each(function(index,bookInfo){
			//bookInfo(bookId,thumbnail,title,author,publisher,publishDate)を
			//一つの塊として認識
			//text()で文字列を取得、indexOf(searchText)で検索値が存在するかをチェック
			if ($(bookInfo).text().indexOf(searchText) == -1){
				$(this).hide();
      		} else{
				$(this).show();
			}    	
  		})							
	};
	 $('#search-text').on('input', searchWord);
});
