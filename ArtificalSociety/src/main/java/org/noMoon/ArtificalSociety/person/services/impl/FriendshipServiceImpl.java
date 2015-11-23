package org.noMoon.ArtificalSociety.person.services.impl;

import org.noMoon.ArtificalSociety.commons.utils.Configuration;
import org.noMoon.ArtificalSociety.commons.utils.Distribution;
import org.noMoon.ArtificalSociety.person.DAO.FriendshipMapper;
import org.noMoon.ArtificalSociety.person.DO.Friendship;
import org.noMoon.ArtificalSociety.person.Enums.FriendshipTypeEnum;
import org.noMoon.ArtificalSociety.person.services.FriendshipService;
import org.noMoon.ArtificalSociety.person.utils.FriendshipCalculator;
import org.noMoon.ArtificalSociety.group.DTO.GroupDTO;
import org.noMoon.ArtificalSociety.group.Services.GroupService;
import org.noMoon.ArtificalSociety.person.DTO.PersonDTO;
import org.noMoon.ArtificalSociety.person.services.PersonService;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by noMoon on 2015-11-12.
 */
public class FriendshipServiceImpl implements FriendshipService {

    private FriendshipMapper friendshipMapper;
    private PersonService personService;
    private GroupService groupService;

    public void CreateEntireFriendshipNetwork() {
        List<String> allPersonIds = personService.getAllIds(Configuration.Society_Id);
        for (String id : allPersonIds) {
            PersonDTO person = personService.selectPerosonDTOById(id);
            createFriendshipNetwork(person);
        }
    }

    private void createFamilyNetwork(PersonDTO person) {

        List<Friendship> newFriendList = new ArrayList<Friendship>();
        // ------------------------------
        // Spouse / Partner.
        // ------------------------------
        if (!StringUtils.isEmpty(person.getPartnerId())) {
            newFriendList.add(new Friendship(person.getSocietyId(), person.getId(), person.getPartnerId(), FriendshipTypeEnum.PARTNER, 0.0, "Spouse"));
            newFriendList.add(new Friendship(person.getSocietyId(), person.getPartnerId(), person.getId(), FriendshipTypeEnum.PARTNER, 0.0, "Spouse"));
        }

        // ------------------------------
        // Children.
        // ------------------------------
        if (person.getChildrenIds().size() > 1) {
            for (String childId : person.getChildrenIds()) {
                newFriendList.add(new Friendship(person.getSocietyId(), person.getId(), childId, FriendshipTypeEnum.PARTNER, 0.0, "Parent"));
                newFriendList.add(new Friendship(person.getSocietyId(), childId, person.getId(), FriendshipTypeEnum.PARTNER, 0.0, "Child"));
            } // end for i (loop through all children of this person)
        } // end if (person has children)


        // ------------------------------
        // Siblings.
        // ------------------------------
        if (person.getSiblingsIds().size() > 1) {
            for (String siblingId : person.getSiblingsIds()) {
                newFriendList.add(new Friendship(person.getSocietyId(), person.getId(), siblingId, FriendshipTypeEnum.PARTNER, 0.0, "Sibling"));
                newFriendList.add(new Friendship(person.getSocietyId(), siblingId, person.getId(), FriendshipTypeEnum.PARTNER, 0.0, "Sibling"));
            } // end for i (loop through all children of this person)
        } // end if (person has children)    }
    }

    public void createFriendshipNetwork(PersonDTO person) {

        HashMap<String, Double> friendshipProbMap = calculateFriendshipProbability(person);
        List<Friendship> newFriendList = new ArrayList<Friendship>();
        Friendship existingFriendQuery = new Friendship();
        existingFriendQuery.setPersonAId(person.getId());
        existingFriendQuery.setSocietyId(person.getSocietyId());
        List<Friendship> existingFriend = friendshipMapper.selectByUserOneId(existingFriendQuery);
        for (String friendId : friendshipProbMap.keySet()) {
            double p_Dbl = friendshipProbMap.get(friendId);

            // Generate a random number.
            double rnd = Distribution.uniform(0.0, 1.0);

            if (rnd <= p_Dbl) {
                boolean flag = true;
                for (Friendship friendship : existingFriend) {
                    if (friendship.getPersonBId().equals(friendId)) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    newFriendList.add(new Friendship(person.getSocietyId(), person.getId(), friendId, FriendshipTypeEnum.FRIEND, 0.0));
                    newFriendList.add(new Friendship(person.getSocietyId(), friendId, person.getId(), FriendshipTypeEnum.FRIEND, 0.0));
                }
            } // end if (random number is within threshold)
        }
        friendshipMapper.insertList(newFriendList);

    } // end createFriendshipNetwork()

    private HashMap<String, Double> calculateFriendshipProbability(PersonDTO person) {

        // Get array of Groups in which this person is involved.
        HashMap<Long, List<String>> groupMap = person.getGroupIds();
        HashMap<String, Double> friendProbability = new HashMap<String, Double>();

//        ArrayList<ArrayList<String>> personGroupIDs = person.getGroupIDs(); // List of group ids.
        for (long key : groupMap.keySet()) {
            GroupDTO group = groupService.getGroupDTOById(key);
            List<String> memberIdList = group.getMemberList();
            for (String memberId : memberIdList) {
                if (!memberId.equals(person.getId())) {
                    double probability = 0.0;
                    if (friendProbability.containsKey(memberId)) {
                        probability = friendProbability.get(memberId);
                    }
                    probability += FriendshipCalculator.CalculateFriendshipProbability(person, null, group);
                    friendProbability.put(memberId, probability);
                }
            }
        }
        return friendProbability;

    } // end SelectFriendships_Option_A()

    public void insertNewFriendship(Friendship friendship) {
        friendshipMapper.insert(friendship);
    }

    public void listInsertNewFriendship(List<Friendship> friendshipList) {

    }

    public void setFriendshipMapper(FriendshipMapper friendshipMapper) {
        this.friendshipMapper = friendshipMapper;
    }

    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }
}
