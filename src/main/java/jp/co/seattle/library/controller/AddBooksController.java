package jp.co.seattle.library.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.ThumbnailService;

/**
 * Handles requests for the application home page.
 */
@Controller //APIの入り口
public class AddBooksController {
    final static Logger logger = LoggerFactory.getLogger(AddBooksController.class);

    @Autowired
    private BooksService booksService;

    @Autowired
    private ThumbnailService thumbnailService;

    @RequestMapping(value = "/addBook", method = RequestMethod.GET)
    //value＝actionで指定したパラメータ
    //RequestParamでname属性を取得
    public String login(Model model) {
        return "addBook";
    }

    /**
     * 書籍情報を登録する
     * @param locale ロケール情報
     * @param bookId 書籍ID 
     * @param title 書籍名
     * @param author 著者名
     * @param publisher 出版社
     * @param publish_date 出版日
     * @param description 書籍説明
     * @param isbn ISBN
     * @param file サムネイルファイル
     * @param model モデル
     * @return 遷移先画面
     */
    @Transactional
    @RequestMapping(value = "/insertBook", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
    public String insertBook(Locale locale,
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("publisher") String publisher,
            @RequestParam("publish_date") String publishDate,
            @RequestParam("description") String description,
            @RequestParam("isbn") String isbn,
            @RequestParam("thumbnail") MultipartFile file,
            Model model) {
        logger.info("Welcome insertBooks.java! The client locale is {}.", locale);

        // パラメータで受け取った書籍情報をDtoに格納する。
        BookDetailsInfo bookInfo = new BookDetailsInfo();
        bookInfo.setTitle(title);
        bookInfo.setAuthor(author);
        bookInfo.setPublisher(publisher);
        bookInfo.setPublishDate(publishDate);
        bookInfo.setDescription(description);
        bookInfo.setIsbn(isbn);

        // クライアントのファイルシステムにある元のファイル名を設定する
        String thumbnail = file.getOriginalFilename();

        if (!file.isEmpty()) {
            try {
                // サムネイル画像をアップロード
                String fileName = thumbnailService.uploadThumbnail(thumbnail, file);
                // URLを取得
                String thumbnailUrl = thumbnailService.getURL(fileName);

                bookInfo.setThumbnailName(fileName);
                bookInfo.setThumbnailUrl(thumbnailUrl);

            } catch (Exception e) {

                // 異常終了時の処理
                logger.error("サムネイルアップロードでエラー発生", e);
                model.addAttribute("bookDetailsInfo", bookInfo);
                return "addBook";
            }
        }
        //「Vaild」意味:有効です
        // 有効であればTrue、無効であればFalse

        //titleのバリデーションチェック(必須項目である)
        boolean isTitleVaild = !(StringUtils.isEmpty(title));

        //著者名のバリデーションチェック(必須項目である)
        boolean isAuthorVaild = !(StringUtils.isEmpty(author));

        //出版社のバリデーションチェック(必須項目である)
        boolean isPublisherVaild = !(StringUtils.isEmpty(publisher));

        //出版日がバリデーションチェック(必須項目である)
        boolean isPublishDateVaild = !(StringUtils.isEmpty(publishDate));

        //必須項目が入力されていない場合の対処
        if (!isTitleVaild || !isAuthorVaild || !isPublisherVaild || !isPublishDateVaild) {
            model.addAttribute("errorInput", "必須項目は全て入力してください");
            return "addBook";
        } else if (isPublishDateVaild) { //出版日が日にちとして有効であるか確認
            try {
                DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                dateFormat.setLenient(false);//厳密にチェックをする
                String transformationPublishDate = dateFormat.format(dateFormat.parse(publishDate)); //入力値を指定されたフォーマット日付型に変換
                if (!publishDate.equals(transformationPublishDate)) {
                    model.addAttribute("errorPublishDate", "有効な日にちを入力してください");
                    return "addBook";
                }
            } catch (Exception e) {
                model.addAttribute("errorPublishDate", "有効な日にちを入力してください");
                return "addBook";
            }

        }

        //ISBNが10桁または13桁の半角数字であるか確認
        boolean isIsbnVaild = isbn.matches("^^([0-9]{10}|[0-9]{13})+$");

        if (!StringUtils.isEmpty(isbn)) { //ISBNにデータが入っているか
            if (!isIsbnVaild) { //数字でない場合
                model.addAttribute("errorIsbn", "①ISBNは10桁または13桁の数字を入力してください");
                return "addBook";
            } else if (!(isbn.length() == 13) || !(isbn.length() == 10)) { //桁でない場合が真
                model.addAttribute("errorIsbn", "②ISBNは10桁または13桁の数字を入力してください");
                    return "addBook";
            }
        }
        

        // 書籍情報を新規登録する
        booksService.registBook(bookInfo);
        //登録した書籍の詳細情報を表示するように実装
        int bookId = booksService.getBookId();
        //書籍の詳細情報の再取得
        model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
        //借りるボタン_活性
        model.addAttribute("rentActivation");
        //返すボタン_非活性
        model.addAttribute("returnActivation", "disabled");
        //貸出ステータス
        model.addAttribute("lendingStatus", "貸出可能");

        //  詳細画面に遷移する
        return "details";
    }

}
