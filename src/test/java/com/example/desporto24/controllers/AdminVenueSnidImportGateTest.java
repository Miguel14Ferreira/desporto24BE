package com.example.desporto24.controllers;

import com.example.desporto24.configuration.SnidVenueImportData;
import org.junit.jupiter.api.Test;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AdminVenueSnidImportGateTest {

    @Test
    void bulkImportIsDisabledByDefaultEvenIfCalledByAnAdmin() {
        var source = mock(SnidVenueImportData.class);
        var executor = mock(TaskExecutor.class);
        var controller = new AdminVenueImportController(null, null, source, executor);

        ResponseStatusException error = assertThrows(
                ResponseStatusException.class, controller::importSnidVenues);
        assertEquals(HttpStatus.FORBIDDEN, error.getStatusCode());
        assertEquals(false, controller.snidImportStatus().get("importEnabled"));
        verifyNoInteractions(source, executor);
    }

    @Test
    void enablingBulkImportRequiresAnExplicitProperty() {
        var source = mock(SnidVenueImportData.class);
        var executor = mock(TaskExecutor.class);
        var controller = new AdminVenueImportController(null, null, source, executor);
        ReflectionTestUtils.setField(controller, "snidImportEnabled", true);

        controller.importSnidVenues();

        assertEquals(true, controller.snidImportStatus().get("importEnabled"));
        verify(executor).execute(any(Runnable.class));
        verifyNoInteractions(source); // queued, not executed in this isolated unit test
    }
}
