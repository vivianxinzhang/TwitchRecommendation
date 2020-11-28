package com.laioffer.jupiter.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Game {
    // annotation
    @JsonProperty("name")
    public String name;

    @JsonProperty("developer")
    public String developer;

    @JsonProperty("release_time")
    public String releaseTime;

    @JsonProperty("website")
    public String website;

    @JsonProperty("price")
    public double price;

    public Game(String name, String developer, String releaseTime, String website, double price) {
        this.name = name;
        this.developer = developer;
        this.releaseTime = releaseTime;
        this.website = website;
        this.price = price;
    }


//    private final String name;    // 初始化后不能修改
//    private String developer;   // no modifier, package private
//    private String releaseDate;
//    private float price;
//
//
//    private Game(GameBuilder builder) {     // private constructor
//        this.name = builder.name;
//        this.developer = builder.developer;
//        this.releaseDate = builder.developer;
//        this.price = builder.price;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public String getDeveloper() {
//        return developer;
//    }
//
//    public String getReleaseDate() {
//        return releaseDate;
//    }
//
//    public float getPrice() {
//        return price;
//    }
//
//    public static class GameBuilder {   // GameBuilder使用 不依赖于Game对象 需要是static
//        private String name;
//        private String developer;   // no modifier, package private
//        private String releaseDate;
//        private float price;
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public void setDeveloper(String developer) {
//            this.developer = developer;
//        }
//
//        public void setReleaseDate(String releaseDate) {
//            this.releaseDate = releaseDate;
//        }
//
//        public void setPrice(float price) {
//            this.price = price;
//        }
//
//        public Game build() {
//            return new Game(this);  // pass in GameBuilder object
//        }
//    }
}
// Game game = new Game("Vincent", "laioffer", "2020-11-28", "10");

// Game.GameBuilder builder = new Game.GameBuilder();
// or
// import com.laioffer.jupiter.entity.Game.GameBuilder;
// GameBuilder builder = new GameBuilder();

// builder.setName(" Vincent");
// builder.setDeveloper("laioffer");
// Game game = builder.build();

// Game game = new Game(builder);
