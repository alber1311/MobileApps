package com.example.languide.tests;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListeningTest {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("instructions")
    @Expose
    private String instructions;
    @SerializedName("audio")
    @Expose
    private String audio;
    @SerializedName("items")
    @Expose
    private List<Item> items = null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return items.toString();
    }

    public class Item {

        @SerializedName("text")
        @Expose
        private String text;
        @SerializedName("gap")
        @Expose
        private String gap;

        public String getText() {
            return text;
        }

        public void setText(String text1) {
            this.text = text1;
        }

        public String getGap() {
            return gap;
        }

        public void setGap(String gap) {
            this.gap = gap;
        }

        @NonNull
        @Override
        public String toString() {
            return gap;
        }

    }
}
