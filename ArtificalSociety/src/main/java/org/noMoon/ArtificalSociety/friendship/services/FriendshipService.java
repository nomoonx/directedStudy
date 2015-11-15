package org.noMoon.ArtificalSociety.friendship.services;

import org.noMoon.ArtificalSociety.friendship.DO.Friendship;

import java.util.List;

/**
 * Created by noMoon on 2015-11-12.
 */
public interface FriendshipService {

    void insertNewFriendship(Friendship friendship);
    void listInsertNewFriendship(List<Friendship> friendshipList);

    void CreateEntireFriendshipNetwork();
}
