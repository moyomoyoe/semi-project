package moyomoyoe.member.auth.model.dto;

import moyomoyoe.member.auth.model.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class UserDTO implements UserDetails, Serializable {

    private int id;
    private String username;
    private String account;
    private String password;
    private String nickname;
    private String email;
    private String phone;
    private UserRole userRole;
    private int imageId;
    private int userRegion;
    private String region;
    private String image;
    private String isActive;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(() -> userRole.getRole());

        System.out.println("요구한다. 권한. = " + authorities);

        return authorities;
    }

    @Override
    public String getPassword() {

        System.out.println("요구한다. 비밀번호. = " + this.password);

        return this.password;
    }

    @Override
    public String getUsername() {

        System.out.println("요구한다. 아이디. = " + this.account);

        return this.account;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public UserDTO() {
    }

    public UserDTO(int id, String username, String account, String password, String nickname, String email, String phone, UserRole userRole, int imageId, int userRegion, String isActive) {
        this.id = id;
        this.username = username;
        this.account = account;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.phone = phone;
        this.userRole = userRole;
        this.imageId = imageId;
        this.userRegion = userRegion;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.username;
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

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", userRole=" + userRole +
                ", imageId=" + imageId +
                ", userRegion=" + userRegion +
                ", region=" + region +
                ", imageName=" + image +
                ", isActive=" + isActive +
                '}';
    }
}
