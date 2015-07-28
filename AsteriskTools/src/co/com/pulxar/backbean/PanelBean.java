package co.com.pulxar.backbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.asteriskjava.manager.action.CoreShowChannelsAction;
import org.asteriskjava.manager.action.SipPeersAction;

import co.com.pulxar.asterisk.AsteriskService;
import co.com.pulxar.asterisk.pojo.Extension;
import co.com.pulxar.asterisk.pojo.Llamada;

public class PanelBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private AsteriskService asteriskService;
	private TreeMap<String, Llamada> llamadasAnterior;
	private List<Extension> extensiones;
	private List<Llamada> llamadas;
	
	public PanelBean() {
		llamadasAnterior = new TreeMap<>();
	}
	
	public void setAsteriskService(AsteriskService asteriskService) {
		this.asteriskService = asteriskService;
	}

	public AsteriskService getAsteriskService() {
		return asteriskService;
	}

	public List<Llamada> getLlamadas() {
		return llamadas;
	}

	public void setLlamadas(List<Llamada> llamadas) {
		this.llamadas = llamadas;
	}

	public List<Extension> getExtensiones() {
		return extensiones;
	}
	
	public void setExtensiones(List<Extension> extensiones) {
		this.extensiones = extensiones;
	}
	
	public void ConsultarExtensiones() {
		SipPeersAction peers = new SipPeersAction();
		asteriskService.sendAction(peers);
		CoreShowChannelsAction channels = new CoreShowChannelsAction();
		asteriskService.sendAction(channels);
		cruzarLlamadasExtensiones();
	}
	
	private void cruzarLlamadasExtensiones(){
		TreeMap<Integer, Extension> phones = asteriskService.getExtensiones();
		TreeMap<String, Llamada> calls = asteriskService.getLlamadas();
		Iterator<Entry<String, Llamada>> it = calls.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Llamada> e = (Entry<String, Llamada>) it.next();
			Integer id = e.getValue().getCallerIdNum();
			if(phones.containsKey(id)){
				Extension temp = phones.get(id);
				temp.setEstadoLlamada(1);
				phones.put(id, temp);
			}
		}
		extensiones = new ArrayList<>(phones.values());
		buscarElementoBorradoHashMap(phones, calls);
	}
	
	public void buscarElementoBorradoHashMap(TreeMap<Integer, Extension> phones,TreeMap<String, Llamada> actual) {
		Iterator<Entry<String, Llamada>> it = llamadasAnterior.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Llamada> e = (Entry<String, Llamada>) it.next();
			if(!actual.containsKey(e.getKey())){
				System.out.println("+++++++++++++++++++++++++++++++"+e.getValue().getCallerIdNum());
				System.out.println("+++++++++++++++++++++++++++++++"+phones.toString());
				Integer id = e.getValue().getCallerIdNum();
				Extension temp = phones.get(id);
				temp.setEstadoLlamada(0);
				phones.put(id, temp);
			}
		}
		llamadasAnterior.clear();
		llamadasAnterior.putAll(actual);
	}
}
