package fit.biepjv.deadlines;

import java.util.Date;

public record Event(int id, String title, String description, Date dueDate) {

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return dueDate;
    }

    @Override
    public String toString() {
        return title + " (due " + dueDate + ")";
    }
}
