package com.example.ido.appex2.entities;

import java.util.Date;

public class Review
{

    private String m_ReviewHedaer;
    private String m_ReviewBody;
    private float  m_Rating;
    private String m_UserEmail;
    private String m_UserID;
    private String m_BookID;
    private String m_Date;
    private String m_UserImageUrl;

    public Review() { }

    public Review(String i_ReviewHedaer, String i_ReviewBody, float i_Rating, String i_UserEmail, String i_UserKey, String i_BookID, String i_date, String i_UserImageUrl)
    {
        this.m_ReviewHedaer = i_ReviewHedaer;
        this.m_ReviewBody = i_ReviewBody;
        this.m_Rating = i_Rating;
        this.m_UserEmail = i_UserEmail;
        this.m_UserID = i_UserKey;
        this.m_BookID = i_BookID;
        this.m_Date = i_date;
        this.m_UserImageUrl = i_UserImageUrl;
    }

    public String getBookID()
    {
        return m_BookID;
    }

    public void setBookID(String bookID)
    {
        m_BookID = bookID;
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

    public float getRating()
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

    public String getM_Date() {
        return m_Date;
    }

    public void setM_Date(String m_Date) {
        this.m_Date = m_Date;
    }

    public String getUserImageUrl()
    {
        return m_UserImageUrl;
    }

    public void setUserImageUrl(String userImageUrl)
    {
        m_UserImageUrl = userImageUrl;
    }

}

