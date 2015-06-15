package com.example.note2;

import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.note2.db.NotesDB;
import com.example.note2.util.JsonParser;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

public class AtySearch extends ListActivity {

	private NotesDB db;
	//private MediaAdapter mediaAdapter = null;
	private SimpleCursorAdapter simpleCursorAdapter = null;
	private SQLiteDatabase dbRead;

	private static String TAG = "IatDemo";
	// ������д����
	private SpeechRecognizer mIat;
	// ������дUI
	private RecognizerDialog iatDialog;

	private SharedPreferences mSharedPreferences;

	private int ret = 0;// �������÷���ֵ

	private Toast mToast;

	// ��������
	private String mEngineType = SpeechConstant.TYPE_CLOUD;

	private String text = "1";

	// ��HashMap�洢��д���
	// private HashMap<String, String> mIatResults = new LinkedHashMap<String,
	// String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_search);
		//mediaAdapter = new MediaAdapter(this);
		db = new NotesDB(this);
		dbRead = db.getReadableDatabase();
		handleIntent(getIntent());
		SpeechUtility.createUtility(this, SpeechConstant.APPID + "=548d4e52");

		// ����SpeechRecognizer���󣬵ڶ��������� ������дʱ��InitListener
		mIat = SpeechRecognizer.createRecognizer(this, null);

		iatDialog = new RecognizerDialog(this, null);

		mSharedPreferences = getSharedPreferences("com.example.note2",
				Activity.MODE_PRIVATE);

		mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
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

