package gguip1.community.domain.topic.repository;

import gguip1.community.domain.topic.entity.Topic;
import gguip1.community.domain.topic.entity.Vote;
import gguip1.community.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    boolean existsByTopicAndUser(Topic topic, User user);
    Optional<Vote> findByTopicAndUser(Topic topic, User user);
}
