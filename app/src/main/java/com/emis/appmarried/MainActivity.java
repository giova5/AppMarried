package com.emis.appmarried;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.cleveroad.slidingtutorial.TutorialFragment;
import com.cleveroad.slidingtutorial.TutorialOptions;
import com.cleveroad.slidingtutorial.TutorialPageProvider;
import com.emis.appmarried.tutorial.TempFacebookLoginFragment;
import com.emis.appmarried.tutorial.TutorialPageFragment;

public class MainActivity extends AppCompatActivity {

    private static final int PAGES = 5;
    private static final int[] pagesColors = new int[]{Color.parseColor("#DE005B"), Color.parseColor("#FFCF1E"), Color.parseColor("#00BDF2"), Color.parseColor("#81C80C")};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);

         setTutorial();
    }

    private void setTutorial(){

        TutorialPageProvider<Fragment> tutorialPageProvider = new TutorialPageProvider<Fragment>() {
            @NonNull
            @Override
            public Fragment providePage(int position) {
                switch (position){
                    case 0:
                        return TutorialPageFragment.newInstance(TutorialPageFragment.FIRST_TUTORIAL_PAGE);
                    case 1:
                        return TutorialPageFragment.newInstance(TutorialPageFragment.SECOND_TUTORIAL_PAGE);
                    case 2:
                        return TutorialPageFragment.newInstance(TutorialPageFragment.THIRD_TUTORIAL_PAGE);
                    case 3:
                        return TutorialPageFragment.newInstance(TutorialPageFragment.FOURTH_TUTORIAL_PAGE);
                    case 4:
                        return new TempFacebookLoginFragment();
                    default:
                        return TutorialPageFragment.newInstance(TutorialPageFragment.FIRST_TUTORIAL_PAGE);
                }
            }
        };

        final TutorialOptions tutorialOptions = TutorialFragment.newTutorialOptionsBuilder(this)
                .setPagesCount(PAGES)
                .setPagesColors(pagesColors)
                .setOnSkipClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //TODO: shared + go to login
                    }
                })
                .setTutorialPageProvider(tutorialPageProvider)
                .build()
                ;

        final TutorialFragment tutorialFragment = TutorialFragment.newInstance(tutorialOptions);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.content, tutorialFragment)
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Facebook", "onActivityResult() activity");
    }
}
