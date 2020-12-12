package com.laioffer.jupiter.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laioffer.jupiter.external.TwitchClient;
import com.laioffer.jupiter.external.TwitchException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

// urlPatterns = {"/search"} convention, url里一般都是小写字母
@WebServlet(name = "SearchServlet", urlPatterns = {"/search"})
public class SearchServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String gameId = request.getParameter("game_id");
        if (gameId == null) {
            // 400 response
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        TwitchClient client = new TwitchClient();
        try {
            // Method 1:
            // response.setContentType("application/json;charset=UTF-8");
            // response.getWriter().print(new ObjectMapper().writeValueAsString(client.searchItems(gameId)));
            // (client.searchItems(gameId) 返回的 也是 itemMap)
            ServletUtil.writeItemMap(response, client.searchItems(gameId));
        } catch (TwitchException e) {
            throw new ServletException(e);
        }
    }
}
