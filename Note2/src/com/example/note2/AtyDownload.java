package com.example.note2;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.note2.AtyEditNote.MediaListCellData;
import com.example.note2.db.NotesDB;

public class AtyDownload extends ListActivity {

	private SimpleCursorAdapter adapter;
	private SQLiteDatabase dbRead,dbWrite;
	private NotesDB db;
	private String itemId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_download);

		ActionBar actionBar = getActionBar();
		actionBar.hide();

		db = new NotesDB(this);
		dbRead = db.getReadableDatabase();
		dbWrite = db.getWritableDatabase();
		
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
		registerForContextMenu(getListView());
	}
	
	
	
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		// 加载xml中的上下文菜单
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.download_delete, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case  R.id.download_delete:
					
			Cursor cursor = adapter.getCursor();
			itemId = cursor.getString(cursor
					.getColumnIndex(NotesDB.COLUMN_NAME_DOWNLOAD_PATH));
			System.out.println(itemId+"");
			
			dbWrite.delete(NotesDB.TABLE_NAME_DOWNLOAD,
					NotesDB.COLUMN_NAME_DOWNLOAD_PATH + "=?",
					new String[] { itemId + "" });
			adapter.notifyDataSetChanged();
			refreshNotesListView();
			
			break;

		default:
			break;
		}
		
		return super.onContextItemSelected(item);
			
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
		Toast.makeText(getApplicationContext(), c.getString(c.getColumnIndex(NotesDB.COLUMN_NAME_DOWNLOAD_PATH))+"", Toast.LENGTH_SHORT).show();
		System.out.println("-----"+c.getString(c.getColumnIndex(NotesDB.COLUMN_NAME_DOWNLOAD_PATH))+"");
		i.putExtra(AtySoundViewer.EXTRA_PATH, c.getString(c.getColumnIndex(NotesDB.COLUMN_NAME_DOWNLOAD_PATH)));
		i.putExtra(AtySoundViewer.EXTRA_CONTENT, " ");
		
		startActivity(i);
		
		
	}
}
