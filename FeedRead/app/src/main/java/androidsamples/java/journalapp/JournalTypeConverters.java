package androidsamples.java.journalapp;

import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;

public class JournalTypeConverters {
    @TypeConverter
    public UUID toUUID(@NonNull String uuid) {
        return UUID.fromString(uuid);
    }

    @TypeConverter
    public String fromUUID(@NonNull UUID uuid) {
        return uuid.toString();
    }
}
