package it.marcof.sharednotes.service.impl;

import it.marcof.sharednotes.Repository.NoteRepository;
import it.marcof.sharednotes.Repository.UserNoteRepository;
import it.marcof.sharednotes.Repository.UserRepository;
import it.marcof.sharednotes.model.Entity.*;
import it.marcof.sharednotes.service.NoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;
    private final UserNoteRepository userNoteRepository;
    private final UserRepository userRepository;

    @Override
    public NoteEntity create(NoteEntity note, String ownerUsername) {
        log.info("Saving note {} with owner {}", note.getName(), ownerUsername);
        note = noteRepository.save(note);
        UserEntity owner = userRepository.findByUsername(ownerUsername);
        UserNoteKey userNoteKey = new UserNoteKey(owner.getId(), note.getId());
        UserNoteEntity userNoteEntity = userNoteRepository.save(new UserNoteEntity(userNoteKey, owner, note, RoleType.OWNER));

        owner.getNotes().add(userNoteEntity);
        return note;
    }

    @Override
    public List<NoteEntity> all(String username) {
        return userRepository.findByUsername(username).
                getNotes().stream()
                .map(UserNoteEntity::getNote)
                .collect(Collectors.toList());
    }

    @Override
    public NoteEntity addEditor(Long noteId, String editor) {
        log.info("Adding note {} to user {} as editor", noteId, editor);

        UserEntity userEntity = userRepository.findByUsername(editor);
        NoteEntity noteEntity = noteRepository.findNoteEntityById(noteId);
        UserNoteKey userNoteKey = new UserNoteKey(userEntity.getId(), noteEntity.getId());
        UserNoteEntity userNoteEntity = userNoteRepository.save(new UserNoteEntity(userNoteKey, userEntity, noteEntity, RoleType.EDITOR));

        userEntity.getNotes().add(userNoteEntity);
        return noteEntity;
    }

}
