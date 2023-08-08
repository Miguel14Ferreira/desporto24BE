package com.example.desporto24.update;

import com.example.desporto24.service.impl.ProjectServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserUpdateService {
    private final ProjectServiceImpl pService;
/*
    public Perfil update(UserUpdateRequest request) throws EmailExistException, PhoneExistException, MessagingException, UsernameExistException, IOException, NotAImageFileException {
        Perfil perfil = pService.updateUser(
                new Perfil(
                        request.getUsername(),
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

 */
}