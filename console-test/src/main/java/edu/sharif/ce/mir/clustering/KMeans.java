package edu.sharif.ce.mir.clustering;

import edu.sharif.ce.mir.dal.data.Entity;
import edu.sharif.ce.mir.dal.datasource.ClusterEntity;
import edu.sharif.ce.mir.dal.impl.MySqlDataStorage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Hossein
 * Date: 7/3/12
 * Time: 8:19 AM
 */
public class KMeans {
    private static final double SINGLE_STOP_DEC_AMOUNT = 1;

    public static void main(String[] args) throws SQLException {
        final MySqlDataStorage storage = new MySqlDataStorage("localhost", "musics", "musics", "1234");
        storage.connect();
        VectorManager vectorManager = new MyVectorManager(storage);
        List<Vector> vectors = null;
        vectors = vectorManager.getAllMusics();

//        for (Long termId : vectors.get(0).getList().keySet()) {
//            System.out.print(termId + ":" + vectors.get(0).getList().get(termId) + ", ");
//        }
//        System.out.println();
//        for (Long termId : vectors.get(10).getList().keySet()) {
//            System.out.print(termId + ":" + vectors.get(10).getList().get(termId) + ", ");
//        }
//        System.out.println();

//        HashMap<Long, Double> map = new HashMap<Long, Double>();
//        map.put(1l, 10.0);
//        map.put(3l, 5.0);
//        Vector vec1 = new Vector(1l, map, 0l);
//        map = new HashMap<Long, Double>();
//        map.put(2l, 4.0);
//        map.put(3l, 3.0);
//        Vector vec2 = new Vector(1l, map, 0l);
//
//        System.out.println(vec1.getDistance(vec2));
//        List<Vector> arrList = new ArrayList<Vector>();
//        arrList.add(vec1);
//        arrList.add(vec2);
//        Vector vec = Vector.calcAvg(arrList);
//        for (Long termId : vec.getList().keySet()) {
//            System.out.print(termId + ":" + vec.getList().get(termId) + ", ");
//        }
//        System.out.println();

        // Do K-means algorithm.
        Map<Vector, List<Vector>> clustering = new HashMap<Vector, List<Vector>>();
        Map<Vector, Integer> clusterIds = new HashMap<Vector, Integer>();
        List<Vector> centroids = null;
        double lastRss = 0, rss = 0;
        double decAmount = 0;
        double stoppingDecAmount = SINGLE_STOP_DEC_AMOUNT * vectors.size();
        System.out.print("stopping decrease amount = " + stoppingDecAmount);
        int iteration = 0;
        do {
            iteration++;
            if (iteration != 1) {
                // Recomputation step
                centroids = recomputeCentroids(clustering);
            } else {
                // Initialize seeds.
                centroids = getInitialCentroids(vectors);
                System.out.println("centroids:");
                for (Vector vector : centroids) {
                    System.out.println(vector.getId());
                }
            }

            // Map id's to cluster centroids.
            int clusterId = 1;
            for (Vector centroid : centroids) {
                clusterIds.put(centroid, clusterId);
                clusterId += 1;
            }

            // Reassignment step
            clustering = reAssignVectors(vectors, centroids, clusterIds);
//            decAmount = improvementAmount(centroids, newCentroids);

            // Calculate RSS.
            lastRss = rss;
            rss = calcRSS(clustering);
            if (iteration != 1) {
                decAmount = lastRss - rss;
                System.out.println("iteration " + iteration + " : " + lastRss + " - " + rss + " = " + decAmount);
            }
//            centroids = newCentroids;
        } while (iteration == 1 || decAmount > stoppingDecAmount);

        // Print clusters in the console.
        for (Vector centroid : clustering.keySet()) {
            System.out.println("cluster of id " + clusterIds.get(centroid) + ":");
            for (Vector vector : clustering.get(centroid)) {
                System.out.print(vector.getId() + ", ");
            }
            System.out.println();
        }

        // Save clusters in the DB.
        for (Vector vector : vectors) {
            Entity entity = new Entity(new ClusterEntity());
            entity.set("id", vector.getId());
            entity.set("group", vector.getClusterId());
            try {
                Entity foundEntity = storage.select(entity);
                // id found:
                storage.update(entity);
            } catch (SQLException e) {
                // id not found:
                storage.insert(entity);
            }
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

    private static Map<Vector, List<Vector>> reAssignVectors(List<Vector> vectors, List<Vector> centroids, Map<Vector, Integer> clusterIds) {
        Map<Vector, List<Vector>> clustering = new HashMap<Vector, List<Vector>>();
        for (Vector centroid : centroids) {
            clustering.put(centroid, new ArrayList<Vector>());
        }

        for (Vector vector : vectors) {
            Vector nearestCent = centroids.get(0);
            double minDist = vector.getDistance(nearestCent);
            System.out.print("song id " + vector.getId() + " : ");
            for (Vector centroid : centroids) {
                double dist = vector.getDistance(centroid);
                System.out.print(clusterIds.get(centroid) + ":" + dist + ", ");
                if (dist < minDist) {
                    minDist = dist;
                    nearestCent = centroid;
                }
            }
            System.out.println(" -> min = " + clusterIds.get(nearestCent));
            vector.setClusterId(clusterIds.get(nearestCent));
            clustering.get(nearestCent).add(vector);
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

    private static double calcRSS(Map<Vector, List<Vector>> clustering) {
        double rss = 0;
        for (Vector centroid : clustering.keySet()) {
            for (Vector vector : clustering.get(centroid)) {
                rss += Math.pow(vector.getDistance(centroid), 2);
            }
        }
        return rss;
    }
}
