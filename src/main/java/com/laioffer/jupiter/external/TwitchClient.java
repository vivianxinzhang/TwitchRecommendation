package com.laioffer.jupiter.external;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laioffer.jupiter.entity.Game;
import com.laioffer.jupiter.entity.Item;
import com.laioffer.jupiter.entity.ItemType;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class TwitchClient {
    private static final String TOKEN = "Bearer mganjf77ttgmc42wdc5qh0qmdnhlc2";
    private static final String CLIENT_ID = "0qlcz30q4un2matqwymexkckckfuob";
    private static final String TOP_GAME_URL = "https://api.twitch.tv/helix/games/top?first=%s";
    private static final String GAME_SEARCH_URL_TEMPLATE = "https://api.twitch.tv/helix/games?name=%s";
    // %s 是template占位符 根据前端传进来的参数会被修改
    private static final int DEFAULT_GAME_LIMIT = 20;
    private static final String STREAM_SEARCH_URL_TEMPLATE = "https://api.twitch.tv/helix/streams?game_id=%s&first=%s";
    private static final String VIDEO_SEARCH_URL_TEMPLATE = "https://api.twitch.tv/helix/videos?game_id=%s&first=%s";
    private static final String CLIP_SEARCH_URL_TEMPLATE = "https://api.twitch.tv/helix/clips?game_id=%s&first=%s";
    private static final String TWITCH_BASE_URL = "https://www.twitch.tv/";
    private static final int DEFAULT_SEARCH_LIMIT = 20;

    // implement buildGameURL function
    private String buildGameURL(String url, String gameName, int limit) {
        if (gameName.equals("")) {
            // top game
            // https://api.twitch.tv/helix/games/top?first=20
            return String.format(url, limit);
        } else {
            try {
                // amont us => among%20us
                gameName = URLEncoder.encode(gameName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            // getGame
            // https://api.twitch.tv/helix/games?name=among%20us
            return String.format(url, gameName);
        }
    }

    // implement buildSearchURL function
    private String buildSearchURL(String url, String gameId, int limit) {
        try {
            gameId = URLEncoder.encode(gameId, "UTF-8");
            //
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return String.format(url, gameId, limit);
    }

    private String searchTwitch(String url) throws TwitchException {
        // httpclient 用来帮助发送请求
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // responseHandler 用来处理请求 此处用了 lambda expression
        // input: response   output:
        ResponseHandler<String> responseHandler = response -> {
            // get response code
            int responseCode = response.getStatusLine().getStatusCode();
            // unsuccessful
            if (responseCode != 200) {
                System.out.println("Response status: " + response.getStatusLine().getReasonPhrase());
                throw new TwitchException("Failed to get result from Twitch API");
            }
            // get response body
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                throw new TwitchException("Failed to get result from Twitch API");
            }
            // 把 response body 的整体内容变成一个 JSONObject
            JSONObject obj = new JSONObject(EntityUtils.toString(entity));
            // 我们需要的信息是 data 这个 key 所对应的 array
            return obj.getJSONArray("data").toString();
        };
        // 用 httpclient 发送请求
        try {
            HttpGet httpGetRequest = new HttpGet(url);
            httpGetRequest.setHeader("Authorization", TOKEN);
            httpGetRequest.setHeader("Client-Id", CLIENT_ID);
            return httpclient.execute(httpGetRequest, responseHandler);
        } catch (IOException e) {
            e.printStackTrace();
            throw new TwitchException("Failed to get result from Twitch API");
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // implement getGameList method to convert Twitch return data to a list of Game objects
    private List<Game> getGameList(String data) throws TwitchException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // 把 JSON 格式的array convert 为Java Object
            // 如果无法一一对应会出现JSONException
            // 后面一个参数（Game[].class）的意思是 convert 成 Game 这个 class 组成的 array
            Game[] games = mapper.readValue(data, Game[].class);
             return Arrays.asList(games);
            // return Arrays.asList(mapper.readValue(data, Game[].class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new TwitchException("Failed to parse game data from Twitch API");
        }
    }

    // 返回当前 popular 的 game
    public List<Game> topGames(int limit) throws TwitchException {
        if (limit <= 0) {
            limit = DEFAULT_GAME_LIMIT;
        }
        // Step 1:
        String url = buildGameURL(TOP_GAME_URL, "", limit);
        // Step 2:
        String responseBody = searchTwitch(url);
        // Step 3:
        return getGameList(responseBody);
        // return getGameList(searchTwitch(buildGameURL(TOP_GAME_URL, "", limit)));
    }

    // 根据 gameName 返回具体内容
    public Game searchGames(String gameName) throws TwitchException {
        List<Game> gameList = getGameList(searchTwitch(buildGameURL(GAME_SEARCH_URL_TEMPLATE, gameName, 0)));
        if (gameList.size() != 0) {
            return gameList.get(0);
        }
        return null;
    }

    private List<Item> getItemList(String data) throws TwitchException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return Arrays.asList(mapper.readValue(data, Item[].class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new TwitchException("Failed to parse item data from Twitch API");
        }
    }

    private List<Item> searchStreams(String gameId, int limit) throws TwitchException {
        List<Item> streams = getItemList(searchTwitch(buildSearchURL(STREAM_SEARCH_URL_TEMPLATE, gameId, limit)));
        for (Item item : streams) {
            item.setType(ItemType.STREAM);
            item.setUrl(TWITCH_BASE_URL + item.getBroadcasterName());
        }
        return streams;
    }


    private List<Item> searchClips(String gameId, int limit) throws TwitchException {
        List<Item> clips = getItemList(searchTwitch(buildSearchURL(CLIP_SEARCH_URL_TEMPLATE, gameId, limit)));
        for (Item item : clips) {
            item.setType(ItemType.CLIP);
        }
        return clips;
    }

    private List<Item> searchVideos(String gameId, int limit) throws TwitchException {
        List<Item> videos = getItemList(searchTwitch(buildSearchURL(VIDEO_SEARCH_URL_TEMPLATE, gameId, limit)));
        for (Item item : videos) {
            item.setType(ItemType.VIDEO);
        }
        return videos;
    }
}
