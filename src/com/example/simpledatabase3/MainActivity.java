package com.example.simpledatabase3;

import java.util.ArrayList;

import com.example.simpledatabase3.R;

import android.R.integer;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MainActivity extends Activity {
	ListView mListView;
	ArrayList<String> mData = new ArrayList<String>();
	ArrayAdapter<String> mAdapter;
	int mSelected = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// widgets
		mListView = (ListView)findViewById(R.id.listView1);
		// mData.add("AA");
		// mData.add("BB");
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, mData);
		mListView.setAdapter(mAdapter);
		mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mSelected = position;			
			}
		});
		clearResult();
		select();
	}
	
	// DBインターフェース取得
	SQLiteDatabase getDb(){
		MyDbHelper helper = new MyDbHelper(this);
		SQLiteDatabase db = helper.getWritableDatabase();
		return db;
	}
	
	// 結果表示
	public void clearResult(){
		TextView textView = (TextView)findViewById(R.id.textView1);
		textView.setText("");
	}
	public void addResult(String result){
		TextView textView = (TextView)findViewById(R.id.textView1);
		textView.setText(textView.getText().toString() + result + "\n");
	}
	
	// 選択中のレコードのIDを取得
	public int getSelectedRecordId(){
		int i = mSelected; // mListView.getSelectedItemPosition();
		if(i >= 0 && i < mData.size()){
			return Integer.parseInt(mData.get(i).split(":")[0]);	
		}
		else{
			return -1;
		}
	}
	
	// INSERT
	public void buttonMethodInsert(View v){
		clearResult();
		SQLiteDatabase db = getDb();
		// insertメソッド版
		try{
			ContentValues values = new ContentValues();
			values.put("body", "def");
			db.insert(
				"messages", // テーブル名
				null,		// データを挿入する際にnull値が許可されていないカラムに代わりに利用される値
				values		// 値群
			);
			addResult("INSERT 成功");
		}
		catch(Exception ex){
			addResult("INSERT 失敗: " + ex.getMessage());
		}
		select();
	}
	
	// SELECT
	public void buttonMethodSelect(View v){
		clearResult();
		select();
	}
	public void select(){
		SQLiteDatabase db = getDb();
		
		// queryメソッド版
		try{
			mData.clear();
			Cursor c = db.query(
				"messages",						// テーブル名
				new String[]{"_id", "body"},	// 選択するカラム群
				null,							// selection
				null,							// selectionArgs
				null,							// group by
				null,							// having
				null							// order by
			);
			while(c.moveToNext()){
				int id = c.getInt(c.getColumnIndex("_id")); // ※ c.getString(0) と同じ
				String body = c.getString(c.getColumnIndex("body")); // ※ c.getString(1) と同じ
				// addResult(id + ":" + body);
				mData.add(id + ":" + body);
			}
			addResult("SELECT 成功");
		}
		catch(Exception ex){
			addResult("SELECT 失敗: " + ex.getMessage());
		}
		finally{
			mAdapter.notifyDataSetChanged();
		}
	}
	
	public void buttonMethodUpdate(View v){
		clearResult();
		SQLiteDatabase db = getDb();

		// 対象レコードID
		int id = getSelectedRecordId();
		if(id == -1){
			addResult("項目が選択されていません");
			return;
		}

		// updateメソッド版
		try{
			ContentValues values = new ContentValues();
			values.put("body", "XYZ");
			db.update(
				"messages",							// テーブル名
				values,								// 値群。
				"_id = ?",							// 条件。
				new String[]{ String.valueOf(id) }	// where args
			);
			addResult("UPDATE 成功");
		}
		catch(Exception ex){
			addResult("UPDATE 失敗: " + ex.getMessage());
		}
		select();
	}

	public void buttonMethodDelete(View v){
		clearResult();
		SQLiteDatabase db = getDb();
		
		// 対象レコードID
		int id = getSelectedRecordId();
		if(id == -1){
			addResult("項目が選択されていません");
			return;
		}
		
		// deleteメソッド版
		try{
			db.delete(
				"messages",							// テーブル名
				"_id = ?",							// 条件。
				new String[]{ String.valueOf(id) }	// where args
			);
			addResult("DELETE 成功");
		}
		catch(Exception ex){
			addResult("DELETE 失敗: " + ex.getMessage());
		}
		select();
	}
}
