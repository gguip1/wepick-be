package gguip1.community.domain.topic.repository;

import gguip1.community.domain.topic.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    
    boolean existsByTargetDate(LocalDate targetDate);

    @Query("SELECT t FROM Topic t LEFT JOIN FETCH t.options WHERE t.targetDate = :targetDate")
    Optional<Topic> findByTargetDateWithOptions(@Param("targetDate") LocalDate targetDate);
}
