package colombia.cartagena.moviluv3.Session;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpClient.StringCallback;
import com.koushikdutta.async.http.AsyncHttpGet;
import com.koushikdutta.async.http.AsyncHttpPost;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.koushikdutta.async.http.body.JSONObjectBody;
import com.koushikdutta.async.http.callback.HttpConnectCallback;
import com.koushikdutta.async.http.socketio.Acknowledge;
import com.koushikdutta.async.http.socketio.ConnectCallback;
import com.koushikdutta.async.http.socketio.EventCallback;
import com.koushikdutta.async.http.socketio.SocketIOClient;

import colombia.cartagena.moviluv3.MainActivity;
import colombia.cartagena.moviluv3.R;
import colombia.cartagena.moviluv3.DataBase.DbHelper;
import colombia.cartagena.moviluv3.Entidad.Estudiante;
import colombia.cartagena.moviluv3.Entidad.Horario;
import colombia.cartagena.moviluv3.R.id;
import colombia.cartagena.moviluv3.R.layout;
import colombia.cartagena.moviluv3.Registro.RegistroActivity;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	String nombre, passw, correo, _id, direccion;
	Button btnLogin;
	EditText txtCorreo, txtPassword;
	int log = 0;

	Estudiante estudiante;

	// User Session Manager Class
	UserSessionManager session;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		estudiante = new Estudiante();
		// User Session Manager
		session = new UserSessionManager(getApplicationContext());

		// get Email, Password input text
		txtCorreo = (EditText) findViewById(R.id.txtCorreo);
		txtPassword = (EditText) findViewById(R.id.txtPassword);

		// User Login button
		btnLogin = (Button) findViewById(R.id.btnLogin);

		// Login button click event
		btnLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// Get username, password from EditText
				String correo = txtCorreo.getText().toString();
				String password = txtPassword.getText().toString();

				getUser(correo, password);

			}
		});
	}


	/************************************ Http Async ****************************************************/
	private void getUser(final String email, final String password) {
		String url = getResources().getString(R.string.ServerUrl)+ "/api/get/usuario/" + email + "/" + password;

		AsyncHttpClient.getDefaultInstance().executeString(
				new AsyncHttpGet(url), new StringCallback() {

					@Override
					public void onCompleted(Exception arg0,
							AsyncHttpResponse arg1, String argument) {
						// TODO Auto-generated method stub

						try {

							JSONObject jo = new JSONObject(argument);
							JSONArray jsonArrayE = jo.getJSONObject("horarios").getJSONArray("entrada");
							JSONArray jsonArrayS = jo.getJSONObject("horarios").getJSONArray("salida");

							addPreferences("_id", jo.getString("_id"));
							addPreferences("nombre", jo.getString("nombre"));
							addPreferences("password", jo.getString("password"));
							addPreferences("correo", jo.getString("correo"));
							String dir = jo.getString("barrio") + " " + jo.getString("direccion");
							addPreferences("direccion", dir);

							DbHelper db = new DbHelper(getApplicationContext());
							db.borrar();

							for (int i = 0; i < jsonArrayE.length(); i++) {

								String _idDia = jsonArrayE.getJSONObject(i).getJSONObject("dia").getString("_id");
								String _idHora = jsonArrayE.getJSONObject(i).getJSONObject("hora").getString("_id");
								String dia = jsonArrayE.getJSONObject(i).getJSONObject("dia").getString("titulo");
								String hora = jsonArrayE.getJSONObject(i).getJSONObject("hora").getString("titulo");

								db.Insertar(dia, hora);
							}

							DbHelper bd = new DbHelper(getApplicationContext());
							bd.borrarHorarioSalida();

							for (int i = 0; i < jsonArrayS.length(); i++) {

								String _idDia = jsonArrayS.getJSONObject(i).getJSONObject("dia").getString("_id");
								String _idHora = jsonArrayS.getJSONObject(i).getJSONObject("hora").getString("_id");
								String dia = jsonArrayS.getJSONObject(i).getJSONObject("dia").getString("titulo");
								String hora = jsonArrayS.getJSONObject(i).getJSONObject("hora").getString("titulo");

								db.InsertarHorarioSalida(dia, hora);
							}

							addPreferences("loged", "Ok");

							// Validate if username, password is filled
							if (email.trim().length() > 0
									&& password.trim().length() > 0) {

								nombre = getPreferences("nombre");
								passw = getPreferences("password");
								correo = getPreferences("correo");
								_id = getPreferences("_id");
								direccion = getPreferences("direccion") ;

								if (getPreferences("loged").equals("Ok")) {

									// emitSocketIO(getString(R.string.ServerUrl));

									// Starting MainActivity
									Intent i = new Intent(getApplicationContext(),MainActivity.class);
									i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

									i.putExtra("_id", _id);
									i.putExtra("nombre", nombre);
									i.putExtra("direccion", direccion);
									// Add new Flag to start new Activity
									i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									startActivity(i);

									finish();

								} else {

									// username / password doesn't match&
									Toast.makeText(getApplicationContext(),"Username/Password is incorrect",Toast.LENGTH_LONG).show();
								}

							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

	}

	/**************************************** Socket.io async ********************************************/
	private void emitSocketIO(String url) {

		SocketIOClient.connect(AsyncHttpClient.getDefaultInstance(), url,
				new ConnectCallback() {
					@Override
					public void onConnectCompleted(Exception ex,
							SocketIOClient client) {
						if (ex != null) {
							ex.printStackTrace();
							return;
						}

						client.emit("nuevo usuario",getJsonArray(getPreferences("_id")));

						client.on("saludo", new EventCallback() {
							@Override
							public void onEvent(JSONArray argument,
									Acknowledge arg1) {
								// TODO Auto-generated method stub
								try {
									JSONObject json_obj = argument
											.getJSONObject(0);
									// estudiante.set_id(json_obj.getString("_id"));
									estudiante.setNombre(json_obj
											.getString("nombre"));
									// estudiante.setCorreo(json_obj.getString("correo"));
									// estudiante.setPassword(json_obj.getString("password"));
									System.out.println(estudiante.getNombre());

								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								// NotifyWithTime(false);

							}
						});

					}
				});

	}

	private JSONArray getJsonArray(String _id) {
		JSONArray json = new JSONArray();
		JSONObject Jo = new JSONObject();

		try {
			Jo.put("_id", _id);
			// Jo.put("Id", );
			json.put(Jo);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return json;
	}

	/************************************* Preferences *********************************************************/
	private void addPreferences(String key, String value) {
		SharedPreferences prefs = getSharedPreferences("MisPreferencias",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(key, value);
		editor.commit();
	}

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

		if (key.equals("loged")) {
			value = prefs.getString(key, "none");
		}

		if (key.equals("direccion")) {
			value = prefs.getString(key, "none");
		}


		return value;
	}

}
