package androidsamples.java.journalapp;

import java.util.List;
import java.util.UUID;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface JournalEntryDao {
    @Insert
    void insert(JournalEntry entry);

    @Query("SELECT * from News_table ORDER BY title ASC")
    LiveData<List<JournalEntry>> getAllEntries();

    @Query("SELECT * from News_table WHERE id=(:id)")
    LiveData<JournalEntry> getEntry(UUID id);

     @Query("DELETE from News_table WHERE source=(:s)")
     void deleteEntries(String s);

    @Query("SELECT * from News_table ORDER BY  title ASC")
    LiveData<List<JournalEntry>> getAllEntriesByTitle();
    @Query("SELECT * from News_table ORDER BY  date ASC")
    LiveData<List<JournalEntry>> getAllEntriesByDate();
    @Query("SELECT * from News_table ORDER BY  visited DESC")
    LiveData<List<JournalEntry>> getAllEntriesByVisited();
    @Query("SELECT * from News_table ORDER BY  favorite DESC")
    LiveData<List<JournalEntry>> getAllEntriesByFavorite();


    @Query("SELECT * from News_table WHERE favorite='true' ORDER BY  title ASC")
    LiveData<List<JournalEntry>> getFavoriteEntriesByTitle();





    @Update
    void update(JournalEntry entry);
}
