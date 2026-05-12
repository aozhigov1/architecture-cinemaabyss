package com.cinemaabyss.events.model;

public class MovieEvent extends Event
{
    private Integer movieId;
    private String title;
    private String action;
    private Integer userId;
    private Double rating;
    private String[] genres;
    private String description;

    public MovieEvent(Integer movieId, String title, String action, Integer userId, Double rating, String[] genres,
            String description)
    {
        super();
        this.movieId = movieId;
        this.title = title;
        this.action = action;
        this.userId = userId;
        this.rating = rating;
        this.genres = genres;
        this.description = description;
        this.id = generateId("movie", movieId);
        this.eventType = "movie";
        this.timestamp = java.time.LocalDateTime.now();
    }

    public MovieEvent()
    {
        super();
    }

    public Integer getMovieId()
    {
        return movieId;
    }

    public void setMovieId(Integer movieId)
    {
        this.movieId = movieId;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getAction()
    {
        return action;
    }

    public void setAction(String action)
    {
        this.action = action;
    }

    public Integer getUserId()
    {
        return userId;
    }

    public void setUserId(Integer userId)
    {
        this.userId = userId;
    }

    public Double getRating()
    {
        return rating;
    }

    public void setRating(Double rating)
    {
        this.rating = rating;
    }

    public String[] getGenres()
    {
        return genres;
    }

    public void setGenres(String[] genres)
    {
        this.genres = genres;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}
