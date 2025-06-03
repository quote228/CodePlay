package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class CoursesFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private RecyclerView testsRecycler;
    private TestAdapter testAdapter;
    private static final String TAG = "CoursesFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.courses, container, false);

        dbHelper = new DatabaseHelper(requireContext());
        testsRecycler = view.findViewById(R.id.tests_recycler);

        // Настройка RecyclerView
        testsRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        List<TestAdapter.TestItem> testItems = loadTests();
        testAdapter = new TestAdapter(testItems, this::onTestClick);
        testsRecycler.setAdapter(testAdapter);

        return view;
    }

    private List<TestAdapter.TestItem> loadTests() {
        List<TestAdapter.TestItem> testItems = new ArrayList<>();
        List<String> tests = dbHelper.getAllTests();
        Log.d(TAG, "Retrieved tests: " + tests.size());

        for (String testName : tests) {
            List<DatabaseHelper.Question> questions = dbHelper.getQuestionsForTest(testName);
            DatabaseHelper.TestProgress progress = dbHelper.getTestProgress(testName);
            int lastQuestionIndex = progress != null ? progress.lastQuestionIndex : -1;
            boolean isCompleted = progress != null && progress.isCompleted;

            String status;
            int progressPercent;
            if (isCompleted) {
                status = "Выполнено";
                progressPercent = 100;
            } else if (lastQuestionIndex >= 0) {
                status = "В прогрессе";
                progressPercent = (lastQuestionIndex + 1) * 100 / questions.size();
            } else {
                status = "Не начат";
                progressPercent = 0;
            }

            testItems.add(new TestAdapter.TestItem(testName, status, progressPercent));
            Log.d(TAG, "Added test: " + testName + ", status: " + status + ", progress: " + progressPercent);
        }

        return testItems;
    }

    private void onTestClick(TestAdapter.TestItem test) {
        Log.d(TAG, "Test clicked: " + test.testName);
        List<DatabaseHelper.Question> questions = dbHelper.getQuestionsForTest(test.testName);
        if (!questions.isEmpty()) {
            TestFragment testFragment = TestFragment.newInstance(test.testName, questions);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, testFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            Log.e(TAG, "No questions found for test: " + test.testName);
        }
    }
}