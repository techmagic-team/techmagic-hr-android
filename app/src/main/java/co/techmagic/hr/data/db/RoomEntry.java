package co.techmagic.hr.data.db;


import co.techmagic.hr.data.entity.Room;
import io.realm.annotations.PrimaryKey;

public class RoomEntry {

    @PrimaryKey
    private String id;
    private String name;

    RoomEntry(final Room room) {
        if (room == null) {
            return;
        }

        id = room.getId();
        name = room.getName();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
