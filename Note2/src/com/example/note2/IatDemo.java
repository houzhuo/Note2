package com.example.note2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import ECSConnecter.TopicConnecter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.note2.util.JsonParser;
import com.example.note2.util.Pcm2Wav;
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
import com.iflytek.sunflower.FlowerCollector;

public class IatDemo extends Activity implements Runnable {
	private static String TAG = "IatDemo";
	// 语音听写对象
	private SpeechRecognizer mIat;
	// 语音听写UI
	private RecognizerDialog iatDialog;
	// 听写结果内容
	private EditText mResultText;

	private Toast mToast;
	private SharedPreferences mSharedPreferences;
	// 引擎类型
	private String mEngineType = SpeechConstant.TYPE_CLOUD;
	// 文件路径
	File pcmPath;
	File wavPath;
	private String currentPath = null;

	private ImageButton button;
	public boolean isLongClick = false;

	@SuppressLint("ShowToast")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.iat_demo);

		SpeechUtility.createUtility(this, SpeechConstant.APPID + "=548d4e52");

		initLayout();

		// 创建SpeechRecognizer对象，第二个参数： 本地听写时传InitListener
		mIat = SpeechRecognizer.createRecognizer(this, mInitListener);
		// 初始化听写Dialog,如果只使用有UI听写功能,无需创建SpeechRecognizer
		iatDialog = new RecognizerDialog(this, mInitListener);

		mSharedPreferences = getSharedPreferences("com.example.note2",
				Activity.MODE_PRIVATE);
		mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		mResultText = ((EditText) findViewById(R.id.iat_text));

		button.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				System.out.println("长按。。。。。。。。。。。");
				isLongClick = true;

				// 设置参数
				setParam();
				boolean isShowDialog = mSharedPreferences.getBoolean(
						"iat_show", true);
				if (isShowDialog) {
					// 显示听写对话框

					iatDialog.setListener(recognizerDialogListener);// 设置回调接口
					iatDialog.show();
					showTip("请开始说话");
				} else {
					// 不显示听写对话框
					ret = mIat.startListening(recognizerListener);
					if (ret != ErrorCode.SUCCESS) {
						showTip("听写失败,错误码：" + ret);
					} else {
						showTip("请开始说话");
					}
				}
				return true;
			}
		});
		button.setOnTouchListener(new MyClickListener());

	}

	class MyClickListener implements OnTouchListener {
		public boolean onTouch(View v, MotionEvent event) {
			if (isLongClick)
				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:

					System.out.println("抬起。。。。。。。。。。。。。。。。");
					isLongClick = false;
					break;
				default:
					break;
				}
			return false;
		}

	}

	/**
	 * 初始化Layout。
	 */
	private void initLayout() {
		button = (ImageButton) findViewById(R.id.iat_Recognize);

		// findViewById(R.id.iat_Recognize).setOnClickListener(this);
		// findViewById(R.id.btnPcm2Wav).setOnClickListener(this);
	}

	int ret = 0;// 函数调用返回值

	/*
	 * @Override public void onClick(View view) { switch (view.getId()) {
	 * 
	 * // 开始听写 case R.id.iat_Recognize: //mResultText.setText(null);// 清空显示内容 //
	 * 设置参数 setParam(); boolean isShowDialog =
	 * mSharedPreferences.getBoolean("iat_show", true); if (isShowDialog) { //
	 * 显示听写对话框
	 * 
	 * iatDialog.setListener(recognizerDialogListener);//设置回调接口
	 * iatDialog.show(); showTip("请开始说话"); } else { // 不显示听写对话框 ret =
	 * mIat.startListening(recognizerListener); if(ret != ErrorCode.SUCCESS){
	 * showTip("听写失败,错误码：" + ret); }else { showTip("请开始说话"); } } break;
	 * 
	 * 
	 * 
	 * case R.id.btnPcm2Wav: pcm2wav();
	 * 
	 * 
	 * break;
	 * 
	 * 
	 * default: break; } }
	 */

	private void pcm2wav() {
		// TODO Auto-generated method stub
		Pcm2Wav tool = new Pcm2Wav();
		try {
			// tool.convertAudioFiles(pcmFilePath, wavFilePath);
			tool.convertAudioFiles(currentPath.toString(), wavPath.toString());
		} catch (Exception e) {
			Log.e(TAG, "pcm failed to convert into wav File:" + e.getMessage());
		}

	}

	/**
	 * 初始化监听器。
	 */
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			Log.d(TAG, "SpeechRecognizer init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
				showTip("初始化失败,错误码：" + code);
			}
		}
	};

	/**
	 * 听写监听器。
	 */
	private RecognizerListener recognizerListener = new RecognizerListener() {
		// 听写结果回调接口(返回Json格式结果)；
		// 一般情况下会通过onResults接口多次返回结果，完整的识别内容是多次结果的累加；
		// 关于解析Json的代码可参见MscDemo中JsonParser类；
		// isLast等于true时会话结束。

		@Override
		public void onResult(RecognizerResult results, boolean isLast) {
			Log.d(TAG, results.getResultString());
			String text = JsonParser.parseIatResult(results.getResultString());
			mResultText.append(text);
			mResultText.setSelection(mResultText.length());

			// showTip(text);
			if (isLast) {
				// TODO 最后的结果
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
	private RecognizerDialogListener recognizerDialogListener = new RecognizerDialogListener() {
		public void onResult(RecognizerResult results, boolean isLast) {
			String text = JsonParser.parseIatResult(results.getResultString());
			mResultText.append(text);
			mResultText.setSelection(mResultText.length());
			// showTip(text);
		}

		/**
		 * 识别回调错误.
		 */
		public void onError(SpeechError error) {
			showTip(error.getPlainDescription(true));
		}

	};

	private void showTip(final String str) {
		mToast.setText(str);
		mToast.show();
	}

	/**
	 * 参数设置
	 * 
	 * @param param
	 * @return
	 */
	public void setParam() {
		// 清空参数
		mIat.setParameter(SpeechConstant.PARAMS, null);

		// 设置听写引擎
		mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
		// 设置返回结果格式
		mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

		String lag = mSharedPreferences.getString("iat_language_preference",
				"mandarin");
		if (lag.equals("en_us")) {
			// 设置语言
			mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
		} else {
			// 设置语言
			mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
			// 设置语言区域
			mIat.setParameter(SpeechConstant.ACCENT, lag);
		}
		// 设置语音前端点
		mIat.setParameter(SpeechConstant.VAD_BOS,
				mSharedPreferences.getString("iat_vadbos_preference", "1000"));
		// 设置语音后端点
		mIat.setParameter(SpeechConstant.VAD_EOS,
				mSharedPreferences.getString("iat_vadeos_preference", "1000"));
		// 设置标点符号
		mIat.setParameter(SpeechConstant.ASR_PTT,
				mSharedPreferences.getString("iat_punc_preference", "1"));
		// 设置音频保存路径

		pcmPath = new File(getMediaDir(), System.currentTimeMillis() + ".pcm");
		wavPath = new File(getMediaDir(), System.currentTimeMillis() + ".wav");

		try {
			pcmPath.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		currentPath = pcmPath.getAbsolutePath();
		mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, currentPath);
	}

	public File getMediaDir() {
		File dir = new File(Environment.getExternalStorageDirectory(),
				"NotesMedia");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 退出时释放连接
		mIat.cancel();
		mIat.destroy();
	}

	@Override
	protected void onResume() {
		// 移动数据统计分析
		FlowerCollector.onResume(this);
		FlowerCollector.onPageStart("IatDemo");
		super.onResume();
	}

	@Override
	protected void onPause() {
		// 移动数据统计分析
		FlowerCollector.onPageEnd("IatDemo");
		FlowerCollector.onPause(this);
		super.onPause();
	}
private String contentString;
	public void onBackPressed() {
		 contentString = mResultText.getText().toString();
		 System.out.println("+++++++++++++++++++++++++++++");
		System.out.println("-----------------------"+contentString+"");
		new AlertDialog.Builder(this)
				.setTitle("是否保存当前音频？")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						if (contentString.isEmpty()) {
							finish();
							
						}else {

							// 点击“确认”后的操作
							Toast.makeText(IatDemo.this, "音频保存成功",
									Toast.LENGTH_LONG).show();
							pcm2wav();
							// 把地址推送到ActivityForResult
							Intent intent = new Intent();
							intent.putExtra(VOICE_EXTRA_PATH, wavPath.toString());
							intent.putExtra(VOICE_EXTRA_CONTENT, mResultText
									.getText().toString());
							
							Thread t = new Thread(IatDemo.this);
							t.start();
							try {
								t.sleep(600);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							System.out.println("topicList " + topicList + "");
							// tvTopic.setText(list+"");
							intent.putExtra(VOICE_EXTRA_TOPIC,topicList+"");
							//
							
							setResult(3, intent);
							finish();
						}

					}
				})
				.setNegativeButton("返回", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
						// 点击“返回”后的操作,这里不设置没有任何操作
					}
				}).show();
	}

	/**
	 * @param 正则表达式去标点
	 * @return
	 */
	/*
	 * public static String stringFormat(String s){ String str=s.replaceAll(
	 * "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|-]"
	 * , ""); return str; }
	 */

	public static final String VOICE_EXTRA_PATH = "wavPath";
	public static final String VOICE_EXTRA_CONTENT = "content";
	public static final String VOICE_EXTRA_TOPIC = "topic";

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		map.put("aimStr", mResultText.getText().toString());

		TopicConnecter conn = new TopicConnecter(map);
		try {
			conn.getXML();
			try {
				conn.readXML();
			} catch (ParserConfigurationException | SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// TopicData data = conn.getData();
		ArrayList<String> list = conn.getData().getResult();
		if (list!=null) {
			for (String string : list) {
				System.out.println(string);
				topicList.add(string);
			}	
		}else {
			topicList.add("服务器在休息。。") ;
		}
		

	}

	private List<String> topicList = new ArrayList<>();

}
