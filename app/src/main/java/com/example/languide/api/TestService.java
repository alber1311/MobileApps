package com.example.languide.api;

import com.example.languide.tests.*;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TestService {
    @GET()
    Call<ReadingTest> loadReadingTest();
    Call<ListeningTest> loadListeningTest();
    Call<SpeakingTest> loadSpeakingTest();
    Call<VocabularyTest> loadVocabularyTest();
    Call<WritingTest> loadWritingTest();
}
