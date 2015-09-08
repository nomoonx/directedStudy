package org.noMoon.ArtificalSociety.institution.services.impl;

import org.noMoon.ArtificalSociety.institution.DAO.ClubMapper;
import org.noMoon.ArtificalSociety.institution.DTO.ClubDTO;
import org.noMoon.ArtificalSociety.institution.services.ClubService;

/**
 * Created by noMoon on 2015-09-08.
 */
public class ClubServiceImpl implements ClubService {

    ClubMapper clubMapper;



    public void insertClubDTO(ClubDTO clubDTO) {
        clubMapper.insertSelective(clubDTO.convertToClub());
    }

    public void setClubMapper(ClubMapper clubMapper) {
        this.clubMapper = clubMapper;
    }
}
