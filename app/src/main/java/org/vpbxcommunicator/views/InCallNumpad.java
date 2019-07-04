package org.vpbxcommunicator.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.Collection;
import org.vpbxcommunicator.R;

/** written by Farhat Samara */
public class InCallNumpad extends LinearLayout implements AddressAware {
    private final boolean mPlayDtmf;

    public InCallNumpad(Context context, boolean mPlayDtmf) {
        super(context);
        this.mPlayDtmf = mPlayDtmf;
        LayoutInflater.from(context).inflate(R.layout.in_call_numpad, this);
        setLongClickable(true);
        onFinishInflate();
    }

    public InCallNumpad(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Numpad);
        mPlayDtmf = 1 == a.getInt(org.vpbxcommunicator.R.styleable.Numpad_play_dtmf, 1);
        a.recycle();
        LayoutInflater.from(context).inflate(R.layout.numpad, this);
        setLongClickable(true);
    }

    @Override
    protected final void onFinishInflate() {
        for (Digit v : retrieveChildren(this, Digit.class)) {
            v.setPlayDtmf(mPlayDtmf);
        }
        super.onFinishInflate();
    }

    public void setAddressWidget(AddressText address) {
        for (AddressAware v : retrieveChildren(this, AddressAware.class)) {
            v.setAddressWidget(address);
        }
    }

    private <T> Collection<T> retrieveChildren(ViewGroup viewGroup, Class<T> clazz) {
        final Collection<T> views = new ArrayList<>();

        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View v = viewGroup.getChildAt(i);
            if (v instanceof ViewGroup) {
                views.addAll(retrieveChildren((ViewGroup) v, clazz));
            } else {
                if (clazz.isInstance(v)) views.add(clazz.cast(v));
            }
        }

        return views;
    }
}
