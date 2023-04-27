package org.goit.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.temporal.ChronoUnit;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/time")
public class TimeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String timezone = req.getParameter("timezone");

        if (timezone != null) {
            timezone = URLDecoder.decode(timezone, StandardCharsets.UTF_8).replace(" ", "+");
        }

        LocalTime currentTime;
        String timezoneString;

        if (timezone != null && timezone.matches("^UTC[+\\-]\\d{1,2}$")) {
            char sign = timezone.charAt(3);
            int offset = Integer.parseInt(timezone.substring(4));
            if (sign == '-') {
                offset = -offset;
            }
            ZoneOffset zoneOffset = ZoneOffset.ofHours(offset);
            currentTime = LocalTime.now(zoneOffset);
            timezoneString = String.format("UTC%s", timezone.substring(3));
        } else {
            currentTime = LocalTime.now();
            timezoneString = ZoneId.systemDefault().toString();
        }

        String date = LocalDate.now().toString();
        String time = currentTime.truncatedTo(ChronoUnit.SECONDS).toString();
        String response = String.format("Current time: %s, %s, %s", date, time, timezoneString);

        resp.getWriter().write(response);
    }
}

