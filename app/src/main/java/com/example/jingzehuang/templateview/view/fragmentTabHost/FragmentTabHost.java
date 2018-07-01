package com.example.jingzehuang.templateview.view.fragmentTabHost;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.example.jingzehuang.templateview.R;


    /**
     * Created by Raych on Jun.09, 18.
     */

    public abstract class FragmentTabHost extends Fragment implements TabHost.OnTabChangeListener{

        @BindView(R.id.history_fragment_tab_host)TabHost tabHost;

        private TabHost mainView;
        private List data;
        private List<String> tagList;
        private final HashMap<String, Fragment> FRAGMENTS_STUB = new HashMap<>();

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater,
                                 @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            mainView = (TabHost) inflater.inflate(R.layout.fragment_fragment_tab_host, container, false);
            ButterKnife.bind(this, mainView);
            getActivity().setTitle("Booking History");
            tagList = new ArrayList<>();
            tagList.add("Driver");
            tagList.add("Host");
            return mainView;
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            tabHost.setup();
            mainView.setOnTabChangedListener(this);

//        initiateTagList();

            tabHost.addTab(tabHost.newTabSpec("Driver").setIndicator("I'M A DRIVER").setContent(new DummyTabContent(getContext())));
            tabHost.addTab(tabHost.newTabSpec("Host").setIndicator("I'M A HOST").setContent(new DummyTabContent(getContext())));
        }

        @Override
        public void onTabChanged(String tabId) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            detachAllFragments(fm, ft);

            Fragment selectedTabFragment = FRAGMENTS_STUB.get(tabId);
            Log.d("Raych", "tabId is: " + tabId);
            if (selectedTabFragment != null) {
                Log.d("Raych", "selectedTabFragment is not null.");
                ft.attach(selectedTabFragment);
            } else {
                Log.d("Raych", "selectedTabFragment is null.");
                selectedTabFragment = getItem(tabId);
                Log.d("Raych", "selectedTabFragment is generated: " + (selectedTabFragment != null));
                FRAGMENTS_STUB.put(tabId, selectedTabFragment);
                ft.add(R.id.real_tabcontent, selectedTabFragment);
            }
            ft.commit();
        }

        private void initiateTagList() {
            if (tagList != null) {
                return;
            }
            tagList = new ArrayList<>();
            for (int i = 0, len = data.size(); i < len; i++) {
                tagList.add("" + (i + 1));
            }
        }

        public void setTagList(List<String> tagList) {
            this.tagList = tagList;
        }

        private void detachAllFragments(FragmentManager fm, FragmentTransaction ft) {
            for (Map.Entry<String, Fragment> entry : FRAGMENTS_STUB.entrySet()) {
                Fragment fragment = entry.getValue();
                if (fragment != null) {
                    ft.detach(fragment);
                }
            }
        }

        public abstract Fragment getItem(String tabId);

    }
