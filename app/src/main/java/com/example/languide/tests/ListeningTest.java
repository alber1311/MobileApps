package com.example.languide.tests;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListeningTest {
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
        private Exercise exercise;
        @SerializedName("exercise_instances")
        @Expose
        private List<ExerciseInstance> exerciseInstances = null;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Exercise getExercise() {
            return exercise;
        }

        public void setExercise(Exercise exercise) {
            this.exercise = exercise;
        }

        public List<ExerciseInstance> getExerciseInstances() {
            return exerciseInstances;
        }

        public void setExerciseInstances(List<ExerciseInstance> exerciseInstances) {
            this.exerciseInstances = exerciseInstances;
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
            @SerializedName("audio")
            @Expose
            private String audio;
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
                    return text;
                }

            }
        }

        public class ExerciseInstance {

            @SerializedName("status")
            @Expose
            private String status;
            @SerializedName("_id")
            @Expose
            private String id;
            @SerializedName("exercise")
            @Expose
            private String exercise;
            @SerializedName("score")
            @Expose
            private String score;
            @SerializedName("date_of_resolution")
            @Expose
            private String dateOfResolution;
            @SerializedName("__v")
            @Expose
            private Integer v;

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getExercise() {
                return exercise;
            }

            public void setExercise(String exercise) {
                this.exercise = exercise;
            }

            public String getScore() {
                return score;
            }

            public void setScore(String score) {
                this.score = score;
            }

            public String getDateOfResolution() {
                return dateOfResolution;
            }

            public void setDateOfResolution(String dateOfResolution) {
                this.dateOfResolution = dateOfResolution;
            }

            public Integer getV() {
                return v;
            }

            public void setV(Integer v) {
                this.v = v;
            }

        }
    }
}
