package com.example.languide.tests;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WritingTest{
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("instructions")
    @Expose
    private String instructions;
    @SerializedName("items")
    @Expose
    private List<WritingTest.Item> items = null;

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

    public List<WritingTest.Item> getItems() {
        return items;
    }

    public void setItems(List<WritingTest.Item> items) {
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
        @SerializedName("word")
        @Expose
        private String word;
        @SerializedName("answer")
        @Expose
        private String answer;
        @SerializedName("text2")
        @Expose
        private String text2;

        public String getText1() {
            return text1;
        }

        public void setText1(String text1) {
            this.text1 = text1;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
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


    }
}
