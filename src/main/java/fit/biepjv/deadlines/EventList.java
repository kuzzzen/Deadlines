package fit.biepjv.deadlines;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EventList extends SimpleListProperty<Event> {

    public EventList() {
        super(FXCollections.observableArrayList());
    }

    @Override
    public boolean add(Event event) {
        return super.add(event);
    }

    @Override
    public Event remove(int eventId) {
        for (Event event: this) {
            if (event.getId() == eventId) {
                this.remove(event);
                return event;
            }
        }
        return null;
    }

    public void refresh(ResultSet newResult) throws SQLException {
        if (!this.isEmpty()) this.clear();
        while (newResult.next()) {
            add(new Event(newResult.getInt("id"), newResult.getString("title"), newResult.getString("description"), newResult.getDate("due_date")));
        }
    }
}
