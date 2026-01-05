package patil.parshwa.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import patil.parshwa.blog.models.Post;
import patil.parshwa.blog.models.PostTag;
import patil.parshwa.blog.models.PostTagId;

import java.util.List;

@Repository
public interface PostTagRepository extends JpaRepository<PostTag, PostTagId> {
    boolean existsByPostTagId(PostTagId postTagId);
    List<PostTag> findAllByTagId(Long tagId);
}
