package moyomoyoe.member.user.model.dto;

public class FindIdDTO {

    private String account;

    public FindIdDTO() {
    }

    public FindIdDTO(String account) {
        this.account = account;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "FindIdDTO{" +
                "account='" + account + '\'' +
                '}';
    }
}
