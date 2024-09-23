package moyomoyoe.reservation.controller;

import jakarta.servlet.http.HttpSession;
import moyomoyoe.reservation.DTO.ScheduleDTO;
import moyomoyoe.reservation.DTO.StoreDTO;
import moyomoyoe.reservation.Service.ScheduleService;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/reservation/schedule")
//리팩토링 , 첨삭 생각 나중에 하기
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
    public String storeInfo(@PathVariable("code") int code, HttpSession session) {
        //code가 0이라면 => 저장된 정보가 없음 등록화면으로
        if (code == 0)
            return "redirect:" + defaultUrl+"regist/store";
        
        // 해당 사업체의 세부정보와 일정정보를 세션에 저장
        StoreDTO store = reserService.getStoreAllInfo(code);
        List<ScheduleDTO> schedule = reserService.getSchedule(code);

        if (store != null) {
            session.setAttribute("storeInfo", store);
            session.setAttribute("schedule", schedule);
        }

        return "redirect:" +defaultUrl+"storeInfo"; // 해당 페이지로 리턴
    }

    //일정 등록 페이지로 가기
    @GetMapping("/storeInfo")
    public String getStoreInfoFromSession(HttpSession session, Model model) {
        // 세션에 저장된 데이터를 가져옴
        StoreDTO storeInfo = (StoreDTO) session.getAttribute("storeInfo");
        List<ScheduleDTO> schedule = (List<ScheduleDTO>) session.getAttribute("schedule");

        // 모델에 추가해서 Thymeleaf로 전달
        model.addAttribute("storeInfo", storeInfo);
        model.addAttribute("schedule", schedule);

        return defaultUrl+"storeInfo";
    }


    //등록페이지로 이동
    @GetMapping("/regist/store/{code}")
    public String RegistStore(Model model, @PathVariable("code") int code, RedirectAttributes redirectAttributes) {
        StoreDTO store = reserService.getStoreAllInfo(code);
        //로그인 정보에서 등록된 게 있으면 미리 작성되어 있는게 좋음
        redirectAttributes.addFlashAttribute("store", store);
        return "redirect:" + defaultUrl + "regist/store";
    }
    @GetMapping("/regist/store")
    public String RegistStore(Model model) {
        StoreDTO store = (StoreDTO) model.getAttribute("store");
        model.addAttribute("store", store==null ? new StoreDTO():store);
        return defaultUrl + "registstore";
    }

    //리다이렉팅에 대한 유동적인 판정이 필요하므로, js에서 처리
    //사업장 등록
    //일정 등록
    @PostMapping("/regist/store")
    @ResponseBody
    public Map<String, String> RegistStore(@ModelAttribute StoreDTO store) {
        Map<String, String> response = new HashMap<>();
        //StoreDTO temp = new StoreDTO(0,"더미 데이터", "종각", "기타","123456","어떤 가게입니다.",1, null);
        System.out.println(store);

        try {
            // Store 등록 서비스 호출
            reserService.registStore(store);
            // 성공 시 응답 설정
            response.put("status", "success");
            response.put("message", "Store registered successfully!");
        } catch (Exception e) {
            // 실패 시 응답 설정
            e.printStackTrace();
            response.put("status", "failure");
            response.put("message", "Failed to register store.");
        }
        return response;
    }

    //일정 관리 (등록/ 수정/ 삭제)
    @PostMapping("/regist/schedule")
    @ResponseBody
    public Map<String, String> RegistSchedule(
                                       @ModelAttribute int code,
                                       @ModelAttribute List <ScheduleDTO> newScheduleDTOS) {
        // 새로운 일정 사라진 일정 반영,
        //1. 해당 일정에 예약이 있는 지 확인함
        // <쉬운방식 아무튼 예약 다 취소함.

        //어려운 방식 기존의 일정테이블을 가져온 뒤,
        // 삭제된 일정의 예약만 취소
        //2. 남아있는지 확인을 어떻게 하는가? equals 오버라이딩
        //그러면 필요한 것. 기존의 스케쥴과 보내준 스케쥴 리스트...

        //성공/ 실패문구 출력, 성공시 마이페이지로,
        Map<String, String> response = new HashMap<>();
        try {
            // Store 등록 서비스 호출
            reserService.registSchedule(code, newScheduleDTOS);
            // 성공 시 응답 설정
            response.put("status", "success");
            response.put("message", "Store registered successfully!");
        } catch (Exception e) {
            // 실패 시 응답 설정
            response.put("status", "failure");
            response.put("message", "Failed to register store.");
        }
        return response;
    }

    @GetMapping("/regist/schedule")
    public String schedule() {
        return defaultUrl+"storeInfo";
    }

}
