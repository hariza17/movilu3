package colombia.cartagena.moviluv3.Mapa;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpPost;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.koushikdutta.async.http.body.JSONObjectBody;

import colombia.cartagena.moviluv3.R;
import colombia.cartagena.moviluv3.Registro.RegistroActivity;
import colombia.cartagena.moviluv3.Session.SessionActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DialogRegistro extends Activity {

	String direccion,latlng,barrio,ref1,ref2,ref3,Id;
	EditText txtDireccion,txtBarrio,txtRef1,txtRef2,txtRef3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_registro);

		txtDireccion = (EditText) findViewById(R.id.txtDireccion);
		txtBarrio = (EditText) findViewById(R.id.txtBarrio);
		txtRef1 = (EditText) findViewById(R.id.txtPreferencia1);
		txtRef2 = (EditText) findViewById(R.id.txtPreferencia2);
		txtRef3 = (EditText) findViewById(R.id.txtPreferencia3);

		Bundle bundle = getIntent().getExtras();
		direccion = bundle.getString("direccion");
		latlng = bundle.getString("latlng");
		Id = bundle.getString("_id");

		txtDireccion.setText(direccion);

		Button btnGuardar = (Button) findViewById(R.id.btnGuardar);
		btnGuardar.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				if(valida()){

					barrio = txtBarrio.getText().toString();
					ref1 = txtRef1.getText().toString();
					ref2 = txtRef2.getText().toString();
					ref3 = txtRef3.getText().toString();

					Registrar(barrio,latlng,direccion,ref1,ref2,ref3,Id);

					System.out.println("Barrio: " + barrio);
					System.out.println("ref1: " + ref1);
					System.out.println("ref2: " + ref2);
					System.out.println("ref3: " + ref3);

//				}else{
//					//campos vacios
//					return;
//				}
			}
		});

	}

	private boolean valida(){
		boolean val = true;
		if(barrio.equals("")){
			val= false;
		}

		int validador = 0;
		if(ref1.equals("")){
			validador+=1;
		}

		if(ref2.equals("")){
			validador+=1;
		}

		if(ref3.equals("")){
			validador+=1;
		}

		if(validador == 3){
			val = false;
		}

		return val;
	}

	private void Registrar(String barrio, String latlng, String direccion, String ref1,String ref2, String ref3,String id) {

		JSONArray json = new JSONArray();
		JSONObject Jo = new JSONObject();
		JSONObject Jo1 = new JSONObject();

		try {
			Jo.put("barrio", barrio);
			Jo.put("latlng", latlng);
			Jo.put("direccion", direccion);
			Jo.put("id", id);

			Jo1.put("ref1", ref1);
			Jo1.put("ref2", ref2);
			Jo1.put("ref3", ref3);
			json.put(Jo1);

			Jo.put("ref", json);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		JSONObjectBody jsonBody = new JSONObjectBody(Jo);

		try {
			String postUrl = getResources().getString(R.string.ServerUrl) + "/api/add/direction";
			AsyncHttpPost post = new AsyncHttpPost(postUrl);
			post.setBody(jsonBody);

			AsyncHttpClient.getDefaultInstance().executeJSONObject(post,new AsyncHttpClient.JSONObjectCallback() {

						@Override
						public void onCompleted(Exception arg0,AsyncHttpResponse arg1, JSONObject argument) {
							// TODO Auto-generated method stub
							try {

								if(argument.getString("value").equals("true")){
									addPreferences("direccion", argument.getString("direccion"));
									finish();
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

	private void addPreferences(String key, String value) {
		SharedPreferences prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(key, value);
		editor.commit();
	}


}


