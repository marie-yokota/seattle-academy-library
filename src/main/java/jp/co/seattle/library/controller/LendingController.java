package jp.co.seattle.library.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.LendingService;

/**
 * 貸出コントローラー
 *
 * 
 */
@Controller
public class LendingController {
    final static Logger logger = LoggerFactory.getLogger(LendingService.class);

    @Autowired
    private BooksService booksService;

    @Autowired
    private LendingService lendingService;

    /**
     * 書籍を借りる
     * @param locale
     * @param bookId
     * @param model
     * @return
     */
    @Transactional
    @RequestMapping(value = "/rentBook", method = RequestMethod.POST)
    public String rentBook(Locale locale,
            @RequestParam("bookId") Integer bookId,
            Model model) {
        // デバッグ用ログ
        logger.info("Welcome rentBook.java! The client locale is {}.", locale);

        //貸出登録
        lendingService.LendingRegistration(bookId);
        //貸出ステータス
        model.addAttribute("lendingStatus", "貸出中");
        //貸出情報の取得・再表示
        model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));

        return "details";
    }

    /**
     * 書籍を返却する
     * @param locale
     * @param bookId
     * @param model
     * @return
     */
    @Transactional
    @RequestMapping(value = "/returnBook", method = RequestMethod.POST)
    public String returnBook(Locale locale,
            @RequestParam("bookId") Integer bookId,
            Model model) {
        // デバッグ用ログ
        logger.info("Welcome returnBook.java! The client locale is {}.", locale);
        //返却登録
        lendingService.deleteLending(bookId);
        //貸出ステータス
        model.addAttribute("lendingStatus", "貸出可能");
        //貸出情報の取得・再表示
        model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
        return "details";
    }


}
