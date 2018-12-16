package com.example.ido.appex2.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;



public class AudioBook implements Parcelable {

    private String name;
    private String author;
    private String genre;
    private String file;
    private String thumbImage;
    private String description;
    private int price;
    private int rating;
    private int reviewsCount;

    private Map<String,Review> reviews;




    public  AudioBook(){
    }

    public AudioBook(String name, String author, String genre, String file, String thumbImage, String description,
                     int price, int rating, int reviewsCount, Map<String, Review> reviews) {
        this.name = name;
        this.author = author;
        this.genre = genre;
        this.file = file;
        this.thumbImage = thumbImage;
        this.description = description;
        this.price = price;
        this.rating = rating;
        this.reviewsCount = reviewsCount;
        this.reviews = reviews;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() { return genre; }

    public void setGenre(String genre) { this.genre = genre;}

    public String getFile() { return file; }

    public void setFile(String file) {
        this.file = file;
    }

    public String getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(String thumbImage) {
        this.thumbImage = thumbImage;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getReviewsCount() {
        return reviewsCount;
    }

    public void setReviewsCount(int reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

    public Map<String, Review> getReviews() {
        return reviews;
    }

    public void setReviews(Map<String, Review> reviews) {
        this.reviews = reviews;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(author);
        parcel.writeString(genre);
        parcel.writeString(file);
        parcel.writeString(thumbImage);
        parcel.writeString(description);
        parcel.writeInt(price);
        parcel.writeInt(rating);
        parcel.writeInt(reviewsCount);
    }

    private AudioBook(Parcel in){
        this.name = in.readString();
        this.author = in.readString();
        this.genre = in.readString();
        this.file = in.readString();
        this.description = in.readString();
        this.thumbImage = in.readString();
        this.price = in.readInt();
        this.rating = in.readInt();
        this.reviewsCount = in.readInt();
    }

    public static final Parcelable.Creator<AudioBook> CREATOR = new Parcelable.Creator<AudioBook>() {
        @Override
        public AudioBook createFromParcel(Parcel source) {
            return new AudioBook(source);
        }

        @Override
        public AudioBook[] newArray(int size) {
            return new AudioBook[size];
        }
    };



}
