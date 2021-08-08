package com.test.pamc.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;

import com.test.pamc.service.EpaperService;

@WebMvcTest
public class EpaperControllerTest {

    @MockBean
    private EpaperService epaperService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnDefaultMessage() throws Exception {
        mockMvc.perform(get("/epaper")
                .param("page", "5")
                .param("size", "10")
                .param("sort", "dpi,desc")   // <-- no space after comma!
                .param("sort", "newspaperName,asc")) // <-- no space after comma!
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableCaptor =
                ArgumentCaptor.forClass(Pageable.class);
        verify(epaperService).findByNewspaperName(isNull(), pageableCaptor.capture());
        PageRequest pageable = (PageRequest) pageableCaptor.getValue();

        assertThat(pageable.getPageNumber()).isEqualTo(5);
        assertThat(pageable.getPageSize()).isEqualTo(10);
        assertThat(pageable.getSort()).contains(new Sort.Order(Sort.Direction.ASC, "newspaperName"));
        assertThat(pageable.getSort()).contains(new Sort.Order(Sort.Direction.DESC, "dpi"));
    }
}
