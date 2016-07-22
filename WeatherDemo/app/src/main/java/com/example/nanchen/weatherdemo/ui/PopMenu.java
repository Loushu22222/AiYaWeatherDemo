package com.example.nanchen.weatherdemo.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.nanchen.weatherdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义PopMenu
 * Created by nanchen on 2016/6/21.
 */
public class PopMenu extends PopupWindow implements AdapterView.OnItemClickListener, View.OnFocusChangeListener {

    private View mainView;

    private ListView mLvItem;

    private MenuItemAdapter mAdapter;

    private List<String> mItemData = new ArrayList<>();

    private OnPopMenuItemClickListener mItemClickListener;

    public interface OnPopMenuItemClickListener {
        void onItemClick(String title);
    }

    public PopMenu(Activity context) {
        this(context, null);
    }

    public PopMenu(Activity context, OnPopMenuItemClickListener listener) {
        super(context);
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        mItemClickListener = listener;
        // 窗口布局
        mainView = LayoutInflater.from(context).inflate(R.layout.layout_pop_menu, null);
        mLvItem = (ListView) mainView.findViewById(R.id.lv_pop_menu);
        mAdapter = new MenuItemAdapter(context, mItemData);
        mLvItem.setAdapter(mAdapter);
        mLvItem.setOnItemClickListener(this);
        setContentView(mainView);
        setWidth(w / 3);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置显示和隐藏动画
        setAnimationStyle(R.style.PopMenuAnim);
        // 设置背景透明
        setBackgroundDrawable(new ColorDrawable(0));
        setFocusable(true);
        getContentView().setOnFocusChangeListener(this);
    }

    public void setOnPopMenuItemClickListener(OnPopMenuItemClickListener listener) {
        mItemClickListener = listener;
    }

    /**
     * 增加一条数目
     * @param item 一条数目
     */
    public void addItem(String item) {
        if (!mItemData.contains(item)) {
            mItemData.add(item);
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 增加多条显示
     * @param items  显示条目数组
     */
    public void addItems(String[] items) {
        for (String item : items) {
            if (!mItemData.contains(item)) {
                mItemData.add(item);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 更新
     */
    public void update() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mItemClickListener != null) {
            mItemClickListener.onItemClick(mItemData.get(position));
        }
        dismiss();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            dismiss();
        }

    }

    private class MenuItemAdapter extends ArrayAdapter<String> {

        private Context mContext;

        /**
         * 创建一个新的实例 MenuItemAdapter.
         *
         * @param context  上下文环境
         * @param objects  所装的
         */
        public MenuItemAdapter(Context context, List<String> objects) {
            super(context, R.layout.layout_pop_menu_item, objects);
            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder item = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_pop_menu_item, parent, false);
                item = new ViewHolder();
                item.mTvTitle = (TextView) convertView.findViewById(R.id.tv_pop_menu_item);
                convertView.setTag(item);
            } else {
                item = (ViewHolder) convertView.getTag();
            }
            item.mTvTitle.setText(getItem(position));

            return convertView;
        }

        private class ViewHolder {
            TextView mTvTitle;
        }
    }
}
