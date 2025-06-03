package com.example.myapplication;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import java.util.ArrayList;
import java.util.List;

public class TestFragment extends Fragment {

    private EditText questionText;
    private Button answerButton1, answerButton2, answerButton3, answerButton4;
    private Button nextButton, prevButton, finishButton;
    private ProgressBar progressBar;
    private DatabaseHelper dbHelper;
    private List<DatabaseHelper.Question> questions;
    private int currentQuestionIndex = 0;
    private boolean answerSelected = false;
    private String testName;
    private static final String ARG_TEST_NAME = "test_name";
    private static final int USER_ID = 1;
    private static final int XP_REWARD = 120;
    private static final String TAG = "TestFragment";

    public TestFragment() {
        this.testName = "Default Test";
        this.questions = new ArrayList<>();
    }

    public static TestFragment newInstance(String testName, List<DatabaseHelper.Question> questions) {
        TestFragment fragment = new TestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEST_NAME, testName);
        fragment.questions = questions != null ? new ArrayList<>(questions) : new ArrayList<>();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            testName = getArguments().getString(ARG_TEST_NAME, "Тест 1");
        }
        if (questions == null) {
            questions = new ArrayList<>();
        }
        dbHelper = new DatabaseHelper(requireContext());

        // Загружаем сохраненный прогресс
        DatabaseHelper.TestProgress progress = dbHelper.getTestProgress(testName);
        if (progress != null && !progress.isCompleted && progress.lastQuestionIndex < questions.size()) {
            currentQuestionIndex = progress.lastQuestionIndex;
            Log.d(TAG, "Resuming test " + testName + " at question " + (currentQuestionIndex + 1));
        } else {
            currentQuestionIndex = 0;
            Log.d(TAG, "Starting test " + testName + " from beginning");
        }

        Log.d(TAG, "TestFragment created with testName: " + testName + ", questions count: " + questions.size() + ", starting at index: " + currentQuestionIndex);
        for (int i = 0; i < questions.size(); i++) {
            DatabaseHelper.Question q = questions.get(i);
            Log.d(TAG, "Question " + (i + 1) + ": " + q.questionText + ", answers: " + q.answers + ", correctIndex: " + q.correctAnswerIndex);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "Inflating test.xml layout");
        View view = inflater.inflate(R.layout.test, container, false);

        // Инициализация UI
        questionText = view.findViewById(R.id.code_display);
        if (questionText == null) {
            Log.e(TAG, "code_display EditText not found in test.xml");
            return view;
        }

        answerButton1 = view.findViewById(R.id.answer_button_1);
        answerButton2 = view.findViewById(R.id.answer_button_2);
        answerButton3 = view.findViewById(R.id.answer_button_3);
        answerButton4 = view.findViewById(R.id.answer_button_4);
        if (answerButton1 == null || answerButton2 == null || answerButton3 == null || answerButton4 == null) {
            Log.e(TAG, "One or more answer buttons not found in test.xml");
            return view;
        }

        nextButton = view.findViewById(R.id.next_button);
        prevButton = view.findViewById(R.id.prev_button);
        finishButton = view.findViewById(R.id.finish_button);
        if (nextButton == null || prevButton == null || finishButton == null) {
            Log.e(TAG, "One or more navigation buttons not found in test.xml");
            return view;
        }

        progressBar = view.findViewById(R.id.progress_bar);
        if (progressBar == null) {
            Log.e(TAG, "ProgressBar not found with ID progress_bar in test.xml");
            return view;
        }

        progressBar.setIndeterminate(false);
        progressBar.setMax(questions.size());
        updateProgressBar();

        // Проверяем, что список вопросов не пуст
        Log.d(TAG, "Received " + questions.size() + " questions for test: " + testName);
        if (questions.isEmpty()) {
            Log.w(TAG, "Questions list is empty for test: " + testName);
            questionText.setText("Нет вопросов для отображения");
            disableAnswerButtons();
            progressBar.setProgress(0);
            return view;
        }

        // Отображаем текущий вопрос
        Log.d(TAG, "Loading question at index: " + currentQuestionIndex);
        setQuestion(questions.get(currentQuestionIndex));

        answerButton1.setOnClickListener(v -> checkAnswer(0));
        answerButton2.setOnClickListener(v -> checkAnswer(1));
        answerButton3.setOnClickListener(v -> checkAnswer(2));
        answerButton4.setOnClickListener(v -> checkAnswer(3));

        nextButton.setOnClickListener(v -> {
            Log.d(TAG, "Next button clicked");
            loadNextQuestion();
            dbHelper.saveTestProgress(testName, currentQuestionIndex, false);
        });

        prevButton.setOnClickListener(v -> {
            Log.d(TAG, "Previous button clicked");
            loadPreviousQuestion();
            dbHelper.saveTestProgress(testName, currentQuestionIndex, false);
        });

        finishButton.setOnClickListener(v -> {
            Log.d(TAG, "Finish button clicked at question " + (currentQuestionIndex + 1));
            finishTest();
        });

        updateNavigationButtons();

        return view;
    }

    private void setQuestion(DatabaseHelper.Question question) {
        if (question == null || question.questionText == null || question.answers == null) {
            Log.e(TAG, "Invalid question data: " + (question == null ? "question is null" : "questionText or answers is null"));
            questionText.setText("Ошибка: некорректный вопрос");
            return;
        }

        Log.d(TAG, "Setting question: " + question.questionText);
        questionText.setText(question.questionText);

        Button[] buttons = {answerButton1, answerButton2, answerButton3, answerButton4};
        for (int i = 0; i < Math.min(question.answers.size(), buttons.length); i++) {
            String answer = question.answers.get(i);
            if (answer != null) {
                buttons[i].setText(answer);
                buttons[i].setBackgroundTintList(getResources().getColorStateList(R.color.grey));
                buttons[i].setEnabled(true);
            } else {
                buttons[i].setText("Ответ отсутствует");
                buttons[i].setEnabled(false);
            }
        }
        answerSelected = false;
        updateNavigationButtons();
        updateProgressBar();
    }

    private void checkAnswer(int selectedIndex) {
        if (questions.isEmpty() || currentQuestionIndex >= questions.size()) {
            Log.e(TAG, "Cannot check answer: questions list is empty or invalid index");
            return;
        }

        DatabaseHelper.Question currentQuestion = questions.get(currentQuestionIndex);
        Button[] buttons = {answerButton1, answerButton2, answerButton3, answerButton4};
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setEnabled(false);
            if (i == currentQuestion.correctAnswerIndex) {
                buttons[i].setBackgroundTintList(getResources().getColorStateList(android.R.color.holo_green_light));
            } else if (i == selectedIndex) {
                buttons[i].setBackgroundTintList(getResources().getColorStateList(android.R.color.holo_red_light));
            }
        }

        answerSelected = true;
        updateNavigationButtons();

        if (selectedIndex != currentQuestion.correctAnswerIndex) {
            // Создаем кастомный диалог
            Dialog dialog = new Dialog(requireContext());
            dialog.setContentView(R.layout.layout_wrong_answer_dialog);
            dialog.setCancelable(true);

            // Находим элементы диалога
            TextView explanationText = dialog.findViewById(R.id.explanation_text);
            Button okButton = dialog.findViewById(R.id.ok_button);

            // Устанавливаем текст объяснения
            if (explanationText != null) {
                explanationText.setText(currentQuestion.explanation);
            } else {
                Log.e(TAG, "Explanation TextView not found in layout_wrong_answer_dialog.xml");
            }

            // Обработчик кнопки ОК
            if (okButton != null) {
                okButton.setOnClickListener(v -> dialog.dismiss());
            } else {
                Log.e(TAG, "OK Button not found in layout_wrong_answer_dialog.xml");
            }

            // Настраиваем фон окна
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            // Устанавливаем ширину диалога (90% от ширины экрана)
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = (int) (requireContext().getResources().getDisplayMetrics().widthPixels * 0.9);
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(params);

            // Логируем размеры диалога для отладки
            Log.d(TAG, "Dialog width set to: " + params.width + " pixels");

            // Показываем диалог
            dialog.show();
            Log.d(TAG, "Showing custom wrong answer dialog with explanation: " + currentQuestion.explanation);
        } else if (currentQuestionIndex == questions.size() - 1) {
            // Если это последний вопрос и ответ правильный, завершаем тест
            finishTest();
        }
    }

    private void loadNextQuestion() {
        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
            setQuestion(questions.get(currentQuestionIndex));
        }
        updateNavigationButtons();
    }

    private void loadPreviousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            setQuestion(questions.get(currentQuestionIndex));
        }
        updateNavigationButtons();
    }

    private void finishTest() {
        boolean isCompleted = currentQuestionIndex == questions.size() - 1 && answerSelected;
        dbHelper.saveTestProgress(testName, currentQuestionIndex, isCompleted);

        if (isCompleted) {
            int testId = dbHelper.getTestIdByName(testName);
            if (testId != -1) {
                dbHelper.addAchievement(USER_ID, testId);
                int currentXP = dbHelper.getUserXP(USER_ID);
                dbHelper.updateUserXP(USER_ID, currentXP + XP_REWARD);
                questionText.setText("Тест завершён! Получено " + XP_REWARD + " XP.");
            } else {
                Log.e(TAG, "Failed to find test ID for test: " + testName);
                questionText.setText("Ошибка: Тест не найден.");
            }
        } else {
            questionText.setText("Тест прерван. Прогресс сохранён на вопросе " + (currentQuestionIndex + 1) + ".");
        }

        disableAnswerButtons();
        nextButton.setEnabled(false);
        nextButton.setAlpha(0.5f);
        finishButton.setEnabled(false);
        updateProgressBar();

        navigateToMainFragment();
    }

    private void navigateToMainFragment() {
        try {
            FragmentManager fragmentManager = getParentFragmentManager();
            if (fragmentManager != null) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, new MainFragment());
                transaction.commit();
                Log.d(TAG, "Navigating to MainFragment");

                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).updateBottomNavigation(R.id.nav_home);
                } else {
                    Log.e(TAG, "Activity is not an instance of MainActivity or is null");
                }
            } else {
                Log.e(TAG, "FragmentManager is null, cannot navigate to MainFragment");
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to navigate to MainFragment", e);
        }
    }

    private void updateNavigationButtons() {
        prevButton.setEnabled(currentQuestionIndex > 0);
        nextButton.setEnabled(answerSelected && currentQuestionIndex < questions.size() - 1);
        finishButton.setEnabled(true);
        if (nextButton.isEnabled()) {
            nextButton.setAlpha(1.0f);
        } else {
            nextButton.setAlpha(0.5f);
        }
    }

    private void updateProgressBar() {
        if (progressBar != null) {
            if (questions.isEmpty()) {
                progressBar.setProgress(0);
            } else if (currentQuestionIndex >= questions.size()) {
                progressBar.setProgress(questions.size());
            } else {
                progressBar.setProgress(currentQuestionIndex + 1);
            }
            Log.d(TAG, "Progress updated: " + progressBar.getProgress() + "/" + progressBar.getMax() + ", indeterminate: " + progressBar.isIndeterminate());
        } else {
            Log.e(TAG, "ProgressBar is null, cannot update progress");
        }
    }

    private void disableAnswerButtons() {
        Button[] buttons = {answerButton1, answerButton2, answerButton3, answerButton4};
        for (Button button : buttons) {
            if (button != null) {
                button.setEnabled(false);
            }
        }
    }
}