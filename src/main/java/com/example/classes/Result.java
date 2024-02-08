package com.example.classes;

public sealed abstract class Result {

    final class Okay extends Result {}

    final class Error extends Result { String message; }

}
