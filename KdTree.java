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
        if (this.isEmpty()) {
            this.root = new Node();
            this.root.point = p;
            this.size++;
            return;
        }
        Node newNode = insertDfs(p, this.root, 0);
        newNode.point = p;
        this.size++;
    }

    private Node insertDfs(Point2D p, Node node, int depth) {
        boolean isGoLeftByX = depth % 2 == 0 && p.x() < node.point.x();
        boolean isGoLeftByY = depth % 2 != 0 && p.y() < node.point.y();
        boolean isGoLeft = isGoLeftByX || isGoLeftByY;
        if (isGoLeft) {
            if (node.left == null) {
                node.left = new Node();
                return node.left;
            } else {
                return insertDfs(p, node.left, depth + 1);
            }
        } else {
            if (node.right == null) {
                node.right = new Node();
                return node.right;
            } else {
                return insertDfs(p, node.right, depth + 1);
            }
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
        ArrayList<Point2D> list = new ArrayList<Point2D>();
        RectHV nodeRect = new RectHV(0, 0, 1, 1);
        this.addToRange(rect, this.root, nodeRect, 0, list);
        return list;
    }

    private void addToRange(RectHV rect, Node node, RectHV nodeRect, int depth, ArrayList<Point2D> list) {
        if (node == null)
            return;
        if (!rect.intersects(nodeRect))
            return;
        if (rect.contains(node.point))
            list.add(node.point);
        RectHV nodeRectLeft = new RectHV(
                nodeRect.xmin(),
                nodeRect.ymin(),
                depth % 2 == 0 ? node.point.x() : nodeRect.xmax(),
                depth % 2 == 0 ? nodeRect.ymax() : node.point.y());
        this.addToRange(rect, node.left, nodeRectLeft, depth + 1, list);
        RectHV nodeRectRight = new RectHV(
                depth % 2 == 0 ? node.point.x() : nodeRect.xmin(),
                depth % 2 == 0 ? nodeRect.ymin() : node.point.y(),
                nodeRect.xmax(),
                nodeRect.ymax());
        this.addToRange(rect, node.right, nodeRectRight, depth + 1, list);
    }

    /**
     * A nearest neighbor in the set to point p; null if the set is empty.
     */
    public Point2D nearest(Point2D p) {
        return new Point2D(0, 0);
    }

    /**
     * Unit testing of the methods (optional).
     */
    public static void main(String[] args) {
        KdTree kdTree = new KdTree();
        kdTree.insert(new Point2D(0.7, 0.2));
        kdTree.insert(new Point2D(0.5, 0.4));
        kdTree.insert(new Point2D(0.2, 0.3));
        kdTree.insert(new Point2D(0.4, 0.7));
        kdTree.insert(new Point2D(0.9, 0.6));
        kdTree.draw();
        System.out.println(kdTree.size());
        RectHV searchRect = new RectHV(0, 0, 0.35, 0.35);
        searchRect.draw();
        System.out.println(kdTree.range(searchRect));
        System.out.println(kdTree.contains(new Point2D(0.9, 0.6)));
    }
}
