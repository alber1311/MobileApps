package com.example.languide.tests;

import java.util.List;

public class ReadingTest {
    private String title;
    private String instructions;
    private List<Items> items;

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

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }

    private class Items{
        private String text;
        private List<Choices> choices;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public List<Choices> getChoices() {
            return choices;
        }

        public void setChoices(List<Choices> choices) {
            this.choices = choices;
        }

        private class Choices {
            private String text;
            private boolean correct;
            private String feedback;

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public boolean isCorrect() {
                return correct;
            }

            public void setCorrect(boolean correct) {
                this.correct = correct;
            }

            public String getFeedback() {
                return feedback;
            }

            public void setFeedback(String feedback) {
                this.feedback = feedback;
            }
        }
    }
}
