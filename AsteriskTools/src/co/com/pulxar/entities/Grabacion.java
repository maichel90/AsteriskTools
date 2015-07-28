package co.com.pulxar.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the grabacion database table.
 * 
 */
@Entity
@NamedQuery(name="Grabacion.findAll", query="SELECT g FROM Grabacion g")
public class Grabacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String archivo;

	private String canal;

	private String destinollamada;

	private Timestamp fecha;

	private String nodo;

	private String numdestino;

	private String numorigen;
	
	private String uniqueid;

	public Grabacion() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getArchivo() {
		return this.archivo;
	}

	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}

	public String getCanal() {
		return this.canal;
	}

	public void setCanal(String canal) {
		this.canal = canal;
	}

	public String getDestinollamada() {
		return this.destinollamada;
	}

	public void setDestinollamada(String destinollamada) {
		this.destinollamada = destinollamada;
	}

	public Timestamp getFecha() {
		return this.fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public String getNodo() {
		return this.nodo;
	}

	public void setNodo(String nodo) {
		this.nodo = nodo;
	}

	public String getNumdestino() {
		return this.numdestino;
	}

	public void setNumdestino(String numdestino) {
		this.numdestino = numdestino;
	}

	public String getNumorigen() {
		return this.numorigen;
	}

	public void setNumorigen(String numorigen) {
		this.numorigen = numorigen;
	}

	public String getUniqueid() {
		return uniqueid;
	}

	public void setUniqueid(String uniqueid) {
		this.uniqueid = uniqueid;
	}

	
}