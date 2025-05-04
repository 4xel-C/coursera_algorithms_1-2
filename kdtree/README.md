# KD-Tree and PointSET Java Implementation

This document provides an overview and explanation of two Java classes: `KdTree` and `PointSET`, which are used for efficient 2D range and nearest neighbor searches using data structures covered in computational geometry.

## Overview

Both classes operate on 2D points (`Point2D`) in the unit square `[0,1] × [0,1]`, using the `algs4` library provided by Princeton's Algorithms course.

---

## 📌 `PointSET` Class

### Purpose

A simple implementation of a set of 2D points backed by a `SET<Point2D>`, supporting basic operations like insertions, containment check, range search, nearest neighbor, and drawing all points.

### Key Methods

- `insert(Point2D p)`  
  Adds the point to the set if it's not already in it.

- `contains(Point2D p)`  
  Checks if the point exists in the set.

- `draw()`  
  Draws all points to the standard drawing canvas.

- `range(RectHV rect)`  
  Returns all points within the given rectangle.

- `nearest(Point2D p)`  
  Returns the point in the set closest to the query point.

### Notes

- Brute-force approach for range and nearest neighbor search.
- Ideal for small datasets or as a baseline for testing the `KdTree`.

---

## 🌲 `KdTree` Class

### Purpose

An efficient structure for storing 2D points, supporting logarithmic-time insertion, containment check, range search, and nearest neighbor search using a 2D tree.

### Internal Structure

- Uses a private `Node` class to build a binary tree alternating between vertical (x-coordinate) and horizontal (y-coordinate) splits.

- Each `Node` contains:
  - Its `Point2D` position.
  - References to left and right child nodes.
  - The splitting level (0 for vertical, 1 for horizontal).
  - A reference to its parent (used for drawing lines).

### Key Methods

- `insert(Point2D p)`  
  Recursively inserts a point, alternating axis based on level.

- `contains(Point2D p)`  
  Checks if the point exists in the kd-tree.

- `draw()`  
  Recursively draws the tree structure:
  - Points in black.
  - Vertical division lines in red.
  - Horizontal division lines in blue.

- `range(RectHV rect)`  
  Recursively finds all points inside the given rectangle by pruning branches that do not intersect.

- `nearest(Point2D p)`  
  Recursively searches for the closest point to `p`, using pruning based on distance to sub-rectangles.

### Optimization Notes

- Sub-rectangles (`RectHV`) are used to prune unnecessary branches in `range()` and `nearest()`.
- Always explores the subtree on the same side of the splitting line first to improve efficiency.

---

## ✅ Usage Tips

- Use `PointSET` for small data or testing correctness of `KdTree`.
- Use `KdTree` for large datasets to benefit from log-time queries.
- Make sure to handle `null` inputs as both classes throw `IllegalArgumentException` if such inputs are passed.

---

## 🧪 Dependencies

- `edu.princeton.cs.algs4.Point2D`
- `edu.princeton.cs.algs4.RectHV`
- `edu.princeton.cs.algs4.StdDraw`
- `edu.princeton.cs.algs4.SET`

Make sure to include the [algs4.jar](https://algs4.cs.princeton.edu/code/algs4.jar) in your classpath when compiling and running these classes.

