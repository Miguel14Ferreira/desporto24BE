package com.example.desporto24.repository;
import com.example.desporto24.model.Perfil;
import com.example.desporto24.model.Sessao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;

@Repository
@Transactional(readOnly = true)
public interface SessaoRepository extends JpaRepository<Sessao, Long> {
    Sessao findSessaoByMorada(String morada);

    Sessao findSessaoByDataDeJogo(Date dataDeJogo);

    Sessao findSessaoByUsername(String username);

    @Transactional
    @Modifying
    @Query("DELETE FROM Sessao a WHERE a.username = ?1")
    int deleteSessao(String email);
}
