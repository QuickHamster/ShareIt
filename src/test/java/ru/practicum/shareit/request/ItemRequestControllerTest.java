package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestListItemDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
public class ItemRequestControllerTest {
    @MockBean
    private ItemRequestService itemRequestService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;

    private ItemRequestDto itemRequestDto;
    private static final String X_HEADER = "X-Sharer-User-Id";

    @BeforeEach
    void beforeEach_1() {
        itemRequestDto = new ItemRequestDto(1L,"description",
                null, LocalDateTime.now());
    }

    @Test
    void addItemRequest() throws Exception {
        doReturn(itemRequestDto).when(itemRequestService).addItemRequest(anyLong(), any());

        mockMvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(X_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription()),  String.class));
    }

    @Test
    void getItemRequestsByUser() throws Exception {
        List<ItemRequestListItemDto> itemRequestListItemDtos = new ArrayList<>();
        doReturn(itemRequestListItemDtos).when(itemRequestService).getItemRequestsByUser(anyLong());

        mockMvc.perform(get("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(X_HEADER, 1L))
                .andExpect(status().isOk());
    }

    @Test
    void getItemRequestByRequestId() throws Exception {
        ItemRequestListItemDto itemRequestListItemDto = new ItemRequestListItemDto();
        doReturn(itemRequestListItemDto).when(itemRequestService).getItemRequestByRequestId(anyLong(),anyLong());

        mockMvc.perform(get("/requests/{id}", itemRequestDto.getId())
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(X_HEADER, itemRequestDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestListItemDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestListItemDto.getDescription()),
                        String.class));
    }

    @Test
    void getAllItemRequests() throws Exception {
        List<ItemRequestListItemDto> itemRequestListItemDtos = new ArrayList<>();
        doReturn(itemRequestListItemDtos).when(itemRequestService).getAllRequests(anyLong(), anyInt(), anyInt());

        mockMvc.perform(get("/requests/all")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(X_HEADER, 1L))
                .andExpect(status().isOk());
    }
}
