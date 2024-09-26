package moyomoyoe.reservation.controller;

import moyomoyoe.reservation.model.dto.ReservationDTO;
import moyomoyoe.reservation.model.dto.ScheduleDTO;
import moyomoyoe.reservation.model.dto.StoreDTO;
import moyomoyoe.reservation.model.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
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

    // 예약 완료 처리
    @PostMapping("/submit")
    public String submitReservation(
            @RequestParam("storeId") int storeId,
            @RequestParam("name") String name,
            @RequestParam("phone") String phone,
            @RequestParam("capacity") int capacity,
            @RequestParam("date") String date,
            @RequestParam("startTime") String startTimeStr,
            @RequestParam("endTime") String endTimeStr,
            RedirectAttributes redirectAttributes) {

        try {
            // 날짜 형식 파싱
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsedDate = dateFormat.parse(date);
            Date sqlDate = new Date(parsedDate.getTime());

            // LocalTime 파싱
            LocalTime startTime = LocalTime.parse(startTimeStr, DateTimeFormatter.ofPattern("H:mm"));
            LocalTime endTime = LocalTime.parse(endTimeStr, DateTimeFormatter.ofPattern("H:mm"));

            // LocalTime을 java.sql.Time으로 변환
            java.sql.Time sqlStartTime = java.sql.Time.valueOf(startTime);
            java.sql.Time sqlEndTime = java.sql.Time.valueOf(endTime);

            // ScheduleDTO 생성 (초기 ID 0)
            ScheduleDTO scheduleDTO = new ScheduleDTO(0, storeId, sqlStartTime, sqlEndTime, capacity);

            // 스케줄 저장 후 생성된 ID를 가져옴
            reservationService.saveSchedule(scheduleDTO);

            // ReservationDTO 생성 및 저장
            ReservationDTO reservationDTO = new ReservationDTO(0, storeId, sqlDate, String.valueOf(capacity), scheduleDTO.getScheduleId());
            reservationService.saveReservation(reservationDTO, scheduleDTO);

            // 성공 메시지 설정 및 페이지 리다이렉트
            redirectAttributes.addFlashAttribute("message", "예약이 성공적으로 처리되었습니다.");
            System.out.println("Redirecting to completion with storeId: " + storeId);
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
        return "redirect:/reservation/";
    }

    // 예약 완료 페이지
    @GetMapping("/completion")
    public String reservationCompletion(@RequestParam("storeId") int storeId, Model model) {
        StoreDTO store = reservationService.getStoreById(storeId);
        model.addAttribute("store", store);
        return "completion";
    }

    @GetMapping("/getReservedTimes")
    @ResponseBody
    public List<String> getReservedTimes(@RequestParam("storeId") int storeId, @RequestParam("date") String date) {
        return reservationService.getReservedTimes(storeId, date);
    }
    // MainController 클래스 존재하여 중복으로 주석처리 추후 제거 예졍
//    @Controller
//    public class MainController {
//
//        @GetMapping("/main")
//        public String mainPage() {
//            return "/static/main"; // main.html 파일을 반환
//        }
//    }

    // 예약 일정 리스트 조회
    @GetMapping("/reservationList")
    public String getReservationList(Model model) {
        List<ScheduleDTO> reservationList = reservationService.getAllReservations();
        model.addAttribute("reservationList", reservationList);
        return "reservation/reservationList";  // reservationList.html 반환
    }

    // 예약 상세 조회
    @GetMapping("/scheduleDetail/{scheduleId}")
    public String getScheduleDetailPage(@PathVariable("scheduleId") int scheduleId, Model model) {
        ScheduleDTO schedule = reservationService.getScheduleById(scheduleId);
        model.addAttribute("schedule", schedule);
        return "reservation/scheduleDetail";  // 상세 정보를 표시할 뷰 파일로 이동
    }

    // 예약 취소
    @PostMapping("/cancelReservation")
    public String cancelReservation(@RequestParam("resId") int resId, Model model) {
        reservationService.cancelReservation(resId);
        model.addAttribute("message", "예약이 취소되었습니다.");
        return "redirect:/scheduleDetail";
    }
}