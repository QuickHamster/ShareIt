package ru.practicum.shareit.request.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor // будет сгенерирован конструктор с одним параметром для каждого поля класса
@NoArgsConstructor
@Entity
@Table(name = "requests", schema = "public")
@Getter
@Setter
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // уникальный идентификатор запроса

    private String description; // текст запроса, содержащий описание требуемой вещи

    @ManyToOne
    @JoinColumn(name = "requestor_id")
    private User requestor; // пользователь, создавший запрос

    private LocalDateTime created; // дата и время создания запроса

    /*@OneToMany(mappedBy = "requestId")
    @ToString.Exclude
    private Set<Item> items = new HashSet<>();*/
}
