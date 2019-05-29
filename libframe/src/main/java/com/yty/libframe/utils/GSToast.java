package com.yty.libframe.utils;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yty.libframe.R;

public class GSToast extends Toast {
    private Context mContext;

    /**
     * Construct an empty GSToast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public GSToast(Context context) {
        super(context.getApplicationContext());
        this.mContext = context.getApplicationContext();
    }

    /**
     * Make a standard toast that just contains a text view with the text from a resource.
     *
     * @param context  The context to use.  Usually your {@link Application}
     *                 or {@link Activity} object.
     * @param resId    The resource id of the string resource to use.  Can be formatted text.
     * @param duration How long to display the message.  Either {@link #LENGTH_SHORT} or
     *                 {@link #LENGTH_LONG}
     *
     * @throws Resources.NotFoundException if the resource can't be found.
     */
    public static Toast makeText(Context context, @StringRes int resId, int duration)
            throws Resources.NotFoundException {
        return makeText(context, context.getResources().getText(resId), duration);
    }

    /**
     * Make a standard toast that just contains a text view.
     *
     * @param context  The context to use.  Usually your {@link Application}
     *                 or {@link Activity} object.
     * @param text     The text to show.  Can be formatted text.
     * @param duration How long to display the message.  Either {@link #LENGTH_SHORT} or
     *                 {@link #LENGTH_LONG}
     *
     */
    public static Toast makeText(Context context, CharSequence text, int duration) {
        Toast result = new GSToast(context);

        LayoutInflater inflate = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.toast_layout, null);
        TextView tv = (TextView)v.findViewById(R.id.text);
        tv.setText(text);

        result.setView(v);
        result.setDuration(duration);

        return result;
    }

    /**
     * Update the text in a GSToast that was previously created using one of the makeText() methods.
     * @param resId The new text for the GSToast.
     */
    public void setText(@StringRes int resId) {
        setText(mContext.getText(resId));
    }

    /**
     * Update the text in a GSToast that was previously created using one of the makeText() methods.
     * @param s The new text for the GSToast.
     */
    public void setText(CharSequence s) {
        View view = getView();
        if (view == null) {
            throw new RuntimeException("This Toast was not created with GSToast.makeText()");
        }

        TextView tv = (TextView) view.findViewById(R.id.text);
        if (tv == null) {
            throw new RuntimeException("This Toast was not created with GSToast.makeText()");
        }
        tv.setText(s);
    }
}