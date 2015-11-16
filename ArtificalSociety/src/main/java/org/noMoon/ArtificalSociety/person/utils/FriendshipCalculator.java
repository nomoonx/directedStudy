package org.noMoon.ArtificalSociety.person.utils;

import org.noMoon.ArtificalSociety.commons.utils.Configuration;
import org.noMoon.ArtificalSociety.group.DTO.GroupDTO;
import org.noMoon.ArtificalSociety.person.DTO.PersonDTO;

/**
 * Created by noMoon on 2015-11-14.
 */
public class FriendshipCalculator {

    public static double CalculateFriendshipProbability(PersonDTO A, PersonDTO B, GroupDTO grp) {
        // This function calculates the likelihood of two Persons, A and B, becoming friends based on their involvement in the given Group, grp.
        // The group size is a reasonable term to include in the computation since smaller groups would be more friendship-prone than large groups.
        // Another possible factor to consider is the level of Intraversion/Extraversion of the persons A and/or B.
        // NOTE: This function is used in the SelectFriendships_Option_A() method.
        //
        // param A: one Person from whom to check for a potential friendship
        // param B: the Person who is being considered as a possible friend for A
        // param grp: the Group from which persons A and B are involved; attributes of the group can help decide the likelihood of the friendship
        //
        // return: the double representing the probabilitiy of persons A and B becoming friends from this particular group (not their overall probability).

        double f_prob;

        // Calculate as a fixed number divided by the group size.
        //f_prob = 0.4 / (double)grp.getNumMembers();
        f_prob = Configuration.FriendFactor / (double)grp.getNumMembers();


        // Scale probabilities with Intraversion/Extraversion value. We will only use person A's I/E value here, not person B's.
        double Extraversion = A.getPersonality()[0];
        // Multiply the I/E value by 0.5, and add it to 0.75, so the scaleFactor will be in {0.75, 1.0, 1.25}.
        double scaleFactor = Extraversion*0.5 + 0.75;


        // Scale the probability by the scaleFactor.
        f_prob = f_prob * scaleFactor;



        return f_prob;
    } // CalculateFriendshipProbability()
}
