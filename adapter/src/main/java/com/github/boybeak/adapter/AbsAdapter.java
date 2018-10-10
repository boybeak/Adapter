package com.github.boybeak.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.github.boybeak.adapter.annotation.Constant;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class AbsAdapter extends RecyclerView.Adapter<AbsDataBindingHolder> {

//    private SparseArrayCompat<Class<? extends AbsDataBindingHolder>> mTypeHolderMap = null; // key -- layout, value -- holderClass

    private LayoutInflater mInflater;
    private HolderFactory mFactory;

    public AbsAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
//        mTypeHolderMap = new SparseArrayCompat<>();
        mFactory = getFactory();
    }

    private HolderFactory getFactory() {
        try {
            Class factoryClz = Class.forName(Constant.PACKAGE + "." + Constant.FACTORY);
            return (HolderFactory)factoryClz.getConstructor().newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public AbsDataBindingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(mInflater, viewType, parent, false);
        return mFactory.getHolder(viewType, binding);
//        return getHolder(viewType, binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AbsDataBindingHolder holder, int position) {
        holder.bindData(getItem(position), position, this);
    }

    /*private AbsDataBindingHolder getHolder (int viewType, ViewDataBinding binding) {
        if (mTypeHolderMap.indexOfKey(viewType) >= 0) {
            Class<? extends AbsDataBindingHolder> clz = mTypeHolderMap.get(viewType);
            if (clz != null) {
                Constructor<? extends AbsDataBindingHolder> constructor = getConstructor(clz, binding);
                if (constructor != null) {
                    try {
                        return constructor.newInstance(binding);
                    } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        throw new IllegalStateException("Can not create ViewHolder for viewType=0X" + Integer.toHexString(viewType));
    }*/

    private Constructor<? extends AbsDataBindingHolder> getConstructor(Class<? extends AbsDataBindingHolder> clz, ViewDataBinding binding) {
        try {
            //For data binding v1
            return clz.getConstructor(binding.getClass());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            try {
                //For data binding v2
                return clz.getConstructor(binding.getClass().getSuperclass());
            } catch (NoSuchMethodException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        LayoutImpl layout = getItem(position);
        /*int type = layout.getLayout();
        Class<? extends AbsDataBindingHolder> holderClz = layout.getHolderClass();
        if (holderClz == null) {
            throw new IllegalStateException("class not be defined by class(" + layout.getClass().getName()
                    + "), please define a layout resource id by getHolderClass");
        }
        if (type <= 0) {
            throw new IllegalStateException("layout not be defined by class(" + layout.getClass().getName()
                    + "), please define a layout resource id by getLayout");
        }
        if (mTypeHolderMap.indexOfKey(type) < 0) {
            mTypeHolderMap.put(type, holderClz);
        }*/
        return layout.getLayout();
    }

    public abstract @NonNull LayoutImpl getItem (int position);
    public abstract int index(LayoutImpl layout);
}
