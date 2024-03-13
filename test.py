import time
import pylab
import numpy as np
from memory_profiler import profile

def fac(n):
    if n == 0:
        return 1
    result = 1
    for i in range(1, n):
        result *= i
    return result
def measure_time(func, N):
    runtime = []
    for n in N:
        start = time.time()
        print(func(n))
        stop = time.time()
        runtime.append(stop-start)
    return runtime
N = list(range(100))
n = 5
rtime = measure_time(fac, N)
rtime2 = [t*1.5 for t in rtime]
pylab.plot(N, rtime, N, rtime2)
pylab.legend(['1', '2'])
pylab.show()

