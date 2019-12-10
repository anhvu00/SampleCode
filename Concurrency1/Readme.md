Goal
- Demo java 8 concurrency

Quick start
- Download this maven project, mvn install
- Run MainApp as application

Note
- The wrapper class to show results are in order
- The result Future list persists after ExecutorService is shut down
- Use StringBuilder, more efficient in thread/task
- ExecutorService requires some overhead. Keep using it and sub sequential executions might be faster than creating the service every time.
- Use Callable because it can return Object while Runnable does not.

TODO
- Experiment with different ThreadPool and number of threads
- ConcurrentHashMap might be even faster
