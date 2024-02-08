package com.example.classes;

public abstract sealed class Shape
        permits Circle, Rectangle, Square, WeirdShape {  }

final class Circle extends Shape { }

sealed class Rectangle extends Shape
        permits TransparentRectangle, FilledRectangle { }
final class TransparentRectangle extends Rectangle { }
final class FilledRectangle extends Rectangle { }

final class Square extends Shape { }

non-sealed class WeirdShape extends Shape { }
