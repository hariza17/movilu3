package colombia.cartagena.moviluv3.Notificacion;

import java.util.ArrayList;

import colombia.cartagena.moviluv3.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdapterNotificacion extends BaseAdapter {

	private Activity activity;
	private ArrayList<Notificacion> listaNoti;

	public AdapterNotificacion(Activity activity, ArrayList lista) {
		this.activity = activity;
		this.listaNoti = lista;

	}

	@Override
	public int getCount() {

		return listaNoti.size();
	}

	@Override
	public Object getItem(int arg0) {

		return listaNoti.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return listaNoti.get(arg0).getId();
	}

	@Override
	public View getView(int posicion, View vista, ViewGroup vGrup) {
		View v = vista;

		if (vista == null) {
			LayoutInflater inf = (LayoutInflater) this.activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inf.inflate(R.layout.item_notificacion, null);
		}

		Notificacion noti = listaNoti.get(posicion);

		ImageView icon = (ImageView) v.findViewById(R.id.icon_noti);

		LinearLayout layout = (LinearLayout) v.findViewById(R.id.noti_tipo);
		noti.setIcon_noti(this.activity, layout);
		icon.setImageDrawable(noti.getIcon_noti());

		TextView fecha = (TextView) v.findViewById(R.id.noti_txt_fecha);
		fecha.setText(noti.getFecha());

		TextView hora = (TextView) v.findViewById(R.id.noti_txt_hora);
		hora.setText(noti.getHora());

		TextView contenido = (TextView) v.findViewById(R.id.noti_txt_contenido);
		contenido.setText(noti.getContenido());

		TextView tag = (TextView) v.findViewById(R.id.noti_txt_tag);
		tag.setText(noti.getTag());

		// Retornamos la vista
		return v;

	}

}
