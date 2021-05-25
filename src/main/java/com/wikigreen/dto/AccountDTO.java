package com.wikigreen.dto;

import com.wikigreen.model.AccountStatus;

import java.util.Objects;

public class AccountDTO{
    private Long id;
    private UserDTO user;
    private String email;
    private String nickName;
    private AccountStatus accountStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUserDTO(UserDTO user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public AccountDTO(Long id, UserDTO user, String email, String nickName, AccountStatus accountStatus) {
        this.id = id;
        this.user = user;
        this.email = email;
        this.nickName = nickName;
        this.accountStatus = accountStatus;
    }

    public AccountDTO(UserDTO user, String email, String nickName, AccountStatus accountStatus) {
        this.user = user;
        this.email = email;
        this.nickName = nickName;
        this.accountStatus = accountStatus;
    }

    public AccountDTO() {}

    public AccountDTO(AccountDTO accountDTO){
        this.id = accountDTO.id;
        this.user = accountDTO.user;
        this.email = accountDTO.email;
        this.nickName = accountDTO.nickName;
        this.accountStatus = accountDTO.accountStatus;
    }

    @Override
    public String toString() {
        return "AccountDTO{" +
                "id=" + id +
                ", userID=" + user +
                ", email='" + email + '\'' +
                ", nickName='" + nickName + '\'' +
                ", accountStatus=" + accountStatus +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountDTO that = (AccountDTO) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(user, that.user)) return false;
        if (!Objects.equals(email, that.email)) return false;
        if (!Objects.equals(nickName, that.nickName)) return false;
        return accountStatus == that.accountStatus;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (nickName != null ? nickName.hashCode() : 0);
        result = 31 * result + (accountStatus != null ? accountStatus.hashCode() : 0);
        return result;
    }
}
