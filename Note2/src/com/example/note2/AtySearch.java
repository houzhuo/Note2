package com.example.note2;

import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.note2.AtyEditNote.MediaAdapter;
import com.example.note2.AtyEditNote.MediaListCellData;
import com.example.note2.db.NotesDB;
import com.example.note2.util.JsonParser;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

public class AtySearch extends ListActivity {

	private NotesDB db;
	private MediaAdapter mediaAdapter = null;
	private SQLiteDatabase dbRead;
	
	private static String TAG = "IatDemo";
	//������д����
	private SpeechRecognizer mIat;
	// ������дUI
	private RecognizerDialog iatDialog;
	
	private SharedPreferences mSharedPreferences;
	
	private int ret = 0;// �������÷���ֵ
	
	private Toast mToast;
	
	// ��������
	private String mEngineType = SpeechConstant.TYPE_CLOUD;
	
	// ��HashMap�洢��д���
	//	private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_search);
		mediaAdapter = new MediaAdapter(this);
		db = new NotesDB(this);
		dbRead = db.getReadableDatabase();
		handleIntent(getIntent());
		SpeechUtility.createUtility(this, SpeechConstant.APPID +"=548d4e52");
		
		//����SpeechRecognizer���󣬵ڶ��������� ������дʱ��InitListener
		mIat = SpeechRecognizer.createRecognizer(this, mInitListener);
		
		iatDialog = new RecognizerDialog(this, mInitListener);
		
		mSharedPreferences = getSharedPreferences("com.example.note2", Activity.MODE_PRIVATE);
		
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
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.aty_search_action_voicesearch:
			
			setParam();
			boolean isShowDialog = mSharedPreferences.getBoolean("iat_show", true);
			if (isShowDialog) {
				// ��ʾ��д�Ի���
				
				iatDialog.setListener(recognizerDialogListener);//���ûص��ӿ�
				iatDialog.show();
				showTip("�뿪ʼ˵��");
			} else {
				// ����ʾ��д�Ի���
				ret = mIat.startListening(recognizerListener);
				if(ret != ErrorCode.SUCCESS){
					showTip("��дʧ��,�����룺" + ret);
				}else {
					showTip("�뿪ʼ˵��");
				}
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
		search.collapseActionView();//Ĭ��չ��������
		search.expandActionView();

		// Associate searchable configuration with the SearchView
		SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.aty_search_action_search)
				.getActionView();// Returns the currently set action view for
									// this menu item.
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		// getComponentName-->Returns complete component name of this activity.
		searchView.setSubmitButtonEnabled(false);
		searchView.setIconifiedByDefault(true);
	

		return true;

	}
	/**
	 * ��д��������
	 */
	private RecognizerListener recognizerListener=new RecognizerListener(){
		//��д����ص��ӿ�(����Json��ʽ���)��
		//һ������»�ͨ��onResults�ӿڶ�η��ؽ����������ʶ�������Ƕ�ν�����ۼӣ�
		//���ڽ���Json�Ĵ���ɲμ�MscDemo��JsonParser�ࣻ
		//isLast����trueʱ�Ự������

		@Override
		public void onResult(RecognizerResult results, boolean isLast) {		
			Log.d(TAG, results.getResultString());
			
			if(isLast) {
				//TODO ���Ľ��
			}
		}

		@Override
		public void onBeginOfSpeech() {	
			showTip("��ʼ˵��");
		}


		@Override
		public void onError(SpeechError error) {
			showTip(error.getPlainDescription(true));
		}

		@Override
		public void onEndOfSpeech() {
			showTip("����˵��");
		}




		@Override
		public void onVolumeChanged(int volume) {
			showTip("��ǰ����˵����������С��" + volume);
		}


		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
		}
	};
	/**
	 * ��дUI������
	 */
	private RecognizerDialogListener recognizerDialogListener=new RecognizerDialogListener(){
		public void onResult(RecognizerResult results, boolean isLast) {
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
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			Log.d(TAG, "SpeechRecognizer init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
        		showTip("��ʼ��ʧ��,�����룺"+code);
        	}
		}
	};

	
	public void setParam(){
		// ��ղ���
		mIat.setParameter(SpeechConstant.PARAMS, null);
		
		// ������д����
		mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
		// ���÷��ؽ����ʽ
		mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
		
		String lag = mSharedPreferences.getString("iat_language_preference", "mandarin");
		if (lag.equals("en_us")) {
			// ��������
			mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
		}else {
			// ��������
			mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
			// ������������
			mIat.setParameter(SpeechConstant.ACCENT,lag);
		}
		// ��������ǰ�˵�
		mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "1000"));
		// ����������˵�
		mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));
		// ���ñ�����
		//mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));
		// ������Ƶ����·��
	}
	private void showTip(final String str) {
		mToast.setText(str);
		mToast.show();
	}
}
