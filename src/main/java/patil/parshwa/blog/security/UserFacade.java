package patil.parshwa.blog.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import patil.parshwa.blog.models.User;

@Component
public class UserFacade {
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return null;

        return (User) auth.getPrincipal(); // cast to your UserDetails implementation
    }
}

