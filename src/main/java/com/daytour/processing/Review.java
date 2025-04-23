package com.daytour.processing;

/**
 * Object to hold review objects.
 *
 * @author  Andri Fannar Kristjánsson, afk6@hi.is.
 * @author  Ástríður Haraldsdóttir Passauer, ahp9@hi.is.
 * @since   2023-02-01
 * @version 1.1
 **/
public class Review
{

    //Variables.
    private final String name, title, review;
    private final boolean liked;

    /**
     * Creates a new Review.
     *
     * @param name   The name of the reviewer.
     * @param title  The title of the review.
     * @param review The review itself.
     * @param liked  If the reviewer like the reviewed object.
     */
    public Review(String name, String title, String review, boolean liked){
        this.name = name;
        this.title = title;
        this.review = review;
        this.liked = liked;
    }
    
    public String getTitle()    { return this.title; }
    
    public String getName()     { return this.name; }
    
    public String getReview()   { return this.review; }
    
    public boolean getLiked()   { return this.liked; }
}
