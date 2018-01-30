package it.unive.milan.roberto.acquaalta.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Roberto on 25 Jan 2018.
 */

@Dao
public interface PlaceDao {

    @Query("SELECT * FROM place")
    List<Place> getAll();

    @Query("SELECT * FROM place WHERE id IN (:ids)")
    List<Place> loadAllByIds(int[] ids);

    @Query("SELECT * FROM place WHERE stazione LIKE :name LIMIT 1")
    Place findByName(String name);

    @Query("SELECT * FROM place WHERE scelto ORDER BY stazione ASC")
    List<Place> getChosenPlaces();

    @Query("SELECT * FROM place WHERE NOT(scelto) ORDER BY stazione ASC")
    List<Place> getNotChosenPlaces();

    @Query("UPDATE place SET scelto = 1 WHERE stazione = :stazione")
    void setChosenPlace(String stazione);

    @Query("UPDATE place SET scelto = 0 WHERE stazione = :stazione")
    void setNotChosenPlace(String stazione);

    @Query("SELECT * FROM place WHERE stazione = :stazione LIMIT 1")
    Place getPlace(String stazione);

    @Query("DELETE FROM place")
    public void deleteAll();

    @Insert
    void insertAll(Place... places);

    @Delete
    void delete(Place places);


}
