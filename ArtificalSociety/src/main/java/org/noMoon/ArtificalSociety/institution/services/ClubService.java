package org.noMoon.ArtificalSociety.institution.services;

import org.noMoon.ArtificalSociety.institution.DO.Club;
import org.noMoon.ArtificalSociety.institution.DTO.ClubDTO;

import java.util.List;

/**
 * Created by noMoon on 2015-09-08.
 */
public interface ClubService {
    void insertClubDTO(ClubDTO clubDTO);

    ClubDTO selectClubDTOByPk(Long id);

    void loadClubsFromFile(String filePath,String societyId);

    List<Club> selectClubBySocietyId(String societyId);
}
