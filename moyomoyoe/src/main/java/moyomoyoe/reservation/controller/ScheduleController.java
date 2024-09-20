package moyomoyoe.reservation.controller;

import moyomoyoe.reservation.DTO.ScheduleDTO;
import moyomoyoe.reservation.DTO.StoreDTO;
import moyomoyoe.reservation.Service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping(value = "/reservation/schedule")
public class ScheduleController {
    //세션에서 role 확인후, 조회등의 페이지를 구분 해야함
    ScheduleService reserService;
    String defaultUrl ="/reservation/schedule/";

    @Autowired
    public ScheduleController(ScheduleService reserService) {
        this.reserService = reserService;
    }

    //사업장 세부정보
    @GetMapping("/storeInfo/{code}")
    public String StoreInfo(@PathVariable("code") int code, RedirectAttributes redirectAttributes) {
        //일반회원이라면 평범한화면, 사업자라면 수정까지 보여지는 화면(마이페이지) >화면에서 할 내용

        //해당 사업체의 세부정보와 일정정보를 담아 리다이렉팅
        StoreDTO store = reserService.getStoreAllInfo(code);
        List<ScheduleDTO> schedule = reserService.getSchedule(code);

        System.out.println(store);
        for (ScheduleDTO scheduleDTO : schedule) {
            System.out.println(scheduleDTO);
        }
        if(store!=null){
            redirectAttributes.addFlashAttribute("storeInfo", store);
            redirectAttributes.addFlashAttribute("schedule", schedule);
        }

        return "redirect:"+defaultUrl+"storeInfo";
    }
    //일정 등록 페이지로 가기
    @GetMapping("storeInfo")
    public String schedule() {
        return defaultUrl+"storeInfo";
    }
    
    //사업장 등록
    @PostMapping("/regist/store")
    public String RegistStore( StoreDTO info) {

        //쿼리문 진행
        int result = reserService.registStore(info);


        return "regist-store";
    }
    


    //일정 등록
    @PostMapping
    public ModelAndView RegistSchedule(ModelAndView mv , List <ScheduleDTO> scheduleDTOS ) {
        int result = reserService.registSchedule(scheduleDTOS);

        mv.setViewName((result==1 ? "storeInfo":"failed"));
        //성공/ 실패문구 출력, 성공시 마이페이지로,
        return mv;
    }


}
