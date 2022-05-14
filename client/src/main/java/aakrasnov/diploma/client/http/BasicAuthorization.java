package aakrasnov.diploma.client.http;

import aakrasnov.diploma.client.domain.User;
import aakrasnov.diploma.common.RsBaseDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.auth.BasicScheme;

@Slf4j
public class BasicAuthorization {

    private final HttpRequestBase rq;

    private final RsBaseDto res;

    public BasicAuthorization(final HttpRequestBase rq, final RsBaseDto res) {
        this.rq = rq;
        this.res = res;
    }

    public void add(User user) {
        UsernamePasswordCredentials creds;
        creds = new UsernamePasswordCredentials(user.getUsername(), user.getPassword());
        try {
            rq.addHeader(new BasicScheme().authenticate(creds, rq, null));
        } catch (AuthenticationException exc) {
            String err = String.format(
                "Unable to generate basic scheme header for user '%s'", user
            );
            log.error(err, exc);
            res.setStatus(HttpStatus.SC_UNAUTHORIZED);
            res.setMsg(err);
        }
    }
}
