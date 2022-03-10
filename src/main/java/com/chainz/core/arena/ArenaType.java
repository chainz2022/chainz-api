package com.chainz.core.arena;

public enum ArenaType {
    THEBRIDGE_1, THEBRIDGE_2, THEBRIDGE_4, THEBRIDGE, SKYWARS, UNKNOWN;

    public String getName() {
        return switch (this) {
            case SKYWARS -> "SkyWars";
            case THEBRIDGE -> "TheBridge";
            case THEBRIDGE_1 -> "TheBridge Singles";
            case THEBRIDGE_2 -> "TheBridge Doubles";
            case THEBRIDGE_4 -> "TheBridge Squads";
            case UNKNOWN -> "Unknown";
        };
    }

    public static ArenaType parseType(String s, int teamSize) {
        try {
            return ArenaType.valueOf(s);
        } catch (IllegalArgumentException ignored) {
            if (s.contains("thebridge")) {
                return switch (teamSize) {
                    case 1 -> THEBRIDGE_1;
                    case 2 -> THEBRIDGE_2;
                    case 4 -> THEBRIDGE_4;
                    default -> THEBRIDGE;
                };
            }
        }
        return UNKNOWN;
    }
}
