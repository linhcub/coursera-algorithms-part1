import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {
    private SET<Point2D> pointSet;

    /**
     * Construct an empty set of points.
     */
    public PointSET() {
        this.pointSet = new SET<Point2D>();
    }

    /**
     * Is the set empty?
     */
    public boolean isEmpty() {
        return this.pointSet.isEmpty();
    }

    /**
     * Number of points in the set.
     */
    public int size() {
        return this.pointSet.size();
    }

    /**
     * Add the point to the set (if it is not already in the set).
     */
    public void insert(Point2D p) {
        this.pointSet.add(p);
    }

    /**
     * Does the set contain point p?
     */
    public boolean contains(Point2D p) {
        return this.pointSet.contains(p);
    }

    /**
     * Draw all points to standard draw.
     */
    public void draw() {
        for (Point2D point2d : this.pointSet) {
            point2d.draw();
        }
    }

    /**
     * All points that are inside the rectangle (or on the boundary).
     */
    public Iterable<Point2D> range(RectHV rect) {
        List<Point2D> list = new ArrayList<Point2D>();
        for (Point2D point2d : this.pointSet) {
            if (rect.contains(point2d)) {
                list.add(point2d);
            }
        }
        return list;
    }

    /**
     * A nearest neighbor in the set to point p; null if the set is empty.
     */
    public Point2D nearest(Point2D p) {
        if (this.isEmpty())
            return null;
        Iterator<Point2D> pointSetIterator = this.pointSet.iterator();
        Point2D nearestPoint = pointSetIterator.next();
        double shortestDistanceSquared = p.distanceSquaredTo(nearestPoint);
        while (pointSetIterator.hasNext()) {
            Point2D point2D = pointSetIterator.next();
            double distanceSquared = p.distanceSquaredTo(point2D);
            if (distanceSquared < shortestDistanceSquared) {
                nearestPoint = point2D;
                shortestDistanceSquared = distanceSquared;
            }
        }
        return nearestPoint;
    }

    /**
     * Unit testing of the methods (optional).
     */
    public static void main(String[] args) {
        SET<Point2D> myset = new SET<Point2D>();
        Point2D a = new Point2D(10, 10);
        Point2D b = new Point2D(10, 10);
        System.out.println(a == b);
        myset.add(a);
        System.out.println(myset.contains(new Point2D(10, 10)));
        // System.out.println(a.distanceSquaredTo(null));

        PointSET pointSet = new PointSET();
        System.out.println("isEmpty: " + pointSet.isEmpty());
        System.out.println("size: " + pointSet.size());
        pointSet.insert(new Point2D(0, 0));
        pointSet.insert(new Point2D(0.5, 0.5));
        System.out.println("isEmpty: " + pointSet.isEmpty());
        System.out.println("size: " + pointSet.size());
        Point2D containedPoint = new Point2D(0, 0);
        Point2D notContainedPoint = new Point2D(1, 0);
        System.out.println("contain " + containedPoint.toString() + ": " + pointSet.contains(containedPoint));
        System.out.println("contain " + notContainedPoint.toString() + ": " + pointSet.contains(notContainedPoint));

        RectHV rect = new RectHV(0, 0, 0.3, 0.3);
        System.out.println("range " + rect.toString());
        for (Point2D point2D : pointSet.range(rect)) {
            System.out.println(point2D);
        }

        RectHV rect2 = new RectHV(0.4, 0.4, 0.5, 0.5);
        System.out.println("range " + rect2.toString());
        for (Point2D point2D : pointSet.range(rect2)) {
            System.out.println(point2D);
        }

        RectHV rect3 = new RectHV(0, 0, 0.5, 0.5);
        System.out.println("range " + rect3.toString());
        for (Point2D point2D : pointSet.range(rect3)) {
            System.out.println(point2D);
        }

        RectHV rect4 = new RectHV(0.6, 0.6, 0.9, 0.9);
        System.out.println("range " + rect4.toString());
        for (Point2D point2D : pointSet.range(rect4)) {
            System.out.println(point2D);
        }

        Point2D targetPoint = new Point2D(0.1, 0.4);
        Point2D nearestPoint = pointSet.nearest(targetPoint);
        System.out.println("nearest point to " + targetPoint.toString() + ": " + nearestPoint.toString());

        pointSet.draw();
    }
}
