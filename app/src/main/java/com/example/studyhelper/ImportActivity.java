package com.example.studyhelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.VolleyError;
import java.util.List;

public class ImportActivity extends AppCompatActivity {

    private LinearLayout mSubjectLayoutContainer;
    private StudyFetcher mStudyFetcher;
    private ProgressBar mLoadingProgressBar;
    private boolean mDarkTheme;
    private SharedPreferences mSharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mDarkTheme = mSharedPrefs.getBoolean(SettingsFragment.PREFERENCE_THEME, false);
        if (mDarkTheme) {
            setTheme(R.style.DarkTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);

        mSubjectLayoutContainer = findViewById(R.id.subjectLayout);

        // Show progress bar
        mLoadingProgressBar = findViewById(R.id.loadingProgressBar);
        mLoadingProgressBar.setVisibility(View.VISIBLE);

        mStudyFetcher = new StudyFetcher(this);
        mStudyFetcher.fetchSubjects(mFetchListener);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.import_questions, menu);
        return true;
    }

    private StudyFetcher.OnStudyDataReceivedListener mFetchListener = new StudyFetcher.OnStudyDataReceivedListener() {

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onSubjectsReceived(List<Subject> subjects) {

            // Hide progress bar
            mLoadingProgressBar.setVisibility(View.GONE);

            // Create a checkbox for each subject
            for (Subject subject: subjects) {
                CheckBox checkBox = new CheckBox(getApplicationContext());
                checkBox.setTextSize(24);
                checkBox.setText(subject.getText());
                checkBox.setTag(subject);
                if (mDarkTheme) {
                    checkBox.setTextAppearance(getApplicationContext(), R.style.CheckTheme);
                    checkBox.setBackgroundColor(getResources().getColor(R.color.colorCheckBox));
                }
                mSubjectLayoutContainer.addView(checkBox);
            }
        }

        @Override
        public void onQuestionsReceived(List<Question> questions) {

            if (questions.size() > 0) {
                StudyDatabase studyDb = StudyDatabase.getInstance(getApplicationContext());

                // Add the questions to the database
                for (Question question : questions) {
                    studyDb.questionDao().insertQuestion(question);
                }

                String subject = questions.get(0).getSubject();
                Toast.makeText(getApplicationContext(), subject + " imported successfully",
                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getApplicationContext(), "Error loading subjects. Try again later.",
                    Toast.LENGTH_LONG).show();
            mLoadingProgressBar.setVisibility(View.GONE);
        }
    };

    public void importButtonClick(View view) {

        StudyDatabase dbHelper = StudyDatabase.getInstance(getApplicationContext());

        // Determine which subjects were selected
        int numCheckBoxes = mSubjectLayoutContainer.getChildCount();
        for (int i = 0; i < numCheckBoxes; i++) {
            CheckBox checkBox = (CheckBox) mSubjectLayoutContainer.getChildAt(i);
            if (checkBox.isChecked()) {
                Subject subject = (Subject) checkBox.getTag();

                // Add subject to the database
                dbHelper.subjectDao().insertSubject(subject);
                mStudyFetcher.fetchQuestions(subject, mFetchListener);
            }
        }
    }
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), SubjectActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
}