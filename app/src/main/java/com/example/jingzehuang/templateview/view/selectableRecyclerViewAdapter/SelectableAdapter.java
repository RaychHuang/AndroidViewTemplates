package com.example.jingzehuang.templateview.view.selectableRecyclerViewAdapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashSet;
import java.util.List;

import butterknife.ButterKnife;

public abstract class SelectableAdapter extends RecyclerView.Adapter{

    private final boolean IS_MULTI_CHECK_MODE;
    private final int NO_VIEW_SELECTED = -1;

    //for multi-selected check
    private HashSet<Integer> selectedViewSet;
    //for single-selected check
    private int selectedView = NO_VIEW_SELECTED;


    public SelectableAdapter(boolean isMultiCheck) {
        this.IS_MULTI_CHECK_MODE = isMultiCheck;
        if (IS_MULTI_CHECK_MODE) {
            selectedViewSet = new HashSet<>();
        } else {
            selectedViewSet = new HashSet<>(10);
        }
        selectedViewSet.add(NO_VIEW_SELECTED);
    }

    @NonNull
    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return onCreateSelectableViewHolder(inflater, parent, viewType);
    }

    @Override
    public final void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        onBindViewHolder(holder, position,null);
    }

    @Override
    public final void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @Nullable List payloads) {
        SelectableViewHolder selectableViewHolder = (SelectableViewHolder) holder;
        selectableViewHolder.setCheckBox(selectedViewSet.contains(position));
        onBindSelectableViewHolder(selectableViewHolder, position);
    }

    public abstract SelectableViewHolder onCreateSelectableViewHolder(LayoutInflater inflater, @NonNull ViewGroup parent, int viewType);

    public abstract void onBindSelectableViewHolder(@NonNull SelectableViewHolder holder, int position);

    private boolean addToSelectedViewSet(int index) {
        if (!IS_MULTI_CHECK_MODE || selectedViewSet.contains(NO_VIEW_SELECTED)) {
            selectedViewSet.clear();
        }
        return selectedViewSet.add(index);
    }

    public void setSelectedView(int index) {
        if (IS_MULTI_CHECK_MODE) {
            setMultiSelectedView(index);
        } else {
            setSingleSelectedView(index);
        }
    }

    public void setSingleSelectedView(int index) {
        if (index < 0 || index >= getItemCount()) {
            return;
        }

        // check if click the same one
        if (selectedViewSet.contains(index)) {
            return;
        }

        // notify old View content
        int oldSelectedViewPosition = selectedViewSet.iterator().next();
        if (oldSelectedViewPosition != NO_VIEW_SELECTED) {
            notifyItemChanged(oldSelectedViewPosition, "");
        }
        notifyItemChanged(index, "");

        addToSelectedViewSet(index);
//        final TabHost.TabSpec spec = mTabSpecs.get(index);
//
//        // Call the tab widget's focusCurrentTab(), instead of just
//        // selecting the tab.
//        mTabWidget.focusCurrentTab(mCurrentTab);
//
//        // tab content
//        mCurrentView = spec.mContentStrategy.getContentView();
//
//        if (mCurrentView.getParent() == null) {
//            mTabContent
//                    .addView(
//                            mCurrentView,
//                            new ViewGroup.LayoutParams(
//                                    ViewGroup.LayoutParams.MATCH_PARENT,
//                                    ViewGroup.LayoutParams.MATCH_PARENT));
//        }
//
//        if (!mTabWidget.hasFocus()) {
//            // if the tab widget didn't take focus (likely because we're in touch mode)
//            // give the current tab content view a shot
//            mCurrentView.requestFocus();
//        }
//
//        //mTabContent.requestFocus(View.FOCUS_FORWARD);
//        invokeOnTabChangeListener();
    }

    public void setMultiSelectedView(int index) {}

    public abstract class SelectableViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public SelectableViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        protected abstract View getCheckBox();

        protected abstract void checkBoxMethod(View checkBox, boolean isChecked);

        public final void setCheckBox(boolean isChecked) {
            checkBoxMethod(getCheckBox(), isChecked);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            int layoutPosition = getLayoutPosition();
            Log.d("Raych", "adapterPosition: " + adapterPosition + ". layoutPosition: " + layoutPosition + ".");
            if (SelectableAdapter.this.IS_MULTI_CHECK_MODE) {

            } else {
                SelectableAdapter.this.setSelectedView(getLayoutPosition());
            }
            onViewHolderClick(this);
        }

        public void onViewHolderClick(SelectableViewHolder viewHolder) {
        }
    }

}
