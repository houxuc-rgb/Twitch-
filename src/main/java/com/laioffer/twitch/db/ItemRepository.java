package com.laioffer.twitch.db;


import com.laioffer.twitch.db.entity.ItemEntity;
import org.springframework.data.repository.ListCrudRepository;


public interface ItemRepository extends ListCrudRepository<ItemEntity, Long> {

    //select * from items where twitch id = ?
    ItemEntity findByTwitchId(String twitchId);

    ItemEntity findByBroadcasterNameStartingWith(String broadcasterName);
}
