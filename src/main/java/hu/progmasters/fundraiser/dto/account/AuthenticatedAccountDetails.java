package hu.progmasters.fundraiser.dto.account;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.stream.Collectors;

public class AuthenticatedAccountDetails {

    private String email;
    private List<String> accountRoleList;


    public AuthenticatedAccountDetails(UserDetails userDetails) {
        this.email = userDetails.getUsername();
        this.accountRoleList = userDetails.getAuthorities().stream().map(GrantedAuthority::toString).collect(Collectors.toList());
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getAccountRoleList() {
        return accountRoleList;
    }

    public void setAccountRoleList(List<String> accountRoleList) {
        this.accountRoleList = accountRoleList;
    }

}
