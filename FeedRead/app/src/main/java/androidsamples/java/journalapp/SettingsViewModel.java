package androidsamples.java.journalapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class SettingsViewModel extends  ViewModel{
    private final JournalRepository mRepository;


    public SettingsViewModel() {
        this.mRepository = JournalRepository.getInstance();

    }
    public LiveData<List<JournalEntry>> getAllEntries() {
        return mRepository.getAllEntries();
    }

    public void insert(JournalEntry entry) {
        mRepository.insert(entry);
    }

    public void deleteEntries(String s){
        mRepository.deleteEntries(s);
    }






}
