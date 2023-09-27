package com.techatpark.workout.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techatpark.workout.model.Category;
import com.techatpark.workout.model.Choice;
import com.techatpark.workout.model.Question;
import com.techatpark.workout.model.QuestionType;
import com.techatpark.workout.service.CategoryService;
import com.techatpark.workout.service.QuestionService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class QuestionsLoader {


    /**
     * Quetion Owner.
     */
    public static final String USER_NAME = "tom@email.com";
    /**
     * Category Service.
     */
    @Autowired
    private CategoryService tagService;

    /**
     * Json Mapper.
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Seed Folder.
     */
    @Value("${app.seed.folder:src/test/resources}")
    private String seedFolder;

    /**
     * Question Service.
     */
    @Autowired
    private QuestionService questionService;

    /**
     * Loads Questions.
     */
    @PostConstruct
    void load() throws IOException {
        if (seedFolder != null) {
            questionService.deleteAll();
            createAllCategories(USER_NAME);
            File questionsFolder = new File(seedFolder, "questions");
            Files.find(Path.of(questionsFolder.getPath()),
                        Integer.MAX_VALUE,
                        (filePath, fileAttr)
                                -> fileAttr.isRegularFile()
                                && !filePath.toFile().getName().contains("-"))
                    .forEach(path -> {
                        try {
                            createQuestion(USER_NAME, path.toFile());
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }

    private void createAllCategories(final String userName) throws IOException {
        if (seedFolder != null) {
            questionService.deleteAll();
            File questionsFolder = new File(seedFolder, "questions");
            Files.find(Path.of(questionsFolder.getPath()),
                            Integer.MAX_VALUE,
                            (filePath, fileAttr)
                                    -> fileAttr.isDirectory())
                    .forEach(tagFolder -> {
                        if (!tagFolder.equals(questionsFolder)) {
                            try {
                                tagService.create(userName, null,
                                new Category(tagFolder.getFileName().toString(),
                                        tagFolder.getFileName().toString(),
                                        null,
                                        userName, null,
                                        userName));
                            } catch (DuplicateKeyException e) {
                                System.out.println("Duplicate Category "
                                        + tagFolder.getFileName().toString());
                            }

                        }
                    });
        }
    }

    private Question createQuestion(final String userName,
                                    final File questionFile)
            throws JsonProcessingException {
        Question question = getObject(questionFile, Question.class);
        final String nameOfQuestion = questionFile.getName()
                .replaceFirst(".json", "");
        boolean isWindows = System
                .getProperty("os.name").toLowerCase().indexOf("win") >= 0;
        String regexForQuestions = isWindows
                ? "\\\\questions\\\\" : "/questions/";
        String regexPath = isWindows
                ? "\\\\" : "/";
        String thePath = questionFile.getPath().split(regexForQuestions)[1];

        List<String> tokens =
                new ArrayList<>(List.of(thePath.split(regexPath)));


        tokens.remove(tokens.size() - 1);
        String chapterPath = tokens.stream().collect(Collectors.joining("/"));

        Stream<Choice> rightAnswers = question.getChoices()
                .stream()
                .filter(choice
                        -> choice.isAnswer() != null
                        && choice.isAnswer());

        QuestionType questionType = rightAnswers.count() == 1
                ? QuestionType.CHOOSE_THE_BEST
                : QuestionType.MULTI_CHOICE;

        Question createdQuestion = questionService.create(tokens,
                null,
                questionType,
                null, userName, question).get();

        List<File> questionLocalizedFiles = List.of(
                Objects.requireNonNull(questionFile.getParentFile()
                        .listFiles((dir, name) -> name.endsWith(".json")
                                && name.contains(nameOfQuestion + "-"))));

        questionLocalizedFiles.forEach(questionLocalizedFile -> {
            Locale locale =  Locale.of(questionLocalizedFile.getName()
                    .replaceFirst(nameOfQuestion + "-", "")
                    .replaceFirst(".json", ""));
            final Question questionLocalized =
                    getObject(questionLocalizedFile, Question.class);
            questionLocalized.setId(createdQuestion.getId());
            for (int i = 0; i < createdQuestion.getChoices().size(); i++) {
                questionLocalized.getChoices().get(i)
                        .setId(createdQuestion.getChoices().get(i).getId());
                questionLocalized.getChoices().get(i)
                        .setAnswer(
                                createdQuestion.getChoices().get(i).isAnswer());
            }

            questionService.update(
                    questionType,
                    createdQuestion.getId(), locale, questionLocalized).get();

        });

        return createdQuestion;
    }


    private <T> T getObject(final File jsonFile, final Class<T> type) {
        T t;
        try {
            t = objectMapper.readValue(jsonFile, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return t;
    }


}