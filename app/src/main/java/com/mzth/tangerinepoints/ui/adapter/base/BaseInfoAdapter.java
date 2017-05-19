package com.mzth.tangerinepoints.ui.adapter.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * @author leeandy007
 * @Desc: 一级列表适配器基类
 * @version :
 * 
 */
public abstract class BaseInfoAdapter<T> extends BaseAdapter {

	protected Context _context;
	protected int _ResourceId;
	protected List<T> _List;

	public BaseInfoAdapter(Context context, List<T> list, int resId) {
		this._context = context;
		this._List = list;
		this._ResourceId = resId;
	}
	
	public List<T> getList() {
		return _List;
	}

	@Override
	public int getCount() {
		return _List.size();
	}
	
	public void replaceBean(int position , T t){
		_List.set(position, t);
		this.notifyDataSetChanged();
	}
	
	public void deleteItem(int position) {
		_List.remove(position);
		this.notifyDataSetChanged();
	}
	
	public void clearAll() {
		_List.clear();
		this.notifyDataSetChanged();
	}

	public void add(List<T> beans) {
		_List.addAll(beans);
		this.notifyDataSetChanged();
	}
	
	public void refresh(){
		this.notifyDataSetChanged();
	}
	
	@Override
	public Object getItem(int position) {
		return _List.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return dealView(_context, _List, _ResourceId, position, convertView);
	}
	
	public abstract View dealView(Context context, List<T> list, int resId, int position, View convertView);


	
}
