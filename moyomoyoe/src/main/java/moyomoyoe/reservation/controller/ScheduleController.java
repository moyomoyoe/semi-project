package moyomoyoe.reservation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import moyomoyoe.reservation.model.dto.ScheduleDTO;
import moyomoyoe.reservation.model.dto.StoreDTO;
import moyomoyoe.reservation.model.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
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
        System.out.println("[사업자 마이페이지]");
        //code가 0이라면 => 저장된 정보가 없음 등록화면으로
        Integer storeId = reserService.FindUserStore(code);
        if (code <= 0 || storeId==null){
            return "redirect:" + defaultUrl+"regist/store/"+code;
        }
        else {
            // 해당 사업체의 세부정보와 일정정보를 세션에 저장
            StoreDTO store = reserService.getStoreAllInfo(storeId);

            System.out.println("store = " + store);

            session.setAttribute("store", store);
            return "redirect:" + defaultUrl + "storeInfo"; // 해당 페이지로 리턴
        }
    }

    @GetMapping("/storeInfo")
    public String getStoreInfoFromSession(HttpSession session, Model model) {
        // 세션에 저장된 데이터를 가져옴
        StoreDTO storeInfo = (StoreDTO) session.getAttribute("store");
        List<ScheduleDTO> schedule = reserService.getSchedule(storeInfo.getStoreId());

        String url = "/static/image/image1.png";
        // 모델에 추가해서 Thymeleaf로 전달
        model.addAttribute("store", storeInfo);
        model.addAttribute("schedule", schedule);
        model.addAttribute("image",url);

        return defaultUrl+"storeInfo";
    }


    //등록페이지로 이동
    @GetMapping("/regist/store/{code}")
    public String RegistStore(@PathVariable("code") int code, RedirectAttributes redirectAttributes) {
        //등록된 가게가 있는 사업자라면 "수정" 할 수 있도록 , 없다면 등록하도록 함
        System.out.println("사업자 아이디" +code);
        StoreDTO store;
        Integer storeId = reserService.FindUserStore(code);
        if (storeId!=null){
            System.out.println("정보있음");
            store = reserService.getStoreAllInfo(storeId);
        }
        else{
            System.out.println("정보없음");
            store=new StoreDTO();
            store.setUserId(code);
        }

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
    public Map<String, String> RegistStore(@RequestBody StoreDTO store) {
        Map<String, String> response = new HashMap<>();
        System.out.println(store+"확인된 데이터");
        //StoreDTO temp = new StoreDTO(0,"더미 데이터", "종각", "기타","123456","어떤 가게입니다.",1, null);
        System.out.println(store);
        try {
            // Store 등록 서비스 호출
            reserService.registStore(store);
            // 성공 시 응답 설정
            response.put("status", "success");
            response.put("message", "Store registered successfully!");
            response.put("확인", "이게맞나?");
        } catch (Exception e) {
            // 실패 시 응답 설정
            e.printStackTrace();
            response.put("status", "failure");
            response.put("message", "Failed to register store.");
        }

        return response;
    }

    //일정 관리 (등록/ 수정/ 삭제)
    @PostMapping("/regist/schedule/{code}")
    @ResponseBody
    public Map<String, String> RegistSchedule(@RequestBody List<ScheduleDTO> schedules, @PathVariable("code") int code) {

        //성공/ 실패문구 출력, 성공시 마이페이지로,
        System.out.println("출력");
        System.out.println("schedules: " + schedules);
        Map<String, String> response = new HashMap<>();

        try {
            // Store 등록 서비스 호출
            reserService.registSchedule(code, schedules);
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

    @GetMapping("/regist/schedule/{code}")
    public String schedule(@PathVariable("code") int code, HttpSession session) {
        System.out.println("닿는 중");
        Integer storeId = reserService.FindUserStore(code);
        List<ScheduleDTO> schedule;
        if (storeId!=null)
            schedule =reserService.getSchedule(storeId);
        else {
            schedule = new ArrayList<>();
        }
        System.out.println("스케쥴 출력"+schedule);
        //로그인 정보에서 등록된 게 있으면 미리 작성되어 있는게 좋음
        session.setAttribute("schedule", schedule);
        session.setAttribute("storeId",storeId);
        session.setAttribute("userId",code);
        return "redirect:" + defaultUrl + "regist/schedule";
    }
    @GetMapping("/regist/schedule")
    public String registSchedule(HttpSession session, HttpServletRequest req,  Model model) {
        System.out.println("session ID : " + session.getId());
        Map<String, Object> userSession = (Map<String, Object>) req.getSession().getAttribute("user");

        List<ScheduleDTO> schedule = (List<ScheduleDTO>) session.getAttribute("schedule");
        ObjectMapper objectMapper = new ObjectMapper();
        String scheduleJson = "[]";  // 기본값으로 빈 배열 설정

        // schedule이 null이 아닌 경우 JSON 문자열로 변환
        if (schedule != null) {
            try {
                scheduleJson = objectMapper.writeValueAsString(schedule);
            } catch (JsonProcessingException e) {
                e.printStackTrace();  // JSON 변환 예외 처리
            }
        };
        model.addAttribute("schedule", scheduleJson);
        model.addAttribute("userSession", userSession);
        model.addAttribute("storeId", session.getAttribute("storeId"));
        return defaultUrl + "registschedule";
    }
    @GetMapping("/delete/store/{code}")
    @ResponseBody
    public  Map<String, String> deleteStore(@PathVariable("code") int code){
        System.out.println("delete요청이 왔습니다");
        Integer storeId = reserService.FindUserStore(code);
        Map<String, String> response = new HashMap<>();
        try {
            reserService.deleteStore(storeId);
            response.put("status", "success");
            response.put("message", "Store delete successfully!");
        } catch (Exception e) {
            // 실패 시 응답 설정
            e.printStackTrace();
            response.put("status", "failure");
            response.put("message", "Failed to delete store.");
        }

        return response;
    }

}
