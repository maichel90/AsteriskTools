package co.com.pulxar.asterisk;

import java.io.IOException;
import java.io.Serializable;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.SendActionCallback;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.ManagerAction;
import org.asteriskjava.manager.action.SipShowPeerAction;
import org.asteriskjava.manager.event.BridgeEvent;
import org.asteriskjava.manager.event.CoreShowChannelEvent;
import org.asteriskjava.manager.event.HangupEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.NewChannelEvent;
import org.asteriskjava.manager.event.NewStateEvent;
import org.asteriskjava.manager.event.PeerEntryEvent;
import org.asteriskjava.manager.response.ManagerResponse;
import org.asteriskjava.manager.response.SipShowPeerResponse;
import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

import co.com.pulxar.addons.LoadProperties;
import co.com.pulxar.asterisk.pojo.Extension;
import co.com.pulxar.asterisk.pojo.Llamada;

public class AsteriskService implements Serializable, ManagerEventListener, SendActionCallback{

	private static final long serialVersionUID = 1L;
	private Logger logger;
	private LoadProperties properties;
	private ManagerConnectionFactory factory;
	private ManagerConnection managerConnection;
	private TreeMap<Integer, Extension> extensiones;
	private TreeMap<String, Llamada> llamadas;
    
    public  AsteriskService() {
    	properties = new LoadProperties();
    	logger = Logger.getLogger(AsteriskService.class);
    	extensiones = new TreeMap<>();
    	llamadas = new TreeMap<>();
    }   
    
	public TreeMap<Integer, Extension> getExtensiones() {
		return extensiones;
	}

	public TreeMap<String, Llamada> getLlamadas() {
		return llamadas;
	}
	
	@PostConstruct
	public void ConectarAsterisk() {
		try {
    		factory = new ManagerConnectionFactory(properties.getAsteriskhost(),properties.getAsteriskport(),properties.getAsteriskuser(),properties.getAsteriskpass());
			managerConnection = factory.createManagerConnection();
    		managerConnection.addEventListener(this);
			managerConnection.login();
			logger.log(Level.INFO, "..............CONEXION A ASTERISK RELIZADA..............");
		} catch (IllegalArgumentException e) {
			logger.log(Level.ERROR, "<!Conectar-------------------Method has been passed an illegal or inappropriate argument");
		} catch (IllegalStateException e) {
			logger.log(Level.ERROR, "<!Conectar-------------------Method has been invoked at an illegal or inappropriate time");
		} catch (IOException e) {
			logger.log(Level.ERROR, "<!Conectar-------------------Failed or interrupted I/O operations.");
		} catch (TimeoutException e) {
			logger.log(Level.ERROR, "<!Conectar-------------------Timeout expired for connection asterisk server");
		} catch (AuthenticationFailedException e) {
			logger.log(Level.ERROR, "<!Conectar-------------------Autenticacion errada en asterisk");
		}
	}
	
	@PreDestroy
	public void DesconectarAsterisk() {
		try {
			managerConnection.logoff();
			logger.log(Level.INFO, "..............DESCONEXION A ASTERISK RELIZADA..............");
		} catch (Exception e) {
			logger.log(Level.ERROR, "<!Desconectar-------------------Error desconectandose del asterisk");
		}
		
	}
	
	public void sendAction(ManagerAction action){
    	try {
    		managerConnection.sendAction(action,this);
		} catch (IllegalArgumentException e) {
			logger.log(Level.ERROR, "<!SendAction-------------------Method has been passed an illegal or inappropriate argument");
		} catch (IllegalStateException e) {
			logger.log(Level.ERROR, "<!SendAction-------------------Method has been invoked at an illegal or inappropriate time");
		} catch (IOException e) {
			logger.log(Level.ERROR, "<!SendAction-------------------Failed or interrupted I/O operations.");
		} catch (NullPointerException e) {
			logger.log(Level.ERROR, "<!SendAction-------------------NullPointerException pero cierra la conexion");
		}
	}
	
	@Override
	public void onManagerEvent(ManagerEvent event) {
		if(event instanceof PeerEntryEvent){
			PeerEntryEvent entry = (PeerEntryEvent) event;
			SipShowPeerAction action = new SipShowPeerAction(entry.getObjectName());
			sendAction(action);
		}		
		if(event instanceof NewChannelEvent){
			NewChannelEvent entry = (NewChannelEvent) event;
			if(!llamadas.containsKey(entry.getUniqueId())){
				Llamada ll=new Llamada();
				ll.setUniqueId(entry.getUniqueId());
				ll.setChannel(entry.getChannel());
				ll.setChannelState(entry.getChannelState());
				ll.setChannelStateDesc(entry.getChannelStateDesc());
				ll.setCallerIdNum(Integer.parseInt(entry.getCallerIdNum()));
				ll.setCallerIdName(entry.getCallerIdName());
				ll.setExten(Integer.parseInt(entry.getExten()));
				llamadas.put(ll.getUniqueId(), ll);
			}
		}
		if(event instanceof NewStateEvent){
			NewStateEvent entry = (NewStateEvent) event;
			if(llamadas.containsKey(entry.getUniqueId())){
				Llamada ll = llamadas.get(entry.getUniqueId());
				ll.setChannelState(entry.getChannelState());
				ll.setChannelStateDesc(entry.getChannelStateDesc());
				llamadas.put(ll.getUniqueId(), ll);
			}
		}
		if(event instanceof BridgeEvent){
			BridgeEvent entry = (BridgeEvent) event;
			if(llamadas.containsKey(entry.getUniqueId1())){
				Llamada ll = llamadas.get(entry.getUniqueId1());
				ll.setUniqueIdDest(entry.getUniqueId2());
				llamadas.put(ll.getUniqueId(), ll);
			}
			if(llamadas.containsKey(entry.getUniqueId2())){
				Llamada ll = llamadas.get(entry.getUniqueId2());
				ll.setUniqueIdDest(entry.getUniqueId1());
				llamadas.put(ll.getUniqueId(), ll);
			}
		}
		if(event instanceof HangupEvent){
			HangupEvent entry = (HangupEvent) event;
			if(llamadas.containsKey(entry.getUniqueId())){
				llamadas.remove(entry.getUniqueId());
			}
		}
		if(event instanceof CoreShowChannelEvent){
			CoreShowChannelEvent entry = (CoreShowChannelEvent) event;
		}
	}
	
	@Override
	public void onResponse(ManagerResponse response) {
		try{
			if(response instanceof SipShowPeerResponse){
				SipShowPeerResponse resp = (SipShowPeerResponse) response;
				Integer cid =Integer.parseInt(resp.getObjectName());
				Extension e = extensiones.get(cid);
				if(e==null){
					e = new Extension();
					e.setNumCid(Integer.parseInt(resp.getObjectName()));
					e.setContexto(resp.getContext());
					e.setNombreCid(resp.getCallerId());
					e.setTipoCanal(resp.getChannelType());
					e.setEstadoLlamada(0);
				}
				e.setIp(resp.getAddressIp());
				e.setEstado(resp.getStatus());
				extensiones.put(e.getNumCid(),e);
			}
		}catch(Exception e){
		}
	}
}
