import java.util.ArrayList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class KdTree {
    private class Node {
        Point2D point;
        Node left;
        Node right;
    }

    private Node root;
    private int size = 0;
    private RectHV rootRect = new RectHV(0, 0, 1, 1);

    /**
     * Construct an empty set of points.
     */
    public KdTree() {
        this.root = null;
    }

    /**
     * Is the set empty?
     */
    public boolean isEmpty() {
        return this.root == null;
    }

    /**
     * Number of points in the set.
     */
    public int size() {
        return this.size;
    }

    /**
     * Add the point to the set (if it is not already in the set).
     */
    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (this.isEmpty()) {
            this.root = new Node();
            this.root.point = p;
            this.size++;
            return;
        }
        Node newNode = insertDfs(p, this.root, 0);
        if (newNode != null) {
            newNode.point = p;
            this.size++;
        }
    }

    private Node insertDfs(Point2D p, Node node, int depth) {
        boolean isGoLeftByX = depth % 2 == 0 && p.x() < node.point.x();
        boolean isGoLeftByY = depth % 2 != 0 && p.y() < node.point.y();
        boolean isGoLeft = isGoLeftByX || isGoLeftByY;

        if (node.point.x() == p.x() && node.point.y() == p.y())
            return null;
        if (isGoLeft) {
            if (node.left != null)
                return insertDfs(p, node.left, depth + 1);
            node.left = new Node();
            return node.left;
        } else {
            if (node.right != null)
                return insertDfs(p, node.right, depth + 1);
            node.right = new Node();
            return node.right;
        }
    }

    /**
     * Does the set contain point p?
     */
    public boolean contains(Point2D p) {
        return this.containsDfs(p, this.root, 0);
    }

    private boolean containsDfs(Point2D p, Node node, int depth) {
        if (node == null)
            return false;
        if (node.point.equals(p))
            return true;
        boolean isGoLeftByX = depth % 2 == 0 && p.x() < node.point.x();
        boolean isGoLeftByY = depth % 2 != 0 && p.y() < node.point.y();
        boolean isGoLeft = isGoLeftByX || isGoLeftByY;
        if (isGoLeft)
            return this.containsDfs(p, node.left, depth + 1);
        return this.containsDfs(p, node.right, depth + 1);
    }

    /**
     * Draw all points to standard draw.
     */
    public void draw() {
        this.drawDfs(this.root);
    }

    private void drawDfs(Node node) {
        if (node == null) {
            return;
        }
        drawDfs(node.left);
        System.out.println(node.point);
        node.point.draw();
        System.out.println();
        drawDfs(node.right);
    }

    /**
     * All points that are inside the rectangle (or on the boundary).
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();
        ArrayList<Point2D> list = new ArrayList<Point2D>();
        this.addToRange(rect, this.root, this.rootRect, 0, list);
        return list;
    }

    private void addToRange(RectHV rect, Node node, RectHV nodeRect, int depth, ArrayList<Point2D> list) {
        if (node == null)
            return;
        if (!rect.intersects(nodeRect))
            return;
        if (rect.contains(node.point))
            list.add(node.point);
        this.addToRange(rect, node.left, this.getNodeRectGoLeft(node, nodeRect, depth), depth + 1, list);
        this.addToRange(rect, node.right, this.getNodeRectGoRight(node, nodeRect, depth), depth + 1, list);
    }

    /**
     * A nearest neighbor in the set to point p; null if the set is empty.
     */
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (this.root == null)
            return null;
        Point2D[] nearestList = { this.root.point };
        this.nearestDfs(p, this.root, this.rootRect, 0, nearestList);
        return nearestList[0];
    }

    private void nearestDfs(Point2D p, Node node, RectHV nodeRect, int depth, Point2D[] nearestHM) {
        if (node == null)
            return;
        Point2D nearestPoint = nearestHM[0];
        double nearestDistanceSquared = nearestPoint.distanceSquaredTo(p);
        if (nearestDistanceSquared < nodeRect.distanceSquaredTo(p))
            return;
        if (nearestDistanceSquared > node.point.distanceSquaredTo(p))
            nearestHM[0] = node.point;
        this.nearestDfs(p, node.left, this.getNodeRectGoLeft(node, nodeRect, depth), depth + 1, nearestHM);
        this.nearestDfs(p, node.right, this.getNodeRectGoRight(node, nodeRect, depth), depth + 1, nearestHM);
    }

    private RectHV getNodeRectGoLeft(Node node, RectHV nodeRect, int depth) {
        return new RectHV(
                nodeRect.xmin(),
                nodeRect.ymin(),
                depth % 2 == 0 ? node.point.x() : nodeRect.xmax(),
                depth % 2 == 0 ? nodeRect.ymax() : node.point.y());
    }

    private RectHV getNodeRectGoRight(Node node, RectHV nodeRect, int depth) {
        return new RectHV(
                depth % 2 == 0 ? node.point.x() : nodeRect.xmin(),
                depth % 2 == 0 ? nodeRect.ymin() : node.point.y(),
                nodeRect.xmax(),
                nodeRect.ymax());
    }

    /**
     * Unit testing of the methods (optional).
     */
    public static void main(String[] args) {
        KdTree kdTree = new KdTree();
        kdTree.insert(new Point2D(0.75, 0.25));
        kdTree.insert(new Point2D(1.0, 0.0));
        kdTree.insert(new Point2D(1.0, 0.5));
        kdTree.insert(new Point2D(0.75, 1.0));
        kdTree.insert(new Point2D(0.5, 1.0));
        kdTree.insert(new Point2D(1.0, 0.0));
        System.out.println(kdTree.size);
    }
}
