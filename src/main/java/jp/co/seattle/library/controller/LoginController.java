package jp.co.seattle.library.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.seattle.library.dto.BookInfo;
import jp.co.seattle.library.dto.UserInfo;
import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.UsersService;


/**
* ログインコントローラー
*/
@Controller /** APIの入り口 */
public class LoginController {
    final static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private BooksService booksService;
    @Autowired
    private UsersService usersService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String first(Model model) {
        return "login"; //jspファイル名
    }

    /**
     * ログイン処理
     *
     * @param email メールアドレス
     * @param password パスワード
     * @param model
     * @return　ホーム画面に遷移
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            Model model) {

        // 下記のコメントアウトを外してサービスクラスを使用してください。
        UserInfo selectedUserInfo = usersService.selectUserInfo(email, password);
        // パスワードとメールアドレスの組み合わせ存在チェック実装
        if (selectedUserInfo == null) {
            model.addAttribute("errorMessage", "パスワードとメールアドレスが一致していません");
            return "login";
        }

        //リストないがからであるか確認
        List<BookInfo> getedBookList = booksService.getBookList();
        if (getedBookList.size() == 0) {
            model.addAttribute("resultMessage", "登録されている書籍はありません");
        } else {
            model.addAttribute("bookList", getedBookList);
        }

        return "home";
    }
}
