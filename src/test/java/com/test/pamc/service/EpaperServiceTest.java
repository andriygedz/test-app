package com.test.pamc.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.test.pamc.entity.EpaperEntity;
import com.test.pamc.repository.EpaperRepository;


@ExtendWith(MockitoExtension.class)
public class EpaperServiceTest {
    @InjectMocks
    private EpaperService epaperService;

    @Mock
    private EpaperRepository epaperRepository;

    @Captor
    ArgumentCaptor<EpaperEntity> epaperCaptor;

    @Test
    void createEpaperValidFile() throws IOException, JAXBException {
        InputStream resourceAsStream = this.getClass().getResourceAsStream("/valid.xml");

        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        when(multipartFile.getInputStream()).thenReturn(resourceAsStream);
        when(multipartFile.getOriginalFilename()).thenReturn("test");

        epaperService.createEpaper(multipartFile);

        verify(epaperRepository).save(epaperCaptor.capture());
        EpaperEntity value = epaperCaptor.getValue();
        assertThat(value.getFileName()).isEqualTo("test");
        assertThat(value.getNewspaperName()).isEqualTo("abb");

    }

    @Test
    void createEpaperInValidFile() throws IOException {
        InputStream resourceAsStream = this.getClass().getResourceAsStream("/invalid.xml");

        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        when(multipartFile.getInputStream()).thenReturn(resourceAsStream);

        JAXBException exception = assertThrows(JAXBException.class, () -> epaperService.createEpaper(multipartFile));

        String expectedMessage = "cvc-minInclusive-valid: Value '-752' is not facet-valid with respect to minInclusive '0' for type 'nonNegativeInt'.";
        String actualMessage = exception.getCause().getMessage();

        assertThat(actualMessage).containsSequence(expectedMessage);
        verifyNoInteractions(epaperRepository);
    }
}
