package com.example.bolmalre.common.auth.infrastructure.port;

import com.example.bolmalre.common.auth.domain.Refresh;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String refresh;

    private String expiration;




    public static RefreshEntity fromModel(Refresh refresh) {

        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.id = refresh.getId();
        refreshEntity.username = refresh.getUsername();
        refreshEntity.refresh = refresh.getRefresh();
        refreshEntity.expiration = refresh.getExpiration();

        return refreshEntity;
    }


    public Refresh toModel() {
        return Refresh.builder()
                .id(id)
                .username(username)
                .refresh(refresh)
                .expiration(expiration)
                .build();
    }
}
