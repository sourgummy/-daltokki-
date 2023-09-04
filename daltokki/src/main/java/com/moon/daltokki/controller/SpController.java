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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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

        09/03 추가 항목
        1. 전체적인 message form css 업데이트 (border & button & 비율 등)
        2. 메시지 작성 시 금지어 체크 blur 이벤트로 변경 + 금지어 갯수와 상관없이 alert 는 한번 실행 + 한번에 모든 금지어 삭제처리
        3. 중복닉네임 작성 후 submit 시 focus 처리 및 폼 제출 막기 (수정 완료)
        4. submit 동작 시
          (1). 모든 항목 작성 시 제출
          (2). 중복 닉네임 사용 시 닉네임으로 focus (수정시에만 제출가능)
        5. placeholder 메시지 수정
        6. 메시지 작성(성공) 완료 시 송편 속 타입에 따라 alert();
           ex) "메시지에 재물운을 담아 전송했습니다!"
        7. 메시지 길이 제한 200byte로 샹향 + 초과 시 폼 작성 제한
        8. 송편 제목/작성날짜 칼럼 추가

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

    /* 08/23 업데이트
        db에 메시지 저장(spModel) + user테이블에 sp_record에 (+1)
       09/03 업데이트
        작성일자(한국시간대로 변경) 저장
     */

    @PostMapping("/saveSp")
    public String saveSp(@ModelAttribute("sp") SpModel sp, Model model) {


        // sp테이블에 작성일자 저장
        // 1. 한국 시간대로 설정
        TimeZone seoulTimeZone = TimeZone.getTimeZone("Asia/Seoul");
        Date currentDate = new Date();

        // 2. SimpleDateFormat을 사용하여 날짜를 문자열로 변환
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        sdf.setTimeZone(seoulTimeZone); // 한국 시간대로 설정

        String spCreateDate = sdf.format(currentDate);

        // 3. DB에 작성일자 저장
        sp.setSpCreateDate(spCreateDate);

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
