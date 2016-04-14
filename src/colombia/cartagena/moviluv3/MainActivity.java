package colombia.cartagena.moviluv3;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import colombia.cartagena.moviluv3.DataBase.DbHelper;
import colombia.cartagena.moviluv3.Entidad.Estudiante;
import colombia.cartagena.moviluv3.Horario.FragmentHorarioEntrada;
import colombia.cartagena.moviluv3.Horario.FragmentHorarioSalida;
import colombia.cartagena.moviluv3.Horario.FragmentHorarios;
import colombia.cartagena.moviluv3.Mapa.FragmetMap;
import colombia.cartagena.moviluv3.Notificacion.FragmentNotificacion;

import colombia.cartagena.moviluv3.Registro.RegistroActivity;
import colombia.cartagena.moviluv3.Session.UserSessionManager;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpGet;
import com.koushikdutta.async.http.AsyncHttpPost;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.koushikdutta.async.http.AsyncHttpClient.StringCallback;
import com.koushikdutta.async.http.body.JSONObjectBody;
import com.koushikdutta.async.http.socketio.Acknowledge;
import com.koushikdutta.async.http.socketio.ConnectCallback;
import com.koushikdutta.async.http.socketio.EventCallback;
import com.koushikdutta.async.http.socketio.SocketIOClient;

import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

public class MainActivity extends android.support.v4.app.FragmentActivity {

	private final int NOTIFICATION_ID = 1010;
	// User Session Manager Class
	UserSessionManager session;
	Estudiante estudiante;
	boolean verifica = false;
	boolean isOk = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		session = new UserSessionManager(getApplicationContext());
		estudiante = new Estudiante();
		cargaGoogleMap();

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		// getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// get user data from session
		HashMap<String, String> user = session.getUserDetails();

		// get name
		String name = user.get(UserSessionManager.KEY_NAME);

		// get email
		String email = user.get(UserSessionManager.KEY_EMAIL);

		// getActionBar().setTitle(email);

		ActionBar abar = getActionBar();

		Resources res = getResources();

		// Establecemos el modo de navegación por pestañas
		abar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Ocultamos el título de la actividad
		// abar.setDisplayShowTitleEnabled(false);

		// Creamos las pestañas
		ActionBar.Tab tab1 = abar.newTab().setIcon(
				res.getDrawable(R.drawable.ic_action_user));

		ActionBar.Tab tab2 = abar.newTab().setIcon(
				res.getDrawable(R.drawable.ic_action_calendar));

		ActionBar.Tab tab3 = abar.newTab().setIcon(
				res.getDrawable(R.drawable.ic_action_notify));

		Bundle bundle = getIntent().getExtras();

		String id1 = "IdFHorarios";
		String id2 = "IdFMapa";
		String id3 = "IdFNotific";
		Bundle arguments = new Bundle();
		arguments.putString("id1", id1);
		arguments.putString("id2", id2);
		arguments.putString("id3", id3);
		arguments.putString("_id", bundle.getString("_id"));
		arguments.putString("nombre", bundle.getString("nombre"));
		arguments.putString("direccion", bundle.getString("direccion"));

		FragmentHorarios tab1fragHorario = FragmentHorarios.newInstance(
				arguments, MainActivity.this);
		FragmetMap tab2fragMapa = FragmetMap.newInstance(arguments, MainActivity.this);
		FragmentNotificacion tab3fragNoty = FragmentNotificacion
				.newInstance(arguments);

		tab1.setTabListener(new MiTabListener(tab2fragMapa));
		tab2.setTabListener(new MiTabListener(tab1fragHorario));
		tab3.setTabListener(new MiTabListener(tab3fragNoty));

		// Añadimos las pestañas a la action bar

