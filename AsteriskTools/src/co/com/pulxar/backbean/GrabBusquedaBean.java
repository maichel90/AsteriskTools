package co.com.pulxar.backbean;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.model.StreamedContent;

import co.com.pulxar.addons.GsmUtils;
import co.com.pulxar.entities.Grabacion;

@ManagedBean
public class GrabBusquedaBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date fechaInicio;
	private Date fechaFinal;
	private String origenLlamada;
	private String destinoLlamada;
	private String orientacion;
	private String nodo;
	private String canal;
	private StreamedContent archivoGSM;
	private StreamedContent  archivoWAV;
	private List<Grabacion> registros;
	@PersistenceContext	EntityManager em;

	public GrabBusquedaBean() {
		// TODO Auto-generated constructor stub
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	public String getOrigenLlamada() {
		return origenLlamada;
	}

	public void setOrigenLlamada(String origenLlamada) {
		this.origenLlamada = origenLlamada;
	}

	public String getDestinoLlamada() {
		return destinoLlamada;
	}

	public void setDestinoLlamada(String destinoLlamada) {
		this.destinoLlamada = destinoLlamada;
	}

	public String getOrientacion() {
		return orientacion;
	}

	public void setOrientacion(String orientacion) {
		this.orientacion = orientacion;
	}

	public String getNodo() {
		return nodo;
	}

	public void setNodo(String nodo) {
		this.nodo = nodo;
	}

	public String getCanal() {
		return canal;
	}

	public void setCanal(String canal) {
		this.canal = canal;
	}

	public List<Grabacion> getRegistros() {
		return registros;
	}

	public void setRegistros(List<Grabacion> registros) {
		this.registros = registros;
	}

	public StreamedContent getArchivoGSM() {
		return archivoGSM;
	}

	public StreamedContent getArchivoWAV() {
		return archivoWAV;
	}

	public void RealizarBusqueda() {
		String query = obtenerQuery();
		Query q = em.createQuery(query);
		if(fechaInicio != null){
			if(fechaFinal != null){
				q.setParameter("fechaInicio", fechaInicio);
				q.setParameter("fechaFinal", fechaFinal);
			}else {
				Date fechaTemp = (Date) fechaInicio.clone();
				fechaTemp.setHours(23);
				fechaTemp.setMinutes(59);
				q.setParameter("fechaInicio", fechaInicio);
				q.setParameter("fechaFinal", fechaTemp);
			}
			if(origenLlamada != null && !origenLlamada.equals("")){
				q.setParameter("origenLlamada", origenLlamada);
			}
			if(destinoLlamada != null && !destinoLlamada.equals("")){
				q.setParameter("destinoLlamada", destinoLlamada);
			}
			if(orientacion != null && !orientacion.equals("")){
				q.setParameter("orientacion", orientacion);
			}
			if(nodo != null && !nodo.equals("")){
				q.setParameter("nodo", nodo);
			}
			if(canal != null && !canal.equals("")){
				q.setParameter("canal", canal);
			}
			registros = q.getResultList();
		}
	}
	
	public String obtenerQuery() {
		String query = new String("select g from Grabacion g where fecha between :fechaInicio and :fechaFinal");
		
		if(origenLlamada != null && !origenLlamada.equals("")){
			query += " and numorigen = :origenLlamada";
		}
		if(destinoLlamada != null && !destinoLlamada.equals("")){
			query += " and numdestino = :destinoLlamada";
		}
		if(orientacion != null && !orientacion.equals("")){
			query += " and destinoLlamada = :orientacion";
		}
		if(nodo != null && !nodo.equals("")){
			query += " and nodo = :nodo";
		}
		if(canal != null && !canal.equals("")){
			query += " and canal = :canal";
		}
		return query;
	}
	
	public void descargarGrabacionGSM(Grabacion grabacion) throws IOException {
		ObtenerDirectorioGrabacion(grabacion);
		GsmUtils gsm = new GsmUtils(grabacion.getArchivo(),ObtenerDirectorioGrabacion(grabacion));
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
		response.reset();
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition","attachment;filename="+grabacion.getArchivo());
		OutputStream responseOutputStream = response.getOutputStream();	
		FileInputStream archivo = new FileInputStream(gsm.obtenerNombreArchivoEntrada());
		byte[] bytesBuffer = new byte[archivo.available()];
		int bytesRead;
		while ((bytesRead = archivo.read(bytesBuffer)) > 0) {
			responseOutputStream.write(bytesBuffer, 0, bytesRead);
		}
		responseOutputStream.flush();
		archivo.close();
        responseOutputStream.close();
        facesContext.responseComplete();
	}
	public void descargarGrabacionWAV(Grabacion grabacion) throws IOException {
		ObtenerDirectorioGrabacion(grabacion);
		GsmUtils gsm = new GsmUtils(grabacion.getArchivo(),ObtenerDirectorioGrabacion(grabacion));
		gsm.convetirGsmWav();
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
		response.reset();
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition","attachment;filename="+gsm.obtenerNombreWav());
		OutputStream responseOutputStream = response.getOutputStream();	
		FileInputStream archivo = new FileInputStream(gsm.obtenerNombreArchivoSalida());
		byte[] bytesBuffer = new byte[archivo.available()];
		int bytesRead;
		while ((bytesRead = archivo.read(bytesBuffer)) > 0) {
			responseOutputStream.write(bytesBuffer, 0, bytesRead);
		}
		responseOutputStream.flush();
		archivo.close();
        responseOutputStream.close();
        facesContext.responseComplete();
	}
	
	public void escucharGrabacionWAV(Grabacion grabacion) throws IOException {
		HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		GsmUtils gsm = new GsmUtils(grabacion.getArchivo(),ObtenerDirectorioGrabacion(grabacion));
		gsm.convetirGsmWav();
		String URLdest = "http://"+origRequest.getLocalAddr()+"/WAV/"+gsm.obtenerNombreWav();
		FacesContext.getCurrentInstance().getExternalContext().redirect(URLdest);
		return;
	}
	
	public String ObtenerDirectorioGrabacion(Grabacion g) {
		NumberFormat formatter = new DecimalFormat("00");
		String directorios = new String(); 
		Calendar calendar = Calendar.getInstance();
		Timestamp ts = g.getFecha();
		calendar.setTimeInMillis((ts.getTime()));
		int dia = calendar.get(Calendar.DAY_OF_MONTH);
		int mes = calendar.get(Calendar.MONTH)+1;
		int ano =calendar.get(Calendar.YEAR);		
		directorios = ano+"/"+formatter.format(mes)+"/"+formatter.format(dia)+"/";
		System.out.println(directorios);
		return directorios;
	}
}
