package softuniBlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import softuniBlog.entity.Video;

public interface VideoRepository extends JpaRepository<Video, Integer> {
}
