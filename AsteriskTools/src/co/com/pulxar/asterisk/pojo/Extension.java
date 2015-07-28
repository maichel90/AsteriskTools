package co.com.pulxar.asterisk.pojo;

import java.io.Serializable;

public class Extension implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Integer numCid;
	private String contexto;
	private String tipoCanal;
	private String nombreCid;
	private String ip;
	private String estado;
	private Integer estadoLlamada;
	
	public Extension() {
		// TODO Auto-generated constructor stub
	}
	
	public Integer getNumCid() {
		return numCid;
	}
	
	public void setNumCid(Integer numCid) {
		this.numCid = numCid;
	}
	
	public String getContexto() {
		return contexto;
	}
	
	public void setContexto(String contexto) {
		this.contexto = contexto;
	}
	
	public String getTipoCanal() {
		return tipoCanal;
	}
	
	public void setTipoCanal(String tipoCanal) {
		this.tipoCanal = tipoCanal;
	}
	
	public String getNombreCid() {
		return nombreCid;
	}
	
	public void setNombreCid(String nombreCid) {
		this.nombreCid = nombreCid;
	}
	
	public String getIp() {
		return ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public String getEstado() {
		return estado;
	}
	
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public Integer getEstadoLlamada() {
		return estadoLlamada;
	}
	
	public void setEstadoLlamada(Integer estadoLlamada) {
		this.estadoLlamada = estadoLlamada;
	}

	@Override
	public String toString() {
		return "Extension [numCid=" + numCid + ", contexto=" + contexto
				+ ", tipoCanal=" + tipoCanal + ", nombreCid=" + nombreCid
				+ ", ip=" + ip + ", estado=" + estado + ", estadoLlamada="
				+ estadoLlamada + "]";
	}
}
