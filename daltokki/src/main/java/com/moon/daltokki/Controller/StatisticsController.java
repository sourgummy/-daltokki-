package com.moon.daltokki.Controller;

import com.moon.daltokki.Model.StatisticsModel;
import com.moon.daltokki.Service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
public class StatisticsController {

  @Autowired
  StatisticsService statisticsService;

  @GetMapping (value = "/myStatistics")
  public String myStatistics(Model model, @RequestParam String id)
  {
    // httpsession으로 id 받아와서 db 조회해서 뿌리기!!!! => 지금안됨
    // , @SessionAttribute String userId
    // , @RequestParam String name
    // 아이디,송편,앙꼬 보내기~

//    model.addAttribute(userId, "userId");
    List<StatisticsModel> spList = statisticsService.selectStatistics(id);

    int spTypeCount1 = 0;
    int spTypeCount2 = 0;
    int spTypeCount3 = 0;
    int spColorCount1 = 0;
    int spColorCount2 = 0;
    int spColorCount3 = 0;
    int spColorCount4 = 0;
    for(StatisticsModel sp : spList){

      if(sp.getSpType().equals("BEAN")){
        spTypeCount1++;
      } else if(sp.getSpType().equals("REDBEAN")){
        spTypeCount2++;
      } else if(sp.getSpType().equals("HONEY")){
        spTypeCount3++;
      }
      if(sp.getSpColor().equals("WHITE")){
        spColorCount1++;
      } else if(sp.getSpColor().equals("PINK")){
        spColorCount2++;
      } else if(sp.getSpColor().equals("GREEN")){
        spColorCount3++;
      } else if(sp.getSpColor().equals("RAINBOW")){
        spColorCount4++;
      }
    }

    StatisticsModel sp = new StatisticsModel();
    sp.setSpRecipient(id);
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

  @ResponseBody
  @GetMapping(value = "/ajaxAlarm")
  public void alarm(Model model, HttpServletResponse response, @RequestParam String spId){
    List<StatisticsModel> newList = statisticsService.selectStatistics(spId);

    JSONArray jsonArray = new JSONArray();

    for(StatisticsModel sModel : newList){
      System.out.println(sModel);
      JSONObject jsonObject = new JSONObject();

      jsonObject.put("spSender", sModel.getSpSender());

      jsonArray.add(jsonObject);
    }
    System.out.println(jsonArray.size());
    System.out.println(jsonArray.toJSONString());
    try {
      response.setCharacterEncoding("UTF-8");
      response.getWriter().print(jsonArray);
    } catch (IOException e) {
      e.printStackTrace();
    }




  }
}
