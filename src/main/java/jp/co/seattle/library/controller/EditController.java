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
import jp.co.seattle.library.service.LendingService;
import jp.co.seattle.library.service.ThumbnailService;

/**
 * 書籍情報編集コントローラー
 *
 * 2021/04/26
 */
@Controller //
public class EditController {
    final static Logger logger = LoggerFactory.getLogger(BooksService.class);

    @Autowired
    private BooksService booksService;

    @Autowired
    private ThumbnailService thumbnailService;

    @Autowired
    private LendingService lendingService;


    /**
     * 書籍情報の更新をする
     * @param locale
     * @param bookId
     * @param model
     * @return 詳細情報に遷移する
     */
    @Transactional
    @RequestMapping(value = "/editBook", method = RequestMethod.POST)
    public String displayEdit(Locale locale,
            @RequestParam("bookId") Integer bookId,
            Model model) {
        // デバッグ用ログ
        logger.info("Welcome displayEdit.java! The client locale is {}.", locale);

        model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
        return "editBook";
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
     * @return 詳細画面に遷移する
     */
    @Transactional
    @RequestMapping(value = "/updateBook", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
    public String editBook(Locale locale,
            @RequestParam("bookId") int bookId,
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("publisher") String publisher,
            @RequestParam("publish_date") String publishDate,
            @RequestParam("description") String description,
            @RequestParam("isbn") String isbn,
            @RequestParam("thumbnail") MultipartFile file,
            Model model) {
        logger.info("Welcome editBook.java! The client locale is {}.", locale);

        // パラメータで受け取った書籍情報をDtoに格納する。
        BookDetailsInfo bookInfo = new BookDetailsInfo();
        bookInfo.setBookId(bookId);
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
                return "editBook";
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

        //出版日が半角数字YYYYMMDD形式であるか確認
        boolean isPublishDateVaild = !(StringUtils.isEmpty(publishDate));

        //必須項目が入力されていない場合の対処
        if (!isTitleVaild || !isAuthorVaild || !isPublisherVaild || !isPublishDateVaild) {
            model.addAttribute("errorInput", "必須項目は全て入力してください");
            return "editBook";
        } else if (isPublishDateVaild) { //出版日が日にちとして有効であるか確認
            try {
                DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                dateFormat.setLenient(false);//厳密にチェックをする
                String transformationPublishDate = dateFormat.format(dateFormat.parse(publishDate)); //入力値を指定されたフォーマット日付型に変換
                if (!publishDate.equals(transformationPublishDate)) {
                    model.addAttribute("errorPublishDate", "有効な日にちを入力してください");
                    return "editBook";
                }
            } catch (Exception e) {
                model.addAttribute("errorPublishDate", "有効な日にちを入力してください");
                return "editBook";
            }

        }

        //ISBNが半角数字であるか確認
        boolean isIsbnVaild = isbn.matches("^([0-9])+$");

        if (!StringUtils.isEmpty(isbn)) { //ISBNにデータが入っているか
            if (!isIsbnVaild) { //数字でない場合
                model.addAttribute("errorIsbn", "①ISBNは10桁または13桁の数字を入力してください");
                return "editBook";
            } else if (!(isbn.length() == 13) && !(isbn.length() == 10)) { //10桁13桁でいない場合が真
                model.addAttribute("errorIsbn", "②ISBNは10桁または13桁の数字を入力してください");
                return "editBook";
            }
        }

        // 書籍情報を更新する
        booksService.editBook(bookInfo);

        //model.addAttribute("resultMessage", "登録完了");

        //更新した書籍の詳細情報を表示するように実装

        model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));

        //貸出状況を確認とフロントに表示
        int count = lendingService.LendingConfirmation(bookId);
        if (count == 1) {
            //借りるボタン_非活性
            model.addAttribute("rentActivation", "disabled");
            //返すボタン_活性
            model.addAttribute("returnActivation");
            //貸出ステータス
            model.addAttribute("lendingStatus", "貸出中");
        } else {
            //借りるボタン_活性
            model.addAttribute("rentActivation");
            //返すボタン_非活性
            model.addAttribute("returnActivation", "disabled");
            //貸出ステータス
            model.addAttribute("lendingStatus", "貸出可能");
        }
        //  詳細画面に遷移する
        return "details";
    }
}
