package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@AllArgsConstructor // будет сгенерирован конструктор с одним параметром для каждого поля класса
@NoArgsConstructor
@Entity
@Table(name = "comments", schema = "public")
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // уникальный идентификатор комментария

    @Size(max = 1024, message = "Comment too long!")
    private String text; // содержимое комментария

    @ManyToOne()
    @JoinColumn(name = "item_id")
    private Item item; // вещь, к которой относится комментарий

    @ManyToOne()
    @JoinColumn(name = "author_id")
    private User author; // автор комментария

    private LocalDate created; // дата создания комментария
}
