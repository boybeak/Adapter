package com.github.boybeak.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.boybeak.selector.Selector;
import com.github.boybeak.adapter.selection.MultipleSelection;
import com.github.boybeak.adapter.selection.Selection;
import com.github.boybeak.adapter.selection.SingleSelection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


/**
 * Created by gaoyunfei on 2018/3/7.
 */

public class DataBindingAdapter extends AbsAdapter {

    private static final String TAG = DataBindingAdapter.class.getSimpleName();

    private List<LayoutImpl> mHeaderList = null;
    private List<LayoutImpl> mDataList;
    private List<LayoutImpl> mFooterList = null;

    public DataBindingAdapter () {
        super();
        mDataList = new ArrayList<>();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull AbsDataBindingHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.onViewAttachedToWindow();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull AbsDataBindingHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.onViewDetachedFromWindow();
    }

    public List<LayoutImpl> getDataList () {
        return mDataList;
    }

    public Selector<LayoutImpl> dataSelector () {
        return dataSelector(LayoutImpl.class);
    }

    public <Layout> Selector<Layout> dataSelector (Class<Layout> clz) {
        return Selector.selector(clz, mDataList);
    }

    @Override
    public int getItemCount() {
        return getHeaderSize() + getDataSize() + getFooterSize();
    }

    public int getHeaderSize() {
        if (mHeaderList == null) {
            return 0;
        }
        return mHeaderList.size();
    }

    public int getDataSize() {
        return mDataList.size();
    }

    public int getFooterSize() {
        if (mFooterList == null) {
            return 0;
        }
        return mFooterList.size();
    }

    /*@Override
    public long getItemId(int position) {
        return position;
    }*/

    @Override
    public LayoutImpl getItem(int position) {
        if (position >= 0 && position < getHeaderSize()) {
            return mHeaderList.get(position);
        } else if (position >= getHeaderSize() && position < getHeaderSize() + getDataSize()) {
            return mDataList.get(position - getHeaderSize());
        } else {
            return mFooterList.get(position - getHeaderSize() - getDataSize());
        }
    }

    private int getFooterStartPosition () {
        return getHeaderSize() + getDataSize();
    }

    public boolean isEmpty () {
        return getItemCount() == 0;
    }

    public boolean isDataEmpty () {
        return mDataList.isEmpty();
    }

    public boolean isHeaderEmpty() {
        return mHeaderList == null || mHeaderList.isEmpty();
    }

    public boolean isFooterEmpty() {
        return mFooterList == null || mFooterList.isEmpty();
    }

