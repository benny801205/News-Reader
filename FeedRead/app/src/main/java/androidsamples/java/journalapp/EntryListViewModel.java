package androidsamples.java.journalapp;

import android.util.Log;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class EntryListViewModel extends ViewModel {
    private final JournalRepository mRepository;

    public EntryListViewModel() {
        mRepository = JournalRepository.getInstance();
    }

    public LiveData<List<JournalEntry>> getAllEntries() {
        return mRepository.getAllEntries();
    }


    public LiveData<List<JournalEntry>> getEntriesByTitle() {
        return mRepository.getEntriesByTitle();
    }


    public LiveData<List<JournalEntry>> getEntriesByDate() {
        return mRepository.getEntriesByDate();
    }

    public LiveData<List<JournalEntry>> getEntriesByVisited() {
        return mRepository.getEntriesByVisited();
    }

    public LiveData<List<JournalEntry>> getEntriesByFavorite() {
        return mRepository.getEntriesByFavorite();
    }

    public LiveData<List<JournalEntry>> getFavoriteEntriesByTitle() {
        return mRepository.getFavoriteEntries();
    }

    void saveEntry(JournalEntry entry) {
        mRepository.update(entry);
    }
}
