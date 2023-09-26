package com.example.desporto24.service.impl;

import com.example.desporto24.exception.domain.EmailExistException;
import com.example.desporto24.model.Perfil;
import com.example.desporto24.registo.token.ConfirmationTokenService;
import com.example.desporto24.repository.PerfilRepository;
import com.example.desporto24.repository.SessaoRepository;
import com.example.desporto24.service.EmailService;
import com.example.desporto24.service.ProjectService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    @Mock
    private PerfilRepository perfilRepository;
    private ConfirmationTokenService confirmationTokenService;
    private ProjectServiceImpl underTest;
    private BCryptPasswordEncoder passwordEncoder;
    private EmailService emailService;
    private SessaoRepository sessaoRepository;
    private ProjectService projectService;

    /*
    @BeforeEach
    void setUp() {
        underTest = new ProjectServiceImpl(perfilRepository,passwordEncoder,loginAttemptService,emailService,confirmationTokenService,sessaoRepository);
    }
     */

    @AfterEach
    void tearDown() throws Exception {
    }

    /*
    @Test
    void signUpPerfil3() throws EmailExistException, MessagingException, PhoneExistException, IOException, UsernameExistException, NotAnImageFileException {
        Perfil perfil = new Perfil("Miguel14","miguel1","a","30","ol","lo","a","post","98124","masc","tmfftw@gmail.com",
                "futebol, karts e ténis",null);
        underTest.signUpPerfil3(perfil);
        ArgumentCaptor<Perfil> perfilArgumentCaptor = ArgumentCaptor.forClass(Perfil.class);
        verify(perfilRepository).save(perfilArgumentCaptor.capture());
        Perfil novoPerfil = perfilArgumentCaptor.getValue();
        assertThat(novoPerfil).isEqualTo(perfil);
    }
     */

    @Test
    void willThrowWhenEmailIsTaken(){
        Perfil perfil = new Perfil("Miguel14","miguel1","a","30","ol","lo","a","post","98124","masc","tmfftw@gmail.com",
                "futebol, karts e ténis",null);
        given(perfilRepository.findUserByEmail(perfil.getEmail())).willReturn(perfil);
        assertThatThrownBy(() -> underTest.signUpPerfil3(perfil))
                .isInstanceOf(EmailExistException.class)
                .hasMessageContaining("Email already exist");
        verify(perfilRepository, never()).save(any());
    }

    @Test
    void getPerfis() {
        underTest.getPerfis();
        verify(perfilRepository).findAll();
    }

    @Test
    @Disabled
    void updateUser() {
    }

    @Test
    @Disabled
    void deleteUser() {
    }
}