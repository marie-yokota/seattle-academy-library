package jp.co.seattle.library.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.seattle.library.dto.BookInfo;
import jp.co.seattle.library.service.BooksService;

/**
 * Handles requests for the application home page.
 */
@Controller //APIの入り口
public class HomeController {
    final static Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private BooksService booksService;

    /**
     * Homeボタンからホーム画面に戻るページ
     * @param model
     * @return
     */
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String transitionHome(Model model) {
        //リスト内が空の場合→「登録されている書籍はありません」を表示 
        //それ以外は一覧を表示する
        List<BookInfo> getedBookList = booksService.getBookList();
        if (getedBookList.size() == 0) {
            model.addAttribute("resultMessage", "登録されている書籍はありません");
        } else {
            model.addAttribute("bookList", getedBookList);
        }
        return "home";
    }

}
