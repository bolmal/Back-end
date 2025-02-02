package com.example.bolmalre.concert;

import com.example.bolmalre.concert.domain.Concert;
import com.example.bolmalre.concert.domain.ConcertImage;
import com.example.bolmalre.concert.domain.enums.ConcertRound;
import com.example.bolmalre.concert.domain.enums.OnlineStore;
import com.example.bolmalre.concert.infrastructure.ConcertImageRepository;
import com.example.bolmalre.concert.infrastructure.ConcertRepository;
import com.example.bolmalre.concert.service.ConcertServiceImpl;
import com.example.bolmalre.concert.web.dto.ConcertHomeDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ConcertServiceImplTest {

    @Mock
    private ConcertRepository concertRepository;

    @Mock
    private ConcertImageRepository concertImageRepository;

    @InjectMocks
    ConcertServiceImpl concertService;

    List<Concert> testConcerts;
    List<ConcertImage> testConcertImages;

    @BeforeEach
    void setUp() {
        // Concert 더미 데이터 생성 (10개)
        testConcerts = IntStream.range(1, 11)
                .mapToObj(i -> createDummyConcert((long) i))
                .collect(Collectors.toList());

        // ConcertImage 더미 데이터 생성 (각 Concert에 2개의 이미지 추가)
        testConcertImages = new ArrayList<>();
        for (Concert concert : testConcerts) {
            testConcertImages.add(createDummyConcertImage(1L, concert));
            testConcertImages.add(createDummyConcertImage(2L, concert));
        }

    }

    /**
     * 상단 광고
     */
    @Test
    @DisplayName("광고 여부에 대해 광고 콘서트만 정확히 반환된다")
    public void get_Correct_AD_Concert_Info_test() {
        // given

        // 광고 콘서트만 필터링
        List<Concert> adConcertList = testConcerts.stream()
                .filter(Concert::isAdvertisement)
                .collect(Collectors.toList());

        // 광고 콘서트와 이미지에 대한 Mock 설정
        Mockito.when(concertRepository.findByAdvertisementIsTrue()).thenReturn(adConcertList);

        for (Concert concert : adConcertList) {
            ConcertImage image = testConcertImages.stream()
                    .filter(img -> img.getConcert().equals(concert))
                    .findFirst()
                    .orElse(null);

            Mockito.when(concertImageRepository.findByConcert(concert))
                    .thenReturn(Optional.ofNullable(image));
        }

        // when
        List<ConcertHomeDTO.AdvertisementConcertDTO> result = concertService.getAdConcertInfo();

        // then
        assertEquals(adConcertList.size(), result.size());
        assertTrue(result.stream().allMatch(dto -> dto.getPosterUrl() != null));
    }

    /**
     * 지금 볼래 말래
     */

    @Test
    @DisplayName("로그인 전에는 일간 조회수가 가장 높은 공연 8개를 반환한다")
    public void get_Recommend_Concert_Before_Login() {
        //given

        //when

        //then

    }

    @Test
    @DisplayName("로그인 후에는 해당 사용자의 추천도 점수에 맞춰 공연 8개를 반환한다")
    public void get_Recommend_Concert_After_Login() {
        //given

        //when

        //then

    }


    /**
     * 이번주 가장 인기 있는 콘서트
     */
    @Test
    @DisplayName("주간 조회수가 가장 높은 콘서트를 8개 반환한다") //FIXME ( 알림 개수 추가 필요 ) 
    public void get_WeekHot_Concert_Info() {
        //given

        //when

        //then

    }


    /**
     * 콘서트 페이지
     */
    @Test
    @DisplayName("콘서트 페이지에서 인기순을 눌렀을 경우 조회수가 가장 높은 공연 순서대로 20개씩 페이징 하여 반환한다")
    public void get_Concert_Hot () {
        //given
    
        //when
    
        //then
    
    }























    private Concert createDummyConcert(Long id) {
        return Concert.builder()
                .id(id)
                .concertName("Dummy Concert " + id)
                .concertRound(ConcertRound.FIRST)
                .concertPlace("Dummy Place " + id)
                .concertDate(LocalDate.now().plusDays(id.intValue()))
                .ticketOpenDate(LocalDateTime.now().plusDays(id.intValue()))
                .concertRuntime("2 hours")
                .price(10000 + (id.intValue() * 500))
                .concertAge(18)
                .viewingRestrict("None")
                .recommendRate(85)
                .onlineStore(OnlineStore.INTERPARK)
                .dailyViewCount(10 + id.intValue())
                .weeklyViewCount(50 + id.intValue())
                .advertisement(id % 2 == 0)
                .concertArtists(new ArrayList<>())
                .build();
    }

    private ConcertImage createDummyConcertImage(Long id, Concert concert) {
        return ConcertImage.builder()
                .id(id)
                .imageLink("https://dummy-image.com/image" + id + ".jpg")
                .fileName("dummy_image_" + id + ".jpg")
                .imageName("Dummy Image " + id)
                .concert(concert)
                .build();
    }
}
