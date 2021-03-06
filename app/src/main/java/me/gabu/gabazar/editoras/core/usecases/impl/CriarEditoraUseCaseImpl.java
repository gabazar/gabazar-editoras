package me.gabu.gabazar.editoras.core.usecases.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import me.gabu.gabazar.editoras.adapters.data.dao.EditoraDAO;
import me.gabu.gabazar.editoras.core.model.Editora;
import me.gabu.gabazar.editoras.core.usecases.CriarEditoraUseCase;
import me.gabu.gabazar.editoras.service.ValidationService;
import me.gabu.gabazar.editoras.service.validations.ValidationEnum;

@Slf4j
@Service
public class CriarEditoraUseCaseImpl implements CriarEditoraUseCase {

    private @Autowired EditoraDAO dao;
    private @Autowired ValidationService validator;

    @Override
    public Editora run(Editora editora, String usuario) {
        log.info("[USECASE] [CREATE] {}", editora);
        validator.validate(editora, ValidationEnum.CREATE);
        editora.setUsuarioCriacao(usuario);
        return dao.save(editora);
    }

}
