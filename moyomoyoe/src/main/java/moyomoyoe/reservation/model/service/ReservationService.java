package moyomoyoe.reservation.model.service;

import moyomoyoe.reservation.model.dao.ReservationMapper;
import moyomoyoe.reservation.model.dao.ScheduleMapper;
import moyomoyoe.reservation.model.dto.ReservationDTO;
import moyomoyoe.reservation.model.dto.ScheduleDTO;
import moyomoyoe.reservation.model.dto.StoreDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReservationService {
    private final ScheduleMapper dao;
    private final ReservationMapper reservationMapper;

    @Autowired
    public ReservationService(ScheduleMapper dao, ReservationMapper reservationMapper) {
        this.dao = dao;
        this.reservationMapper = reservationMapper;
    }

    @Transactional
    public void saveReservation(ReservationDTO reservationDTO, ScheduleDTO scheduleDTO) {
        // ScheduleDTO에서 일정 ID 가져오기
        int scheduleId = scheduleDTO.getScheduleId(); // 여기서 scheduleId를 가져옴
        int storeId = scheduleDTO.getStoreId();

        // 예약 DTO에 scheduleId 설정
        reservationDTO.setScheduleId(scheduleId); // 여기서 reservationDTO에 scheduleId 설정

        // 스케줄 존재 여부 확인
        Map<String, Object> params = new HashMap<>();
        params.put("scheduleId", scheduleId);
        params.put("storeId", storeId);

        boolean exists = reservationMapper.scheduleExists(params);
        if (!exists) {
            throw new IllegalArgumentException("해당 스케줄은 존재하지 않습니다.");
        }

        // 예약 저장
        reservationMapper.insertReservation(reservationDTO);
    }

    public List<String> getReservedTimes(int storeId, String date) {
        return reservationMapper.getReservedTimes(storeId, date);
    }

    public List<StoreDTO> getAllStores() {
        return reservationMapper.getAllStores();
    }

    public StoreDTO getStoreById(int storeId) {
        return reservationMapper.getStoreById(storeId);
    }


    // 모든 예약 일정 조회
    public List<ScheduleDTO> getAllReservations() {
        return reservationMapper.getAllReservations();
    }

    public ReservationDTO getReservationById(int resId) {
        return reservationMapper.getReservationById(resId);
    }

    // 특정 스케줄 상세 조회
    public ScheduleDTO getScheduleById(int scheduleId) {
        return reservationMapper.getScheduleById(scheduleId);
    }

    // 특정 매장의 일정 조회
    public List<ScheduleDTO> getSchedulesByStoreId(int storeId) {
        return reservationMapper.getSchedulesByStoreId(storeId);
    }

    // 사용자 예약 정보 조회
    public List<ReservationDTO> getUserReservations(int userId) {
        return reservationMapper.getUserReservations(userId);
    }

    // 사용자 예약 목록 조회시 시간 가져오기
    public List<Map<String, Object>> getUserReservationsWithSchedule(int userId) {
        return reservationMapper.getUserReservationsWithSchedule(userId);
    }

    // 상세조회(상점정보+예약정보)
    public Map<String, Object> getReservationDetailWithStore(int resId) {
        return reservationMapper.getReservationDetailWithStore(resId);
    }

    // 사용자의 사업장 ID 조회 메소드 추가
    public Integer getStoreIdByUserId(int userId) {
        return reservationMapper.getStoreIdByUserId(userId);
    }

    public List<ReservationDTO> getReservationsByStore(int storeId) {
        return reservationMapper.getReservationsByStore(storeId);
    }

    // 기타 예약 관련 서비스 로직들 추가
    public void saveReservation(ReservationDTO reservationDTO) {
        reservationMapper.insertReservation(reservationDTO);
    }

    // 예약 취소
    @Transactional
    public void cancelReservation(int resId) {
        reservationMapper.deleteReservation(resId);
    }

    public List<Map<String, Object>> getAllStoresWithImages() {
        List<StoreDTO> stores = reservationMapper.getAllStores();
        List<Map<String, Object>> storeListWithImagePaths = new ArrayList<>();

        String[] possibleExtensions = {".jpg", ".png", ".gif"};
        String basePath = "src/main/resources/static/image/";
        String defaultImagePath = "/static/image/default.jpg";

        for (StoreDTO store : stores) {
            Map<String, Object> storeMap = new HashMap<>();
            storeMap.put("store", store);

            // 이미지 경로 설정
            String imagePath = defaultImagePath;
            Integer imageId = store.getImageId();

            if (imageId != null) {
                for (String extension : possibleExtensions) {
                    File file = new File(basePath + imageId + extension);
                    if (file.exists()) {
                        imagePath = "/static/image/" + imageId + extension;
                        break;
                    }
                }
            }

            storeMap.put("imagePath", imagePath);
            storeListWithImagePaths.add(storeMap);
        }

        return storeListWithImagePaths;
    }

    public String getImageById(int id) {
        return reservationMapper.getImageById(id);
    }

    // 매장 ID로 이미지 경로 조회
    public String getImagePathByStoreId(int storeId) {
        String basePath = "/static/image/";
        String defaultImagePath = "/static/image/097ad46b162e449a91545154fc6d4216.jpg";
        String imagePath = defaultImagePath;

        // DB에서 매장 ID에 해당하는 이미지 ID를 조회하고, 이미지 파일 경로 생성
        Integer imageId = reservationMapper.getImageIdByStoreId(storeId);

        if (imageId != null) {
            String imageName = reservationMapper.getImageById(imageId);
            if (imageName != null && !imageName.isEmpty()) {
                imagePath = imageName;
            }
        }

        return imagePath;
    }

    // 기존의 이미지 경로 생성 로직 사용
    public String getImagePathById(Integer imageId) {
        if (imageId == null) {
            return "/static/image/default.jpg";  // 기본 이미지 경로 설정
        }

        String[] possibleExtensions = {".jpg", ".png", ".gif"};
        String basePath = "src/main/resources/static/image/";

        for (String extension : possibleExtensions) {
            File file = new File(basePath + imageId + extension);
            if (file.exists()) {
                return "/static/image/" + imageId + extension;
            }
        }

        return "/static/image/default.jpg";  // 이미지 파일이 없을 경우 기본 이미지 반환
    }
}
