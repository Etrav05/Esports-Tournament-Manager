package src.core;

public enum TournamentFormat {
    SINGLE_ELIM {
        @Override
        public int calculateRounds(int teams) {
            return (int) (Math.log(teams) / Math.log(2));
        }
    },

    DOUBLE_ELIM {
        @Override
        public int calculateRounds(int teams) {
            return (int) (Math.log(teams) / Math.log(2)) * 2;
        }
    },
    
    ROUND_ROBIN {
        @Override
        public int calculateRounds(int teams) {
            return teams - 1;
        }
    };

    public abstract int calculateRounds(int teams);
}