		abar.addTab(tab1);
		abar.addTab(tab2);
		abar.addTab(tab3);

	}

	public void CambiarHorario(View view) {

		Fragment newFragment = null;
		Bundle bundle = getIntent().getExtras();

		Button bten = (Button) findViewById(R.id.bt_entrada);
		Button btsa = (Button) findViewById(R.id.bt_salida);
		if (view == findViewById(R.id.bt_entrada)) {
			newFragment = FragmentHorarioEntrada.newInstance(bundle,
					MainActivity.this);
			bten.setActivated(true);
			btsa.setActivated(false);
			// Log.e("TAG", "ENtrada");

		} else if (view == findViewById(R.id.bt_salida)) {
			newFragment = FragmentHorarioSalida.newInstance(bundle,MainActivity.this);
			bten.setActivated(false);
			btsa.setActivated(true);
			// Log.e("TAG", "Salida");
		}

		FragmentManager fm = getFragmentManager();

		FragmentTransaction ftra = fm.beginTransaction();

		ftra.replace(R.id.contenedor_principal, newFragment);
		// ftra.addToBackStack(null);
		ftra.commit();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:

			// nkkToast.makeText(MainActivity.this,
			// "presiono el home",Toast.LENGTH_SHORT).show();
			finish();
			return true;

		case R.id.logout:
			// close session
			SharedPreferences prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
			prefs.getString("loged", "No");
			DbHelper db = new DbHelper(MainActivity.this);
			db.borrar();
			
			session.logoutUser();
			finish();

			return true;

		case R.id.primero:
			// close session
			// Intent intent = new Intent(MainActivity.this,
			// HorarioActivity.class);
			// startActivity(intent);
			return true;

		case R.id.segundo:
			// close session
			String serverUrl = getResources().getString(R.string.ServerUrl);
			getJson(serverUrl);

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**************************************** Notificacion ********************************************/

	private void NotifyWithTime(Boolean time) {
		if (time) {

			Timer timer = new Timer();
			TimerTask timerTask = new TimerTask() {
				@Override
				public void run() {
					// triggerNotification();
				}
			};
			timer.schedule(timerTask, 3000);

		} else {
			// triggerNotification();
		}
	}

	// private void triggerNotification() {
	//
	// String titulo = "Notificación Movilú";
	//
	// NotificationManager notificationManager = (NotificationManager)
	// getSystemService(NOTIFICATION_SERVICE);
	// NotificationCompat.Builder mBuilder = new
	// NotificationCompat.Builder(MainActivity.this)
	// .setSmallIcon(android.R.drawable.stat_notify_more)
	// .setLargeIcon(
	// (((BitmapDrawable) getResources().getDrawable(
	// R.drawable.ic_launcher)).getBitmap()))
	// .setContentTitle("Movilu v.3")
	// .setContentText(estudiante.get_id())
	// .setContentInfo("" + 2)
	// .setSound(
	// RingtoneManager
	// .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
	// .setTicker("¡Nuevo mensaje!");
	//
	// RemoteViews contentView = new RemoteViews(getPackageName(),
	// R.layout.notificacion_layout);
	// contentView.setImageViewResource(R.id.img_notificacion,
	// R.drawable.ic_launcher);
	//
	// contentView.setTextViewText(R.id.txt_notificacion,
	// "Movilú \n Se ha creado un el sitio: " + titulo);
	//
	// // Intent notificationIntent = new Intent(this, HorarioActivity.class);
	// // PendingIntent contentIntent = PendingIntent.getActivity(this,
	// // 0,notificationIntent, 0);
	// // mBuilder.setContentIntent(contentIntent);
	// // mBuilder.setAutoCancel(true);
	// // notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	//
	// }

	/**************************************** Http async ********************************************/
	private void getPositionInZone(String url) {

		AsyncHttpClient.getDefaultInstance().executeString(
				new AsyncHttpGet(url), new StringCallback() {

					@Override
					public void onCompleted(Exception arg0,
							AsyncHttpResponse arg1, String argument) {
						// TODO Auto-generated method stub
						JSONObject jo;
						try {
							jo = new JSONObject(argument);
							if (jo.getString("respuesta").equals("true")) {
								estudiante.setInZone(jo.getString("respuesta"));
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

	}

	/**************************************** Socket.io async ********************************************/
	private void getJson(String url) {

		SocketIOClient.connect(AsyncHttpClient.getDefaultInstance(), url,
				new ConnectCallback() {
					@Override
					public void onConnectCompleted(Exception ex,
							SocketIOClient client) {
						if (ex != null) {
							ex.printStackTrace();
							return;
						}

						client.emit("hola", getJsonArray());

						client.on("quetal", new EventCallback() {
							@Override
							public void onEvent(JSONArray argument,
									Acknowledge arg1) {
								// TODO Auto-generated method stub
								try {
									JSONObject json_obj = argument
											.getJSONObject(0);
									estudiante.set_id(json_obj.getString("_id"));
									estudiante.setNombre(json_obj
											.getString("nombre"));
									estudiante.setCorreo(json_obj
											.getString("correo"));
									estudiante.setPassword(json_obj
											.getString("password"));

								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								NotifyWithTime(false);

							}
						});

					}
				});

	}

	private JSONArray getJsonArray() {
		JSONArray json = new JSONArray();
		JSONObject Jo = new JSONObject();

		try {
			Jo.put("Nombre", getPreferences("nombre"));
			Jo.put("Correo", getPreferences("correo"));
			Jo.put("_id", getPreferences("_id"));
			// Jo.put("Id", );
			json.put(Jo);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return json;
	}

	/**************************************** Preferences ********************************************/
	public String getPreferences(String key) {
		String value = "";
		SharedPreferences prefs = getSharedPreferences("MisPreferencias",
				Context.MODE_PRIVATE);

		if (key.equals("nombre")) {
			value = prefs.getString(key, "none");
		}

		if (key.equals("correo")) {
			value = prefs.getString(key, "none");
		}

		if (key.equals("password")) {
			value = prefs.getString(key, "none");
		}

		if (key.equals("_id")) {
			value = prefs.getString(key, "none");
		}

		return value;
	}

	/**************************************** Map ********************************************/

	/** Referencia a GoogleMap */
	private GoogleMap mMap;

	/*
	 * El método cargaGoogleMap realiza la carga de la referencia a GoogleMap.
	 */
	private void cargaGoogleMap() {
		if (mMap == null) {
			// mMap = ((SupportMapFragment)
			// getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

			if (mMap != null) {
				// mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				mMap.setMyLocationEnabled(true);
				OpcionesMap();
				Eventos();
			}
		}
	}

	/**
	 * This is where we can add markers or lines, add listeners or move the
	 * camera. In this case, we just add a marker near Africa.
	 * <p>
	 * This should only be called once and when we are sure that {@link #mMap}
	 * is not null.
	 */
	private void OpcionesMap() {
		LatLng cartagena = new LatLng(10.413534, -75.536512);

		CameraPosition camPos = new CameraPosition(cartagena, 16, 10, 10);

		CameraUpdate ActualizarCamera = CameraUpdateFactory
				.newCameraPosition(camPos);
		mMap.animateCamera(ActualizarCamera);

		CrearMarker(cartagena, "Cartagena", "ninguno..");

	}

	private void CrearMarker(LatLng coordenadas, String titulo, String detalle) {

		// pintarNotificacion bitmap = new pintarNotificacion(getResources(),
		// Idimg,texto);

		mMap.addMarker(new MarkerOptions()
				.position(
						new LatLng(coordenadas.latitude, coordenadas.longitude))
				.title(titulo).snippet(detalle));
	}

	private void Eventos() {

		mMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(Marker marker) {
				Toast.makeText(MainActivity.this,
						"Marcador presionado:\n " + marker.getTitle(),
						Toast.LENGTH_SHORT).show();
				return false;
			}
		});

		mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			@Override
			public void onInfoWindowClick(Marker marker) {
				Toast.makeText(MainActivity.this,
						"Detalles del marcador:\n " + marker.getSnippet(),
						Toast.LENGTH_SHORT).show();
			}
		});

		mMap.setOnMapClickListener(new OnMapClickListener() {
			public void onMapClick(LatLng point) {
				// Projection proj = mMap.getProjection();
				// Point coord = proj.toScreenLocation(point);
				LatLng myPosition = new LatLng(point.latitude, point.longitude);
				String url = getString(R.string.ServerUrl) + "/isInside/"
						+ myPosition.longitude + "/" + myPosition.latitude;

				CrearMarker(myPosition, "titulo", "detalles");
				getPositionInZone(getString(R.string.ServerUrl) + "/isInside/"
						+ myPosition.latitude + "/" + myPosition.longitude);
				Toast.makeText(MainActivity.this, estudiante.getInZone(),
						Toast.LENGTH_LONG).show();
				// Toast.makeText(MainActivity.this,"Click\n" + "Lat: " +
				// point.latitude + "\n" + "Lng: "+ point.longitude + "\n" +
				// "X: " + coord.x+ " - Y: " + coord.y,
				// Toast.LENGTH_SHORT).show();

			}
		});
	}

}
