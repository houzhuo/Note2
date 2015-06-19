package com.example.note2;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.note2.arcmenu.ArcMenu;
import com.example.note2.arcmenu.ArcMenu.OnMenuItemClickListener;
import com.example.note2.db.NotesDB;

public class AtyEditNote extends ListActivity {

	private ArcMenu arcMenu;

	private ListView mListView;
	
	
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_eidt_note);

		/*
		 * 去actionbar
		 */
		ActionBar actionBar = getActionBar();
		actionBar.hide();

		db = new NotesDB(this);
		dbRead = db.getReadableDatabase();
		dbWrite = db.getWritableDatabase();

		adapter = new MediaAdapter(this);
		setListAdapter(adapter);

		etName = (EditText) findViewById(R.id.etName);
		etTopic = (EditText) findViewById(R.id.etTopic);
		imageBack = (ImageView) findViewById(R.id.ic_atyedit_back);
		imageBack.setOnClickListener(btn_back_onclickHandler);

		noteId = getIntent().getIntExtra(EXTRA_NOTE_ID, -1);

		if (noteId > -1) {
			etName.setText(getIntent().getStringExtra(EXTRA_NOTE_NAME));
			etTopic.setText(getIntent().getStringExtra(EXTRA_NOTE_TOPIC));

			Cursor c = dbRead.query(NotesDB.TABLE_NAME_MEDIA, null,
					NotesDB.COLUMN_NAME_MEDIA_OWNER_NOTE_ID + "=?",
					new String[] { noteId + "" }, null, null, null);

			while (c.moveToNext()) {

				adapter.add(new MediaListCellData(
						c.getString(c
								.getColumnIndex(NotesDB.COLUMN_NAME_MEDIA_TOPIC)),
						c.getString(c
								.getColumnIndex(NotesDB.COLUMN_NAME_MEDIA_DATE)),
						c.getString(c
								.getColumnIndex(NotesDB.COLUMN_NAME_MEDIA_PATH)),
						c.getString(c
								.getColumnIndex(NotesDB.COLUMN_NAME_MEDIA_CONTENT)),
						c.getInt(c.getColumnIndex(NotesDB.COLUMN_NAME_ID))));
			}
			adapter.notifyDataSetChanged();
		}
		initView();
		initEvent();

		/*
		 * // actionbar导航 ActionBar actionBar = getActionBar();
		 * actionBar.setTitle("                    滴滴备忘");
		 * actionBar.setHomeAsUpIndicator(R.drawable.actionbar_back_icon);
		 * actionBar.setDisplayHomeAsUpEnabled(true);// 设置返回图标
		 * actionBar.setDisplayShowHomeEnabled(false);// 没有系统图标
		 * actionBar.setHomeButtonEnabled(true);//
		 * 设置左侧返回图标，其中setHomeButtonEnabled和setDisplayShowHomeEnabled共同起作用， //
		 * 如果setHomeButtonEnabled设成false
		 * ，即使setDisplayShowHomeEnabled设成true，图标也不能点击
		 */
		mListView = getListView();
		registerForContextMenu(mListView);

	}

	/*
	 * 保存音频
	 */
	private OnClickListener btn_back_onclickHandler = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			saveMedia(saveNote());
			setResult(RESULT_OK);
			finish();
		}
	};

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		// 加载xml中的上下文菜单
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		// 相应上下文的操作
		switch (item.getItemId()) {

		case R.id.put:
			Toast.makeText(AtyEditNote.this, "正在上传", Toast.LENGTH_LONG).show();

			int putPosition = (int) getListAdapter().getItemId(info.position);
			MediaListCellData d = adapter.getItem(putPosition);
			String path = d.path.toString();
			String fileName = path.substring(path.length()-17, path.length());
			String fileNameWithoutWav = path.substring(path.length()-17, path.length()-4);
			
			MeidaPut media = new MeidaPut(path,fileName,fileNameWithoutWav);
			media.upload();
			
			//Toast.makeText(AtyEditNote.this, MeidaPut.msg.getData().getString("result"), Toast.LENGTH_SHORT).show();
			Toast.makeText(AtyEditNote.this,"上传成功", Toast.LENGTH_LONG).show();
			
			break;
		case R.id.delete:
			Toast.makeText(AtyEditNote.this, info.position+"", Toast.LENGTH_LONG)
					.show();
			int position = (int) getListAdapter().getItemId(info.position);
			MediaListCellData data = adapter.getItem(position);
			adapter.delete(position);
			adapter.notifyDataSetChanged();
			dbWrite.delete(NotesDB.TABLE_NAME_MEDIA,
					NotesDB.COLUMN_NAME_MEDIA_PATH + "=?",
					new String[] { data.path + "" });

			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	private void initEvent() {
		arcMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			File f;
			Intent i;

			@Override
			public void onClick(View view, int pos) {
				switch (pos) {
				case 1:
					i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					f = new File(getMediaDir(), System.currentTimeMillis()
							+ ".jpg");
					if (!f.exists()) {
						try {
							f.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					currentPath = f.getAbsolutePath();
					imagePath = Uri.fromFile(f);
					i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));// 媒体输出路径,存储位置
					startActivityForResult(i, REQUEST_CODE_GET_PHOTO);
					break;
				case 3:

					i = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
					f = new File(getMediaDir(), System.currentTimeMillis()
							+ ".mp4");
					if (!f.exists()) {
						try {
							f.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					currentPath = f.getAbsolutePath();
					videoPath = Uri.fromFile(f);
					i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

					startActivityForResult(i, REQUEST_CODE_GET_VIDEO);
					break;
				case 4:
					saveMedia(saveNote());
					setResult(RESULT_OK);
					finish();
					break;
				case 5:
					setResult(RESULT_CANCELED);
					finish();
					break;
				case 2:

					Intent i = new Intent(AtyEditNote.this, IatDemo.class);

					startActivityForResult(i, REQUEST_CODE_GET_SOUND);

				default:
					break;

				}
			}

		});

	}

	private void initView() {

		arcMenu = (ArcMenu) findViewById(R.id.id_menu);

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		MediaListCellData data = adapter.getItem(position);
		Intent i;
		System.out.println(data.type);
		System.out.println(data.path);

		switch (data.type) {
		case MediaType.PHOTO:
			/*
			 * i = new Intent(this, AtyPhotoViewer.class);
			 * i.putExtra(AtyPhotoViewer.EXTRA_PATH, data.path);
			 * System.out.println("onListItemClick.path:" + data.path);
			 * startActivity(i); break;
			 */

			// 使用Intent
			Intent imageIntent = new Intent(Intent.ACTION_VIEW);
			// Uri mUri = Uri.parse("file://" +
			// picFile.getPath());Android3.0以后最好不要通过该方法，存在一些小Bug
			imageIntent.setDataAndType(imagePath, "image/*");
			startActivity(imageIntent);
			break;
		case MediaType.VIDEO:
			/*
			 * i = new Intent(this, AtyVideoViewer.class);
			 * i.putExtra(AtyVideoViewer.EXTRA_PATH, data.path);
			 * startActivity(i);
			 */
			Intent videoIntent = new Intent(Intent.ACTION_VIEW);
			// Uri mUri = Uri.parse("file://" +
			// picFile.getPath());Android3.0以后最好不要通过该方法，存在一些小Bug
			videoIntent.setDataAndType(videoPath, "video/*");
			startActivity(videoIntent);
			break;

		case MediaType.SOUND:
			i = new Intent(this, AtySoundViewer.class);
			i.putExtra(AtySoundViewer.EXTRA_PATH, data.path);
			i.putExtra(AtySoundViewer.EXTRA_CONTENT, data.content);
			System.out.println("++++++++++++++++++++++" + data.content);
			i.putExtra(AtySoundViewer.EXTRA_TOPIC, data.topic);

			System.out.println("dataContent" + data.topic + "");
			startActivity(i);
			break;
		}

		super.onListItemClick(l, v, position, id);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case REQUEST_CODE_GET_PHOTO:
			if (resultCode == RESULT_OK) {
				DateFormat date = DateFormat.getDateTimeInstance(
						DateFormat.LONG, DateFormat.SHORT); // 显示日期，时间（精确到分）
				String Date = date.format(new Date());

				adapter.add(new MediaListCellData("Photos", Date, currentPath,
						"no media content"));
				adapter.notifyDataSetChanged();
				/*
				 * Uri audioPath = data.getData(); Toast.makeText(this,
				 * audioPath.toString(), Toast.LENGTH_LONG) .show();
				 */
			}
			break;
		case REQUEST_CODE_GET_VIDEO:

			if (resultCode == RESULT_OK) {
				DateFormat date = DateFormat.getDateTimeInstance(
						DateFormat.LONG, DateFormat.SHORT); // 显示日期，时间（精确到分）
				String Date = date.format(new Date());

				adapter.add(new MediaListCellData("Videos", Date, currentPath,
						"no media content"));
				adapter.notifyDataSetChanged();
				/*
				 * Uri audioPath = data.getData(); Toast.makeText(this,
				 * audioPath.toString(), Toast.LENGTH_LONG) .show();
				 */
			}
			break;
		case REQUEST_CODE_GET_SOUND:

			if (resultCode == RESULT_OK || resultCode == 3 || resultCode == 5) {

				DateFormat date = DateFormat.getDateTimeInstance(
						DateFormat.LONG, DateFormat.SHORT); // 显示日期，时间（精确到分）
				String Date = date.format(new Date());

				/*
				 * 获取wav的Path
				 */
				String wavPath = data.getExtras().getString(
						IatDemo.VOICE_EXTRA_PATH);
				voiceContent = data.getExtras().getString(
						IatDemo.VOICE_EXTRA_CONTENT);

				topic = data.getExtras().getString(IatDemo.VOICE_EXTRA_TOPIC);
				System.out.println("aty topic  " + topic + "");

				System.out.println("voiceContent:" + voiceContent);
				etTopic.append("/" + stringFormat(topic + ""));
				System.out.println("____________________________" + wavPath);
				adapter.add(new MediaListCellData(topic, Date, wavPath,
						voiceContent));
				adapter.notifyDataSetChanged();
			}
			break;
		default:
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	public File getMediaDir() {
		File dir = new File(Environment.getExternalStorageDirectory(),
				"NotesMedia");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	public void saveMedia(int noteId) {

		MediaListCellData data;
		ContentValues cv;

		for (int i = 0; i < adapter.getCount(); i++) {
			data = adapter.getItem(i);

			if (data.id <= -1) {// data.id初始即为-1
				System.out.println("dataID:" + data.id);
				System.out.println("dataTopic:" + data.topic);
				if (data.topic != null) {
					cv = new ContentValues();
					cv.put(NotesDB.COLUMN_NAME_MEDIA_PATH, data.path);
					System.out.println("saveMedia:data.path:" + data.path);
					cv.put(NotesDB.COLUMN_NAME_MEDIA_OWNER_NOTE_ID, noteId);
					cv.put(NotesDB.COLUMN_NAME_MEDIA_TOPIC, data.topic);
					cv.put(NotesDB.COLUMN_NAME_MEDIA_CONTENT, data.content);
					cv.put(NotesDB.COLUMN_NAME_MEDIA_DATE, data.date);
					System.out.println("++++" + data.topic);
					// System.out.println(data.content.toString());
					dbWrite.insert(NotesDB.TABLE_NAME_MEDIA, null, cv);
				} else {
					cv = new ContentValues();
					cv.put(NotesDB.COLUMN_NAME_MEDIA_PATH, data.path);
					cv.put(NotesDB.COLUMN_NAME_MEDIA_OWNER_NOTE_ID, noteId);
					cv.put(NotesDB.COLUMN_NAME_MEDIA_DATE, data.date);
					// cv.put(NotesDB.COLUMN_NAME_MEDIA_CONTENT,);
					System.out.println("else dataTopic" + data.topic);
					// System.out.println(data.content.toString());
					dbWrite.insert(NotesDB.TABLE_NAME_MEDIA, null, cv);
				}

			}
		}

	}

	public int saveNote() {
		DateFormat date = DateFormat.getDateTimeInstance(DateFormat.LONG,
				DateFormat.SHORT); // 显示日期，时间（精确到分）
		String Date = date.format(new Date());

		ContentValues cv = new ContentValues();
		cv.put(NotesDB.COLUMN_NAME_NOTE_NAME, etName.getText().toString());
		cv.put(NotesDB.COLUMN_NAME_NOTE_TOPIC, etTopic.getText().toString());
		cv.put(NotesDB.COLUMN_NAME_NOTE_DATE, Date);
		// cv.put(NotesDB.COLUMN_NAME_NOTE_DATE, new SimpleDateFormat(
		// "yyyy-MM-dd HH:mm:ss").format(new Date()));
		System.out.println("noteId:" + noteId);
		if (noteId > -1) {// mainActivity点击列项传入的NoteId，点击添加按钮时NoteId为-1
			dbWrite.update(NotesDB.TABLE_NAME_NOTES, cv, NotesDB.COLUMN_NAME_ID
					+ "=?", new String[] { noteId + "" });

			return noteId;
		} else {
			return (int) dbWrite.insert(NotesDB.TABLE_NAME_NOTES, null, cv);
		}
	}

	@Override
	protected void onDestroy() {
		dbRead.close();
		dbWrite.close();
		super.onDestroy();
	}

	private int noteId = -1;
	private ImageView imageBack;
	private EditText etName, etTopic;
	private MediaAdapter adapter;
	private NotesDB db;
	private SQLiteDatabase dbRead, dbWrite;
	private String currentPath = null;

	private Uri imagePath;
	private Uri videoPath;
	private Uri wavPath;

	private String voiceContent;
	private String topic;

	public static final int REQUEST_CODE_GET_PHOTO = 1;
	public static final int REQUEST_CODE_GET_VIDEO = 2;
	public static final int REQUEST_CODE_GET_SOUND = 3;

	public static final String EXTRA_NOTE_ID = "noteId";
	public static final String EXTRA_NOTE_NAME = "noteName";
	public static final String EXTRA_NOTE_TOPIC = "noteTopic";

	static class MediaAdapter extends BaseAdapter {

		public MediaAdapter(Context context) {
			this.context = context;
		}

		public void add(MediaListCellData data) {
			list.add(data);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		public void delete(int position) {
			list.remove(position);
		}

		@Override
		public MediaListCellData getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.media_list_cell, null);
			}

			MediaListCellData data = getItem(position);

			ImageView ivIcon = (ImageView) convertView
					.findViewById(R.id.ivIcon);
			TextView tvPath = (TextView) convertView.findViewById(R.id.tvPath);
			TextView tvSoundTopic = (TextView) convertView
					.findViewById(R.id.tvMeidaTopic);
			TextView tvMediaDate = (TextView) convertView
					.findViewById(R.id.tvMediaDate);
			TextView tvMediaContent = (TextView) convertView
					.findViewById(R.id.tvSoundContent);
			// ImageView ivAbout = (ImageView)
			// convertView.findViewById(R.id.ivAbout);

			ivIcon.setImageResource(data.iconId);
			// ivAbout.setImageResource(R.drawable.ic_settings_about);
			// System.out.println("icon:"+data.iconId);
			// ivIcon.setImageBitmap(getVideoThumbnail(urlvideo, 200, 200,
			// MediaStore.Images.Thumbnails.MICRO_KIND));
			tvPath.setText(data.path);
			tvMediaDate.setText(data.date);
			tvSoundTopic.setText(data.topic);
			tvMediaContent.setText(data.content);
			return convertView;
		}

		// private Bitmap getVideoThumbnail(String uri, int width, int
		// height,int kind){
		// Bitmap bitmap = null;
		// bitmap = ThumbnailUtils.createVideoThumbnail(uri, kind);
		// bitmap = ThumbnailUtils.extractThumbnail(bitmap, width,
		// height,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		//
		// return bitmap;
		// }

		private Context context;
		private List<MediaListCellData> list = new ArrayList<AtyEditNote.MediaListCellData>();
	}

	static class MediaListCellData {

		int type = 0;
		int id = -1;
		String path = "";
		String topic = "";
		String content = "";
		String date = "";
		int iconId = R.drawable.ic_launcher;

		public MediaListCellData(String topic, String date, String path,
				String content) {
			this.topic = topic;
			this.date = date;
			this.path = path;
			this.content = content;

			if (path.endsWith(".jpg")) {
				iconId = R.drawable.icon_photo;
				type = MediaType.PHOTO;
			}
			if (path.endsWith(".mp4")) {
				iconId = R.drawable.icon_video;
				type = MediaType.VIDEO;
			} else if (path.endsWith(".wav")) {
				iconId = R.drawable.icon_sound;
				type = MediaType.SOUND;
			}
			System.out.println("MediaListCellData:" + type);

			/*
			 * else { iconId = R.drawable.icon_sound; type = MediaType.SOUND; }
			 */
		}

		public MediaListCellData(String topic, String date, String path,
				String content, int id) {

			this(topic, date, path, content);
			this.id = id;
		}

	}

	static class MediaType {
		static final int PHOTO = 1;
		static final int VIDEO = 2;
		static final int SOUND = 3;
	}

	/*
	 * 设置actionbar
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.edit_actionbar_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/*
		 * switch (item.getItemId()) { case android.R.id.home:
		 * saveMedia(saveNote()); setResult(RESULT_OK); finish();
		 * 
		 * return true;
		 * 
		 * default: break; }
		 */
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		saveMedia(saveNote());
		setResult(RESULT_OK);
		finish();
	}

	public static String stringFormat(String s) {
		String str = s
				.replaceAll(
						"[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|-]",
						"");
		return str;
	}

}
