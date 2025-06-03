package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textview.MaterialTextView;
import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {

    private List<TestItem> tests;
    private OnTestClickListener listener;

    public interface OnTestClickListener {
        void onTestClick(TestItem test);
    }

    public TestAdapter(List<TestItem> tests, OnTestClickListener listener) {
        this.tests = tests;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_item, parent, false);
        return new TestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
        TestItem test = tests.get(position);
        holder.testTitle.setText(test.testName);
        holder.testStatus.setText(test.status);
        holder.testProgressText.setText(test.progress + "%");
        holder.testProgressBar.setProgress(test.progress);
        holder.startTestButton.setOnClickListener(v -> listener.onTestClick(test));
    }

    @Override
    public int getItemCount() {
        return tests.size();
    }

    static class TestViewHolder extends RecyclerView.ViewHolder {
        ImageView testIcon;
        MaterialTextView testTitle;
        MaterialTextView testStatus;
        LinearProgressIndicator testProgressBar;
        MaterialTextView testProgressText;
        MaterialButton startTestButton;

        TestViewHolder(@NonNull View itemView) {
            super(itemView);
            testIcon = itemView.findViewById(R.id.test_icon);
            testTitle = itemView.findViewById(R.id.test_title);
            testStatus = itemView.findViewById(R.id.test_status);
            testProgressBar = itemView.findViewById(R.id.test_progress_bar);
            testProgressText = itemView.findViewById(R.id.test_progress_text);
            startTestButton = itemView.findViewById(R.id.start_test_button);
        }
    }

    public static class TestItem {
        public String testName;
        public String status;
        public int progress;

        public TestItem(String testName, String status, int progress) {
            this.testName = testName;
            this.status = status;
            this.progress = progress;
        }
    }
}