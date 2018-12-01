package cn.bluemobi.dylan.step.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.bluemobi.dylan.step.database.DbAdapter;
import cn.bluemobi.dylan.step.record.PathRecord;
import cn.bluemobi.dylan.step.R;
import cn.bluemobi.dylan.step.adapter.RecordAdapter;

/**
 * 所有轨迹list展示activity
 *
 */
public class RecordActivity extends AppCompatActivity implements OnItemClickListener{

	private RecordAdapter mAdapter;
	private ListView mAllRecordListView;
	private DbAdapter mDataBaseHelper;
	private List<PathRecord> mAllRecord = new ArrayList<PathRecord>();

	public static final String RECORD_ID = "record_id";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recordlist);
		mAllRecordListView = (ListView) findViewById(R.id.recordlist);
		mDataBaseHelper = new DbAdapter(this);
		mDataBaseHelper.open();
		searchAllRecordFromDB();
		mAdapter = new RecordAdapter(this, mAllRecord);
		mAllRecordListView.setAdapter(mAdapter);
		mAllRecordListView.setOnItemClickListener(this);
	}

	private void searchAllRecordFromDB() {
		mAllRecord = mDataBaseHelper.queryRecordAll();
	}

	public void onBackClick(View view) {
		this.finish();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
		PathRecord recorditem = (PathRecord) parent.getAdapter().getItem(
				position);
		Intent intent = new Intent(RecordActivity.this,
				RecordShowActivity.class);
		intent.putExtra(RECORD_ID, recorditem.getId());
		startActivity(intent);
	}
}
