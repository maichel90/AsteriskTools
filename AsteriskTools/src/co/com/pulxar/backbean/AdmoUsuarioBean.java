package co.com.pulxar.backbean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.primefaces.event.RowEditEvent;

import co.com.pulxar.addons.Encriptar;
import co.com.pulxar.entities.Usuario;

public class AdmoUsuarioBean implements Serializable {

	@Resource UserTransaction ut;
	@PersistenceContext(unitName="AsteriskTools") 
	protected EntityManager em;
	
	private String nombreUsuario;
	private String apellidoUsuario;
	private String username;
	private String cUsername;
	private String contrasena;
	private String cContrasena;
	private String nuevaContrasena;
	private String cNuevaContrasena;
	
	private Boolean permisoBuscarGrabacionesArbol;
	private Boolean permisoBuscarGrabacionesAvanzado;
	private Boolean permisoVisualizarPanel;
	private Boolean permisoVisualizarRegistroLlamadas;
	private Boolean permisoAdministracionUsuarios;
	private Boolean permisoRestablecerContrasena;
	
	private List<Usuario> usuarios;
	
	
	public String getNombreUsuario() {
		return nombreUsuario;
	}
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}
	public String getApellidoUsuario() {
		return apellidoUsuario;
	}
	public void setApellidoUsuario(String apellidoUsuario) {
		this.apellidoUsuario = apellidoUsuario;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getcUsername() {
		return cUsername;
	}
	public void setcUsername(String cUsername) {
		this.cUsername = cUsername;
	}
	public String getContrasena() {
		return contrasena;
	}
	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}
	public String getcContrasena() {
		return cContrasena;
	}
	public void setcContrasena(String cContrasena) {
		this.cContrasena = cContrasena;
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
	public String getNuevaContrasena() {
		return nuevaContrasena;
	}
	public void setNuevaContrasena(String nuevaContrasena) {
		this.nuevaContrasena = nuevaContrasena;
	}
	public String getcNuevaContrasena() {
		return cNuevaContrasena;
	}
	public void setcNuevaContrasena(String cNuevaContrasena) {
		this.cNuevaContrasena = cNuevaContrasena;
	}
	public List<Usuario> getUsuarios() {
		return usuarios;
	}
	
	public void AgregarNuevoUsuario() {
		if(username.equals(cUsername)){
			if(contrasena.equals(cContrasena)){
				Integer cantidadIguales = em.createNamedQuery("Usuario.findUsername").setParameter("username",username).getResultList().size();
				System.out.println("++++++++++++++++++++++"+cantidadIguales);
				if(cantidadIguales == 0 ){
					Usuario nuevoUsuario = new Usuario();
					String passEncriptado = new Encriptar().generate("MD5", contrasena);
					
					nuevoUsuario.setNombre(nombreUsuario);
					nuevoUsuario.setApellido(apellidoUsuario);
					nuevoUsuario.setUsuario(username);
					nuevoUsuario.setContrasena(passEncriptado);
					nuevoUsuario.setPermisoAdministracionUsuarios(permisoAdministracionUsuarios);
					nuevoUsuario.setPermisoBuscarGrabacionesArbol(permisoBuscarGrabacionesArbol);
					nuevoUsuario.setPermisoBuscarGrabacionesAvanzado(permisoBuscarGrabacionesAvanzado);
					nuevoUsuario.setPermisoRestablecerContrasena(permisoRestablecerContrasena);
					nuevoUsuario.setPermisoVisualizarPanel(permisoVisualizarPanel);
					nuevoUsuario.setPermisoVisualizarRegistroLlamadas(permisoVisualizarRegistroLlamadas);
					try {
						ut.begin();
						em.persist(nuevoUsuario);
						ut.commit();
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "INFO", "Se creo satisfactoriamente el usuario"));
					} catch (Exception e) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "FATAL", "Se genero un error al momento de persistir en la base de datos"));
						e.printStackTrace();
					} 					
				}else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "El usuario que esta intentando crear ya existe en el sistema"));
				}
			}else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Verifique que la contrasena que esta ingresando sea igual"));
			}
		}else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Verifique que el usuario que esta ingresando sea igual"));
		}
	}
	
	public void RestablecerContrasena(String nombreUsusario) {
		 if(nuevaContrasena.equals(cNuevaContrasena)){
			 Usuario usuarioCambio = (Usuario) em.createNamedQuery("Usuario.findUsername").setParameter("username",nombreUsusario).getSingleResult();
			 String passEncriptado = new Encriptar().generate("MD5", contrasena);
			 if(passEncriptado.equals(usuarioCambio.getContrasena())){
				 String passNuevoEncriptado = new Encriptar().generate("MD5", nuevaContrasena);
				 usuarioCambio.setContrasena(passNuevoEncriptado);
				try {
					ut.begin();
					em.merge(usuarioCambio);
					ut.commit();
					FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"INFO","Se realizo el cambio de clave satisfactoriamente"));
				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_FATAL,"FATAL","Se genero un error al momento de persistir en la base de datos"));
					e.printStackTrace();
				}
			 }else {
				 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Verifique la contrasena actual"));
			}
		 }else {
			 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Verifique que la nueva contrasena este igual en los dos campos"));
		}
	}
	
	@PostConstruct
	public void ConsultarUsuarios() {
		usuarios = em.createNamedQuery("Usuario.findAll").getResultList();
	}
	
	public void onRowEdit(RowEditEvent event) {
		
		Usuario usuarioEditado = (Usuario) event.getObject();		
		try {
			ut.begin();
			em.merge(usuarioEditado);
			ut.commit();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "INFO", "Se edito el usuario satisfactoriamente"));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "FATAL", "Se genero un error al momento de persistir en la base de datos"));
			e.printStackTrace();
		}
	}
}
