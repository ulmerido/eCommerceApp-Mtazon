package com.example.ido.appex2.entities;

public class Review
{

    private String m_ReviewHedaer;
    private String m_ReviewBody;
    private int m_Rating;
    private String m_UserEmail;
    private String m_UserID;
    private String m_BookID;

    public String getBookID()
    {
        return m_BookID;
    }

    public void setBookID(String bookID)
    {
        m_BookID = bookID;
    }

    public Review(String i_ReviewHedaer, String i_ReviewBody, int i_Rating, String i_UserEmail, String i_UserKey, String i_BookID)
    {
        this.m_ReviewHedaer = i_ReviewHedaer;
        this.m_ReviewBody = i_ReviewBody;
        this.m_Rating = i_Rating;
        this.m_UserEmail = i_UserEmail;
        this.m_UserID = i_UserKey;
        this.m_BookID =i_BookID;
    }

    public Review()
    {

    }

    public String getReviewHedaer()
    {
        return m_ReviewHedaer;
    }

    public void setReviewHedaer(String i_ReviewHedaer)
    {
        this.m_ReviewHedaer = i_ReviewHedaer;
    }

    public String getReviewBody()
    {
        return m_ReviewBody;
    }

    public void setReviewBody(String i_ReviewBody)
    {
        this.m_ReviewBody = i_ReviewBody;
    }

    public int getRating()
    {
        return m_Rating;
    }

    public void setRating(int i_Rating)
    {
        this.m_Rating = i_Rating;
    }

    public String getUserEmail()
    {
        return m_UserEmail;
    }

    public void setUserEmail(String i_UserEmail)
    {
        this.m_UserEmail = i_UserEmail;
    }

    public String getUserKey()
    {
        return m_UserID;
    }

    public void setUserKey(String i_UserKey)
    {
        this.m_UserID = i_UserKey;
    }

}

