package jp.co.seattle.library.controller;

import java.util.List;
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

import jp.co.seattle.library.dto.BookInfo;
import jp.co.seattle.library.dto.UserInfo;
import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.UsersService;

/**
 * アカウント作成コントローラー
 */
@Controller //APIの入り口
public class AccountController {
    final static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private BooksService booksService;
    @Autowired
    private UsersService usersService;

    @RequestMapping(value = "/newAccount", method = RequestMethod.GET) //value＝actionで指定したパラメータ
    public String createAccount(Model model) {
        return "createAccount";
    }

    /**
     * 新規アカウント作成
     *
     * @param email メールアドレス
     * @param password パスワード
     * @param passwordForCheck 確認用パスワード
     * @param model
     * @return　ホーム画面に遷移
     */
    @Transactional
    @RequestMapping(value = "/createAccount", method = RequestMethod.POST)
    public String createAccount(Locale locale,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("passwordForCheck") String passwordForCheck,
            Model model) {
        // デバッグ用ログ
        logger.info("Welcome createAccount! The client locale is {}.", locale);

        // パラメータで受け取った書籍情報をDtoに格納する。
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(email);


        // TODO バリデーションチェック、パスワード一致チェック実装
        // バリデーションチェック
        boolean isMailVaild = email.matches("^([a-zA-Z0-9])+([a-zA-Z0-9\\._-])*@([a-zA-Z0-9_-])+([a-zA-Z0-9\\._-]+)+$");
        boolean isPWVaild = password.matches("^[A-Za-z0-9]+$");
        boolean isPWForCheckVaild = passwordForCheck.matches("^[A-Za-z0-9]+$");

        if (!isMailVaild || !isPWVaild || !isPWForCheckVaild) {
            model.addAttribute("errorLetter", "メールアドレス、パスワードは半角英数字を使用してください");
            return "createAccount";
        }

        boolean matchPWandPWcheck = password.equals(passwordForCheck);
        if (!matchPWandPWcheck) { //パスワード一致チェック実装
            model.addAttribute("errorPW", "パスワードが一致していません");
            return "createAccount";
        }

        userInfo.setPassword(password);
        usersService.registUser(userInfo);

        //リストないがからであるか確認
        List<BookInfo> getedBookList = booksService.getBookList();
        if (getedBookList.size() == 0) {
            model.addAttribute("resultMessage", "登録されている書籍はありません");
            return "home";
        }
        model.addAttribute("bookList", getedBookList);
        return "home";
    }


}
