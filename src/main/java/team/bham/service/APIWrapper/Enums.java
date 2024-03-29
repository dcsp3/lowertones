package team.bham.service.APIWrapper;

public class Enums {

    public enum SpotifyTimeRange {
        SHORT_TERM("short_term"),
        MEDIUM_TERM("medium_term"),
        LONG_TERM("long_term");

        public final String label;

        private SpotifyTimeRange(String label) {
            this.label = label;
        }
    }
}
