package com.example.desporto24.repository;

import com.example.desporto24.model.Perfil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class PerfilRepositoryTest {

    @Autowired
    private PerfilRepository pj;

    @Test
    void findUserByUsername() {String a = "Miguel14";
        Perfil perfil = new Perfil(a,"miguel1");
        pj.save(perfil);
        Perfil userByUsername = pj.findUserByUsername(a);
        assertThat(userByUsername).isEqualTo(perfil);
    }
    @Test
    void notFindUserByUsername() {String a = "Miguel14";
        Perfil perfil = new Perfil(a,"miguel1");
        pj.save(perfil);
        Perfil userByUsername = pj.findUserByUsername("Miguel141");
        assertThat(userByUsername).isNotEqualTo(perfil);
    }

    @Test
    void enablePerfil(){
        String email = "tmfftw@gmail.com";
        Perfil perfil = new Perfil("Miguel14","miguel1","a","30","ol","lo","a","post","98124","masc",email,
                "futebol, karts e ténis",null);
        pj.save(perfil);
        int i = pj.enablePerfil(email);
        assertThat(i).isEqualTo(1);
    }
    @Test
    void disablePerfil() {
        String email = "tmfftw@gmail.com";
        Perfil perfil = new Perfil("Miguel14", "miguel1", "a", "30", "ol", "lo", "a", "post", "98124", "masc", email,
                "futebol, karts e ténis", null);
        pj.save(perfil);
        int i = pj.disablePerfil(email);
        assertThat(i).isEqualTo(1);
    }
}