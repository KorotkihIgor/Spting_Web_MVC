package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepository {
    private final ConcurrentHashMap<Long, Post> posts = new ConcurrentHashMap<>();
    private final AtomicLong postId = new AtomicLong(1);

    public List<Post> all() {
        return new ArrayList<>(posts.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(posts.get(id));
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            var id = postId.getAndIncrement();
            post.setId(id);
            posts.put(id, post);
            return post;
        }
        if (posts.containsKey(post.getId())) {
            posts.put(post.getId(), post);
            return post;
        }
        throw new NotFoundException(String.format("Post with id %d not found", post.getId()));
    }

    public void removeById(long id) {
        posts.remove(id);
    }

}
