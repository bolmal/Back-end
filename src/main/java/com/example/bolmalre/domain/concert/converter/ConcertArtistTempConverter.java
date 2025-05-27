package com.example.bolmalre.domain.concert.converter;

import com.example.bolmalre.domain.artist.domain.Artist;
import com.example.bolmalre.domain.concert.domain.Concert;
import com.example.bolmalre.domain.concert.domain.ConcertArtist;

public class ConcertArtistTempConverter {

    public static ConcertArtist toConcertArtist(Concert concert, Artist artist){
        return ConcertArtist.builder()
                .concert(concert)
                .artist(artist)
                .build();
    }
}
