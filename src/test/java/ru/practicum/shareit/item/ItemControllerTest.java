package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
public class ItemControllerTest {
    @MockBean
    private ItemService itemService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    private ItemDto itemDto;

    private ItemOutputDto itemOutputDto;
    private ItemCommentsOutputDto itemCommentsOutputDto;
    private CommentInputDto commentInputDto;
    private CommentOutputDto commentOutputDto;
    private Item item;

    private static final String X_HEADER = "X-Sharer-User-Id";

    public ItemControllerTest() {
    }

    @BeforeEach
    void BeforeEach_1() {
        item = new Item();
        item.setId(1L);
        item.setName("username");
        item.setDescription("description");
        item.setAvailable(true);

        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("username");
        itemDto.setDescription("description");
        itemDto.setAvailable(true);

        itemOutputDto = new ItemOutputDto();
        itemOutputDto.setId(1L);
        itemOutputDto.setName("username");
        itemOutputDto.setDescription("description");
        itemOutputDto.setAvailable(true);

        itemCommentsOutputDto = new ItemCommentsOutputDto();
        itemCommentsOutputDto.setId(1L);
        itemCommentsOutputDto.setName("username");
        itemCommentsOutputDto.setDescription("description");
        itemCommentsOutputDto.setAvailable(true);

        commentInputDto = new CommentInputDto();
        commentInputDto.setText("text comment");

        commentOutputDto = new CommentOutputDto();
        commentOutputDto.setId(1L);
        commentOutputDto.setText("text comment");
        commentOutputDto.setAuthorName("author name");
        commentOutputDto.setCreated(LocalDate.now());
    }

    @Test
    void addItem() throws Exception {
        when(itemService.addItem(anyLong(), any(ItemDto.class)))
                .thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(X_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription()), String.class))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable()), Boolean.class));
    }

    @Test
    void updateItem() throws Exception {
        when(itemService.changeItem(any(ItemDto.class), anyLong(), anyLong()))
                .thenReturn(itemDto);

        mockMvc.perform(patch("/items/{id}", itemDto.getId())
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(X_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription()), String.class))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable()), Boolean.class));
    }

    @Test
    void removeItem() throws Exception {
        when(itemService.deleteItem(anyLong()))
                .thenReturn(1L);

        mockMvc.perform(delete("/items/{id}", itemDto.getId())
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllItems() throws Exception {
        List<ItemOutputDto> items = List.of(itemOutputDto);
        when(itemService.getAllItems(anyLong())).thenReturn(List.of(itemOutputDto));

        mockMvc.perform(get("/items")
                        .header(X_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(items.size()));
    }

    @Test
    void findItemById() throws Exception {
        when(itemService.findItemById(anyLong(), anyLong()))
                .thenReturn(itemCommentsOutputDto);

        mockMvc.perform(get("/items/{id}", itemCommentsOutputDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(X_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemCommentsOutputDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemCommentsOutputDto.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemCommentsOutputDto.getDescription()), String.class))
                .andExpect(jsonPath("$.available", is(itemCommentsOutputDto.getAvailable()), Boolean.class));
    }

    @Test
    void addCommentToItem() throws Exception {
        when(itemService.addCommentToItem(anyLong(), anyLong(), any(CommentInputDto.class)))
                .thenReturn(commentOutputDto);

        mockMvc.perform(post("/items/{itemId}/comment", itemDto.getId())
                        .content(mapper.writeValueAsString(commentInputDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(X_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentOutputDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentOutputDto.getText()), String.class))
                .andExpect(jsonPath("$.authorName", is(commentOutputDto.getAuthorName()), String.class))
                .andExpect(jsonPath("$.created", is(commentOutputDto.getCreated().toString()), String.class));
    }

    @Test
    void findItemByText() throws Exception {

        when(itemService.searchItems(anyString()))
                .thenReturn(List.of(item));

        mockMvc.perform(get("/items/search")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("text", "text"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(item.getName()), String.class))
                .andExpect(jsonPath("$[0].description", is(item.getDescription()), String.class))
                .andExpect(jsonPath("$[0].available", is(item.getAvailable()), Boolean.class));
    }
}
