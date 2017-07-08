package com.xyzlf.custom.keyboardlib;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class KeyboardDialog extends BaseDialog implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private PasswordView mPasswordView;
    private KeyboardGridAdapter mKeyboardAdapter;

    private IKeyboardListener mOnListener;

    public KeyboardDialog(Context context) {
        this(context, R.style.KeyboardDialog);
    }

    public KeyboardDialog(Context context, int style) {
        super(context, style);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keyboard_dialog);
        setDialogSize(getContext().getResources().getDisplayMetrics().widthPixels, 0);
        setCanceledOnTouchOutside(false);
        findViewById(R.id.dialog_blank).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.dialog_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.dialog_forget_pwd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnListener) {
                    mOnListener.onForgetPwd();
                }
            }
        });

        mPasswordView = (PasswordView) findViewById(R.id.dialog_password);
        mPasswordView.setOnPasswordListener(new PasswordView.OnPasswordListener() {
            @Override
            public void onPasswordChange(String pwd) {
                if (null != mOnListener) {
                    mOnListener.onPasswordChange(pwd);
                }
            }

            @Override
            public void onPasswordComplete(String pwd) {
                if (null != mOnListener) {
                    mOnListener.onPasswordComplete(pwd);
                }
            }

        });
        GridView keyboardGrid = (GridView) findViewById(R.id.dialog_grid);

        List<Integer> datas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            datas.add(i);
        }
        List<Integer> randomList = randomList(datas);
        randomList.add(9, KeyboardGridAdapter.TYPE_VALUE_BANK);
        randomList.add(KeyboardGridAdapter.TYPE_VALUE_DELETE);
        mKeyboardAdapter = new KeyboardGridAdapter(randomList);
        keyboardGrid.setAdapter(mKeyboardAdapter);
        keyboardGrid.setOnItemClickListener(this);
        keyboardGrid.setOnItemLongClickListener(this);
    }

    public void setKeyboardLintener(IKeyboardListener listener) {
        this.mOnListener = listener;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null == mKeyboardAdapter) {
            return;
        }
        if (position == (mKeyboardAdapter.getCount() - 3)) {
            return;
        }
        if (position == (mKeyboardAdapter.getCount() - 1)) {
            mPasswordView.deletePassword();
        } else {
            int value = mKeyboardAdapter.getItem(position);
            mPasswordView.addPassword(value);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == mKeyboardAdapter.getCount() - 1) {
            mPasswordView.clearPassword();
            return true;
        }
        return false;
    }

    private static <V> List<V> randomList(List<V> sourceList) {
        if (isEmpty(sourceList)) {
            return sourceList;
        }

        List<V> randomList = new ArrayList<V>(sourceList.size());
        do {
            int randomIndex = Math.abs(new Random().nextInt(sourceList.size()));
            randomList.add(sourceList.remove(randomIndex));
        } while (sourceList.size() > 0);

        return randomList;
    }

    private static <V> boolean isEmpty(List<V> sourceList) {
        return (sourceList == null || sourceList.size() == 0);
    }
}
