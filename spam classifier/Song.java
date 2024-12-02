import java.util.*;

// This interface represents a piece of data that can be classified by some arbitrary
// machine learning model
public class Song implements Classifiable {

    private static final Set<String> FEATURES = Set.of("danceability", "energy", "popularity");
    private Map<String, Double> featureVals;

    public Song(double danceability, double energy, double key, double loudness, 
            double speechiness, double instrumentalness) {
        this.featureVals = new HashMap<>();
        this.featureVals.put("danceability", danceability);
        this.featureVals.put("energy", energy);
        this.featureVals.put("loudness", loudness);
        this.featureVals.put("key", key);
        this.featureVals.put("speechiness", speechiness);
        this.featureVals.put("instrumentalness", instrumentalness);
    }
    
    
    // Returns the numeric value corresponding to the provided feature for this instance.
    // 'feature' should be non-null.
    public double get(String feature) {
        if (!FEATURES.contains(feature)) {
            throw new IllegalArgumentException();
        }
        if (featureVals.keySet().contains(feature)) {
            return featureVals.get(feature);
        }
        return 0.0;

    }
    
    // Returns a Set of all valid features for this datatype.
    public Set<String> getFeatures() {
        return FEATURES;
    }
    
    // Returns a Split (feature and threshold) that segments this instance and provided 'other'
    // 'other' should be non-null
    // Throws an IllegalArgumentException
    //      If other isn't an instanceof this datatype
    public Split partition(Classifiable other) {
        if (!(other instanceof Song)) {
            throw new IllegalArgumentException();
        }

        Song otherSong = (Song) other;

        String bestFeature = null;
        double highestDiff = 0;

        for (String feature : FEATURES) {
            double currDiff = Math.abs(get(feature) - otherSong.get(feature));
            if (currDiff >= highestDiff) {
                bestFeature = feature;
                highestDiff = currDiff;
            }
        }

        double halfway = Split.midpoint(get(bestFeature), otherSong.get(bestFeature));
        return new Split(bestFeature, halfway);
    }

    public static Classifiable toClassifiable(List<String> row) {
        double danceability = Double.parseDouble(row.get(11));
        double energy = Double.parseDouble(row.get(12));
        double loudness = Double.parseDouble(row.get(14));
        double key = Double.parseDouble(row.get(13));
        double speechiness = Double.parseDouble(row.get(16));
        double instrumentalness = Double.parseDouble(row.get(18));
        return new Song(danceability, energy, key, loudness, speechiness, instrumentalness);
    }
}

