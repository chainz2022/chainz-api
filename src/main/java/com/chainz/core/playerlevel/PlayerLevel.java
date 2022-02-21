package com.chainz.core.playerlevel;

import com.chainz.core.async.reply.CallbackReply;

public interface PlayerLevel {
    void addeExperience(String p0, Integer p1);

    void addExperienceAsync(String p0, Integer p1, CallbackReply p2);

    Integer getExperience(String p0);

    void getExperienceAsync(String p0, CallbackReply p1);

    Integer getLevel(String p0);

    void getLevelAsync(String p0, CallbackReply p1);

    Integer getExperienceToNextLevel(String p0);

    void getExperienceToNextLevelAsync(String p0, CallbackReply p1);

    Integer getLevelPercent(String p0);

    void getLevelPercentAsync(String p0, CallbackReply p1);

    void setLevel(String p0, Integer p1);

    void setLevelAsync(String p0, Integer p1, CallbackReply p2);

    void getLevelInfoAsync(String p0, CallbackReply p1);

    void getExperienceInfoAsync(String p0, CallbackReply p1);
}
