package edu.sharif.ce.mir.clustering;

import edu.sharif.ce.mir.dal.data.Entity;
import edu.sharif.ce.mir.dal.datasource.ClusterEntity;
import edu.sharif.ce.mir.dal.impl.MySqlDataStorage;

import java.sql.SQLException;
import java.util.*;

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

        long n = vectors.size();
        System.out.println("N = " + n);
        int radicalN = (int) Math.sqrt(vectors.size() * 1.0);
        System.out.println("radical N = " + radicalN);
        long stopMaxClusterSize = (long) (n + Math.sqrt(n * 1.0)) / 2;
        System.out.println("stopping cluster size = " + stopMaxClusterSize);
        double stoppingDecAmount = SINGLE_STOP_DEC_AMOUNT * vectors.size();
        System.out.println("stopping decrease amount = " + stoppingDecAmount);

        Map<Vector, List<Vector>> clustering = new HashMap<Vector, List<Vector>>();
        Map<Vector, Integer> clusterIds = new HashMap<Vector, Integer>();
        List<Vector> centroids = null;

        // Repeat until having balanced Clustering.
        int algRepeat = 0;
        do {
            algRepeat++;
            if (algRepeat == 1) {
                // Initialize seeds.
                centroids = peekCentroids(vectors, radicalN);
                System.out.println("initial centroids:");
                for (Vector vector : centroids) {
                    System.out.println(vector.getId());
                }
            } else {
                centroids = getBalancedCentroids(clustering, radicalN);
            }

            // Do K-means algorithm.
            double lastRss = 0, rss = 0;
            double decAmount = 0;
            int iteration = 0;
            do {
                iteration++;
                if (iteration != 1) {
                    // Recomputation step
                    centroids = recomputeCentroids(clustering);
                }

                // Map id's to cluster centroids.
                int clusterId = 1;
                for (Vector centroid : centroids) {
                    clusterIds.put(centroid, clusterId);
                    clusterId += 1;
                }

                // Reassignment step
                clustering = reAssignVectors(vectors, centroids, clusterIds);

                // Calculate RSS.
                lastRss = rss;
                rss = calcRSS(clustering);
                if (iteration != 1) {
                    decAmount = lastRss - rss;
                    System.out.println("iteration " + iteration + " : " + lastRss + " - " + rss + " = " + decAmount);
                }
            } while (iteration == 1 || decAmount > stoppingDecAmount);

        } while (getMaxClusterSize(clustering) > stopMaxClusterSize);

        // Print clusters in the console.
        for (Vector centroid : clustering.keySet()) {
            System.out.print("cluster of id " + clusterIds.get(centroid) + " (" + clustering.get(centroid).size() + " song(s)):");
            for (Vector vector : clustering.get(centroid)) {
                System.out.print(vector.getId() + ", ");
            }
            System.out.println();
        }

        // Save clusters in the DB.
        saveClusters(vectors, storage);
    }

    private static List<Vector> peekCentroids(List<Vector> vectors, int centroidCount) {
        long clusterSize = vectors.size() / centroidCount;
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
//            System.out.print("song id " + vector.getId() + " : ");
            for (Vector centroid : centroids) {
                double dist = vector.getDistance(centroid);
//                System.out.print(clusterIds.get(centroid) + ":" + dist + ", ");
                if (dist < minDist) {
                    minDist = dist;
                    nearestCent = centroid;
                }
            }
//            System.out.println(" -> min = " + clusterIds.get(nearestCent));
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

//    private static double improvementAmount(List<Vector> oldCentroids, List<Vector> newCentroids) {
//        if (oldCentroids.size() != newCentroids.size()) {
//            throw new IllegalArgumentException("Sizes of oldCentroids and newCentroids are not equal.");
//        } else {
//            double sum = 0;
//            for (int i = 0; i < oldCentroids.size(); i++) {
//                sum += oldCentroids.get(i).getDistance(newCentroids.get(i));
//            }
//            return sum;
//        }
//    }

    private static double calcRSS(Map<Vector, List<Vector>> clustering) {
        double rss = 0;
        for (Vector centroid : clustering.keySet()) {
            for (Vector vector : clustering.get(centroid)) {
                rss += Math.pow(vector.getDistance(centroid), 2);
            }
        }
        return rss;
    }

    private static int getMaxClusterSize(Map<Vector, List<Vector>> clustering) {
        int maxSize = 0;
        for (List<Vector> vecList : clustering.values()) {
            if (vecList.size() > maxSize) {
                maxSize = vecList.size();
            }
        }
        return maxSize;
    }

    private static List<Vector> getBalancedCentroids(Map<Vector, List<Vector>> clustering, int radicalN) {
        int maxSize = 0;
        List<Vector> largest = null;
        for (List<Vector> vecList : clustering.values()) {
            if (vecList.size() > maxSize) {
                maxSize = vecList.size();
                largest = vecList;
            }
        }
        List<Vector> centroids = sortCentroids(clustering);
        System.out.print("sorted cluster sizes : ");
        for (Vector centroid : centroids) {
            System.out.print(clustering.get(centroid).size() + ", ");
        }
        // Remove centroid with smallest cluster size.
        int newCentroidsCount = maxSize / radicalN;
        System.out.print(" -> " + (newCentroidsCount - 1) + " centroids removed then : ");
        for (int i = 0; i < newCentroidsCount - 1; i++) {
            centroids.remove(0);
        }
        for (Vector centroid : centroids) {
            System.out.print(clustering.get(centroid).size() + ", ");
        }

        // Add some centroids instead of the centroid with largest cluster size.
        centroids.remove(largest);
        List<Vector> newCentroids = peekCentroids(largest, maxSize / radicalN);
        System.out.print("centroids added instead of the largest : ");
        for (Vector vector : newCentroids) {
            System.out.print(vector.getId() + ", ");
        }
        System.out.println();
        centroids.addAll(peekCentroids(largest, maxSize / radicalN));

        return centroids;
    }

    private static List<Vector> sortCentroids(final Map<Vector, List<Vector>> clustering) {
        List<Vector> centroids = new ArrayList<Vector>();
        centroids.addAll(clustering.keySet());
        Collections.sort(centroids, new Comparator<Vector>() {
            @Override
            public int compare(Vector o1, Vector o2) {
                int size1 = clustering.get(o1).size();
                int size2 = clustering.get(o2).size();
                if (size1 > size2) {
                    return 1;
                } else if (size1 < size2) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        return centroids;
    }

    private static void saveClusters(List<Vector> vectors, final MySqlDataStorage storage) throws SQLException {
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
}
