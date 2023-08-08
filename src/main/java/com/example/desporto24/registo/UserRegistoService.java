package com.example.desporto24.registo;
import com.example.desporto24.exception.domain.EmailExistException;
import com.example.desporto24.exception.domain.PhoneExistException;
import com.example.desporto24.exception.domain.UsernameExistException;
import com.example.desporto24.model.Perfil;
import com.example.desporto24.registo.token.ConfirmationToken;
import com.example.desporto24.registo.token.ConfirmationTokenService;
import com.example.desporto24.repository.PerfilRepository;
import com.example.desporto24.service.impl.NotAnImageFileException;
import com.example.desporto24.service.impl.ProjectServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class UserRegistoService {
    private final ProjectServiceImpl pService;
    private final ConfirmationTokenService confirmationTokenService;
    private final PerfilRepository pr;

    public Perfil register(UserRegistoRequest request) throws EmailExistException, PhoneExistException, UsernameExistException, IOException, NotAnImageFileException, jakarta.mail.MessagingException {
        Perfil perfil = pService.signUpPerfil2(
                new Perfil(
                        request.getUsername(),
                        request.getPassword(),
                        request.getFullName(),
                        request.getDateOfBirth(),
                        request.getAddress(),
                        request.getCountry(),
                        request.getLocation(),
                        request.getPostalCode(),
                        request.getPhone(),
                        request.getGender(),
                        request.getEmail(),
                        request.getDesportosFavoritos(),
                        request.getFoto()));
        return perfil;
    }
    /*

    public Perfil register2(UserRegistoRequest request,MultipartFile foto) throws EmailExistException, PhoneExistException, MessagingException, UsernameExistException, IOException, NotAnImageFileException {
        Perfil perfil = pService.signUpPerfil(
                new Perfil(
                        request.getUsername(),
                        request.getPassword(),
                        request.getFullName(),
                        request.getDateOfBirth(),
                        request.getAddress(),
                        request.getCountry(),
                        request.getLocation(),
                        request.getPostalCode(),
                        request.getPhone(),
                        request.getGender(),
                        request.getEmail(),
                        request.getDesportosFavoritos()),
                        foto);
        return perfil;
    }
     */

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token não encontrado."));
        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Email já foi confirmado.");
        }
        LocalDateTime expiredAt = confirmationToken.getExpiredAt();
        if (expiredAt.isBefore(LocalDateTime.now())){
            throw new IllegalStateException("Token expirado");
        }
        confirmationTokenService.setConfirmedAt(token);
        pr.enablePerfil(confirmationToken.getPerfil().getEmail());
        return "<span style=\\\"display:none;font-size:1px;color:#fff;max-height:0\\\"></span>\\n\" +\n" +
                "                        \"\\n\" +\n" +
                "                        \"  <table role=\\\"presentation\\\" width=\\\"100%\\\" style=\\\"border-collapse:collapse;min-width:100%;width:100%!important\\\" cellpadding=\\\"0\\\" cellspacing=\\\"0\\\" border=\\\"0\\\">\\n\" +\n" +
                "                        \"    <tbody><tr>\\n\" +\n" +
                "                        \"        \\n\" +\n" +
                "                        \"        <table role=\\\"presentation\\\" width=\\\"100%\\\" style=\\\"border-collapse:collapse;max-width:580px\\\" cellpadding=\\\"0\\\" cellspacing=\\\"0\\\" border=\\\"0\\\" align=\\\"center\\\">\\n\" +\n" +
                "                        \"          <tbody><tr>\\n\" +\n" +
                "                        \"                <table role=\\\"presentation\\\" cellpadding=\\\"0\\\" cellspacing=\\\"0\\\" border=\\\"0\\\" style=\\\"border-collapse:collapse\\\">\\n\" +\n" +
                "                        \"                  <tbody><tr>\\n\" +\n" +
                "                        \"                    <td style=\\\"padding-left:10px\\\">\\n\" +\n" +
                "                        \"                  \\n\" +\n" +
                "                        \"                    </td>\\n\" +\n" +
                "                        \"                    <td style=\\\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\\\">\\n\" +\n" +
                "                        \"                      <span style=\\\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#000000;text-decoration:none;vertical-align:top;display:inline-block\\\">Registo no Desporto24!</span>\\n\" +\n" +
                "                        \"                    </td>\\n\" +\n" +
                "                        \"                  </tr>\\n\" +\n" +
                "                        \"                </tbody></table>\\n\" +\n" +
                "                        \"              </a>\\n\" +\n" +
                "                        \"            </td>\\n\" +\n" +
                "                        \"          </tr>\\n\" +\n" +
                "                        \"        </tbody></table>\\n\" +\n" +
                "                        \"        \\n\" +\n" +
                "                        \"      </td>\\n\" +\n" +
                "                        \"    </tr>\\n\" +\n" +
                "                        \"  </tbody></table>\\n\" +\n" +
                "                        \"  <table role=\\\"presentation\\\" class=\\\"m_-6186904992287805515content\\\" align=\\\"center\\\" cellpadding=\\\"0\\\" cellspacing=\\\"0\\\" border=\\\"0\\\" style=\\\"border-collapse:collapse;max-width:580px;width:100%!important\\\" width=\\\"100%\\\">\\n\" +\n" +
                "                        \"    <tbody><tr>\\n\" +\n" +
                "                        \"      <td width=\\\"10\\\" height=\\\"10\\\" valign=\\\"middle\\\"></td>\\n\" +\n" +
                "                        \"      <td>\\n\" +\n" +
                "                        \"        \\n\" +\n" +
                "                        \"                <table role=\\\"presentation\\\" width=\\\"100%\\\" cellpadding=\\\"0\\\" cellspacing=\\\"0\\\" border=\\\"0\\\" style=\\\"border-collapse:collapse\\\">\\n\" +\n" +
                "                        \"                  <tbody><tr>\\n\" +\n" +
                "                        \"                  </tr>\\n\" +\n" +
                "                        \"                </tbody></table>\\n\" +\n" +
                "                        \"        \\n\" +\n" +
                "                        \"      </td>\\n\" +\n" +
                "                        \"      <td width=\\\"10\\\" valign=\\\"middle\\\" height=\\\"10\\\"></td>\\n\" +\n" +
                "                        \"    </tr>\\n\" +\n" +
                "                        \"  </tbody></table>\\n\" +\n" +
                "                        \"\\n\" +\n" +
                "                        \"\\n\" +\n" +
                "                        \"\\n\" +\n" +
                "                        \"  <table role=\\\"presentation\\\" class=\\\"m_-6186904992287805515content\\\" align=\\\"center\\\" cellpadding=\\\"0\\\" cellspacing=\\\"0\\\" border=\\\"0\\\" style=\\\"border-collapse:collapse;max-width:580px;width:100%!important\\\" width=\\\"100%\\\">\\n\" +\n" +
                "                        \"    <tbody><tr>\\n\" +\n" +
                "                        \"      <td height=\\\"30\\\"><br></td>\\n\" +\n" +
                "                        \"    </tr>\\n\" +\n" +
                "                        \"    <tr>\\n\" +\n" +
                "                        \"      <td width=\\\"10\\\" valign=\\\"middle\\\"><br></td>\\n\" +\n" +
                "                        \"      <td style=\\\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\\\">\\n\" +\n" +
                "                        \"        \\n\" +\n" +
                "                        \"            <p style=\\\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\\\">Olá \" + name + \",</p><p style=\\\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\\\"> Obrigado por te registares na nossa aplicação. Clica neste link para ativar a tua conta: </p><p style=\\\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\\\"> <a href=\\\"\" + link + \"\\\">Ativar agora</a> </p><p>Cumprimentos,</p><p>DESPORTO24APP</p>\" +\n" +
                "                        \"        \\n\" +\n" +
                "                        \"      </td>\\n\" +\n" +
                "                        \"      <td width=\\\"10\\\" valign=\\\"middle\\\"><br></td>\\n\" +\n" +
                "                        \"    </tr>\\n\" +\n" +
                "                        \"    <tr>\\n\" +\n" +
                "                        \"      <td height=\\\"30\\\"><br></td>\\n\" +\n" +
                "                        \"    </tr>\\n\" +\n" +
                "                        \"  </tbody></table><div class=\\\"yj6qo\\\"></div><div class=\\\"adL\\\">\\n\" +\n" +
                "                        \"\\n\" +\n" +
                "                        \"</div></div>";
    }
    @Transactional
    public String confirmEmergencyToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token não encontrado."));
        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Este link já foi clicado.");
        }
        confirmationTokenService.setConfirmedAt(token);
        pService.disablePerfil(confirmationToken.getPerfil().getEmail());
        return "A tua conta neste momento encontra-se bloqueada, podes fechar esta janela.";
    }
}