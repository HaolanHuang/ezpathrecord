package cn.bluemobi.dylan.step.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import cn.bluemobi.dylan.step.R;
import cn.bluemobi.dylan.step.record.PathRecord;

public class RecordAdapter extends BaseAdapter {

	private Context mContext;
	private List<PathRecord> mRecordList;

	public RecordAdapter(Context context, List<PathRecord> list) {
		this.mContext = context;
		this.mRecordList = list;
	}

	@Override
	public int getCount() {
		return mRecordList.size();
	}

	@Override
	public Object getItem(int position) {
		return mRecordList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.recorditem, null);
			holder.date = (TextView) convertView.findViewById(R.id.date);
			holder.rd_distance = (TextView) convertView.findViewById(R.id.rd_distance);
			holder.historyTime = (TextView)convertView.findViewById(R.id.historyTime);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		PathRecord item = mRecordList.get(position);
		holder.date.setText(item.getDate());
		DecimalFormat decimalFormat = new DecimalFormat("0.0");
		if(item.getDistance()<1000){ holder.rd_distance.setText(decimalFormat.format(item.getDistance())); }
		else{ holder.rd_distance.setText(decimalFormat.format(item.getDistance()/1000d)); }
		int  seconds = (int)item.getDuration();
		holder.historyTime.setText(String.format(Locale.CHINA,"%02d:%02d:%02d",seconds/3600 % 24, seconds / 60 % 60, seconds % 60));
		return convertView;
	}

	private class ViewHolder {
		TextView date;
		TextView rd_distance;
		TextView historyTime;
	}
}
