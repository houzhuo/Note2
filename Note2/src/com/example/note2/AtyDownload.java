package com.example.note2;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.note2.db.NotesDB;

public class AtyDownload extends ListActivity {

	private SimpleCursorAdapter adapter;
	private SQLiteDatabase dbRead;
	private NotesDB db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_download);

		ActionBar actionBar = getActionBar();
		actionBar.hide();

		db = new NotesDB(this);
		dbRead = db.getReadableDatabase();
		
		adapter = new SimpleCursorAdapter(this,
				R.layout.download_media_list_cell, null,
				new String[] { NotesDB.COLUMN_NAME_DOWNLOAD_NAME,NotesDB.COLUMN_NAME_DOWNLOAD_PATH },
				new int[] { R.id.download_tvName ,R.id.download_tvPath});
		setListAdapter(adapter);
		refreshNotesListView();
		
		findViewById(R.id.ic_download_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	public void refreshNotesListView() {

		adapter.changeCursor(dbRead.query(NotesDB.TABLE_NAME_DOWNLOAD, null, null,
				null, null, null, null));

		// dbRead.query(table, columns, selection, selectionArgs, groupBy,
		// having, orderBy)

	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Cursor c = adapter.getCursor();
		c.moveToPosition(position);
		Intent i;
		i = new Intent(this, AtySoundViewer.class);
		i.putExtra(AtySoundViewer.EXTRA_PATH, c.getString(c.getColumnIndex(NotesDB.COLUMN_NAME_DOWNLOAD_PATH)));
		i.putExtra(AtySoundViewer.EXTRA_CONTENT, " ");
		

		startActivity(i);
		
		
	}
}
