package com.chainz.core.playerlevel;

import com.chainz.core.async.reply.ExpReply;
import com.chainz.core.async.reply.LevelReply;

import java.util.UUID;

public interface PlayerLevel {
    void addExperience(UUID p0, Integer p1);

    Integer getExperience(UUID p0);

    Integer getLevel(UUID p0);

    Integer getExperienceToNextLevel(UUID p0);

    Integer getLevelPercent(UUID p0);

    void setLevel(UUID p0, Integer p1);

    LevelReply getLevelInfo(UUID uuid);

    ExpReply getExperienceInfo(UUID p0);
}
