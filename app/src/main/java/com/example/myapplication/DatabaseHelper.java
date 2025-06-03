package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "QuizDB";
    private static final int DATABASE_VERSION = 11;
    private static final String TABLE_QUESTIONS = "questions";
    private static final String TABLE_ACHIEVEMENTS = "achievements";
    private static final String TABLE_USERS = "users";
    private static final String TABLE_TEST_PROGRESS = "test_progress";
    private static final String TABLE_TESTS = "tests";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_QUESTION = "question";
    private static final String COLUMN_ANSWER1 = "answer1";
    private static final String COLUMN_ANSWER2 = "answer2";
    private static final String COLUMN_ANSWER3 = "answer3";
    private static final String COLUMN_ANSWER4 = "answer4";
    private static final String COLUMN_CORRECT_INDEX = "correct_index";
    private static final String COLUMN_EXPLANATION = "explanation";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_TEST_ID = "test_id";
    private static final String COLUMN_COMPLETED_AT = "completed_at";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_XP = "xp";
    private static final String COLUMN_TEST_NAME = "test_name";
    private static final String COLUMN_LAST_QUESTION_INDEX = "last_question_index";
    private static final String COLUMN_IS_COMPLETED = "is_completed";
    private static final String TAG = "DatabaseHelper";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        logDatabaseContents();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TESTS_TABLE = "CREATE TABLE " + TABLE_TESTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TEST_NAME + " TEXT UNIQUE"
                + ")";
        db.execSQL(CREATE_TESTS_TABLE);

        String CREATE_QUESTIONS_TABLE = "CREATE TABLE " + TABLE_QUESTIONS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_QUESTION + " TEXT,"
                + COLUMN_ANSWER1 + " TEXT,"
                + COLUMN_ANSWER2 + " TEXT,"
                + COLUMN_ANSWER3 + " TEXT,"
                + COLUMN_ANSWER4 + " TEXT,"
                + COLUMN_CORRECT_INDEX + " INTEGER,"
                + COLUMN_EXPLANATION + " TEXT,"
                + COLUMN_TEST_ID + " INTEGER,"
                + "FOREIGN KEY (" + COLUMN_TEST_ID + ") REFERENCES " + TABLE_TESTS + "(" + COLUMN_ID + ")"
                + ")";
        db.execSQL(CREATE_QUESTIONS_TABLE);

        String CREATE_ACHIEVEMENTS_TABLE = "CREATE TABLE " + TABLE_ACHIEVEMENTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_ID + " INTEGER,"
                + COLUMN_TEST_ID + " INTEGER,"
                + COLUMN_COMPLETED_AT + " TEXT"
                + ")";
        db.execSQL(CREATE_ACHIEVEMENTS_TABLE);

        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_XP + " INTEGER"
                + ")";
        db.execSQL(CREATE_USERS_TABLE);

        String CREATE_TEST_PROGRESS_TABLE = "CREATE TABLE " + TABLE_TEST_PROGRESS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TEST_NAME + " TEXT,"
                + COLUMN_LAST_QUESTION_INDEX + " INTEGER,"
                + COLUMN_IS_COMPLETED + " INTEGER"
                + ")";
        db.execSQL(CREATE_TEST_PROGRESS_TABLE);

        ContentValues testValues1 = new ContentValues();
        testValues1.put(COLUMN_TEST_NAME, "Переменные");
        long testId1 = db.insert(TABLE_TESTS, null, testValues1);
        Log.d(TAG, "Inserted test Переменные with ID: " + testId1);

        ContentValues testValues2 = new ContentValues();
        testValues2.put(COLUMN_TEST_NAME, "Сортировка");
        long testId2 = db.insert(TABLE_TESTS, null, testValues2);
        Log.d(TAG, "Inserted test Сортировка with ID: " + testId2);

        ContentValues testValues3 = new ContentValues();
        testValues3.put(COLUMN_TEST_NAME, "Рекурсия");
        long testId3 = db.insert(TABLE_TESTS, null, testValues3);
        Log.d(TAG, "Inserted test Рекурсия with ID: " + testId3);

        ContentValues testValues4 = new ContentValues();
        testValues4.put(COLUMN_TEST_NAME, "Бинарный поиск");
        long testId4 = db.insert(TABLE_TESTS, null, testValues4);
        Log.d(TAG, "Inserted test Бинарный поиск with ID: " + testId4);

        // Тест "Переменные" (4 вопроса)
        ContentValues qValues1 = new ContentValues();
        qValues1.put(COLUMN_QUESTION, "Какой будет результат выполнения кода: int x = 2; x += 10; System.out.println(x);");
        qValues1.put(COLUMN_ANSWER1, "10");
        qValues1.put(COLUMN_ANSWER2, "2");
        qValues1.put(COLUMN_ANSWER3, "12");
        qValues1.put(COLUMN_ANSWER4, "Ошибка компиляции");
        qValues1.put(COLUMN_CORRECT_INDEX, 2);
        qValues1.put(COLUMN_EXPLANATION, "Правильный ответ: 12. Код выполняет операцию x += 10.");
        qValues1.put(COLUMN_TEST_ID, testId1);
        long qId1 = db.insert(TABLE_QUESTIONS, null, qValues1);
        Log.d(TAG, "Inserted question for test Переменные with ID: " + qId1);

        ContentValues qValues2 = new ContentValues();
        qValues2.put(COLUMN_QUESTION, "Что выведет код: System.out.println(5 / 2);");
        qValues2.put(COLUMN_ANSWER1, "2.5");
        qValues2.put(COLUMN_ANSWER2, "2");
        qValues2.put(COLUMN_ANSWER3, "3");
        qValues2.put(COLUMN_ANSWER4, "Ошибка");
        qValues2.put(COLUMN_CORRECT_INDEX, 1);
        qValues2.put(COLUMN_EXPLANATION, "Правильный ответ: 2. Деление целых чисел возвращает целую часть.");
        qValues2.put(COLUMN_TEST_ID, testId1);
        long qId2 = db.insert(TABLE_QUESTIONS, null, qValues2);
        Log.d(TAG, "Inserted question for test Переменные with ID: " + qId2);

        ContentValues qValues3 = new ContentValues();
        qValues3.put(COLUMN_QUESTION, "Как объявить целочисленную переменную в Java?");
        qValues3.put(COLUMN_ANSWER1, "int x;");
        qValues3.put(COLUMN_ANSWER2, "var x;");
        qValues3.put(COLUMN_ANSWER3, "integer x;");
        qValues3.put(COLUMN_ANSWER4, "x int;");
        qValues3.put(COLUMN_CORRECT_INDEX, 0);
        qValues3.put(COLUMN_EXPLANATION, "Правильный ответ: int x;. Это правильный синтаксис в Java.");
        qValues3.put(COLUMN_TEST_ID, testId1);
        long qId3 = db.insert(TABLE_QUESTIONS, null, qValues3);
        Log.d(TAG, "Inserted question for test Переменные with ID: " + qId3);

        ContentValues qValues4 = new ContentValues();
        qValues4.put(COLUMN_QUESTION, "Что произойдёт, если попытаться разделить на ноль?");
        qValues4.put(COLUMN_ANSWER1, "Ошибка во время выполнения");
        qValues4.put(COLUMN_ANSWER2, "Бесконечность");
        qValues4.put(COLUMN_ANSWER3, "0");
        qValues4.put(COLUMN_ANSWER4, "Ничего");
        qValues4.put(COLUMN_CORRECT_INDEX, 0);
        qValues4.put(COLUMN_EXPLANATION, "Правильный ответ: Ошибка во время выполнения. Деление на ноль вызывает ArithmeticException.");
        qValues4.put(COLUMN_TEST_ID, testId1);
        long qId4 = db.insert(TABLE_QUESTIONS, null, qValues4);
        Log.d(TAG, "Inserted question for test Переменные with ID: " + qId4);

        // Тест "Сортировка" (3 вопроса)
        ContentValues qValues5 = new ContentValues();
        qValues5.put(COLUMN_QUESTION, "Какой метод сортировки быстрее для частично отсортированных массивов?");
        qValues5.put(COLUMN_ANSWER1, "Bubble Sort");
        qValues5.put(COLUMN_ANSWER2, "Quick Sort");
        qValues5.put(COLUMN_ANSWER3, "Insertion Sort");
        qValues5.put(COLUMN_ANSWER4, "Merge Sort");
        qValues5.put(COLUMN_CORRECT_INDEX, 2);
        qValues5.put(COLUMN_EXPLANATION, "Правильный ответ: Insertion Sort. Он эффективен для частично отсортированных данных.");
        qValues5.put(COLUMN_TEST_ID, testId2);
        long qId5 = db.insert(TABLE_QUESTIONS, null, qValues5);
        Log.d(TAG, "Inserted question for test Сортировка with ID: " + qId5);

        ContentValues qValues6 = new ContentValues();
        qValues6.put(COLUMN_QUESTION, "Какой метод сортировки имеет сложность O(n log n)?");
        qValues6.put(COLUMN_ANSWER1, "Bubble Sort");
        qValues6.put(COLUMN_ANSWER2, "Quick Sort");
        qValues6.put(COLUMN_ANSWER3, "Selection Sort");
        qValues6.put(COLUMN_ANSWER4, "Insertion Sort");
        qValues6.put(COLUMN_CORRECT_INDEX, 1);
        qValues6.put(COLUMN_EXPLANATION, "Правильный ответ: Quick Sort. В среднем он работает за O(n log n).");
        qValues6.put(COLUMN_TEST_ID, testId2);
        long qId6 = db.insert(TABLE_QUESTIONS, null, qValues6);
        Log.d(TAG, "Inserted question for test Сортировка with ID: " + qId6);

        ContentValues qValues7 = new ContentValues();
        qValues7.put(COLUMN_QUESTION, "Что такое стабильная сортировка?");
        qValues7.put(COLUMN_ANSWER1, "Сортировка с сохранением порядка равных элементов");
        qValues7.put(COLUMN_ANSWER2, "Сортировка с минимальной памятью");
        qValues7.put(COLUMN_ANSWER3, "Сортировка без сравнений");
        qValues7.put(COLUMN_ANSWER4, "Сортировка с максимальной скоростью");
        qValues7.put(COLUMN_CORRECT_INDEX, 0);
        qValues7.put(COLUMN_EXPLANATION, "Правильный ответ: Сортировка с сохранением порядка равных элементов.");
        qValues7.put(COLUMN_TEST_ID, testId2);
        long qId7 = db.insert(TABLE_QUESTIONS, null, qValues7);
        Log.d(TAG, "Inserted question for test Сортировка with ID: " + qId7);

        // Тест "Рекурсия" (5 вопросов)
        ContentValues qValues8 = new ContentValues();
        qValues8.put(COLUMN_QUESTION, "Что такое рекурсия?");
        qValues8.put(COLUMN_ANSWER1, "Цикл");
        qValues8.put(COLUMN_ANSWER2, "Функция, вызывающая сама себя");
        qValues8.put(COLUMN_ANSWER3, "Массив");
        qValues8.put(COLUMN_ANSWER4, "Переменная");
        qValues8.put(COLUMN_CORRECT_INDEX, 1);
        qValues8.put(COLUMN_EXPLANATION, "Правильный ответ: Функция, вызывающая сама себя.");
        qValues8.put(COLUMN_TEST_ID, testId3);
        long qId8 = db.insert(TABLE_QUESTIONS, null, qValues8);
        Log.d(TAG, "Inserted question for test Рекурсия with ID: " + qId8);

        ContentValues qValues9 = new ContentValues();
        qValues9.put(COLUMN_QUESTION, "Какой случай остановки нужен для рекурсии?");
        qValues9.put(COLUMN_ANSWER1, "Базовый случай");
        qValues9.put(COLUMN_ANSWER2, "Циклический случай");
        qValues9.put(COLUMN_ANSWER3, "Итеративный случай");
        qValues9.put(COLUMN_ANSWER4, "Рекурсивный случай");
        qValues9.put(COLUMN_CORRECT_INDEX, 0);
        qValues9.put(COLUMN_EXPLANATION, "Правильный ответ: Базовый случай. Без него рекурсия будет бесконечной.");
        qValues9.put(COLUMN_TEST_ID, testId3);
        long qId9 = db.insert(TABLE_QUESTIONS, null, qValues9);
        Log.d(TAG, "Inserted question for test Рекурсия with ID: " + qId9);

        ContentValues qValues10 = new ContentValues();
        qValues10.put(COLUMN_QUESTION, "Какова сложность факториала через рекурсию?");
        qValues10.put(COLUMN_ANSWER1, "O(n)");
        qValues10.put(COLUMN_ANSWER2, "O(n^2)");
        qValues10.put(COLUMN_ANSWER3, "O(2^n)");
        qValues10.put(COLUMN_ANSWER4, "O(log n)");
        qValues10.put(COLUMN_CORRECT_INDEX, 0);
        qValues10.put(COLUMN_EXPLANATION, "Правильный ответ: O(n). Каждое рекурсивное вызов добавляет константу.");
        qValues10.put(COLUMN_TEST_ID, testId3);
        long qId10 = db.insert(TABLE_QUESTIONS, null, qValues10);
        Log.d(TAG, "Inserted question for test Рекурсия with ID: " + qId10);

        ContentValues qValues11 = new ContentValues();
        qValues11.put(COLUMN_QUESTION, "Пример рекурсивной функции?");
        qValues11.put(COLUMN_ANSWER1, "sum(int n)");
        qValues11.put(COLUMN_ANSWER2, "factorial(int n)");
        qValues11.put(COLUMN_ANSWER3, "print()");
        qValues11.put(COLUMN_ANSWER4, "loop()");
        qValues11.put(COLUMN_CORRECT_INDEX, 1);
        qValues11.put(COLUMN_EXPLANATION, "Правильный ответ: factorial(int n). Это классический пример рекурсии.");
        qValues11.put(COLUMN_TEST_ID, testId3);
        long qId11 = db.insert(TABLE_QUESTIONS, null, qValues11);
        Log.d(TAG, "Inserted question for test Рекурсия with ID: " + qId11);

        ContentValues qValues12 = new ContentValues();
        qValues12.put(COLUMN_QUESTION, "Что произойдёт без базового случая?");
        qValues12.put(COLUMN_ANSWER1, "Переполнение стека");
        qValues12.put(COLUMN_ANSWER2, "Бесконечный цикл");
        qValues12.put(COLUMN_ANSWER3, "Остановка программы");
        qValues12.put(COLUMN_ANSWER4, "Ошибка компиляции");
        qValues12.put(COLUMN_CORRECT_INDEX, 0);
        qValues12.put(COLUMN_EXPLANATION, "Правильный ответ: Переполнение стека. Без базового случая рекурсия бесконечна.");
        qValues12.put(COLUMN_TEST_ID, testId3);
        long qId12 = db.insert(TABLE_QUESTIONS, null, qValues12);
        Log.d(TAG, "Inserted question for test Рекурсия with ID: " + qId12);

        // Тест "Бинарный поиск" (3 вопроса)
        ContentValues qValues13 = new ContentValues();
        qValues13.put(COLUMN_QUESTION, "В каком случае бинарный поиск эффективнее линейного?");
        qValues13.put(COLUMN_ANSWER1, "Когда массив отсортирован");
        qValues13.put(COLUMN_ANSWER2, "Когда массив неотсортирован");
        qValues13.put(COLUMN_ANSWER3, "Когда массив пуст");
        qValues13.put(COLUMN_ANSWER4, "Никогда");
        qValues13.put(COLUMN_CORRECT_INDEX, 0);
        qValues13.put(COLUMN_EXPLANATION, "Правильный ответ: Когда массив отсортирован.");
        qValues13.put(COLUMN_TEST_ID, testId4);
        long qId13 = db.insert(TABLE_QUESTIONS, null, qValues13);
        Log.d(TAG, "Inserted question for test Бинарный поиск with ID: " + qId13);

        ContentValues qValues14 = new ContentValues();
        qValues14.put(COLUMN_QUESTION, "Какова сложность бинарного поиска?");
        qValues14.put(COLUMN_ANSWER1, "O(log n)");
        qValues14.put(COLUMN_ANSWER2, "O(n)");
        qValues14.put(COLUMN_ANSWER3, "O(n log n)");
        qValues14.put(COLUMN_ANSWER4, "O(n^2)");
        qValues14.put(COLUMN_CORRECT_INDEX, 0);
        qValues14.put(COLUMN_EXPLANATION, "Правильный ответ: O(log n). Бинарный поиск делит массив пополам.");
        qValues14.put(COLUMN_TEST_ID, testId4);
        long qId14 = db.insert(TABLE_QUESTIONS, null, qValues14);
        Log.d(TAG, "Inserted question for test Бинарный поиск with ID: " + qId14);

        ContentValues qValues15 = new ContentValues();
        qValues15.put(COLUMN_QUESTION, "Что нужно для использования бинарного поиска?");
        qValues15.put(COLUMN_ANSWER1, "Отсортированный массив");
        qValues15.put(COLUMN_ANSWER2, "Неотсортированный массив");
        qValues15.put(COLUMN_ANSWER3, "Связный список");
        qValues15.put(COLUMN_ANSWER4, "Дерево");
        qValues15.put(COLUMN_CORRECT_INDEX, 0);
        qValues15.put(COLUMN_EXPLANATION, "Правильный ответ: Отсортированный массив. Бинарный поиск требует отсортированных данных.");
        qValues15.put(COLUMN_TEST_ID, testId4);
        long qId15 = db.insert(TABLE_QUESTIONS, null, qValues15);
        Log.d(TAG, "Inserted question for test Бинарный поиск with ID: " + qId15);

        ContentValues userValues = new ContentValues();
        userValues.put(COLUMN_USERNAME, "User1");
        userValues.put(COLUMN_XP, 0);
        long userId = db.insert(TABLE_USERS, null, userValues);
        Log.d(TAG, "Inserted user with ID: " + userId);

        Log.d(TAG, "Database created with tables and initial data");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACHIEVEMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEST_PROGRESS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TESTS);
        onCreate(db);
        Log.d(TAG, "Database upgraded from version " + oldVersion + " to " + newVersion);
    }

    public List<String> getAllTests() {
        List<String> tests = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TESTS, new String[]{COLUMN_TEST_NAME}, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String testName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEST_NAME));
                tests.add(testName);
                Log.d(TAG, "Found test: " + testName);
            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.d(TAG, "Retrieved " + tests.size() + " tests from database: " + tests);
        return tests;
    }

    public List<Question> getQuestionsForTest(String testName) {
        List<Question> questions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d(TAG, "Querying questions for testName: " + testName);
        Cursor cursor = db.rawQuery("SELECT q.* FROM " + TABLE_QUESTIONS + " q JOIN " + TABLE_TESTS + " t ON q." + COLUMN_TEST_ID + " = t." + COLUMN_ID + " WHERE t." + COLUMN_TEST_NAME + " = ?", new String[]{testName});

        Log.d(TAG, "Cursor count for test " + testName + ": " + cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                Question question = new Question();
                question.id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                question.questionText = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUESTION));
                question.answers = new ArrayList<>();
                question.answers.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ANSWER1)));
                question.answers.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ANSWER2)));
                question.answers.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ANSWER3)));
                question.answers.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ANSWER4)));
                question.correctAnswerIndex = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CORRECT_INDEX));
                question.explanation = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXPLANATION));
                questions.add(question);
                Log.d(TAG, "Question for test " + testName + ": " + question.questionText);
            } while (cursor.moveToNext());
        } else {
            Log.w(TAG, "No questions found for test: " + testName);
        }
        cursor.close();
        Log.d(TAG, "Retrieved " + questions.size() + " questions for test: " + testName);
        return questions;
    }

    public List<Question> getAllQuestions() {
        List<Question> questions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_QUESTIONS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Question question = new Question();
                question.id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                question.questionText = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUESTION));
                question.answers = new ArrayList<>();
                question.answers.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ANSWER1)));
                question.answers.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ANSWER2)));
                question.answers.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ANSWER3)));
                question.answers.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ANSWER4)));
                question.correctAnswerIndex = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CORRECT_INDEX));
                question.explanation = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXPLANATION));
                questions.add(question);
            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.d(TAG, "Retrieved " + questions.size() + " questions from database");
        return questions;
    }

    public void addAchievement(int userId, int testId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_TEST_ID, testId);
        values.put(COLUMN_COMPLETED_AT, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
        db.insert(TABLE_ACHIEVEMENTS, null, values);
        Log.d(TAG, "Added achievement for user " + userId + ", test " + testId);
    }

    public int getUserXP(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_XP};
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int xp = 0;
        if (cursor.moveToFirst()) {
            xp = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_XP));
        }
        cursor.close();
        Log.d(TAG, "Retrieved XP for user " + userId + ": " + xp);
        return xp;
    }

    public void updateUserXP(int userId, int newXP) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_XP, newXP);
        String whereClause = COLUMN_ID + " = ?";
        String[] whereArgs = {String.valueOf(userId)};
        db.update(TABLE_USERS, values, whereClause, whereArgs);
        Log.d(TAG, "Updated XP for user " + userId + " to " + newXP);
    }

    // Сохранение прогресса теста (оптимизированная версия)
    public void saveTestProgress(String testName, int lastQuestionIndex, boolean isCompleted) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEST_NAME, testName);
        values.put(COLUMN_LAST_QUESTION_INDEX, lastQuestionIndex);
        values.put(COLUMN_IS_COMPLETED, isCompleted ? 1 : 0);

        // Проверяем, существует ли запись
        Cursor cursor = db.query(TABLE_TEST_PROGRESS,
                new String[]{COLUMN_ID},
                COLUMN_TEST_NAME + " = ?",
                new String[]{testName},
                null, null, null);

        if (cursor.moveToFirst()) {
            // Обновляем существующую запись
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
            db.update(TABLE_TEST_PROGRESS,
                    values,
                    COLUMN_ID + " = ?",
                    new String[]{String.valueOf(id)});
            Log.d(TAG, "Updated test progress for " + testName + ": lastIndex=" + lastQuestionIndex + ", isCompleted=" + isCompleted);
        } else {
            // Создаем новую запись
            db.insert(TABLE_TEST_PROGRESS, null, values);
            Log.d(TAG, "Inserted new test progress for " + testName + ": lastIndex=" + lastQuestionIndex + ", isCompleted=" + isCompleted);
        }
        cursor.close();
        db.close(); // Закрываем базу данных
    }

    // Получение прогресса теста (оптимизированная версия)
    public TestProgress getTestProgress(String testName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TEST_PROGRESS,
                new String[]{COLUMN_LAST_QUESTION_INDEX, COLUMN_IS_COMPLETED},
                COLUMN_TEST_NAME + " = ?",
                new String[]{testName},
                null, null, null);

        TestProgress progress = null;
        if (cursor.moveToFirst()) {
            progress = new TestProgress();
            progress.lastQuestionIndex = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LAST_QUESTION_INDEX));
            progress.isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_COMPLETED)) == 1;
            Log.d(TAG, "Retrieved progress for " + testName + ": lastIndex=" + progress.lastQuestionIndex + ", isCompleted=" + progress.isCompleted);
        } else {
            Log.d(TAG, "No progress found for " + testName);
        }
        cursor.close();
        db.close(); // Закрываем базу данных
        return progress;
    }

    public boolean isTestCompleted(int userId, int testId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID};
        String selection = COLUMN_USER_ID + " = ? AND " + COLUMN_TEST_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId), String.valueOf(testId)};
        Cursor cursor = db.query(TABLE_ACHIEVEMENTS, columns, selection, selectionArgs, null, null, null);
        boolean completed = cursor.moveToFirst();
        cursor.close();
        Log.d(TAG, "Test " + testId + " for user " + userId + " completed: " + completed);
        return completed;
    }

    public void resetProgress(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ACHIEVEMENTS, null, null);
        db.delete(TABLE_TEST_PROGRESS, null, null);
        updateUserXP(userId, 0);
        Log.d(TAG, "Progress reset for user " + userId);
    }

    public void logDatabaseContents() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor testCursor = db.query(TABLE_TESTS, null, null, null, null, null, null);
        Log.d(TAG, "Logging all tests in database:");
        if (testCursor.moveToFirst()) {
            do {
                int id = testCursor.getInt(testCursor.getColumnIndexOrThrow(COLUMN_ID));
                String name = testCursor.getString(testCursor.getColumnIndexOrThrow(COLUMN_TEST_NAME));
                Log.d(TAG, "Test ID: " + id + ", name: " + name);
            } while (testCursor.moveToNext());
        } else {
            Log.w(TAG, "No tests found in database");
        }
        testCursor.close();

        Cursor questionCursor = db.query(TABLE_QUESTIONS, null, null, null, null, null, null);
        Log.d(TAG, "Logging all questions in database:");
        if (questionCursor.moveToFirst()) {
            do {
                int id = questionCursor.getInt(questionCursor.getColumnIndexOrThrow(COLUMN_ID));
                String questionText = questionCursor.getString(questionCursor.getColumnIndexOrThrow(COLUMN_QUESTION));
                int testId = questionCursor.getInt(questionCursor.getColumnIndexOrThrow(COLUMN_TEST_ID));
                Log.d(TAG, "Question ID: " + id + ", questionText: " + questionText + ", testId: " + testId);
            } while (questionCursor.moveToNext());
        } else {
            Log.w(TAG, "No questions found in database");
        }
        questionCursor.close();
    }

    public int getTestIdByName(String testName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID};
        String selection = COLUMN_TEST_NAME + " = ?";
        String[] selectionArgs = {testName};
        Cursor cursor = db.query(TABLE_TESTS, columns, selection, selectionArgs, null, null, null);
        int testId = -1;
        if (cursor.moveToFirst()) {
            testId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
        }
        cursor.close();
        Log.d(TAG, "Retrieved test ID for " + testName + ": " + testId);
        return testId;
    }

    public List<Achievement> getUserAchievements(int userId) {
        List<Achievement> achievements = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT a." + COLUMN_TEST_ID + ", t." + COLUMN_TEST_NAME + ", a." + COLUMN_COMPLETED_AT
                + " FROM " + TABLE_ACHIEVEMENTS + " a"
                + " JOIN " + TABLE_TESTS + " t ON a." + COLUMN_TEST_ID + " = t." + COLUMN_ID
                + " WHERE a." + COLUMN_USER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                Achievement achievement = new Achievement();
                achievement.testId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TEST_ID));
                achievement.testName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEST_NAME));
                achievement.completedAt = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COMPLETED_AT));
                achievements.add(achievement);
                Log.d(TAG, "Achievement for user " + userId + ": testId=" + achievement.testId + ", testName=" + achievement.testName);
            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.d(TAG, "Retrieved " + achievements.size() + " achievements for user " + userId);
        return achievements;
    }

    public static class Question {
        public int id;
        public String questionText;
        public List<String> answers;
        public int correctAnswerIndex;
        public String explanation;
    }

    public static class TestProgress {
        public int lastQuestionIndex;
        public boolean isCompleted;
    }

    public static class Achievement {
        public int testId;
        public String testName;
        public String completedAt;
    }
}