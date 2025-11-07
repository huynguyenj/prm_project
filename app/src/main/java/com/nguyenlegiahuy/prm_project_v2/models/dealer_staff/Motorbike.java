package com.nguyenlegiahuy.prm_project_v2.models.dealer_staff;

import java.util.List;

public class Motorbike {
    private int id;
    private String name;
    private double price;
    private String description;
    private String model;
    private String makeFrom;
    private String version;
    private boolean isDeleted;
    private List<Image> images;

    public static class Image {
        private int id;
        private String imageUrl;

        public int getId() { return id; }
        public String getImageUrl() { return imageUrl; }
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }

    public String getModel() {
        return model;
    }

    public String getMakeFrom() {
        return makeFrom;
    }

    public String getVersion() {
        return version;
    }

    public String getDescription() { return description; }
    public List<Image> getImages() { return images; }
}
