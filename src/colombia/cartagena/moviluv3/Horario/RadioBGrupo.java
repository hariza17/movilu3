package colombia.cartagena.moviluv3.Horario;

import java.util.ArrayList;

import colombia.cartagena.moviluv3.R;

import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RadioBGrupo extends ArrayList<Button> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String dia;


	Resources res = null;

	public RadioBGrupo(Fragment f) {
		res = f.getResources();
	}

	public String getDia() {
		return dia;
	}

	public void setDia(String dia) {
		this.dia = dia;
	}

	ArrayList<String> listadoEntrada = new ArrayList<String>();
	ArrayList<String> listadoSalida = new ArrayList<String>();

	public String getHoraEntrada(int posicion){

		listadoEntrada.add("7:00 AM");
		listadoEntrada.add("9:00 AM");
		listadoEntrada.add("11:00 AM");
		listadoEntrada.add("1:00 PM");
		listadoEntrada.add("3:00 PM");

		return listadoEntrada.get(posicion);
	}

	public String getHoraSalida(int posicion){
		listadoSalida.add("11:00 AM");
		listadoSalida.add("1:00 PM");
		listadoSalida.add("3:00 PM");
		listadoSalida.add("5:00 PM");
		listadoSalida.add("6:00 PM");
		listadoSalida.add("7:00 PM");

		return listadoSalida.get(posicion);
	}

	public void acciones() {

		for (int i = 0; i < size(); i++) {

			get(i).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Button r = (Button) v;

					for (int j = 0; j < size(); j++) {
						if ((Integer) get(j).getTag() != 3) {
							get(j).setTag(1);
						}

					}

					r.setTag(2);
					asignarEstado();
				}

			});
		}

	}

	public void setTags() {
		for (int i = 0; i < size(); i++) {

			if (get(i).getText().equals("0")) {
				get(i).setTag(3);
			} else {
				if (get(i).getText().equals("seleccionado")) {
					get(i).setTag(2);
				}else{
					get(i).setTag(1);
					get(i).setText("12");
				}

			}
		}
	}

	public void asignarEstado() {
		for (int i = 0; i < size(); i++) {
			int tag = (Integer) get(i).getTag();

			switch (tag) {
			case 1:
				// estado natural
				get(i).setBackgroundDrawable(res.getDrawable(R.drawable.bt_normal));
				get(i).setTextSize(12);
				get(i).setTextColor(Color.WHITE);
				get(i).setEnabled(true);
				get(i).setActivated(false);
				break;

			case 2:

				// seleccionado
				get(i).setBackgroundDrawable(res.getDrawable(R.drawable.bt_normal));
				get(i).setTextSize(1);
				get(i).setEnabled(true);
				get(i).setActivated(true);

				break;

			case 3:
				// esta en cero
				get(i).setBackgroundDrawable(res.getDrawable(R.drawable.bt_normal));
				get(i).setEnabled(false);
				get(i).setActivated(false);
				get(i).setTextColor(Color.WHITE);
				get(i).setTextSize(12);
				break;

			default:
				get(i).setBackgroundColor(Color.rgb(255, 255, 255));
				break;

			}
		}
	}
}
