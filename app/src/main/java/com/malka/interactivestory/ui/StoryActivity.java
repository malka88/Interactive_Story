package com.malka.interactivestory.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.malka.interactivestory.R;
import com.malka.interactivestory.model.Page;
import com.malka.interactivestory.model.Story;

import java.util.Stack;

public class StoryActivity extends AppCompatActivity {
    private String name;
    private Story story;
    private ImageView storyImageView;
    private TextView storyTextView;
    private Button choice1Button;
    private Button choice2Button;
    private Stack<Integer> pageStack = new Stack<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        storyImageView = findViewById(R.id.storyImageView);
        storyTextView = findViewById(R.id.storyTextView);
        choice1Button = findViewById(R.id.choice1Button);
        choice2Button = findViewById(R.id.choice2Button);

        Intent intent = getIntent();
        name = intent.getStringExtra(getString(R.string.key_name));
        if (name == null || name.isEmpty()) name = "Noname";

        story = new Story();
        loadPage(0);
    }

    private void loadPage(int pageNumber) {
        pageStack.push(pageNumber);
        Page page = story.getPage(pageNumber);

        Drawable image = ContextCompat.getDrawable(this, page.getImageId());
        storyImageView.setImageDrawable(image);

        String pageText = getString(page.getTextId());
        pageText = String.format(pageText, name);
        storyTextView.setText(pageText);

        if (page.isFinalPage()) {
            choice1Button.setVisibility(View.INVISIBLE);
            choice2Button.setText(R.string.play_again_button_text);
            choice2Button.setOnClickListener(view -> {
                loadPage(0);
                pageStack.clear();
            });
        }
        else {
            loadButtons(page);
        }
    }

    private void loadButtons(Page page) {
        choice1Button.setVisibility(View.VISIBLE);
        choice1Button.setText(page.getChoice1().getTextId());
        choice1Button.setOnClickListener(view -> {
            int nextPage = page.getChoice1().getNextPage();
            loadPage(nextPage);
        });

        choice2Button.setText(page.getChoice2().getTextId());
        choice2Button.setOnClickListener(view -> {
            int nextPage = page.getChoice2().getNextPage();
            loadPage(nextPage);
        });
    }

    @Override
    public void onBackPressed() {
        pageStack.pop();
        if (pageStack.isEmpty()) super.onBackPressed();
        else loadPage(pageStack.pop());
    }
}