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
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_eidt_note);

		db = new NotesDB(this);
		dbRead = db.getReadableDatabase();
		dbWrite = db.getWritableDatabase();

		adapter = new MediaAdapter(this);
		setListAdapter(adapter);

		etName = (EditText) findViewById(R.id.etName);
		etContent = (EditText) findViewById(R.id.etContent);

		noteId = getIntent().getIntExtra(EXTRA_NOTE_ID, -1);

		if (noteId > -1) {
			etName.setText(getIntent().getStringExtra(EXTRA_NOTE_NAME));
			etContent.setText(getIntent().getStringExtra(EXTRA_NOTE_CONTENT));

			Cursor c = dbRead.query(NotesDB.TABLE_NAME_MEDIA, null,
					NotesDB.COLUMN_NAME_MEDIA_OWNER_NOTE_ID + "=?",
					new String[] { noteId + "" }, null, null, null);

			while (c.moveToNext()) {
				// System.out.println("onCreatePath:"+
				// c.getString(c.getColumnIndex(NotesDB.COLUMN_NAME_MEDIA_PATH)));
				System.out
						.println("onCreateContent:"
								+ c.getString(c
										.getColumnIndex(NotesDB.COLUMN_NAME_MEDIA_CONTENT)));
				adapter.add(new MediaListCellData(
						c.getString(c
								.getColumnIndex(NotesDB.COLUMN_NAME_MEDIA_CONTENT)),
						c.getString(c
								.getColumnIndex(NotesDB.COLUMN_NAME_MEDIA_PATH)),
						c.getInt(c.getColumnIndex(NotesDB.COLUMN_NAME_ID))));
			}
			adapter.notifyDataSetChanged();
		}
		initView();
		initEvent();

		// actionbar导航
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("                    滴滴备忘");
		actionBar.setHomeAsUpIndicator(R.drawable.actionbar_back_icon);
		actionBar.setDisplayHomeAsUpEnabled(true);// 设置返回图标
		actionBar.setDisplayShowHomeEnabled(false);// 没有系统图标
		actionBar.setHomeButtonEnabled(true);// 设置左侧返回图标，其中setHomeButtonEnabled和setDisplayShowHomeEnabled共同起作用，
		// 如果setHomeButtonEnabled设成false，即使setDisplayShowHomeEnabled设成true，图标也不能点击

		mListView = getListView();
		registerForContextMenu(mListView);

	}

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
		case R.id.edit:

			break;
		case R.id.share:
			Toast.makeText(AtyEditNote.this, "share", Toast.LENGTH_LONG).show();
			break;
		case R.id.delete:
			Toast.makeText(AtyEditNote.this, "delete", Toast.LENGTH_LONG)
					.show();
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
			i = new Intent(this, AtyPhotoViewer.class);
			i.putExtra(AtyPhotoViewer.EXTRA_PATH, data.path);
			System.out.println("onListItemClick.path:" + data.path);
			startActivity(i);
			break;
		case MediaType.VIDEO:
			i = new Intent(this, AtyVideoViewer.class);
			i.putExtra(AtyVideoViewer.EXTRA_PATH, data.path);
			startActivity(i);
			break;

		case MediaType.SOUND:
			i = new Intent(this, AtySoundViewer.class);
			i.putExtra(AtySoundViewer.EXTRA_PATH, data.path);
			startActivity(i);
			break;
		}

		super.onListItemClick(l, v, position, id);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case REQUEST_CODE_GET_PHOTO:
		case REQUEST_CODE_GET_VIDEO:

			if (resultCode == RESULT_OK) {
				DateFormat date = DateFormat.getDateTimeInstance(
						DateFormat.LONG, DateFormat.SHORT); // 显示日期，时间（精确到分）
				String Date = date.format(new Date());

				adapter.add(new MediaListCellData(Date, currentPath));
				adapter.notifyDataSetChanged();
				Uri audioPath = data.getData();
				Toast.makeText(this, audioPath.toString(), Toast.LENGTH_LONG)
						.show();
			}
			break;
		case REQUEST_CODE_GET_SOUND:

			if (resultCode == RESULT_OK || resultCode == 3) {
				/*
				 * audioPath = data.getData(); //获取到Uri Toast.makeText(this,
				 * audioPath.toString(), Toast.LENGTH_LONG).show(); recordPath =
				 * audioPath.toString();//转换 System.out.println(recordPath);
				 * 
				 * adapter.add(new MediaListCellData(recordPath));
				 * adapter.notifyDataSetChanged();
				 */

				/*
				 * 获取wav的Path
				 */
				String wavPath = data.getExtras().getString(
						IatDemo.VOICE_EXTRA_PATH);
				voiceContent = data.getExtras().getString(
						IatDemo.VOICE_EXTRA_CONTENT);
				// Toast.makeText(AtyEditNote.this, voiceContent,
				// Toast.LENGTH_LONG);
				System.out.println("voiceContent:" + voiceContent);
				etContent.append("/" + stringFormat(voiceContent));
				System.out.println("____________________________" + wavPath);

				adapter.add(new MediaListCellData(voiceContent, wavPath));
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
				System.out.println("dataContent:" + data.content);
				if (data.content != null) {
					cv = new ContentValues();
					cv.put(NotesDB.COLUMN_NAME_MEDIA_PATH, data.path);
					System.out.println("saveMedia:data.path:" + data.path);
					cv.put(NotesDB.COLUMN_NAME_MEDIA_OWNER_NOTE_ID, noteId);
					cv.put(NotesDB.COLUMN_NAME_MEDIA_CONTENT, data.content);
					System.out.println("++++" + data.content);
					// System.out.println(data.content.toString());
					dbWrite.insert(NotesDB.TABLE_NAME_MEDIA, null, cv);
				} else {
					cv = new ContentValues();
					cv.put(NotesDB.COLUMN_NAME_MEDIA_PATH, data.path);
					cv.put(NotesDB.COLUMN_NAME_MEDIA_OWNER_NOTE_ID, noteId);
					// cv.put(NotesDB.COLUMN_NAME_MEDIA_CONTENT,);
					System.out.println("else dataContent" + data.content);
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
		cv.put(NotesDB.COLUMN_NAME_NOTE_CONTENT, etContent.getText().toString());
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
	private EditText etName, etContent;
	private MediaAdapter adapter;
	private NotesDB db;
	private SQLiteDatabase dbRead, dbWrite;
	private String currentPath = null;

	private Uri audioPath;
	private String recordPath;
	private String wavPath;

	private String voiceContent;

	public static final int REQUEST_CODE_GET_PHOTO = 1;
	public static final int REQUEST_CODE_GET_VIDEO = 2;
	public static final int REQUEST_CODE_GET_SOUND = 3;

	public static final String EXTRA_NOTE_ID = "noteId";
	public static final String EXTRA_NOTE_NAME = "noteName";
	public static final String EXTRA_NOTE_CONTENT = "noteContent";

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
			TextView tvSoundContent = (TextView) convertView
					.findViewById(R.id.tvSoundContent);

			ivIcon.setImageResource(data.iconId);
			// System.out.println("icon:"+data.iconId);
			// ivIcon.setImageBitmap(getVideoThumbnail(urlvideo, 200, 200,
			// MediaStore.Images.Thumbnails.MICRO_KIND));
			tvPath.setText(data.path);
			tvSoundContent.setText(data.content);
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
		String content = "";
		int iconId = R.drawable.ic_launcher;

		public MediaListCellData(String content, String path) {
			this.content = content;
			this.path = path;

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

		public MediaListCellData(String content, String path, int id) {

			this(content, path);
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
		switch (item.getItemId()) {
		case android.R.id.home:
			saveMedia(saveNote());
			setResult(RESULT_OK);
			finish();

			return true;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public static String stringFormat(String s) {
		String str = s
				.replaceAll(
						"[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|-]",
						"");
		return str;
	}

}
