package com.example.bolmalre.concert.converter;

import com.example.bolmalre.artist.domain.Artist;
import com.example.bolmalre.concert.domain.Concert;
import com.example.bolmalre.concert.domain.ConcertArtist;

public class ConcertArtistTempConverter {

    public static ConcertArtist toConcertArtist(Concert concert, Artist artist){
        return ConcertArtist.builder()
                .concert(concert)
                .artist(artist)
                .build();
    }
}
