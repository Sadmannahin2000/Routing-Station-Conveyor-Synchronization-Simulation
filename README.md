# ⚙️ Routing Station Conveyor Synchronization Simulation

A multithreaded Java simulation that models routing stations connected by conveyors.  
Each station represents a worker thread that processes package groups while acquiring and releasing shared conveyor locks to prevent synchronization conflicts.

---

## 🧠 Overview
The system simulates **multiple stations (S1–S4)** processing packages concurrently.  
Each station:
- Reads its workload configuration from `config.txt`
- Locks input and output conveyors before processing
- Logs its activity (work, waiting, locking/unlocking, completion)
- Demonstrates **thread synchronization**, **deadlock avoidance**, and **resource sharing**

The simulation output is written to the console and saved as `sample_execution.txt`.
