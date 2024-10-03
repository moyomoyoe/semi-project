package moyomoyoe.reservation.controller;

import jakarta.servlet.http.HttpSession;
import moyomoyoe.member.auth.model.dto.UserDTO;
import moyomoyoe.reservation.model.dao.ReservationMapper;
import moyomoyoe.reservation.model.dto.ReservationDTO;
import moyomoyoe.reservation.model.dto.ScheduleDTO;
import moyomoyoe.reservation.model.dto.StoreDTO;
import moyomoyoe.reservation.model.service.ReservationService;
import moyomoyoe.reservation.model.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    ReservationService reservationService;
    ScheduleService reserService;
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
        model.addAttribute("stores", stores);
        return "reservation/storeList";
    }

    // 매장 상세 조회
    @GetMapping("/storeDetail/{id}")
    public String getStoreDetailPage(@PathVariable("id") int id, Model model) {
        StoreDTO store = reservationService.getStoreById(id);
        model.addAttribute("store", store);
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
            ReservationDTO reservationDTO = new ReservationDTO(0, userDTO.getId(), sqlDate, capacity, 0,"");

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
        //List<ReservationDTO> userReservations = reservationService.getUserReservations(userId);

        List<Map<String,String>> reservation = reserService.getUserFullReserInfo(userId);

        //session.setAttribute("userReservations", userReservations);
        //mv.addObject("userReservations", userReservations);
        mv.addObject("reservations", reservation);

        mv.addObject("user", userDTO);
        return mv;
    }

    // 예약 상세 조회
    @GetMapping("/reservationDetail/{resId}")
    public ModelAndView getReservationDetail(@PathVariable("resId") int resId) {
        ModelAndView mv = new ModelAndView("reservation/reservationDetail");

        Map<String, Object> reservationDetailWithStore = reservationService.getReservationDetailWithStore(resId);

        if (reservationDetailWithStore == null || reservationDetailWithStore.isEmpty()) {
            mv.addObject("error", "해당 예약을 찾을 수 없습니다.");
            return mv;
        }

        mv.addObject("reservation", reservationDetailWithStore);  // Map 전체를 모델에 추가

        return mv;
    }

    // 예약 목록 조회
    @GetMapping("/reservationList")
    public String getReservationList(Model model) {
        List<ScheduleDTO> reservationList = reservationService.getAllReservations();
        model.addAttribute("reservationList", reservationList);
        return "reservation/reservationList";
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

    @GetMapping("/storeInfo")
    public String getStoreInfoFromSession(HttpSession session, Model model) {
        // 세션에 저장된 데이터를 가져옴
        StoreDTO storeInfo = (StoreDTO) session.getAttribute("store");
        List<ScheduleDTO> schedule = (List<ScheduleDTO>) session.getAttribute("schedule");

        // 모델에 추가해서 Thymeleaf로 전달
        model.addAttribute("store", storeInfo);
        model.addAttribute("schedule", schedule);

        return defaultUrl+"storeInfo";
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