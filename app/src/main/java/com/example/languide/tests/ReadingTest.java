package com.example.languide.tests;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReadingTest {
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("instructions")
        @Expose
        private String instructions;
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

        public List<Item> getItems() {
            return items;
        }

        public void setItems(List<Item> items) {
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
            private List<Choice> choice = null;
            @SerializedName("text2")
            @Expose
            private String text2;

            public String getText1() {
                return text1;
            }

            public void setText1(String text1) {
                this.text1 = text1;
            }

            public List<Choice> getChoices() {
                return choice;
            }

            public void setChoices(List<Choice> choices) {
                this.choice = choice;
            }

            public String getText2() {
                return text2;
            }

            public void setText2(String text2) {
                this.text2 = text2;
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

            @Override
            public String toString() {
                return text1 + "_____" + text2;
            }
        }
}
