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
	// ������д����
	private SpeechRecognizer mIat;
	// ������дUI
	private RecognizerDialog iatDialog;
	// ��д�������
	private EditText mResultText;

	private Toast mToast;
	private SharedPreferences mSharedPreferences;
	// ��������
	private String mEngineType = SpeechConstant.TYPE_CLOUD;
	// �ļ�·��
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

		// ����SpeechRecognizer���󣬵ڶ��������� ������дʱ��InitListener
		mIat = SpeechRecognizer.createRecognizer(this, mInitListener);
		// ��ʼ����дDialog,���ֻʹ����UI��д����,���贴��SpeechRecognizer
		iatDialog = new RecognizerDialog(this, mInitListener);

		mSharedPreferences = getSharedPreferences("com.example.note2",
				Activity.MODE_PRIVATE);
		mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		mResultText = ((EditText) findViewById(R.id.iat_text));

		button.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				System.out.println("��������������������������");
				isLongClick = true;

				// ���ò���
				setParam();
				boolean isShowDialog = mSharedPreferences.getBoolean(
						"iat_show", true);
				if (isShowDialog) {
					// ��ʾ��д�Ի���

					iatDialog.setListener(recognizerDialogListener);// ���ûص��ӿ�
					iatDialog.show();
					showTip("�뿪ʼ˵��");
				} else {
					// ����ʾ��д�Ի���
					ret = mIat.startListening(recognizerListener);
					if (ret != ErrorCode.SUCCESS) {
						showTip("��дʧ��,�����룺" + ret);
					} else {
						showTip("�뿪ʼ˵��");
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

					System.out.println("̧�𡣡�����������������������������");
					isLongClick = false;
					break;
				default:
					break;
				}
			return false;
		}

	}

	/**
	 * ��ʼ��Layout��
	 */
	private void initLayout() {
		button = (ImageButton) findViewById(R.id.iat_Recognize);

		// findViewById(R.id.iat_Recognize).setOnClickListener(this);
		// findViewById(R.id.btnPcm2Wav).setOnClickListener(this);
	}

	int ret = 0;// �������÷���ֵ

	/*
	 * @Override public void onClick(View view) { switch (view.getId()) {
	 * 
	 * // ��ʼ��д case R.id.iat_Recognize: //mResultText.setText(null);// �����ʾ���� //
	 * ���ò��� setParam(); boolean isShowDialog =
	 * mSharedPreferences.getBoolean("iat_show", true); if (isShowDialog) { //
	 * ��ʾ��д�Ի���
	 * 
	 * iatDialog.setListener(recognizerDialogListener);//���ûص��ӿ�
	 * iatDialog.show(); showTip("�뿪ʼ˵��"); } else { // ����ʾ��д�Ի��� ret =
	 * mIat.startListening(recognizerListener); if(ret != ErrorCode.SUCCESS){
	 * showTip("��дʧ��,�����룺" + ret); }else { showTip("�뿪ʼ˵��"); } } break;
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
	 * ��ʼ����������
	 */
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			Log.d(TAG, "SpeechRecognizer init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
				showTip("��ʼ��ʧ��,�����룺" + code);
			}
		}
	};

	/**
	 * ��д��������
	 */
	private RecognizerListener recognizerListener = new RecognizerListener() {
		// ��д����ص��ӿ�(����Json��ʽ���)��
		// һ������»�ͨ��onResults�ӿڶ�η��ؽ����������ʶ�������Ƕ�ν�����ۼӣ�
		// ���ڽ���Json�Ĵ���ɲμ�MscDemo��JsonParser�ࣻ
		// isLast����trueʱ�Ự������

		@Override
		public void onResult(RecognizerResult results, boolean isLast) {
			Log.d(TAG, results.getResultString());
			String text = JsonParser.parseIatResult(results.getResultString());
			mResultText.append(text);
			mResultText.setSelection(mResultText.length());

			// showTip(text);
			if (isLast) {
				// TODO ���Ľ��
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
	private RecognizerDialogListener recognizerDialogListener = new RecognizerDialogListener() {
		public void onResult(RecognizerResult results, boolean isLast) {
			String text = JsonParser.parseIatResult(results.getResultString());
			mResultText.append(text);
			mResultText.setSelection(mResultText.length());
			// showTip(text);
		}

		/**
		 * ʶ��ص�����.
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
	 * ��������
	 * 
	 * @param param
	 * @return
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
		mIat.setParameter(SpeechConstant.ASR_PTT,
				mSharedPreferences.getString("iat_punc_preference", "1"));
		// ������Ƶ����·��

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
		// �˳�ʱ�ͷ�����
		mIat.cancel();
		mIat.destroy();
	}

	@Override
	protected void onResume() {
		// �ƶ�����ͳ�Ʒ���
		FlowerCollector.onResume(this);
		FlowerCollector.onPageStart("IatDemo");
		super.onResume();
	}

	@Override
	protected void onPause() {
		// �ƶ�����ͳ�Ʒ���
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
				.setTitle("�Ƿ񱣴浱ǰ��Ƶ��")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						if (contentString.isEmpty()) {
							finish();
							
						}else {

							// �����ȷ�ϡ���Ĳ���
							Toast.makeText(IatDemo.this, "��Ƶ����ɹ�",
									Toast.LENGTH_LONG).show();
							pcm2wav();
							// �ѵ�ַ���͵�ActivityForResult
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
				.setNegativeButton("����", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
						// ��������ء���Ĳ���,���ﲻ����û���κβ���
					}
				}).show();
	}

	/**
	 * @param ������ʽȥ���
	 * @return
	 */
	/*
	 * public static String stringFormat(String s){ String str=s.replaceAll(
	 * "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~��@#��%����& amp;*��������+|{}������������������������|-]"
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
			topicList.add("����������Ϣ����") ;
		}
		

	}

	private List<String> topicList = new ArrayList<>();

}
