package co.com.pulxar.asterisk.pojo;

import java.io.Serializable;

public class Llamada implements Serializable{

	private static final long serialVersionUID = 1L;
	private String uniqueId;
	private String channel;
	private Integer channelState;
	private String channelStateDesc;
	private Integer callerIdNum;
	private String callerIdName;
	private Integer exten;
	private String uniqueIdDest;
	
	public Llamada() {
		// TODO Auto-generated constructor stub
	}
	
	public String getUniqueId() {
		return uniqueId;
	}
	
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	
	public String getChannel() {
		return channel;
	}
	
	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	public Integer getChannelState() {
		return channelState;
	}
	
	public void setChannelState(Integer channelState) {
		this.channelState = channelState;
	}
	
	public String getChannelStateDesc() {
		return channelStateDesc;
	}
	
	public void setChannelStateDesc(String channelStateDesc) {
		this.channelStateDesc = channelStateDesc;
	}
	
	public Integer getCallerIdNum() {
		return callerIdNum;
	}
	
	public void setCallerIdNum(Integer callerIdNum) {
		this.callerIdNum = callerIdNum;
	}
	
	public String getCallerIdName() {
		return callerIdName;
	}
	
	public void setCallerIdName(String callerIdName) {
		this.callerIdName = callerIdName;
	}
	
	public Integer getExten() {
		return exten;
	}
	
	public void setExten(Integer exten) {
		this.exten = exten;
	}
	
	public String getUniqueIdDest() {
		return uniqueIdDest;
	}
	
	public void setUniqueIdDest(String uniqueIdDest) {
		this.uniqueIdDest = uniqueIdDest;
	}
	
	@Override
	public String toString() {
		return "Llamada [uniqueId=" + uniqueId + ", channel=" + channel
				+ ", channelState=" + channelState + ", channelStateDesc="
				+ channelStateDesc + ", callerIdNum=" + callerIdNum
				+ ", callerIdName=" + callerIdName + ", exten=" + exten
				+ ", uniqueIdDest=" + uniqueIdDest + "]";
	}
}
