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

        // 하드코딩된 가게 데이터 (테스트용)
        storeMap.put(1, new Store(1, "업체명 1", "업종 1", "주소 1", "09:00 - 21:00", "상세 설명 1", "image-url-1.jpg"));
        storeMap.put(2, new Store(2, "업체명 2", "업종 2", "주소 2", "10:00 - 22:00", "상세 설명 2", "image-url-2.jpg"));
        storeMap.put(3, new Store(3, "업체명 3", "업종 3", "주소 3", "11:00 - 23:00", "상세 설명 3", "image-url-3.jpg"));
    }

    // 매장 목록 페이지로 이동
    @GetMapping("/storeList")
    public String getStoreListPage(Model model) {
        model.addAttribute("stores", new ArrayList<>(storeMap.values()));
        return "reservation/storeList";  // storeList.html이 reservation 폴더 내에 있는지 확인
    }

    // 매장 세부정보 페이지로 이동
    @GetMapping("/storeDetail/{id}")
    public String getStoreDetailPage(@PathVariable("id") int id, Model model) {
        Store store = storeMap.get(id);
        if (store == null) {
            return "error/404";
        }
        model.addAttribute("store", store);
        return "reservation/storeDetailView";
    }

    // 예약 페이지로 이동
    @GetMapping
    public String showReservationPage(@RequestParam("storeId") int storeId, Model model) {
        Store store = storeMap.get(storeId);
        if (store == null) {
            return "error/404";
        }
        model.addAttribute("store", store);
        return "reservation/reservation";
    }

    // 예약 완료 처리
    @PostMapping("/submit")
    public String submitReservation(
            @RequestParam("name") String name,
            @RequestParam("phone") String phone,
            @RequestParam("capacity") String capacity,
            @RequestParam("date") String date,
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime,
            @RequestParam("storeId") int storeId,
            @RequestParam("storeName") String storeName,
            RedirectAttributes redirectAttributes) {

        // 임시로 부여한 사용자 ID (테스트용)
        int userId = 1;

        // ScheduleDTO 생성 및 설정
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setStoreId(storeId);
        scheduleDTO.setStoreName(storeName);
        scheduleDTO.setResDate(date);
        scheduleDTO.setStartTime(startTime);
        scheduleDTO.setEndTime(endTime);
        scheduleDTO.setCapacity(capacity);

        // ReservationDTO 생성 및 설정
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setUserIdRes(userId);
        reservationDTO.setResDate(java.sql.Date.valueOf(date));
        reservationDTO.setCapacity(capacity);

        // 서비스 호출하여 예약 및 일정 저장
        reservationService.saveReservation(reservationDTO, scheduleDTO);

        redirectAttributes.addFlashAttribute("message", "예약이 성공적으로 처리되었습니다.");
        return "redirect:/reservation/completion";
    }

    // 예약 완료 페이지로 이동
    @GetMapping("/completion")
    public String reservationCompletion(@RequestParam(value = "storeId", required = false, defaultValue = "1") int storeId, Model model) {
        Store store = storeMap.get(storeId);
        if (store == null) {
            return "error/404";  // 매장 정보를 찾을 수 없을 때 404 페이지로 이동
        }
        model.addAttribute("store", store);  // store 객체를 모델에 추가
        return "completion";  // completion.html 반환
    }

    // 이미 예약된 시간 가져오기
    @GetMapping("/getReservedTimes")
    @ResponseBody
    public List<String> getReservedTimes(@RequestParam("storeId") int storeId, @RequestParam("date") String date) {
        if (storeId <= 0 || date == null || date.isEmpty()) {
            throw new IllegalArgumentException("Invalid parameters: storeId and date must be provided.");
        }
        return reservationService.getReservedTimes(storeId, date);
    }

    // 예약 일정 리스트 조회 페이지로 이동
//    @GetMapping("/reservationList")
//    public String getReservationListPage(Model model) {
//        return "reservationList";  // reservationList.html 반환
//    }

    @GetMapping("/reservationList")
    public String getReservationListPage(Model model) {
        // 서비스에서 예약 데이터를 조회
        List<ScheduleDTO> reservationList = reservationService.getAllReservations();

        // 예약 리스트가 제대로 조회되었는지 콘솔에 출력 (디버깅용)
        System.out.println("예약 리스트: " + reservationList); // 여기서 예약 리스트 확인

        // 모델에 추가하여 뷰에 전달
        model.addAttribute("reservationList", reservationList);
        return "reservation/reservationList";  // reservationList.html로 연결
    }

    // 예약 일정 리스트 데이터를 반환
//    @GetMapping("/reservationList")
//    public String getReservationList(Model model) {
//        // 예약 데이터를 model에 추가하는 로직 (예시)
//        model.addAttribute("reservations", reservationService.getAllReservations());
//
//        // "reservationList" 템플릿을 반환
//        return "reservation/reservationList";
//    }

    // 홈으로 돌아가기 기능 추가
    @Controller
    public class HomeController {

        @GetMapping("/static/index")
        public String serveStaticIndex() {
            return "redirect:/index.html";  // 정적 파일로 리다이렉트
        }
    }

    // Store 클래스 정의 (예약에 필요한 가게 정보)
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

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public String getAddress() {
            return address;
        }

        public String getHours() {
            return hours;
        }

        public String getDescription() {
            return description;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }
}