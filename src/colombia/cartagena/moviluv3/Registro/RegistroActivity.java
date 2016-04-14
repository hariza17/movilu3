package colombia.cartagena.moviluv3.Registro;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import colombia.cartagena.moviluv3.MainActivity;
import colombia.cartagena.moviluv3.R;
import colombia.cartagena.moviluv3.DataBase.DbHelper;
import colombia.cartagena.moviluv3.Entidad.Estudiante;
import colombia.cartagena.moviluv3.Entidad.Universidad;
import colombia.cartagena.moviluv3.R.id;
import colombia.cartagena.moviluv3.R.layout;
import colombia.cartagena.moviluv3.Session.SessionActivity;
import colombia.cartagena.moviluv3.Session.UserSessionManager;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpClient.JSONArrayCallback;
import com.koushikdutta.async.http.AsyncHttpGet;
import com.koushikdutta.async.http.AsyncHttpPost;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.koushikdutta.async.http.AsyncHttpClient.StringCallback;
import com.koushikdutta.async.http.body.JSONObjectBody;
import com.koushikdutta.async.http.socketio.Acknowledge;
import com.koushikdutta.async.http.socketio.ConnectCallback;
import com.koushikdutta.async.http.socketio.EventCallback;
import com.koushikdutta.async.http.socketio.SocketIOClient;

