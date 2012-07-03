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
 * User: Administrator
 * Date: 7/3/12
 * Time: 8:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class KMeans {
    private static final double MIN_IMPROVE_AMOUNT = 2;

    public static void main(String[] args) throws SQLException {
        final MySqlDataStorage storage = new MySqlDataStorage("localhost", "musics", "musics", "1234");
        storage.connect();
        VectorManager vectorManager = new MyVectorManager(storage);
        List<Vector> vectors = null;
        try {
            vectors = vectorManager.getAllMusics();
        } catch (SQLException e) {
            e.printStackTrace();
        }


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

        List<Vector> centroids = getInitialCentroids(vectors);
        System.out.println("centroids:");
        for (Vector vector : centroids) {
            System.out.println(vector.getId());
        }

        double impAmount = 0;
        Map<Vector, List<Vector>> clustering;
        Map<Vector, Integer> clusterIds = new HashMap<Vector, Integer>();
        int iteration = 1;
        do {
            int clusterId = 1;
            for (Vector centroid : centroids) {
                clusterIds.put(centroid, clusterId);
                clusterId += 1;
            }

            clustering = reAssignVectors(vectors, centroids, clusterIds);
            List<Vector> newCentroids = recomputeCentroids(clustering);
            impAmount = improvementAmount(centroids, newCentroids);
            System.out.println("improvement amount for iteration " + iteration + " = " + impAmount);
            centroids = newCentroids;
            iteration++;
        } while (impAmount > Math.sqrt(vectors.size()) * MIN_IMPROVE_AMOUNT);

        for (Vector vector : vectors) {
            Entity entity = new Entity(new ClusterEntity());
            entity.set("id", vector.getId());
            entity.set("group", vector.getClusterId());
        }

        for (Vector centroid : clustering.keySet()) {
            System.out.println("cluster of id " + clusterIds.get(centroid) + ":");
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

    private static Map<Vector, List<Vector>> reAssignVectors(List<Vector> vectors, List<Vector> centroids, Map<Vector, Integer> clusterIds) {
        Map<Vector, List<Vector>> clustering = new HashMap<Vector, List<Vector>>();
        for (Vector centroid : centroids) {
            clustering.put(centroid, new ArrayList<Vector>());
        }

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
}
