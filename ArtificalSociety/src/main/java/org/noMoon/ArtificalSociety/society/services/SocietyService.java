package org.noMoon.ArtificalSociety.society.services;

import org.noMoon.ArtificalSociety.society.DO.Society;

/**
 * Created by noMoon on 2015-08-21.
 */
public interface SocietyService {

    String insertNewSociety(Society society);

    String getCurrentSocietyId();
}
