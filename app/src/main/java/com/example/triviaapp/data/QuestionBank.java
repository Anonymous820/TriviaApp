package com.example.triviaapp.data;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.triviaapp.Model.Question;
import com.example.triviaapp.controller.AppController;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class QuestionBank {
    private String url="https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements.json";
    private ArrayList<Question> arrayList=new ArrayList<>();


    public List<Question> getQuestions(final AnswerLiseAsyncResponse callback)
        {


    JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response) {

            for (int i = 0; i < response.length(); i++) {

                try {
                    Question question=new Question();

                    question.setAnswer(response.getJSONArray(i).getString(0));
                    question.setAnswertrue(response.getJSONArray(i).getBoolean(1));
                    arrayList.add(question);
                   // Log.d("Tokes", "onResponse: "+response.getJSONArray(i).getString(0));
                   // Log.d("Tokes", "onResponse: "+response.getJSONArray(i).getBoolean(1));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (callback!=null)
                callback.processFinished(arrayList);


        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

        }
    });

    AppController.getInstance().addToRequestQueue(jsonArrayRequest);




    return arrayList;
}



}


