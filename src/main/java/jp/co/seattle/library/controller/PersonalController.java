package jp.co.seattle.library.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.seattle.library.dto.UsersLendingInfo;
import jp.co.seattle.library.service.UsersLendingHistoryService;

@Controller
public class PersonalController {
    final static Logger logger = LoggerFactory.getLogger(PersonalController.class);

    @Autowired
    private UsersLendingHistoryService usersLendingHistoryService;

    @Transactional
    @RequestMapping(value = "/mypage", method = RequestMethod.POST)
    //value＝actionで指定したパラメー
    //RequestParamでname属性を取得
    public String mypage(
            @RequestParam("usersId") int usersId,
            Model model) {
        //usersIdに紐づく、貸出中の時のリストの取得
        List<UsersLendingInfo> getedLendingBookList = usersLendingHistoryService.UsersLendingBookList(usersId);
        model.addAttribute("getedLendingBookList", getedLendingBookList);

        //usersIdに紐づく、貸出可の時のリストの取得
        List<UsersLendingInfo> getedHistoryBookList = usersLendingHistoryService.UsersHistoryBookList(usersId);
        model.addAttribute("getedHistoryBookList", getedHistoryBookList);
        return "personal";
    }
}
