package com.example.myapplication;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textview.MaterialTextView;
import java.util.List;

public class MainFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private LinearProgressIndicator levelProgressBar;
    private MaterialTextView levelText;
    private RecyclerView achievementsRecycler;
    private MaterialButton resetProgressButton;
    private AchievementAdapter achievementAdapter;
    private static final int USER_ID = 1;
    private static final String TAG = "MainFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main, container, false);

        // Инициализация базы данных
        dbHelper = new DatabaseHelper(requireContext());

        // Инициализация элементов интерфейса
        levelProgressBar = view.findViewById(R.id.level_progress_bar);
        levelText = view.findViewById(R.id.level_text);
        achievementsRecycler = view.findViewById(R.id.achievements_recycler);
        resetProgressButton = view.findViewById(R.id.reset_progress_button);

        // Настройка RecyclerView
        achievementsRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        List<DatabaseHelper.Achievement> achievements = dbHelper.getUserAchievements(USER_ID);
        achievementAdapter = new AchievementAdapter(achievements, this::showAchievementDialog);
        achievementsRecycler.setAdapter(achievementAdapter);

        // Обновление уровня и прогресса
        updateLevelAndProgress();

        // Настройка кнопки сброса прогресса
        resetProgressButton.setOnClickListener(v -> showResetConfirmationDialog());

        // Анимация появления карточки уровня
        View levelCard = view.findViewById(R.id.level_card);
        levelCard.startAnimation(AnimationUtils.loadAnimation(requireContext(), android.R.anim.fade_in));

        return view;
    }

    private void updateLevelAndProgress() {
        int xp = dbHelper.getUserXP(USER_ID);
        int level = (xp / 100) + 1;
        int progress = xp % 100;

        levelProgressBar.setMax(100);
        levelProgressBar.setProgress(progress, true); // Анимированное обновление

        levelText.setText(String.format("%d%%\nУровень %d", progress, level));
    }

    private void showAchievementDialog(DatabaseHelper.Achievement achievement) {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.achievement_dialog, null);
        MaterialTextView titleView = dialogView.findViewById(R.id.achievement_title);
        MaterialTextView messageView = dialogView.findViewById(R.id.achievement_message);
        MaterialButton okButton = dialogView.findViewById(R.id.achievement_ok_button);

        titleView.setText("Достижение: " + achievement.testName);
        messageView.setText("Завершено: " + achievement.completedAt);

        Dialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setView(dialogView)
                .create();

        okButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

        // Анимация появления диалога
        dialogView.setScaleX(0.7f);
        dialogView.setScaleY(0.7f);
        dialogView.animate()
                .scaleX(1.0f)
                .scaleY(1.0f)
                .setDuration(200)
                .start();
    }

    private void showResetConfirmationDialog() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Сброс прогресса")
                .setMessage("Вы уверены, что хотите сбросить весь прогресс? Это действие нельзя отменить.")
                .setPositiveButton("Да", (dialog, which) -> {
                    Log.d(TAG, "Resetting progress for user " + USER_ID);
                    dbHelper.resetProgress(USER_ID);
                    updateLevelAndProgress();
                    achievementAdapter.updateAchievements(dbHelper.getUserAchievements(USER_ID));
                    Log.d(TAG, "Progress reset completed");
                })
                .setNegativeButton("Нет", (dialog, which) -> dialog.dismiss())
                .setCancelable(true)
                .show();
    }
}