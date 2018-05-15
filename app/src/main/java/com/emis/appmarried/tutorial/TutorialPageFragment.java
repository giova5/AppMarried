package com.emis.appmarried.tutorial;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.cleveroad.slidingtutorial.Direction;
import com.cleveroad.slidingtutorial.PageFragment;
import com.cleveroad.slidingtutorial.TransformItem;
import com.emis.appmarried.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jo5 on 14/05/18.
 */

public class TutorialPageFragment extends PageFragment {

    private static final String TUTORIAL_PAGE_ID = "TUTORIAL_PAGE_ID";

    public static final int FIRST_TUTORIAL_PAGE = 0;
    public static final int SECOND_TUTORIAL_PAGE = 1;
    public static final int THIRD_TUTORIAL_PAGE = 2;
    public static final int FOURTH_TUTORIAL_PAGE = 3;

    private int tutorialPageID;

    @Override
    protected int getLayoutResId() {
        return getLayoutFromID();
    }

    public static TutorialPageFragment newInstance(int pageID) {
        TutorialPageFragment fragment = new TutorialPageFragment();
        Bundle args = getFragmentArguments(pageID);
        fragment.setArguments(args);
        return fragment;
    }

    public static Bundle getFragmentArguments(int pageID){
        Bundle args = new Bundle();
        args.putInt(TUTORIAL_PAGE_ID, pageID);
        return args;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle args = getArguments();
            tutorialPageID = args.getInt(TUTORIAL_PAGE_ID);
        }
    }

    @NonNull
    @Override
    protected TransformItem[] getTransformItems() {

        Log.d("TutorialPageFragment", "getTransformItems() called");

        List<TransformItem> transformItemList = new ArrayList<>();

        transformItemList.clear();

        switch (tutorialPageID){
            case FIRST_TUTORIAL_PAGE:
                transformItemList.add(TransformItem.create(R.id.background_icon, Direction.LEFT_TO_RIGHT, 0.5f));
                transformItemList.add(TransformItem.create(R.id.first_icon, Direction.LEFT_TO_RIGHT, 0.2f));
                break;
            case SECOND_TUTORIAL_PAGE:
                transformItemList.add(TransformItem.create(R.id.background_icon, Direction.LEFT_TO_RIGHT, 0.5f));
                break;
            case THIRD_TUTORIAL_PAGE:
                transformItemList.add(TransformItem.create(R.id.background_icon, Direction.LEFT_TO_RIGHT, 0.5f));
                transformItemList.add(TransformItem.create(R.id.first_icon, Direction.LEFT_TO_RIGHT, 0.2f));
                transformItemList.add(TransformItem.create(R.id.second_icon, Direction.LEFT_TO_RIGHT, 0.2f));
                transformItemList.add(TransformItem.create(R.id.third_icon, Direction.LEFT_TO_RIGHT, 0.2f));
                break;
            case FOURTH_TUTORIAL_PAGE:
                transformItemList.add(TransformItem.create(R.id.background_icon, Direction.LEFT_TO_RIGHT, 0.5f));
                break;
            default:
                break;
        }

        TransformItem[] transformItemsArray = new TransformItem[transformItemList.size()];
        transformItemList.toArray(transformItemsArray);

        Log.d("TutorialPageFragment", "ID page: " + tutorialPageID + " Array: " + transformItemsArray.length);

        return transformItemsArray;
    }

    private int getLayoutFromID(){

        int layoutID;

        switch (tutorialPageID){
            case FIRST_TUTORIAL_PAGE:
                layoutID = R.layout.tutorial_page_fragment_first;
                break;
            case SECOND_TUTORIAL_PAGE:
                layoutID = R.layout.tutorial_page_fragment_second;
                break;
            case THIRD_TUTORIAL_PAGE:
                layoutID = R.layout.tutorial_page_fragment_third;
                break;
            case FOURTH_TUTORIAL_PAGE:
                layoutID = R.layout.tutorial_page_fragment_fourth;
                break;
            default:
                layoutID = R.layout.tutorial_page_fragment_first;
                break;
        }

        return layoutID;
    }
}