    public boolean containsInHeader (Class<? extends LayoutImpl> clz) {
        if (mHeaderList == null) {
            return false;
        }
        for (LayoutImpl layout : mHeaderList) {
            if (clz.isInstance(layout)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsInHeader(Object source) {
        if (!hasHeaders()) {
            return false;
        }
        for (LayoutImpl layout : mHeaderList) {
            if (layout.getSource().equals(source)) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(LayoutImpl layout) {
        return index(layout) >= 0;
    }

    public boolean contains(Class<? extends LayoutImpl> clz) {
        return containsInData(clz) || containsInHeader(clz) || containsInFooter(clz);
    }

    public boolean contains(Object source) {
        return containsInData(source) || containsInHeader(source) || containsInFooter(source);
    }

    public boolean containsInData (Class<? extends LayoutImpl> clz) {
        for (LayoutImpl layout : getDataList()) {
            if (clz.isInstance(layout)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsInData(Object source) {
        for (LayoutImpl layout : getDataList()) {
            if (layout.getSource().equals(source)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsInFooter(Class<? extends LayoutImpl> clz) {
        if (mFooterList == null) {
            return false;
        }
        for (LayoutImpl layout : mFooterList) {
            if (clz.isInstance(layout)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsInFooter(Object source) {
        if (!hasFooters()) {
            return false;
        }
        for (LayoutImpl layout : mFooterList) {
            if (layout.getSource().equals(source)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int index(LayoutImpl layout) {
        int index = indexOfHeader(layout);
        if (index  >= 0) {
            return index;
        }
        index = getAdapterPositionOfData(layout);
        if (index >= 0) {
            return index;
        }
        index = getAdapterPositionOfFooter(layout);
        if (index >= 0) {
            return index;
        }
        return -1;
    }

    public int indexBySource (Object source) {
        int index = indexOfHeaderBySource(source);
        if (index >= 0) {
            return index;
        }
        index = indexOfDataBySource(source);
        if (index >= 0) {
            return getAdapterPositionOfData(index);
        }
        index = indexOfFooterBySource(source);
        if (index >= 0) {
            return getAdapterPositionOfFooter(index);
        }
        return -1;
    }

    public int indexOfHeader(LayoutImpl layout) {
        if (mHeaderList == null) {
            return -1;
        }
        return mHeaderList.indexOf(layout);
    }

    public int indexOfHeaderBySource(Object source) {
        if (!hasHeaders()) {
            return -1;
        }
        for (int i = 0; i < mHeaderList.size(); i++) {
            if (mHeaderList.get(i).getSource().equals(source)) {
                return i;
            }
        }
        return -1;
    }

    public int indexOfData (LayoutImpl layout) {
        return mDataList.indexOf(layout);
    }

    public int indexOfDataBySource(Object object) {
        for (int i = 0; i < mDataList.size(); i++) {
            if (mDataList.get(i).getSource().equals(object)) {
                return i;
            }
        }
        return -1;
    }

    public int indexOfFooter(LayoutImpl layout) {
        if (mFooterList == null) {
            return -1;
        }
        return mFooterList.indexOf(layout);
    }

    public int indexOfFooterBySource(Object source) {
        if (!hasFooters()) {
            return -1;
        }
        for (int i = 0; i < mFooterList.size(); i++) {
            if (mFooterList.get(i).getSource().equals(source)) {
                return i;
            }
        }
        return -1;
    }

    public <T> Selector<T> getDataSelector (Class<T> tClass) {
        return Selector.selector(tClass, mDataList);
    }

    public Selector<LayoutImpl> getDataSelector () {
        return Selector.selector(LayoutImpl.class, mDataList);
    }

    public DataChange add (LayoutImpl layout) {
        mDataList.add(layout);
        return new DataChange(this, getHeaderSize() + getDataSize() - 1, DataChange.TYPE_ITEM_INSERTED);
    }

    /**
     * @param position
     * @param layout
     * @return
     */
    public DataChange add (int position, LayoutImpl layout) {
        if (isHeaderIndex(position)) {
            mHeaderList.add(position, layout);
        } else if (isDataIndex(position)) {
            mDataList.add(position - getHeaderSize(), layout);
        } else if (isFooterIndex(position)) {
            mFooterList.add(position - getHeaderSize() - getDataSize(), layout);
        }
        return new DataChange(this, position, DataChange.TYPE_ITEM_INSERTED);
    }

    public DataChange addIfNotExist (LayoutImpl layout) {
        if (mDataList.contains(layout)) {
            return DataChange.doNothingInstance();
        }
        return add(layout);
    }

    public DataChange addIfNotExist (int position, LayoutImpl layout) {
        if (mDataList.contains(layout)) {
            return DataChange.doNothingInstance();
        }
        return add(position, layout);
    }

    public DataChange addAll (Collection<LayoutImpl> layouts) {
        int start = getHeaderSize() + getDataSize();
        mDataList.addAll(layouts);
        return new DataChange(this, start, layouts.size(),
                DataChange.TYPE_ITEM_RANGE_INSERTED);
    }

    public DataChange addAll(int position, Collection<LayoutImpl> layouts) {
        if (isHeaderIndex(position)) {
            mHeaderList.addAll(position, layouts);
        } else if (isDataIndex(position)) {
            mDataList.addAll(position - getHeaderSize(), layouts);
        } else if (isFooterIndex(position)) {
            mFooterList.addAll(position - getHeaderSize() - getDataSize(), layouts);
        }
        return new DataChange(this, position, layouts.size(),
                DataChange.TYPE_ITEM_RANGE_INSERTED);
    }

    public <Data, Layout extends LayoutImpl> DataChange addAll (Collection<Data> dataList, Converter<Data, Layout> converter) {
        List<LayoutImpl> layouts = new ArrayList<>();
        for (Data data : dataList) {
            layouts.add(converter.convert(data, this));
        }
        return addAll(layouts);
    }

    public <Data, Layout extends LayoutImpl> DataChange addAll (int position, Collection<Data> dataList, Converter<Data, Layout> converter) {
        List<LayoutImpl> layouts = new ArrayList<>();
        for (Data data : dataList) {
            layouts.add(converter.convert(data, this));
        }
        return addAll(position, layouts);
    }

    public <Data, Layout extends LayoutImpl> DataChange addAll(Data[] dataArray, Converter<Data, Layout> converter) {
        return addAll(Arrays.asList(dataArray), converter);
    }

    public <Data, Layout extends LayoutImpl> DataChange addAll(int position, Data[] dataArray, Converter<Data, Layout> converter) {
        return addAll(position, Arrays.asList(dataArray), converter);
    }

    public <Data> DataChange addAll(Collection<Data> dataList, ListConverter<Data> converter) {
        List<LayoutImpl> layouts = new ArrayList<>();
        for (Data data : dataList) {
            layouts.addAll(converter.convert(data, this));
        }
        return addAll(layouts);
    }

    public <Data> DataChange addAll(int position, Collection<Data> dataList, ListConverter<Data> converter) {
        List<LayoutImpl> layouts = new ArrayList<>();
        for (Data data : dataList) {
            layouts.addAll(converter.convert(data, this));
        }
        return addAll(position, layouts);
    }

    public <Data> DataChange add(Data data, ListConverter<Data> converter) {
        return addAll(converter.convert(data, this));
    }

    public <Data> DataChange add(int position, Data data, ListConverter<Data> converter) {
        return addAll(position, converter.convert(data, this));
    }

    public <Data> DataChange replaceFirst (Data from, Data to) {
        for (int i = 0; i < getDataSize(); i++) {
            LayoutImpl layout = mDataList.get(i);
            if (layout.getSource().equals(from)) {
                layout.setSource(to);
                return new DataChange(this, getAdapterPositionOfData(i), DataChange.TYPE_ITEM_CHANGED);
            }
        }
        return DataChange.doNothingInstance();
    }

    public <Data> DataChange replaceFirst (String id, Data replacement) {
        for (int i = 0; i < getDataSize(); i++) {
            LayoutImpl layout = mDataList.get(i);
            if (layout.id().equals(id)) {
                layout.setSource(replacement);
                return new DataChange(this, getAdapterPositionOfData(i), DataChange.TYPE_ITEM_CHANGED);
            }
        }
        return DataChange.doNothingInstance();
    }

    public DataChange replace (LayoutImpl layout) {
        int index = mDataList.indexOf(layout);
        if (index < 0) {
            return DataChange.doNothingInstance();
        }
        mDataList.set(index, layout);
        return new DataChange(this, getAdapterPositionOfData(index), DataChange.TYPE_ITEM_CHANGED);
    }

    public DataChange removeFromData(int indexOfData) {
        if (indexOfData >= 0 && indexOfData < mDataList.size()) {
            mDataList.remove(indexOfData);
            return new DataChange(this, getAdapterPositionOfData(indexOfData), DataChange.TYPE_ITEM_REMOVED);
        }
        return DataChange.doNothingInstance();
    }

    public DataChange remove (LayoutImpl layout) {
        int index = mDataList.indexOf(layout);
        if (mDataList.remove(layout)) {
            return new DataChange(this, getAdapterPositionOfData(index), DataChange.TYPE_ITEM_REMOVED);
        }
        return DataChange.doNothingInstance();
    }

    public <Data> DataChange remove (Data data) {
        int removeIndex = -1;
        for (int i = 0; i < getDataSize(); i++) {
            if (mDataList.get(i).getSource().equals(data)) {
                removeIndex = i;
                break;
            }
        }
        if (removeIndex >= 0) {
            mDataList.remove(removeIndex);
            return new DataChange(this, removeIndex, DataChange.TYPE_ITEM_REMOVED);
        }
        return DataChange.doNothingInstance();
    }

    public DataChange remove(int index) {
        if (isHeaderIndex(index)) {
            return removeHeader(index);
        }
        if (isDataIndex(index)) {
            return removeFromData(index - getHeaderSize());
        }
        if (isFooterIndex(index)) {
            return removeFooter(index - getHeaderSize() - getDataSize());
        }
        return DataChange.doNothingInstance();
    }

    public DataChange removeMany(Collection<LayoutImpl<?>> layouts) {
//        Iterator<LayoutImpl> it = mDataList.iterator();
        boolean removed = mDataList.removeAll(layouts);
//        while (it.hasNext()) {
//            LayoutImpl l = it.next();
//            if (layouts.contains(l)) {
//                it.remove();
//                removed = true;
//            }
//
//        }
        if (removed) {
            return DataChange.notifyDataSetChangeInstance(this);
        }
        return DataChange.doNothingInstance();
    }

    public <Data> DataChange removeManyData(Collection<Data> dataCollection) {
        Iterator<LayoutImpl> it = mDataList.iterator();
        boolean removed = false;
        while (it.hasNext()) {
            LayoutImpl l = it.next();
            if (dataCollection.contains(l.getSource())) {
                it.remove();
                removed = true;
            }
        }
        if (removed) {
            return DataChange.notifyDataSetChangeInstance(this);
        }
        return DataChange.doNothingInstance();
    }

    public boolean isHeaderIndex(int index) {
        return index >= 0 && index < getHeaderSize();
    }

    public boolean isDataIndex(int index) {
        int idx = index - getHeaderSize();
        return idx >= 0 && idx < getDataSize();
    }

    public boolean isFooterIndex(int index) {
        int idx = index - getHeaderSize() - getDataSize();
        return idx >= 0 && idx < getFooterSize();
    }

    public DataChange addHeader (LayoutImpl layout) {
        if (mHeaderList == null) {
            mHeaderList = new ArrayList<>();
        }
        mHeaderList.add(layout);
        return new DataChange(this, mHeaderList.indexOf(layout), DataChange.TYPE_ITEM_INSERTED);
    }

    public DataChange addHeaders (Collection<LayoutImpl> layouts) {
        if (mHeaderList == null) {
            mHeaderList = new ArrayList<>();
        }
        int start = mHeaderList.size();
        mHeaderList.addAll(layouts);
        return new DataChange(this, start, layouts.size(), DataChange.TYPE_ITEM_RANGE_INSERTED);
    }

    public <Data, Layout extends LayoutImpl> DataChange addHeaders (Collection<Data> dataCollection, Converter<Data, Layout> converter) {
        List<LayoutImpl> layouts = new ArrayList<>();
        for (Data data : dataCollection) {
            layouts.add(converter.convert(data, this));
        }
        return addHeaders(layouts);
    }

    public DataChange clearHeaders() {
        if (isHeaderEmpty()) {
            return DataChange.doNothingInstance();
        }
        int count = mHeaderList.size();
        mHeaderList.clear();
        return new DataChange(this, 0, count);
    }

    public DataChange removeHeader (LayoutImpl layout) {
        if (isHeaderEmpty()) {
            return DataChange.doNothingInstance();
        }
        int index = mHeaderList.indexOf(layout);
        if (index < 0) {
            return DataChange.doNothingInstance();
        }
        return removeHeader(index);
    }

    public DataChange removeHeader(int indexInHeader) {
        if (!isHeaderEmpty() && indexInHeader >= 0 && indexInHeader <= getHeaderSize()) {
            mHeaderList.remove(indexInHeader);
            return new DataChange(this, getAdapterPositionOfHeader(indexInHeader), DataChange.TYPE_ITEM_REMOVED);
        }
        return DataChange.doNothingInstance();
    }

    public DataChange removeFooter(LayoutImpl layout) {
        if (isFooterEmpty()) {
            return DataChange.doNothingInstance();
        }
        int index = mFooterList.indexOf(layout);
        if (index < 0) {
            return DataChange.doNothingInstance();
        }
        return removeFooter(index);
    }

    public DataChange removeFooter(int indexInFooter) {
        if (!isFooterEmpty() && indexInFooter >= 0 && indexInFooter < getFooterSize()) {
            mFooterList.remove(indexInFooter);
            return new DataChange(this, getAdapterPositionOfFooter(indexInFooter), DataChange.TYPE_ITEM_REMOVED);
        }
        return DataChange.doNothingInstance();
    }

    public DataChange addFooter (LayoutImpl layout) {
        if (mFooterList == null) {
            mFooterList = new ArrayList<>();
        }
        mFooterList.add(layout);
        return new DataChange(this, getItemCount(),
                DataChange.TYPE_ITEM_INSERTED);
    }

    public DataChange clearData () {
        int dataSize = getDataSize();
        mDataList.clear();
        return new DataChange(this, getHeaderSize(), dataSize, DataChange.TYPE_ITEM_RANGE_REMOVED);
    }

    public DataChange clear () {
        int itemCount = getItemCount();
        if (mHeaderList != null && !mHeaderList.isEmpty()) {
            mHeaderList.clear();
        }
        if (!mDataList.isEmpty()) {
            mDataList.clear();
        }
        if (mFooterList != null && !mFooterList.isEmpty()) {
            mFooterList.clear();
        }
        return new DataChange(this, 0, itemCount, DataChange.TYPE_ITEM_RANGE_REMOVED);
    }

    public int getAdapterPositionOfHeader (int positionInHeaderList) {
        return positionInHeaderList;
    }

    public int getAdapterPositionOfHeader (LayoutImpl layout) {
        return getAdapterPositionOfHeader(indexOfHeader(layout));
    }

    public int getAdapterPositionOfData (int positionInDataList) {
        return getHeaderSize() + positionInDataList;
    }

    public int getAdapterPositionOfData (LayoutImpl layout) {
        return getAdapterPositionOfData(indexOfData(layout));
    }

    public int getAdapterPositionOfFooter(int positionInFooterList) {
        return getHeaderSize() + getDataSize() + positionInFooterList;
    }

    public int getAdapterPositionOfFooter(LayoutImpl layout) {
        if(mFooterList == null) {
            return -1;
        }
        if(mFooterList.isEmpty() || !mFooterList.contains(layout)) {
            return -1;
        }
        return getAdapterPositionOfFooter(mFooterList.indexOf(layout));
    }

    public boolean hasFooters() {
        return getFooterSize() > 0;
    }
    public void notifyFooters() {
        if (hasFooters()) {
            notifyItemRangeChanged(getFooterStartPosition(), getFooterSize());
        }
    }

    public boolean hasHeaders() {
        return getHeaderSize() > 0;
    }
    public void notifiyHeaders() {
        if (hasHeaders()) {
            notifyItemRangeChanged(0, getHeaderSize());
        }
    }

    public SingleSelection singleSelection() {
        return Selection.Companion.obtainSingle(this);
    }

    public MultipleSelection multipleSelection() {
        return Selection.Companion.obtainMultiple(this);
    }

}
