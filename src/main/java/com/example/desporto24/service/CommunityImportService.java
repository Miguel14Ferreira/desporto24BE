package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.model.dto.community.CommunityImportPreviewResponse;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CommunityImportService {

    private final ProfileRepository profileRepository;
    private final CommunityMemberRepository memberRepository;

    public CommunityImportPreviewResponse preview(
            Long communityId,
            String username,
            MultipartFile file) {
        Profile actor = profileRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado"));
        requireAdmin(communityId, actor);
        validateFile(file);

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            List<CommunityImportPreviewResponse.SheetPreview> sheets = new ArrayList<>();
            for (Sheet sheet : workbook) {
                int nonEmptyRows = 0;
                for (Row row : sheet) {
                    boolean hasValue = false;
                    for (var cell : row) {
                        if (!cell.toString().isBlank()) {
                            hasValue = true;
                            break;
                        }
                    }
                    if (hasValue) nonEmptyRows++;
                }
                sheets.add(new CommunityImportPreviewResponse.SheetPreview(
                        sheet.getSheetName(),
                        nonEmptyRows
                ));
            }
            return new CommunityImportPreviewResponse(file.getOriginalFilename(), sheets);
        } catch (IOException | RuntimeException exception) {
            throw new IllegalArgumentException("Não foi possível ler o ficheiro Excel", exception);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Seleciona um ficheiro Excel");
        }
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase(Locale.ROOT).endsWith(".xlsx")) {
            throw new IllegalArgumentException("O ficheiro deve estar no formato .xlsx");
        }
    }

    private void requireAdmin(Long communityId, Profile actor) {
        CommunityMember membership = memberRepository
                .findByCommunityIdAndProfileId(communityId, actor.getId())
                .orElseThrow(() -> new AccessDeniedException("Não pertences a esta comunidade"));
        if (membership.getRole() != CommunityRole.OWNER
                && membership.getRole() != CommunityRole.ADMIN) {
            throw new AccessDeniedException("Apenas Owners e Admins podem importar histórico");
        }
    }
}
