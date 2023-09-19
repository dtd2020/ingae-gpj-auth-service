package mz.gov.inage.authservice.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class UserSecurityHolder {

    public static Long  getUserId() {
       var authentication= SecurityContextHolder.getContext()
                .getAuthentication();
        if(authentication!=null && authentication.isAuthenticated()){
            String userId = (String) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();

            return Long.parseLong(userId);
        }

        return null;
    }
}
