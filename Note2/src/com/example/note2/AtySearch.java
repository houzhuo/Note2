package com.example.note2;

import com.example.note2.AtyEditNote.MediaAdapter;
import com.example.note2.AtyEditNote.MediaListCellData;
import com.example.note2.db.NotesDB;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;
import android.widget.Toast;

public class AtySearch extends ListActivity {

	private NotesDB db;
	private MediaAdapter mediaAdapter = null;
	private SQLiteDatabase dbRead;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_search);
		mediaAdapter = new MediaAdapter(this);
		db = new NotesDB(this);
		dbRead = db.getReadableDatabase();
		handleIntent(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {

		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			doMySearch(query);
		}
	}

	private void doMySearch(String query) {

		Cursor c = dbRead.query(NotesDB.TABLE_NAME_NOTES, null,
				NotesDB.COLUMN_NAME_NOTE_NAME + " like?",
				new String[] { "%"+query+"%" }, null, null, null);

		c.moveToFirst();
	
		System.out.println("++++++++++++" + c.getCount());

		Toast.makeText(getApplicationContext(),
				c.getString(c.getColumnIndex(NotesDB.COLUMN_NAME_ID)),
				Toast.LENGTH_LONG).show(); 
		
		if (mediaAdapter != null) {
			for (int i = 0; i < c.getCount(); i++) {
				mediaAdapter
						.add(new MediaListCellData(
								c.getString(c
										.getColumnIndex(NotesDB.COLUMN_NAME_NOTE_NAME))+".mp3",
								c.getInt(c
										.getColumnIndex(NotesDB.COLUMN_NAME_ID))));
				c.moveToNext();
			}
		}
		/*
		 * mediaAdapter.add(new MediaListCellData(c.getString(c
		 * .getColumnIndex(NotesDB.COLUMN_NAME_NOTE_NAME)), c
		 * .getInt(c.getColumnIndex(NotesDB.COLUMN_NAME_ID))));
		 */
		// }
		
		setListAdapter(mediaAdapter);
		mediaAdapter.notifyDataSetChanged();
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.search, menu);

		// Associate searchable configuration with the SearchView
		SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();// Returns the currently set action view for
									// this menu item.
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		// getComponentName-->Returns complete component name of this activity.
		searchView.setSubmitButtonEnabled(true);
		searchView.setIconifiedByDefault(true);

		return true;

	}

}
