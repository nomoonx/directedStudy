package org.noMoon.ArtificalSociety.history.services;

import org.noMoon.ArtificalSociety.history.DTO.HistoryDTO;
import org.noMoon.ArtificalSociety.history.DTO.HometownHistoryDTO;
import org.noMoon.ArtificalSociety.history.DTO.SchoolHistoryDTO;
import org.noMoon.ArtificalSociety.history.DTO.WorkHistoryDTO;

import java.util.ArrayList;

/**
 * Created by noMoon on 2015-10-16.
 */
public interface HistoryService {
    Long insertNewHistoryDTO(HistoryDTO historyDTO);

    void addHometownsForPeriod(HometownHistoryDTO archive, HometownHistoryDTO localArchive, int startYear, int endYear, ArrayList<String> possibleCitiesForMoves);

    HometownHistoryDTO getHometownHistoryById(Long id);
    SchoolHistoryDTO getSchoolHistoryById(Long id);
    WorkHistoryDTO getWorkHistoryById(Long id);
}