	/*private void doMySearch(String query) {
		int nameId = -1;
		Cursor c = dbRead.query(NotesDB.TABLE_NAME_NOTES, null,
				NotesDB.COLUMN_NAME_NOTE_NAME + " like?", new String[] { "%"
						+ query + "%" }, null, null, null);


		while (c.moveToNext()) {
			mediaAdapter.add(new MediaListCellData(c.getString(c
					.getColumnIndex(NotesDB.COLUMN_NAME_NOTE_NAME)) + ".mp3", c
					.getInt(c.getColumnIndex(NotesDB.COLUMN_NAME_ID))));
			nameId = c.getInt(c
					.getColumnIndex(NotesDB.COLUMN_NAME_NOTE_NAME));
			System.out.println("EEEEEEEEEEEEEEEEEEEEEEEEEEEEE"+ nameId);
		}
		if (nameId != -1) {
			
			mediaAdapter.notifyDataSetChanged();
			setListAdapter(mediaAdapter);
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ nameId);
		} else {
			showTip("0���������");
			setListAdapter(null);
		}

	}*/
	private void doMySearch(String query) {
		int nameId = -1;
		Cursor c = dbRead.query(NotesDB.TABLE_NAME_NOTES, null,
				NotesDB.COLUMN_NAME_NOTE_TOPIC + " like?", new String[] { "%"
						+ query + "%" }, null, null, null);
		

		while (c.moveToNext()) {
			simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.notes_list_cell, c,
					new String[] { NotesDB.COLUMN_NAME_NOTE_NAME,
					NotesDB.COLUMN_NAME_NOTE_DATE,
					NotesDB.COLUMN_NAME_NOTE_TOPIC }, new int[] {
					R.id.tvName, R.id.tvDate, R.id.tvContent });
			nameId = c.getInt(c
					.getColumnIndex(NotesDB.COLUMN_NAME_NOTE_NAME));
		}
		if (nameId != -1) {
			
			simpleCursorAdapter.notifyDataSetChanged();
			setListAdapter(simpleCursorAdapter);
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ nameId);
		} else {
			showTip("0���������");
			setListAdapter(null);
		}
	

	}
	private void doVoiceMySearch(String query) {
	
		Cursor c = dbRead.query(NotesDB.TABLE_NAME_NOTES, null,
				NotesDB.COLUMN_NAME_NOTE_TOPIC + " like?", new String[] { "%"
						+ query + "%" }, null, null, null);
		

		while (c.moveToNext()) {
			simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.notes_list_cell, c,
					new String[] { NotesDB.COLUMN_NAME_NOTE_NAME,
					NotesDB.COLUMN_NAME_NOTE_DATE,
					NotesDB.COLUMN_NAME_NOTE_TOPIC }, new int[] {
					R.id.tvName, R.id.tvDate, R.id.tvContent });
			
			setListAdapter(simpleCursorAdapter);
			System.out.println("setListAdapter");//��������Ϊ�ƴ󷵻������ν�����ڶ����Ǿ�š�
		}
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.aty_search_action_voicesearch:

			setParam();
			boolean isShowDialog = mSharedPreferences.getBoolean("iat_show",
					true);
			if (isShowDialog) {
				// ��ʾ��д�Ի���

				iatDialog.setListener(recognizerDialogListener);// ���ûص��ӿ�
				iatDialog.show();
				showTip("�뿪ʼ˵��");
			} else {
				/*// ����ʾ��д�Ի���
				ret = mIat.startListening(recognizerListener);
				if (ret != ErrorCode.SUCCESS) {
					showTip("��дʧ��,�����룺" + ret);
				} else {
					showTip("�뿪ʼ˵��");
				}*/showTip("no dialog");

			}
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.search, menu);
		MenuItem search = menu.findItem(R.id.aty_search_action_search);
		search.collapseActionView();// Ĭ��չ��������
		search.expandActionView();

		// Associate searchable configuration with the SearchView
		SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(
				R.id.aty_search_action_search).getActionView();// Returns the
																// currently set
																// action view
																// for
																// this menu
																// item.
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		// getComponentName-->Returns complete component name of this activity.
		searchView.setSubmitButtonEnabled(false);
		searchView.setIconifiedByDefault(true);

		return true;

	}

	/**
	 * ��дUI������
	 */
	private RecognizerDialogListener recognizerDialogListener = new RecognizerDialogListener() {
		public void onResult(RecognizerResult results, boolean isLast) {
			text = JsonParser.parseIatResult(results.getResultString());
			System.out.println("UI-------" + text);
			doVoiceMySearch(text);
		}

		/**
		 * ʶ��ص�����.
		 */
		public void onError(SpeechError error) {
			showTip(error.getPlainDescription(true));
		}

	};

	/**
	 * ��ʼ����������
	 */
	/*
	 * private InitListener mInitListener = new InitListener() {
	 * 
	 * @Override public void onInit(int code) { Log.d(TAG,
	 * "SpeechRecognizer init() code = " + code); if (code != ErrorCode.SUCCESS)
	 * { showTip("��ʼ��ʧ��,�����룺" + code); } } };
	 */

	public void setParam() {
		// ��ղ���
		mIat.setParameter(SpeechConstant.PARAMS, null);

		// ������д����
		mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
		// ���÷��ؽ����ʽ
		mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

		String lag = mSharedPreferences.getString("iat_language_preference",
				"mandarin");
		if (lag.equals("en_us")) {
			// ��������
			mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
		} else {
			// ��������
			mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
			// ������������
			mIat.setParameter(SpeechConstant.ACCENT, lag);
		}
		// ��������ǰ�˵�
		mIat.setParameter(SpeechConstant.VAD_BOS,
				mSharedPreferences.getString("iat_vadbos_preference", "1000"));
		// ����������˵�
		mIat.setParameter(SpeechConstant.VAD_EOS,
				mSharedPreferences.getString("iat_vadeos_preference", "1000"));
		// ���ñ�����
		mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));
		// mSharedPreferences.getString("iat_punc_preference", "1"));
		// ������Ƶ����·��
	}

	private void showTip(final String str) {
		mToast.setText(str);
		mToast.show();
	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Cursor c = simpleCursorAdapter.getCursor();
		c.moveToPosition(position);
		
		Intent i = new Intent(AtySearch.this, AtyEditNote.class);
		i.putExtra(AtyEditNote.EXTRA_NOTE_ID,
				c.getInt(c.getColumnIndex(NotesDB.COLUMN_NAME_ID)));
		i.putExtra(AtyEditNote.EXTRA_NOTE_NAME,
				c.getString(c.getColumnIndex(NotesDB.COLUMN_NAME_NOTE_NAME)));
		i.putExtra(AtyEditNote.EXTRA_NOTE_CONTENT,
				c.getString(c.getColumnIndex(NotesDB.COLUMN_NAME_NOTE_TOPIC)));
		startActivityForResult(i, REQUEST_CODE_EDIT_NOTE);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case REQUEST_CODE_EDIT_NOTE:
			refreshNotesListView();
			break;

		default:
			break;
		}
	}
	public void refreshNotesListView() {

		simpleCursorAdapter.changeCursor(dbRead.query(NotesDB.TABLE_NAME_NOTES, null, null,
				null, null, null, null));

	}
	
	
	public static final int REQUEST_CODE_EDIT_NOTE = 2;
}