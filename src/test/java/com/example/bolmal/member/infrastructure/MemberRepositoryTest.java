package com.example.bolmal.member.infrastructure;

import com.example.bolmal.member.infrastructure.entity.MemberEntity;
import com.example.bolmal.member.infrastructure.repository.MemberJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest(showSql = true)
@Sql("/sql/user-repository-test-data.sql")
public class MemberRepositoryTest {

    /**
     * repository층의 테스트코드는 우선순위도 낮고 구현난이도도 어렵지 않다고 판단
     *
     * 무엇보다 H2를 써야하기 때문에 전체적인 테스트 속도 저하의 원인이 될 수 있으므로 추후 커버리지를 위해 구현
     * */

    @Autowired
    private MemberJpaRepository memberRepository;



    @Test
    @DisplayName("findByUsername()을 통해 Username과 일치하는 데이터를 가져올 수 있다")
    public void title(){
        //given
        //when
        MemberEntity test = memberRepository.findByUsername("testtest1")
                .orElseThrow(()->new IllegalArgumentException("회원 SQL 작성이 잘못됨"));

        //then
        assertThat(test.getId()).isEqualTo(1L);
    }
}
