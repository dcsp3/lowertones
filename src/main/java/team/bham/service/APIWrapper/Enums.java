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

    public enum SpotifyReleasePrecision {
        DAY("day"),
        MONTH("month"),
        YEAR("year");

        public final String label;

        private SpotifyReleasePrecision(String label) {
            this.label = label;
        }
    }

    public enum SpotifySearchType {
        ALBUM("album"),
        ARTIST("artist"),
        PLAYLIST("playlist"),
        TRACK("track");

        public final String label;

        private SpotifySearchType(String label) {
            this.label = label;
        }
    }
}
