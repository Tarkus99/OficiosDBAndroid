package com.example.myrecyclerviewexample.model;

public class ImagenRecibida {
    private String encode;
    private String image;

    public ImagenRecibida(String e, String i){
        encode = e;
        image = i;
    }

    public String getEncode() {
        return encode;
    }

    public String getImage() {
        return image;
    }
}
