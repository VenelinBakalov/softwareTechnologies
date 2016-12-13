package softuniBlog.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import softuniBlog.entity.Position;

public interface PositionRepository extends JpaRepository<Position, Integer> {
    Position findByName(String name);
}
