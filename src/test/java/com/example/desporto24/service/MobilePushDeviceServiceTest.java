package com.example.desporto24.service;

import com.example.desporto24.model.MobilePushDevice;
import com.example.desporto24.repository.MobilePushDeviceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MobilePushDeviceServiceTest {
    private final MobilePushDeviceRepository repository=mock(MobilePushDeviceRepository.class);
    private final MobilePushDeviceService service=new MobilePushDeviceService(repository);
    private static final String DEVICE="131df783-4b8f-42da-8f58-382a08a395a1";
    private static final String TOKEN="ExponentPushToken[0123456789abcdefghijkl]";

    @Test
    void registersOnlyAuthenticatedOwnersWithoutReturningToken() {
        when(repository.findByPushToken(TOKEN)).thenReturn(Optional.empty());
        when(repository.findById(DEVICE)).thenReturn(Optional.empty());
        var result=service.register("PLAYER",new MobilePushDeviceService.Registration(DEVICE,TOKEN,"android"));
        assertTrue(result.registered());
        verify(repository).save(argThat(device->
            device.getUsername().equals("player") && device.getDeviceId().equals(DEVICE)
            && device.getPushToken().equals(TOKEN) && device.getUpdatedAt()!=null));
    }

    @Test
    void preventsTokenTransferToAnotherAccount() {
        var previous=new MobilePushDevice();
        previous.setDeviceId(DEVICE);
        previous.setUsername("other");
        previous.setPushToken(TOKEN);
        when(repository.findByPushToken(TOKEN)).thenReturn(Optional.of(previous));
        assertThrows(ResponseStatusException.class,()->
            service.register("player",new MobilePushDeviceService.Registration(DEVICE,TOKEN,"android")));
        verify(repository,never()).save(any());
    }

    @Test
    void rejectsInvalidTokensAndPlatforms() {
        assertThrows(ResponseStatusException.class,()->
            service.register("player",new MobilePushDeviceService.Registration(DEVICE,"fake","android")));
        assertThrows(ResponseStatusException.class,()->
            service.register("player",new MobilePushDeviceService.Registration(DEVICE,TOKEN,"web")));
        verify(repository,never()).save(any());
    }

    @Test
    void unregisterOnlyRemovesOwnDevice() {
        var previous=new MobilePushDevice();
        previous.setDeviceId(DEVICE);
        previous.setUsername("other");
        when(repository.findById(DEVICE)).thenReturn(Optional.of(previous));
        service.unregister("player",DEVICE);
        verify(repository,never()).delete(any());
        previous.setUsername("player");
        service.unregister("PLAYER",DEVICE);
        verify(repository).delete(previous);
    }
}
