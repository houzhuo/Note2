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
	//语音听写对象
	private SpeechRecognizer mIat;
	// 语音听写UI
	private RecognizerDialog iatDialog;
	
	private SharedPreferences mSharedPreferences;
	
	private int ret = 0;// 函数调用返回值
	
	private Toast mToast;
	
	// 引擎类型
	private String mEngineType = SpeechConstant.TYPE_CLOUD;
	
	// 用HashMap存储听写结果
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
		
		//创建SpeechRecognizer对象，第二个参数： 本地听写时传InitListener
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
				// 显示听写对话框
				
				iatDialog.setListener(recognizerDialogListener);//设置回调接口
				iatDialog.show();
				showTip("请开始说话");
			} else {
				// 不显示听写对话框
				ret = mIat.startListening(recognizerListener);
				if(ret != ErrorCode.SUCCESS){
					showTip("听写失败,错误码：" + ret);
				}else {
					showTip("请开始说话");
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
		search.collapseActionView();//默认展开搜索框
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
	 * 听写监听器。
	 */
	private RecognizerListener recognizerListener=new RecognizerListener(){
		//听写结果回调接口(返回Json格式结果)；
		//一般情况下会通过onResults接口多次返回结果，完整的识别内容是多次结果的累加；
		//关于解析Json的代码可参见MscDemo中JsonParser类；
		//isLast等于true时会话结束。

		@Override
		public void onResult(RecognizerResult results, boolean isLast) {		
			Log.d(TAG, results.getResultString());
			
			if(isLast) {
				//TODO 最后的结果
			}
		}

		@Override
		public void onBeginOfSpeech() {	
			showTip("开始说话");
		}


		@Override
		public void onError(SpeechError error) {
			showTip(error.getPlainDescription(true));
		}

		@Override
		public void onEndOfSpeech() {
			showTip("结束说话");
		}




		@Override
		public void onVolumeChanged(int volume) {
			showTip("当前正在说话，音量大小：" + volume);
		}


		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
		}
	};
	/**
	 * 听写UI监听器
	 */
	private RecognizerDialogListener recognizerDialogListener=new RecognizerDialogListener(){
		public void onResult(RecognizerResult results, boolean isLast) {
		}

		/**
		 * 识别回调错误.
		 */
		public void onError(SpeechError error) {
			showTip(error.getPlainDescription(true));
		}

	};
	
	/**
	 * 初始化监听器。
	 */
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			Log.d(TAG, "SpeechRecognizer init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
        		showTip("初始化失败,错误码："+code);
        	}
		}
	};

	
	public void setParam(){
		// 清空参数
		mIat.setParameter(SpeechConstant.PARAMS, null);
		
		// 设置听写引擎
		mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
		// 设置返回结果格式
		mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
		
		String lag = mSharedPreferences.getString("iat_language_preference", "mandarin");
		if (lag.equals("en_us")) {
			// 设置语言
			mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
		}else {
			// 设置语言
			mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
			// 设置语言区域
			mIat.setParameter(SpeechConstant.ACCENT,lag);
		}
		// 设置语音前端点
		mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "1000"));
		// 设置语音后端点
		mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));
		// 设置标点符号
		//mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));
		// 设置音频保存路径
	}
	private void showTip(final String str) {
		mToast.setText(str);
		mToast.show();
	}
}