import android.R.integer;
import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class RegistroActivity extends Activity {

	EditText nombres,apellido,correo,contrasena,contrasena2,celular,fecha;
	// User Session Manager Class
	ArrayList<Universidad> Universidades;
	Spinner spinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registro);

		Universidades = new ArrayList<Universidad>();
		getUniversidades();

		nombres = (EditText) findViewById(R.id.txtNombres);
		apellido = (EditText) findViewById(R.id.txtApellidos);
		correo = (EditText) findViewById(R.id.txtCorreo);
		contrasena = (EditText) findViewById(R.id.txtPassword);
		contrasena2 = (EditText) findViewById(R.id.txtConfirPassword);
		celular = (EditText) findViewById(R.id.txtCelular);
		fecha = (EditText) findViewById(R.id.txtFecha);

		
		Button btnEnviar = (Button) findViewById(R.id.btnResgitrar);

		btnEnviar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (validar()) {
					
					String name,lastname,email,password,password2,date,university,celphone;
					
					name = nombres.getText().toString();
					lastname = apellido.getText().toString();
					email = correo.getText().toString();
					password = contrasena.getText().toString();
					password2 = contrasena2.getText().toString();
					celphone = celular.getText().toString();
					date = fecha.getText().toString();
					university = Universidades.get(spinner.getSelectedItemPosition()).getId();
					
					System.out.println("Nombres: " + name);
					System.out.println("Apellidos: " + lastname);
					System.out.println("Correo: " + email);
					System.out.println("Contraseña: " + password);
					System.out.println("Celular: " + celphone);
					System.out.println("Fecha: " + date);
					System.out.println("IdUnivesidad: " + university);
					
					Registrar(name, lastname, email, password, password2, celphone, date, university);
					
				} else {
					Toast.makeText(RegistroActivity.this,"Todos los campos son requeridos",Toast.LENGTH_LONG).show();
				}
			}
		});

	}

	private void LLenarSpinner(final ArrayList<Universidad> Universidades){
		runOnUiThread(new Runnable() {
		     @Override
		     public void run() {

		       //stuff that updates ui
		    	 spinner  = (Spinner)findViewById(R.id.spinner1);
		 		ArrayList<String> nombres = new ArrayList<String>();
		 		
		 		for (int i = 0; i < Universidades.size(); i++) {
		 			nombres.add(Universidades.get(i).getNombre());
		 		}
		 		
		 		spinner = (Spinner)findViewById(R.id.spinner1);
		 		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(RegistroActivity.this, android.R.layout.simple_spinner_item, nombres); //selected item will look like a spinner set from XML
		 		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 		spinner.setAdapter(spinnerArrayAdapter);
		    }
		});
		
	}
	
	private boolean validar() {
		boolean valida = true;

		if (nombres.getText().toString().equals("")) {
			valida = false;
		}
		
		if (apellido.getText().toString().equals("")) {
			valida = false;
		}
		

		if (celular.getText().toString().equals("")) {
			valida = false;
		}

		if (correo.getText().toString().equals("")) {
			valida = false;
		}
		
		if (fecha.getText().toString().equals("")) {
			valida = false;
		}

		if (contrasena.getText().toString().equals("")) {
			valida = false;
		}
		
		if(spinner.getSelectedItem() == null){
			valida = false;
		}
		
		return valida;
	}

	/*********************** HttpAsync Post ********************************/
	
	private void Registrar(String nombre, String apellido, String correo, String password,String password2, String celular, String fecha, String idUniversidad) {

		JSONArray json = new JSONArray();
		JSONObject Jo = new JSONObject();

		try {
			Jo.put("nombres", nombre);
			Jo.put("apellidos", apellido);
			Jo.put("celular", celular);
			Jo.put("fecha_nacimiento", fecha);
			Jo.put("universidad", idUniversidad);
			Jo.put("correo", correo);
			Jo.put("password", password);
			Jo.put("password2", password2);
			json.put(Jo);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		JSONObjectBody jsonBody = new JSONObjectBody(Jo);

		try {
			String postUrl = getResources().getString(R.string.ServerUrl) + "/api/registro/estudiante";
			AsyncHttpPost post = new AsyncHttpPost(postUrl);
			post.setBody(jsonBody);

			AsyncHttpClient.getDefaultInstance().executeJSONObject(post,new AsyncHttpClient.JSONObjectCallback() {

						@Override
						public void onCompleted(Exception arg0,AsyncHttpResponse arg1, JSONObject argument) {
							// TODO Auto-generated method stub
							try {

								if(argument.getString("value").equals("true")){
//									System.out.println(argument.getString("mensaje"));
									Intent intent = new Intent(RegistroActivity.this,SessionActivity.class);
									startActivity(intent);
								}else{
//									System.out.println(argument.getJSONArray("mensaje").toString());
								}
								
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
//							goActivity(argument);
						}
					});

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	
	/************************************ Http Async ****************************************************/
	private void getUniversidades() {
		String url = getResources().getString(R.string.ServerUrl)+ "/api/get/universidades";
		
		AsyncHttpClient.getDefaultInstance().executeJSONArray(new AsyncHttpGet(url), new JSONArrayCallback() {
			
			@Override
			public void onCompleted(Exception arg0, AsyncHttpResponse arg1,JSONArray jsonArray) {
				// TODO Auto-generated method stub
				try{
				
					for (int i = 0; i < jsonArray.length(); i++) {
						String id = jsonArray.getJSONObject(i).getString("_id");
						String nombre = jsonArray.getJSONObject(i).getString("nombre");
						String direccion = jsonArray.getJSONObject(i).getString("direccion");
						String location = jsonArray.getJSONObject(i).getString("location");
						Universidades.add(new Universidad(id, nombre, direccion, location));
						System.out.println(Universidades.get(i).getNombre());
					}					
					
					LLenarSpinner(Universidades);
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
	}
	
	public void showDatePickerDialog(View v) {
		DatePickerFragment newFragment = new DatePickerFragment(fecha);
		newFragment.show(getFragmentManager(), "datePicker");

	}

	public static class DatePickerFragment extends DialogFragment implements
			OnDateSetListener {

		private static String anio;
		private static String mes;
		private static String dia;
		private static String fecha;
		private EditText txt;

		public DatePickerFragment(EditText textFeha) {
			this.txt = textFeha;
		}

		public String getFecha() {
			return fecha;
		}

		public static String getAnio() {
			return anio;
		}

		public static void setAnio(String anio) {
			DatePickerFragment.anio = anio;
		}

		public static String getMes() {
			return mes;
		}

		public static void setMes(String mes) {
			DatePickerFragment.mes = mes;
		}

		public static String getDia() {
			return dia;
		}

		public static void setDia(String dia) {
			DatePickerFragment.dia = dia;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		@Override
		public void onDateSet(DatePicker arg0, int anio, int mes, int dia) {
			System.out.println("Mes: " + mes);
			System.out.println("Dia: " + dia);
			System.out.println("Anio: " + anio);
			this.anio = anio + "";
			this.mes = (mes+1) + "";
			this.dia = dia + "";
			this.fecha = this.mes + "/" + this.dia + "/" + this.anio;
			this.txt.setText(fecha);
		}
	}


}
