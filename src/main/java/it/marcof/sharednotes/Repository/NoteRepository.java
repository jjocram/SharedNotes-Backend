package it.marcof.sharednotes.Repository;

import it.marcof.sharednotes.model.Entity.NoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<NoteEntity, Long> {
    NoteEntity findNoteEntityById(Long id);
}
