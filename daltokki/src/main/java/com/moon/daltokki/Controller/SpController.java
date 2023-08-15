package com.moon.daltokki.Controller;

import com.moon.daltokki.Model.SpModel;
import com.moon.daltokki.Service.SpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class SpController {

    //  ----------------------------------------------------------------------------  miju
/*    <송편쓰기 - 메시지 작성 파트>
        1. 작성 버튼 클릭 시 > 메시지 작성 페이지 리턴 (완료)
        2. 메시지 작성자가 자기 닉네임(1회성)을 설정할 수 있어야함(sender 파라미터로 전달)
        3. 중복 작성 불가 - 개개인 식별 가능한 토큰 필요
           -> 이 경우 모든 사람이 로그인 해야만 메시지 작성 가능? 아니라면 어떻게 식별?
        4. 선택 항목 (송편색 / 앙꼬 / 작성 메시지 / 작성자 닉네임) - (완료)
           -> DB 저장 항목 (+ 받는 사람 / 중복 작성 불가능하게 인식 가능한 토큰)
        5. 메시지 작성 후 로딩화면 > 선택한 송편색과 앙꼬 보여주며 문구 출력
           -> "재물복이 깃든 꿀을 넣었다"
*/
    @Autowired
    SpService spService;

    // 메시지 작성 페이지로 이동
    @GetMapping(value = "/writeSp")
    public String openMessagePage(Model model) {
        model.addAttribute("sp", new SpModel()); // 빈 MessageModel 객체를 모델에 추가

        return "message/writeMessage";
    }

    // db에 메시지 저장
    @PostMapping("/saveSp")
    public String saveSp(@ModelAttribute("sp") SpModel sp, Model model) {
        spService.saveSp(sp);
        model.addAttribute("sp", sp);
        
        // result.jsp 삭제하고 /main 매핑되도록 수정 - 지은
        return "redirect:main";
    }


//  ----------------------------------------------------------------------------  miju

}
