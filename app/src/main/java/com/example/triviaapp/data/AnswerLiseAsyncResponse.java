package com.example.triviaapp.data;

import com.example.triviaapp.Model.Question;

import java.util.ArrayList;
import java.util.List;

public  interface AnswerLiseAsyncResponse {

    public void processFinished(List<Question> questionsArrayList);

}
