package com.example.desporto24.repository;
import com.example.desporto24.model.Ideias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
@Repository
@Transactional(readOnly = true)
public interface IdeiasRepository extends JpaRepository<Ideias, Long> {
}
