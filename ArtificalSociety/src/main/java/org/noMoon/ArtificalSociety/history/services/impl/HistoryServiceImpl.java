package org.noMoon.ArtificalSociety.history.services.impl;

import org.noMoon.ArtificalSociety.commons.utils.Configuration;
import org.noMoon.ArtificalSociety.commons.utils.Distribution;
import org.noMoon.ArtificalSociety.history.DAO.HistoryMapper;
import org.noMoon.ArtificalSociety.history.DO.History;
import org.noMoon.ArtificalSociety.history.DTO.HistoryDTO;
import org.noMoon.ArtificalSociety.history.DTO.HometownHistoryDTO;
import org.noMoon.ArtificalSociety.history.DTO.SchoolHistoryDTO;
import org.noMoon.ArtificalSociety.history.DTO.WorkHistoryDTO;
import org.noMoon.ArtificalSociety.history.Records.HistoryRecord;
import org.noMoon.ArtificalSociety.history.services.HistoryService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noMoon on 2015-10-16.
 */
public class HistoryServiceImpl implements HistoryService {

    HistoryMapper historyMapper;

    public Long insertNewHistoryDTO(HistoryDTO historyDTO) {
        History insertDO = historyDTO.convertToHistory();
        historyMapper.insert(insertDO);
        historyDTO.setId(insertDO.getId());
        return insertDO.getId();
    }

