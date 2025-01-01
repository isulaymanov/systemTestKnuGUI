package com.example.stestingknugui.controller;

import com.example.stestingknugui.model.Answer;
import com.example.stestingknugui.model.Question;
import com.example.stestingknugui.service.QuestionsService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import org.apache.poi.xwpf.usermodel.*;
import java.io.FileOutputStream;

public class QuestionsAnswersController {
    private final HttpClient client = HttpClient.newHttpClient();


    @FXML
    private TableView<Question> questionsTable;
    @FXML
    private TableColumn<Question, String> questionColumn;

    @FXML
    private TableView<Answer> answersTable;
    @FXML
    private TableColumn<Answer, String> answerColumn;
    @FXML
    private TableColumn<Answer, Boolean> correctColumn;

    private final QuestionsService questionsService = new QuestionsService();

    private ObservableList<Question> questions = FXCollections.observableArrayList();
    private ObservableList<Answer> answers = FXCollections.observableArrayList();

    private int testId;
    private String jwtToken;
    //System.out.println("Токен в QuestionsAnswersController" + jwtToken);
    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public void setTestId(int testId) {
        this.testId = testId;
        loadQuestions();
    }



    private void loadQuestions() {
        try {
            List<Question> loadedQuestions = questionsService.getQuestionsByTestId(testId, jwtToken);
            questions.setAll(loadedQuestions);
            questionsTable.setItems(questions);
        } catch (Exception e) {
            showError("Ошибка загрузки вопросов", e.getMessage());
        }
    }

    private void loadAnswers(int questionId) {
        try {
            List<Answer> loadedAnswers = questionsService.getAnswersByQuestionId(questionId, jwtToken);
            answers.setAll(loadedAnswers);
            answersTable.setItems(answers);
        } catch (Exception e) {
            showError("Ошибка загрузки ответов", e.getMessage());
        }
    }



