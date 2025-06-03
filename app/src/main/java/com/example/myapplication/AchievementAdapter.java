package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textview.MaterialTextView;
import java.util.List;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.AchievementViewHolder> {

    private List<DatabaseHelper.Achievement> achievements;
    private OnAchievementClickListener listener;

    public interface OnAchievementClickListener {
        void onAchievementClick(DatabaseHelper.Achievement achievement);
    }

    public AchievementAdapter(List<DatabaseHelper.Achievement> achievements, OnAchievementClickListener listener) {
        this.achievements = achievements;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AchievementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.achievement_item, parent, false);
        return new AchievementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementViewHolder holder, int position) {
        DatabaseHelper.Achievement achievement = achievements.get(position);
        holder.achievementName.setText(achievement.testName);
        holder.achievementIcon.setAlpha(1.0f); // Все достижения в списке уже получены
        holder.itemView.setOnClickListener(v -> listener.onAchievementClick(achievement));

        // Анимация появления
        holder.itemView.setScaleX(0.8f);
        holder.itemView.setScaleY(0.8f);
        holder.itemView.animate()
                .scaleX(1.0f)
                .scaleY(1.0f)
                .setDuration(300)
                .start();
    }

    @Override
    public int getItemCount() {
        return achievements.size();
    }

    static class AchievementViewHolder extends RecyclerView.ViewHolder {
        ImageView achievementIcon;
        MaterialTextView achievementName;

        AchievementViewHolder(@NonNull View itemView) {
            super(itemView);
            achievementIcon = itemView.findViewById(R.id.achievement_icon);
            achievementName = itemView.findViewById(R.id.achievement_name);
        }
    }

    public void updateAchievements(List<DatabaseHelper.Achievement> newAchievements) {
        this.achievements = newAchievements;
        notifyDataSetChanged();
    }
}