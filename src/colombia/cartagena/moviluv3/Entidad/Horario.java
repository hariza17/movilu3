package colombia.cartagena.moviluv3.Entidad;

public class Horario {
	String dia, hora, _idDia, _idHora, _idHorario;

	public Horario() {

	}

	public Horario(String dia, String hora, String _idDia, String _idHora) {
		super();
		this.dia = dia;
		this.hora = hora;
		this._idDia = _idDia;
		this._idHora = _idHora;
	}

	public String get_idHorario() {
		return _idHorario;
	}

	public void set_idHorario(String _idHorario) {
		this._idHorario = _idHorario;
	}

	public String getDia() {
		return dia;
	}

	public void setDia(String dia) {
		this.dia = dia;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String get_idDia() {
		return _idDia;
	}

	public void set_idDia(String _idDia) {
		this._idDia = _idDia;
	}

	public String get_idHora() {
		return _idHora;
	}

	public void set_idHora(String _idHora) {
		this._idHora = _idHora;
	}

}
