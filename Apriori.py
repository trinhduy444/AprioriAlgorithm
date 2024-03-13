import time
import pylab
import numpy as np
from memory_profiler import profile


@profile
def max(A,B):
    if(A>B):
        return A
    else: return B
if __name__ == '__main__':
    A= 10
    B= 5
    start = time.time()
    max(A,B)
    stop = time.time()
    print(start)
    print(stop-start)