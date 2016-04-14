package colombia.cartagena.moviluv3.Notificacion;

import colombia.cartagena.moviluv3.R;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;

public class Notificacion {

	private int id;
	private Drawable icon_noti;
	private String fecha;
	private String hora;
	private String contenido;
	private String tag;

	private int tipo;

	public Notificacion(int id, String fecha, String hora, String contenido,
			String tag, int tipo) {
		this.id = id;
		this.fecha = fecha;
		this.hora = hora;
		this.contenido = contenido;
		this.tag = tag;
		this.tipo = tipo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Drawable getIcon_noti() {
		return icon_noti;
	}

	public void setIcon_noti(Activity f,View v) {

		switch (tipo) {
		case 1:
			icon_noti = f.getResources().getDrawable(R.drawable.ic_notify);
			v.setBackgroundColor(Color.rgb(0, 160, 198));
			break;
		case 2:
			icon_noti = f.getResources().getDrawable(R.drawable.ic_notify);
			v.setBackgroundColor(Color.rgb(223, 0, 36));
			break;

		case 3:
			icon_noti = f.getResources().getDrawable(R.drawable.ic_notify);
			v.setBackgroundColor(Color.rgb(255, 210, 0));
			break;

		default:
			icon_noti = f.getResources().getDrawable(R.drawable.ic_notify);
			v.setBackgroundColor(Color.rgb(0, 160, 198));
			break;
		}

	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

}
