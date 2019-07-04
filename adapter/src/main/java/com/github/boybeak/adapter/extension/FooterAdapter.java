package com.github.boybeak.adapter.extension;

import android.content.Context;
import android.support.annotation.StringRes;

import com.github.boybeak.adapter.DataBindingAdapter;
import com.github.boybeak.adapter.DataChange;
import com.github.boybeak.adapter.LayoutImpl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by gaoyunfei on 2018/3/9.
 */

public class FooterAdapter extends DataBindingAdapter {

    private AbsFooterLayout mFooterImpl;

    public FooterAdapter(AbsFooterLayout footerLayout) {
        super();
        mFooterImpl = footerLayout;
        super.addFooter(mFooterImpl);
        notifyEmptyFooter();
    }

    public FooterAdapter(Class<? extends AbsFooterLayout> clz) {
        super();
        mFooterImpl = makeFooterByClz(clz);
        super.addFooter(mFooterImpl);
        notifyEmptyFooter();
    }

    private AbsFooterLayout makeFooterByClz(Class<? extends AbsFooterLayout> clz) {
        try {
            Constructor<? extends AbsFooterLayout> constructor = clz.getConstructor(Footer.class);
            return constructor.newInstance(new Footer());
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        throw  new IllegalStateException("Maybe class[" + clz.getName() + "] not exist or constructor method not "
                + clz.getSimpleName() + "(Footer footer)");
    }

    public AbsFooterLayout getFooterLayout() {
        return mFooterImpl;
    }

    public Footer getFooter() {
        return mFooterImpl.getSource();
    }

    public void notifyFooter (@Footer.State int state, String message) {
        mFooterImpl.getSource().setState(state);
        mFooterImpl.getSource().setMessage(message);
        notifyFooters();
    }

    public void notifyLoadingFooter () {
        notifyFooter(Footer.LOADING, null);
    }

    public void notifySuccessFooter(Context context, @StringRes int msgRes) {
        notifySuccessFooter(context.getString(msgRes));
    }

    public void notifySuccessFooter(Context context, @StringRes int msgRes, Object ... args) {
        notifySuccessFooter(context.getString(msgRes, args));
    }

    public void notifySuccessFooter (String message) {
        notifyFooter(Footer.SUCCESS, message);
    }

    public void notifySuccessFooter () {
        notifySuccessFooter(null);
    }

    public void notifyFailedFooter(Context context, @StringRes int msgRes) {
        notifyFailedFooter(context.getString(msgRes));
    }

    public void notifyFailedFooter(Context context, @StringRes int msgRes, Object ... args) {
        notifyFailedFooter(context.getString(msgRes, args));
    }

    public void notifyFailedFooter (String message) {
        notifyFooter(Footer.FAILED, message);
    }

    public void notifyFailedFooter () {
        notifyFailedFooter(null);
    }

    public void notifyEmptyFooter(Context context, @StringRes int msgRes) {
        notifyEmptyFooter(context.getString(msgRes));
    }

    public void notifyEmptyFooter(Context context, @StringRes int msgRes, Object ... args) {
        notifyEmptyFooter(context.getString(msgRes, args));
    }

    public boolean isNone() {
        return isDataEmpty();
    }

    public void notifyEmptyFooter (String message) {
        if (isNone()) {
            notifyFooter(Footer.NO_ONE, message);
        } else {
            notifyFooter(Footer.NO_MORE, message);
        }
    }

    public void notifyEmptyFooter () {
        notifyEmptyFooter(null);
    }

    public boolean isLoading () {
        return mFooterImpl.getSource().state == Footer.LOADING;
    }

    @Override
    public DataChange addFooter(LayoutImpl layout) {
        throw new IllegalStateException("addFooter not available for FooterAdapter");
    }
}