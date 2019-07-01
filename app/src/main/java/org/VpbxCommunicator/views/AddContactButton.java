package org.VpbxCommunicator.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

@SuppressLint("AppCompatCustomView")
public class AddContactButton extends ImageView
        implements AddressAware, View.OnClickListener, View.OnLongClickListener, TextWatcher {

    private AddressText mAddress;

    public AddContactButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEnabled(false);
        setOnClickListener(this);
        setOnLongClickListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        setEnabled(s.length() > 0);

        // set erasebutton visible and invisible accordingly
        setVisibility(VISIBLE);
        if (mAddress.length() == 0) {
            setVisibility(INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if (mAddress.getText().length() > 0) {
            int lBegin = mAddress.getSelectionStart();
            if (lBegin == -1) {
                lBegin = mAddress.getEditableText().length() - 1;
            }
            if (lBegin > 0) {
                mAddress.getEditableText().delete(lBegin - 1, lBegin);
            }
        }
        setEnabled(mAddress.getText().length() > 0);
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void setAddressWidget(AddressText address) {
        mAddress = address;
        address.addTextChangedListener(this);
    }
}
