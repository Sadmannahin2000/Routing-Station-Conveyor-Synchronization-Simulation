import java.util.concurrent.locks.ReentrantLock;

/*
 Name:   Sadman Nahin
 Course: CNT 4714 Summer 2025
 Assignment title: Project 2 – Multi-threaded programming in Java
 Date:   June 15, 2025
 Class:  CNT 4714
*/
public class Station implements Runnable {
    private final int id, workload, stationCount;
    private final ReentrantLock inLock, outLock;
    private final int inId, outId;

    public Station(int id,
                   int workload,
                   ReentrantLock inLock, int inId,
                   ReentrantLock outLock, int outId,
                   int stationCount) {
        this.id           = id;
        this.workload     = workload;
        this.inLock       = inLock;
        this.outLock      = outLock;
        this.inId         = inId;
        this.outId        = outId;
        this.stationCount = stationCount;
    }

    @Override
    public void run() {
        // 1) Input conveyor assignment
        System.out.printf(
                "Routing Station S%d: Input conveyor assigned to conveyor number C%d.%n",
                id, inId
        );

        // 2) Output conveyor assignment
        System.out.printf(
                "Routing Station S%d: Output conveyor assigned to conveyor number C%d.%n",
                id, outId
        );

        for (int i = 0; i < workload; i++) {
            // 10) Hard at work
            System.out.printf(
                    "* * Routing Station S%d: * * CURRENTLY HARD AT WORK MOVING PACKAGES. * *%n",
                    id
            );

            // lock ordering to avoid deadlock
            ReentrantLock first  = inId < outId ? inLock : outLock;
            ReentrantLock second = (first == inLock ? outLock : inLock);

            boolean done = false;
            while (!done) {
                first.lock();
                try {
                    // 4) Currently holds input
                    System.out.printf(
                            "Routing Station S%d:  Currently holds lock on input conveyor C%d.%n",
                            id, inId
                    );

                    if (!second.tryLock()) {
                        // 8) Unable to lock output → release input
                        int holder = ((outId % stationCount) + 1);
                        System.out.printf(
                                "Routing Station S%d: UNABLE TO LOCK OUTPUT CONVEYOR C%d.   " +
                                        "SYNCHRONIZATION ISSUE: Station S%d currently holds the lock on " +
                                        "output conveyor C%d – Station S%d releasing lock on input conveyor C%d.%n",
                                id, outId, holder, outId, id, inId
                        );
                        first.unlock();
                        sleep(100);
                        continue;
                    }

                    try {
                        // 5) Currently holds output
                        System.out.printf(
                                "Routing Station S%d:  Currently holds lock on output conveyor C%d.%n",
                                id, outId
                        );

                        // simulate moving
                        sleep(200 + (int)(Math.random() * 800));

                        // 11) Workflow complete
                        System.out.printf(
                                "Routing Station S%d: Package group completed - %d package groups remaining to move.%n",
                                id, workload - i - 1
                        );

                        done = true;
                    } finally {
                        // 7) Unlock output
                        outLock.unlock();
                        System.out.printf(
                                "Routing Station S%d: Unlocks/releases output conveyor C%d.%n",
                                id, outId
                        );
                    }

                } finally {
                    // 6) Unlock input
                    inLock.unlock();
                    System.out.printf(
                            "Routing Station S%d: Unlocks/releases input conveyor C%d.%n",
                            id, inId
                    );
                }
            }
        }

        // 9) Going offline
        System.out.printf(
                "# # Routing Station S%d: going offline – work completed!   BYE!  # #%n",
                id
        );
    }

    private void sleep(int ms) {
        try { Thread.sleep(ms); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
