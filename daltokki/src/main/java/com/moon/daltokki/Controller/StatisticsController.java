package com.moon.daltokki.Controller;

import com.moon.daltokki.Model.SpModel;
import com.moon.daltokki.Model.StatisticsModel;
import com.moon.daltokki.Service.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class StatisticsController {

  @Autowired
  StatisticsService statisticsService;

  @GetMapping (value = "/myStatistics")
  public String myStatistics(Model model)
  {
    // httpsession으로 id 받아와서 db 조회해서 뿌리기!!!! => 지금안됨
    // , @SessionAttribute String userId
    // , @RequestParam String name
    // 아이디,송편,앙꼬 보내기~

//    model.addAttribute(userId, "userId");
    String name = "yeramii"; //임시
    List<StatisticsModel> spList = statisticsService.selectStatistics(name);
    int spTypeCount1 = 0;
    int spTypeCount2 = 0;
    int spTypeCount3 = 0;
    int spColorCount1 = 0;
    int spColorCount2 = 0;
    int spColorCount3 = 0;
    int spColorCount4 = 0;
    for(StatisticsModel sp : spList){
      if(sp.getSpType() == 1){
        spTypeCount1++;
      } else if(sp.getSpType() == 2){
        spTypeCount2++;
      } else if(sp.getSpType() == 3){
        spTypeCount3++;
      }
      if(sp.getSpColor() == 1){
        spColorCount1++;
      } else if(sp.getSpColor() == 2){
        spColorCount2++;
      } else if(sp.getSpColor() == 3){
        spColorCount3++;
      } else if(sp.getSpColor() == 4){
        spColorCount4++;
      }
    }

    StatisticsModel sp = new StatisticsModel();
    sp.setSpTypeCount1(spTypeCount1);
    sp.setSpTypeCount2(spTypeCount2);
    sp.setSpTypeCount3(spTypeCount3);
    sp.setSpColorCount1(spColorCount1);
    sp.setSpColorCount2(spColorCount2);
    sp.setSpColorCount3(spColorCount3);
    sp.setSpColorCount4(spColorCount4);

    model.addAttribute("sp", sp);

    return "message/myStatistics";
  }

}
