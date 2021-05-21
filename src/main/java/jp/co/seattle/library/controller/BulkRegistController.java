package jp.co.seattle.library.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
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

/**
 * 一括登録コントローラー
 *
 * 
 */
@Controller
public class BulkRegistController {
    final static Logger logger = LoggerFactory.getLogger(AddBooksController.class);

    @Autowired
    private BooksService booksService;

    //value＝actionで指定したパラメータ
    //RequestParamでname属性を取得
    @RequestMapping(value = "/bulkRegist", method = RequestMethod.GET)
    public String bulkRegist(Model model) {
        return "bulkRegist";
    }

    /**
     * ファイル一括登録
     * 
     * @param locale
     * @param fileName
     * @param model
     * @return
     */
    @Transactional
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
    public String uploadFlie(Locale locale,
            @RequestParam("file_name") MultipartFile fileName,
            Model model) {
        logger.info("Welcome insertBooks.java! The client locale is {}.", locale);

        //以下ファイルの読み込みとデータを格納する

        List<String[]> data = new ArrayList<String[]>();
        String line = null;
        List<String> errorList = new ArrayList<String>();

        int i = 1;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileName.getInputStream()));) {
            while ((line = bufferedReader.readLine()) != null) { // readLineで一行ずつ読み込む
                data.add(line.split(",", -1));
                //1行ずつ取り出しバリデーションチェック(errorリストに格納)                
            }
        } catch (Exception e) {
            model.addAttribute("errorRead", "読み込みに失敗しました。");
            return "bulkRegist";
        }
        for (String[] validation : data) {
            String requiredItem = "";
            String errorStr = "";
            String vaildError = "";

            //出版日のバリデーションチェック(必須項目である)
            boolean isPublishDateVaild = !(StringUtils.isEmpty(validation[3]));

            //必須項目が入力されていない場合の対処
            //titleが空である時
            if (StringUtils.isEmpty(validation[0])) {
                requiredItem = "「タイトル」";
            }

            //著者名が空である時
            if (StringUtils.isEmpty(validation[1])) {
                requiredItem = requiredItem + "「著者名」";
            }

            //出版社が空である時
            if (StringUtils.isEmpty(validation[2])) {
                requiredItem = requiredItem + "「出版社」";
            }

            if (!isPublishDateVaild) {//出版日が無効である
                requiredItem = requiredItem + "「出版日」";
            } else if (isPublishDateVaild) { //出版日が日にちとして有効である
                try {
                    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                    dateFormat.setLenient(false);//厳密にチェックをする
                    String transformationPublishDate = dateFormat.format(dateFormat.parse(validation[3])); //入力値を指定されたフォーマット日付型に変換
                    if (!validation[3].equals(transformationPublishDate)) {
                        errorStr = "「出版日」";
                    }
                } catch (Exception e) {
                    errorStr = "「出版日」";
                }
            }

            //ISBNが10桁または13桁の半角数字であるか確認
            boolean isIsbnVaild = validation[4].matches("^([0-9]{10}||[0-9]{13})$");
            if (!(StringUtils.isEmpty(validation[4]))) { //ISBNにデータが入っている場合
                if (!isIsbnVaild) { //有効でない場合
                    errorStr = errorStr + "「ISBN」";
                }
            }

            //エラー表示の設定
            //requiredItem:必須項目が未入力の時の表示
            if (!(StringUtils.isEmpty(requiredItem))) {
                vaildError = "必須項目:" + requiredItem + "が入力されていません。";
            }
            //errorStr:日付、ISBNが有効でない時の表示
            if (!(StringUtils.isEmpty(errorStr))) {
                vaildError = vaildError + "エラー:" + errorStr;
            }

            if (!StringUtils.isEmpty(vaildError)) {
                errorList.add("[" + i++ + "]" + vaildError);
            } else {
                i++;
            }
        }

        //errorリストが空  
        if (errorList.isEmpty()) {
            //→Yes：1行ずつDBサーバー登録
            for (String[] line_data : data) {//line_dataに一つの書籍情報を格納する
                // 各行データを要素毎に格納                 
                BookDetailsInfo bookInfo = new BookDetailsInfo();
                bookInfo.setTitle(line_data[0]);
                bookInfo.setAuthor(line_data[1]);
                bookInfo.setPublisher(line_data[2]);
                bookInfo.setPublishDate(line_data[3]);
                bookInfo.setDescription(line_data[4]);
                bookInfo.setIsbn(line_data[5]);

                booksService.registBook(bookInfo);
            }
            //→：Noエラー表示
        } else {
            model.addAttribute("errorList", errorList);
            return "bulkRegist";
        }
        model.addAttribute("completed", "登録完了しました。");
        return "bulkRegist";
    }
}
