package colombia.cartagena.moviluv3.Horario;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpPost;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.koushikdutta.async.http.body.JSONObjectBody;

import colombia.cartagena.moviluv3.R;
import colombia.cartagena.moviluv3.DataBase.DbHelper;
import colombia.cartagena.moviluv3.Entidad.Horario;
import android.os.Bundle;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentHorarioEntrada extends Fragment {

	View vista;
	static String idUsuario = "";
	static Context contexto = null;
	static ArrayList<Horario> horario = null;

	RadioBGrupo grupoLunes = null;
	RadioBGrupo grupoMartes = null;
	RadioBGrupo grupoMiercoles = null;
	RadioBGrupo grupoJueves = null;
	RadioBGrupo grupoViernes = null;



	public static FragmentHorarioEntrada newInstance(Bundle arguments,Context ctx) {
		FragmentHorarioEntrada f = new FragmentHorarioEntrada();
		if (arguments != null) {
			f.setArguments(arguments);
		}

		contexto = ctx;
		idUsuario = f.getArguments().getString("_id");
		horario = new ArrayList<Horario>();
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		vista = inflater.inflate(R.layout.fragemet_horario_entrada, container,
				false);

		grupoLunes = new RadioBGrupo(this);
		grupoMartes = new RadioBGrupo(this);
		grupoMiercoles = new RadioBGrupo(this);
		grupoJueves = new RadioBGrupo(this);
		grupoViernes = new RadioBGrupo(this);

		Button r1lu = (Button) vista.findViewById(R.id.bt_lunes1);
		Button r2lu = (Button) vista.findViewById(R.id.bt_lunes2);
		Button r3lu = (Button) vista.findViewById(R.id.bt_lunes3);
		Button r4lu = (Button) vista.findViewById(R.id.bt_lunes4);
		Button r5lu = (Button) vista.findViewById(R.id.bt_lunes5);

		// r1lu.setText("seleccionado");
		// r2lu.setText("0");

		grupoLunes.add((Button) r1lu);
		grupoLunes.add((Button) r2lu);
		grupoLunes.add((Button) r3lu);
		grupoLunes.add((Button) r4lu);
		grupoLunes.add((Button) r5lu);
		grupoLunes.setTags();

		grupoLunes.acciones();

		Button r1ma = (Button) vista.findViewById(R.id.bt_martes1);
		Button r2ma = (Button) vista.findViewById(R.id.bt_martes2);
		Button r3ma = (Button) vista.findViewById(R.id.bt_martes3);
		Button r4ma = (Button) vista.findViewById(R.id.bt_martes4);
		Button r5ma = (Button) vista.findViewById(R.id.bt_martes5);

		grupoMartes.add((Button) r1ma);
		grupoMartes.add((Button) r2ma);
		grupoMartes.add((Button) r3ma);
		grupoMartes.add((Button) r4ma);
		grupoMartes.add((Button) r5ma);
		grupoMartes.setTags();

		grupoMartes.acciones();

		Button r1mi = (Button) vista.findViewById(R.id.bt_miercoles1);
		Button r2mi = (Button) vista.findViewById(R.id.bt_miercoles2);
		Button r3mi = (Button) vista.findViewById(R.id.bt_miercoles3);
		Button r4mi = (Button) vista.findViewById(R.id.bt_miercoles4);
		Button r5mi = (Button) vista.findViewById(R.id.bt_miercoles5);

		grupoMiercoles.add((Button) r1mi);
		grupoMiercoles.add((Button) r2mi);
		grupoMiercoles.add((Button) r3mi);
		grupoMiercoles.add((Button) r4mi);
		grupoMiercoles.add((Button) r5mi);

		grupoMiercoles.setTags();

		grupoMiercoles.acciones();

		Button r1ju = (Button) vista.findViewById(R.id.bt_jueves1);
		Button r2ju = (Button) vista.findViewById(R.id.bt_jueves2);
		Button r3ju = (Button) vista.findViewById(R.id.bt_jueves3);
		Button r4ju = (Button) vista.findViewById(R.id.bt_jueves4);
		Button r5ju = (Button) vista.findViewById(R.id.bt_jueves5);

		grupoJueves.add((Button) r1ju);
		grupoJueves.add((Button) r2ju);
		grupoJueves.add((Button) r3ju);
		grupoJueves.add((Button) r4ju);
		grupoJueves.add((Button) r5ju);

		grupoJueves.setTags();

		grupoJueves.acciones();

		Button r1vi = (Button) vista.findViewById(R.id.bt_viernes1);
		Button r2vi = (Button) vista.findViewById(R.id.bt_viernes2);
		Button r3vi = (Button) vista.findViewById(R.id.bt_viernes3);
		Button r4vi = (Button) vista.findViewById(R.id.bt_viernes4);
		Button r5vi = (Button) vista.findViewById(R.id.bt_viernes5);
		grupoViernes.add((Button) r1vi);
		grupoViernes.add((Button) r2vi);
		grupoViernes.add((Button) r3vi);
		grupoViernes.add((Button) r4vi);
		grupoViernes.add((Button) r5vi);

		grupoViernes.setTags();

		grupoViernes.acciones();

		grupoLunes.setDia("Lunes_");
		grupoMartes.setDia("Martes_");
		grupoMiercoles.setDia("Miercoles_");
		grupoJueves.setDia("Jueves_");
		grupoViernes.setDia("Viernes_");

		DbHelper db = new DbHelper(contexto);
		ArrayList<Horario> registros = db.obtenerRegistros();

		if (registros.size() > 0) {

			cargarSeleccionados(registros, grupoLunes, "Lunes");
			cargarSeleccionados(registros, grupoMartes, "Martes");
			cargarSeleccionados(registros, grupoMiercoles, "Miercoles");
			cargarSeleccionados(registros, grupoJueves, "Jueves");
			cargarSeleccionados(registros, grupoViernes, "Viernes");
		}

		grupoLunes.asignarEstado();
		grupoMartes.asignarEstado();
		grupoMiercoles.asignarEstado();
		grupoJueves.asignarEstado();
		grupoViernes.asignarEstado();

		Button btnEnviar = (Button) vista.findViewById(R.id.btn_enviar);

		btnEnviar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				String sLunes = getSelected(grupoLunes);
				String sMartes = getSelected(grupoMartes);
				String sMiercoles = getSelected(grupoMiercoles);
				String sJueves = getSelected(grupoJueves);
				String sViernes = getSelected(grupoViernes);

				RegistrarHorario(sLunes, sMartes, sMiercoles, sJueves, sViernes);

			}
		});



		return vista;

	}

	private void cargarSeleccionados(ArrayList<Horario> registros, RadioBGrupo grupo, String dia) {
		for (int i = 0; i < registros.size(); i++) {
			String day = registros.get(i).getDia();
			String hora = registros.get(i).getHora();
			if (day.equals(dia)) {

				if (hora.equals("7:00 AM")) {
					grupo.get(0).setText("seleccionado");
				}

				if (hora.equals("9:00 AM")) {
					grupo.get(1).setText("seleccionado");
				}

				if (hora.equals("11:00 AM")) {
					grupo.get(2).setText("seleccionado");
				}

				if (hora.equals("1:00 PM")) {
					grupo.get(3).setText("seleccionado");
				}

				if (hora.equals("3:00 PM")) {
					grupo.get(4).setText("seleccionado");
				}
			}
			grupo.setTags();
		}
	}

	private String getSelected(RadioBGrupo grupos) {
		String selected = "";

		for (int i = 0; i < grupos.size(); i++) {
			int tag = Integer.parseInt(grupos.get(i).getTag().toString());

			if (tag == 2) {

				selected = grupos.getDia() + grupos.getHoraEntrada(i).toString();
				break;
			}
		}
		return selected;
	}



	/*************************************** httpAsync post ********************************************************/

	private void RegistrarHorario(String lunes, String martes,String miercoles, String jueves, String viernes) {

		JSONArray json = new JSONArray();
		JSONObject Jo = new JSONObject();

		try {
			Jo.put("lunes", lunes);
			Jo.put("martes", martes);
			Jo.put("miercoles", miercoles);
			Jo.put("jueves", jueves);
			Jo.put("viernes", viernes);
			Jo.put("tipo", "entrada");
			json.put(Jo);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JSONObjectBody jsonBody = new JSONObjectBody(Jo);

		try {

			String postUrl = getResources().getString(R.string.ServerUrl)+ "/api/add/horario/" + idUsuario;
			AsyncHttpPost post = new AsyncHttpPost(postUrl);
			post.setBody(jsonBody);

			AsyncHttpClient.getDefaultInstance().executeJSONArray(post,new AsyncHttpClient.JSONArrayCallback() {

						@Override
						public void onCompleted(Exception arg0,AsyncHttpResponse arg1, JSONArray jsonArray) {
							// TODO Auto-generated method stub

							try {

								horario.clear();

								for (int i = 0; i < jsonArray.length(); i++) {

									String _idDia = jsonArray.getJSONObject(i).getJSONObject("dia").getString("_id");
									String _idHora = jsonArray.getJSONObject(i).getJSONObject("hora").getString("_id");
									String dia = jsonArray.getJSONObject(i).getJSONObject("dia").getString("titulo");
									String hora = jsonArray.getJSONObject(i).getJSONObject("hora").getString("titulo");

									horario.add(new Horario(dia, hora, _idDia,_idHora));
								}

								DbHelper db = new DbHelper(contexto);
								db.borrar();
								for (int i = 0; i < horario.size(); i++) {
									System.out.println("Dia: " + horario.get(i).getDia());
									db.Insertar(horario.get(i).getDia(),horario.get(i).getHora());
								}

							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		// Orientacion();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

}
