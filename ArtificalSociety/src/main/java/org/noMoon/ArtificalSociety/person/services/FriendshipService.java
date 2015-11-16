package org.noMoon.ArtificalSociety.person.services;

import org.noMoon.ArtificalSociety.person.DO.Friendship;

import java.util.List;

/**
 * Created by noMoon on 2015-11-12.
 */
public interface FriendshipService {

    void insertNewFriendship(Friendship friendship);
    void listInsertNewFriendship(List<Friendship> friendshipList);

    void CreateEntireFriendshipNetwork();
}
