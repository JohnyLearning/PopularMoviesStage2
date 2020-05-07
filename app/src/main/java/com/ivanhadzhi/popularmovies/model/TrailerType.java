package com.ivanhadzhi.popularmovies.model;

public enum TrailerType {

    Trailer("Trailer"),
    Teaser("Teaser"),
    Clip("Clip"),
    Featurette("Featurette"),
    OpeningCredits("Opening Credits"),
    BehindTheScenes("Behind the Scenes"),
    Bloopers("Bloopers");

    private String name;

    TrailerType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
