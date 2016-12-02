package ua.com.juja.sqlcmd.controller.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = getAction(req);

        if (action.equals("/menu")) {
            req.getRequestDispatcher("menu.jsp").forward(req, resp);
        } else if (action.equals("/help")) {
            req.getRequestDispatcher("help.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(req.getParameterMap().toString());
    }

    private String getAction(HttpServletRequest req) {
        String requestURI = req.getRequestURI();
        String action = requestURI.substring(req.getContextPath().length(), requestURI.length());
        return action;
    }
}
