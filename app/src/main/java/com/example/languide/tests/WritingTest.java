package com.example.languide.tests;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WritingTest{
    private String status;
    private String message;
    private Data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @NonNull
    @Override
    public String toString() {
        return data.getExercise().getItems().toString();
    }

    public class Data {

        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("exercise")
        @Expose
        private VocabularyTest.Data.Exercise exercise;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public VocabularyTest.Data.Exercise getExercise() {
            return exercise;
        }

        public void setExercise(VocabularyTest.Data.Exercise exercise) {
            this.exercise = exercise;
        }


        @NonNull
        @Override
        public String toString() {
            return exercise.getItems().toString();
        }

        public class Exercise {

            @SerializedName("title")
            @Expose
            private String title;
            @SerializedName("instructions")
            @Expose
            private String instructions;
            @SerializedName("random")
            @Expose
            private Boolean random;
            @SerializedName("randomiseChoices")
            @Expose
            private Boolean randomiseChoices;
            @SerializedName("showQuestionNumbers")
            @Expose
            private Boolean showQuestionNumbers;
            @SerializedName("sectionsOnSamePage")
            @Expose
            private Boolean sectionsOnSamePage;
            @SerializedName("items")
            @Expose
            private List<VocabularyTest.Data.Exercise.Item> items = null;

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

            public Boolean getRandom() {
                return random;
            }

            public void setRandom(Boolean random) {
                this.random = random;
            }

            public Boolean getRandomiseChoices() {
                return randomiseChoices;
            }

            public void setRandomiseChoices(Boolean randomiseChoices) {
                this.randomiseChoices = randomiseChoices;
            }

            public Boolean getShowQuestionNumbers() {
                return showQuestionNumbers;
            }

            public void setShowQuestionNumbers(Boolean showQuestionNumbers) {
                this.showQuestionNumbers = showQuestionNumbers;
            }

            public Boolean getSectionsOnSamePage() {
                return sectionsOnSamePage;
            }

            public void setSectionsOnSamePage(Boolean sectionsOnSamePage) {
                this.sectionsOnSamePage = sectionsOnSamePage;
            }

            public List<VocabularyTest.Data.Exercise.Item> getItems() {
                return items;
            }

            public void setItems(List<VocabularyTest.Data.Exercise.Item> items) {
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
                @SerializedName("choices")
                @Expose
                private List<VocabularyTest.Data.Exercise.Item.Choice> choices = null;
                @SerializedName("text2")
                @Expose
                private String text2;

                public String getText1() {
                    return text1;
                }

                public void setText1(String text1) {
                    this.text1 = text1;
                }

                public List<VocabularyTest.Data.Exercise.Item.Choice> getChoices() {
                    return choices;
                }

                public void setChoices(List<VocabularyTest.Data.Exercise.Item.Choice> choices) {
                    this.choices = choices;
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
    }
}
