package com.example.stestingknugui.service;

import com.example.stestingknugui.model.Answer;
import com.example.stestingknugui.model.Question;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class QuestionsService {
    private final HttpClient client = HttpClient.newHttpClient();

    public List<Question> getQuestionsByTestId(int testId, String jwtToken) throws Exception {
        String url = "http://localhost:8087/api/question/testing/" + testId;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + jwtToken) // Передача токена
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONArray array = new JSONArray(response.body());
        List<Question> questions = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            Question question = new Question();
            question.idProperty().set(obj.getInt("id"));
            question.questionTextProperty().set(obj.getString("questionText"));
            questions.add(question);
        }
        return questions;
    }

    public List<Answer> getAnswersByQuestionId(int questionId, String jwtToken) throws Exception {
        String url = "http://localhost:8087/api/answer-option/question/" + questionId;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + jwtToken)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONArray array = new JSONArray(response.body());
        List<Answer> answers = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            Answer answer = new Answer();
            answer.idProperty().set(obj.getInt("id"));
            answer.answerTextProperty().set(obj.getString("answerText"));
            answer.isCorrectProperty().set(obj.getBoolean("isCorrect"));
            answers.add(answer);
        }
        return answers;
    }

    public void updateQuestion(Question question,  String jwtToken) throws Exception {
        //System.out.println("updateQuestion: " + jwtToken);
        String url = "http://localhost:8087/api/question/" + question.getId();
        JSONObject json = new JSONObject();
        json.put("questionText", question.getQuestionText());
        //System.out.println("questionText: " + question);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .method("PATCH", HttpRequest.BodyPublishers.ofString(json.toString())) // Используем метод PATCH
                //.PUT(HttpRequest.BodyPublishers.ofString(json.toString()))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + jwtToken) // Передаем токен
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

    }

    public void updateAnswer(Answer answer,  String jwtToken) throws Exception {

        String url = "http://localhost:8087/api/answer-option/" + answer.getId();
        JSONObject json = new JSONObject();
        json.put("answerText", answer.getAnswerText());
        json.put("isCorrect", answer.isIsCorrect());
        //System.out.println("answer: " + answer);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .method("PATCH", HttpRequest.BodyPublishers.ofString(json.toString())) // Используем метод PATCH
                //.PUT(HttpRequest.BodyPublishers.ofString(json.toString()))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + jwtToken) // Передаем токен
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }


}
