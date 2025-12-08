package gguip1.community.domain.topic.entity;

import gguip1.community.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "topics")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Topic extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long topicId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDate targetDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TopicStatus status;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TopicOption> options = new ArrayList<>();

    public Topic(String title, String description, LocalDate targetDate, TopicStatus status) {
        this.title = title;
        this.description = description;
        this.targetDate = targetDate;
        this.status = status;
    }

    public void addOption(TopicOption option) {
        this.options.add(option);
    }

    public void update(String title, String description, LocalDate targetDate, TopicStatus status) {
        if (title != null) this.title = title;
        if (description != null) this.description = description;
        if (targetDate != null) this.targetDate = targetDate;
        if (status != null) this.status = status;
    }
}
