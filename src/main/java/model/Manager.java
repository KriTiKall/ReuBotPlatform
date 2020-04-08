package model;

import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Manager {
    private ExecutorService executor = Executors.newFixedThreadPool(3);
}
