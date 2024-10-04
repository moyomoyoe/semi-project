package moyomoyoe.reservation.controller;

import jakarta.servlet.http.HttpSession;
import moyomoyoe.member.auth.model.dto.UserDTO;
import moyomoyoe.member.user.model.service.UserService;
import moyomoyoe.reservation.model.dao.ReservationMapper;
import moyomoyoe.reservation.model.dto.ReservationDTO;
import moyomoyoe.reservation.model.dto.ScheduleDTO;
import moyomoyoe.reservation.model.dto.StoreDTO;
import moyomoyoe.reservation.model.service.ReservationService;
import moyomoyoe.reservation.model.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeParseException;
import java.util.*;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    ReservationService reservationService;
    ScheduleService reserService;
    UserService uerService;
    ReservationMapper reservationMapper;
    String defaultUrl ="/reservation/schedule/";

    @Autowired
    public ReservationController(ScheduleService reserService, ReservationService reservationService) {
        this.reserService = reserService;
        this.reservationService = reservationService;
    }

    // 매장 목록 조회
    @GetMapping("/storeList")
    public String getStoreListPage(Model model) {
        List<StoreDTO> stores = reservationService.getAllStores();
        List<Map<String, Object>> storeListWithImagePaths = new ArrayList<>();

        for (StoreDTO store : stores) {
            Map<String, Object> storeMap = new HashMap<>();
            storeMap.put("store", store);

            // 매장 ID를 이용해 이미지 경로 조회
            String imagePath = reservationService.getImagePathByStoreId(store.getStoreId());
            storeMap.put("imagePath", imagePath);

            storeListWithImagePaths.add(storeMap);
        }

        model.addAttribute("stores", storeListWithImagePaths);
        return "reservation/storeList";
    }

    // 매장 상세 조회
    @GetMapping("/storeDetail/{id}")
    public String getStoreDetailPage(@PathVariable("id") int id, Model model) {
        StoreDTO store = reservationService.getStoreById(id);

        // 매장 ID로 이미지 경로 조회
        String imagePath = reservationService.getImagePathByStoreId(id);

        model.addAttribute("store", store);
        model.addAttribute("imagePath", imagePath);

        return "reservation/storeDetailView";
    }

    // 매장상세에서 예약 페이지로 이동
    @GetMapping("/submit")
    public String submitReservationPage(@RequestParam("storeId") int storeId, Model model) {
        StoreDTO store = reservationService.getStoreById(storeId);
        model.addAttribute("store", store);
        return "reservation/reservation";
    }

    // 예약 처리 (POST 요청)
    @PostMapping("/reservation/submit")
    public String submitReservation(
            @RequestParam("storeId") int storeId,
            @RequestParam("capacity") int capacity,
            @RequestParam("date") String date,
            @AuthenticationPrincipal UserDTO userDTO,
            RedirectAttributes redirectAttributes) {

        try {
            // 날짜 형식 파싱
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsedDate = dateFormat.parse(date);
            Date sqlDate = new Date(parsedDate.getTime());

            // 해당 매장 ID로 스케줄 정보를 조회
            List<ScheduleDTO> scheduleList = reservationService.getSchedulesByStoreId(storeId); // 매장 ID로 모든 스케줄 조회
            if (scheduleList == null || scheduleList.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "해당 매장에 대한 스케줄이 존재하지 않습니다.");
                return "redirect:/reservation/completion?storeId=" + storeId;
            }

            ScheduleDTO scheduleDTO = scheduleList.get(0); // 첫 번째 스케줄을 선택

            // 예약 DTO 생성
            ReservationDTO reservationDTO = new ReservationDTO(0, userDTO.getId(), sqlDate, capacity, 0, "");

            // 예약 저장
            reservationService.saveReservation(reservationDTO, scheduleDTO);

            // 성공 메시지와 함께 완료 페이지로 리다이렉트
            redirectAttributes.addFlashAttribute("message", "예약이 성공적으로 처리되었습니다.");
            return "redirect:/reservation/completion?storeId=" + storeId;

        } catch (ParseException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "날짜 형식이 올바르지 않습니다.");
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "시간 형식이 올바르지 않습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "예약 처리 중 오류가 발생했습니다.");
        }
        return "redirect:/reservation/completion?storeId=" + storeId;
    }

    // 예약 완료 페이지
    @GetMapping("/completion")
    public String reservationCompletion(@RequestParam("storeId") int storeId, Model model) {
        StoreDTO store = reservationService.getStoreById(storeId);
        model.addAttribute("store", store);
        return "completion";
    }

    // 예약된 시간 조회
    @GetMapping("/getReservedTimes")
    @ResponseBody
    public List<String> getReservedTimes(@RequestParam("storeId") int storeId, @RequestParam("date") String date) {
        return reservationService.getReservedTimes(storeId, date);
    }

    // 특정 매장 일정 조회
    @GetMapping("/schedules/{storeId}")
    @ResponseBody
    public List<ScheduleDTO> getSchedulesByStoreId(@PathVariable("storeId") int storeId) {
        return (List<ScheduleDTO>) reservationService.getSchedulesByStoreId(storeId);
    }

    // 사용자 예약 목록 조회
    @GetMapping("/userReservations")
    public ModelAndView getUserReservations(@AuthenticationPrincipal UserDTO userDTO, HttpSession session) {
        ModelAndView mv = new ModelAndView("reservation/userReservations");
        int userId = userDTO.getId();
        List<ReservationDTO> userReservations = reservationService.getUserReservations(userId);

        // 스케줄 정보와 예약 정보를 결합하여 반환
        List<Map<String, Object>> reservationDetails = new ArrayList<>();
        for (ReservationDTO reservation : userReservations) {
            Map<String, Object> reservationMap = new HashMap<>();
            reservationMap.put("resId", reservation.getResId());
            reservationMap.put("userIdRes", reservation.getUserIdRes());
            reservationMap.put("resDate", reservation.getResDate());
            reservationMap.put("customerNum", reservation.getCustomerNum());
            reservationMap.put("scheduleId", reservation.getScheduleId());

            // 스케줄 정보를 통해 시작 및 종료 시간 추가
            ScheduleDTO schedule = reservationService.getScheduleById(reservation.getScheduleId());
            if (schedule != null) {
                reservationMap.put("startTime", schedule.getStartTime());
                reservationMap.put("endTime", schedule.getEndTime());
            } else {
                reservationMap.put("startTime", "시간 없음");
                reservationMap.put("endTime", "시간 없음");
            }

            reservationDetails.add(reservationMap);
        }

        session.setAttribute("userReservations", reservationDetails);
        mv.addObject("userReservations", reservationDetails);
        //List<ReservationDTO> userReservations = reservationService.getUserReservations(userId);

        List<Map<String,String>> reservation = reserService.getUserFullReserInfo(userId);

        //session.setAttribute("userReservations", userReservations);
        //mv.addObject("userReservations", userReservations);
        mv.addObject("reservations", reservation);

        mv.addObject("user", userDTO);
        return mv;
    }

    // 사용자 예약 목록 조회 - JSON 응답
    @GetMapping("/userReservationsData")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getUserReservationsData(@AuthenticationPrincipal UserDTO userDTO) {
        int userId = userDTO.getId();
        List<ReservationDTO> userReservations = reservationService.getUserReservations(userId);

        if (userReservations == null || userReservations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Collections.emptyList());
        } else {
            List<Map<String, Object>> reservationDataList = new ArrayList<>();

            for (ReservationDTO reservation : userReservations) {
                Map<String, Object> map = new HashMap<>();
                map.put("resId", reservation.getResId());
                map.put("userIdRes", reservation.getUserIdRes());
                map.put("resDate", reservation.getResDate());
                map.put("customerNum", reservation.getCustomerNum());
                map.put("scheduleId", reservation.getScheduleId());
                map.put("startTime", reservationService.getScheduleById(reservation.getScheduleId()).getStartTime());
                map.put("endTime", reservationService.getScheduleById(reservation.getScheduleId()).getEndTime());

                reservationDataList.add(map);
            }

            return ResponseEntity.ok(reservationDataList);
        }
    }

    // 예약 상세 정보
    @GetMapping("/reservationDetail/{resId}")
    public ModelAndView getReservationDetail(@PathVariable("resId") int resId) {
        ModelAndView mv = new ModelAndView("reservation/reservationDetail");

        // 예약 정보 조회
        Map<String, Object> reservationDetailWithStore = reservationService.getReservationDetailWithStore(resId);

        if (reservationDetailWithStore == null || reservationDetailWithStore.isEmpty()) {
            mv.addObject("error", "해당 예약을 찾을 수 없습니다.");
            return mv;
        }

        // 필요한 필드를 명시적으로 추가하여 Thymeleaf에서 사용 가능하도록 설정
        mv.addObject("resId", reservationDetailWithStore.get("res_id"));
        mv.addObject("resDate", reservationDetailWithStore.get("res_date"));
        mv.addObject("startTime", reservationDetailWithStore.get("start_time"));
        mv.addObject("endTime", reservationDetailWithStore.get("end_time"));
        mv.addObject("customerNum", reservationDetailWithStore.get("customer_num"));
        mv.addObject("storeName", reservationDetailWithStore.get("store_name"));
        mv.addObject("storeSort", reservationDetailWithStore.get("store_sort"));
        mv.addObject("storeAddress", reservationDetailWithStore.get("store_address"));

        return mv;
    }

    // 예약 취소
    @PostMapping("/reservationCancel")
    public String cancelReservation(@RequestParam("resId") int resId, Model model) {
        try {
            reservationService.cancelReservation(resId);
            model.addAttribute("message", "예약이 성공적으로 취소되었습니다.");
            return "reservation/cancelCompletion";  // 예약 취소 완료 페이지로 이동
        } catch (Exception e) {
            model.addAttribute("message", "예약 취소 중 오류가 발생했습니다.");
            return "reservation/userReservations";  // 예약 취소 페이지로 되돌아가기
        }
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
            List<ScheduleDTO> schedule = reserService.getSchedule(storeId);

            System.out.println("store = " + store);
            System.out.println("schedule = " + schedule);
            if (store != null) {
                System.out.println("store!=null 조건 충족 닿았습니다");
                session.setAttribute("store", store);
                session.setAttribute("schedule", schedule);
            }
            return "redirect:" + defaultUrl + "storeInfo"; // 해당 페이지로 리턴
        }

    }

    // 사업장별 예약 목록 조회
    // Controller 코드 수정
    @GetMapping("/store/reservations/{userId}")
    public String getReservationsByStore(@PathVariable("userId") int userId, Model model) {
        // 사용자의 사업장 ID 조회
        Integer storeId = reservationService.getStoreIdByUserId(userId);

        if (storeId == null) {
            model.addAttribute("error", "해당 사업장을 찾을 수 없습니다.");
            return "redirect:/reservation/schedule/error";
        }

        // 사업장 정보 및 예약 목록 조회
        StoreDTO store = reservationService.getStoreById(storeId);
        List<ReservationDTO> reservations = reservationService.getReservationsByStore(storeId);
        List<ScheduleDTO> schedules = reserService.getSchedule(storeId);

        // 모델에 추가해서 Thymeleaf로 전달
        model.addAttribute("store", store);
        model.addAttribute("reservations", reservations);
        model.addAttribute("schedules", schedules);

        return "reservation/storeReservationList";
    }
}