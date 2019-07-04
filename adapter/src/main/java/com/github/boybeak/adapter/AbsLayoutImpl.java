package com.github.boybeak.adapter;

import java.util.UUID;

/**
 * Created by gaoyunfei on 2018/3/8.
 */

public abstract class AbsLayoutImpl<Data>
        implements LayoutImpl<Data> {

    private Data data;
    private String id = null;

    private boolean selected = false, isSelectable = false;

    @Override
    public final String id() {
        return id;
    }

    @Override
    public void setId(String id) {
        if (id == null) {
            throw new IllegalStateException("id for BaseLayoutImpl can not be null");
        }
        this.id = id;
    }

    public AbsLayoutImpl(Data data) {
        this(data, UUID.randomUUID().toString());
    }

    public AbsLayoutImpl(Data data, String id) {
        this.data = data;
        setId(id);
    }

    @Override
    public Data getSource() {
        return data;
    }

    @Override
    public <T> T getSourceUnSafe() {
        return (T)getSource();
    }

    @Override
    public void setSource (Data data) {
        this.data = data;
    }

    @Override
    public Class<? extends AbsDataBindingHolder> getHolderClass() {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof LayoutImpl) {
            return id().equals(((LayoutImpl) obj).id()) || getSource().equals(((LayoutImpl) obj).getSource());
        } else if (data.getClass().isInstance(obj)) {
            return data.equals(obj);
        }
        return false;
    }

    @Override
    public void setSelected(boolean selected) {
        if (!isSelectable()) {
            return;
        }
        this.selected = selected;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelectable(boolean selectable) {
        if (!supportSelect()) {
            return;
        }
        isSelectable = selectable;
    }

    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public boolean supportSelect() {
        return false;
    }
}
