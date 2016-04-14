package colombia.cartagena.moviluv3.Entidad;

public class Estudiante {

	String nombre, correo, password, _id, inZone;

	public String getInZone() {
		return inZone;
	}

	public void setInZone(String inZone) {
		this.inZone = inZone;
	}

	public Estudiante(String nombre, String correo, String password, String _id) {
		super();
		this.nombre = nombre;
		this.correo = correo;
		this.password = password;
		this._id = _id;
	}

	public Estudiante() {
		super();
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}



}
