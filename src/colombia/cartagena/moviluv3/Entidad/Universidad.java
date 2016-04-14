package colombia.cartagena.moviluv3.Entidad;

public class Universidad {

	String id,nombre,direccion,location;

	public Universidad(){

	}

	public Universidad(String id, String nombre, String direccion,
			String location) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.direccion = direccion;
		this.location = location;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
