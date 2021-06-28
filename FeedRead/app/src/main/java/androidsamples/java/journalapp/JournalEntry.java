package androidsamples.java.journalapp;

import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "News_table")
public class JournalEntry {
    @PrimaryKey
    @ColumnInfo(name = "id")
    @NonNull
    private UUID mUid;

    @ColumnInfo(name = "title")
    private String mTitle;

    @ColumnInfo(name = "date")
    private String mDate;

    @ColumnInfo(name = "link")
    private String mLink;


    @ColumnInfo(name = "favorite")
    private String mFavorite;

    @ColumnInfo(name = "visited")
    private String mVisited;

    @ColumnInfo(name = "source")
    private String mSource;




    public JournalEntry(@NonNull String title, String date,String link, String favorite,String visited, String source) {
        mUid = UUID.randomUUID();
        mTitle = title;
        mDate = date;
        mFavorite = favorite;
        mVisited = visited;
        mSource = source;
        mLink = link;
    }

    @NonNull
    public UUID getUid() {
        return mUid;
    }

    public void setUid(@NonNull UUID id) {
        mUid = id;
    }

    @NonNull
    public String title() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String date() {
        return mDate;
    }

    public void setDate(String date) { mDate = date; }


    @NonNull
    public String link() {
        return mLink;
    }

    public void setLink(String link) {
        mLink = link;
    }

    public String favorite() {
        return mFavorite;
    }

    public void setFavorate(String favorite) {
        mFavorite = favorite;
    }

    public String visited() {
        return mVisited;
    }

    public void setVisited(String visited) {
        mVisited = visited;
    }

    public String source() {
        return mSource;
    }

    public void setmSource(String source) {
        mSource = source;
    }




}
