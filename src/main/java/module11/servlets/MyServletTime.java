package module11.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;

@WebServlet(value = "/time")
public class MyServletTime extends HttpServlet {
    private final String DEFAULT_TIME_ZONE = "UTC";
    private final String TIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss z";
    private TemplateEngine engine;

    @Override
    public void init() throws ServletException {
        engine = new TemplateEngine();

        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix("/WEB-INF/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        engine.addTemplateResolver(resolver);
    }

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=utf-8");
        String timezoneParam = req.getParameter("timezone");
        if (timezoneParam == null || timezoneParam.isEmpty()) {
            timezoneParam = getLastTimezoneFromCookie(req);
        }
        else {
            saveLastTimezoneToCookie(resp,timezoneParam);
        }

        String currentTime = ZonedDateTime
                .now(ZoneId.of(timezoneParam))
                .format(DateTimeFormatter.ofPattern(TIME_FORMAT_PATTERN));

        Context simpleContext = new Context(
                req.getLocale(),
                Map.of("currentTime", currentTime)
        );

        engine.process("time", simpleContext, resp.getWriter());
        resp.getWriter().close();
    }

    private void saveLastTimezoneToCookie(HttpServletResponse response, String timezone) {
        Cookie timezoneCookie = new Cookie("lastTimezone", timezone);
        timezoneCookie.setMaxAge(30 * 24 * 60 * 60);
        response.addCookie(timezoneCookie);
    }

    private String getLastTimezoneFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("lastTimezone".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return DEFAULT_TIME_ZONE;
    }
}
