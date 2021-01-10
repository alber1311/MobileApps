package com.example.languide.api;

import com.example.languide.tests.*;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TestService {
    @GET("api/exercise/5f6c7c6e0c583f06fabc9634")
    Call<ReadingTest> loadReadingTest();
    Call<ListeningTest> loadListeningTest();
    Call<SpeakingTest> loadSpeakingTest();
    Call<VocabularyTest> loadVocabularyTest();
    Call<WritingTest> loadWritingTest();
}
