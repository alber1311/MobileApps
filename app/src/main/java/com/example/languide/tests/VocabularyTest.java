package com.example.languide.tests;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VocabularyTest{

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("instructions")
    @Expose
    private String instructions;
    @SerializedName("items")
    @Expose
    private List<VocabularyTest.Item> items = null;

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

    public List<VocabularyTest.Item> getItems() {
        return items;
    }

    public void setItems(List<VocabularyTest.Item> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public String toString() {
        return items.toString();
    }

    public class Item {

        @SerializedName("text1")
        @Expose
        private String text1;
        @SerializedName("choice")
        @Expose
        private List<VocabularyTest.Item.Choice> choice = null;
        @SerializedName("text2")
        @Expose
        private String text2;

        public String getText1() {
            return text1;
        }

        public void setText1(String text1) {
            this.text1 = text1;
        }

        public List<VocabularyTest.Item.Choice> getChoices() {
            return choice;
        }

        public void setChoices(List<VocabularyTest.Item.Choice> choices) {
            this.choice = choices;
        }

        public String getText2() {
            return text2;
        }

        public void setText2(String text2) {
            this.text2 = text2;
        }

        @NonNull
        @Override
        public String toString() {
            return text1 + "_____" + text2;
        }

        public class Choice {

            @SerializedName("text")
            @Expose
            private String text;
            @SerializedName("correct")
            @Expose
            private Boolean correct;

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public Boolean getCorrect() {
                return correct;
            }

            public void setCorrect(Boolean correct) {
                this.correct = correct;
            }

        }
    }
}
