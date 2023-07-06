setup
https://algs4.cs.princeton.edu/code/

scale window
```python
    StdDraw.enableDoubleBuffering();
    StdDraw.setPenRadius(0.015);
    StdDraw.setXscale(0, 32768);
    StdDraw.setYscale(0, 32768);
    new Point2D(32768 / 2, 32768 / 2).draw();
    // new Point2D(0.5, 0.5).draw();
    StdDraw.show();
```