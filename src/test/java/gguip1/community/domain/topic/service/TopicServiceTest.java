//package gguip1.community.domain.topic.service;
//
//import gguip1.community.domain.topic.dto.request.VoteRequest;
//import gguip1.community.domain.topic.dto.response.TopicResponse;
//import gguip1.community.domain.topic.entity.*;
//import gguip1.community.domain.topic.repository.TopicOptionRepository;
//import gguip1.community.domain.topic.repository.TopicRepository;
//import gguip1.community.domain.topic.repository.VoteRepository;
//import gguip1.community.domain.user.entity.User;
//import gguip1.community.domain.user.repository.UserRepository;
//import gguip1.community.global.exception.ErrorCode;
//import gguip1.community.global.exception.ErrorException;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.time.LocalDate;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.verify;
//
//@ExtendWith(MockitoExtension.class)
//class TopicServiceTest {
//
//    @InjectMocks
//    private TopicService topicService;
//
//    @Mock
//    private TopicRepository topicRepository;
//    @Mock
//    private TopicOptionRepository topicOptionRepository;
//    @Mock
//    private VoteRepository voteRepository;
//    @Mock
//    private UserRepository userRepository;
//
//    @Test
//    @DisplayName("오늘의 토픽 조회 성공")
//    void getTodayTopic_Success() {
//        // given
//        Topic topic = new Topic("Title", "Desc", LocalDate.now(), TopicStatus.OPEN);
//        TopicOption optionA = new TopicOption(topic, OptionLabel.A, "Option A");
//        TopicOption optionB = new TopicOption(topic, OptionLabel.B, "Option B");
//        topic.addOption(optionA);
//        topic.addOption(optionB);
//
//        given(topicRepository.findByTargetDateWithOptions(any(LocalDate.class))).willReturn(Optional.of(topic));
//
//        // when
//        TopicResponse response = topicService.getTodayTopic(null);
//
//        // then
//        assertThat(response.getTitle()).isEqualTo("Title");
//        assertThat(response.getOptions()).hasSize(2);
//    }
//
//    @Test
//    @DisplayName("투표 성공")
//    void vote_Success() {
//        // given
//        Long userId = 1L;
//        Long topicId = 1L;
//        Long optionId = 100L;
//        VoteRequest request = new VoteRequest(optionId);
//
//        User user = User.builder().email("test@test.com").nickname("test").password("pw").build();
//        ReflectionTestUtils.setField(user, "userId", userId);
//
//        Topic topic = new Topic("Title", "Desc", LocalDate.now(), TopicStatus.OPEN);
//        ReflectionTestUtils.setField(topic, "topicId", topicId);
//
//        TopicOption option = new TopicOption(topic, OptionLabel.A, "Option A");
//        ReflectionTestUtils.setField(option, "optionId", optionId);
//
//        given(userRepository.findById(userId)).willReturn(Optional.of(user));
//        given(topicRepository.findById(topicId)).willReturn(Optional.of(topic));
//        given(voteRepository.existsByTopicAndUser(topic, user)).willReturn(false);
//        given(topicOptionRepository.findById(optionId)).willReturn(Optional.of(option));
//
//        // when
//        topicService.vote(topicId, request, userId);
//
//        // then
//        verify(voteRepository).save(any(Vote.class));
//        verify(topicOptionRepository).incrementVoteCount(optionId);
//    }
//
//    @Test
//    @DisplayName("중복 투표 실패")
//    void vote_Duplicate_Fail() {
//        // given
//        Long userId = 1L;
//        Long topicId = 1L;
//        Long optionId = 100L;
//        VoteRequest request = new VoteRequest(optionId);
//
//        User user = User.builder().email("test@test.com").nickname("test").password("pw").build();
//        Topic topic = new Topic("Title", "Desc", LocalDate.now(), TopicStatus.OPEN);
//
//        given(userRepository.findById(userId)).willReturn(Optional.of(user));
//        given(topicRepository.findById(topicId)).willReturn(Optional.of(topic));
//        given(voteRepository.existsByTopicAndUser(topic, user)).willReturn(true);
//
//        // when & then
//        assertThatThrownBy(() -> topicService.vote(topicId, request, userId))
//                .isInstanceOf(ErrorException.class)
//                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.DUPLICATE_VOTE);
//    }
//}
