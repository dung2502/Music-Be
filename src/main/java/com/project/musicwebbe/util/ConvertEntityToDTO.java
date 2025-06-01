package com.project.musicwebbe.util;

import com.project.musicwebbe.dto.albumDTO.*;
import com.project.musicwebbe.dto.artistDTO.*;
import com.project.musicwebbe.dto.commentDTO.*;
import com.project.musicwebbe.dto.favoriteDTO.FavoriteDTOT;
import com.project.musicwebbe.dto.paymentDTO.PaymentDTO;
import com.project.musicwebbe.dto.paymentDTO.VipPackageDTO;
import com.project.musicwebbe.dto.playlistDTO.*;
import com.project.musicwebbe.dto.songDTO.*;
import com.project.musicwebbe.dto.userDTO.UserDTO;
import com.project.musicwebbe.entities.*;
import com.project.musicwebbe.entities.permission.AppUser;
import com.project.musicwebbe.service.Comment.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConvertEntityToDTO {
    @Autowired
    private GetCurrentUser getCurrentUser;

    @Autowired
    private ICommentService commentService;

    public AlbumDTO convertToAlbumDTO(Album album) {
        AlbumDTO albumDTO = new AlbumDTO();
        albumDTO.setAlbumId(album.getAlbumId());
        albumDTO.setTitle(album.getTitle());
        albumDTO.setDateCreate(album.getDateCreate());
        albumDTO.setCoverImageUrl(album.getCoverImageUrl());
        albumDTO.setProvide(album.getProvide());
        if (album.getSongs() != null) {
            List<SongOfAlbumDTO> songOfAlbumDTOS = album.getSongs().stream()
                    .map(song -> SongOfAlbumDTO.builder()
                            .songId(song.getSongId())
                            .title(song.getTitle())
                            .dateCreate(song.getDateCreate())
                            .lyrics(song.getLyrics())
                            .songUrl(song.getSongUrl())
                            .duration(song.getDuration())
                            .coverImageUrl(song.getCoverImageUrl())
                            .artists(song.getArtists().stream()
                                    .map(artist -> ArtistOfSongDTO.builder()
                                            .artistId(artist.getArtistId())
                                            .artistName(artist.getArtistName())
                                            .build()).toList())
                            .build()).toList();
            albumDTO.setSongs(songOfAlbumDTOS);
        }

        if (album.getGenres() != null) {
            albumDTO.setGenres(album.getGenres());
        }

        if (album.getArtists() != null){
            List<ArtistOfAlbumDTO> artistOfAlbumDTOS = album.getArtists().stream()
                    .map(artist -> {
                        return ArtistOfAlbumDTO.builder()
                                .artistId(artist.getArtistId())
                                .artistName(artist.getArtistName())
                                .avatar(artist.getAvatar())
                                .build();
                    }).collect(Collectors.toList());
            albumDTO.setArtists(artistOfAlbumDTOS);
        }
        if (album.getFavoriteAlbums() != null) {
            List<FavoriteAlbumDTO> favoriteDTOS = album.getFavoriteAlbums().stream()
                    .map(favoriteAlbum -> FavoriteAlbumDTO.builder()
                            .favoriteAlbumId(favoriteAlbum.getFavoriteAlbumId())
                            .appUser(UserOfFavoriteAlbumDTO.builder()
                                    .userId(favoriteAlbum.getAppUser().getUserId())
                                    .email(favoriteAlbum.getAppUser().getEmail())
                                    .fullName(favoriteAlbum.getAppUser().getFullName())
                                    .userCode(favoriteAlbum.getAppUser().getUserCode()).build())
                            .addedAt(favoriteAlbum.getAddedAt())
                            .build()).toList();
            albumDTO.setFavorites(favoriteDTOS);

            AppUser appUser = getCurrentUser.getUser();
            if (appUser != null) {
                boolean userFavoriteStatus = album.getFavoriteAlbums().stream()
                        .anyMatch(favoriteAlbum -> favoriteAlbum.getAppUser().getUserId().equals(appUser.getUserId()));
                albumDTO.setUserFavoriteStatus(userFavoriteStatus);
            }
        }
        return albumDTO;
    }

    public SongDTO convertToSongDTO(Song song) {
        SongDTO songDTO = new SongDTO();
        songDTO.setSongId(song.getSongId());
        songDTO.setTitle(song.getTitle());
        songDTO.setDateCreate(song.getDateCreate());
        songDTO.setDuration(song.getDuration());
        songDTO.setCoverImageUrl(song.getCoverImageUrl());
        songDTO.setLyrics(song.getLyrics());
        songDTO.setSongUrl(song.getSongUrl());
        songDTO.setListens(song.getSongListens().stream().mapToInt(SongListen::getTotal).sum());
        if (song.getAlbum() != null) {
            AlbumOfSongDTO album = new AlbumOfSongDTO(song.getAlbum().getAlbumId(), song.getAlbum().getTitle());
            songDTO.setAlbum(album);
        }
        if (song.getArtists() != null) {

            List<ArtistOfSongDTO> artistOfSongDTOS = song.getArtists().stream()
                    .map(artist -> ArtistOfSongDTO.builder()
                            .artistId(artist.getArtistId())
                            .artistName(artist.getArtistName())
                            .avatar(artist.getAvatar())
                            .build())
                    .toList();
            songDTO.setArtists(artistOfSongDTOS);
        }
        if (song.getGenres() != null) {
            songDTO.setGenres(song.getGenres());
        }
        if (song.getSongListens() != null) {
            List<SongListenDTO> songListenDTOS = song.getSongListens().stream()
                    .map(songListen -> SongListenDTO.builder()
                            .listensId(songListen.getListensId())
                            .dateCreate(songListen.getDateCreate())
                            .total(songListen.getTotal())
                            .build()).toList();
            songDTO.setSongListens(songListenDTOS);
        }

        if (song.getFavorites() != null) {
            List<FavoriteDTO> favoriteDTOS = song.getFavorites().stream()
                    .map(favorite -> FavoriteDTO.builder()
                            .favoriteId(favorite.getFavoriteId())
                            .appUser(UserOfFavoriteDTO.builder()
                                    .userId(favorite.getAppUser().getUserId())
                                    .email(favorite.getAppUser().getEmail())
                                    .fullName(favorite.getAppUser().getFullName())
                                    .userCode(favorite.getAppUser().getUserCode()).build())
                            .addedAt(favorite.getAddedAt())
                            .build()).toList();
            songDTO.setFavorites(favoriteDTOS);

            AppUser appUser = getCurrentUser.getUser();
            if (appUser != null) {
                boolean userFavoriteStatus = song.getFavorites().stream()
                        .anyMatch(favorite -> favorite.getAppUser().getUserId().equals(appUser.getUserId()));
                songDTO.setUserFavoriteStatus(userFavoriteStatus);
            }
        }

        return songDTO;
    }

    public ArtistDTO convertToArtistDTO(Artist artist) {
        ArtistDTO artistDTO = new ArtistDTO();
        artistDTO.setArtistId(artist.getArtistId());
        artistDTO.setArtistName(artist.getArtistName());
        artistDTO.setAvatar(artist.getAvatar());
        artistDTO.setBiography(artist.getBiography());
        if (artist.getGenres() != null) {
            artistDTO.setGenres(artist.getGenres());
        }
        if (artist.getSongs() != null) {
            List<SongOfArtistDTO> songOfArtistDTOS = artist.getSongs().stream()
                    .map(song -> SongOfArtistDTO.builder()
                            .songId(song.getSongId())
                            .title(song.getTitle())
                            .dateCreate(song.getDateCreate())
                            .lyrics(song.getLyrics())
                            .songUrl(song.getSongUrl())
                            .duration(song.getDuration())
                            .coverImageUrl(song.getCoverImageUrl())
                            .artists(song.getArtists().stream()
                                    .map(artist1 -> ArtistOfSongDTO.builder()
                                            .artistId(artist1.getArtistId())
                                            .artistName(artist1.getArtistName())
                                            .build()).toList())
                            .build())
                    .toList();
            artistDTO.setSongs(songOfArtistDTOS);
        }
        if (artist.getAlbums() != null) {
            List<AlbumOfArtistDTO> albumOfArtistDTOS = artist.getAlbums().stream()
                    .map(album -> AlbumOfArtistDTO.builder()
                            .albumId(album.getAlbumId())
                            .title(album.getTitle())
                            .provide(album.getProvide())
                            .dateCreate(album.getDateCreate())
                            .coverImageUrl(album.getCoverImageUrl())
                            .build()).toList();
            artistDTO.setAlbums(albumOfArtistDTOS);
        }

        if (artist.getFavoriteArtists() != null) {
            List<FavoriteArtistDTO> favoriteDTOS = artist.getFavoriteArtists().stream()
                    .map(favoriteArtist -> FavoriteArtistDTO.builder()
                            .favoriteArtistId(favoriteArtist.getFavoriteArtistId())
                            .appUser(UserOfFavoriteArtistDTO.builder()
                                    .userId(favoriteArtist.getAppUser().getUserId())
                                    .email(favoriteArtist.getAppUser().getEmail())
                                    .fullName(favoriteArtist.getAppUser().getFullName())
                                    .userCode(favoriteArtist.getAppUser().getUserCode()).build())
                            .addedAt(favoriteArtist.getAddedAt())
                            .build()).toList();
            artistDTO.setFavorites(favoriteDTOS);

            AppUser appUser = getCurrentUser.getUser();
            if (appUser != null) {
                boolean userFavoriteStatus = artist.getFavoriteArtists().stream()
                        .anyMatch(favoriteArtist -> favoriteArtist.getAppUser().getUserId().equals(appUser.getUserId()));
                artistDTO.setUserFavoriteStatus(userFavoriteStatus);
            }
        }

        // Chuyển đổi các entity liên quan sang DTO tương ứng nếu cần
        return artistDTO;
    }

    public PlaylistDTO convertToPlaylistDTO(Playlist playlist) {
        PlaylistDTO playlistDTO = PlaylistDTO.builder()
                .playlistId(playlist.getPlaylistId())
                .playlistName(playlist.getPlaylistName())
                .playlistCode(playlist.getPlaylistCode())
                .coverImageUrl(playlist.getCoverImageUrl())
                .dateCreate(playlist.getDateCreate())
                .playlistStatus(playlist.isPlaylistStatus())
                .description(playlist.getDescription())
                .build();
        if (playlist.getPlayListSongs() != null) {
            List<SongOfPlaylistDTO> songOfPlaylistDTOS = playlist.getPlayListSongs().stream()
                    .map(playListSong -> {
                        Song song = playListSong.getSong();
                        Album album = song.getAlbum();

                        AlbumOfSongDTO albumDTO = null;
                        if (album != null) {
                            albumDTO = AlbumOfSongDTO.builder()
                                    .albumId(album.getAlbumId())
                                    .title(album.getTitle())
                                    .build();
                        }
                        List<ArtistOfSongDTO> artistOfSongDTOS = song.getArtists() != null
                                ? song.getArtists().stream()
                                .map(artist -> ArtistOfSongDTO.builder()
                                        .artistId(artist.getArtistId())
                                        .artistName(artist.getArtistName())
                                        .avatar(artist.getAvatar())
                                        .build())
                                .toList()
                                : List.of();
                        return SongOfPlaylistDTO.builder()
                                .id(playListSong.getId())
                                .songId(song.getSongId())
                                .title(song.getTitle())
                                .dateAdd(playListSong.getDateAdd())
                                .dateCreate(song.getDateCreate())
                                .lyrics(song.getLyrics())
                                .album(albumDTO)
                                .songUrl(song.getSongUrl())
                                .duration(song.getDuration())
                                .coverImageUrl(song.getCoverImageUrl())
                                .genres(song.getGenres())
                                .artists(artistOfSongDTOS)
                                .build();
                    })
                    .toList();
            playlistDTO.setSongOfPlaylist(songOfPlaylistDTOS);
        }

        if (playlist.getAppUser() != null) {
            UserOfPlaylistDTO userOfPlaylistDTO = UserOfPlaylistDTO.builder()
                    .userId(playlist.getAppUser().getUserId())
                    .fullName(playlist.getAppUser().getFullName())
                    .build();
            playlistDTO.setAppUser(userOfPlaylistDTO);
        }
        if (playlist.getFavoritePlaylists() != null) {
            List<FavoritePlaylistDTO> favoriteDTOS = playlist.getFavoritePlaylists().stream()
                    .map(favoritePlaylistDTO -> FavoritePlaylistDTO.builder()
                            .favoritePlaylistId(favoritePlaylistDTO.getFavoritePlaylistId())
                            .appUser(UserOfFavoritePlaylistDTO.builder()
                                    .userId(favoritePlaylistDTO.getAppUser().getUserId())
                                    .email(favoritePlaylistDTO.getAppUser().getEmail())
                                    .fullName(favoritePlaylistDTO.getAppUser().getFullName())
                                    .userCode(favoritePlaylistDTO.getAppUser().getUserCode()).build())
                            .addedAt(favoritePlaylistDTO.getAddedAt())
                            .build()).toList();
            playlistDTO.setFavorites(favoriteDTOS);

            AppUser appUser = getCurrentUser.getUser();
            if (appUser != null) {
                boolean userFavoriteStatus = playlist.getFavoritePlaylists().stream()
                        .anyMatch(favoritePlaylist -> favoritePlaylist.getAppUser().getUserId().equals(appUser.getUserId()));
                playlistDTO.setUserFavoriteStatus(userFavoriteStatus);
            }
        }

        return playlistDTO;
    }

    public UserDTO convertToUserDTO(AppUser appUser) {
        if (appUser == null) return null;

        return UserDTO.builder()
                .userId(appUser.getUserId())
                .email(appUser.getEmail())
                .userCode(appUser.getUserCode())
                .dateCreate(appUser.getDateCreate())
                .fullName(appUser.getFullName())
                .gender(appUser.getGender())
                .dateOfBirth(appUser.getDateOfBirth())
                .avatar(appUser.getAvatar())
                .phoneNumber(appUser.getPhoneNumber())
                .address(appUser.getAddress())
                .isVip(appUser.getIsVip())
                .vipExpiredDate(appUser.getVipExpiredDate())
                .build();
    }

    public UserListenDTO convertUserListenDTO(UserListen userListen) {
        if (userListen == null) return null;

        return UserListenDTO.builder()
                .listensId(userListen.getListensId())
                .listenedAt(userListen.getListenedAt())
                .user(SimpleUserDTO.builder()
                        .userId(userListen.getAppUser().getUserId())
                        .fullName(userListen.getAppUser().getFullName())
                        .build())
                .song(convertToSongDTO(userListen.getSong()))
                .build();
    }

    public PaymentDTO convertToPaymentDTO(Payment payment) {
        if (payment == null) return null;

        VipPackageDTO vipPackageDTO = null;
        if (payment.getVipPackage() != null) {
            vipPackageDTO = VipPackageDTO.builder()
                    .packageId(payment.getVipPackage().getPackageId())
                    .name(payment.getVipPackage().getName())
                    .durationMonths(payment.getVipPackage().getDurationMonths())
                    .price(payment.getVipPackage().getPrice())
                    .build();
        }

        return PaymentDTO.builder()
                .paymentId(payment.getPaymentId())
                .userId(payment.getAppUser() != null ? payment.getAppUser().getUserId() : null)
                .fullName(payment.getAppUser() != null ? payment.getAppUser().getFullName() : null)
                .amount(payment.getAmount())
                .paymentTime(payment.getPaymentTime())
                .paymentMethod(payment.getPaymentMethod())
                .transactionCode(payment.getTransactionCode())
                .paymentStatus(payment.getPaymentStatus())
                .description(payment.getDescription())
                .vipPackage(vipPackageDTO)
                .build();
    }

    public FavoriteDTOT convertToFavoriteDTO(Favorite favorite) {
        if (favorite == null) return null;

        return FavoriteDTOT.builder()
                .favoriteId(favorite.getFavoriteId())
                .addedAt(favorite.getAddedAt())
                .userFav(SimpleUserDTO.builder()
                        .userId(favorite.getAppUser().getUserId())
                        .fullName(favorite.getAppUser().getFullName())
                        .build())
                .favoriteSong(convertToSongDTO(favorite.getSong()))
                .build();
    }


    private CommentDTO convertToCommentDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setCommentId(comment.getCommentId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setCreatedAt(comment.getCreatedAt());
        ;
        if (comment.getUser() != null) {
            commentDTO.setUser(UserOfCommentDTO.builder()
                    .userId(comment.getUser().getUserId())
                    .email(comment.getUser().getEmail())
                    .fullName(comment.getUser().getFullName())
                    .avatar(comment.getUser().getAvatar())
                    .userCode(comment.getUser().getUserCode())
                    .build());
        }

        if (comment.getSong() != null) {
            commentDTO.setSong(SongOfCommentDTO.builder()
                    .songId(comment.getSong().getSongId())
                    .songUrl(comment.getSong().getSongUrl())
                    .title(comment.getSong().getTitle())
                    .dateCreate(comment.getSong().getDateCreate())
                    .lyrics(comment.getSong().getLyrics())
                    .coverImageUrl(comment.getSong().getCoverImageUrl())
                    .duration(comment.getSong().getDuration())
                    .build());
        }

        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt")); // Số lượng replies trên mỗi trang
        Page<Comment> repliesPage = commentService.findByParentCommentId(comment.getCommentId(), pageable);

        if (repliesPage != null && !repliesPage.isEmpty()) {
            Page<CommentDTO> replies = repliesPage.map(this::convertToCommentDTO);
            commentDTO.setReplies(replies);
        }

        if (comment.getLikes() != null && !comment.getLikes().isEmpty()) {
            List<CommentLikeDTO> likes = comment.getLikes().stream()
                    .map(commentLike -> CommentLikeDTO.builder()
                            .likeId(commentLike.getEmotionId())
                            .user(UserOfCommentDTO.builder()
                                    .userId(commentLike.getUser().getUserId())
                                    .email(commentLike.getUser().getEmail())
                                    .fullName(commentLike.getUser().getFullName())
                                    .avatar(commentLike.getUser().getAvatar())
                                    .userCode(commentLike.getUser().getUserCode())
                                    .build())
                            .likedAt(commentLike.getCreatedAt())
                            .build()).toList();
            commentDTO.setLikes(likes);
        }

        if (comment.getDislikes() != null && !comment.getDislikes().isEmpty()) {
            List<CommentDislikeDTO> dislikes = comment.getDislikes().stream()
                    .map(commentDislike -> CommentDislikeDTO.builder()
                            .dislikeId(commentDislike.getEmotionId())
                            .user(UserOfCommentDTO.builder()
                                    .userId(commentDislike.getUser().getUserId())
                                    .email(commentDislike.getUser().getEmail())
                                    .fullName(commentDislike.getUser().getFullName())
                                    .avatar(commentDislike.getUser().getAvatar())
                                    .userCode(commentDislike.getUser().getUserCode())
                                    .build())
                            .dislikedAt(commentDislike.getCreatedAt())
                            .build()).toList();
            commentDTO.setDislikes(dislikes);
        }

        if (comment.getHahas() != null && !comment.getHahas().isEmpty()) {
            List<CommentHahaDTO> hahaDTOS = comment.getHahas().stream()
                    .map(commentHaha -> CommentHahaDTO.builder()
                            .hahaId(commentHaha.getEmotionId())
                            .user(UserOfCommentDTO.builder()
                                    .userId(commentHaha.getUser().getUserId())
                                    .email(commentHaha.getUser().getEmail())
                                    .fullName(commentHaha.getUser().getFullName())
                                    .avatar(commentHaha.getUser().getAvatar())
                                    .userCode(commentHaha.getUser().getUserCode())
                                    .build())
                            .hahaAt(commentHaha.getCreatedAt())
                            .build()).toList();
            commentDTO.setHahas(hahaDTOS);
        }

        if (comment.getWows() != null && !comment.getWows().isEmpty()) {
            List<CommentWowDTO> wowDTOS = comment.getWows().stream()
                    .map(commentWow -> CommentWowDTO.builder()
                            .wowId(commentWow.getEmotionId())
                            .user(UserOfCommentDTO.builder()
                                    .userId(commentWow.getUser().getUserId())
                                    .email(commentWow.getUser().getEmail())
                                    .fullName(commentWow.getUser().getFullName())
                                    .avatar(commentWow.getUser().getAvatar())
                                    .userCode(commentWow.getUser().getUserCode())
                                    .build())
                            .wowAt(commentWow.getCreatedAt())
                            .build()).toList();
            commentDTO.setWows(wowDTOS);
        }

        if (comment.getHearts() != null && !comment.getHearts().isEmpty()) {
            List<CommentHeartDTO> dislikes = comment.getHearts().stream()
                    .map(commentHeart -> CommentHeartDTO.builder()
                            .heartId(commentHeart.getEmotionId())
                            .user(UserOfCommentDTO.builder()
                                    .userId(commentHeart.getUser().getUserId())
                                    .email(commentHeart.getUser().getEmail())
                                    .fullName(commentHeart.getUser().getFullName())
                                    .avatar(commentHeart.getUser().getAvatar())
                                    .userCode(commentHeart.getUser().getUserCode())
                                    .build())
                            .heartedAt(commentHeart.getCreatedAt())
                            .build()).toList();
            commentDTO.setHearts(dislikes);
        }
        return commentDTO;
    }



}
