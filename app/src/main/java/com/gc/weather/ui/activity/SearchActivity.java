package com.gc.weather.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.weather.R;
import com.gc.weather.presenter.SearchPresenter;
import com.gc.weather.ui.adapter.ArrayListAdapter;
import com.gc.weather.entity.City;
import com.gc.weather.common.ToastUtil;
import com.gc.weather.view.ISearchView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity implements ISearchView {

    private EditText mSearchText;
    private Button mSearchButton;
    private ListView mListView;
    private List<City> mCityList;
    private ArrayListAdapter mAdapter;
    private ProgressDialog mProgressDialog;
    private SearchPresenter mPresenter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_search);
        initListView();
        initEditView();
        mPresenter = new SearchPresenter(this);
    }

    private void initListView() {
        mListView = (ListView) findViewById(R.id.id_search_result_list);
        TextView emptyView = (TextView) findViewById(R.id.id_empty_view);
        mListView.setEmptyView(emptyView);
        mCityList = new ArrayList<>();
        mAdapter = new ArrayListAdapter(this, mCityList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("city", mCityList.get(position));
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void initEditView() {
        mSearchButton = (Button) findViewById(R.id.id_search_button);
        mSearchText = (EditText) findViewById(R.id.id_search_text);
        // 监听输入，当输入为空时显示取消，不为空显示为搜索
        mSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // 输入空格会改变状态
                if (mSearchText.getText().length() > 0) {
                    mSearchButton.setText(getString(R.string.search));
                } else {
                    mSearchButton.setText(getString(R.string.search_cancel));
                }
            }
        });
        // 监听键盘输入，使键盘确定键显示为搜索样式
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 这个判断是防止用户输入空格后点击键盘上的搜索按钮出现关闭的问题
                    if (mSearchText.getText().toString().trim().isEmpty()) {
                        ToastUtil.show(getString(R.string.search_tips));
                        return true;
                    }
                    mPresenter.search(SearchActivity.this);
                    return true;
                }
                return false;
            }
        });
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.search(SearchActivity.this);
            }
        });
    }

    @Override
    protected void fetchData() {
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideLoadingDialog();
        mPresenter = null;
    }

    @Override
    public String getSearchString() {
        return mSearchText.getText().toString();
    }

    @Override
    public EditText getSearchView() {
        return mSearchText;
    }

    @Override
    public List<City> getResultList() {
        return mCityList;
    }

    @Override
    public ListView getListView() {
        return mListView;
    }

    @Override
    public ArrayListAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void showLoadingDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("搜索中...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    @Override
    public void hideLoadingDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
