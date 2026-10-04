package com.example.desporto24.service;

import com.example.desporto24.exception.UserNotFoundException;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.dto.venue.VenuePhotoResponse;
import com.example.desporto24.model.venue.Venue;
import com.example.desporto24.model.venue.VenuePhoto;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.venue.VenuePhotoRepository;
import com.example.desporto24.repository.venue.VenueRepository;
import com.example.desporto24.storage.MediaStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.example.desporto24.constant.UserImplConstant.NO_USER_FOUND_BY_USERNAME;

@Service
@RequiredArgsConstructor
@Transactional
public class VenuePhotoService {

    private final VenueRepository venueRepository;
    private final VenuePhotoRepository venuePhotoRepository;
    private final ProfileRepository profileRepository;

    @Autowired(required = false)
    private MediaStorage mediaStorage;

    @Transactional(readOnly = true)
    public List<VenuePhotoResponse> listPhotos(Long venueId) {
        if (!venueRepository.existsById(venueId)) {
            throw new IllegalArgumentException("Recinto não encontrado");
        }

        return venuePhotoRepository.findByVenueIdOrderByCreatedAtAsc(venueId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public VenuePhotoResponse addPhoto(Long venueId, String username, MultipartFile photo) {
        if (mediaStorage == null) {
            throw new IllegalStateException("Media storage is not configured");
        }

        if (photo == null || photo.isEmpty()) {
            throw new IllegalArgumentException("A fotografia é obrigatória");
        }

        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new IllegalArgumentException("Recinto não encontrado"));

        Profile uploader = profileRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        String storageKey = "venues/" + venueId + "/" + UUID.randomUUID();
        var uri = mediaStorage.store(storageKey, photo);

        VenuePhoto saved = venuePhotoRepository.save(VenuePhoto.builder()
                .venue(venue)
                .photoUrl(uri.toString())
                .storageKey(storageKey)
                .uploadedBy(uploader)
                .createdAt(Instant.now())
                .build());

        return toResponse(saved);
    }

    private VenuePhotoResponse toResponse(VenuePhoto photo) {
        return new VenuePhotoResponse(
                photo.getId(),
                photo.getPhotoUrl(),
                photo.getUploadedBy() == null ? null : photo.getUploadedBy().getUsername(),
                photo.getCreatedAt()
        );
    }
}
