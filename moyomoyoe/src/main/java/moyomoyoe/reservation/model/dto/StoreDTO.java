package moyomoyoe.reservation.model.dto;

public class StoreDTO {
    private int id;
    private String name;
    private String address;
    private String sort;
    private int businessUserId;
    private String description;

    public StoreDTO() {
    }

    public StoreDTO(String name, String address, String sort, String description) {
        this.name = name;
        this.address = address;
        this.sort = sort;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public int getBusinessUserId() {
        return businessUserId;
    }

    public void setBusinessUserId(int businessUserId) {
        this.businessUserId = businessUserId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Store{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", sort='" + sort + '\'' +
                ", businessUserId=" + businessUserId +
                ", description='" + description + '\'' +
                '}';
    }
}
