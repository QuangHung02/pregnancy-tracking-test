package com.example.pregnancy_tracking.controller;

import com.example.pregnancy_tracking.dto.PregnancyDTO;
import com.example.pregnancy_tracking.entity.Pregnancy;
import com.example.pregnancy_tracking.entity.User;
import com.example.pregnancy_tracking.security.JwtUtil;
import com.example.pregnancy_tracking.service.PregnancyService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest
@AutoConfigureMockMvc
class PregnancyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PregnancyService pregnancyService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "test@example.com", roles = "USER")
    void createPregnancy_ShouldReturn200_WhenValidRequest() throws Exception {
        String token = jwtUtil.generateToken("test@example.com");
        PregnancyDTO pregnancyDTO = new PregnancyDTO();
        pregnancyDTO.setUserId(1L);
        pregnancyDTO.setExamDate(LocalDate.now());
        pregnancyDTO.setGestationalWeeks(12);
        pregnancyDTO.setGestationalDays(3);

        Pregnancy mockPregnancy = new Pregnancy();
        mockPregnancy.setPregnancyId(1L);

        Mockito.when(pregnancyService.createPregnancy(any())).thenReturn(mockPregnancy);

        mockMvc.perform(post("/api/pregnancies/create")
            .header("Authorization", "Bearer " + token) // Thêm token vào header
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(pregnancyDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.pregnancyId").value(1L));
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = "USER")
    void createPregnancy_ShouldReturn400_WhenInvalidData() throws Exception {
        PregnancyDTO pregnancyDTO = new PregnancyDTO();
        pregnancyDTO.setUserId(null);
        pregnancyDTO.setExamDate(null);
        pregnancyDTO.setGestationalWeeks(-5);
        pregnancyDTO.setGestationalDays(40);

        mockMvc.perform(post("/api/pregnancies/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pregnancyDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = "USER")
    void createPregnancy_ShouldReturn400_WhenRequestBodyIsNull() throws Exception {
        mockMvc.perform(post("/api/pregnancies/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = "USER")
    void updatePregnancy_ShouldReturn200_WhenValidRequest() throws Exception {
        Long id = 1L;
        PregnancyDTO pregnancyDTO = new PregnancyDTO();
        pregnancyDTO.setUserId(1L);
        pregnancyDTO.setExamDate(LocalDate.now());
        pregnancyDTO.setGestationalWeeks(20);
        pregnancyDTO.setGestationalDays(3);
        User user = new User();
        user.setId(1L);
        Pregnancy updatedPregnancy = new Pregnancy();
        updatedPregnancy.setPregnancyId(id);
        updatedPregnancy.setUser(user);
        updatedPregnancy.setExamDate(LocalDate.now());
        updatedPregnancy.setGestationalWeeks(20);
        updatedPregnancy.setGestationalDays(3);

        Mockito.when(pregnancyService.updatePregnancy(eq(id), any())).thenReturn(updatedPregnancy);

        mockMvc.perform(put("/api/pregnancies/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pregnancyDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pregnancyId").value(id))
                .andExpect(jsonPath("$.gestationalWeeks").value(20))
                .andExpect(jsonPath("$.gestationalDays").value(3));
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = "USER")
    void updatePregnancy_ShouldReturn400_WhenInvalidData() throws Exception {
        Long id = 1L;
        PregnancyDTO pregnancyDTO = new PregnancyDTO();
        pregnancyDTO.setUserId(null);
        pregnancyDTO.setExamDate(null);
        pregnancyDTO.setGestationalWeeks(-10);
        pregnancyDTO.setGestationalDays(50);

        mockMvc.perform(put("/api/pregnancies/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pregnancyDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = "USER")
    void updatePregnancy_ShouldReturn404_WhenPregnancyNotFound() throws Exception {
        Long id = 99L;
        PregnancyDTO pregnancyDTO = new PregnancyDTO();
        pregnancyDTO.setUserId(1L);
        pregnancyDTO.setExamDate(LocalDate.now());
        pregnancyDTO.setGestationalWeeks(12);
        pregnancyDTO.setGestationalDays(3);

        Mockito.when(pregnancyService.updatePregnancy(eq(id), any(PregnancyDTO.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Pregnancy not found"));

        mockMvc.perform(put("/api/pregnancies/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pregnancyDTO)))
                .andExpect(status().isNotFound());
    }
}
