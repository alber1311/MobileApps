package com.example.languide.tests;

import java.util.List;

public class ListeningTest extends Test{
    private String audio;
    private List<Items> items;


    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }

    private class Items {
        private String text;
        private String gap;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getGap() {
            return gap;
        }

        public void setGap(String gap) {
            this.gap = gap;
        }
    }
}
