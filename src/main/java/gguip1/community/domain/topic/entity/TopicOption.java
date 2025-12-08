package gguip1.community.domain.topic.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "topic_options")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TopicOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long optionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OptionLabel label;

    @Column(nullable = false)
    private String text;

    @Column
    private String description;

    @Column(nullable = false)
    private Long voteCount = 0L;

    public TopicOption(Topic topic, OptionLabel label, String text, String description) {
        this.topic = topic;
        this.label = label;
        this.text = text;
        this.description = description;
        this.voteCount = 0L;
    }

    public void increaseVoteCount() {
        this.voteCount++;
    }

    public void updateText(String text) {
        if (text != null) {
            this.text = text;
        }
    }

    public void updateDescription(String description) {
        if (description != null) {
            this.description = description;
        }
    }
}
