package com.project.musicwebbe.service.song;

import com.project.musicwebbe.entities.SongListen;
import com.project.musicwebbe.service.IGeneralService;

public interface ISongListenService extends IGeneralService<SongListen> {
    SongListen findBySongIdToday(Long songId);;

    Integer getTotalListenBySongId(Long songId);
}
