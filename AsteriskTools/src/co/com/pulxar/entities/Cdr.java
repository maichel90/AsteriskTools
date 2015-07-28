package co.com.pulxar.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the cdr database table.
 * 
 */
@Entity
@NamedQuery(name="Cdr.findAll", query="SELECT c FROM Cdr c")
public class Cdr implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String accountcode;

	private Integer amaflags;

	private Integer billsec;

	private Timestamp calldate;

	private String channel;

	private String clid;

	private String dcontext;

	private String disposition;

	private String dst;

	private String dstchannel;

	private Integer duration;

	private String lastapp;

	private String lastdata;

	private String linkedid;

	private String peeraccount;

	private Integer sequence;

	private String src;

	private String uniqueid;

	private String userfield;

	public Cdr() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAccountcode() {
		return this.accountcode;
	}

	public void setAccountcode(String accountcode) {
		this.accountcode = accountcode;
	}

	public Integer getAmaflags() {
		return this.amaflags;
	}

	public void setAmaflags(Integer amaflags) {
		this.amaflags = amaflags;
	}

	public Integer getBillsec() {
		return this.billsec;
	}

	public void setBillsec(Integer billsec) {
		this.billsec = billsec;
	}

	public Timestamp getCalldate() {
		return this.calldate;
	}

	public void setCalldate(Timestamp calldate) {
		this.calldate = calldate;
	}

	public String getChannel() {
		return this.channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getClid() {
		return this.clid;
	}

	public void setClid(String clid) {
		this.clid = clid;
	}

	public String getDcontext() {
		return this.dcontext;
	}

	public void setDcontext(String dcontext) {
		this.dcontext = dcontext;
	}

	public String getDisposition() {
		return this.disposition;
	}

	public void setDisposition(String disposition) {
		this.disposition = disposition;
	}

	public String getDst() {
		return this.dst;
	}

	public void setDst(String dst) {
		this.dst = dst;
	}

	public String getDstchannel() {
		return this.dstchannel;
	}

	public void setDstchannel(String dstchannel) {
		this.dstchannel = dstchannel;
	}

	public Integer getDuration() {
		return this.duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getLastapp() {
		return this.lastapp;
	}

	public void setLastapp(String lastapp) {
		this.lastapp = lastapp;
	}

	public String getLastdata() {
		return this.lastdata;
	}

	public void setLastdata(String lastdata) {
		this.lastdata = lastdata;
	}

	public String getLinkedid() {
		return this.linkedid;
	}

	public void setLinkedid(String linkedid) {
		this.linkedid = linkedid;
	}

	public String getPeeraccount() {
		return this.peeraccount;
	}

	public void setPeeraccount(String peeraccount) {
		this.peeraccount = peeraccount;
	}

	public Integer getSequence() {
		return this.sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getSrc() {
		return this.src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getUniqueid() {
		return this.uniqueid;
	}

	public void setUniqueid(String uniqueid) {
		this.uniqueid = uniqueid;
	}

	public String getUserfield() {
		return this.userfield;
	}

	public void setUserfield(String userfield) {
		this.userfield = userfield;
	}

}