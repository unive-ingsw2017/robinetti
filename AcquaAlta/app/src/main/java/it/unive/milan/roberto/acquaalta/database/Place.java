package it.unive.milan.roberto.acquaalta.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Roberto on 25 Jan 2018.
 */

@Entity
public class Place {

    // campi
    @PrimaryKey(autoGenerate = true)
    private int id;

    private Integer ordine;
    private String stazione;
    private String nome_abbr;
    private Double latDMSN;
    private Double lonDMSE;
    private Double latDDN;
    private Double lonDDE;
    private Date data;
    private Double valore;

    private Boolean scelto = false;

    // getters
    public int getId() {
        return id;
    }
    public Integer getOrdine() {
        return ordine;
    }
    public String getStazione() {
        return stazione;
    }
    public String getNome_abbr() {
        return nome_abbr;
    }
    public Double getLatDMSN() {
        return latDMSN;
    }
    public Double getLonDMSE() {
        return lonDMSE;
    }
    public Double getLatDDN() {
        return latDDN;
    }
    public Double getLonDDE() {
        return lonDDE;
    }
    public Date getData() {
        return data;
    }
    public Double getValore() {
        return valore;
    }
    public Boolean isScelto() {
        return scelto;
    }

    // setters
    public void setId(int id) {
        this.id = id;
    }
    public void setOrdine(Integer ordine) {
        this.ordine = ordine;
    }
    public void setStazione(String stazione) {
        this.stazione = stazione;
    }
    public void setNome_abbr(String nome_abbr) {
        this.nome_abbr = nome_abbr;
    }
    public void setLatDMSN(Double latDMSN) {
        this.latDMSN = latDMSN;
    }
    public void setLonDMSE(Double lonDMSE) {
        this.lonDMSE = lonDMSE;
    }
    public void setLatDDN(Double latDDN) {
        this.latDDN = latDDN;
    }
    public void setLonDDE(Double lonDDE) {
        this.lonDDE = lonDDE;
    }
    public void setData(Date data) {
        this.data = data;
    }
    public void setValore(Double valore) {
        this.valore = valore;
    }
    public void setScelto(Boolean scelto) {
        this.scelto = scelto;
    }

    // metodi
    public Map<String, Double> getDDCoordinates() {
        Map<String, Double> map = new HashMap<String, Double>();
        map.put("lat", latDDN);
        map.put("lon", lonDDE);
        return map;
    }

    public Map<String, Double> getDMSCoordinates() {
        Map<String, Double> map = new HashMap<String, Double>();
        map.put("lat", latDMSN);
        map.put("lon", lonDMSE);
        return map;
    }

    @Override
    public String toString(){
        String response = "PLACE " + id + ":\n";
        response+=("\n\tordine: " + ordine);
        response+=("\n\tstazione: " + stazione);
        response+=("\n\tnome_abbr: " + nome_abbr);
        response+=("\n\tlatDMSN: " + latDMSN);
        response+=("\n\tlonDMSE: " + lonDMSE);
        response+=("\n\tlatDDN: " + latDDN);
        response+=("\n\tlonDDE: " + lonDDE);
        response+=("\n\tdata: " + data);
        response+=("\n\tvalore: " + valore);

        return response;
    }
}