    public void addHometownsForPeriod(HometownHistoryDTO archive, HometownHistoryDTO localArchive, int startYear, int endYear, List<String> possibleCitiesForMoves) {
        //public static void addHometownsForPeriod (ActivityArchive archive, ActivityArchive localArchive, int minNumMoves, int maxNumMoves, int startYear, int endYear, String[] possibleCitiesForMoves) {
        // This function is important for generating a random sequence of hometowns for a person, based on a variety of probabilities and other parameters.
        // To begin, if the prevHometown value is part of the possibleCitiesForMoves array of locations, then that prevHometown has a probability of continuing
        // to be the person's hometown, based on the probability p_sameAsPrevious. If, however, the prevHometown is not a valid option (an example of this
        // is if the person needs to live in a city with a university but the prevHometown does not have a university, then the person cannot stay there),
        // then the person definitely will move. In the case of a person moving, random locations and time periods are generated based on the parameters
        // minNumMoves, maxNumMoves, startYear, and endYear. NOTE that the location generation is uniform, in that there are equal probabilities of choosing
        // any of the cities. In the case of staying in the prevHometown, nothing needs to be done. All hometown information is added to the ActivityArchive
        // parameter archive, and all hometowns in the simulation's society are recorded also in the localArchive parameter.
        //
        // param archive: the ActivityArchive for all activity
        // param localArchive: the ActivityArchive for activity that occurs locally, in the society of focus
        // param minNumMoves: the minimum number of moves the person will make during the given period
        // param maxNumMoves: the maximum number of moves the person will make during the given period
        // param startYear: the starting year for the residence activity to take place
        // param endYear: the final year for the residence activity to take place
        // param possibleCitiesForMoves: the array of possible locations that person can live in for the given period

        double rndSameHometown = Distribution.uniform(0.0, 1.0);

        // ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // * NOTE * This ensures that the person only stays in their current hometown if that city is a possible location for them at this time; and if the random number allows it.
        // ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


        // Multiple hometowns throughout this period.
        //int numMoves = Distribution.uniform(minNumMoves, maxNumMoves); 				// Should be Config parameters.
        //int numMoves = Configuration.DetermineNumMoves(endYear-startYear);
        int numMoves = Math.round((endYear - startYear) / Configuration.numAvgYearsBetweenMoves);

        int y;
        int rndYear;
        int prevMove = startYear;
        String moveCity;

        if (numMoves == 0) {
            // IF ONE HOMETOWN OVER ENTIRE PERIOD.

            ArrayList<String> hometownLocations = Distribution.permutation(Configuration.OtherCities, 1);
            moveCity = (String) hometownLocations.get(0);
            archive.getRecordList().add(new HistoryRecord(moveCity, startYear, endYear));
            if (moveCity.equals(Configuration.SocietyName)) {
                localArchive.getRecordList().add(new HistoryRecord(moveCity, startYear, endYear));
            } // end if (check if in society)
        } else {
            // IF MULTIPLE HOMETOWNS OVER PERIOD.

            // Create array of hometown locations. This will include the local society and numMoves-1 randomly chosen locations.
            ArrayList<String> hometownLocations = Distribution.permutation(Configuration.OtherCities, numMoves - 1);    // First get numMoves-1 random locations from outside of the society.
            hometownLocations.add(Configuration.SocietyName);                                    // Then add society to ensure it is included somewhere (currently at end). If we didn't add it separately, there would be a chance that the society would get omitted from the final list.
            //DebugTools.printArray(hometownLocations.toArray());
            hometownLocations = (ArrayList<String>) Distribution.permutation(hometownLocations);                    // Shuffle up list again, this time with society in the list.
            //System.out.println("\t~~~~~\t");
            //DebugTools.printArray(hometownLocations.toArray());
            //System.out.println("........................................................");

            int gapSupporter;

            // Loop through to create all hometowns except final one.
            for (y = 0; y < numMoves - 1; y++) {

                // Choose random location for move.
                //moveCity = (String)Distribution.uniformRandomObject(possibleCitiesForMoves);
                moveCity = (String) hometownLocations.get(y);        // Get locations from shuffled list.


                // The gapSupporter helps the moves to be spaced out so that they can't be all clustered together.
                gapSupporter = (numMoves - y - 1) * Configuration.numAvgYearsBetweenMoves;

                //System.err.println("Remaining moves: " + (numMoves-y) + " | " + gapSupporter + " || <" + prevMove + "," + (endYear-gapSupporter) + ">");

                // Choose random year for move.
                //rndYear = Distribution.uniform(prevMove, endYear);

                do {
                    // Ensure that the person does not move too frequently. Choose a new move year if it is too close to the last move.
                    rndYear = Distribution.uniform(prevMove, endYear - gapSupporter); // Subtract 1 from endYear because we don't want the person to move more than once in the same year.
                }
                while ((rndYear - prevMove) < Configuration.minYearsBetweenMoves); // end do-while (ensure person doesn't move too many times within a short amount of time)

                //System.err.println("Remaining moves: " + (numMoves-y) + " | " + gapSupporter + " || <" + prevMove + "," + (endYear-gapSupporter) + "> --- " + rndYear);

                //System.out.println("random number in [" + prevMove + "," + (endYear-gapSupporter) + "] --> " + rndYear);

                //System.out.println("Generating move year for " + startYear + ", " + endYear + " || " + rndYear);

                // Add hometown to archive.
                archive.getRecordList().add(new HistoryRecord(moveCity, startYear, endYear));
                // If in the local society, then add to local archive.
                if (moveCity.equals(Configuration.SocietyName)) {
                    localArchive.getRecordList().add(new HistoryRecord(moveCity, startYear, endYear));
                } // end if (check if in society)

                // Update previous move.
                prevMove = rndYear;
            } // end for y (number of moves)


            // Lastly, add the final, current hometown to the archive.

            // Choose random location for move.
            //moveCity = (String)Distribution.uniformRandomObject(possibleCitiesForMoves);
            moveCity = (String) hometownLocations.get(numMoves - 1);        // Get last location from shuffled list.

            // Add hometown to archive.
            archive.getRecordList().add(new HistoryRecord(moveCity, startYear, endYear));   // This endYear must be set in the final archive entry. This is why we can't include this in the above loop.
            // If in the local society, then add to local archive.
            if (moveCity.equals(Configuration.SocietyName)) {
                localArchive.getRecordList().add(new HistoryRecord(moveCity, startYear, endYear));
            } // end if (check if in society)

        } // end if (determine whether or not the person has multiple hometowns over the period)

        //DebugTools.printArray(archive);

    } // end addHometownsForPeriod()

    public HometownHistoryDTO getHometownHistoryById(Long id) {
        History history=historyMapper.selectByPrimaryKey(id);
        return new HometownHistoryDTO(history);
    }

    public SchoolHistoryDTO getSchoolHistoryById(Long id) {
        History history=historyMapper.selectByPrimaryKey(id);
        return new SchoolHistoryDTO(history);
    }

    public WorkHistoryDTO getWorkHistoryById(Long id) {
        History history=historyMapper.selectByPrimaryKey(id);
        return new WorkHistoryDTO(history);
    }

    public void updateHistoryDTO(HistoryDTO historyDTO) {
        historyMapper.updateByPrimaryKeyWithBLOBs(historyDTO.convertToHistory());
    }

    public void setHistoryMapper(HistoryMapper historyMapper) {
        this.historyMapper = historyMapper;
    }
}
