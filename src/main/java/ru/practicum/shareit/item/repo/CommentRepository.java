package ru.practicum.shareit.item.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c " +
           "where c.author.id = ?1 and c.item.id = ?2 order by c.id")
    List<Comment> findCommentsByAuthorAndItem(long authorId, long itemId);
}
