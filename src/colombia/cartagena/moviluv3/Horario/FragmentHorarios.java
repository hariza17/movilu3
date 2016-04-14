package colombia.cartagena.moviluv3.Horario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpPost;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.koushikdutta.async.http.body.JSONObjectBody;

import colombia.cartagena.moviluv3.R;
import colombia.cartagena.moviluv3.Entidad.Estudiante;
import colombia.cartagena.moviluv3.Registro.RegistroActivity;
import colombia.cartagena.moviluv3.Session.SessionActivity;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class FragmentHorarios extends Fragment {

	View vista;
	static Bundle bundle = null;
	static Context contexto = null;

	boolean isOk = false;

	public static FragmentHorarios newInstance(Bundle arguments, Context ctx) {
		FragmentHorarios f = new FragmentHorarios();
		if (arguments != null) {
			f.setArguments(arguments);
		}

		contexto = ctx;
		bundle = arguments;
		return f;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		vista = inflater.inflate(R.layout.fragment_horarios, container, false);

		FragmentManager fm = getFragmentManager();
		FragmentTransaction ftra = fm.beginTransaction();

		FragmentHorarioEntrada horarioE = FragmentHorarioEntrada.newInstance(
				bundle, contexto);

		ftra.add(vista.findViewById(R.id.contenedor_principal).getId(),
				horarioE);

		ftra.commit();

		Button btnHentrada = (Button) vista.findViewById(R.id.bt_entrada);
		btnHentrada.setActivated(true);

		setRetainInstance(true);

		LinearLayout layout = (LinearLayout) vista.findViewById(R.id.contenedorGeneral);

//		if (isOk == true) {
//			for (int i = 0; i < container.getChildCount(); i++) {
//				View vie = container.getChildAt(i);
//				vie.setEnabled(true);
//			}
//		} else {
//			for (int i = 0; i < container.getChildCount(); i++) {
//				View vie = container.getChildAt(i);
//				vie.setEnabled(false);
//
//			}
//			Toast.makeText(getActivity().getApplicationContext(),
//					"HOlalalalalal", 5000).show();
//		}




		return vista;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
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
