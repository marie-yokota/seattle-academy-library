package jp.co.seattle.library.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class BulkRegistController {
    final static Logger logger = LoggerFactory.getLogger(AddBooksController.class);

    // @Autowired
    //private BooksService booksService;

    @RequestMapping(value = "/bulkRegist", method = RequestMethod.GET)
    //value＝actionで指定したパラメータ
    //RequestParamでname属性を取得
    public String bulkRegist(Model model) {
        return "bulkRegist";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
    public String uploadFile(Locale locale,
            @RequestParam("fileName") MultipartFile fileName,
            Model model) {
        logger.info("Welcome insertBooks.java! The client locale is {}.", locale);

        //以下ファイルの読み込みとデータを格納する
        // クライアントのファイルシステムにある元のファイル名を設定する
        //String fileName = file.getOriginalFilename();

        return "bulkRegist";

    }
}
