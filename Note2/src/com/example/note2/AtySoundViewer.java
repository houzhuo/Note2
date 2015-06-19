package com.example.note2;

import java.io.IOException;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class AtySoundViewer extends Activity {
	/** Called when the activity is first created. */

	private SeekBar skb_audio = null;
	private Button btn_start_audio = null;
	private Button btn_stop_audio = null;
	private TextView tvSoundTopic;
	private TextView tvSoundContent;
	private TextView textView;

	// private boolean ifPlay = false;

	private MediaPlayer m = null;

	public static final String EXTRA_PATH = "path";
	public static final String EXTRA_CONTENT = "content";
	public static final String EXTRA_TOPIC = "topic";

	// private String mp3Path =
	// "/storage/emulated/0/kgmusic/download/libai.mp3";

	private boolean ifPlay = false;
	private boolean ifFirst = false; // ������ͣ����ֵ

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.aty_sound_viewer);

		// ----------Media�ؼ�����---------//
		m = new MediaPlayer();

		// ���Ž������ͷ�MediaPlayer
		m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			public void onCompletion(MediaPlayer arg0) {
				// Toast.makeText(Mplayer.this, "����", Toast.LENGTH_LONG).show();
				m.release();
			}
		});

		btn_start_audio = (Button) this.findViewById(R.id.Button01);
		btn_stop_audio = (Button) this.findViewById(R.id.Button02);
		btn_start_audio.setOnClickListener(new ClickEvent());
		btn_stop_audio.setOnClickListener(new ClickEvent());
		skb_audio = (SeekBar) this.findViewById(R.id.SeekBar01);
		skb_audio.setOnSeekBarChangeListener(new SeekBarChangeEvent());
		
		tvSoundTopic = (TextView)findViewById(R.id.tvSoundviewTopic);
		
		String topic = getIntent().getStringExtra(EXTRA_TOPIC);
		System.err.println("soundviewertopic"+topic+"");
		tvSoundTopic.setText(topic);
		
		tvSoundContent = (TextView)findViewById(R.id.tvSoundViewContent);
		
		String content = getIntent().getStringExtra(EXTRA_CONTENT);
		System.err.println(content+"");
		tvSoundContent.setText(content);
		
		
		
		
//		if (content!=null) {
//		tv_sound_textview.setText(content);
//		}else {
//			System.out.println(content+"");
//		}	

	}

	/*
	 * �����¼�����
	 */
	class ClickEvent implements View.OnClickListener {
		public void onClick(View v) {
			String path = getIntent().getStringExtra(EXTRA_PATH);
			System.out.println("soundPath---"+path);
			switch (v.getId()) {
			
			
			case R.id.Button01:
				
				
				
				if (m != null && !ifPlay) {
					btn_start_audio.setText("��ͣ");

					// ����audio��video��ǣ��Ա���Ƹ��ԵĽ������仯

					if (!ifFirst) {

						// �ָ���δ��ʼ����״̬
						m.reset();

						// �����ַ�ʽ��ȡ��Դ�ļ����ӹ��̵�resourceĿ¼������ָ��·���������ļ��Ƚϴ����Ա�ʾ�����Ǵ�SD����ȡ
						// m=MediaPlayer.create(TestPlayer.this,
						// R.raw.big);//��ȡ��Ƶ
						// m = MediaPlayer.create(AtySoundViewer.this,
						// Uri.parse("file://" + mp3Path));
						m = new MediaPlayer();
						try {
							m.setDataSource(path);
						} catch (IllegalArgumentException | SecurityException
								| IllegalStateException | IOException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						try {
							m.prepare();
						} catch (IllegalStateException | IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						// ����SeekBar�ĳ���
						skb_audio.setMax(m.getDuration());

						// ÿ�β��Ŷ�������������Ϊ0
						skb_audio.setProgress(0);

						ifFirst = true;
					}
					m.start(); // ����

					// ����һ�����߳����ڸ�����Ƶ�Ľ�����
					aseekth as = new aseekth();
					as.start();

					ifPlay = true;

				} else if (ifPlay) {
					btn_start_audio.setText("����");
					m.pause();
					ifPlay = false;

			}
				break;
			case R.id.Button02:
			
				 finish(); 
			
			default:
				break;

		}
		}
	}

	/*
	 * SeekBar���ȸı��¼�
	 */
	class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {

		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
		}

		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		public void onStopTrackingTouch(SeekBar seekBar) {
			m.seekTo(seekBar.getProgress());
		}
	}

	/*
	 * ��Ƶ�������̴߳���
	 */
	private class aseekth extends Thread {
		public void run() {
			try {
				while (ifPlay) {
					if (skb_audio.getProgress() < m.getCurrentPosition()) {
						skb_audio.setProgress(m.getCurrentPosition());
						sleep(10);
					}
				}
			} catch (Exception e) {
			}
		}
	}

	// ���紦��
	protected void onDestroy() {
		if (m != null) {
			if (m.isPlaying()) {
				m.stop();
			}
			m.release();
		}
		super.onDestroy();
	}

	protected void onPause() {
		if (m != null) {
			if (m.isPlaying()) {
				m.pause();
			}
		}
		super.onPause();
	}
}
