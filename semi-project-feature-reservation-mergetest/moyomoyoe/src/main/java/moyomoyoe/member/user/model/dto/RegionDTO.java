package moyomoyoe.member.user.model.dto;

public class RegionDTO {

    private int regionCode;
    private String city;
    private String district;

    public RegionDTO() {
    }

    public RegionDTO(int region_code, String city, String district) {
        this.regionCode = region_code;
        this.city = city;
        this.district = district;
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
                "region_code=" + regionCode +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                '}';
    }
}
