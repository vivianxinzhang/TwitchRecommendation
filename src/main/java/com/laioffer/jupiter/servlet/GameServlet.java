package com.laioffer.jupiter.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.json.JSONObject;
import org.apache.commons.io.IOUtils;

@WebServlet(name = "GameServlet", urlPatterns = {"/game"})  // 如果不写urlPattern在url中应该写ClassName：GameServlet
public class GameServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject jsonRequest = new JSONObject(IOUtils.toString(request.getReader()));
        String name = jsonRequest.getString("name");
        String developer = jsonRequest.getString("developer");
        String releaseTime = jsonRequest.getString("release_time");
        String website = jsonRequest.getString("website");
        float price = jsonRequest.getFloat("price");

        System.out.println("Name is: " + name);
        System.out.println("Developer is: " + developer);
        System.out.println("Release time is: " + releaseTime);
        System.out.println("Website is: " + website);
        System.out.println("Price is: " + price);

        response.setContentType("application/json");
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", "ok");
        response.getWriter().print(jsonResponse);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // response.getWriter().print("Hello World!");
        // String gamename = request.getParameter("gamename");
        // response.getWriter().print("Game is: " + gamename);

        response.setContentType("application/json");
        JSONObject game = new JSONObject();
        game.put("name", "World of Warcraft");
        game.put("developer", "Blizzard Entertainment");
        game.put("release_time", "Feb 11, 2005");
        game.put("website", "https://www.worldofwarcraft.com");
        game.put("price", 49.99);
        response.getWriter().print(game);
    }
}
