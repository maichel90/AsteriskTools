package co.com.pulxar.backbean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.com.pulxar.entities.Cdr;
 
public class CdrBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date fechaInicio;
	private Date fechaFinal;
	private String origenLlamada;
	private String destinoLlamada;
	private Integer duracionLlamada;
	private String opcionDuracionLlamada;
	private List<Cdr> registros;
	@PersistenceContext	EntityManager em;
	
	public CdrBean() {
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

	public Integer getDuracionLlamada() {
		return duracionLlamada;
	}

	public void setDuracionLlamada(Integer duracionLlamada) {
		this.duracionLlamada = duracionLlamada;
	}

	public String getOpcionDuracionLlamada() {
		return opcionDuracionLlamada;
	}

	public void setOpcionDuracionLlamada(String opcionDuracionLlamada) {
		this.opcionDuracionLlamada = opcionDuracionLlamada;
	}
		
	public List<Cdr> getRegistros() {
		return registros;
	}

	public void setRegistros(List<Cdr> registros) {
		this.registros = registros;
	}

	public void RealizarBusqueda() {
		String query = obtenerQuery();
		System.out.println(query);
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
			if(duracionLlamada != null && !duracionLlamada.equals("") ){
				q.setParameter("duracionLlamada", new Float(duracionLlamada));
			}
			registros = q.getResultList();
		}
	}
	
	public String obtenerQuery() {
		String query = new String("select c from Cdr c where calldate between :fechaInicio and :fechaFinal");		
		
		if(origenLlamada != null && !origenLlamada.equals("")){
			query += " and src = :origenLlamada";
		}
		if(destinoLlamada != null && !destinoLlamada.equals("")){
			query += " and dst = :destinoLlamada";
		}
		if(duracionLlamada != null && duracionLlamada != 0){
			if(opcionDuracionLlamada.equals("")){
				query += " and duration <= :duracionLlamada";
			}
			if (opcionDuracionLlamada.equals("minorEq")) {
				query += " and duration <= :duracionLlamada";
			}
			if (opcionDuracionLlamada.equals("equals")) {
				query += " and duration = :duracionLlamada";
			}
			if (opcionDuracionLlamada.equals("majorEq")) {
				query += " and duration >= :duracionLlamada";
			}
		}
		return query;
	}
}
