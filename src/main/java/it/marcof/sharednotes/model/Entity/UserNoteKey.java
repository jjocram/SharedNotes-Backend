package it.marcof.sharednotes.model.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserNoteKey implements Serializable {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "note_id")
    private Long noteId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserNoteKey that = (UserNoteKey) o;
        return Objects.equals(userId, that.userId) && Objects.equals(noteId, that.noteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, noteId);
    }
}
