package colombia.cartagena.moviluv3;

import colombia.cartagena.moviluv3.Registro.RegistroActivity;
import colombia.cartagena.moviluv3.Session.LoginActivity;
import colombia.cartagena.moviluv3.Session.SessionActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class InicialActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inicial);


		Button btnEntrar = (Button) findViewById(R.id.btnEntrar);
		Button btnRegistrar = (Button) findViewById(R.id.btnRegistrar);

		btnEntrar.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub



				SharedPreferences prefs = getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);
				if(prefs.getString("loged", "No").equals("Ok")){
					Intent intent = new Intent(InicialActivity.this,MainActivity.class);
					intent.putExtra("_id", prefs.getString("_id", "none"));
					intent.putExtra("nombre", prefs.getString("nombre", "none"));
					startActivity(intent);
				}else{
					Intent intent = new Intent(InicialActivity.this,SessionActivity.class);
					startActivity(intent);
				}
			}

		});

		btnRegistrar.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(InicialActivity.this,RegistroActivity.class);
				startActivity(intent);
			}

		});
	}

}
