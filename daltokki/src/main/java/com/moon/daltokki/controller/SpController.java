package com.moon.daltokki.controller;

import com.moon.daltokki.Model.SpModel;
import com.moon.daltokki.service.SpService;
import com.moon.daltokki.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class SpController {

    //  ----------------------------------------------------------------------------  miju
/*    <송편쓰기 - 메시지 작성 파트>
        1. 작성 버튼 클릭 시 > 메시지 작성 페이지 리턴 (완료)
        2. 메시지 작성자가 자기 닉네임(1회성)을 설정할 수 있어야함(sender 파라미터로 전달)
        3. 선택 항목 (송편색 / 앙꼬 / 작성 메시지 / 작성자 닉네임) - (완료)
        4. 글자 수 확인하기, 금지어 적용하기 (8/20 완료)
        5. 메시지 작성 후 로딩화면 > 선택한 송편색과 앙꼬 보여주며 문구 출력
           -> "재물복이 깃든 꿀을 넣었다"

        08/23 추가 항목
        1. (진행예정)메시지 다 작성하고 금지어 n개 확인 후 있으면 고치라고 alert 및 색깔 변경
        2. eventListener 무조건 실행되는 것 방지(완료)
        3. 메시지 작성 완료 시 수신자 user테이블에 spRecode +1 (완료)
        4. 한 user 테이블에 보내는 사람 닉네임은 중복 방지 (완료)
            -> ajax로 wrtieMessage.html에서 중복확인
        5. 메시지 일부항목 미선택 및 미입력시 폼제출 불가 (완료)
        6. 메시지 작성 완료 시 이동하는 매핑주소 수정 (완료)

*/
    @Autowired
    private SpService spService;

    @Autowired
    private UserService userService;

    // 메시지 작성 페이지로 이동
    @GetMapping(value = "/writeSp")
    public String openMessagePage(@RequestParam("id") String id, Model model) {
        model.addAttribute("sp", new SpModel()); // 빈 MessageModel 객체를 모델에 추가
        model.addAttribute("id", id);
        return "message/writeMessage";
    }

    // 08/23 업데이트
    // db에 메시지 저장(spModel) + user테이블에 sp_record에 (+1)
    @PostMapping("/saveSp")
    public String saveSp(@ModelAttribute("sp") SpModel sp, Model model) {

        // sp테이블에 메시지 저장하는 구문
        spService.saveSp(sp);
        model.addAttribute("sp", sp);
        log.info("[SpController][saveSp] Message saved successfully");

        // log.info("[SpController][saveSp] Parsed username: {}", sp.getSpRecipient());
        String username = sp.getSpRecipient();
        // user테이블에 spRecord +1 하는 구문
        userService.incrementSpRecord(username);

        // (08/23)공유된 링크주소로 매핑 변경
        return "redirect:main?id="+ sp.getSpRecipient();
    }

    // (8/23)해당 user의 송편을 작성한 발신자 닉네임 중복여부 체크
    @PostMapping("/checkDuplicateSender")
    public ResponseEntity<Boolean> checkDuplicateSender(@RequestParam String spSender) {
        // log.info("[SpController][checkDuplicateSender] 넘어온 닉네임 : {}", spSender);
        return ResponseEntity.ok(spService.isSenderDuplicate(spSender));
    }


//  ----------------------------------------------------------------------------  miju

}
