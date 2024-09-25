package moyomoyoe.reservation.controller;

import moyomoyoe.reservation.model.dto.ReservationDTO;
import moyomoyoe.reservation.model.dto.ScheduleDTO;
import moyomoyoe.reservation.model.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    private Map<Integer, Store> storeMap = new HashMap<>();

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;

        // 임시로 만든 업체 데이터
        storeMap.put(1, new Store(1, "업체명 1", "업종 1", "주소 1", "09:00 - 21:00", "상세 설명 1", "image-url-1.jpg"));
        storeMap.put(2, new Store(2, "업체명 2", "업종 2", "주소 2", "10:00 - 22:00", "상세 설명 2", "image-url-2.jpg"));
        storeMap.put(3, new Store(3, "업체명 3", "업종 3", "주소 3", "11:00 - 23:00", "상세 설명 3", "image-url-3.jpg"));
    }

    // 업체리스트 페이지로 이동
    @GetMapping("/storeList")
    public String getStoreListPage(Model model) {
        model.addAttribute("stores", new ArrayList<>(storeMap.values()));
        return "reservation/storeList";
    }

    // 업체 페이지 예약할 수 있는 페이지로 이동
    @GetMapping("/storeDetail/{id}")
    public String getStoreDetailPage(@PathVariable("id") int id, Model model) {
        Store store = storeMap.get(id);
        if (store == null) {
            return null;
        }
        model.addAttribute("store", store);
        return "reservation/storeDetailView";
    }

    // 예약하기 페이지로 이동
    @GetMapping
    public String showReservationPage(@RequestParam("storeId") int storeId, Model model) {
        Store store = storeMap.get(storeId);
        if (store == null) {
            return null;
        }
        model.addAttribute("store", store);
        return "reservation/reservation";
    }

    // 예약하기 제출 부분 수정
    @PostMapping("/submit")
    public String submitReservation(
            @RequestParam("storeId") int storeId,
            @RequestParam(value = "storeName", required = false) String storeName,
            @RequestParam("name") String name,
            @RequestParam("phone") String phone,
            @RequestParam("capacity") String capacity,
            @RequestParam("date") String date,
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime,
            RedirectAttributes redirectAttributes) {

        Store store = storeMap.get(storeId);

        // storeId가 storeMap에 없는 경우 오류 처리
        if (store == null) {
            redirectAttributes.addFlashAttribute("error", "유효하지 않은 가게입니다.");
            return "redirect:/error/404";
        }

        // ScheduleDTO 설정
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setStoreId(storeId);
        scheduleDTO.setStoreName(store.getName());  // storeName 설정
        scheduleDTO.setResDate(date);
        scheduleDTO.setStartTime(startTime);
        scheduleDTO.setEndTime(endTime);
        scheduleDTO.setCapacity(capacity);

        // ReservationDTO 설정
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setUserIdRes(1); // 임시 사용자 ID
        reservationDTO.setResDate(java.sql.Date.valueOf(date));
        reservationDTO.setCapacity(capacity);

        try {
            // 예약 및 일정 저장
            reservationService.saveReservation(reservationDTO, scheduleDTO);
            redirectAttributes.addFlashAttribute("message", "예약이 성공적으로 처리되었습니다.");
            return "redirect:/reservation/completion?storeId=" + storeId;
        } catch (Exception e) {
            // DB 제약 조건 위반 또는 기타 오류 처리?
            redirectAttributes.addFlashAttribute("error", "예약 처리 중 오류가 발생했습니다.");
            return "redirect:/reservation";
        }
    }

    // 예약완료 페이지 이동
    @GetMapping("/completion")
    public String reservationCompletion(@RequestParam("storeId") int storeId, Model model) {
        Store store = storeMap.get(storeId);
        if (store == null) {
            return null;
        }
        model.addAttribute("store", store);
        return "completion";
    }

    // 예약된 시간, 업체 가져오기
    @GetMapping("/getReservedTimes")
    @ResponseBody
    public List<String> getReservedTimes(@RequestParam("storeId") int storeId, @RequestParam("date") String date) {
        if (storeId <= 0 || date == null || date.isEmpty()) {
            throw new IllegalArgumentException("Invalid parameters: storeId and date must be provided.");
        }
        return reservationService.getReservedTimes(storeId, date);
    }

    // 예약리스트 페이지 이동
    @GetMapping("/reservationList")
    public String getReservationListPage(Model model) {
        List<ScheduleDTO> reservationList = reservationService.getAllReservations();
        model.addAttribute("reservationList", reservationList);
        return "reservation/reservationList";
    }

    // 404 Page
    @GetMapping("/error/404")
    public String notFoundPage() {
        return "error/404";
    }

    // 처음으로 돌아가기 페이지 이동
    @Controller
    public class HomeController {
        @GetMapping("/static/index")
        public String serveStaticIndex() {
            return "redirect:/index.html";
        }
    }

    // Store Class
    public static class Store {
        private int id;
        private String name;
        private String type;
        private String address;
        private String hours;
        private String description;
        private String imageUrl;

        public Store(int id, String name, String type, String address, String hours, String description, String imageUrl) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.address = address;
            this.hours = hours;
            this.description = description;
            this.imageUrl = imageUrl;
        }

        // Getters
        public int getId() { return id; }
        public String getName() { return name; }
        public String getType() { return type; }
        public String getAddress() { return address; }
        public String getHours() { return hours; }
        public String getDescription() { return description; }
        public String getImageUrl() { return imageUrl; }
    }
}