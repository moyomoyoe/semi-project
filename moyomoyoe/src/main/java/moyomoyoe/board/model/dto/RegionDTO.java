package moyomoyoe.board.model.dto;

public class RegionDTO {

    private int regionCode;
    private String city;
    private String district;

    public RegionDTO() {
    }

    public int getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(int regionCode) {
        this.regionCode = regionCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Override
    public String toString() {
        return "RegionDTO{" +
                "regionCode=" + regionCode +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                '}';
    }
}
