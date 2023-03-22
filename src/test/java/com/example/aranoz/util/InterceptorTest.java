import com.example.aranoz.util.Interceptor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

public class InterceptorTest {

    @Test
    public void testPreHandle() throws Exception {
        Interceptor interceptor = new Interceptor();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        Object handler = new Object();

        boolean result = interceptor.preHandle(request, response, handler);

        assertEquals(true, result);
        assertEquals(System.currentTimeMillis(), request.getAttribute("startTime"));
        verify(Interceptor.logger).info(Mockito.anyString());
    }

    @Test
    public void testPostHandle() throws Exception {
        Interceptor interceptor = new Interceptor();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        Object handler = new Object();
        ModelAndView modelAndView = new ModelAndView();

        interceptor.postHandle(request, response, handler, modelAndView);

        verify(Interceptor.logger).info(Mockito.anyString());
    }

    @Test
    public void testAfterCompletion() throws Exception {
        Interceptor interceptor = new Interceptor();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        Object handler = new Object();
        Exception ex = new Exception();
        request.setAttribute("startTime", System.currentTimeMillis());

        interceptor.afterCompletion(request, response, handler, ex);

        verify(Interceptor.logger, Mockito.times(2)).info(Mockito.anyString());
    }
}