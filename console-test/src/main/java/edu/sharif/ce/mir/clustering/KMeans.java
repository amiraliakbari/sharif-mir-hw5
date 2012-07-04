package edu.sharif.ce.mir.clustering;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 7/3/12
 * Time: 8:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class KMeans {
    private static final double MIN_IMPROVE_AMOUNT = 10;

    public static void main(String[] args) {
        VectorManager vectorManager = new VectorManagerImpl();
        List<Vector> vectors = null;
        try {
            vectors = vectorManager.getAllMusics();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        List<Vector> centroids = getInitialCentroids(vectors);
        System.out.println("centroids:");
        for (Vector vector : centroids) {
            System.out.println(vector.getId());
        }

        double impAmount = 0;
        Map<Vector, List<Vector>> clustering;
        do {
            clustering = reAssignVectors(vectors, centroids);
            List<Vector> newCentroids = recomputeCentroids(clustering);
            impAmount = improvementAmount(centroids, newCentroids);
            centroids = newCentroids;
        } while (impAmount > MIN_IMPROVE_AMOUNT);

        //TODO: save centroids into DB.

        for (Vector centroid : clustering.keySet()) {
            System.out.println("cluster of id " + centroid.getId() + " : ");
            for (Vector vector : clustering.get(centroid)) {
                System.out.print(vector.getId() + ", ");
            }
            System.out.println();
        }
    }

    private static List<Vector> getInitialCentroids(List<Vector> vectors) {
        long clusterSize = (long) Math.sqrt(vectors.size() * 1.0);
        System.out.println("cluster size = " + clusterSize);
        long counter = 0;
        List<Vector> centroids = new ArrayList<Vector>();
        for (Vector vector : vectors) {
            counter++;
            if (counter == clusterSize) {
                centroids.add(vector);
                counter = 0;
            }
        }
        return centroids;
    }

    private static Map<Vector, List<Vector>> reAssignVectors(List<Vector> vectors, List<Vector> centroids) {
        Map<Vector, List<Vector>> clustering = new HashMap<Vector, List<Vector>>();
        for (Vector vector : vectors) {
            double minDist = 0;
//            long nearest = centroids.get(0).getId();
            Vector nearestCent = centroids.get(0);
            for (Vector centroid : centroids) {
                double dist = vector.getDistance(centroid);
                if (dist < minDist) {
                    minDist = dist;
//                    nearest = centroid.getId();
                }
            }
//            vector.setCentroidId(nearest);
            if (!clustering.containsKey(nearestCent)) {
                List<Vector> cluster = new ArrayList<Vector>();
                cluster.add(vector);
                clustering.put(nearestCent, cluster);
            } else {
                clustering.get(nearestCent).add(vector);
            }
        }
        return clustering;
    }

    private static List<Vector> recomputeCentroids(Map<Vector, List<Vector>> clustering) {
        List<Vector> newCentroids = new ArrayList<Vector>();
        for (Vector centroid : clustering.keySet()) {
            Vector newCentroid = Vector.calcAvg(clustering.get(centroid));
            newCentroids.add(newCentroid);
        }
        return newCentroids;
    }

    private static double improvementAmount(List<Vector> oldCentroids, List<Vector> newCentroids) {
        if (oldCentroids.size() != newCentroids.size()) {
            throw new IllegalArgumentException("Sizes of oldCentroids and newCentroids are not equal.");
        } else {
            double sum = 0;
            for (int i = 0; i < oldCentroids.size(); i++) {
                sum += oldCentroids.get(i).getDistance(newCentroids.get(i));
            }
            return sum;
        }
    }
}
