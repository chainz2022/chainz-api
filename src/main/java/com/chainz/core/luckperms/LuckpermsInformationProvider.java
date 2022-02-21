package com.chainz.core.luckperms;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;

import java.util.Optional;
import java.util.UUID;

public class LuckpermsInformationProvider {
    private final LuckPerms luckPerms = LuckPermsProvider.get();

    public Optional<String> getPrefix(UUID uuid) {
        return Optional.ofNullable(getMetaData(uuid).getPrefix());
    }

    public Optional<String> getSuffix(UUID uuid) {
        return Optional.ofNullable(getMetaData(uuid).getSuffix());
    }

    private CachedMetaData getMetaData(UUID uuid) {
        User user = this.luckPerms.getUserManager().getUser(uuid);
        assert user != null;
        return user.getCachedData().getMetaData(this.luckPerms.getContextManager().getQueryOptions(user).orElse(QueryOptions.nonContextual()));
    }
}
