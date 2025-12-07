package gguip1.community.domain.topic.repository;

import gguip1.community.domain.topic.entity.TopicOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TopicOptionRepository extends JpaRepository<TopicOption, Long> {

    @Modifying
    @Query("UPDATE TopicOption to SET to.voteCount = to.voteCount + 1 WHERE to.optionId = :optionId")
    void incrementVoteCount(@Param("optionId") Long optionId);
}
