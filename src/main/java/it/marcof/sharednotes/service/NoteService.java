package it.marcof.sharednotes.service;

import it.marcof.sharednotes.model.Entity.NoteEntity;

import java.util.List;

public interface NoteService {
    NoteEntity create(NoteEntity note, String ownerUsername);

    List<NoteEntity> all(String username);

    NoteEntity addEditor(Long noteId, String editor);
}
