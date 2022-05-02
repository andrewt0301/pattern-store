package aakrasnov.diploma.client.http;

import aakrasnov.diploma.common.RsBaseDto;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

public interface ExceptionCatcher {

    void runAndSetFail(RsBaseDto res);

    @Slf4j
    class IOCatcher<E extends IOException> implements ExceptionCatcher {
        private final CheckedRunnable<E> runnable;

        public IOCatcher(CheckedRunnable<E> runnable) {
            this.runnable = runnable;
        }

        public void runAndSetFail(RsBaseDto res) {
            try {
                runnable.run();
            } catch (IOException exc) {
                log.error("Failed to convert entity", exc);
                res.setStatus(HttpStatus.SC_BAD_REQUEST);
            } catch (Exception exc) {
                throw new RuntimeException(exc);
            }
        }
    }
}
