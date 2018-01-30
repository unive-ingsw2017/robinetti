package it.unive.milan.roberto.acquaalta.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * Created by Roberto on 25 Jan 2018.
 */

@Entity
public class Forecast {

    // campi
    @PrimaryKey(autoGenerate = true)
    private int id;

    private Date dataPrevisione;
    private Date dataEstremale;
    private String tipoEstremale;
    private Integer valore;

    // getters
    public int getId() {
        return id;
    }
    public Date getDataPrevisione() {
        return dataPrevisione;
    }
    public Date getDataEstremale() {
        return dataEstremale;
    }
    public String getTipoEstremale() {
        return tipoEstremale;
    }
    public Integer getValore() {
        return valore;
    }


    // setters
    public void setId(int id) {
        this.id = id;
    }
    public void setDataPrevisione(Date dataPrevisione) {
        this.dataPrevisione = dataPrevisione;
    }
    public void setDataEstremale(Date dataEstremale) {
        this.dataEstremale = dataEstremale;
    }
    public void setTipoEstremale(String tipoEstremale) {
        this.tipoEstremale = tipoEstremale;
    }
    public void setValore(Integer valore) {
        this.valore = valore;
    }

    // metodi
    @Override
    public String toString(){
        String response = "FORECAST " + id + ":\n";
        response+=("\n\tdataPrevisione: " + getDataPrevisione());
        response+=("\n\tdataEstremale: " + getDataEstremale());
        response+=("\n\ttipoEstremale: " + getTipoEstremale());
        response+=("\n\tvalore: " + getValore());

        return response;
    }

}
