package moyomoyoe.member.user.model.dto;

public class FindPwdDTO {

    private String account;
    private String password;

    public FindPwdDTO() {
    }

    public FindPwdDTO(String account) {
        this.account = account;
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

    @Override
    public String toString() {
        return "FindPwdDTO{" +
                "account='" + account + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
