package colombia.cartagena.moviluv3.Mapa;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import colombia.cartagena.moviluv3.MainActivity;
import colombia.cartagena.moviluv3.R;
import colombia.cartagena.moviluv3.Session.LoginActivity;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpClient.JSONObjectCallback;
import com.koushikdutta.async.http.AsyncHttpPost;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.koushikdutta.async.http.body.MultipartFormDataBody;
import com.koushikdutta.async.http.socketio.Acknowledge;
import com.koushikdutta.async.http.socketio.StringCallback;

public class FragmetMap extends Fragment {

	private MapView MiMapView;
	private GoogleMap mMap;
	private Bundle mBundle;
	Marker melbourne;

	private String direccion, latlng;
	boolean bandera = false;

	private static String nombre;
	private static String IdFile;
	private static String direction;
	private static int SELECT_PICTURE = 1;
	static Context contexto = null;
	static MainActivity mActivity = null;

	View vista;

	public static FragmetMap newInstance(Bundle arguments, MainActivity activity) {
		FragmetMap f = new FragmetMap();
		if (arguments != null) {
			f.setArguments(arguments);
		}

		mActivity = activity;
		contexto = activity.getApplicationContext();
		nombre = f.getArguments().getString("nombre");
		IdFile = f.getArguments().getString("_id");
		direction = f.getArguments().getString("direccion");
		return f;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		vista = inflater.inflate(R.layout.google_map_layout, container, false);

		String dir = getPreferences("direccion");
		if (dir.equals("none")) {
			dir = direction;
		}

		CargarDatos(nombre.toUpperCase(), dir, "INACTIVO");
		subirImagen();

		try {
			MapsInitializer.initialize(getActivity());
			String path = getPreferences("path");
			System.out.println("Path: " + path);
			File file = new File(path);
			if (file.exists()) {
				loadImageFromStorage(path);
			}
		} catch (GooglePlayServicesNotAvailableException e) {
			// TODO handle this situation
		}

		MiMapView = (MapView) vista.findViewById(R.id.mapa);

		MiMapView.onCreate(savedInstanceState);

		setUpMapIfNeeded(MiMapView);

		Button btContinuar = (Button) vista.findViewById(R.id.btn_continuar);

		btContinuar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (bandera == true) {
					Intent i = new Intent(mActivity.getApplicationContext(),DialogRegistro.class);
					i.putExtra("direccion", direccion);
					i.putExtra("latlng", latlng);
					i.putExtra("_id", IdFile);
					startActivity(i);
				} else {
					Toast.makeText(mActivity.getApplicationContext(),
							"Debes acercarte mas...", Toast.LENGTH_LONG).show();

				}
			}
		});

		setRetainInstance(true);

		return vista;

	}

	private void subirImagen() {
		ImageView img = (ImageView) vista.findViewById(R.id.imgPerfil);
		img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				try {

					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
					intent.setType("image/*");
					startActivityForResult(intent, SELECT_PICTURE);

				} catch (Exception ex) {

				}

			}

		});

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == SELECT_PICTURE) {
			try {

				Uri imgUri = data.getData();
				String imgPath = getPath(imgUri);
				System.out.println("IdFile: " + IdFile);

				String id = IdFile;
				new colombia.cartagena.moviluv3.File.HttpUpload(
						mActivity.getApplicationContext(), imgPath).execute(id);

				// Uri selectedImage = data.getData();
				// InputStream is;
				// is =
				// contexto.getContentResolver().openInputStream(selectedImage);
				// BufferedInputStream bis = new BufferedInputStream(is);
				// final Bitmap bitmap = BitmapFactory.decodeStream(bis);
				// ImageView iv = (ImageView)vista.findViewById(R.id.imgPerfil);
				// iv.setImageBitmap(bitmap);
				//
				// System.out.println("Internal storage path: " +
				// selectedImage.getPath());
				//
				// new Thread(new Runnable() {
				// public void run() {
				// //Aquí ejecutamos nuestras tareas costosas
				// String path = saveToInternalSorage(bitmap);
				// addPreferences("path", path);
				//
				// }
				// }).start();

			} catch (Exception e) {
				System.out.println("Exception: " + e.toString());
			}
		}

	}

	// Get the path from Uri
	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = mActivity.managedQuery(uri, projection, null, null,
				null);
		mActivity.startManagingCursor(cursor);
		int column_index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	private String saveToInternalSorage(Bitmap bitmapImage) {

		ContextWrapper cw = new ContextWrapper(contexto);
		// path to /data/data/yourapp/app_data/imageDir
		File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

		// Create imageDir
		File mypath = new File(directory, "profile.jpg");

		FileOutputStream fos = null;
		try {

			fos = new FileOutputStream(mypath);

			// Use the compress method on the BitMap object to write image to
			// the OutputStream
			bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos. ();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String path = directory.getAbsolutePath();

		AsyncHttpPost post = new AsyncHttpPost(getResources().getString(
				R.string.ServerUrl)
				+ "/api/save/image");
		MultipartFormDataBody body = new MultipartFormDataBody();
		body.addFilePart("my-file", new File(path + "profile.jpg"));
		body.addStringPart("foo", "bar");
		post.setBody(body);

		AsyncHttpClient.getDefaultInstance().executeJSONObject(post,
				new JSONObjectCallback() {
					@Override
					public void onCompleted(Exception arg0,
							AsyncHttpResponse arg1, JSONObject arg2) {
						// TODO Auto-generated method stub
						try {
							System.out.println(arg2.getString("mensaje")
									.toString());
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

		return path;
	}

	private void loadImageFromStorage(String path) {

		try {
			File f = new File(path, "profile.jpg");
			Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
			ImageView img = (ImageView) vista.findViewById(R.id.imgPerfil);
			img.setImageBitmap(b);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	private void addPreferences(String key, String value) {
		SharedPreferences prefs = contexto.getSharedPreferences(
				"MisPreferencias", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public String getPreferences(String key) {
		String value = "";
		SharedPreferences prefs = contexto.getSharedPreferences(
				"MisPreferencias", Context.MODE_PRIVATE);

		if (key.equals("path")) {
			value = prefs.getString(key, "none");
		}

		if (key.equals("direccion")) {
			value = prefs.getString(key, "none");
		}

		return value;
	}

	private void CargarDatos(String nombre, String direccion, String plan) {
		TextView lblNombre = (TextView) vista.findViewById(R.id.lblNombre);
		TextView lblDireccion = (TextView) vista
				.findViewById(R.id.lblDireccion);
		TextView lblPlan = (TextView) vista.findViewById(R.id.lblPlan);

		lblNombre.setText(nombre);
		lblDireccion.setText(direccion);
		lblPlan.setText(plan);
	}

	private void setUpMapIfNeeded(View inflatedView) {

		mMap = MiMapView.getMap();
		if (mMap != null) {
			OpcionesMap();
			Log.e("Tag", "Mapa2");
		}
	}

	private void OpcionesMap() {
		LatLng cartagena = new LatLng(10.413534, -75.536512);

//		CameraPosition camPos = new CameraPosition(cartagena, 16, 10, 10);
//
//		CameraUpdate ActualizarCamera = CameraUpdateFactory
//				.newCameraPosition(camPos);
//		mMap.animateCamera(ActualizarCamera);
		mMap.setMyLocationEnabled(true);
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cartagena,16));

		// MapView map = (MapView) vista.findViewById(R.id.mapa);

		// ImageView imgMarker=(ImageView)vista.findViewById(R.id.imageViewId);
		// imgMarker.set

		mMap.setOnCameraChangeListener(new OnCameraChangeListener() {

			@Override
			public void onCameraChange(CameraPosition position) {

				float currentZoom = 20;
				if (position.zoom >= currentZoom) {

					Button bt = (Button) vista
							.findViewById(R.id.googlemaps_select_location);

					VisibleRegion visibleRegion = mMap.getProjection()
							.getVisibleRegion();

					Point x = mMap.getProjection().toScreenLocation(
							visibleRegion.farRight);

					Point y = mMap.getProjection().toScreenLocation(
							visibleRegion.nearLeft);

					Point centerPoint = new Point(x.x / 2, y.y / 2);

					bt.setX(centerPoint.x - (bt.getWidth() / 2));
					bt.setY(centerPoint.y - bt.getHeight());

					LatLng centerFromPoint = mMap.getProjection()
							.fromScreenLocation(centerPoint);

					latlng = centerFromPoint.latitude + ","
							+ centerFromPoint.longitude;

					try {
						Geocoder gcd = new Geocoder(mActivity.getApplicationContext(), Locale.getDefault());

						List<Address> addresses = gcd.getFromLocation(centerFromPoint.latitude,centerFromPoint.longitude, 1);

						if (addresses.size() > 0) {
							System.out.println(addresses.size());
							System.out.println(addresses.get(0).getAddressLine(
									0));
							System.out.println(addresses.get(0).getAddressLine(
									1));
							System.out.println(addresses.get(0).getAddressLine(
									2));

							direccion = addresses.get(0).getAddressLine(0);

							System.out.println("Direccion test1: " + direccion);
						}

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					bandera = true;
					System.out.println("Direccion test: " + direccion);
				} else {
					bandera = false;
					direccion = "";
				}
			}
		});


	}

	// private void CrearMarker(LatLng coordenadas, String titulo, String
	// detalle,
	// int Idimg, String texto) {
	//
	// // pintarNotificacion bitmap = new pintarNotificacion(getResources(),
	// // Idimg,texto);
	// // LatLng center = mMap.getCameraPosition().target;
	//
	// // melbourne = mMap.addMarker(new MarkerOptions().position(center));
	//
	// }

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		if (vista != null) {
			// ViewGroup container = null;
			// Inflater inflater = new Inflater();
			// vista = inflater.inflate(R.layout.google_map_layout, container,
			// false);

			try {
				MapsInitializer.initialize(getActivity());
			} catch (GooglePlayServicesNotAvailableException e) {
				// TODO handle this situation
			}

			MiMapView = (MapView) vista.findViewById(R.id.mapa);

			MiMapView.onCreate(savedInstanceState);

			mMap = MiMapView.getMap();

			mMap.setMyLocationEnabled(true);

			setUpMapIfNeeded(MiMapView);

			Log.e("TAG", "MAPA");
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		if (MiMapView != null)
			MiMapView.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		if (MiMapView != null)
			MiMapView.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (MiMapView != null)
			MiMapView.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		// MiMapView.onLowMemory();
	}

}
