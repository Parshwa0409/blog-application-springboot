package patil.parshwa.blog.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class WebSecurityConfig {
    //1. Request comes in (with username/password, token, etc.)

    //2. SecurityFilterChain intercepts the request
    // Controllers are NOT called yet

    //3. Filter creates an Authentication object (unauthenticated)
    // username + credentials only

    //4. Filter calls AuthenticationManager.authenticate()

    //5. AuthenticationManager loops through AuthenticationProviders
    // "Can you authenticate this?"

    //6. Matching AuthenticationProvider is found
    // e.g. DaoAuthenticationProvider

    //7. Provider calls UserDetailsService.loadUserByUsername()
    // Fetch user from DB / in-memory / anywhere

    //8. UserDetails is returned
    // username, password(hash), roles, flags

    //9. Provider checks password using PasswordEncoder
    // raw password vs stored hash

    //10. If valid → Authentication marked as authenticated

    //11. Authentication stored in SecurityContext
    // User is now trusted for this request

    //12. Request continues to Controller

    // For InMemory Authentication, we need the UserDetails. And we need to set up the UserDetailsService which returns the UserDetails object.
    @Bean
    UserDetailsService userDetailsService() {
        UserDetails user = org.springframework.security.core.userdetails.User.withDefaultPasswordEncoder()
                .username("admin")
                .password("password")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user);
    }

}
