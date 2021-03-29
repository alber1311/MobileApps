package com.example.languide.tests;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SpeakingTest{
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("instructions")
        @Expose
        private String instructions;
        @SerializedName("items")
        @Expose
        private List<com.example.languide.tests.SpeakingTest.Item> items = null;

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
        public List<com.example.languide.tests.SpeakingTest.Item> getItems() {
            return items;
        }

        public void setItems(List<com.example.languide.tests.SpeakingTest.Item> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public String toString() {
            return items.toString();
        }

        public class Item {

            @SerializedName("text")
            @Expose
            private String text;
            @SerializedName("answer")
            @Expose
            private String answer = null;

            public String getText1() {
                return text;
            }

            public void setText1(String text1) {
                this.text = text1;
            }

            public String getAnswer() {
                return answer;
            }

            public void setAnswer(String answer) {
                this.answer = answer;
            }
            @NonNull
            @Override
            public String toString() {
                return answer;
            }
        }
}
