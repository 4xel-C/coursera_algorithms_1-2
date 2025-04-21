
# FastCollinearPoints

This project is an implementation of the **Fast Collinear Points** algorithm as part of the *Algorithms, Part I* course by Princeton University on Coursera. It efficiently detects all line segments containing 4 or more collinear points in a given set of 2D points.

---

## 📁 Files Overview

### 1. `Point.java`
- Represents an immutable data type for 2D points.
- Implements:
  - `compareTo(Point that)` for lexicographic ordering.
  - `slopeTo(Point that)` to compute slope between points.
  - `slopeOrder()` to compare two points based on the slopes they make with the invoking point.
- Includes visualization methods using `StdDraw`.

### 2. `LineSegment.java`
- Represents a line segment connecting two `Point` objects.
- Immutable and used to encapsulate a segment of collinear points.
- Includes a `draw()` method and a human-readable `toString()` method.
- Hashing is intentionally unsupported as per course restrictions.

### 3. `FastCollinearPoints.java`
- Core class that implements the efficient detection of all line segments containing 4 or more collinear points.
- Uses sorting based on slopes and comparisons to avoid duplicates.
- Validates input for nulls and duplicate points.
- Returns results through:
  - `numberOfSegments()`
  - `segments()`

---

## 🚀 Usage

1. Compile:
   ```bash
   javac Point.java LineSegment.java FastCollinearPoints.java
   ```

2. Run with your own `main` class or use test clients provided by the course.

3. Sample dependencies (used for drawing):
   - `edu.princeton.cs.algs4.StdDraw`
   - `edu.princeton.cs.algs4.In`

---

## ✅ Key Features

- Input validation for null entries and duplicate points.
- Comparator-based detection of collinear segments.
- Prevents duplicates by only adding segments if the reference point is the smallest.

---

## 🧪 Testing

`Point.java` includes a `main` method with unit tests covering:
- `slopeTo()`
- `compareTo()`
- `slopeOrder()`

---

## 📚 References

- [Algorithms, Part I - Princeton University (Coursera)](https://www.coursera.org/learn/algorithms-part1)

This implementation is aligned with the conventions and requirements of the course.

---

© 2025 - Developed as part of Coursera coursework.
