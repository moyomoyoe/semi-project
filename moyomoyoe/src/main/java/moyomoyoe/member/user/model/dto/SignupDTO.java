package moyomoyoe.member.user.model.dto;

public class SignupDTO {

    private String username;
    private String account;
    private String password;
    private String nickname;
    private String email;
    private String phone;
    private String userRole;
    private int imageId;
    private int userRegion;

    public SignupDTO() {
    }

    public SignupDTO(String username, String account, String password, String nickname, String email, String phone, String userRole, int imageId, int userRegion) {
        this.username = username;
        this.account = account;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.phone = phone;
        this.userRole = userRole;
        this.imageId = imageId;
        this.userRegion = userRegion;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getUserRegion() {
        return userRegion;
    }

    public void setUserRegion(int userRegion) {
        this.userRegion = userRegion;
    }

    @Override
    public String toString() {
        return "SignupDTO{" +
                "username='" + username + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", userRole='" + userRole + '\'' +
                ", imageId=" + imageId +
                ", userRegion=" + userRegion +
                '}';
    }
}
