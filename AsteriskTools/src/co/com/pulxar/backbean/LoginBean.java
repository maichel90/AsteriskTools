package co.com.pulxar.backbean;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import co.com.pulxar.addons.Encriptar;
import co.com.pulxar.entities.Usuario;

public class LoginBean implements Serializable{
	
	@Resource UserTransaction ut;
	@PersistenceContext(unitName="AsteriskTools") 
	protected EntityManager em;
	private String username;
	private String password;
	private MenuModel menu;
	private Boolean isLoged = false;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Boolean getIsLoged() {
		return isLoged;
	}	
	public MenuModel getMenu() {
		return menu;
	}
	
	public String AutenticarUsuario() {
		if(username!=null && !username.isEmpty() ){
			if(password != null  && !password.isEmpty() ){
				String passEncriptado = new Encriptar().generate("MD5", password);
				Integer autenticacion = em.createNamedQuery("Usuario.login")
						.setParameter("username", username)
						.setParameter("password", passEncriptado)
						.getResultList().size();
				if(autenticacion == 1){
					isLoged = true;
					CrearMenuUsuario();
					return "Inicio";
				}else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "No se ha podido autenticar en el sistema"));
				}
			}else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "La contrasena no puede ser vacia"));
			}
		}else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "El usuario no puede ser vacio"));
		}
		return null;
			
	}
	
	private void CrearMenuUsuario() {
		Usuario usuarioLogeado = (Usuario) em.createNamedQuery("Usuario.findUsername").setParameter("username", username).getSingleResult();
		
		MenuModel menuPrincipal = new DefaultMenuModel();
		
		DefaultSubMenu grabaciones = new DefaultSubMenu("Grabaciones");
		DefaultSubMenu usuarios = new DefaultSubMenu("Usuarios");
		
		DefaultMenuItem inicio = new DefaultMenuItem("Inicio");
		inicio.setOutcome("Inicio");
		DefaultMenuItem arbol = new DefaultMenuItem("Arbol");
		arbol.setOutcome("GrabacionArbol");
		DefaultMenuItem avanzada = new DefaultMenuItem("Avanzada");
		avanzada.setOutcome("GrabacionBusqueda");
		DefaultMenuItem panel = new DefaultMenuItem("Panel");
		panel.setOutcome("PanelPage");
		DefaultMenuItem cdr = new DefaultMenuItem("Registro de llamadas");
		cdr.setOutcome("CdrPage");
		DefaultMenuItem agregarUsuario = new DefaultMenuItem("Agregar");
		agregarUsuario.setOutcome("CrearUsuario");
		DefaultMenuItem modificarUsuario = new DefaultMenuItem("Modificar");
		modificarUsuario.setOutcome("ModificarUsuario");
		DefaultMenuItem cambiarContrasena = new DefaultMenuItem("Cambiar contrasena");
		cambiarContrasena.setOutcome("RestablecerContrasena");
		DefaultMenuItem cerrarSesion = new DefaultMenuItem("Cerrar sesion");
		cerrarSesion.setCommand("#{loginBean.SalirSistema()}");
		
		menuPrincipal.addElement(inicio);
		if(usuarioLogeado.getPermisoBuscarGrabacionesArbol()){
			grabaciones.addElement(arbol);
		}
		if(usuarioLogeado.getPermisoBuscarGrabacionesAvanzado()){
			grabaciones.addElement(avanzada);
		}
		menuPrincipal.addElement(grabaciones);
		if(usuarioLogeado.getPermisoVisualizarPanel()){
			menuPrincipal.addElement(panel);
		}
		if(usuarioLogeado.getPermisoVisualizarRegistroLlamadas()){
			menuPrincipal.addElement(cdr);
		}
		if(usuarioLogeado.getPermisoAdministracionUsuarios()){
			usuarios.addElement(agregarUsuario);
			usuarios.addElement(modificarUsuario);
		}
		menuPrincipal.addElement(usuarios);
		if (usuarioLogeado.getPermisoRestablecerContrasena()) {
			menuPrincipal.addElement(cambiarContrasena);
		}
		menuPrincipal.addElement(cerrarSesion);
		
		menu =  menuPrincipal;
	}
	
	public String SalirSistema() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		session.invalidate();
		isLoged = false;
		return "/login.xhtml";
	}

}
