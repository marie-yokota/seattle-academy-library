<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%@ page contentType="text/html; charset=utf8"%>
<%@ page import="java.util.*"%>
<html>
<head>
<title>個人ページ｜シアトルライブラリ｜シアトルコンサルティング株式会社</title>
<link href="<c:url value="/resources/css/reset.css" />" rel="stylesheet" type="text/css">
<link href="https://fonts.googleapis.com/css?family=Noto+Sans+JP" rel="stylesheet">
<link href="<c:url value="/resources/css/default.css" />" rel="stylesheet" type="text/css">
<link href="https://use.fontawesome.com/releases/v5.6.1/css/all.css" rel="stylesheet">
<link href="<c:url value="/resources/css/home.css" />" rel="stylesheet" type="text/css">
<script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<script src="resources/js/home.js"></script>
<script src="resources/js/sessionUsersId.js" /></script>
</head>
<body class="wrapper">
    <header>
        <div class="left">
            <img class="mark" src="resources/img/logo.png" />
            <div class="logo">Seattle Library</div>
        </div>
        <div class="right">
            <ul>
                <li><a href="<%=request.getContextPath()%>/home" class="menu">Home</a></li>
                <li><a href="<%=request.getContextPath()%>/">ログアウト</a></li>
            </ul>
        </div>
    </header>
    <main>
        <h1>貸出履歴一覧</h1>
        <div class="content_body">
            <c:if test="${!empty resultMessage}">
                <div class="error_msg">${resultMessage}</div>
            </c:if>
            <form class="search_box">
                <input type="search" id="search-text" placeholder="検索ワードを入力" value="">
            </form>
            <div class="booklist">
                 <div class="lending_status"> <p>貸出中</p></div>
               
                <c:forEach var="bookInfo" items="${getedLendingBookList}">
                    <!--貸出中の書籍リスト-->
                    <div class="Lendingbooks">
                        <ul>
                            <li class="rent_date" value="${bookInfo.lendingRegDate}">貸出日:${bookInfo.lendingRegDate}</li>
                        </ul>
                        <form method="post" class="book_thumnail" action="<%=request.getContextPath()%>/details">
                             <input class="usersId" type="hidden" name="usersId" value="${usersId}" id="usersId">
                            <a href="javascript:void(0)" onclick="this.parentNode.submit();"> <c:if test="${bookInfo.thumbnail =='null'}">
                                    <img class="book_noimg" src="resources/img/noImg.png">
                                </c:if> <c:if test="${bookInfo.thumbnail !='null'}">
                                    <img class="book_img" src="${bookInfo.thumbnail}">
                                </c:if>
                            </a> <input type="hidden" name="bookId" value="${bookInfo.bookId}">
                        </form>
                        <ul>
                            <li class="book_title" value="${bookInfo.title}">${bookInfo.title}</li>
                            <li class="book_author" value="${bookInfo.author}">${bookInfo.author}</li>
                            <li class="book_publisher" value="${bookInfo.publisher}">出版社:${bookInfo.publisher}</li>
                            <li class="book_publish_date" value="${bookInfo.publishDate}">出版日:${bookInfo.publishDate}</li>
                        </ul>
                    </div>
                </c:forEach>
                <!--貸出可の書籍リスト-->
                <div class="lending_status"><p >貸出履歴</p></div>
                
                <c:forEach var="bookInfo" items="${getedHistoryBookList}">
                    <div class="Lendingbooks">
                        <ul>
                            <li class="rent_date" value="${bookInfo.lendingRegDate}">貸出日:${bookInfo.lendingRegDate}</li>
                            <li class="return_date" value="${bookInfo.lendingUpdDate}">返却日:${bookInfo.lendingUpdDate}</li>
                        </ul>
                        <form method="post" class="book_thumnail" action="<%=request.getContextPath()%>/details">
                         <input class="usersId" type="hidden" name="usersId" value="${usersId}" id="usersId">
                            <a href="javascript:void(0)" onclick="this.parentNode.submit();"> <c:if test="${bookInfo.thumbnail =='null'}">
                                    <img class="book_noimg" src="resources/img/noImg.png">
                                </c:if> <c:if test="${bookInfo.thumbnail !='null'}">
                                    <img class="book_img" src="${bookInfo.thumbnail}">
                                </c:if>
                            </a> <input type="hidden" name="bookId" value="${bookInfo.bookId}">
                        </form>
                        <ul>
                            <li class="book_title" value="${bookInfo.title}">${bookInfo.title}</li>
                            <li class="book_author" value="${bookInfo.author}">${bookInfo.author}</li>
                            <li class="book_publisher" value="${bookInfo.publisher}">出版社:${bookInfo.publisher}</li>
                            <li class="book_publish_date" value="${bookInfo.publishDate}">出版日:${bookInfo.publishDate}</li>
                        </ul>
                    </div>
                </c:forEach>
            </div>
        </div>
    </main>
</body>
</html>
