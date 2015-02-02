package com.Shareitapplication.shareit;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint({ "DefaultLocale", "NewApi" })
public class connectMe extends Activity implements OnClickListener,OnItemClickListener ,OnScrollListener, TextWatcher, OnCancelListener {

	public static FilesAndFolders fif=null;

	public  static ArrayAdapter<String> adapter=null;

	public static Context con;
	//Server ser;
	Timer timer;
	static String [] filearr=null;
	Vector<Integer> Searching=new Vector<Integer>();
	Vector<Integer> Searching2=new Vector<Integer>();
	String Searchingnow="";
	int Searchfield=0;
	int timerRound=0;
	boolean searching=false;
	DatagramSocket socket = null;

	downloading download=null;
	/*public*//* static*/ ListView listView =null;
	static Client c=null;
	public static Button re;
	ArrayAdapter<String> Searchadapter = null;
	/** Called when the activity is first created. */

	AdView adView =null;
	//AdRequest request = new AdRequest();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Tracker.activities.add(this);
		con=this;
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {

			e.printStackTrace();
		}
		//adView = new AdView(this, AdSize.BANNER, "ca-app-pub-5024757240238210/1128899887");        

		if(Tracker.bc==0){

			adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1); 
			refresh();
		}
		Tracker.bc=1;

		//adView = new AdView(this, AdSize.BANNER, "ca-app-pub-5024757240238210/1128899887");     



	}
	LinearLayout rlmain =null;
	Timer timerColor=null;
	@SuppressWarnings("deprecation")
	@Override
	public void onResume( ) {
		super.onResume();

		Tracker.currentCon=this;
		if(Client.getIPAddress(true).compareTo(Tracker.my_ip)!=0){
			Tracker.my_ip=Client.getIPAddress(true);
			refresh();
		}
		filearr=new String[Tracker.files.size()];
		//listarr.clear();
		adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1/*,filearrlistarr*/); 
		for(int i=0;i<Tracker.files.size();i++){
			filearr[i]=Tracker.files.elementAt(i).getName();
			//listarr.add(filearr[i]);
			adapter.add(filearr[i]);
		}


		con=this;


		rlmain = new LinearLayout(this);
		rlmain.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		rlmain.setOrientation(LinearLayout.VERTICAL);
		re=new Button(this);
		re.setText("Refresh");
		re.setBackgroundColor(Color.BLACK);
		re.setTextColor(Color.rgb(255,69,0));
		AdRequest request = new AdRequest();
		re.setOnClickListener(this);					 
		adView =new AdView(this, AdSize.BANNER, "ca-app-pub-5024757240238210/4712991480");
		adView.loadAd(request);
		// adView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,50));
		rlmain.addView(adView);
		rlmain.addView(re);

		//RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(MainActivity.width,MainActivity.height);
		//params.leftMargin =0;
		//params.topMargin = MainActivity.height/16;

		listView = new ListView(this);
		//listView.setBackgroundColor(Color.BLACK);
		listView.setDivider(new ColorDrawable(Color.BLACK));

		listView.setDividerHeight(1);



		listView.setAdapter(adapter);
		listView.setOnScrollListener(this);

		listView.setOnItemClickListener(this);
		listView.setClickable(true);
		//rl.addView(listView,params);
		rlmain.addView(listView);
		listView.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener 
				(){ 
			@Override 
			public boolean onItemLongClick(AdapterView<?> av, final View v, final int 
					pos, long id) { 
				AlertDialog.Builder builder = new AlertDialog.Builder(con);
				builder.setMessage("Do you want to download File "+Tracker.files.get(pos).getName()+"?")
				.setCancelable(false)
				.setNegativeButton("No",  new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						//do things
					}
				})
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						try {

							v.setBackgroundColor(Color.YELLOW);
							//new RetreiveFeedTask().execute(pos);
							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
								new RetreiveFeedTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,  pos);
							else
								new RetreiveFeedTask().execute( pos);


						} catch (Exception e) {

							e.printStackTrace();
						}


					}
				});
				AlertDialog alert = builder.create();
				alert.setCanceledOnTouchOutside(true);
				alert.show();

				return false; 
			} 
		}); 

		timerColor=new Timer();

		timerColor.schedule(new TimerTask() {
			@Override
			public void run() {

				connectMe.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						try{
							adapter.notifyDataSetChanged();
						}
						catch(Exception e){
							e.printStackTrace();
						}

					}
				});
			}
		}, 0, 2500);

		this.setContentView(rlmain);


	}

	@Override
	public void onItemClick(AdapterView<?> parent, final View v,final int pos, long id)  {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);
		arrayAdapter.add("Name: "+Tracker.files.get(pos).getName());

		if(Tracker.Sizes.get(pos)!=0)
			arrayAdapter.add("Size: "+Tracker.Sizes.get(pos)+" bytes");
		else
			arrayAdapter.add("Size: "+"-");
		if(Tracker.bt_ineffect==false)
			arrayAdapter.add("Peer's Name: "+ Tracker.Get_from_map(Tracker.files.get(pos).getName(),pos)[1]);

		builder.setAdapter(arrayAdapter,null);

		builder.setPositiveButton("Download", new DialogInterface.OnClickListener() { 
			@Override
			public void onClick(DialogInterface dialog, int which) {


				v.setBackgroundColor(Color.YELLOW);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
					new RetreiveFeedTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,  pos);
				else
					new RetreiveFeedTask().execute( pos);

			}
		});
		builder.setNeutralButton("Comment", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				comment(pos);
			}
		});

		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		//builder.show();
		AlertDialog dialog=builder.create();
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();

	}


	ListView lv2=null;
	chatadapter chat_adapter; 
	AlertDialog.Builder comment_builder=null;
	@SuppressLint("SimpleDateFormat")
	private void comment(final int position) {
		comment_builder = new AlertDialog.Builder(this);
		comment_builder.setCancelable(true);
		comment_builder.setTitle(Tracker.files.get(position).getName()+" Comments");
		View footerView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.comments, null, false);
		final LinearLayout rl2 =(LinearLayout)footerView.findViewById(R.id.LinearLayout01);// new LinearLayout(this);

		final EditText  tv=(EditText) footerView.findViewById(R.id.editText1);//new EditText (this);

		final Button send=(Button) footerView.findViewById(R.id.button1);//new Button(this);
		if(Tracker.Colored_Chat_Shared.contains(Tracker.Get_List_name(Tracker.files.get(position).getName(),position))){
			Tracker.Colored_Chat_Shared.remove(Tracker.Get_List_name(Tracker.files.get(position).getName(),position));
		}

		if(Tracker.file_to_comment.get(position)==null){
			Tracker.file_to_comment.put(position, new Vector<ChatServerdata>());
		}

		chat_adapter = new chatadapter(this,R.layout.chat, Tracker.file_to_comment.get(position));

		final Timer timer2=new Timer();
		timer2.schedule(new TimerTask() {
			@Override
			public void run() {

				connectMe.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						try{
							chat_adapter.notifyDataSetChanged();
							lv2.setSelection(chat_adapter.getCount());
						}catch(Exception e){}
					}
				});
			}
		}, 0, 1000);




		send.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Calendar cal = Calendar.getInstance();
				cal.getTime();
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
				String ip=Tracker.Get_from_map(Tracker.files.get(position).getName(),position)[0];
				ChatServerdata csd=new ChatServerdata(Tracker.files.get(position).getName(),tv.getText().toString(),sdf.format(cal.getTime()),
						Tracker.id,ip);
				Tracker.file_to_comment.get(position).add(csd);
				//Tracker.file_to_adapter.get(csd.name).notifyDataSetChanged();
				tv.setText("");
				chat_adapter.notifyDataSetChanged();
				lv2.setSelection(chat_adapter.getCount());
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutput out2 = null;
				byte[] yourBytes=null;
				try {
					out2 = new ObjectOutputStream(bos);
					out2.writeObject(csd);
					out2.flush();
					yourBytes = bos.toByteArray();

					out2.close();
					bos.close();
				} catch (IOException e1) {

					e1.printStackTrace();
				}   

				for(int i=0;i<Tracker.ips.size();i++){
					InetAddress pingAddr;
					try {
						pingAddr = InetAddress.getByName(Tracker.ips.get(i));
						DatagramPacket packet = new DatagramPacket(yourBytes, yourBytes.length,
								pingAddr, 4330);

						socket.send(packet);
					} catch (Exception e) {

						e.printStackTrace();
					}


				}
			}

		});
		lv2=(ListView) footerView.findViewById(R.id.list);
		rl2.setBackgroundColor(Color.WHITE);
		lv2.setAdapter(chat_adapter);

		comment_builder.setView(rl2);


		tv.requestFocus();
		final AlertDialog dialog = comment_builder.create(); 
		dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey (DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK && 
						event.getAction() == KeyEvent.ACTION_UP && 
						!event.isCanceled()) {
					dialog.cancel();
					lv2=null;
					chat_adapter=null;
					timer2.cancel();
					comment_builder=null;
					System.gc();
					return true;
				}
				return false;
			}
		});

		dialog.setCanceledOnTouchOutside(true);

		dialog.show();

		dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE  | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		//dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		//dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
	}

	class toDownload{
		int vnum,pos;
		public toDownload(int a,int b){
			vnum=a;
			pos=b;
		}
	}

	class RetreiveFeedTask extends AsyncTask<Integer, Integer,Integer> {



		@Override
		protected Integer doInBackground(final Integer... params) {
			final int a=params[0];

			try {
				if(Tracker.bt_ineffect==false)
					new tcpConnection(Tracker.files.get(a).getName(),a);
				else{
					//new tcpConnection(Tracker.files.get(a).getName(),a);
				}
				Tracker.colored.add(Tracker.Get_List_name(Tracker.files.get(a).getName(), a));
			} catch (ClassNotFoundException e) {

				e.printStackTrace();
			} catch (IOException e) {
				new Thread() {
					public void run() {


						connectMe.this.runOnUiThread(new Runnable(){

							@Override
							public void run(){
								Tracker.NoSpace();
							}
						});


					}
				}.start();


			} catch (alertException e) {

				new Thread() {
					public void run() {


						connectMe.this.runOnUiThread(new Runnable(){

							@Override
							public void run(){
								AlertDialog.Builder builder = new AlertDialog.Builder(app.appconn);
								builder.setMessage("The file "+Tracker.files.get(a).getName()+" is no longer shareable")
								.setCancelable(false)
								.setPositiveButton("OK", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
									}
								});
								AlertDialog alert = builder.create();

								alert.setCanceledOnTouchOutside(true);

								alert.show();

							}
						});


					}
				}.start();

			} catch (hostDoesNotExistEx e) {
				new Thread() {
					public void run() {


						connectMe.this.runOnUiThread(new Runnable(){

							@Override
							public void run(){
								AlertDialog.Builder builder = new AlertDialog.Builder(app.appconn);
								builder.setMessage(Tracker.files.get(a).getName() +" host is no longer on Network File will be removed")
								.setCancelable(false)
								.setPositiveButton("OK", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
									}
								});
								AlertDialog alert = builder.create();
								alert.setCanceledOnTouchOutside(true);
								alert.show();
								/*String name=Tracker.files.get(a).getAbsolutePath();
								if(name.charAt(0)=='/')
									name=name.substring(1,name.length());
								int index=Tracker.files.indexOf(Tracker.files.get(a).getName());
								Tracker.files.remove(index);
								filearr=new String[Tracker.files.size()];
								for(int i=0;i<Tracker.files.size();i++){
									filearr[i]=Tracker.files.elementAt(i).getName();
								}
								Tracker.file_to_ip.remove(Tracker.files.get(a).getName());

								adapter=new ArrayAdapter<String>(connectMe.this, android.R.layout.simple_list_item_1,filearr); 
								listView.setAdapter(adapter);*/
								refresh();
							}
						});


					}
				}.start();


			} catch (PrivateException e) {
				new Thread() {
					public void run() {


						connectMe.this.runOnUiThread(new Runnable(){

							@Override
							public void run(){
								AlertDialog.Builder builder = new AlertDialog.Builder(app.appconn);
								builder.setTitle("Privacy");
								builder.setMessage(Tracker.files.get(a).getName()+" is Private and you are not in the file's private list");
								builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
									@Override
									public void onClick(DialogInterface dialog, int which) {
									}

								});


								//builder.show();
								AlertDialog alert = builder.create();
								alert.setCanceledOnTouchOutside(true);
								alert.show();
							}
						});


					}
				}.start();

			} 
			catch(Exception e){

			}


			return a;
		}

		protected void onPostExecute(Integer params) {
			super.onPostExecute(params);
			try{
				listView.getChildAt(params).setBackgroundColor(Color.GREEN);
			}
			catch(Exception e){

			}
			System.gc();
		}


	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ( keyCode == KeyEvent.KEYCODE_MENU ) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1);
			arrayAdapter.add("Your WiFi WebSite:\n"+Client.getIPAddress(true)+":5432");
			arrayAdapter.add("Search");
			arrayAdapter.add("Exit");
			ListView Alertlist = new ListView(this);
			Alertlist.setAdapter(arrayAdapter);
			Alertlist.setOnItemClickListener(new ListView.OnItemClickListener() {
				public void onItemClick(AdapterView<?> listView, View itemView, int position, long itemId) {
					if(position==0){
						if(arrayAdapter.getItem(0).compareTo("Search by File Name")==0){
							SBV();
							Searchfield=1;
						}
					}
					if(position==1){
						if(arrayAdapter.getItem(1).compareTo("Search")==0)
						{
							arrayAdapter.clear();
							arrayAdapter.add("Search by File Name");
							arrayAdapter.add("Search by User Name");
							arrayAdapter.notifyDataSetChanged();
						}
						else{

							SBV();
							Searchfield=2;
						}
					}
					if(position==2){
						if(arrayAdapter.getItem(2).compareTo("Exit")==0){
							AlertDialog.Builder builderexit = new AlertDialog.Builder(app.appconn);
							builderexit.setMessage("Do you want to exit?")
							.setCancelable(true)
							.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									Tracker.desrtory(0);
									System.gc();
									//Tracker.al.CancelAlarm(app.appconn);
									Intent intent = new Intent(Intent.ACTION_MAIN);
									intent.addCategory(Intent.CATEGORY_HOME);
									intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									startActivity(intent);
									System.exit(0);
								}
							}).setNegativeButton("No", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {

								}
							});
							AlertDialog alert = builderexit.create();

							alert.setCanceledOnTouchOutside(true);
							alert.show();
						}

					}


				}




			});
			Alertlist.setBackgroundColor(Color.WHITE);
			builder.setView(Alertlist);

			//builder.show();
			AlertDialog dialog=builder.create();
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();
			// dialog.setCanceledOnTouchOutside(true);
			return true;
		}

		else
		{

		}





		return super.onKeyDown(keyCode, event);
	}

	@SuppressWarnings("deprecation")
	private void SBV() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Search")
		.setCancelable(true);
		LinearLayout rl = new LinearLayout(this);
		rl.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		rl.setOrientation(LinearLayout.VERTICAL);
		final EditText input = new EditText(this);
		rl.addView(input);
		input.addTextChangedListener(this);
		Searchadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);
		builder.setOnCancelListener(this);
		ListView Alertlist = new ListView(this);
		Alertlist.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, final View v,final int pos, long id)  {
				int postion=0;
				if(Searching.size()>0){
					postion=Searching.get(pos);
				}
				if(Searching2.size()>0){
					postion=Searching2.get(pos);
				}
				final int postion2=postion;
				AlertDialog.Builder builder = new AlertDialog.Builder(connectMe.con);
				ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(connectMe.con,
						android.R.layout.simple_list_item_1);
				arrayAdapter.add("Name: "+Tracker.files.get(postion).getName());


				arrayAdapter.add("Size: "+Tracker.files.get(postion).length());
				if(Tracker.bt_ineffect==false)
					arrayAdapter.add("Peer's Name: "+ Tracker.Get_from_map(Tracker.files.get(postion).getName(),postion)[1]);


				builder.setAdapter(arrayAdapter,null);

				builder.setPositiveButton("Download", new DialogInterface.OnClickListener() { 
					@Override
					public void onClick(DialogInterface dialog, int which) {


						v.setBackgroundColor(Color.YELLOW);
						//new RetreiveFeedTask().execute(postion2);
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
							new RetreiveFeedTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,  postion2);
						else
							new RetreiveFeedTask().execute( postion2);
					}
				});
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});

				//builder.show();
				AlertDialog alert = builder.create();
				alert.setCanceledOnTouchOutside(true);
				alert.show();

			}

		});
		Alertlist.setAdapter(Searchadapter);
		rl.addView(Alertlist);
		builder.setView(rl);
		AlertDialog alert = builder.create();
		alert.setCanceledOnTouchOutside(true);
		alert.show();

		//builder.show();

	}



	@Override
	public void onBackPressed(){
		Tracker.desrtory(1);
		System.gc();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);

	}
	@Override
	protected void onPause() {
		super.onPause();
		timerColor.cancel();
		rlmain.removeView(adView);
		adView.removeAllViews();
		adView.destroy();
		System.gc();
		//Tracker.al.SetAlarm(app.appconn);
	}
	@Override
	public void onRestart(){
		super.onRestart();


	}



	class connect extends AsyncTask<String, String,String> {

		@Override
		protected String doInBackground(String... arg0) {
			c=new Client();
			try {
				//Tracker.wait=true;
				c.broadCasting();

			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				return Client.getIPAddress(true);
			} catch (Exception e) {

				e.printStackTrace();
			}

			return null;
		}



		protected void onPostExecute(String params) {
			super.onPostExecute(params);
			Toast.makeText(app.appconn.getApplicationContext(),"Your WiFi WebSite is:\n"+ params+":5432", Toast.LENGTH_LONG).show();

			/*if(Tracker.files.size()>0)
				adapter.clear();*/
			//adapter=new ArrayAdapter<String>(connectMe.con, android.R.layout.simple_list_item_1,filearr);
			/*try{
				for(int i=0;i<filearr.length;i++){
					filearr[i]="\0";

				}
				listarr.clear();
				adapter.clear();
				filearr=new String[Tracker.files.size()];

				for(int i=0;i<Tracker.files.size();i++){
					filearr[i]=Tracker.files.elementAt(i).getName();
					listarr.add(filearr[i]);
					adapter.add(Tracker.files.elementAt(i).getName());
				}



				//adapter=new ArrayAdapter<String>(connectMe.con, android.R.layout.simple_list_item_1,listarr); 

				//listView.setAdapter(adapter);
				//listView.invalidate();
				//adapter.notifyDataSetChanged();
				//adapter=new ArrayAdapter<String>(connectMe.con, android.R.layout.simple_list_item_1,listarr); 
				if(adapter!=null && filearr.length>0){

				}


			}
			catch (Exception e) {

				e.printStackTrace();
			}*/


		}


	}


	@Override
	public void onClick(View arg0) {
		refresh();
		/*if(Tracker.clicked==true)
			return;
		if(adapter!=null){
			//adapter.clear();
			Tracker.clearAll();
			Tracker.wait=0;

			Tracker.clicked=true;
			new connect().execute();
			try {
				Thread.sleep(60);
			} catch (InterruptedException e1) {

				e1.printStackTrace();
			}
			Tracker.timerRound=6;
			timer=new Timer();
			timer.schedule(new TimerTask() {
			    @Override
			    public void run() {

			        connectMe.this.runOnUiThread(new Runnable() {
			            @Override
			            public void run() {
			            	//Tracker.clicked=false;

			            	adapter.clear();
							System.gc();
							filearr=new String[Tracker.files.size()];

							int size=Tracker.files.size();
							Log.e("add", String.valueOf(size));
							int j=0;
							while(j<size){
								filearr[j]=Tracker.files.elementAt(j).getName();
								//listarr.add(Tracker.files.elementAt(j).getName());
								adapter.add(Tracker.files.elementAt(j).getName());
								Log.e("add", "adding");
								j++;
							}
							j=0;


							adapter.notifyDataSetChanged();


							Tracker.timerRound--;
							if(Tracker.timerRound<=0 ){
								timer.cancel();
								Tracker.clicked=false;
								Thread.interrupted();
							}
			            }
			        });
			    }
			}, 0, 1000);

		//	new load().execute();
			/*new Thread() {
				public void run() {


					connectMe.this.runOnUiThread(new Runnable(){

						@Override
						public void run(){
							int num=0;
							do{
								//synchronized(Tracker.lock){
									try{

										//listarr.clear();
										adapter.clear();
										System.gc();
										filearr=new String[Tracker.files.size()];

										int size=Tracker.files.size();
										Log.e("add", String.valueOf(size));
										int j=0;
										while(j<size){

											listarr.add(Tracker.files.elementAt(j).getName());
											adapter.add(Tracker.files.elementAt(j).getName());
											Log.e("add", "adding");
											j++;
										}
										j=0;


										adapter.notifyDataSetChanged();
									}
									catch (Exception e) {

										e.printStackTrace();
									}
									num++;
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {

										e.printStackTrace();
									}

								//}
							}while(num<5);

						}
					});


				}
			};*/
		//while(Tracker.wait!=0);

		//}

	}

	public void refresh() {
		if(Tracker.clicked==true)
			return;
		if(adapter!=null){
			//adapter.clear();
			Tracker.clearAll();
			//Tracker.wait=0;

			Tracker.clicked=true;
			//new connect().execute();
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				new connect().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,  "");
			else
				new connect().execute( "");
			try {
				Thread.sleep(60);
			} catch (InterruptedException e1) {

				e1.printStackTrace();
			}
			Tracker.timerRound=10;
			timer=new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {

					connectMe.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							//Tracker.clicked=false;

							adapter.clear();
							System.gc();
							filearr=new String[Tracker.files.size()];

							int size=Tracker.files.size();

							int j=0;
							while(j<size){
								try{
									filearr[j]=Tracker.files.elementAt(j).getName();

									adapter.add(Tracker.files.elementAt(j).getName());

									j++;
								}
								catch(Exception e){
									break;
								}

							}
							j=0;


							adapter.notifyDataSetChanged();


							Tracker.timerRound--;
							if(Tracker.timerRound<=0 ){
								timer.cancel();
								Tracker.clicked=false;
								Thread.interrupted();
							}
						}
					});
				}
			}, 0, 1000);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(adView !=null)
			adView.destroy();

	}

	public static void newFiles() {


		Vector<Vector<ChatServerdata>> vec=new Vector<Vector<ChatServerdata>>();
		Vector<Integer> Sizes=new Vector<Integer>();
		final File[] listOfFiles = new File[Tracker.newfiles.size()];
		for(int i=0;i<Tracker.newfiles.size();i++){
			listOfFiles[i]=Tracker.newfiles.get(i);
			if(Tracker.myfiles_to_comments.get(Tracker.newfiles.get(i).getName())==null)
				Tracker.myfiles_to_comments.put(Tracker.newfiles.get(i).getName(), new Vector<ChatServerdata>());
			vec.add(Tracker.myfiles_to_comments.get(Tracker.newfiles.get(i).getName()));
			Sizes.add((int)listOfFiles[i].length());
		}

		for(int i=0;i<Tracker.ips.size();i++)
		{
			if(Tracker.ips.get(i).compareTo(Tracker.my_ip)==0)
				continue;
			try{
				//new newFiles().execute(new data(listOfFiles,i,vec,Sizes));
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
					new newFiles().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,  new data(listOfFiles,i,vec,Sizes));
				else
					new newFiles().execute(new data(listOfFiles,i,vec,Sizes));
			}
			catch(Exception e){

			}


		}


	}
	static class data{
		File[] listOfFiles;
		public int num;
		Vector<Vector<ChatServerdata>> vec;
		Vector<Integer> Sizes=null;
		public data(File[] f,int num,Vector<Vector<ChatServerdata>> vec,Vector<Integer> Sizes){
			listOfFiles=f;
			this.num=num;
			this.vec=vec;
			this.Sizes=Sizes;
		}
	}
	static class newFiles extends AsyncTask<data, data,data> {

		@Override
		protected data doInBackground(data... arg) {
			try {
				new newFileDataClient(Tracker.ips.get(arg[0].num),arg[0].listOfFiles,arg[0].vec,arg[0].Sizes);


			} catch (ClassNotFoundException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}

			return null;
		}


	}
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		try{
			for(int i=firstVisibleItem;i<firstVisibleItem+visibleItemCount;i++){

				listView.getChildAt(i-firstVisibleItem).setBackgroundColor(Color.WHITE);

			}


			for(int i=firstVisibleItem;i<firstVisibleItem+visibleItemCount;i++){
				String str=Tracker.Get_List_name(adapter.getItem(i),i);
				if(Tracker.colored.contains(str)==true){
					listView.getChildAt(i-firstVisibleItem).setBackgroundColor(Color.GREEN);

				}
				if(Tracker.Colored_Chat_Shared.contains(str)==true){
					listView.getChildAt(i-firstVisibleItem).setBackgroundColor(Color.BLUE);

				}
			}
		}
		catch(Exception e){}

	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {


	}

	/*@Override
	public boolean onKey(View arg0, int arg1, KeyEvent event) {
		Searchingnow=Searchingnow+event.getUnicodeChar();
		if(event.getUnicodeChar()>=33 && event.getUnicodeChar()<126){
			if(Searchingnow.length()==1)
				for(int i=0;i<filearr.length;i++){
					if(filearr[i].charAt(0)==(char) event.getUnicodeChar()){
						Searching.add(i);
					}
				}
			else{
				if(Searching.size()>0){
					for(int i=0;i<Searching.size();i++){
						if(filearr[i].charAt((Searchingnow.length()-1))==(char) event.getUnicodeChar()){
							Searching2.add(i);
						}
					}
					Searching.clear();
				}
				else{
					for(int i=0;i<Searching2.size();i++){
						if(filearr[i].charAt((Searchingnow.length()-1))==(char) event.getUnicodeChar()){
							Searching.add(i);
						}
					}
					Searching2.clear();
				}
			}

		}
		return false;
	}*/

	@Override
	public void afterTextChanged(Editable arg0) {


	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {


	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		Searchingnow=arg0.toString();

		if(arg0.toString().compareTo("")==0){
			Searchadapter.clear();
			Searching2.clear();
			Searching.clear();
			Searchingnow="";
			return;
		}
		Searchingnow=Searchingnow.toLowerCase();
		if(Searchfield==1){
			if(Searchingnow.length()==1)
				for(int i=0;i<filearr.length;i++){
					if(filearr[i].charAt(0)==Searchingnow.charAt(0) || Character.toLowerCase(filearr[i].charAt(0))==Searchingnow.charAt(0)){
						Searching.add(i);
					}
				}
			else{
				if(Searching.size()>0){
					for(int i=0;i<Searching.size();i++){
						if(filearr[Searching.get(i)].charAt((Searchingnow.length()-1))==Searchingnow.charAt(Searchingnow.length()-1)
								|| Character.toLowerCase(filearr[Searching.get(i)].charAt((Searchingnow.length()-1)))==Searchingnow.charAt(Searchingnow.length()-1)){
							Searching2.add(Searching.get(i));
						}
					}
					Searching.clear();
				}
				else{
					for(int i=0;i<Searching2.size();i++){
						if(filearr[Searching2.get(i)].charAt((Searchingnow.length()-1))==Searchingnow.charAt(Searchingnow.length()-1)
								|| Character.toLowerCase(filearr[Searching2.get(i)].charAt((Searchingnow.length()-1)))==Searchingnow.charAt(Searchingnow.length()-1)){
							Searching.add(Searching2.get(i));
						}
					}
					Searching2.clear();
				}
			}
			Searchadapter.clear();
			if(Searching.size()>0){

				for(int i=0;i<Searching.size();i++){
					Searchadapter.add(filearr[Searching.get(i)]);
				}
				Searchadapter.notifyDataSetChanged();
			}
			if(Searching2.size()>0){

				for(int i=0;i<Searching2.size();i++){
					Searchadapter.add(filearr[Searching2.get(i)]);
				}
				Searchadapter.notifyDataSetChanged();
			}
		}
		if(Searchfield==2){
			if(Tracker.user_to_file.containsKey(Searchingnow)){
				for(int i=0;i<Tracker.user_to_file.get(Searchingnow).size();i++){
					try{
						Searchadapter.add(filearr[Tracker.user_to_file.get(Searchingnow).elementAt(i)]);
					}catch(Exception e){
						return;
					}
				}
				Searchadapter.notifyDataSetChanged();
			}
		}



	}

	@Override
	public void onCancel(DialogInterface arg0) {
		Searching2.clear();
		Searching.clear();
		Searchingnow="";
		Searchadapter.clear();
		Searchfield=0;
	}







}
