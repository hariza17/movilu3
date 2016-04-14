package colombia.cartagena.moviluv3.Notificacion;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import colombia.cartagena.moviluv3.R;

public class FragmentNotificacion extends Fragment {

	public static FragmentNotificacion newInstance(Bundle arguments) {
		FragmentNotificacion f = new FragmentNotificacion();
		if (arguments != null) {
			f.setArguments(arguments);
		}
		return f;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View vista=inflater.inflate(R.layout.notificacion_layout, container, false);
		
		
		 ListView lista = (ListView) vista.findViewById(R.id.listViewNoti);
	        ArrayList<Notificacion> arraynoti = new ArrayList<Notificacion>();
	        Notificacion noti;
	 
	        // Introduzco los datos
	        Notificacion n1=new Notificacion(1, "25/04/04", "7:00am", "Lo más complicado ya ha pasado. Ahora lo único que..", "nuevo", 1);
	        Notificacion n2=new Notificacion(2, "25/04/04", "7:00am", "Esta es una notificacion  prueba Lo más complicado ya ha pasado. Ahora lo único que..", "nuevo", 2);
	        Notificacion n3=new Notificacion(3, "25/04/04", "7:00am", "Esta es una notifica Lo más complicado ya ha pasado. Ahora lo único que..", "nuevo", 3);
	        Notificacion n4=new Notificacion(4, "25/04/04", "7:00am", "Esta es una notificacion de prueba Lo más complicado ya ha pasado. Ahora lo único que..", "nuevo", 1);
	        Notificacion n5=new Notificacion(5, "25/04/04", "7:00am", "Esta es una notificacion de prueba", "nuevo", 2);
	        Notificacion n6=new Notificacion(6, "25/04/04", "7:00am", "EsLo más complicado ya ha pasado. Ahora lo único que.. notificacion  de prueba", "nuevo", 3);
	        
	        
	        arraynoti.add(n1);
	        arraynoti.add(n2);
	        arraynoti.add(n3);
	        arraynoti.add(n4);
	        arraynoti.add(n5);
	        arraynoti.add(n6);
	        // Creo el adapter personalizado
	        AdapterNotificacion adapter=new AdapterNotificacion(getActivity(), arraynoti);
	 
	        // Lo aplico
	        lista.setAdapter(adapter);
		
		return vista;
	}
}
