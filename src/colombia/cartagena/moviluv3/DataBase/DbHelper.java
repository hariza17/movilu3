package colombia.cartagena.moviluv3.DataBase;

import java.util.ArrayList;
import java.util.List;

import colombia.cartagena.moviluv3.Entidad.Horario;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;

public class DbHelper extends SQLiteOpenHelper {

	public DbHelper(Context context) {
		super(context, "MiDataBase", null, 8);
		// TODO Auto-generated constructor stub
	}

	public void Abrir() {
		this.getWritableDatabase();
	}

	public void Cerrar() {
		this.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String Query = "CREATE TABLE horario(" + _ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "dia TEXT, hora text); ";

		db.execSQL(Query);

		String Query2 = "CREATE TABLE horarioSalida(" + _ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "dia TEXT, hora text); ";

		db.execSQL(Query2);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String Query = "DROP TABLE IF EXISTS horario";
		db.execSQL(Query);

		String Query2 = "DROP TABLE IF EXISTS horarioSalida";
		db.execSQL(Query2);

		onCreate(db);
	}

	public boolean Insertar(String dia, String hora) {
		boolean result = false;
		try {

			if (!dia.isEmpty() || !hora.isEmpty()) {

				Abrir();
				ContentValues registro = new ContentValues();
				registro.put("dia", dia);
				registro.put("hora", hora);
				this.getWritableDatabase().insert("horario", null, registro);
				Cerrar();
				result = true;
			} else {
				return false;
			}

		} catch (Exception ex) {
			result = false;
		}

		return result;
	}

	public boolean InsertarHorarioSalida(String dia, String hora) {
		boolean result = false;
		try {

			if (!dia.isEmpty() || !hora.isEmpty()) {

				Abrir();
				ContentValues registro = new ContentValues();
				registro.put("dia", dia);
				registro.put("hora", hora);
				this.getWritableDatabase().insert("horarioSalida", null, registro);
				Cerrar();
				result = true;
			} else {
				return false;
			}

		} catch (Exception ex) {
			result = false;
		}

		return result;
	}

	public void borrar() {
		try {
			Abrir();
			SQLiteDatabase db = getWritableDatabase();
			db.delete("horario", null, null);
			db.close();
			Cerrar();
		} catch (Exception ex) {
			//escribir en un log
		}

	}

	public void borrarHorarioSalida() {
		try {
			Abrir();
			SQLiteDatabase db = getWritableDatabase();
			db.delete("horarioSalida", null, null);
			db.close();
			Cerrar();
		} catch (Exception ex) {
			//escribir en un log
		}

	}

	public void borrarPorId(int id) {
		try{


		Abrir();
		SQLiteDatabase db = getWritableDatabase();
		db.delete("horario", "_id=" + id, null);
		Cerrar();

		}catch(Exception ex){
			//escribir en un log
		}
	}

	public void modificar(Horario horario) {
		try {

			Abrir();
			SQLiteDatabase db = getWritableDatabase();
			ContentValues valores = new ContentValues();
			valores.put("_id", horario.get_idHorario());
			valores.put("dia", horario.getDia());
			valores.put("hora", horario.getHora());
			db.update("horario", valores, "_id=" + horario.get_idHorario(),null);
			Cerrar();

		} catch (Exception ex) {
			//escribir en un log
		}

	}

	public ArrayList<Horario> obtenerRegistros() {
		int colId, colDia, colHora;
		ArrayList<Horario> horarios = new ArrayList<Horario>();
		try{

		Abrir();
		String columnas[] = { _ID, "dia", "hora" };
		Cursor c = this.getReadableDatabase().query("horario", columnas, null,null, null, null, null);

		colId = c.getColumnIndex(_ID);
		colDia = c.getColumnIndex("dia");
		colHora = c.getColumnIndex("hora");

		if (c.getCount() > 0) {
			c.moveToFirst();

			do {
				Horario horario = new Horario();
				horario.set_idHorario(c.getString(colId));
				horario.setDia(c.getString(colDia));
				horario.setHora(c.getString(colHora));

				horarios.add(horario);
			} while (c.moveToNext());
		}

		Cerrar();

		}catch(Exception ex){
			//escribir en un log
		}

		return horarios;
	}

	public ArrayList<Horario> obtenerRegistrosHorarioSalida() {
		int colId, colDia, colHora;
		ArrayList<Horario> horarios = new ArrayList<Horario>();
		try{

		Abrir();
		String columnas[] = { _ID, "dia", "hora" };
		Cursor c = this.getReadableDatabase().query("horarioSalida", columnas, null,null, null, null, null);

		colId = c.getColumnIndex(_ID);
		colDia = c.getColumnIndex("dia");
		colHora = c.getColumnIndex("hora");

		if (c.getCount() > 0) {
			c.moveToFirst();

			do {
				Horario horario = new Horario();
				horario.set_idHorario(c.getString(colId));
				horario.setDia(c.getString(colDia));
				horario.setHora(c.getString(colHora));

				horarios.add(horario);
			} while (c.moveToNext());
		}

		Cerrar();

		}catch(Exception ex){
			//escribir en un log
		}

		return horarios;
	}

}
