package guru.sfg.brewery.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class UrlParametersAuthFilter extends AbstractCustomFilter {

    public UrlParametersAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    protected String getPassword(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getParameter("password");
    }

    @Override
    protected String getUserName(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getParameter("username");
    }
}
