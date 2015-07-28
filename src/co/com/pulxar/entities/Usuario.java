package co.com.pulxar.entities;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the Usuario database table.
 * 
 */
@Entity

@NamedQueries({
	@NamedQuery(name="Usuario.findAll", query="SELECT u FROM Usuario u"),
	@NamedQuery(name="Usuario.findUsername", query="SELECT u FROM Usuario u where u.usuario = :username"),
	@NamedQuery(name="Usuario.login", query="SELECT u FROM Usuario u where u.usuario = :username and u.contrasena = :password" )
})

public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idUsuario;

	private String apellido;

	private String contrasena;

	private String nombre;

	private String usuario;
	
	private Boolean permisoBuscarGrabacionesArbol;
	private Boolean permisoBuscarGrabacionesAvanzado;
	private Boolean permisoVisualizarPanel;
	private Boolean permisoVisualizarRegistroLlamadas;
	private Boolean permisoAdministracionUsuarios;
	private Boolean permisoRestablecerContrasena;

	public Usuario() {
	}

	public int getIdUsuario() {
		return this.idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getApellido() {
		return this.apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getContrasena() {
		return this.contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Boolean getPermisoBuscarGrabacionesArbol() {
		return permisoBuscarGrabacionesArbol;
	}

	public void setPermisoBuscarGrabacionesArbol(
			Boolean permisoBuscarGrabacionesArbol) {
		this.permisoBuscarGrabacionesArbol = permisoBuscarGrabacionesArbol;
	}

	public Boolean getPermisoBuscarGrabacionesAvanzado() {
		return permisoBuscarGrabacionesAvanzado;
	}

	public void setPermisoBuscarGrabacionesAvanzado(
			Boolean permisoBuscarGrabacionesAvanzado) {
		this.permisoBuscarGrabacionesAvanzado = permisoBuscarGrabacionesAvanzado;
	}

	public Boolean getPermisoVisualizarPanel() {
		return permisoVisualizarPanel;
	}

	public void setPermisoVisualizarPanel(Boolean permisoVisualizarPanel) {
		this.permisoVisualizarPanel = permisoVisualizarPanel;
	}

	public Boolean getPermisoVisualizarRegistroLlamadas() {
		return permisoVisualizarRegistroLlamadas;
	}

	public void setPermisoVisualizarRegistroLlamadas(
			Boolean permisoVisualizarRegistroLlamadas) {
		this.permisoVisualizarRegistroLlamadas = permisoVisualizarRegistroLlamadas;
	}

	public Boolean getPermisoAdministracionUsuarios() {
		return permisoAdministracionUsuarios;
	}

	public void setPermisoAdministracionUsuarios(
			Boolean permisoAdministracionUsuarios) {
		this.permisoAdministracionUsuarios = permisoAdministracionUsuarios;
	}

	public Boolean getPermisoRestablecerContrasena() {
		return permisoRestablecerContrasena;
	}

	public void setPermisoRestablecerContrasena(Boolean permisoRestablecerContrasena) {
		this.permisoRestablecerContrasena = permisoRestablecerContrasena;
	}

}