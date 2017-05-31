package model;

import java.sql.Timestamp;

public class Picture {
	private String pid = "";
	private String padd = "";
	private String pname = "";
	private Timestamp puptime;
	private String pmodpeo = "";
	private Timestamp pmodtime = Timestamp.valueOf("1970-01-02 00:00:00");
	private String pcheckpeo = "";
	private Timestamp pchecktime = Timestamp.valueOf("1970-01-02 00:00:00");
	private String pok = "";
	private Timestamp pdeadline = Timestamp.valueOf("1970-01-02 00:00:00");
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getPadd() {
		return padd;
	}
	public void setPadd(String padd) {
		this.padd = padd;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public Timestamp getPuptime() {
		return puptime;
	}
	public void setPuptime(Timestamp puptime) {
		this.puptime = puptime;
	}
	public String getPmodpeo() {
		return pmodpeo;
	}
	public void setPmodpeo(String pmodpeo) {
		this.pmodpeo = pmodpeo;
	}
	public Timestamp getPmodtime() {
		return pmodtime;
	}
	public void setPmodtime(Timestamp pmodtime) {
		this.pmodtime = pmodtime;
	}
	public String getPcheckpeo() {
		return pcheckpeo;
	}
	public void setPcheckpeo(String pcheckpeo) {
		this.pcheckpeo = pcheckpeo;
	}
	public Timestamp getPchecktime() {
		return pchecktime;
	}
	public void setPchecktime(Timestamp pchecktime) {
		this.pchecktime = pchecktime;
	}
	public String getPok() {
		return pok;
	}
	public void setPok(String pok) {
		this.pok = pok;
	}
	public Timestamp getPdeadline() {
		return pdeadline;
	}
	public void setPdeadline(Timestamp pdeadline) {
		this.pdeadline = pdeadline;
	}
	
}
