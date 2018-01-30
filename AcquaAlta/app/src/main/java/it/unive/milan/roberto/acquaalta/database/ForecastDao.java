package it.unive.milan.roberto.acquaalta.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by Roberto on 25 Jan 2018.
 */

@Dao
public interface ForecastDao {

    @Query("SELECT * FROM forecast")
    List<Forecast> getAll();

    @Query("SELECT * FROM forecast WHERE id IN (:ids)")
    List<Forecast> loadAllByIds(int[] ids);

    @Query("SELECT * FROM forecast WHERE dataEstremale LIKE :date LIMIT 1")
    Forecast findByDate(Date date);

    @Query("SELECT dataEstremale FROM forecast ORDER BY dataEstremale DESC")
    List<Date> findForecastDates();

    @Query("DELETE FROM forecast")
    void deleteAll();

    @Insert
    void insertAll(Forecast... forecasts);

    @Delete
    void delete(Forecast forecasts);
}
