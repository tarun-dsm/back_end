package toyproject.syxxn.back_end.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import toyproject.syxxn.back_end.exception.handler.AuthenticationEntryPointImpl;
import toyproject.syxxn.back_end.security.jwt.JwtTokenProvider;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationEntryPointImpl authenticationEntryPoint;
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/account").permitAll()
                .antMatchers(HttpMethod.GET, "/account/email/**").permitAll()
                .antMatchers(HttpMethod.GET, "/account/nickname/**").permitAll()
                .antMatchers("/email").permitAll()
                .antMatchers("/auth").permitAll()
                .anyRequest().authenticated()
                .and().httpBasic().authenticationEntryPoint(authenticationEntryPoint)
                .and().apply(new FilterConfig(jwtTokenProvider));
    }

}