    @FXML
    private void initialize() {
        questionColumn.setCellValueFactory(data -> data.getValue().questionTextProperty());
        questionColumn.setCellFactory(TextFieldTableCell.forTableColumn()); // Для редактирования текста
        questionColumn.setOnEditCommit(event -> {
            Question question = event.getRowValue();
            question.setQuestionText(event.getNewValue());

        });
        //System.out.println("Updated question: ID=" + question.getId() + ", Text=" + question.getQuestionText());

        answerColumn.setCellValueFactory(data -> data.getValue().answerTextProperty());
        answerColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        answerColumn.setOnEditCommit(event -> {

            Answer answer = event.getRowValue();
            answer.setAnswerText(event.getNewValue());
        });

        correctColumn.setCellValueFactory(data -> data.getValue().isCorrectProperty());
        correctColumn.setCellFactory(tc -> new CheckBoxTableCell<>());
        correctColumn.setEditable(true);

        questionsTable.setEditable(true);
        answersTable.setEditable(true);

        questionsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loadAnswers(newSelection.getId());
            }
        });
    }

    @FXML
    private void saveChanges() {
        try {
            if (questionsTable.getEditingCell() != null) {
                questionsTable.edit(-1, null); // Завершить редактирование в таблице вопросов
            }
            if (answersTable.getEditingCell() != null) {
                answersTable.edit(-1, null); // Завершить редактирование в таблице ответов
            }
            for (Question question : questions) {
                questionsService.updateQuestion(question, jwtToken);
            }
            //System.out.println("saveChanges: " + jwtToken);
            for (Answer answer : answers) {
                questionsService.updateAnswer(answer, jwtToken);
            }
            //System.out.println("saveChanges: " + jwtToken);
            showInfo("Сохранение", "Изменения успешно сохранены.");
        } catch (Exception e) {
            showError("Ошибка сохранения", e.getMessage());
        }
    }


    public void addQuestion(Question question, String jwtToken) throws Exception {
        String url = "http://localhost:8087/api/question";
        JSONObject json = new JSONObject();
        json.put("questionText", question.getQuestionText());

        JSONObject testing = new JSONObject();
        testing.put("id", testId);
        json.put("testing", testing);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + jwtToken)
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());

    }
    //System.out.println("Question added: " + question.getQuestionText());

    //System.out.println("Adding answer: " + answer.getAnswerText());
    //System.out.println("Request JSON: " + json.toString());

    public void addAnswer(Answer answer, String jwtToken) throws Exception {
        String url = "http://localhost:8087/api/answer-option";
        JSONObject json = new JSONObject();
        json.put("answerText", answer.getAnswerText());
        json.put("isCorrect", answer.isIsCorrect());


        Question selectedQuestion = questionsTable.getSelectionModel().getSelectedItem();
        if (selectedQuestion != null) {
            JSONObject questionJson = new JSONObject();
            questionJson.put("id", selectedQuestion.getId());
            json.put("question", questionJson);
        }
        //System.out.println("Selected question ID: " + selectedQuestion.getId());
//        else {
//            System.out.println("No question selected.");
//        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + jwtToken)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


        // Проверяем успешность ответа
//        if (response.statusCode() == 200 || response.statusCode() == 201) {
//            System.out.println("Answer added successfully: " + answer.getAnswerText());
//        } else {
//            System.out.println("Failed to add answer. Status code: " + response.statusCode());
//        }
    }

    // Логируем статус ответа
    //System.out.println("Server response status: " + response.statusCode());
    //System.out.println("Server response body: " + response.body());

    public void deleteQuestion(int questionId, String jwtToken) throws Exception {
        List<Answer> answersToDelete = questionsService.getAnswersByQuestionId(questionId, jwtToken);
        for (Answer answer : answersToDelete) {
            deleteAnswer(answer.getId(), jwtToken);
        }

        String url = "http://localhost:8087/api/question/" + questionId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .header("Authorization", "Bearer " + jwtToken)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        if (response.statusCode() == 200 || response.statusCode() == 204) {
//            System.out.println("Question deleted: ID=" + questionId);
//        } else {
//            System.out.println("Failed to delete question. Status code: " + response.statusCode());
//        }

        loadQuestions();
        answers.clear();
    }


    public void deleteAnswer(int answerId, String jwtToken) throws Exception {
        String url = "http://localhost:8087/api/answer-option/" + answerId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .header("Authorization", "Bearer " + jwtToken)
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());
        //System.out.println("Answer deleted: ID=" + answerId);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        if (response.statusCode() == 200 || response.statusCode() == 204) {
//            System.out.println("Answer deleted: ID=" + answerId);
//        } else {
//            System.out.println("Failed to delete answer. Status code: " + response.statusCode());
//        }


        Question selectedQuestion = questionsTable.getSelectionModel().getSelectedItem();
        if (selectedQuestion != null) {
            loadAnswers(selectedQuestion.getId());
        } else {
            answers.clear();
        }
    }


    @FXML
    private void addQuestion() {
        Question newQuestion = new Question();
        newQuestion.setQuestionText("Новый вопрос");
        try {
            addQuestion(newQuestion, jwtToken);
            showInfo("Успех", "Вопрос успешно добавлен.");
            loadQuestions();
        } catch (Exception e) {
            showError("Ошибка добавления", e.getMessage());
        }
    }



    @FXML
    private void addAnswer() {
        Answer newAnswer = new Answer();
        newAnswer.setAnswerText("Новый ответ");
        newAnswer.setIsCorrect(false);

        try {
            // Получаем выбранный вопрос
            Question selectedQuestion = questionsTable.getSelectionModel().getSelectedItem();
            if (selectedQuestion == null) {
                showError("Ошибка", "Не выбран вопрос для ответа.");
                return;
            }

            //System.out.println("Adding answer to question ID: " + selectedQuestion.getId());  // Логируем ID выбранного вопроса

            // Привязываем ответ к вопросу
            addAnswer(newAnswer, jwtToken);  // Добавляем ответ на сервер

            showInfo("Успех", "Ответ успешно добавлен.");

            // После добавления, загрузим ответы для текущего вопроса
            loadAnswers(selectedQuestion.getId());  // Обновляем список ответов для выбранного вопроса
        } catch (Exception e) {
            showError("Ошибка добавления", e.getMessage());
            //System.out.println("Error adding answer: " + e.getMessage());  // Логируем ошибку
        }
    }



    @FXML
    private void deleteQuestion() {
        Question selectedQuestion = questionsTable.getSelectionModel().getSelectedItem();
        if (selectedQuestion != null) {
            loadAnswers(selectedQuestion.getId());
            try {
                deleteQuestion(selectedQuestion.getId(), jwtToken);
                showInfo("Успех", "Вопрос успешно удален.");
                loadQuestions();  // Перезагружаем список вопросов
            } catch (Exception e) {
                showError("Ошибка удаления", e.getMessage());
            }
        } else {
            answers.clear();  // Если нет выбранного вопроса, очищаем ответы
            showError("Ошибка", "Не выбран вопрос для удаления.");
        }
    }


    @FXML
    private void deleteAnswer() {
        Answer selectedAnswer = answersTable.getSelectionModel().getSelectedItem();
        if (selectedAnswer != null) {
            try {
                deleteAnswer(selectedAnswer.getId(), jwtToken);
                showInfo("Успех", "Ответ успешно удален.");

                // Получаем текущий выбранный вопрос, для которого удаляли ответ
                Question selectedQuestion = questionsTable.getSelectionModel().getSelectedItem();
                if (selectedQuestion != null) {
                    loadAnswers(selectedQuestion.getId());  // Перезагружаем все ответы для выбранного вопроса
                } else {
                    answers.clear();  // Если нет выбранного вопроса, очищаем таблицу
                }

            } catch (Exception e) {
                showError("Ошибка удаления", e.getMessage());
            }
        } else {
            showError("Ошибка", "Не выбран ответ для удаления.");
        }
    }





    @FXML
    private void printQuestions() {
        try {
            // Создаем Word-документ
            XWPFDocument document = new XWPFDocument();

            // Добавляем заголовок
            XWPFParagraph title = document.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleRun = title.createRun();
            titleRun.setText("Вопросы теста");
            titleRun.setBold(true);
            titleRun.setFontSize(16);

            // Добавляем вопросы
            for (Question question : questions) {
                XWPFParagraph questionParagraph = document.createParagraph();
                XWPFRun questionRun = questionParagraph.createRun();
                questionRun.setText("- " + question.getQuestionText());
                questionRun.setFontSize(12);
            }

            // Выбор места для сохранения файла
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Сохранить файл");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Word Document", "*.docx"));
            File file = fileChooser.showSaveDialog(null);

            if (file != null) {
                try (FileOutputStream out = new FileOutputStream(file)) {
                    document.write(out);
                }
                showInfo("Успех", "Документ с вопросами успешно сохранен: " + file.getAbsolutePath());
            }
        } catch (Exception e) {
            showError("Ошибка", "Не удалось создать документ: " + e.getMessage());
        }
    }



    @FXML
    private void printQuestionsWithAnswers() {
        try {
            XWPFDocument document = new XWPFDocument();

            XWPFParagraph title = document.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleRun = title.createRun();
            titleRun.setText("Вопросы с ответами");
            titleRun.setBold(true);
            titleRun.setFontSize(16);

            for (Question question : questions) {
                XWPFParagraph questionParagraph = document.createParagraph();
                XWPFRun questionRun = questionParagraph.createRun();
                questionRun.setText("Вопрос: " + question.getQuestionText());
                questionRun.setBold(true);
                questionRun.setFontSize(14);

                List<Answer> relatedAnswers = questionsService.getAnswersByQuestionId(question.getId(), jwtToken);

                for (Answer answer : relatedAnswers) {
                    XWPFParagraph answerParagraph = document.createParagraph();
                    XWPFRun answerRun = answerParagraph.createRun();
                    answerRun.setText("  - " + answer.getAnswerText());
                    answerRun.setFontSize(12);
                }
            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Сохранить файл");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Word Document", "*.docx"));
            File file = fileChooser.showSaveDialog(null);

            if (file != null) {
                try (FileOutputStream out = new FileOutputStream(file)) {
                    document.write(out);
                }
                showInfo("Успех", "Документ с вопросами и ответами успешно сохранен: " + file.getAbsolutePath());
            }
        } catch (Exception e) {
            showError("Ошибка", "Не удалось создать документ: " + e.getMessage());
        }
    }


    private void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showInfo(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


}
